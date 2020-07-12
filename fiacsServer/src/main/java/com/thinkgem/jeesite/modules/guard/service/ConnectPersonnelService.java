/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.entity.ConnectPersonnel;
import com.thinkgem.jeesite.modules.guard.dao.ConnectPersonnelDao;
import com.thinkgem.jeesite.modules.guard.dao.FingerInfoDao;

/**
 * 人员交接明细Service
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class ConnectPersonnelService extends CrudService<ConnectPersonnelDao, ConnectPersonnel> {

	@Autowired
	private ConnectPersonnelDao connectPersonnelDao;
	
	public ConnectPersonnel get(String id) {
		return super.get(id);
	}

	public ConnectPersonnel getTwo(@Param("id")String id, @Param("staff_id")String staff_id) {
		return connectPersonnelDao.getTwo(id, staff_id);
	}

	public List<ConnectPersonnel> findList(ConnectPersonnel connectPersonnel) {
		return super.findList(connectPersonnel);
	}

	public Page<ConnectPersonnel> findPage(Page<ConnectPersonnel> page, ConnectPersonnel connectPersonnel) {
		return super.findPage(page, connectPersonnel);
	}

	@Transactional(readOnly = false)
	public void save(ConnectPersonnel connectPersonnel) {
		super.save(connectPersonnel);
	}

	@Transactional(readOnly = false)
	public void delete(ConnectPersonnel connectPersonnel) {
		super.delete(connectPersonnel);
	}

}