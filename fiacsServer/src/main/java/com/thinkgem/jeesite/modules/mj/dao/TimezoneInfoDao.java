/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.mj.entity.TimezoneInfo;
import org.apache.ibatis.annotations.Param;


/**
 * access_door_timezoneDAO接口
 * @author demo
 * @version 2020-07-01
 */
@MyBatisDao
public interface TimezoneInfoDao extends CrudDao<TimezoneInfo> {
	public int deleteByEId(String id);

	public TimezoneInfo getByWdAndAid(TimezoneInfo timezoneInfo);

	public String findAIdById(String id);

	public Integer findCountByNum(@Param("id") String id, @Param("num") String num);
}