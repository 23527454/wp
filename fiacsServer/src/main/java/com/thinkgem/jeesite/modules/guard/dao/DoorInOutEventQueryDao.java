/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.CarEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEventQuery;

/**
 * 门禁出入事件查询DAO接口
 * @author zgx
 * @version 2018-12-29
 */
@MyBatisDao
public interface DoorInOutEventQueryDao extends CrudDao<DoorInOutEventQuery> {
	
}