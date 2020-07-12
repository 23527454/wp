/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.dao.CarDao;
import com.thinkgem.jeesite.modules.guard.dao.InformationReleaseDao;
import com.thinkgem.jeesite.modules.guard.entity.*;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@Service
@Transactional(readOnly = true)
public class InformationReleaseService extends CrudService<InformationReleaseDao, InformationRelease> {

	@Autowired
	private InformationReleaseDao informationReleaseDao;
	@Autowired
	private EquipmentService equipmentService;

	public InformationRelease get(String id) {
		InformationRelease info = super.get(id);
		return info;
	}
	
	public List<InformationRelease> findList(InformationRelease info) {
		return super.findList(info);
	}
	
	public Page<InformationRelease> findPage(Page<InformationRelease> page, InformationRelease info) {
		//car.getSqlMap().put("dsf", dataScopeFilterArea(car.getCurrentUser(), "a4", "u"));
		return super.findPage(page, info);
	}
	
	@Transactional(readOnly = false)
	public void save(InformationRelease info) {
		boolean messageType;
		if (info.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}
		
		super.save(info);
		this.insertDownload(info,messageType);
	}
	

	@Transactional(readOnly = false)
	public void delete(InformationRelease info) {
		super.delete(info);
		informationReleaseDao.deleteDownload(info.getId());
	}

	private void insertDownload(InformationRelease info,boolean messageType){
		if(!messageType){
			informationReleaseDao.deleteDownload(info.getId());
		}
		String[] officeIds = info.getScope().split(",");
		for(String officeId : officeIds){
			Equipment equip = equipmentService.getByOfficeId(officeId);
			if(equip==null){
				continue;
			}
			info.setEquipId(equip.getId());

			informationReleaseDao.insertDownload(info);
		}
	}
}