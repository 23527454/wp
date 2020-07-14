/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;


import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 人员信息管理DAO接口
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@MyBatisDao
public interface StaffDao extends CrudDao<Staff> {
	
	List<String> findListByOfficeId(Map<String,Object> map);
	
	void updateStaffArea(Map<String,Object> map);

	List<Staff> findAll(@Param("name") String name, @Param("workNum") String workNum,@Param("pageIndex") Integer pageIndex,@Param("size") Integer size);

	List<Staff> findByOfficeId(String officeId);
}