/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxOrder;

/**
 * 款箱预约DAO接口
 * @author Jumbo
 * @version 2017-07-29
 */
@MyBatisDao
public interface MoneyBoxOrderDao extends CrudDao<MoneyBoxOrder> {
	public List<MoneyBoxOrder> findListByArea(Map map);
}