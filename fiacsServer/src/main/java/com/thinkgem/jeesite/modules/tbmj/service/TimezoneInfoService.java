/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tbmj.dao.TimezoneInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.TimezoneInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * access_door_timezoneService
 * @author demo
 * @version 2020-07-01
 */
@Service
@Transactional(readOnly=true)
public class TimezoneInfoService extends CrudService<TimezoneInfoDao, TimezoneInfo> {
	@Autowired
	private TimezoneInfoDao timezoneInfoDao;

	public List<TimezoneInfo> getByEId(String id){return timezoneInfoDao.getByEId(id);}

	public Integer modifyByWeekNum(TimezoneInfo timezoneInfo){return timezoneInfoDao.modifyByWeekNum(timezoneInfo);}

	public Integer findCountByNum(String id,String doorPos, String num,String type){
		return timezoneInfoDao.findCountByNum(id,doorPos,num,type);
	}

	public int deleteByEId(String id){
		return timezoneInfoDao.deleteByEId(id);
	}

	public String findAIdById(String id){
		return timezoneInfoDao.findAIdById(id);
	}

	public TimezoneInfo getByWdAndAid(TimezoneInfo timezoneInfo){
		return timezoneInfoDao.getByWdAndAid(timezoneInfo);
	}

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public TimezoneInfo get(String id) {
		return super.get(id);
	}

	/**
	 * 查询分页
	 * @param page 分页对象
	 * @param timezoneInfo
	 * @return
	 */
	public Page<TimezoneInfo> findPage(Page<TimezoneInfo> page, TimezoneInfo timezoneInfo) {
		return super.findPage(page, timezoneInfo);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param timezoneInfo
	 */
	@Transactional(readOnly=false)
	public void save(TimezoneInfo timezoneInfo) {
		super.save(timezoneInfo);
	}

	/**
	 * 删除数据
	 * @param timezoneInfo
	 */
	@Transactional(readOnly=false)
	public void delete(TimezoneInfo timezoneInfo) {
		super.delete(timezoneInfo);
	}
	
}