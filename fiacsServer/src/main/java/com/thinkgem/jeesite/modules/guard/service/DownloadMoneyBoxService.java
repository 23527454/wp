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
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBox;
import com.thinkgem.jeesite.modules.guard.dao.DownloadMoneyBoxDao;

/**
 * 款箱同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadMoneyBoxService extends CrudService<DownloadMoneyBoxDao, DownloadMoneyBox> {
	@Autowired
	private DownloadMoneyBoxDao downloadMoneyBoxDao;

	public DownloadMoneyBox get(String id) {
		return super.get(id);
	}
	
	public List<DownloadMoneyBox> findList(DownloadMoneyBox downloadMoneyBox) {
		return super.findList(downloadMoneyBox);
	}
	
	public Page<DownloadMoneyBox> findPage(Page<DownloadMoneyBox> page, DownloadMoneyBox downloadMoneyBox) {
		return super.findPage(page, downloadMoneyBox);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadMoneyBox downloadMoneyBox) {
		AssertUtil.assertNotNull(downloadMoneyBox.getRegisterTime(), "RegisterTime");
		super.save(downloadMoneyBox);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadMoneyBox downloadMoneyBox) {
		super.delete(downloadMoneyBox);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadMoneyBox downloadMoneyBox){
		return downloadMoneyBoxDao.countByEntity(downloadMoneyBox);
	}
}