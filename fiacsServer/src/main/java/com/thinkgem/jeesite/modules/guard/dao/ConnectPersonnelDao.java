/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.ConnectPersonnel;

/**
 * 人员交接明细接口
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@MyBatisDao
public interface ConnectPersonnelDao extends CrudDao<ConnectPersonnel> {
	public ConnectPersonnel getTwo(@Param("id") String id, @Param("staff_id") String staff_id);
}