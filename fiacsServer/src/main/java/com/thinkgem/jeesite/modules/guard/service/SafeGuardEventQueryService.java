/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.SafeGuardEventQueryDao;
import com.thinkgem.jeesite.modules.guard.entity.SafeGuardEventQuery;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 *维保员事件查询Service
 * @author zgx
 * @version 2018-12-31
 */
@Service
@Transactional(readOnly = true)
public class SafeGuardEventQueryService extends CrudService<SafeGuardEventQueryDao, SafeGuardEventQuery> {

	public SafeGuardEventQuery get(String id) {
		return super.get(id);
	}
	
	public List<SafeGuardEventQuery> findList(SafeGuardEventQuery safeGuardEvent) {
		return super.findList(safeGuardEvent);
	}
	
	public Page<SafeGuardEventQuery> findPage(Page<SafeGuardEventQuery> page, SafeGuardEventQuery safeGuardEvent) {
		for (Role r : safeGuardEvent.getCurrentUser().getRoleList()) {
			if ("3".equals(r.getId())) {
				if(r.getCurrentUser()!=null) {
				safeGuardEvent.setOffice(r.getCurrentUser().getOffice());
				}
			}else {
				
			}
		}
		return super.findPage(page, safeGuardEvent);
	}
	
}