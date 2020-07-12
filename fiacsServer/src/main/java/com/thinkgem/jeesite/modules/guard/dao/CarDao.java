/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.Car;

/**
 * 车辆信息DAO接口
 * @author Jumbo
 * @version 2017-06-27
 */
@MyBatisDao
public interface CarDao extends CrudDao<Car> {
	List<String> findListByCompanyIds(String companyId);
	
	void updateCarArea(Map<String,Object> map);
}