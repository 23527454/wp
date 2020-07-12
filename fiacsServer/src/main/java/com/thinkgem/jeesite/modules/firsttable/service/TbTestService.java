/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.firsttable.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.firsttable.entity.TbTest;
import com.thinkgem.jeesite.modules.firsttable.dao.TbTestDao;

/**
 * 测试Service
 * @author 张胜利
 * @version 2020-06-12
 */
@Service
@Transactional(readOnly = true)
public class TbTestService extends CrudService<TbTestDao, TbTest> {

	public TbTest get(String id) {
		return super.get(id);
	}
	
	public List<TbTest> findList(TbTest tbTest) {
		return super.findList(tbTest);
	}
	
	public Page<TbTest> findPage(Page<TbTest> page, TbTest tbTest) {
		return super.findPage(page, tbTest);
	}
	
	@Transactional(readOnly = false)
	public void save(TbTest tbTest) {
		super.save(tbTest);
	}
	
	@Transactional(readOnly = false)
	public void delete(TbTest tbTest) {
		super.delete(tbTest);
	}
	
}