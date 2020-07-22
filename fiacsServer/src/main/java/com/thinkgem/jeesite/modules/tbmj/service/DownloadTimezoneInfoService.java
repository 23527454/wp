/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.tbmj.dao.DownloadTimezoneInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadTimezoneInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 时区同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadTimezoneInfoService extends CrudService<DownloadTimezoneInfoDao, DownloadTimezoneInfo> {
	@Autowired
	private DownloadTimezoneInfoDao downloadTimezoneInfoDao;

	public DownloadTimezoneInfo get(String id) {
		return super.get(id);
	}
	
	public List<DownloadTimezoneInfo> findList(DownloadTimezoneInfo downloadTimezoneInfo) {
		return super.findList(downloadTimezoneInfo);
	}
	
	public Page<DownloadTimezoneInfo> findPage(Page<DownloadTimezoneInfo> page, DownloadTimezoneInfo downloadTimezoneInfo) {
		return super.findPage(page, downloadTimezoneInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadTimezoneInfo downloadTimezoneInfo) {
		AssertUtil.assertNotNull(downloadTimezoneInfo.getRegisterTime(), "RegisterTime");
		super.save(downloadTimezoneInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadTimezoneInfo downloadTimezoneInfo) {
		super.delete(downloadTimezoneInfo);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadTimezoneInfo downloadTimezoneInfo) {
		return super.delete1(downloadTimezoneInfo);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadTimezoneInfo downloadTimezoneInfo){
		return downloadTimezoneInfoDao.countByEntity(downloadTimezoneInfo);
	}

}