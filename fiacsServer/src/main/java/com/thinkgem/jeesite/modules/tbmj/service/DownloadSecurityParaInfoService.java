/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.tbmj.dao.DownloadSecurityParaInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadSecurityParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 防盗参数同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadSecurityParaInfoService extends CrudService<DownloadSecurityParaInfoDao, DownloadSecurityParaInfo> {
	@Autowired
	private DownloadSecurityParaInfoDao downloadSecurityParaInfoDao;

	public DownloadSecurityParaInfo get(String id) {
		return super.get(id);
	}
	
	public List<DownloadSecurityParaInfo> findList(DownloadSecurityParaInfo downloadSecurityParaInfo) {
		return super.findList(downloadSecurityParaInfo);
	}
	
	public Page<DownloadSecurityParaInfo> findPage(Page<DownloadSecurityParaInfo> page, DownloadSecurityParaInfo downloadSecurityParaInfo) {
		return super.findPage(page, downloadSecurityParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadSecurityParaInfo downloadSecurityParaInfo) {
		AssertUtil.assertNotNull(downloadSecurityParaInfo.getRegisterTime(), "RegisterTime");
		super.save(downloadSecurityParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadSecurityParaInfo downloadSecurityParaInfo) {
		super.delete(downloadSecurityParaInfo);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadSecurityParaInfo downloadSecurityParaInfo) {
		return super.delete1(downloadSecurityParaInfo);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadSecurityParaInfo person){
		return downloadSecurityParaInfoDao.countByEntity(person);
	}

}