/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.Company;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 第三方公司DAO接口
 * @author Jumbo
 * @version 2017-07-19
 */
@MyBatisDao
public interface CompanyDao extends TreeDao<Company> {
	public List<Company> findParentIdsList(@Param("id") String id);
	public Company getById(String id);
}