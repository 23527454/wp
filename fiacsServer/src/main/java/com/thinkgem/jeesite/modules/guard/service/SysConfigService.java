/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.SysConfigDao;
import com.thinkgem.jeesite.modules.guard.entity.SysConfig;

/**
 * 系统配置Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class SysConfigService extends CrudService<SysConfigDao, SysConfig> {

	public List<SysConfig> findList(SysConfig sysConfig) {
		return super.findList(sysConfig);
	}
	
	/**
	 * 专员数量
	 * @return
	 */
	public String getInterNum(){
		SysConfig sysConfig  = new SysConfig();
		sysConfig.setAttribute("nInterNum");
		SysConfig persist =  super.findOne(sysConfig);
		return persist.getValue();
	}
	/**
	 * 押款员数量
	 * @return
	 */
	public String getSuperGoNum(){
		SysConfig sysConfig  = new SysConfig();
		sysConfig.setAttribute("nSuperGoNum");
		SysConfig persist =  super.findOne(sysConfig);
		return persist.getValue();
		
	}
	
	
	/**
	 * 专员数量
	 * @return
	 */
	public int getInterNumInt(){
		SysConfig sysConfig  = new SysConfig();
		sysConfig.setAttribute("nInterNum");
		SysConfig persist =  super.findOne(sysConfig);
		if(null== persist.getValue()){
			return 0;
		}else{
			return Integer.parseInt(persist.getValue());
		}
	}
	/**
	 * 押款员数量
	 * @return
	 */
	public int getSuperGoNumInt(){
		SysConfig sysConfig  = new SysConfig();
		sysConfig.setAttribute("nSuperGoNum");
		SysConfig persist =  super.findOne(sysConfig);
		if(null== persist.getValue()){
			return 0;
		}else{
			return Integer.parseInt(persist.getValue());
		}
	}
	/**
	 * 款箱派送/回收切割时间
	 * @return
	 */
	public String getDtTaskCut(){
		SysConfig sysConfig  = new SysConfig();
		sysConfig.setAttribute("dt_task_cut");
		SysConfig persist =  super.findOne(sysConfig);
		return persist.getValue();
	}
	
	@Transactional(readOnly = false)
	public void save(SysConfig sysConfig) {
		for (int i = 1; i <= 16; i++) {
			if(i==1){
				sysConfig.setAttribute("corporation_name");
				sysConfig.setValue(sysConfig.getCorporationName());
			}else if(i==2){
				sysConfig.setAttribute("system_name");
				sysConfig.setValue(sysConfig.getSystemName());
			}else if(i==3){
				sysConfig.setAttribute("nSuperGoNum");
				sysConfig.setValue(sysConfig.getSuperGoNum());
			}else if(i==4){
				sysConfig.setAttribute("nInterNum");
				sysConfig.setValue(sysConfig.getInterNum());
			}else if(i==5){
				sysConfig.setAttribute("reset_upload_connect_items");
				sysConfig.setValue(sysConfig.getResetUploadConnectItems());
			}else if(i==6){
				sysConfig.setAttribute("reset_upload_alarm_items");
				sysConfig.setValue(sysConfig.getResetUploadAlarmItems());
			}else if(i==7){
				sysConfig.setAttribute("sync_time1");
				sysConfig.setValue(sysConfig.getSyncTime1());
			}else if(i==8){
				sysConfig.setAttribute("sync_time2");
				sysConfig.setValue(sysConfig.getSyncTime2());
			}else if(i==9){
				sysConfig.setAttribute("sync_time3");
				sysConfig.setValue(sysConfig.getSyncTime3());
			}else if(i==10){
				sysConfig.setAttribute("is_dog_run");
				sysConfig.setValue(sysConfig.getIsDogRun());
			}else if(i==11){
				sysConfig.setAttribute("is_lock_protect");
				sysConfig.setValue(sysConfig.getIsLockProtect());
			}else if(i==12){
				sysConfig.setAttribute("is_auto_login");
				sysConfig.setValue(sysConfig.getIsAutoLogin());
			}else if(i==13){
				sysConfig.setAttribute("auto_open_when_start_computer");
				sysConfig.setValue(sysConfig.getAutoOpenWhenStartComputer());
			}else if(i==14){
				sysConfig.setAttribute("version_number");
				sysConfig.setValue(sysConfig.getVersionNumber());
			}else if(i == 15){
				sysConfig.setAttribute("dt_task_cut");
				sysConfig.setValue(sysConfig.getDtTaskCut());
			}else if(i == 16){
				sysConfig.setAttribute("staff_validity");
				sysConfig.setValue(sysConfig.getStaffValidity());
			}
			saveOrUpdate(sysConfig);
		}
	}

	private void saveOrUpdate(SysConfig sysConfig) {
		SysConfig persist =  super.findOne(sysConfig);
		if(null != persist){
			sysConfig.preUpdate();
			dao.update(sysConfig);
		}else{
			dao.insert(sysConfig);
		}
	}
	
}