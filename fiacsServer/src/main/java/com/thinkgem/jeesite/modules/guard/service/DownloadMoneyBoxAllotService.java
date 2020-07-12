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
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.dao.DownloadMoneyBoxAllotDao;

/**
 * 款箱调拨同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadMoneyBoxAllotService extends CrudService<DownloadMoneyBoxAllotDao, DownloadMoneyBoxAllot> {

	public DownloadMoneyBoxAllot get(String id) {
		return super.get(id);
	}
	
	public List<DownloadMoneyBoxAllot> findList(DownloadMoneyBoxAllot downloadMoneyBoxAllot) {
		return super.findList(downloadMoneyBoxAllot);
	}
	
	public Page<DownloadMoneyBoxAllot> findPage(Page<DownloadMoneyBoxAllot> page, DownloadMoneyBoxAllot downloadMoneyBoxAllot) {
		return super.findPage(page, downloadMoneyBoxAllot);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadMoneyBoxAllot downloadMoneyBoxAllot) {
		AssertUtil.assertNotNull(downloadMoneyBoxAllot.getRegisterTime(), "RegisterTime");
		super.save(downloadMoneyBoxAllot);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadMoneyBoxAllot downloadMoneyBoxAllot) {
		super.delete(downloadMoneyBoxAllot);
	}
	
}