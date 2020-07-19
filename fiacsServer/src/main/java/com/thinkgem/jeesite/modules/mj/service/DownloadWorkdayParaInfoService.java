/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.mj.dao.DownloadWorkdayParaInfoDao;
import com.thinkgem.jeesite.modules.mj.entity.DownloadWorkdayParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 工作日同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadWorkdayParaInfoService extends CrudService<DownloadWorkdayParaInfoDao, DownloadWorkdayParaInfo> {
	@Autowired
	private DownloadWorkdayParaInfoDao downloadWorkdayParaInfoDao;

	public DownloadWorkdayParaInfo get(String id) {
		return super.get(id);
	}
	
	public List<DownloadWorkdayParaInfo> findList(DownloadWorkdayParaInfo downloadWorkdayParaInfo) {
		return super.findList(downloadWorkdayParaInfo);
	}
	
	public Page<DownloadWorkdayParaInfo> findPage(Page<DownloadWorkdayParaInfo> page, DownloadWorkdayParaInfo downloadWorkdayParaInfo) {
		return super.findPage(page, downloadWorkdayParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadWorkdayParaInfo downloadWorkdayParaInfo) {
		AssertUtil.assertNotNull(downloadWorkdayParaInfo.getRegisterTime(), "RegisterTime");
		super.save(downloadWorkdayParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadWorkdayParaInfo downloadWorkdayParaInfo) {
		super.delete(downloadWorkdayParaInfo);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadWorkdayParaInfo downloadWorkdayParaInfo) {
		return super.delete1(downloadWorkdayParaInfo);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadWorkdayParaInfo person){
		return downloadWorkdayParaInfoDao.countByEntity(person);
	}

}