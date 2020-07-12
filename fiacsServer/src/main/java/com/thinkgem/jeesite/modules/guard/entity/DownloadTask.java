/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 排班同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadTask extends DownloadEntity<DownloadTask> {
	
	private static final long serialVersionUID = 1L;
	private String taskId;		// 任务ID
	private String equipmentId;		// 设备ID
	private TaskScheduleInfo task;
	private ClassTaskInfo classTaskInfo; 
	private Office office;
	private String downloadTimeTwo; 
	private List<String> officeIds;
	
	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
	}

	public DownloadTask() {
		super();
	}

	public DownloadTask(String id){
		super(id);
	}

	public String getTaskId() {
		return taskId;
	}
	
	public String getDownloadTimeTwo() {
		return downloadTimeTwo;
	}

	public void setDownloadTimeTwo(String downloadTimeTwo) {
		this.downloadTimeTwo = downloadTimeTwo;
	}

	public TaskScheduleInfo getTask() {
		return task;
	}

	public void setTask(TaskScheduleInfo task) {
		this.task = task;
	}

	public ClassTaskInfo getClassTaskInfo() {
		return classTaskInfo;
	}

	public void setClassTaskInfo(ClassTaskInfo classTaskInfo) {
		this.classTaskInfo = classTaskInfo;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
}