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
import com.thinkgem.jeesite.modules.guard.entity.EventDetailQuery;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailQueryDao;

/**
 * 人员交接明细Service
 * 
 * @author Jumbo
 * @version 2017-07-20
 */
@Service
@Transactional(readOnly = true)
public class EventDetailQueryService extends CrudService<EventDetailQueryDao, EventDetailQuery> {
	@Autowired
	private StaffService staffService;

	public EventDetailQuery get(String id) {
		EventDetailQuery eventDetailQuery = super.get(id);
		if (eventDetailQuery == null)
			return null;
		eventDetailQuery.setStaff(staffService.getWithDel(eventDetailQuery.getPersonId()));
		return eventDetailQuery;
	}

	public List<EventDetailQuery> findList(EventDetailQuery eventDetailQuery) {
		for (Role r : eventDetailQuery.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				eventDetailQuery.getSqlMap().put("dsf", dataScopeFilter(eventDetailQuery.getCurrentUser(), "o2", ""));
			} else {
				eventDetailQuery.getSqlMap().put("dsf", dataScopeFilter(eventDetailQuery.getCurrentUser(), "o1", ""));
			}
		}
		
		return super.findList(eventDetailQuery);
	}

	public Page<EventDetailQuery> findPage(Page<EventDetailQuery> page, EventDetailQuery eventDetailQuery) {
		for (Role r : eventDetailQuery.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
				eventDetailQuery.getSqlMap().put("dsf", dataScopeFilter(eventDetailQuery.getCurrentUser(), "o1", ""));
			} else if(r.getDataScope().equals("2") ){
				eventDetailQuery.getSqlMap().put("dsf", dataScopeFilter(eventDetailQuery.getCurrentUser(), "o2", ""));
			}else {
				eventDetailQuery.getSqlMap().put("dsf", dataScopeFilter(eventDetailQuery.getCurrentUser(), "o1", ""));
			}
		}
		Page<EventDetailQuery> pageEventDetail = super.findPage(page, eventDetailQuery);
		for (int i = 0; i < pageEventDetail.getList().size(); i++) {
			pageEventDetail.getList().get(i).setStaff(staffService.getWithDel(pageEventDetail.getList().get(i).getPersonId()));
		}
		return pageEventDetail;
	}

	@Transactional(readOnly = false)
	public void save(EventDetailQuery eventDetailQuery) {
		super.save(eventDetailQuery);
	}

	@Transactional(readOnly = false)
	public void delete(EventDetailQuery eventDetailQuery) {
		super.delete(eventDetailQuery);
	}

}