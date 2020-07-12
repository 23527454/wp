/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;

/**
 * 款箱信息DAO接口
 * @author Jumbo
 * @version 2017-06-27
 */
@MyBatisDao
public interface MoneyBoxDao extends CrudDao<MoneyBox> {
	public List<MoneyBox> findTmpList(MoneyBox moneyBox);
	
	public List<MoneyBox> findParentTmpList(@Param(value="parentIds")String[] parentIds,@Param(value="officeId")String officeId);

	public MoneyBox getByBoxCode(String boxCode);
}