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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.ConnectPersonnel;
import com.thinkgem.jeesite.modules.guard.dao.ConnectEventQueryDao;
import com.thinkgem.jeesite.modules.guard.entity.ConnectCommissioner;
import com.thinkgem.jeesite.modules.guard.dao.ConnectCommissionerDao;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;

/**
 * 交接事件查询Service
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class ConnectEventQueryService extends CrudService<ConnectEventQueryDao, ConnectEventQuery> {

	public ConnectEventQuery get(String id) {
		return super.get(id);
	}
	
	public List<ConnectEventQuery> findList(ConnectEventQuery connectPersonnel) {
		return super.findList(connectPersonnel);
	}
	
	public Page<ConnectEventQuery> findPage(Page<ConnectEventQuery> page, ConnectEventQuery connectEventQuery) {
		return super.findPage(page, connectEventQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(ConnectEventQuery connectEventQuery) {
		super.save(connectEventQuery);
	}
	
	@Transactional(readOnly = false)
	public void delete(ConnectEventQuery connectEventQuery) {
		super.delete(connectEventQuery);
	}
	
}