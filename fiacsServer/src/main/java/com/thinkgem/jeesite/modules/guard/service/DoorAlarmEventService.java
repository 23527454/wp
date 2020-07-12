/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.DoorAlarmEventDao;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEvent;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 车辆事件Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DoorAlarmEventService extends CrudService<DoorAlarmEventDao, DoorAlarmEvent> {

	public DoorAlarmEvent get(String id) {
		return super.get(id);
	}
	
	public List<DoorAlarmEvent> findList(DoorAlarmEvent DoorAlarmEvent) {
		for (Role r : DoorAlarmEvent.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				DoorAlarmEvent.getSqlMap().put("dsf", dataScopeFilter(DoorAlarmEvent.getCurrentUser(), "c", ""));
			} else {
				DoorAlarmEvent.getSqlMap().put("dsf", dataScopeFilter(DoorAlarmEvent.getCurrentUser(), "b", ""));
			}
		}
		
		
		return super.findList(DoorAlarmEvent);
	}
	
	public Page<DoorAlarmEvent> findPage(Page<DoorAlarmEvent> page, DoorAlarmEvent DoorAlarmEvent) {
		for (Role r : DoorAlarmEvent.getCurrentUser().getRoleList()) {
			if ( r.getDataScope().equals("3")) {
				/*DoorAlarmEvent.getSqlMap().put("dsf", dataScopeFilter(DoorAlarmEvent.getCurrentUser(), "c", ""));*/
				DoorAlarmEvent.getSqlMap().put("dsf", dataScopeFilter(DoorAlarmEvent.getCurrentUser(), "b", ""));
			}else if(r.getDataScope().equals("2")){
				DoorAlarmEvent.getSqlMap().put("dsf", dataScopeFilter(DoorAlarmEvent.getCurrentUser(), "c", ""));
			} else {
				DoorAlarmEvent.getSqlMap().put("dsf", dataScopeFilter(DoorAlarmEvent.getCurrentUser(), "b", ""));
			}
		}
		return super.findPage(page, DoorAlarmEvent);
	}
	
	@Transactional(readOnly = false)
	public void save(DoorAlarmEvent DoorAlarmEvent) {
		super.save(DoorAlarmEvent);
	}
	
	@Transactional(readOnly = false)
	public void delete(DoorAlarmEvent DoorAlarmEvent) {
		super.delete(DoorAlarmEvent);
	}
	
	public List<DoorAlarmEvent> getFeeds(String latestId, String nodes){
		DoorAlarmEvent DoorAlarmEvent = new DoorAlarmEvent();
		DoorAlarmEvent.setId(latestId);
		DoorAlarmEvent.setNodes(nodes);
		return super.dao.getFeeds(DoorAlarmEvent);
	}
	
}