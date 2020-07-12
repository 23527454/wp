/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.dao.DownloadCarDao;

import javax.xml.ws.soap.Addressing;

/**
 * 车辆同步信息Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class DownloadCarService extends CrudService<DownloadCarDao, DownloadCar> {
	@Autowired
	private DownloadCarDao downloadCarDao;

	public DownloadCar get(String id) {
		return super.get(id);
	}
	
	public List<DownloadCar> findList(DownloadCar downloadCar) {
		return super.findList(downloadCar);
	}
	
	public Page<DownloadCar> findPage(Page<DownloadCar> page, DownloadCar downloadCar) {
		return super.findPage(page, downloadCar);
	}
	
	@Transactional(readOnly = false)
	public void save(DownloadCar downloadCar) {
		AssertUtil.assertNotNull(downloadCar.getRegisterTime(), "RegisterTime");
		super.save(downloadCar);
	}
	
	@Transactional(readOnly = false)
	public void delete(DownloadCar downloadCar) {
		super.delete(downloadCar);
	}

	@Transactional(readOnly = true)
	public int countByEntity(DownloadCar downloadCar){
		return downloadCarDao.countByEntity(downloadCar);
	}
}