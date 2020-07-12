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
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxOrder;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxOrderDao;

/**
 * 款箱预约Service
 * @author Jumbo
 * @version 2017-07-29
 */
@Service
@Transactional(readOnly = true)
public class MoneyBoxOrderService extends CrudService<MoneyBoxOrderDao, MoneyBoxOrder> {

	@Autowired
	private MoneyBoxDao moneyBoxDao;
	
	public MoneyBoxOrder get(String id) {
		return super.get(id);
	}
	
	public List<MoneyBoxOrder> findList(MoneyBoxOrder moneyBoxOrder) {
		for (Role r : moneyBoxOrder.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBoxOrder.getSqlMap().put("dsf", dataScopeFilter(moneyBoxOrder.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxOrder.getSqlMap().put("dsf", dataScopeFilter(moneyBoxOrder.getCurrentUser(), "o", ""));
			}
		}
		return super.findList(moneyBoxOrder);
	}
	
	public List<MoneyBoxOrder> findListByArea(Map map) {
		return dao.findListByArea(map);
	}
	
	public Page<MoneyBoxOrder> findPage(Page<MoneyBoxOrder> page, MoneyBoxOrder moneyBoxOrder) {
		for (Role r : moneyBoxOrder.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				moneyBoxOrder.getSqlMap().put("dsf", dataScopeFilter(moneyBoxOrder.getCurrentUser(), "ar", ""));
			} else {
				moneyBoxOrder.getSqlMap().put("dsf", dataScopeFilter(moneyBoxOrder.getCurrentUser(), "o", ""));
			}
		}
		Page<MoneyBoxOrder> result =  super.findPage(page, moneyBoxOrder);
		return result;
	}
	
	@Transactional(readOnly = false)
	public void save(MoneyBoxOrder moneyBoxOrder) {
		super.save(moneyBoxOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(MoneyBoxOrder moneyBoxOrder) {
		super.delete(moneyBoxOrder);
	}
	
}