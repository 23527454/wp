/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.Tbmoneylockers;
import com.thinkgem.jeesite.modules.guard.dao.TbmoneylockersDao;

/**
 * 款箱管理表单Service
 * @author Jumbo
 * @version 2017-06-26
 */
@Service
@Transactional(readOnly = true)
public class TbmoneylockersService extends CrudService<TbmoneylockersDao, Tbmoneylockers> {

	public Tbmoneylockers get(String id) {
		return super.get(id);
	}
	
	public List<Tbmoneylockers> findList(Tbmoneylockers tbmoneylockers) {
		return super.findList(tbmoneylockers);
	}
	
	public Page<Tbmoneylockers> findPage(Page<Tbmoneylockers> page, Tbmoneylockers tbmoneylockers) {
		return super.findPage(page, tbmoneylockers);
	}
	
	@Transactional(readOnly = false)
	public void save(Tbmoneylockers tbmoneylockers) {
		super.save(tbmoneylockers);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tbmoneylockers tbmoneylockers) {
		super.delete(tbmoneylockers);
	}
	
}