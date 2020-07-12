/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfo;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.guard.dao.TaskScheduleInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.DownloadPerson;
import com.thinkgem.jeesite.modules.guard.entity.DownloadTask;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.TaskCarInfo;
import com.thinkgem.jeesite.modules.guard.dao.TaskCarInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.TaskLineInfo;
import com.thinkgem.jeesite.modules.guard.dao.TaskLineInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.TaskPersonInfo;
import com.thinkgem.jeesite.modules.guard.dao.TaskPersonInfoDao;

/**
 * 排班信息Service
 * 
 * @author Junbo
 * @version 2017-08-01
 */
@Service
@Transactional(readOnly = true)
public class TaskScheduleInfoService extends CrudService<TaskScheduleInfoDao, TaskScheduleInfo> {

	@Autowired
	private TaskCarInfoDao taskCarInfoDao;
	@Autowired
	private TaskLineInfoDao taskLineInfoDao;
	@Autowired
	private TaskPersonInfoDao taskPersonInfoDao;
	@Autowired
	private DownloadTaskService downloadTaskService;
	@Autowired
	private DownloadPersonService downloadPersonService;
	@Autowired
	private DownloadCarService downloadCarService;
	public TaskScheduleInfo get(String id) {
		TaskScheduleInfo taskScheduleInfo = super.get(id);
		taskScheduleInfo.setTaskCarInfoList(taskCarInfoDao.findList(new TaskCarInfo(taskScheduleInfo)));
		taskScheduleInfo.setTaskLineInfoList(taskLineInfoDao.findList(new TaskLineInfo(taskScheduleInfo)));
		taskScheduleInfo.setTaskPersonInfoList(taskPersonInfoDao.findList(new TaskPersonInfo(taskScheduleInfo)));
		return taskScheduleInfo;
	}

	public List<TaskScheduleInfo> findList(TaskScheduleInfo taskScheduleInfo) {
		taskScheduleInfo.getSqlMap().put("dsf", dataScopeFilterArea(taskScheduleInfo.getCurrentUser(), "d", "u"));
		return super.findList(taskScheduleInfo);
	}

	public Page<TaskScheduleInfo> findPage(Page<TaskScheduleInfo> page, TaskScheduleInfo taskScheduleInfoRequest) {
		taskScheduleInfoRequest.getSqlMap().put("dsf", dataScopeFilterArea(taskScheduleInfoRequest.getCurrentUser(), "d", "u"));
		Page<TaskScheduleInfo> pageTaskSchedule = super.findPage(page, taskScheduleInfoRequest);
		
		for (TaskScheduleInfo taskScheduleInfo : pageTaskSchedule.getList()) {
			taskScheduleInfo.setTaskCarInfoList(taskCarInfoDao.findList(new TaskCarInfo(taskScheduleInfo)));
			taskScheduleInfo.setTaskPersonInfoList( taskPersonInfoDao.findList(new TaskPersonInfo(taskScheduleInfo)));
			taskScheduleInfo.setTaskLineInfoList(taskLineInfoDao.findList(new TaskLineInfo(taskScheduleInfo)));
		}
		return pageTaskSchedule;
	}

