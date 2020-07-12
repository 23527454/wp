/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxAllotDao;
import com.thinkgem.jeesite.modules.guard.dao.AllotSeqDao;
import com.thinkgem.jeesite.modules.guard.entity.AllotParam;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.entity.AllotSeq;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 款箱调拨Service
 * @author Jumbo
 * @version 2017-07-29
 */
@Service
@Transactional(readOnly = true)
public class MoneyBoxAllotService extends CrudService<MoneyBoxAllotDao, MoneyBoxAllot> {
	@Autowired
	private DownloadMoneyBoxAllotService downloadMoneyBoxAllotService;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private AllotSeqDao taskSeqDao;
	
	public MoneyBoxAllot get(String id) {
		return super.get(id);
	}
	public List<MoneyBoxAllot> findListdel(MoneyBoxAllot moneyBoxAllot) {
		for (Role r : moneyBoxAllot.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
				moneyBoxAllot.getSqlMap().put("dsf", dataScopeFilter1(moneyBoxAllot.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxAllot.getSqlMap().put("dsf", dataScopeFilter(moneyBoxAllot.getCurrentUser(), "o", ""));
			}
		}
		return super.findList(moneyBoxAllot);
	}
	public List<MoneyBoxAllot> findList(MoneyBoxAllot moneyBoxAllot) {
		for (Role r : moneyBoxAllot.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
				moneyBoxAllot.getSqlMap().put("dsf", dataScopeFilter(moneyBoxAllot.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxAllot.getSqlMap().put("dsf", dataScopeFilter(moneyBoxAllot.getCurrentUser(), "o", ""));
			}
		}
		return super.findList(moneyBoxAllot);
	}
	
	public List<MoneyBoxAllot> findListByArea(Map map) {
		return dao.findListByArea(map);
	}
	
	public Page<MoneyBoxAllot> findPage(Page<MoneyBoxAllot> page, MoneyBoxAllot moneyBoxAllot) {
		for (Role r : moneyBoxAllot.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBoxAllot.getSqlMap().put("dsf", dataScopeFilter(moneyBoxAllot.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxAllot.getSqlMap().put("dsf", dataScopeFilter(moneyBoxAllot.getCurrentUser(), "o", ""));
			}
		}
		return super.findPage(page, moneyBoxAllot);
	}
	
	@Transactional(readOnly = false)
	public void save(MoneyBoxAllot moneyBoxAllot) {
		AssertUtil.assertNotNull(moneyBoxAllot.getScheduleTime(), "ScheduleTime");
		AssertUtil.assertNotNull(moneyBoxAllot.getAllotType(), "AllotType");
		AssertUtil.assertNotNull(moneyBoxAllot.getClassTaskInfo(), "ClassTaskInfo");
		AssertUtil.assertNotNull(moneyBoxAllot.getClassTaskInfo().getId(), "ClassTaskInfo.id");
		super.save(moneyBoxAllot);
		this.insentDownload(moneyBoxAllot, DownloadEntity.DOWNLOAD_TYPE_ADD);
	}
	
	@Transactional(readOnly = false)
	public void saveBoxAllots(AllotParam allot){
		User user = UserUtils.getUser();
		AllotSeq taskSeq = new AllotSeq();
		taskSeq.preInsert();
		taskSeqDao.insert(taskSeq);
		for(int i=0;allot!=null&&allot.getRows()!=null&&i<allot.getRows().size();i++){
			AssertUtil.assertNotNull(taskSeq.getId(), "id");
			MoneyBox box = allot.getRows().get(i);
			MoneyBoxAllot boxAllot = new MoneyBoxAllot();
			boxAllot.setScheduleTime(DateUtils.parseDate(allot.getScheduleDate()));
			boxAllot.setCreateBy(user);
			boxAllot.setEquipmentId(allot.getEquipmentId());
			boxAllot.setMoneyBoxId(box.getId());
			boxAllot.setTaskScheduleId(taskSeq.getId());
			boxAllot.setClassTaskInfo(allot.getClassTaskInfo());
			boxAllot.setAllotType(allot.getAlloType());
			boxAllot.setCardNum(box.getCardNum());
			this.save(boxAllot);
		}
	}

	@Transactional(readOnly = false)
	public void insentDownload(MoneyBoxAllot moneyBoxAllot,String type) {
		
			String equipmentId = moneyBoxAllot.getEquipmentId();
			DownloadMoneyBoxAllot downloadMoneyBoxAllot = new DownloadMoneyBoxAllot();
			//款箱ID
			downloadMoneyBoxAllot.setMoneyBoxId(moneyBoxAllot.getMoneyBoxId());
			downloadMoneyBoxAllot.setEquipmentId(equipmentId);
			downloadMoneyBoxAllot.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadMoneyBoxAllot.setTaskScheduleId(moneyBoxAllot.getTaskScheduleId());
			downloadMoneyBoxAllot.setIsDownload("0");
			downloadMoneyBoxAllot.setDownloadType(type);
			downloadMoneyBoxAllot.setDownloadTime(DateUtils.formatDateTime(moneyBoxAllot.getCreateDate()));
			downloadMoneyBoxAllotService.save(downloadMoneyBoxAllot);
	}
	
	@Transactional(readOnly = false)
	public void delete(MoneyBoxAllot moneyBoxAllot) {
		super.delete(moneyBoxAllot);
		this.insentDownload(moneyBoxAllot, DownloadEntity.DOWNLOAD_TYPE_DEL);
	}

	@Transactional(readOnly = true)
	public int countByEntityCodition(MoneyBoxAllot moneyBoxAllot){
		return super.dao.countByEntityCodition(moneyBoxAllot);
	}
}