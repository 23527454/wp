/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tbmj.dao.WorkdayParaInfoDao;
import com.thinkgem.jeesite.modules.tbmj.entity.WorkdayParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

	public Integer selMaxNum(){return workdayParaInfoDao.selMaxNum();}

	public WorkdayParaInfo findAllByEIdAndYear(WorkdayParaInfo workdayParaInfo){
		return workdayParaInfoDao.findAllByEIdAndYear(workdayParaInfo);
	}

	public WorkdayParaInfo findByEIdAndDate(WorkdayParaInfo workdayParaInfo){
		return workdayParaInfoDao.findByEIdAndDate(workdayParaInfo);
	}

	public Integer modifyRestDayById(WorkdayParaInfo workdayParaInfo){
		return workdayParaInfoDao.modifyRestDayById(workdayParaInfo);
	}

	public Integer deleteAllByEId(String eId){
		return workdayParaInfoDao.deleteAllByEId(eId);
	}

	public Page<WorkdayParaInfo> findPage(Page<WorkdayParaInfo> page, WorkdayParaInfo workdayParaInfo) {

		Page<WorkdayParaInfo> workdayParaInfoPage = super.findPage(page, workdayParaInfo);

		return workdayParaInfoPage;
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