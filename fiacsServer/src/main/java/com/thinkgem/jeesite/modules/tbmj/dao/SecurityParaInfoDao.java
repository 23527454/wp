/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tbmj.entity.SecurityParaInfo;

import java.util.List;

/**
 * access_antitheftDAO接口
 * @author demo
 * @version 2020-07-02
 */
@MyBatisDao
public interface SecurityParaInfoDao extends CrudDao<SecurityParaInfo> {
    public List<SecurityParaInfo> getByEId(String id);

    public int deleteByEId(String id);
}