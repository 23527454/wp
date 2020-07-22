/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DefenseParaInfo;

import java.util.List;

/**
 * access_defense_infoDAO接口
 * @author demo
 * @version 2020-07-03
 */
@MyBatisDao
public interface DefenseParaInfoDao extends CrudDao<DefenseParaInfo> {
    public List<DefenseParaInfo> getByEId(String id);

    public int deleteByEId(String id);
	
}