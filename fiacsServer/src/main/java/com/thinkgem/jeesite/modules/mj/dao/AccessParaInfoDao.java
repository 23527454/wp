/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.mj.entity.AccessParaInfo;

import java.util.List;

/**
 * access_para_infoDAO接口
 * @author demo
 * @version 2020-06-29
 */
@MyBatisDao
public interface AccessParaInfoDao extends CrudDao<AccessParaInfo> {
	public List<AccessParaInfo> getByEId(String id);

	public int deleteByEId(String id);

	public List<AccessParaInfo> findAll();

	List<AccessParaInfo> findListById(String id);
}