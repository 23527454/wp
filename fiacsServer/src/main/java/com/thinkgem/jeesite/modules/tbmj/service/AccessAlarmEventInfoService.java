/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tbmj.dao.AccessAlarmEventInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessAlarmEventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * tbmj_access_event_infoService
 * @author demo
 * @version 2020-07-25
 */
@Service
@Transactional(readOnly=true)
public class AccessAlarmEventInfoService extends CrudService<AccessAlarmEventInfoDao, AccessAlarmEventInfo> {

	@Autowired
	private AccessAlarmEventInfoDao accessAlarmEventInfoDao;

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	@Override
	public AccessAlarmEventInfo get(String id) {
		return super.get(id);
	}

	/**
	 * 查询分页数据
	 * @param accessAlarmEventInfo 查询条件
	 * @param page 分页对象
	 * @return
	 */
	@Override
	public Page<AccessAlarmEventInfo> findPage(Page<AccessAlarmEventInfo> page, AccessAlarmEventInfo accessAlarmEventInfo) {
		return super.findPage(page,accessAlarmEventInfo);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param accessAlarmEventInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AccessAlarmEventInfo accessAlarmEventInfo) {
		super.save(accessAlarmEventInfo);
	}

	/**
	 * 更新状态
	 * @param accessAlarmEventInfo
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void updateStatus(AccessAlarmEventInfo accessAlarmEventInfo) {
		super.updateStatus(accessAlarmEventInfo);
	}*/

	/**
	 * 删除数据
	 * @param accessAlarmEventInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AccessAlarmEventInfo accessAlarmEventInfo) {
		super.delete(accessAlarmEventInfo);
	}

}