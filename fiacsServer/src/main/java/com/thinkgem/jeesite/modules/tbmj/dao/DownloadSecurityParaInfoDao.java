/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadSecurityParaInfo;

/**
 * 防盗参数同步信息DAO接口
 * @author Jumbo
 * @version 2017-06-28
 */
@MyBatisDao
public interface DownloadSecurityParaInfoDao extends CrudDao<DownloadSecurityParaInfo> {
    int countByEntity(DownloadSecurityParaInfo downloadSecurityParaInfo);
}