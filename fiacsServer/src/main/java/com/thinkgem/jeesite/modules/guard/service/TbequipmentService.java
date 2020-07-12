/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.Tbequipment;
import com.thinkgem.jeesite.modules.guard.dao.TbequipmentDao;

/**
 * 网点列表表单Service
 * @author Jumbo
 * @version 2017-06-20
 */
@Service
@Transactional(readOnly = true)
public class TbequipmentService extends CrudService<TbequipmentDao, Tbequipment> {

	public Tbequipment get(String id) {
		return super.get(id);
	}
	
	public List<Tbequipment> findList(Tbequipment tbequipment) {
		return super.findList(tbequipment);
	}
	
	public Page<Tbequipment> findPage(Page<Tbequipment> page, Tbequipment tbequipment) {
		return super.findPage(page, tbequipment);
	}
	
	@Transactional(readOnly = false)
	public void save(Tbequipment tbequipment) {
		super.save(tbequipment);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tbequipment tbequipment) {
		super.delete(tbequipment);
	}
	
}