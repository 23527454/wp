/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.guard.entity.DownloadPerson;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.guard.dao.DownloadPersonDao;

/**
 * 人员同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadPersonService extends CrudService<DownloadPersonDao, DownloadPerson> {
	@Autowired
	private DownloadPersonDao downloadPersonDao;

	public DownloadPerson get(String id) {
		return super.get(id);
	}
	
	public List<DownloadPerson> findList(DownloadPerson downloadPerson) {
		return super.findList(downloadPerson);
	}
	
	public Page<DownloadPerson> findPage(Page<DownloadPerson> page, DownloadPerson downloadPerson) {
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
	public void save(DownloadPerson downloadPerson) {
		AssertUtil.assertNotNull(downloadPerson.getRegisterTime(), "RegisterTime");
		super.save(downloadPerson);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadPerson downloadPerson) {
		super.delete(downloadPerson);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadPerson downloadPerson) {
		return super.delete1(downloadPerson);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadPerson person){
		return downloadPersonDao.countByEntity(person);
	}

}