/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.Tbequipment;

/**
 * 网点列表表单DAO接口
 * @author Jumbo
 * @version 2017-06-26
 */
@MyBatisDao
public interface TbequipmentDao extends CrudDao<Tbequipment> {
	
}