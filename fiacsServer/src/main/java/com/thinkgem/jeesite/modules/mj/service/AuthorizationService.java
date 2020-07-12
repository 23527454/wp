/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.mj.dao.AuthorizationDao;
import com.thinkgem.jeesite.modules.mj.entity.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * power_authorizationService
 * @author demo
 * @version 2020-07-04
 */
@Service
@Transactional(readOnly=true)
public class AuthorizationService extends CrudService<AuthorizationDao, Authorization> {

	@Autowired
	private AuthorizationDao authorizationDao;

	//,accessParaInfoId,String accessParaInfoId
	public int getCountBySId(String staffId,String accessParaInfoId){
		return authorizationDao.getCountBySId(staffId,accessParaInfoId);
	}

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public Authorization get(String id) {
		Authorization authorization = super.get(id);
		return authorization;
	}

	public Page<Authorization> findPage(Page<Authorization> page, Authorization authorization) {

		Page<Authorization> authorizationPage = super.findPage(page, authorization);

		return authorizationPage;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param authorization
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Authorization authorization) {
		super.save(authorization);
	}
	
	/**
	 * 更新状态
	 * @param powerAuthorization
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void updateStatus(PowerAuthorization powerAuthorization) {
		super.updateStatus(powerAuthorization);
	}*/

	/**
	 * 删除数据
	 * @param authorization
	 */
	@Transactional(readOnly=false)
	public int deleteById(Authorization authorization) {
		return authorizationDao.delete(authorization);
	}

	/**
	 * 删除数据2
	 * @param id
	 */
	@Transactional(readOnly=false)
	public int deleteByAId(String id) {
		return authorizationDao.deleteByAId(id);
	}
	
}