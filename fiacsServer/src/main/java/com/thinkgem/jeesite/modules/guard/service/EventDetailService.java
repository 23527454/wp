/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;

/**
 * 事件详情Service
 * @author Jumbo
 * @version 2017-07-14
 */
@Service
@Transactional(readOnly = true)
public class EventDetailService extends CrudService<EventDetailDao, EventDetail> {

	public EventDetail get(String id) {
		return super.get(id);
	}
	
	public List<EventDetail> findList(EventDetail eventDetail) {
		return super.findList(eventDetail);
	}
	
	public Page<EventDetail> findPage(Page<EventDetail> page, EventDetail eventDetail) {
		return super.findPage(page, eventDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(EventDetail eventDetail) {
		super.save(eventDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(EventDetail eventDetail) {
		super.delete(eventDetail);
	}
	
}