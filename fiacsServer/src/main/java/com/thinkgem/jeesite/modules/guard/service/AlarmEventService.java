/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.AlarmEventDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEventDetail;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 异常报警事件Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class AlarmEventService extends CrudService<AlarmEventDao, AlarmEvent> {
	
	@Autowired
	private MoneyBoxEventDetailDao moneyBoxEventDetailDao;
	@Autowired
	private MoneyBoxDao moneyBoxDao;
	public AlarmEvent get(String id) {
		AlarmEvent alarmEvent = super.get(id);
		if (alarmEvent == null){
			return null;
		}
		
		MoneyBoxEventDetail moneyBoxEventDetailRequest = new MoneyBoxEventDetail(alarmEvent);
		moneyBoxEventDetailRequest.setEventType(MoneyBoxEventDetail.EVENT_TYPE_ALARM_EVENT);
		alarmEvent.setMoneyBoxEventDetailList(moneyBoxEventDetailDao.findList(moneyBoxEventDetailRequest));
		for (MoneyBoxEventDetail moneyBoxEventDetail : alarmEvent.getMoneyBoxEventDetailList()) {
			moneyBoxEventDetail.setMoneyBox(moneyBoxDao.findOne(new MoneyBox(moneyBoxEventDetail)));
		}
		return alarmEvent;
	}
	
	public List<AlarmEvent> findList(AlarmEvent alarmEvent) {
		for (Role r : alarmEvent.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				alarmEvent.getSqlMap().put("dsf", dataScopeFilter(alarmEvent.getCurrentUser(), "h", ""));
			} else {
				alarmEvent.getSqlMap().put("dsf", dataScopeFilter(alarmEvent.getCurrentUser(), "b", ""));
			}
		}
		
		
		return super.findList(alarmEvent);
	}
	
	public Page<AlarmEvent> findPage(Page<AlarmEvent> page, AlarmEvent alarmEvent) {
		for (Role r : alarmEvent.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
			/*	alarmEvent.getSqlMap().put("dsf", dataScopeFilter(alarmEvent.getCurrentUser(), "h", ""));*/
					alarmEvent.getSqlMap().put("dsf", dataScopeFilter(alarmEvent.getCurrentUser(), "b", ""));
			} else if(r.getDataScope().equals("2")){
				alarmEvent.getSqlMap().put("dsf", dataScopeFilter(alarmEvent.getCurrentUser(), "h", ""));
			}else {
				alarmEvent.getSqlMap().put("dsf", dataScopeFilter(alarmEvent.getCurrentUser(), "b", ""));
			}
		}
		return super.findPage(page, alarmEvent);
	}
	
	@Transactional(readOnly = false)
	public void save(AlarmEvent alarmEvent) {
		super.save(alarmEvent);
	}
	
	@Transactional(readOnly = false)
	public void delete(AlarmEvent alarmEvent) {
		super.delete(alarmEvent);
	}
	
	public List<AlarmEvent> getFeeds(String id, String latestTime, String nodes){
		AlarmEvent alarmEvent = new AlarmEvent();
		alarmEvent.setId(id);
		alarmEvent.setTime(latestTime);
		alarmEvent.setNodes(nodes);
		return super.dao.getFeeds(alarmEvent);
	}
}