/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.modules.guard.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.dao.EquipmentDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;

/**
 * 设备信息Service
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class EquipmentService extends CrudService<EquipmentDao, Equipment> {
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
	@Autowired
	private @Lazy TbAccessParametersService tbAccessParametersService;
	@Autowired
	private @Lazy TbDoorTimeZoneService tbDoorTimeZoneService;
	@Autowired
	private @Lazy TbDownloadAccessParametersService tbDownLoadAccessParametersService;
	@Autowired
	private @Lazy TbDownloadDoorTimeZoneService tbDownLoadDoorTimeZoneService;
	@Autowired
	private  @Lazy  DispatchPersonInfoService dispatchPersonInfoService;
	@Transactional(readOnly=false)
	public Equipment get(String id) {
		Equipment equipment = super.get(id);
		if(null != equipment){
			equipment.setOffice(officeService.get(equipment.getOffice().getId()));
			
			TbAccessParameters tbParam = new TbAccessParameters();
			tbParam.setEquipmentId(id);
			tbParam.setEquipSn(equipment.getSerialNum());
			
			List<TbAccessParameters> pList = tbAccessParametersService.findList(tbParam);
			if(pList!=null&&pList.size()>0) {
				if(equipment.getAccessType()==0) {
					if(pList.size()>2) {
						/*for(TbAccessParameters tt : pList) {
							if(tt.getDoorPos()==3||tt.getDoorPos()==4) {
								pList.remove(tt);
								tbAccessParametersService.delete(tt);
							}
						}*/
						for(int i=0;i<pList.size();i++) {
							TbAccessParameters tt = pList.get(i);
							if(tt.getDoorPos()==3||tt.getDoorPos()==4) {
								pList.remove(i);
								tbAccessParametersService.delete(tt);
							}
						}
					}
				}else {
					if(pList.size()<3) {
						for(int i=3;i<5;i++) {
							TbAccessParameters tbp = new TbAccessParameters();
							tbp.setDoorPos(i);
							pList.add(tbp);
						}
					}
				}
				equipment.setAccessParametersList(pList);
			}else {
				pList = new ArrayList<TbAccessParameters>();
				for(int i=1;i<(equipment.getAccessType()==0?3:5);i++) {
					TbAccessParameters tbp = new TbAccessParameters();
					tbp.setDoorPos(i);
					pList.add(tbp);
				}
				equipment.setAccessParametersList(pList);
			}
			
			TbDoorTimeZone tbZone = new TbDoorTimeZone();
			tbZone.setEquipmentId(id);
			tbZone.setEquipSn(equipment.getSerialNum());
			equipment.setDoorTimeZonesList(tbDoorTimeZoneService.findList(tbZone));
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
				/*equipment.getSqlMap().put("dsf", dataScopeFilter(equipment.getCurrentUser(), "o3", "u"));*/
				equipment.getSqlMap().put("dsf", dataScopeFilter(equipment.getCurrentUser(), "o2", "u"));
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
			this.insentDownload(equipment);
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改设备:["+equipment.getOffice().getId()+"]");
		}
		
	}
	
	@Transactional(readOnly = false)
	public void saveSetting(Equipment equipment) {
		for(TbAccessParameters tbp : equipment.getAccessParametersList()) {
			if("0".equals(tbp.getDelFlag())) {
				tbAccessParametersService.save(tbp);
				
				TbDownloadAccessParameters tbd = new TbDownloadAccessParameters();
				tbd.setParametersId(tbp.getId());
				tbd.setEquipmentId(equipment.getId());
				tbd.setIsDownload("0");
				tbd.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
				tbd.setRegisterTime(DateUtils.formatDateTime(new Date()));
				tbDownLoadAccessParametersService.save(tbd);
			}else {
				tbAccessParametersService.delete(tbp);
				TbDownloadAccessParameters tbd = new TbDownloadAccessParameters();
				tbd.setParametersId(tbp.getId());
				tbd.setEquipmentId(equipment.getId());
				tbd.setIsDownload("0");
				tbd.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_DEL);
				tbd.setRegisterTime(DateUtils.formatDateTime(new Date()));
				tbDownLoadAccessParametersService.save(tbd);
			}
		}
		
		for(TbDoorTimeZone ttz : equipment.getDoorTimeZonesList()) {
			if("0".equals(ttz.getDelFlag())){
				tbDoorTimeZoneService.save(ttz);
				
				TbDownloadDoorTimeZone tbd = new TbDownloadDoorTimeZone();
				tbd.setTimeZoneId(ttz.getId());
				tbd.setEquipmentId(equipment.getId());
				tbd.setIsDownload("0");
				tbd.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
				tbd.setRegisterTime(DateUtils.formatDateTime(new Date()));
				tbDownLoadDoorTimeZoneService.save(tbd);
			}else {
				tbDoorTimeZoneService.delete(ttz);
				
				TbDownloadDoorTimeZone tbd = new TbDownloadDoorTimeZone();
				tbd.setTimeZoneId(ttz.getId());
				tbd.setEquipmentId(equipment.getId());
				tbd.setIsDownload("0");
				tbd.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_DEL);
				tbd.setRegisterTime(DateUtils.formatDateTime(new Date()));
				tbDownLoadDoorTimeZoneService.save(tbd);
			}
		}
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
			
			if (Staff.STAFF_TYPE_SUPERCARGO.equals(staff.getStaffType())) {
				// 判断是押款员就直接插入
				DownloadPerson downloadPerson = new DownloadPerson();
				downloadPerson.setPersonId(staff_id);
				downloadPerson.setEquipId(equipment.getId());
				downloadPerson.setIsDownload("0");
				downloadPerson.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
				downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
				if(downloadPersonService.countByEntity(downloadPerson)==0){
					downloadPersonService.save(downloadPerson);
				}
				//如果维保员  则派遣到此网点的人员重新同步
			}else if(Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())){
				/*DispatchPersonInfo query = new DispatchPersonInfo();
				query.setOfficeId(equipment.getOffice().getId());
				List<DispatchPersonInfo> dispatchPersonInfoList = dispatchPersonInfoService.findList(query);
				for(DispatchPersonInfo dispatchPersonInfo : dispatchPersonInfoList){
					DownloadPerson downloadPerson = new DownloadPerson();
					downloadPerson.setPersonId(dispatchPersonInfo.getStaffId());
					downloadPerson.setEquipId(equipment.getId());
					downloadPerson.setIsDownload("0");
					downloadPerson.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
					downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
					if(downloadPersonService.countByEntity(downloadPerson)==0){
						downloadPersonService.save(downloadPerson);
					}
				}*/
				continue;
			} else {
				//判断是否是此网点的
				if (staff.getOffice().getId().equals(equipment.getOffice().getId())) {
					List<Office> officeList = officeService.findParentIDList(staff.getOffice());
					for (Office office : officeList) {
						/*if("4".equals(office.getType())) {
							continue;
						}*/
						if(!office.getId().equals(equipment.getOffice().getId())) {
							continue;
						}
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
						if(downloadPersonService.countByEntity(downloadPerson)==0){
							downloadPersonService.save(downloadPerson);
						}
					}
				}
				if (staff.getOffice().getId().equals(equipment.getOffice().getParent().getId())) {
					List<Office> officeList = officeService.findParentIDList(staff.getOffice());
					for (Office office : officeList) {
						/*if("4".equals(office.getType())) {
							continue;
						}*/
						if(!office.getId().equals(equipment.getOffice().getId())) {
							continue;
						}
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
						if(downloadPersonService.countByEntity(downloadPerson)==0){
							downloadPersonService.save(downloadPerson);
						}
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
			if(downloadCarService.countByEntity(downloadCar)==0){
				downloadCarService.save(downloadCar);
			}
		}
	}

	//款箱同步
	private void _insentDownloadMoneyBox(Equipment downloadEquipment) {
		Area area = downloadEquipment.getArea();
		
		/*if(Equipment.SITE_TYPE_ZXJK.equals(downloadEquipment.getSiteType())){
			MoneyBox zxjkMoneyBoxRequest = new MoneyBox();
			zxjkMoneyBoxRequest.setOffice(downloadEquipment.getOffice());
			List<MoneyBox> zxjkMoneyBoxList = moneyBoxService.findList(zxjkMoneyBoxRequest);
			//同步自己的款箱到所有网点
			Equipment equipmentRequest = new Equipment();
			equipmentRequest.setArea(area);
			List<Equipment> equipments = this.findList(equipmentRequest);
			Set<Equipment> equipments2 = new HashSet<Equipment>();
			for (Equipment equipment : equipments) {
				equipments2.add(equipment);
			}
			for (Equipment equipment : equipments) {
				if(equipment.getControlPos().equals(downloadEquipment.getControlPos())) {
					for (MoneyBox zxjkMoneyBox : zxjkMoneyBoxList) {
						moneyBoxService.insentDownload2Office(zxjkMoneyBox, equipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
					}
				}
			}*/
		if(Equipment.SITE_TYPE_ZXJK.equals(downloadEquipment.getSiteType())){
			MoneyBox zxjkMoneyBoxRequest = new MoneyBox();
			zxjkMoneyBoxRequest.setOffice(downloadEquipment.getOffice());
			List<MoneyBox> zxjkMoneyBoxList = moneyBoxService.findList(zxjkMoneyBoxRequest);
			//同步自己的款箱到所有网点
			Equipment equipmentRequest = new Equipment();
			equipmentRequest.setArea(area);
			List<Equipment> equipments = this.findList(equipmentRequest);
			for (Equipment equipment : equipments) {
				if("1".equals(equipment.getSiteType())) {
					for (MoneyBox zxjkMoneyBox : zxjkMoneyBoxList) {
						moneyBoxService.insentDownload2Office(zxjkMoneyBox, equipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
					}
				}
			}
		}else{
			//款箱同步
			MoneyBox moneyBoxRequest = new MoneyBox();
			moneyBoxRequest.setOffice(downloadEquipment.getOffice());
			//同步当前网点的款箱到到当前网点
			List<MoneyBox> moneyBoxList = moneyBoxService.findList(moneyBoxRequest);
			for (MoneyBox moneyBox : moneyBoxList) {
				if(moneyBox.getOffice().getId().equals(downloadEquipment.getOffice().getId())) {
					moneyBoxService.insentDownload2Office(moneyBox, downloadEquipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
				}
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
					if(zxjkMoneyBox.getOffice().getId().equals(zxjyEquipment.getOffice().getId())) {
						moneyBoxService.insentDownload2Office(zxjkMoneyBox, downloadEquipment.getOffice().getId(), DownloadEntity.DOWNLOAD_TYPE_ADD);
					}
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
	
	@Transactional(readOnly=false)
	public void parametersDownload(Map<String, Object> params) {
		String type = (String)params.get("type");
		//门禁参数
		if("parameters".equals(type)) {
			TbAccessParameters tbp = new TbAccessParameters();
			tbp.setAlarmTime(Integer.valueOf((String)params.get("alarmTime")));
			tbp.setCenterPermit(Integer.valueOf((String)params.get("centerPermit")));
			tbp.setCombinationNumber(Integer.valueOf((String)params.get("combinationNumber")));
			tbp.setDelayCloseTime(Integer.valueOf((String)params.get("delayCloseTime")));
			tbp.setDoorPos(Integer.valueOf((String)params.get("doorPos")));
			tbp.setEquipmentId((String)params.get("equipmentId"));
			tbp.setEquipSn((String)params.get("equipSn"));
			tbp.setRelayActionTime(Integer.valueOf((String)params.get("relayActionTime")));
			tbp.setTimeZoneNumber(Integer.valueOf((String)params.get("timeZoneNumber")));
			tbp.setId((String)params.get("id"));
			tbp.setOpenType(Integer.valueOf((String)params.get("openType")));
			tbAccessParametersService.save(tbp);
			
			TbDownloadAccessParameters tbd = new TbDownloadAccessParameters();
			tbd.setParametersId(tbp.getId());
			tbd.setEquipmentId(tbp.getEquipmentId());
			tbd.setIsDownload("0");
			tbd.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			tbd.setRegisterTime(DateUtils.formatDateTime(new Date()));
			tbDownLoadAccessParametersService.save(tbd);
		}else {
			TbDoorTimeZone ttz = new TbDoorTimeZone();
			ttz.setDoorPos(Integer.valueOf((String)params.get("doorPos")));
			ttz.setEquipmentId((String)params.get("equipmentId"));
			ttz.setEquipSn((String)params.get("equipSn"));
			ttz.setTimeZoneNumber(Integer.valueOf((String)params.get("timeZoneNumber")));
			ttz.setWeekNumber(Integer.valueOf((String)params.get("weekNumber")));
			ttz.setId((String)params.get("id"));
			ttz.setTimeFrameEnd1((String)params.get("timeFrameEnd1"));
			ttz.setTimeFrameEnd2((String)params.get("timeFrameEnd2"));
			ttz.setTimeFrameEnd3((String)params.get("timeFrameEnd3"));
			ttz.setTimeFrameEnd4((String)params.get("timeFrameEnd4"));
			ttz.setTimeFrameStart1((String)params.get("timeFrameStart1"));
			ttz.setTimeFrameStart2((String)params.get("timeFrameStart2"));
			ttz.setTimeFrameStart3((String)params.get("timeFrameStart3"));
			ttz.setTimeFrameStart4((String)params.get("timeFrameStart4"));
			
			tbDoorTimeZoneService.save(ttz);
			
			TbDownloadDoorTimeZone tbd = new TbDownloadDoorTimeZone();
			tbd.setTimeZoneId(ttz.getId());
			tbd.setEquipmentId(ttz.getEquipmentId());
			tbd.setIsDownload("0");
			tbd.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			tbd.setRegisterTime(DateUtils.formatDateTime(new Date()));
			tbDownLoadDoorTimeZoneService.save(tbd);
		}
	}
}