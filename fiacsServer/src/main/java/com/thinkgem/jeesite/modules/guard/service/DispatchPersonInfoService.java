/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.DispatchPersonInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.DispatchPersonInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.DispatchPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.DispatchPersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DispatchPersonInfoService extends CrudService<DispatchPersonInfoDao, DispatchPersonInfo> {
	
	@Autowired
	private DispatchPersonInfoDao dispatchPersonInfoDao;

	public DispatchPersonInfo get(String id) {
		DispatchPersonInfo info = super.get(id);
		return info;
	}
	
	public List<DispatchPersonInfo> findList(DispatchPersonInfo info) {
		return super.findList(info);
	}
	
	public Page<DispatchPersonInfo> findPage(Page<DispatchPersonInfo> page, DispatchPersonInfo info) {
		return super.findPage(page, info);
	}
	
	@Transactional(readOnly = false)
	public void save(DispatchPersonInfo info) {
		boolean messageType;
		if (info.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		
		super.save(info);
	}
	

	@Transactional(readOnly = false)
	public void delete(DispatchPersonInfo info) {
		super.delete(info);
	}

	@Transactional(readOnly = false)
	public int deleteDispatch(String id){
		return dispatchPersonInfoDao.deleteDispatch(id);
	}

}