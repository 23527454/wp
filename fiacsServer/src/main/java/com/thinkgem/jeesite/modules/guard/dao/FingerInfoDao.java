/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;
import java.util.Set;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.FingerInfo;

/**
 * 人员信息管理DAO接口
 * @author Jumbo
 * @version 2017-06-27
 */
@MyBatisDao
public interface FingerInfoDao extends CrudDao<FingerInfo> {
	
	
	/**
	 * 查询所有数据列表
	 * @param entity
	 * @return
	 */
	public Set<Integer> findAllFingerNumList(FingerInfo entity);
	
	public String findByFingerNum(String fingerNum);
}