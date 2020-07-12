/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.guard.dao.TbAccessParametersDao;
import com.thinkgem.jeesite.modules.guard.entity.TbAccessParameters;
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadAccessParameters;

/**
 * 设备门禁参数设置Service
 * @author zgx
 * @version 2019-01-16
 */
@Service
@Transactional(readOnly = true)
public class TbAccessParametersService extends CrudService<TbAccessParametersDao, TbAccessParameters> {

	public TbAccessParameters get(String id) {
		return super.get(id);
	}
	
	public List<TbAccessParameters> findList(TbAccessParameters tbAccessParameters) {
		return super.findList(tbAccessParameters);
	}
	
	@Transactional(readOnly = false)
	public void save(TbAccessParameters tbAccessParameters) {
		super.save(tbAccessParameters);
	}
	
	@Transactional(readOnly = false)
	public void delete(TbAccessParameters tbAccessParameters) {
		super.delete(tbAccessParameters);
	}
	
	@Transactional(readOnly = false)
	public void insert(TbAccessParameters downloadPerson) {
		dao.insert(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void update(TbAccessParameters downloadPerson) {
		dao.update(downloadPerson);
	}
}