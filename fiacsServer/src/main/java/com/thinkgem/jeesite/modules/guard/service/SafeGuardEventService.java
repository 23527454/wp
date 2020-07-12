/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.SafeGuardEventDao;
import com.thinkgem.jeesite.modules.guard.entity.SafeGuardEvent;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 维保员事件Service
 * @author zgx
 * @version 2018-12-31
 */
@Service
@Transactional(readOnly = true)
public class SafeGuardEventService extends CrudService<SafeGuardEventDao, SafeGuardEvent> {

	public SafeGuardEvent get(String id) {
		return super.get(id);
	}
	
	public List<SafeGuardEvent> findList(SafeGuardEvent SafeGuardEvent) {
		for (Role r : SafeGuardEvent.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				SafeGuardEvent.getSqlMap().put("dsf", dataScopeFilter(SafeGuardEvent.getCurrentUser(), "c", ""));
			} else {
				SafeGuardEvent.getSqlMap().put("dsf", dataScopeFilter(SafeGuardEvent.getCurrentUser(), "b", ""));
			}
		}
		
		
		return super.findList(SafeGuardEvent);
	}
	
	public Page<SafeGuardEvent> findPage(Page<SafeGuardEvent> page, SafeGuardEvent SafeGuardEvent) {
		for (Role r : SafeGuardEvent.getCurrentUser().getRoleList()) {
			if ( r.getDataScope().equals("3")) {
				/*SafeGuardEvent.getSqlMap().put("dsf", dataScopeFilter(SafeGuardEvent.getCurrentUser(), "c", ""));*/
				SafeGuardEvent.getSqlMap().put("dsf", dataScopeFilter(SafeGuardEvent.getCurrentUser(), "b", ""));
			}else if(r.getDataScope().equals("2")){
				SafeGuardEvent.getSqlMap().put("dsf", dataScopeFilter(SafeGuardEvent.getCurrentUser(), "c", ""));
			} else {
				SafeGuardEvent.getSqlMap().put("dsf", dataScopeFilter(SafeGuardEvent.getCurrentUser(), "b", ""));
			}
		}
		return super.findPage(page, SafeGuardEvent);
	}
	
	@Transactional(readOnly = false)
	public void save(SafeGuardEvent SafeGuardEvent) {
		super.save(SafeGuardEvent);
	}
	
	@Transactional(readOnly = false)
	public void delete(SafeGuardEvent SafeGuardEvent) {
		super.delete(SafeGuardEvent);
	}
	
	public List<SafeGuardEvent> getFeeds(String latestId, String nodes){
		SafeGuardEvent SafeGuardEvent = new SafeGuardEvent();
		SafeGuardEvent.setId(latestId);
		SafeGuardEvent.setNodes(nodes);
		return super.dao.getFeeds(SafeGuardEvent);
	}
	
}