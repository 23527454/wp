/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.mj.dao.AccessParaInfoDao;
import com.thinkgem.jeesite.modules.mj.entity.AccessParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * access_para_infoService
 * @author demo
 * @version 2020-06-29
 */
@Service
@Transactional(readOnly=true)
public class AccessParaInfoService extends CrudService<AccessParaInfoDao, AccessParaInfo> {
	@Autowired
	private AccessParaInfoDao accessParaInfoDao;

	public List<AccessParaInfo> findAll(){
		return accessParaInfoDao.findAll();
	}


	public List<AccessParaInfo> findListById(String id){
		return accessParaInfoDao.findListById(id);
	}

	/**
	 * 查询单个数据
	 * @param id
	 * @return
	 */
	public AccessParaInfo get(String id) {
		return super.get(id);
	}

	/**
	 * 根据equipment.id查找数据
	 * @param id
	 * @return
	 */
	public List<AccessParaInfo> getByEId(String id){
		return accessParaInfoDao.getByEId(id);
	}

	/**
	 * 根据equipment.id删除数据
	 * @param id
	 * @return
	 */
	public int deleteByEId(String id){
		return accessParaInfoDao.deleteByEId(id);
	}

	/**
	 * 查询分页数据
	 * @param accessParaInfo 查询条件
	 * @return
	 */
	public Page<AccessParaInfo> findPage(Page<AccessParaInfo> page, AccessParaInfo accessParaInfo) {
		return super.findPage(page,accessParaInfo);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param accessParaInfo
	 */
	@Transactional(readOnly=false)
	public void save(AccessParaInfo accessParaInfo) {
		super.save(accessParaInfo);
	}
	
	/**
	 * 删除数据
	 * @param accessParaInfo
	 */
	@Transactional(readOnly=false)
	public void delete(AccessParaInfo accessParaInfo) {
		super.delete(accessParaInfo);
	}
	
}