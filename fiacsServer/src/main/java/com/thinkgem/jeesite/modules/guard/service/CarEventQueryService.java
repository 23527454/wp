/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.CarEventQuery;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.guard.dao.CarEventQueryDao;

/**
 * 车辆事件查询Service
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class CarEventQueryService extends CrudService<CarEventQueryDao, CarEventQuery> {

	public CarEventQuery get(String id) {
		return super.get(id);
	}
	
	public List<CarEventQuery> findList(CarEventQuery carEventQuery) {
		return super.findList(carEventQuery);
	}
	
	public Page<CarEventQuery> findPage(Page<CarEventQuery> page, CarEventQuery carEventQuery) {
		for (Role r : carEventQuery.getCurrentUser().getRoleList()) {
			if ("3".equals(r.getId())) {
				if(r.getCurrentUser()!=null) {
				carEventQuery.setOffice(r.getCurrentUser().getOffice());
				}
			}else {
				
			}
		}
		return super.findPage(page, carEventQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(CarEventQuery carEventQuery) {
		super.save(carEventQuery);
	}
	
	@Transactional(readOnly = false)
	public void delete(CarEventQuery carEventQuery) {
		super.delete(carEventQuery);
	}
	
}