/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;


import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班信息Entity
 * @author Junbo
 * @version 2017-08-01
 */
public class TaskLineInfo extends DataEntity<TaskLineInfo> {
	
	private static final long serialVersionUID = 1L;
	private String taskScheduleId;		// 班次ID 父类
	private String equipmentId;		// 设备ID
	private String name;  
	private String type="未完成";   // 线路监控中的是否完成
	private String delete="1";
	public TaskLineInfo() {
		super();
	}

	public TaskLineInfo(String id){
		super(id);
	}

	public TaskLineInfo(TaskScheduleInfo taskScheduleId){
		if(taskScheduleId != null)
			this.taskScheduleId = taskScheduleId.getId();
	}
	
	public TaskLineInfo(LineEvent lineEvent){
		if(lineEvent != null)
			this.taskScheduleId = lineEvent.getId();
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskScheduleId() {
		return taskScheduleId;
	}

	public void setTaskScheduleId(String taskScheduleId) {
		this.taskScheduleId = taskScheduleId;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
}