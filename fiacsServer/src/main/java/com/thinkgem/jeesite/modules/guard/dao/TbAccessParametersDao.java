/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.TbAccessParameters;

/**
 * 设备门禁参数
 * @author zgx
 * @version 2019.1.16
 */
@MyBatisDao
public interface TbAccessParametersDao extends CrudDao<TbAccessParameters> {

}