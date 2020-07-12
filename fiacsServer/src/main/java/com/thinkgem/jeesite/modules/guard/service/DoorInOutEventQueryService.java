/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.DoorInOutEventQueryDao;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEvent;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEventQuery;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 车辆事件查询Service
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class DoorInOutEventQueryService extends CrudService<DoorInOutEventQueryDao, DoorInOutEventQuery> {

	public DoorInOutEventQuery get(String id) {
		return super.get(id);
	}
	
	public List<DoorInOutEventQuery> findList(DoorInOutEventQuery doorInOutEvent) {
		return super.findList(doorInOutEvent);
	}
	
	public Page<DoorInOutEventQuery> findPage(Page<DoorInOutEventQuery> page, DoorInOutEventQuery doorInOutEvent) {
		for (Role r : doorInOutEvent.getCurrentUser().getRoleList()) {
			if ("3".equals(r.getId())) {
				if(r.getCurrentUser()!=null) {
				doorInOutEvent.setOffice(r.getCurrentUser().getOffice());
				}
			}else {
				
			}
		}
		return super.findPage(page, doorInOutEvent);
	}
	
}