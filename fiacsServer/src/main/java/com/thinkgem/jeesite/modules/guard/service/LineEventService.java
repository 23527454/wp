/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.LineEvent;
import com.thinkgem.jeesite.modules.guard.entity.TaskCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskLineInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskPersonInfo;
import com.thinkgem.jeesite.modules.guard.dao.ConnectEventDao;
import com.thinkgem.jeesite.modules.guard.dao.LineEventDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskCarInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskLineInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskPersonInfoDao;

/**
 * 线路监控Service
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class LineEventService extends CrudService<LineEventDao, LineEvent> {
	
	@Autowired
	private TaskCarInfoDao taskCarInfoDao;
	@Autowired
	private TaskLineInfoDao taskLineInfoDao;
	@Autowired
	private TaskPersonInfoDao taskPersonInfoDao;
	@Autowired
	private ConnectEventDao connectEventDao;
	public LineEvent get(String id) {
		LineEvent lineEvent = super.get(id);
		
		lineEvent.setTaskCarInfoList(taskCarInfoDao.findList(new TaskCarInfo(lineEvent)));
		lineEvent.setTaskLineInfoList(taskLineInfoDao.findList(new TaskLineInfo(lineEvent)));
		lineEvent.setTaskPersonInfoList(taskPersonInfoDao.findList(new TaskPersonInfo(lineEvent)));
		lineEvent.setConnectEventList(connectEventDao.findList(new ConnectEvent(lineEvent)));
		
		return lineEvent;
	}

	public List<LineEvent> findList(LineEvent lineEvent) {
		return super.findList(lineEvent);
	}

	public Page<LineEvent> findPage(Page<LineEvent> page, LineEvent lineEvent) {
		return super.findPage(page, lineEvent);
	}

	@Transactional(readOnly = false)
	public void save(LineEvent lineEvent) {
		super.save(lineEvent);
	}

	@Transactional(readOnly = false)
	public void delete(LineEvent lineEvent) {
		super.delete(lineEvent);
	}

	public List<LineEvent> getFeeds(String id) {
		LineEvent lineEvent = new LineEvent();
		lineEvent.setId(id);
		return super.dao.getFeeds(lineEvent);
	}

}