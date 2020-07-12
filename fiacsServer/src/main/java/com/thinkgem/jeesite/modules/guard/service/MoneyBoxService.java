/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.DownloadPerson;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;

/**
 * 款箱信息Service
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class MoneyBoxService extends CrudService<MoneyBoxDao, MoneyBox> {

	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private DownloadMoneyBoxService downloadMoneyBoxService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private AreaDao areaDao;

	public MoneyBox get(String id) {
		return super.get(id);
	}

	public MoneyBox getByBoxCode(String boxCode){
		return super.dao.getByBoxCode(boxCode);
	}

	public List<MoneyBox> findList(MoneyBox moneyBox) {
		for (Role r : moneyBox.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o4", "u"));
			} else {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o3", "u"));
			}
		}
		return super.findList(moneyBox);
	}
	
	/**
	 * 查询网点区域中的中心金库调拨款箱以及自己的调拨款箱	
	 * @param moneyBox
	 * @return
	 */
	public List<MoneyBox> findTmpList(MoneyBox moneyBox) {
		for (Role r : moneyBox.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o4", "u"));
			} else {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o3", "u"));
			}
		}
		return dao.findTmpList(moneyBox);
	}
	
	/**
	 * 上级中心金库的调拨款箱
	 * @param moneyBox
	 * @return
	 */
	public List<MoneyBox> findParentTmpList(MoneyBox moneyBox){
		/*for (Role r : moneyBox.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o4", "u"));
			} else {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o3", "u"));
			}
		}*/
		//需要得到当前网点的上级网点集合
		Office curoffice = officeDao.get(moneyBox.getOffice().getId());
		String[] parentIds = curoffice.getParentIds().split(",");
		
		return dao.findParentTmpList(parentIds,curoffice.getId());
	}
	
	public Page<MoneyBox> findPage(Page<MoneyBox> page, MoneyBox moneyBox) {
		for (Role r : moneyBox.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
				
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o3", "u"));
			} else if(r.getDataScope().equals("2")){
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o4", "u"));
			}else {
				moneyBox.getSqlMap().put("dsf", dataScopeFilter(moneyBox.getCurrentUser(), "o3", "u"));
			}
		}
		return super.findPage(page, moneyBox);
	}

	@Transactional(readOnly = false)
	public void insentDownload(MoneyBox moneyBox, String type) {
		//1.款箱是中心金库的话要同步到同一区域下的所有网点。
		//2.款箱是非中心金库只同步到自己所属网点设备以及同一个区域中心金库设备；
		String officeId = moneyBox.getOffice().getId();
		Equipment equipment = equipmentService.getByOfficeId(officeId);
		if(equipment!=null) {
			if(Equipment.SITE_TYPE_ZXJK.equals(equipment.getSiteType())){//中心金库的款箱
				Office zxjkOffice = officeDao.get(moneyBox.getOffice().getId());
				String areaId = zxjkOffice.getArea().getId();
				List<Office> offices = officeDao.findOfficesByAreaIdHasEquipment(areaId);
				for (Office office : offices) {
					insentDownload2Office(moneyBox, office.getId(), type);
				}
			}else{
				insentDownload2Office(moneyBox, moneyBox.getOffice().getId(), type);
				
				AssertUtil.assertNotBlank(equipment.getArea().getId(), "area.id");
				List<Office> offices = officeDao.findOfficesByAreaIdHasEquipment(equipment.getArea().getId());
				for (Office office : offices) {
					if(!Office.TYPE_ZXJK.equals(office.getType())){
						continue;
					}
					insentDownload2Office(moneyBox, office.getId(), type);
				}
			}
		}
/*		String officeId = moneyBox.getOffice().getId();
		Equipment equipment = equipmentService.getByOfficeId(officeId);
		if(Equipment.SITE_TYPE_ZXJK.equals(equipment.getSiteType())){//中心金库的款箱
			Office zxjkOffice = officeDao.get(moneyBox.getOffice().getId());
			String areaId = zxjkOffice.getArea().getId();
			List<Office> offices = officeDao.findOfficesByAreaIdHasEquipment(areaId);
			for (Office office : offices) {
				insentDownload2Office(moneyBox, office.getId(), type);
			}
		}else{
			insentDownload2Office(moneyBox, moneyBox.getOffice().getId(), type);
			
			AssertUtil.assertNotBlank(moneyBox.getArea().getId(), "area.id");
			List<Office> offices = officeDao.findOfficesByAreaIdHasEquipment(moneyBox.getArea().getId());
			for (Office office : offices) {
				if(!Office.TYPE_ZXJK.equals(office.getType())){
					continue;
				}
				insentDownload2Office(moneyBox, office.getId(), type);
			}
		}
*/	}
	
	@Transactional(readOnly = false)
	public void insentDownload2Office(MoneyBox moneyBox, String officeId, String downloadType) {
		Equipment equipment = equipmentService.getByOfficeId(officeId);
		if(null == equipment){
			return;
		}
		DownloadMoneyBox downloadMoneyBox = new DownloadMoneyBox();
		downloadMoneyBox.setMoneyBoxId(moneyBox.getId());
		downloadMoneyBox.setEquipmentId(equipment.getId());
		downloadMoneyBox.setRegisterTime(DateUtils.formatDateTime(new Date()));
		downloadMoneyBox.setIsDownload("0");
		downloadMoneyBox.setDownloadType(downloadType);
		if(downloadMoneyBoxService.countByEntity(downloadMoneyBox)==0){
			downloadMoneyBoxService.save(downloadMoneyBox);
		}
	}

	@Transactional(readOnly = false)
	public void save(MoneyBox moneyBox) {
		boolean messageType;
		if (moneyBox.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		
		MoneyBox oldMoney = this.get(moneyBox.getId());
		
		super.save(moneyBox);
		//edit 2019-7-1 如果是更新网点 需要增加同步删除老网点的款箱信息
		if(oldMoney!=null&&!oldMoney.getOffice().getId().equals(moneyBox.getOffice().getId())) {
			this.insentDownload(oldMoney, DownloadEntity.DOWNLOAD_TYPE_DEL);
		}
		
		this.insentDownload(moneyBox, DownloadEntity.DOWNLOAD_TYPE_ADD);
		
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "新增款箱:["+moneyBox.getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改款箱:["+moneyBox.getId()+"]");
		}
	}

	@Transactional(readOnly = false)
	public void delete(MoneyBox moneyBox) {
		super.delete(moneyBox);
		
		DownloadMoneyBox downloadMoneyBox = new DownloadMoneyBox();
		downloadMoneyBox.setMoneyBoxId(moneyBox.getId());
		downloadMoneyBox.setDownloadType("0");
		downloadMoneyBox.setIsDownload("0");
		int deleted = downloadMoneyBoxService.delete1(downloadMoneyBox);
		this.insentDownload(moneyBox, DownloadEntity.DOWNLOAD_TYPE_DEL);
		
		LogUtils.saveLog(Servlets.getRequest(), "删除款箱:["+moneyBox.getId()+"]");
	}

	public int countByBoxCode(String id, String boxCode) {
		if(StringUtils.isBlank(boxCode)) {
			return 0;
		}
		MoneyBox e = new MoneyBox();
		e.setId(id);
		e.setBoxCode(boxCode);
		return super.countByColumnExceptSelf(e);
	}

	public int countByCardNum(String id, String cardNum) {
		if(StringUtils.isBlank(cardNum)) {
			return 0;
		}
		MoneyBox e = new MoneyBox();
		e.setId(id);
		e.setCardNum(cardNum);;;
		return super.countByColumnExceptSelf(e);
	}

}