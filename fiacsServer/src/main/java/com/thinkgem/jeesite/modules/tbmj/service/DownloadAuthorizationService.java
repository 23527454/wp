/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.tbmj.dao.DownloadAuthorizationDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadAuthorizationService extends CrudService<DownloadAuthorizationDao, DownloadAuthorization> {
	@Autowired
	private DownloadAuthorizationDao downloadAuthorizationDao;

	public DownloadAuthorization get(String id) {
		return super.get(id);
	}
	
	public List<DownloadAuthorization> findList(DownloadAuthorization downloadAuthorization) {
		return super.findList(downloadAuthorization);
	}
	
	public Page<DownloadAuthorization> findPage(Page<DownloadAuthorization> page, DownloadAuthorization downloadAuthorization) {
		return super.findPage(page, downloadAuthorization);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadAuthorization downloadAuthorization) {
		AssertUtil.assertNotNull(downloadAuthorization.getRegisterTime(), "RegisterTime");
		super.save(downloadAuthorization);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadAuthorization downloadAuthorization) {
		super.delete(downloadAuthorization);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadAuthorization downloadAuthorization) {
		return super.delete1(downloadAuthorization);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadAuthorization person){
		return downloadAuthorizationDao.countByEntity(person);
	}

}