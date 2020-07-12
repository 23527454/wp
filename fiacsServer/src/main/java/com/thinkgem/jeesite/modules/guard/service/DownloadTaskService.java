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
import com.thinkgem.jeesite.modules.guard.entity.DownloadTask;
import com.thinkgem.jeesite.modules.guard.dao.DownloadTaskDao;

/**
 * 排班同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadTaskService extends CrudService<DownloadTaskDao, DownloadTask> {

	public DownloadTask get(String id) {
		return super.get(id);
	}
	
	public List<DownloadTask> findList(DownloadTask downloadTask) {
		return super.findList(downloadTask);
	}
	
	public Page<DownloadTask> findPage(Page<DownloadTask> page, DownloadTask downloadTask) {
		return super.findPage(page, downloadTask);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadTask downloadTask) {
		AssertUtil.assertNotNull(downloadTask.getRegisterTime(), "RegisterTime");
		super.save(downloadTask);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadTask downloadTask) {
		super.delete(downloadTask);
	}
	
}