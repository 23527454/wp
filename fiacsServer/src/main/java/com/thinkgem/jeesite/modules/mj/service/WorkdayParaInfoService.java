/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.mj.dao.WorkdayParaInfoDao;
import com.thinkgem.jeesite.modules.mj.entity.WorkdayParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * access_workdayService
 * @author demo
 * @version 2020-07-02
 */
@Service
@Transactional(readOnly=true)
public class WorkdayParaInfoService extends CrudService<WorkdayParaInfoDao, WorkdayParaInfo> {

	@Autowired
	private WorkdayParaInfoDao workdayParaInfoDao;

	public List<WorkdayParaInfo> findAllByEIdAndYear(WorkdayParaInfo workdayParaInfo){
		return workdayParaInfoDao.findAllByEIdAndYear(workdayParaInfo);
	}

	public Integer modifyRestDayById(WorkdayParaInfo workdayParaInfo){
		return workdayParaInfoDao.modifyRestDayById(workdayParaInfo);
	}

	public Integer deleteAllByEId(String eId){
		return workdayParaInfoDao.deleteAllByEId(eId);
	}


	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	@Override
	public WorkdayParaInfo get(String id) {
		return workdayParaInfoDao.get(id);
	}

	
	/**
	 * 保存数据（插入或更新）
	 * @param workdayParaInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(WorkdayParaInfo workdayParaInfo) {
		super.save(workdayParaInfo);
	}
	
	/**
	 * 更新状态
	 * @param accessWorkday
	 */
	/*@Override
	@Transactional(readOnly=false)
	public void updateStatus(AccessWorkday accessWorkday) {
		super.updateStatus(accessWorkday);
	}*/
	
	/**
	 * 删除数据
	 * @param workdayParaInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(WorkdayParaInfo workdayParaInfo) {
		super.delete(workdayParaInfo);
	}
	
}