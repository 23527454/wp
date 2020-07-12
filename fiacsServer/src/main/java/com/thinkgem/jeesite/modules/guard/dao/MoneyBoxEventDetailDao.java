/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEventDetail;

/**
 * 款箱事件详情DAO接口
 * @author Jumbo
 * @version 2017-08-01
 */
@MyBatisDao
public interface MoneyBoxEventDetailDao extends CrudDao<MoneyBoxEventDetail> {
	public MoneyBoxEventDetail moneyId(MoneyBoxEventDetail entity);
	public int delflag(MoneyBoxEventDetail entity);
}