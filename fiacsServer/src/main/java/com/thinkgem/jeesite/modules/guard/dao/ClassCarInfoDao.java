/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;

/**
 * 班组DAO接口
 * @author Jumbo
 * @version 2017-07-29
 */
@MyBatisDao
public interface ClassCarInfoDao extends CrudDao<ClassCarInfo> {
	public int deleteCar(@Param("carId") String carId);
	public int deleteClass(@Param("classTaskId") String classTaskId);
	
	
}