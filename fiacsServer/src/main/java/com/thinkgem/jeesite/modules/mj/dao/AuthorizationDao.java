/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.mj.entity.Authorization;
import org.apache.ibatis.annotations.Param;

/**
 * power_authorizationDAO接口
 * @author demo
 * @version 2020-07-04
 */
@MyBatisDao
public interface AuthorizationDao extends CrudDao<Authorization> {

	public int deleteById(Authorization authorization);

	public int deleteByAId(@Param("id")String id);

	public int getCountBySId(@Param("staffId")String staffId, @Param("accessParaInfoId")String accessParaInfoId);
}