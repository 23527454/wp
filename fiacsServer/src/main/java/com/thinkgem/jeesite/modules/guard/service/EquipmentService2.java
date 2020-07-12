/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.DownloadPerson;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.guard.dao.CarDao;
import com.thinkgem.jeesite.modules.guard.dao.EquipmentDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;

/**
 * 设备信息Service
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class EquipmentService2 extends CrudService<EquipmentDao, Equipment> {
	@Autowired
	private DownloadCarService downloadCarService;
	@Autowired
	private DownloadPersonService downloadPersonService;
	@Autowired
	private DownloadMoneyBoxService downloadMoneyBoxService;
	@Autowired
	private @Lazy StaffService staffService;
	@Autowired
	private @Lazy CarService carService;
	@Autowired
	private @Lazy MoneyBoxService moneyBoxService;
	@Autowired
	private @Lazy OfficeService officeService;

	public Equipment get(String id) {
		Equipment equipment = super.get(id);
		if(null != equipment){
			equipment.setOffice(officeService.get(equipment.getOffice().getId()));
		}
		return equipment;
	}
	
	public Equipment getByOfficeId(String officeId){
		Equipment entity = new Equipment();
		entity.setOffice(new Office(officeId));
		return super.findOne(entity);
	}

	public List<Equipment> findList(Equipment equipment) {
		for (Role r : equipment.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				equipment.getSqlMap().put("dsf", dataScopeFilter(equipment.getCurrentUser(), "o3", "u"));
			} else {
				equipment.getSqlMap().put("dsf", dataScopeFilter(equipment.getCurrentUser(), "o2", "u"));
			}
		}
		return super.findList(equipment);
	}

	public Page<Equipment> findPage(Page<Equipment> page, Equipment equipment) {
		for (Role r : equipment.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				equipment.getSqlMap().put("dsf", dataScopeFilter(equipment.getCurrentUser(), "o3", "u"));
			} else {
				equipment.getSqlMap().put("dsf", dataScopeFilter(equipment.getCurrentUser(), "o2", "u"));
			}
		}
		
		return super.findPage(page, equipment);
	}

	@Transactional(readOnly = false)
	public void save(Equipment equipment) {
		boolean messageType;
		if (equipment.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		super.save(equipment);
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "添加设备:["+equipment.getOffice().getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改设备:["+equipment.getOffice().getId()+"]");
		}
		
		this.insentDownload(equipment);
	}

	@Transactional(readOnly = false)
	public void insentDownload(Equipment equ) {
		// 为了防止是保存之后生成同步 通过ID再进行获取一次数据
		Equipment equipment = get(equ.getId());
		//同步人员
		insentDownloadStaff(equipment);

		// 同区域的车辆才可同步
		insentDownloadCar(equipment);

		//款箱同步
		_insentDownloadMoneyBox(equipment);
	}

	//同步人员
	private void insentDownloadStaff(Equipment equipment) {
		Area area = equipment.getArea();
		// 同区域的人员才能插入同步记录
		// 人员分专员和押款员 专员所属网点需要与该网点相同
		Staff staffRequest = new Staff();
		staffRequest.setArea(area);
		
		List<Staff> staffList = staffService.findList(staffRequest);
		for (Staff staff : staffList) {
			if(!staff.isApproved()){
				continue;
			}
			String staff_id = staff.getId();
			
			if (Staff.STAFF_TYPE_SUPERCARGO.equals(staff.getStaffType())  || Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType()) ) {
				// 判断是押款员就直接插入
				DownloadPerson downloadPerson = new DownloadPerson();
				downloadPerson.setPersonId(staff_id);
				downloadPerson.setEquipId(equipment.getId());
				downloadPerson.setIsDownload("0");
				downloadPerson.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
				downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
				downloadPersonService.save(downloadPerson);
			} else {//同步专员
				//判断是否是此网点的
				if (staff.getOffice().getId().equals(equipment.getOffice().getId())) {
					List<Office> officeList = officeService.findParentIDList(staff.getOffice());
					for (Office office : officeList) {
						Equipment staffEquipment= this.getByOfficeId(office.getId());
						if(null == staffEquipment){
							continue;
						}
						DownloadPerson downloadPerson = new DownloadPerson();
						downloadPerson.setPersonId(staff.getId());
						downloadPerson.setEquipId(staffEquipment.getId());
						downloadPerson.setIsDownload("0");
						downloadPerson.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
						downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
						downloadPersonService.save(downloadPerson);
					}
				}
			}
		}
	}

	// 同步车辆
	private void insentDownloadCar(Equipment equipment) {
		Area area = equipment.getArea();
		Car car = new Car();
		car.setArea(area);//同区域的车辆才可同步
		List<Car> carList = carService.findList(car);
		for (int i = 0; i < carList.size(); i++) {
			DownloadCar downloadCar = new DownloadCar();
			downloadCar.setCarId(carList.get(i).getId());
			downloadCar.setEquipId(equipment.getId());
			downloadCar.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadCar.setIsDownload("0");
			downloadCar.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			downloadCarService.save(downloadCar);
		}
	}

	//款箱同步
	private void _insentDownloadMoneyBox(Equipment downloadEquipment) {
		Area area = downloadEquipment.getArea();
		
		if(Equipment.SITE_TYPE_ZXJK.equals(downloadEquipment.getSiteType())){
			MoneyBox zxjkMoneyBoxRequest = new MoneyBox();
			zxjkMoneyBoxRequest.setOffice(downloadEquipment.getOffice());
			List<MoneyBox> zxjkMoneyBoxList = moneyBoxService.findList(zxjkMoneyBoxRequest);
			//同步自己的款箱到所有网点
			Equipment equipmentRequest = new Equipment();
			equipmentRequest.setArea(area);
			List<Equipment> equipments = this.findList(equipmentRequest);
			for (Equipment equipment : equipments) {
				for (MoneyBox zxjkMoneyBox : zxjkMoneyBoxList) {
					moneyBoxService.insentDownload2Office(zxjkMoneyBox, equipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
				}
			}
		}else{
			//款箱同步
			MoneyBox moneyBoxRequest = new MoneyBox();
			moneyBoxRequest.setOffice(downloadEquipment.getOffice());
			//同步当前网点的款箱到到当前网点
			List<MoneyBox> moneyBoxList = moneyBoxService.findList(moneyBoxRequest);
			for (MoneyBox moneyBox : moneyBoxList) {
				moneyBoxService.insentDownload2Office(moneyBox, downloadEquipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
			}
			//同步中心金库的款箱到当前网点
			Equipment equipmentRequest = new Equipment();
			equipmentRequest.setArea(area);
			equipmentRequest.setSiteType(Equipment.SITE_TYPE_ZXJK);
			List<Equipment> zxjkEquipments = this.findList(equipmentRequest);
			for (Equipment zxjyEquipment : zxjkEquipments) {
				MoneyBox zxjkMoneyBoxRequest = new MoneyBox();
				zxjkMoneyBoxRequest.setOffice(zxjyEquipment.getOffice());
				List<MoneyBox> zxjkMoneyBoxList = moneyBoxService.findList(zxjkMoneyBoxRequest);
				for (MoneyBox zxjkMoneyBox : zxjkMoneyBoxList) {
					moneyBoxService.insentDownload2Office(zxjkMoneyBox, downloadEquipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(Equipment equipment) {
		super.delete(equipment);
		LogUtils.saveLog(Servlets.getRequest(), "删除设备:["+equipment.getId()+"]");
	}

	
	public int countByIp(String id, String ip) {
		if(StringUtils.isBlank(ip)) {
			return 0;
		}
		Equipment e = new Equipment();
		e.setId(id);
		e.setIp(ip);
		return super.countByColumnExceptSelf(e);
	}

	public int countBySerialNum(String id, String serialNum) {
		if(StringUtils.isBlank(serialNum)) {
			return 0;
		}
		Equipment e = new Equipment();
		e.setId(id);
		e.setSerialNum(serialNum);
		return super.countByColumnExceptSelf(e);
	}
}