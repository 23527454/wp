/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tbmj.dao.AccessEventInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessEventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * tbmj_access_event_infoService
 * @author demo
 * @version 2020-07-25
 */
@Service
@Transactional(readOnly=true)
public class AccessEventInfoService extends CrudService<AccessEventInfoDao, AccessEventInfo> {

	@Autowired
	private AccessEventInfoDao accessEventInfoDao;



	public List<AccessEventInfo> getFeeds(String latestId, String nodes){
		AccessEventInfo accessEventInfo = new AccessEventInfo();
		accessEventInfo.setId(latestId);
		accessEventInfo.setNodes(nodes);
		return super.dao.getFeeds(accessEventInfo);
	}

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	@Override
	public AccessEventInfo get(String id) {
		return super.get(id);
	}

	/**
	 * 查询分页数据
	 * @param accessEventInfo 查询条件
	 * @param page 分页对象
	 * @return
	 */
	@Override
	public Page<AccessEventInfo> findPage(Page<AccessEventInfo> page, AccessEventInfo accessEventInfo) {
		return super.findPage(page,accessEventInfo);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param accessEventInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AccessEventInfo accessEventInfo) {
		super.save(accessEventInfo);
	}

	/**
	 * 更新状态
	 * @param accessEventInfo
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void updateStatus(AccessEventInfo accessEventInfo) {
		super.updateStatus(accessEventInfo);
	}*/

	/**
	 * 删除数据
	 * @param accessEventInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AccessEventInfo accessEventInfo) {
		super.delete(accessEventInfo);
	}

}