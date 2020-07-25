/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tbmj.dao.AccessEventDetailDao;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessEventDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 事件详情Service
 * @author Jumbo
 * @version 2017-07-14
 */
@Service
@Transactional(readOnly = true)
public class AccessEventDetailService extends CrudService<AccessEventDetailDao, AccessEventDetail> {

	public AccessEventDetail get(String id) {
		return super.get(id);
	}
	
	public List<AccessEventDetail> findList(AccessEventDetail accessEventDetail) {
		return super.findList(accessEventDetail);
	}
	
	public Page<AccessEventDetail> findPage(Page<AccessEventDetail> page, AccessEventDetail accessEventDetail) {
		return super.findPage(page, accessEventDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(AccessEventDetail accessEventDetail) {
		super.save(accessEventDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(AccessEventDetail accessEventDetail) {
		super.delete(accessEventDetail);
	}
	
}