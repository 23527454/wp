/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.mj.entity.DownloadAccessParaInfo;

/**
 * 门禁同步信息DAO接口
 * @author Jumbo
 * @version 2017-06-28
 */
@MyBatisDao
public interface DownloadAccessParaInfoDao extends CrudDao<DownloadAccessParaInfo> {
    int countByEntity(DownloadAccessParaInfo downloadAccessParaInfo);
}