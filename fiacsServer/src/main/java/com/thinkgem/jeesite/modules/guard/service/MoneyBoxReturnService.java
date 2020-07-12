/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxReturnDao;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxOrder;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxReturn;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 款箱上缴Service
 * @author Jumbo
 * @version 2017-07-29
 */
@Service
@Transactional(readOnly = true)
public class MoneyBoxReturnService extends CrudService<MoneyBoxReturnDao, MoneyBoxReturn> {

	@Autowired
	private MoneyBoxDao moneyBoxDao;
	
	public MoneyBoxReturn get(String id) {
		return super.get(id);
	}
	
	public List<MoneyBoxReturn> findList(MoneyBoxReturn moneyBoxReturn) {
		for (Role r : moneyBoxReturn.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBoxReturn.getSqlMap().put("dsf", dataScopeFilter(moneyBoxReturn.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxReturn.getSqlMap().put("dsf", dataScopeFilter(moneyBoxReturn.getCurrentUser(), "o", ""));
			}
		}
		return super.findList(moneyBoxReturn);
	}
	
	public List<MoneyBoxReturn> findListByArea(Map map) {
		return dao.findListByArea(map);
	}
	
	public Page<MoneyBoxReturn> findPage(Page<MoneyBoxReturn> page, MoneyBoxReturn moneyBoxReturn) {
		for (Role r : moneyBoxReturn.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBoxReturn.getSqlMap().put("dsf", dataScopeFilter(moneyBoxReturn.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxReturn.getSqlMap().put("dsf", dataScopeFilter(moneyBoxReturn.getCurrentUser(), "o", ""));
			}
		}
		Page<MoneyBoxReturn>  result = super.findPage(page, moneyBoxReturn);
		return result;
	}
	
	@Transactional(readOnly = false)
	public void save(MoneyBoxReturn moneyBoxReturn) {
		super.save(moneyBoxReturn);
	}
	
	@Transactional(readOnly = false)
	public void delete(MoneyBoxReturn moneyBoxReturn) {
		super.delete(moneyBoxReturn);
	}
	
}