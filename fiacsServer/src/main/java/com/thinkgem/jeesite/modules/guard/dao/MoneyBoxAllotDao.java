/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;

/**
 * 款箱调拨DAO接口
 * @author Jumbo
 * @version 2017-07-29
 */
@MyBatisDao
public interface MoneyBoxAllotDao extends CrudDao<MoneyBoxAllot> {
	public List<MoneyBoxAllot> findListByArea(Map map);

	public int countByEntityCodition(MoneyBoxAllot moneyBoxAllot);
}