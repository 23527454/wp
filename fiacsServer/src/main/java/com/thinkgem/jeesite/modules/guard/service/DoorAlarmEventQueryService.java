/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.DoorAlarmEventQueryDao;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEventQuery;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 车辆事件查询Service
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class DoorAlarmEventQueryService extends CrudService<DoorAlarmEventQueryDao, DoorAlarmEventQuery> {

	public DoorAlarmEventQuery get(String id) {
		return super.get(id);
	}
	
	public List<DoorAlarmEventQuery> findList(DoorAlarmEventQuery doorAlarmEvent) {
		return super.findList(doorAlarmEvent);
	}
	
	public Page<DoorAlarmEventQuery> findPage(Page<DoorAlarmEventQuery> page, DoorAlarmEventQuery doorAlarmEvent) {
		for (Role r : doorAlarmEvent.getCurrentUser().getRoleList()) {
			if ("3".equals(r.getId())) {
				if(r.getCurrentUser()!=null) {
				doorAlarmEvent.setOffice(r.getCurrentUser().getOffice());
				}
			}else {
				
			}
		}
		return super.findPage(page, doorAlarmEvent);
	}
	
}