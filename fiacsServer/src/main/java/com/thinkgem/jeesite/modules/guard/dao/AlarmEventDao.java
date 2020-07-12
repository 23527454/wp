/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;

/**
 * 异常报警事件DAO接口
 * @author Jumbo
 * @version 2017-06-28
 */
@MyBatisDao
public interface AlarmEventDao extends CrudDao<AlarmEvent> {
	public List<AlarmEvent> getFeeds(AlarmEvent alarmEvent);

	public AlarmEvent findOne(AlarmEvent alarmEvent);
}