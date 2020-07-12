/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.ClassPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 班组DAO接口
 * @author Jumbo
 * @version 2017-07-29
 */
@MyBatisDao
public interface ClassPersonInfoDao extends CrudDao<ClassPersonInfo> {
	public int deletePerson(@Param("personId") String personId);
	public int deleteClass(@Param("classTaskId") String classTaskId);
}