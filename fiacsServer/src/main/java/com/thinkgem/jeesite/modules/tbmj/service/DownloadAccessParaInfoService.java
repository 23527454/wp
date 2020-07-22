/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.tbmj.dao.DownloadAccessParaInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadAccessParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 门禁同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadAccessParaInfoService extends CrudService<DownloadAccessParaInfoDao, DownloadAccessParaInfo> {
	@Autowired
	private DownloadAccessParaInfoDao downloadAccessParaInfoDao;

	public DownloadAccessParaInfo get(String id) {
		return super.get(id);
	}
	
	public List<DownloadAccessParaInfo> findList(DownloadAccessParaInfo downloadAccessParaInfo) {
		return super.findList(downloadAccessParaInfo);
	}
	
	public Page<DownloadAccessParaInfo> findPage(Page<DownloadAccessParaInfo> page, DownloadAccessParaInfo downloadAccessParaInfo) {
		return super.findPage(page, downloadAccessParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadAccessParaInfo downloadAccessParaInfo) {
		AssertUtil.assertNotNull(downloadAccessParaInfo.getRegisterTime(), "RegisterTime");
		super.save(downloadAccessParaInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadAccessParaInfo downloadAccessParaInfo) {
		super.delete(downloadAccessParaInfo);
	}
	@Transactional(readOnly = false)
	public int delete1(DownloadAccessParaInfo downloadAccessParaInfo) {
		return super.delete1(downloadAccessParaInfo);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadAccessParaInfo person){
		return downloadAccessParaInfoDao.countByEntity(person);
	}

}