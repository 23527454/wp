/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadAccessParameters;

/**
  * 门禁参数信息Service
 * @author zgx
 * @version 2019-01-18
 */
@MyBatisDao
public interface TbDownloadAccessParametersDao extends CrudDao<TbDownloadAccessParameters> {
	
}