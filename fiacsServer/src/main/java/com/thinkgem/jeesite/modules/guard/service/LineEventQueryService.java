/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.LineEventQuery;
import com.thinkgem.jeesite.modules.guard.dao.LineEventQueryDao;

/**
 * 排班明细Service
 * @author Jumbo
 * @version 2017-07-20
 */
@Service
@Transactional(readOnly = true)
public class LineEventQueryService extends CrudService<LineEventQueryDao, LineEventQuery> {

	public LineEventQuery get(String id) {
		return super.get(id);
	}
	
	public List<LineEventQuery> findList(LineEventQuery lineEventQuery) {
		return super.findList(lineEventQuery);
	}
	
	public Page<LineEventQuery> findPage(Page<LineEventQuery> page, LineEventQuery lineEventQuery) {
		return super.findPage(page, lineEventQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(LineEventQuery lineEventQuery) {
		super.save(lineEventQuery);
	}
	
	@Transactional(readOnly = false)
	public void delete(LineEventQuery lineEventQuery) {
		super.delete(lineEventQuery);
	}
	
}