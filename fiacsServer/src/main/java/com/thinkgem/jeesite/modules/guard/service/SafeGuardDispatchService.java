/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.InformationReleaseDao;
import com.thinkgem.jeesite.modules.guard.dao.SafeGuardDispatchDao;
import com.thinkgem.jeesite.modules.guard.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SafeGuardDispatchService extends CrudService<SafeGuardDispatchDao, SafeGuardDispatch> {

	@Autowired
	private SafeGuardDispatchDao safeGuardDispatchDao;
	@Autowired
	private DispatchPersonInfoService dispatchPersonInfoService;
	@Autowired
	private DownloadPersonService downloadPersonService;
	@Autowired
	private EquipmentService equipmentService;
	public SafeGuardDispatch get(String id) {
		SafeGuardDispatch info = super.get(id);
		List<String> ids = Lists.newArrayList();
		for(Office o : info.getOfficeList()){
			ids.add(o.getId());
		}
		info.setOfficeIds(StringUtils.join(ids,","));
		return info;
	}
	
	public List<SafeGuardDispatch> findList(SafeGuardDispatch info) {
		return super.findList(info);
	}
	
	public Page<SafeGuardDispatch> findPage(Page<SafeGuardDispatch> page, SafeGuardDispatch info) {
		return super.findPage(page, info);
	}
	
	@Transactional(readOnly = false)
	public void save(SafeGuardDispatch info) {
		boolean messageType;
		if (info.getIsNewRecord()){
			//添加
			messageType=true;
		}else{
			//修改
			messageType=false;
		}

		String[] officeIds = info.getOfficeIds().split(",");
		if(messageType){
			super.save(info);
			List<DispatchPersonInfo> newPersonList = Lists.newArrayList();
			List<DispatchPersonInfo> oldPersonList = Lists.newArrayList();
			//如果是之前已派遣过的人员 重新派遣  则需要删除之前的
			for(DispatchPersonInfo d: info.getDispatchPersonInfoList()) {
				DispatchPersonInfo i = new DispatchPersonInfo();
				i.setStaffId(d.getStaffId());
				List<DispatchPersonInfo> oldStaffInfoList = dispatchPersonInfoService.findList(i);
				for (DispatchPersonInfo staffInfo : oldStaffInfoList) {
					List<String> officeIdsList = Lists.newArrayList(officeIds);
					dispatchPersonInfoService.delete(staffInfo);
					if (!officeIdsList.contains(staffInfo.getOfficeId())) {
						oldPersonList.add(staffInfo);
					}
				}
			}
			for(String officeId : officeIds){
				for(DispatchPersonInfo d: info.getDispatchPersonInfoList()){
					DispatchPersonInfo i = new DispatchPersonInfo();
					i.setStaffId(d.getStaffId());
					i.setOfficeId(officeId);
					i.setDispatchId(info.getId());
					i.setDelete(d.getDelete());
					i.setFingerNum(d.getFingerNum());
					i.setName(d.getName());
					i.setIdentifyNumber(d.getIdentifyNumber());

					dispatchPersonInfoService.save(i);
					newPersonList.add(i);
				}
			}
			this.insertDownPerson(newPersonList,oldPersonList,Lists.newArrayList(officeIds),DownloadPerson.DOWNLOAD_TYPE_ADD);
		}else{
			super.save(info);
			//删除之前的网点派遣信息
			DispatchPersonInfo queryInfo = new DispatchPersonInfo();
			queryInfo.setDispatchId(info.getId());
			//查出已有的映射关系 避免重复插入
			List<DispatchPersonInfo> oldPersonList = dispatchPersonInfoService.findList(queryInfo);

			dispatchPersonInfoService.deleteDispatch(info.getId());
			List<DispatchPersonInfo> newPersonList = Lists.newArrayList();
			List<DispatchPersonInfo> deletePersonList = Lists.newArrayList();
			for(String officeId : officeIds){
				for(DispatchPersonInfo d: info.getDispatchPersonInfoList()){
					if("0".equals(d.getDelete())){
						DispatchPersonInfo i = new DispatchPersonInfo();
						i.setOfficeId(officeId);
						i.setDispatchId(info.getId());
						i.setDelete(d.getDelete());
						i.setFingerNum(d.getFingerNum());
						i.setName(d.getName());
						i.setIdentifyNumber(d.getIdentifyNumber());
						i.setStaffId(d.getStaffId());
						dispatchPersonInfoService.save(i);
						newPersonList.add(i);
					}else{
						DispatchPersonInfo i = new DispatchPersonInfo();
						i.setOfficeId(officeId);
						i.setDispatchId(info.getId());
						i.setDelete(d.getDelete());
						i.setFingerNum(d.getFingerNum());
						i.setName(d.getName());
						i.setIdentifyNumber(d.getIdentifyNumber());
						i.setStaffId(d.getStaffId());
						deletePersonList.add(i);
					}
				}
			}
			this.insertDownPerson(newPersonList,oldPersonList,Lists.newArrayList(officeIds),DownloadPerson.DOWNLOAD_TYPE_ADD);
			this.insertDownPerson(deletePersonList,null,null,DownloadPerson.DOWNLOAD_TYPE_DEL);
		}
	}
	

	@Transactional(readOnly = false)
	public void delete(SafeGuardDispatch info) {
		DispatchPersonInfo queryInfo = new DispatchPersonInfo();
		queryInfo.setDispatchId(info.getId());
		List<DispatchPersonInfo> oldPersonList = dispatchPersonInfoService.findList(queryInfo);
		super.delete(info);
		dispatchPersonInfoService.deleteDispatch(info.getId());
		this.insertDownPerson(oldPersonList,null,null,DownloadPerson.DOWNLOAD_TYPE_DEL);
	}

	@Transactional(readOnly = false)
	public void insertDownPerson(List<DispatchPersonInfo> dispatchPersonInfoList,List<DispatchPersonInfo> oldPersonList,List<String> officeIds,String downloadType){
		//修改的时候 减少了网点 需要将减少的网点对应人员删掉
		if(oldPersonList!=null&&oldPersonList.size()>0){
			for(DispatchPersonInfo oldInfo : oldPersonList){
				if(!officeIds.contains(oldInfo.getOfficeId())){
					Equipment equipment = equipmentService.getByOfficeId(oldInfo.getOfficeId());
					if(null == equipment){
						continue;
					}
					DownloadPerson downloadPerson = new DownloadPerson();
					downloadPerson.setPersonId(oldInfo.getStaffId());
					downloadPerson.setEquipId(equipment.getId());
					downloadPerson.setIsDownload("0");
					downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
                    downloadPerson.setDownloadType(DownloadPerson.DOWNLOAD_TYPE_ADD);
                    //先删除还未同步的新增数据
                    downloadPersonService.delete1(downloadPerson);
                    downloadPerson.setDownloadType(DownloadPerson.DOWNLOAD_TYPE_DEL);
                    if(downloadPersonService.countByEntity(downloadPerson)==0){
						downloadPersonService.save(downloadPerson);
					}
				}
			}
		}
		for(DispatchPersonInfo dispatchPersonInfo : dispatchPersonInfoList){
			boolean flag = false;
			//修改的时候 避免添加多余的同步数据 进行新老数据对比
			if(oldPersonList!=null&&oldPersonList.size()>0){
				for(DispatchPersonInfo oldInfo : oldPersonList){
					//修改时候  修改的网点不在之前网点内 需要将之前网点人员删除
					if(dispatchPersonInfo.getStaffId().equals(oldInfo.getStaffId())&&dispatchPersonInfo.getOfficeId().equals(oldInfo.getOfficeId())){
						flag = true;
						break;
					}
				}
			}
			if(flag){
				continue;
			}
			Equipment equipment = equipmentService.getByOfficeId(dispatchPersonInfo.getOfficeId());
			if(null == equipment){
				continue;
			}
			DownloadPerson downloadPerson = new DownloadPerson();
			downloadPerson.setPersonId(dispatchPersonInfo.getStaffId());
			downloadPerson.setEquipId(equipment.getId());
			downloadPerson.setIsDownload("0");
			downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));
			//派遣会出现同一人员同意网点不同类型的同步数据  减少数据同步 可删除不同类型数据
			if(DownloadPerson.DOWNLOAD_TYPE_ADD.equals(downloadType)){
				downloadPerson.setDownloadType(DownloadPerson.DOWNLOAD_TYPE_DEL);
			}else{
				downloadPerson.setDownloadType(DownloadPerson.DOWNLOAD_TYPE_ADD);
			}
			downloadPersonService.delete1(downloadPerson);
			downloadPerson.setDownloadType(downloadType);
			if(downloadPersonService.countByEntity(downloadPerson)==0){
				downloadPersonService.save(downloadPerson);
			}
		}
	}
}