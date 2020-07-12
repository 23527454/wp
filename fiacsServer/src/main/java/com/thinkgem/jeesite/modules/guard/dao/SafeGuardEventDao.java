/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.SafeGuardEvent;

/**
 * 维保员事件DAO接口
 * @author zengguoxiang
 * @version 2018-12-31
 */
@MyBatisDao
public interface SafeGuardEventDao extends CrudDao<SafeGuardEvent> {
	public List<SafeGuardEvent> getFeeds(SafeGuardEvent event);
	public int delflag(SafeGuardEvent entity);
}