/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.guard.dao.TbDoorTimeZoneDao;
import com.thinkgem.jeesite.modules.guard.entity.TbAccessParameters;
import com.thinkgem.jeesite.modules.guard.entity.TbDoorTimeZone;
import com.thinkgem.jeesite.modules.guard.entity.TbDoorTimeZone;

/**
 * 设备门禁参数设置Service
 * @author zgx
 * @version 2019-01-16
 */
@Service
@Transactional(readOnly = true)
public class TbDoorTimeZoneService extends CrudService<TbDoorTimeZoneDao, TbDoorTimeZone> {

	public TbDoorTimeZone get(String id) {
		return super.get(id);
	}
	
	public List<TbDoorTimeZone> findList(TbDoorTimeZone TbDoorTimeZone) {
		return super.findList(TbDoorTimeZone);
	}
	
	@Transactional(readOnly = false)
	public void save(TbDoorTimeZone TbDoorTimeZone) {
		super.save(TbDoorTimeZone);
	}
	
	@Transactional(readOnly = false)
	public void delete(TbDoorTimeZone TbDoorTimeZone) {
		super.delete(TbDoorTimeZone);
	}
	
	@Transactional(readOnly = false)
	public void insert(TbDoorTimeZone downloadPerson) {
		dao.insert(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void update(TbDoorTimeZone downloadPerson) {
		dao.update(downloadPerson);
	}
	
}