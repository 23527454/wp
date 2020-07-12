/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.CarImage;

/**
 * 车辆信息DAO接口
 * @author Jumbo
 * @version 2017-06-27
 */
@MyBatisDao
public interface CarImageDao extends CrudDao<CarImage> {
	
}