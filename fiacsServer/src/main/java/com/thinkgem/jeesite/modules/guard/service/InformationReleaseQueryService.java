/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.DoorInOutEventQueryDao;
import com.thinkgem.jeesite.modules.guard.dao.InformationReleaseQueryDao;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.InformationReleaseQuery;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆事件查询Service
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class InformationReleaseQueryService extends CrudService<InformationReleaseQueryDao, InformationReleaseQuery> {

	public InformationReleaseQuery get(String id) {
		return super.get(id);
	}
	
	public List<InformationReleaseQuery> findList(InformationReleaseQuery doorInOutEvent) {
		return super.findList(doorInOutEvent);
	}
	
	public Page<InformationReleaseQuery> findPage(Page<InformationReleaseQuery> page, InformationReleaseQuery doorInOutEvent) {
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