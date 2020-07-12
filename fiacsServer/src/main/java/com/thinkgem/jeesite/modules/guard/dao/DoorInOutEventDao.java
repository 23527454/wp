/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEvent;

/**
 * 车辆事件DAO接口
 * @author Jumbo
 * @version 2017-06-28
 */
@MyBatisDao
public interface DoorInOutEventDao extends CrudDao<DoorInOutEvent> {
	public List<DoorInOutEvent> getFeeds(DoorInOutEvent doorAlarmEvent);
	public int delflag(DoorInOutEvent entity);
}