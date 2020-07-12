/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.AlarmEventQueryDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEventDetail;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 异常报警事件查询Service
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class AlarmEventQueryService extends CrudService<AlarmEventQueryDao, AlarmEventQuery> {
	@Autowired
	private MoneyBoxEventDetailDao moneyBoxEventDetailDao;

	public AlarmEventQuery get(String id) {
		AlarmEventQuery alarmEventQuery = super.get(id);
		if (alarmEventQuery == null){
			return null;
		}
		
		MoneyBoxEventDetail moneyBoxEventDetailRequest = new MoneyBoxEventDetail(alarmEventQuery);
		moneyBoxEventDetailRequest.setEventType(MoneyBoxEventDetail.EVENT_TYPE_ALARM_EVENT);
		alarmEventQuery.setMoneyBoxEventDetailList(moneyBoxEventDetailDao.findList(moneyBoxEventDetailRequest));

		return alarmEventQuery;
	}

	public List<AlarmEventQuery> findList(AlarmEventQuery alarmEventQuery) {
		for (Role r : alarmEventQuery.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				alarmEventQuery.getSqlMap().put("dsf", dataScopeFilter(alarmEventQuery.getCurrentUser(), "o2", ""));
			} else {
				alarmEventQuery.getSqlMap().put("dsf", dataScopeFilter(alarmEventQuery.getCurrentUser(), "o1", ""));
			}
		}
		return super.findList(alarmEventQuery);
	}

	public Page<AlarmEventQuery> findPage(Page<AlarmEventQuery> page, AlarmEventQuery alarmEventQuery) {
		for (Role r : alarmEventQuery.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
				alarmEventQuery.getSqlMap().put("dsf", dataScopeFilter(alarmEventQuery.getCurrentUser(), "o1", ""));
			} else if(r.getDataScope().equals("2") ){
				alarmEventQuery.getSqlMap().put("dsf", dataScopeFilter(alarmEventQuery.getCurrentUser(), "o2", ""));
			}else {
				alarmEventQuery.getSqlMap().put("dsf", dataScopeFilter(alarmEventQuery.getCurrentUser(), "o1", ""));
			}
		}
		return super.findPage(page, alarmEventQuery);
	}

	@Transactional(readOnly = false)
	public void save(AlarmEventQuery alarmEventQuery) {
		super.save(alarmEventQuery);
	}

	@Transactional(readOnly = false)
	public void delete(AlarmEventQuery alarmEventQuery) {
		super.delete(alarmEventQuery);
	}

}