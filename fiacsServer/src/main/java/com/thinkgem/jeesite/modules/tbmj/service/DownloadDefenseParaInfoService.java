/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.tbmj.dao.DownloadDefenseParaInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadDefenseParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 防区参数同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadDefenseParaInfoService extends CrudService<DownloadDefenseParaInfoDao, DownloadDefenseParaInfo> {
	@Autowired
	private DownloadDefenseParaInfoDao downloadDefenseParaInfoDao;

	public DownloadDefenseParaInfo get(String id) {
		return super.get(id);
	}
	
	public List<DownloadDefenseParaInfo> findList(DownloadDefenseParaInfo downloadWorkdayParaInfo) {
		return super.findList(downloadWorkdayParaInfo);
	}
	
	public Page<DownloadDefenseParaInfo> findPage(Page<DownloadDefenseParaInfo> page, DownloadDefenseParaInfo downloadWorkdayParaInfo) {
		return super.findPage(page, downloadWorkdayParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadDefenseParaInfo downloadWorkdayParaInfo) {
		AssertUtil.assertNotNull(downloadWorkdayParaInfo.getRegisterTime(), "RegisterTime");
		super.save(downloadWorkdayParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadDefenseParaInfo downloadWorkdayParaInfo) {
		super.delete(downloadWorkdayParaInfo);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadDefenseParaInfo downloadWorkdayParaInfo) {
		return super.delete1(downloadWorkdayParaInfo);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadDefenseParaInfo person){
		return downloadDefenseParaInfoDao.countByEntity(person);
	}

}