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
import com.thinkgem.jeesite.modules.guard.dao.TbDownloadAccessParametersDao;
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadAccessParameters;

/**
  * 门禁参数信息Service
 * @author zgx
 * @version 2019-01-18
 */
@Service
@Transactional(readOnly = true)
public class TbDownloadAccessParametersService extends CrudService<TbDownloadAccessParametersDao, TbDownloadAccessParameters> {

	public TbDownloadAccessParameters get(String id) {
		return super.get(id);
	}
	
	public List<TbDownloadAccessParameters> findList(TbDownloadAccessParameters downloadPerson) {
		return super.findList(downloadPerson);
	}
	
	public Page<TbDownloadAccessParameters> findPage(Page<TbDownloadAccessParameters> page, TbDownloadAccessParameters downloadPerson) {
	/*	for (Role r : downloadPerson.getCurrentUser().getRoleList()) {
			if (r.getDataScope().equals("3")) {
				downloadPerson.getSqlMap().put("dsf", dataScopeFilter(downloadPerson.getCurrentUser(), "b", ""));
				//downloadPerson.getSqlMap().put("dsf", dataScopeFilter(downloadPerson.getCurrentUser(), "h", ""));
			} else if(r.getDataScope().equals("2")){
				downloadPerson.getSqlMap().put("dsf", dataScopeFilter(downloadPerson.getCurrentUser(), "h", ""));
			}else {
				downloadPerson.getSqlMap().put("dsf", dataScopeFilter(downloadPerson.getCurrentUser(), "b", ""));
			}
		}*/
		return super.findPage(page, downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void save(TbDownloadAccessParameters downloadPerson) {
		AssertUtil.assertNotNull(downloadPerson.getRegisterTime(), "RegisterTime");
		super.save(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void insert(TbDownloadAccessParameters downloadPerson) {
		AssertUtil.assertNotNull(downloadPerson.getRegisterTime(), "RegisterTime");
		dao.insert(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void update(TbDownloadAccessParameters downloadPerson) {
		dao.update(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void delete(TbDownloadAccessParameters downloadPerson) {
		super.delete(downloadPerson);
	}
	@Transactional(readOnly = false)
	public int delete1(TbDownloadAccessParameters downloadPerson) {
		return super.delete1(downloadPerson);
	}
	
}