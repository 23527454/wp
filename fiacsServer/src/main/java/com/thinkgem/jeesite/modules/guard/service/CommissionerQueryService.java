/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerQuery;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerQueryDao;

/**
 * 人员交接明细Service
 * @author Jumbo
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class CommissionerQueryService extends CrudService<CommissionerQueryDao, CommissionerQuery> {

	public CommissionerQuery get(String id) {
		return super.get(id);
	}
	
	public List<CommissionerQuery> findList(CommissionerQuery commissionerQuery) {
		return super.findList(commissionerQuery);
	}
	
	public Page<CommissionerQuery> findPage(Page<CommissionerQuery> page, CommissionerQuery commissionerQuery) {
		return super.findPage(page, commissionerQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(CommissionerQuery commissionerQuery) {
		super.save(commissionerQuery);
	}
	
	@Transactional(readOnly = false)
	public void delete(CommissionerQuery commissionerQuery) {
		super.delete(commissionerQuery);
	}
	
}