/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.mj.dao.DefenseParaInfoDao;
import com.thinkgem.jeesite.modules.mj.entity.DefenseParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * access_defense_infoService
 * @author demo
 * @version 2020-07-03
 */
@Service
@Transactional(readOnly=true)
public class DefenseParaInfoService extends CrudService<DefenseParaInfoDao, DefenseParaInfo> {

	@Autowired
	private DefenseParaInfoDao defenseParaInfoDao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	@Override
	public DefenseParaInfo get(String id) {
		return defenseParaInfoDao.get(id);
	}


	public List<DefenseParaInfo> getByEId(String id){
		return defenseParaInfoDao.getByEId(id);
	}

	public int deleteByEId(String id){
		return defenseParaInfoDao.deleteByEId(id);
	}

	/**
	 * 查询分页数据
	 * @param accessDefenseInfo 查询条件
	 * @param accessDefenseInfo.page 分页对象
	 * @return
	 */
	/*@Override
	public Page<AccessDefenseInfo> findPage(AccessDefenseInfo accessDefenseInfo) {
		return super.findPage(accessDefenseInfo);
	}*/
	
	/**
	 * 保存数据（插入或更新）
	 * @param defenseParaInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(DefenseParaInfo defenseParaInfo) {
		super.save(defenseParaInfo);
	}
	
	/**
	 * 更新状态
	 * @param accessDefenseInfo
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void updateStatus(AccessDefenseInfo accessDefenseInfo) {
		super.updateStatus(accessDefenseInfo);
	}*/
	
	/**
	 * 删除数据
	 * @param accessDefenseInfo
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void delete(AccessDefenseInfo accessDefenseInfo) {
		super.delete(accessDefenseInfo);
	}*/
	
}