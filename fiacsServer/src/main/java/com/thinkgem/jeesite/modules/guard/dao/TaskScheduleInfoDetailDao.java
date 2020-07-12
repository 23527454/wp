/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.TaskLineInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfoDetail;

/**
 * 排班明细DAO接口
 * @author Jumbo
 * @version 2017-07-10
 */
@MyBatisDao
public interface TaskScheduleInfoDetailDao extends CrudDao<TaskScheduleInfoDetail> {
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */

}