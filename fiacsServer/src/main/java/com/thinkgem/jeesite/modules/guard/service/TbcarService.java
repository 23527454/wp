/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.Tbcar;
import com.thinkgem.jeesite.modules.guard.dao.TbcarDao;

/**
 * 车辆管理生成Service
 * @author Jumbo
 * @version 2017-06-21
 */
@Service
@Transactional(readOnly = true)
public class TbcarService extends CrudService<TbcarDao, Tbcar> {

	public Tbcar get(String id) {
		return super.get(id);
	}
	
	public List<Tbcar> findList(Tbcar tbcar) {
		return super.findList(tbcar);
	}
	
	public Page<Tbcar> findPage(Page<Tbcar> page, Tbcar tbcar) {
		return super.findPage(page, tbcar);
	}
	
	@Transactional(readOnly = false)
	public void save(Tbcar tbcar) {
		super.save(tbcar);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tbcar tbcar) {
		super.delete(tbcar);
	}
	
}