	@Transactional(readOnly = false)
	public void save(TaskScheduleInfo taskScheduleInfo) {
		
		boolean messageType;
		if (taskScheduleInfo.getIsNewRecord()){
			//添加
			messageType=true;
			taskScheduleInfo.setAllotDate(DateUtils.getDate());
			taskScheduleInfo.setAllotTime(DateUtils.getTime());
		}else{
			//修改
			messageType=false;
		}
		super.save(taskScheduleInfo);
		if(messageType){
			LogUtils.saveLog(Servlets.getRequest(), "新增排班:["+taskScheduleInfo.getId()+"]");
		}else{
			LogUtils.saveLog(Servlets.getRequest(), "修改排班:["+taskScheduleInfo.getId()+"]");
		}
		
		taskCarInfoDao.delete(taskScheduleInfo.getId());
		for (TaskCarInfo taskCarInfo : taskScheduleInfo.getTaskCarInfoList()) {
			if (taskCarInfo.getCarId() == null || taskCarInfo.getCarId() == "") {
				continue;
			}
			if (TaskCarInfo.DEL_FLAG_NORMAL.equals(taskCarInfo.getDelete())) {
				taskCarInfo.setTaskScheduleId(taskScheduleInfo.getId());
				taskCarInfo.preInsert();
				taskCarInfoDao.insert(taskCarInfo);
			}
		}

		taskPersonInfoDao.delete(taskScheduleInfo.getId());
		for (TaskPersonInfo taskPersonInfo : taskScheduleInfo.getTaskPersonInfoList()) {
			if (taskPersonInfo.getPersonId() == null || taskPersonInfo.getPersonId() == "") {
				continue;
			}
			if (TaskPersonInfo.DEL_FLAG_NORMAL.equals(taskPersonInfo.getDelete())) {
				taskPersonInfo.setTaskScheduleId(taskScheduleInfo.getId());
				taskPersonInfo.preInsert();
				taskPersonInfoDao.insert(taskPersonInfo);
			}
		}

		taskLineInfoDao.delete(taskScheduleInfo.getId());
		for (TaskLineInfo taskLineInfo : taskScheduleInfo.getTaskLineInfoList()) {
			if (taskLineInfo.getEquipmentId() == null || taskLineInfo.getEquipmentId() == "") {
				continue;
			}
			if (TaskLineInfo.DEL_FLAG_NORMAL.equals(taskLineInfo.getDelete())) {
				taskLineInfo.setTaskScheduleId(taskScheduleInfo.getId());
				taskLineInfo.preInsert();
				taskLineInfoDao.insert(taskLineInfo);
			}
		}
		//如果是贵金属派送需要同步任务到线路指定的网点, add:20171227， lzw
		/*if(TaskScheduleInfo.TASK_TYPE_GUIJINSHUPAISONG.equals(taskScheduleInfo.getTaskType())){
			this.insentDownload(taskScheduleInfo, DownloadEntity.DOWNLOAD_TYPE_ADD);
		}*/
	}

	// 下载和同步时进行
	@Transactional(readOnly = false)
	public void insentDownload(TaskScheduleInfo task, String type) {
		TaskScheduleInfo taskScheduleInfo;
		if (type.equals(DownloadEntity.DOWNLOAD_TYPE_ADD)) {
			/*taskScheduleInfo = get(task.getId());*/
			taskScheduleInfo = task;
		} else {
			taskScheduleInfo = task;
		}
		List<TaskLineInfo> LineInfoList = taskScheduleInfo.getTaskLineInfoList();
		for (int i = 0; i < LineInfoList.size(); i++) {
			DownloadTask downloadTask = new DownloadTask();
			downloadTask.setTaskId(taskScheduleInfo.getId());
			downloadTask.setEquipmentId(LineInfoList.get(i).getEquipmentId());
			downloadTask.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadTask.setIsDownload("0");
			downloadTask.setDownloadType(type);
			downloadTaskService.save(downloadTask);
			
			//押款员、车辆只有在贵金属派送条件下进行同步
			if("4".equals(taskScheduleInfo.getTaskType())) {
				List<TaskPersonInfo> taskPersonInfoList = taskScheduleInfo.getTaskPersonInfoList();
				for (TaskPersonInfo taskPersonInfo : taskPersonInfoList) {
					//同步押款员
					DownloadPerson downloadPerson = new DownloadPerson();
					downloadPerson.setPersonId(taskPersonInfo.getPersonId());
					downloadPerson.setEquipId(LineInfoList.get(i).getEquipmentId());
					downloadPerson.setIsDownload("0");
					downloadPerson.setRegisterTime(DateUtils.formatDateTime(new Date()));				
					downloadPerson.setDownloadType(type);
					downloadPersonService.save(downloadPerson);
					
				}
				List<TaskCarInfo> taskCarInfoList = taskScheduleInfo.getTaskCarInfoList();
				for (TaskCarInfo taskCarInfo : taskCarInfoList) {
					//同步车辆
					DownloadCar downloadCar = new DownloadCar();
					downloadCar.setCarId(taskCarInfo.getCarId());
					downloadCar.setEquipId(LineInfoList.get(i).getEquipmentId());
					downloadCar.setIsDownload("0");
					downloadCar.setRegisterTime(DateUtils.formatDateTime(new Date()));				
					downloadCar.setDownloadType(type);
					downloadCarService.save(downloadCar);
				
				}
			}
		}	
		
	}

	@Transactional(readOnly = false)
	public void delete(TaskScheduleInfo taskScheduleInfo) {
		super.delete(taskScheduleInfo);
		taskCarInfoDao.delete(new TaskCarInfo(taskScheduleInfo));
		taskLineInfoDao.delete(new TaskLineInfo(taskScheduleInfo));
		taskPersonInfoDao.delete(new TaskPersonInfo(taskScheduleInfo));
		
		this.insentDownload(taskScheduleInfo, DownloadEntity.DOWNLOAD_TYPE_DEL);
		
		LogUtils.saveLog(Servlets.getRequest(), "删除排班:["+taskScheduleInfo.getId()+"]");
		
	}

}