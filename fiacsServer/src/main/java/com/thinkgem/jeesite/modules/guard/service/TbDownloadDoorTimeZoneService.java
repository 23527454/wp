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
import com.thinkgem.jeesite.modules.guard.dao.TbDownloadDoorTimeZoneDao;
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadAccessParameters;
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadDoorTimeZone;

/**
 * 门禁定时信息Service
 * @author zgx
 * @version 2019-01-18
 */
@Service
@Transactional(readOnly = true)
public class TbDownloadDoorTimeZoneService extends CrudService<TbDownloadDoorTimeZoneDao, TbDownloadDoorTimeZone> {

	public TbDownloadDoorTimeZone get(String id) {
		return super.get(id);
	}
	
	public List<TbDownloadDoorTimeZone> findList(TbDownloadDoorTimeZone downloadPerson) {
		return super.findList(downloadPerson);
	}
	
	public Page<TbDownloadDoorTimeZone> findPage(Page<TbDownloadDoorTimeZone> page, TbDownloadDoorTimeZone downloadPerson) {
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
	public void save(TbDownloadDoorTimeZone downloadPerson) {
		AssertUtil.assertNotNull(downloadPerson.getRegisterTime(), "RegisterTime");
		super.save(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void delete(TbDownloadDoorTimeZone downloadPerson) {
		super.delete(downloadPerson);
	}
	@Transactional(readOnly = false)
	public int delete1(TbDownloadDoorTimeZone downloadPerson) {
		return super.delete1(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void insert(TbDownloadDoorTimeZone downloadPerson) {
		AssertUtil.assertNotNull(downloadPerson.getRegisterTime(), "RegisterTime");
		dao.insert(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void update(TbDownloadDoorTimeZone downloadPerson) {
		dao.update(downloadPerson);
	}
	
}