/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.CarEventDao;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 车辆事件Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class CarEventService extends CrudService<CarEventDao, CarEvent> {

	public CarEvent get(String id) {
		return super.get(id);
	}
	
	public List<CarEvent> findList(CarEvent carEvent) {
		for (Role r : carEvent.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
				carEvent.getSqlMap().put("dsf", dataScopeFilter(carEvent.getCurrentUser(), "c", ""));
			} else {
				carEvent.getSqlMap().put("dsf", dataScopeFilter(carEvent.getCurrentUser(), "b", ""));
			}
		}
		
		
		return super.findList(carEvent);
	}
	
	public Page<CarEvent> findPage(Page<CarEvent> page, CarEvent carEvent) {
		for (Role r : carEvent.getCurrentUser().getRoleList()) {
			if ( r.getDataScope().equals("3")) {
				/*carEvent.getSqlMap().put("dsf", dataScopeFilter(carEvent.getCurrentUser(), "c", ""));*/
				carEvent.getSqlMap().put("dsf", dataScopeFilter(carEvent.getCurrentUser(), "b", ""));
			}else if(r.getDataScope().equals("2")){
				carEvent.getSqlMap().put("dsf", dataScopeFilter(carEvent.getCurrentUser(), "c", ""));
			} else {
				carEvent.getSqlMap().put("dsf", dataScopeFilter(carEvent.getCurrentUser(), "b", ""));
			}
		}
		return super.findPage(page, carEvent);
	}
	
	@Transactional(readOnly = false)
	public void save(CarEvent carEvent) {
		super.save(carEvent);
	}
	
	@Transactional(readOnly = false)
	public void delete(CarEvent carEvent) {
		super.delete(carEvent);
	}
	
	public List<CarEvent> getFeeds(String latestId, String nodes){
		CarEvent carEvent = new CarEvent();
		carEvent.setId(latestId);
		carEvent.setNodes(nodes);
		return super.dao.getFeeds(carEvent);
	}
}