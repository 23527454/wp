/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;

/**
 * 线路信息DAO接口
 * @author Jumbo
 * @version 2017-06-29
 */
@MyBatisDao
public interface LineNodesDao extends CrudDao<LineNodes> {
	public List<LineNodes> findList();
	
//当前区域已经在
	public Set<String> findOfficeInLineNodeByArea(@Param("areaId")String areaId);
}