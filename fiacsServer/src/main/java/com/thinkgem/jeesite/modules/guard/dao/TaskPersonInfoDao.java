/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.TaskPersonInfo;

/**
 * 排班信息DAO接口
 * @author Junbo
 * @version 2017-08-01
 */
@MyBatisDao
public interface TaskPersonInfoDao extends CrudDao<TaskPersonInfo> {
	public int delete(@Param("taskScheduleId") String taskScheduleId);
}