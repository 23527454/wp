/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tbmj.dao.SecurityParaInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.SecurityParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * access_antitheftService
 * @author demo
 * @version 2020-07-02
 */
@Service
@Transactional(readOnly=true)
public class SecurityParaInfoService extends CrudService<SecurityParaInfoDao, SecurityParaInfo> {

	@Autowired
	private SecurityParaInfoDao securityParaInfoDao;

	public List<SecurityParaInfo> getByEId(String id){
		return securityParaInfoDao.getByEId(id);
	}

	public int deleteByEId(String id){
		return securityParaInfoDao.deleteByEId(id);
	}

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public SecurityParaInfo get(String id) {
		return securityParaInfoDao.get(id);
	}
	
	/**
	 * 查询分页数据
	 * @param accessAntitheft 查询条件
	 * @param accessAntitheft.page 分页对象
	 * @return
	 */
	/*@Override
	public Page<AccessAntitheft> findPage(AccessAntitheft accessAntitheft) {
		return super.findPage(accessAntitheft);
	}*/
	
	/**
	 * 保存数据（插入或更新）
	 * @param securityParaInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(SecurityParaInfo securityParaInfo) {
		super.save(securityParaInfo);
	}
	
	/**
	 * 更新状态
	 * @param accessAntitheft
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void updateStatus(AccessAntitheft accessAntitheft) {
		super.updateStatus(accessAntitheft);
	}*/
	
	/**
	 * 删除数据
	 * @param securityParaInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(SecurityParaInfo securityParaInfo) {
		super.delete(securityParaInfo);
	}
	
}