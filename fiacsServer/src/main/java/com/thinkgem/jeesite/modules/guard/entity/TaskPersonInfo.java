/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.service.StaffHelper;

/**
 * 排班信息Entity
 * @author Junbo
 * @version 2017-08-01
 */
public class TaskPersonInfo extends DataEntity<TaskPersonInfo> {
	
	private static final long serialVersionUID = 1L;
	private String taskScheduleId;		// 班次ID 父类
	private String personId;		// 押款员ID
	private String fingerNum;		// 指纹号
	private String name;
	private String identifyNumber;
	private String delete="1";
	
	private String areaId;
	
	public TaskPersonInfo() {
		super();
	}

	public TaskPersonInfo(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public TaskPersonInfo(TaskScheduleInfo taskScheduleId){
		if(taskScheduleId != null)
			this.taskScheduleId = taskScheduleId.getId();
	}
	public TaskPersonInfo(LineEvent lineEvent){
		if(lineEvent != null)
			this.taskScheduleId = lineEvent.getId();
	}

	public String getTaskScheduleId() {
		return taskScheduleId;
	}

	public void setTaskScheduleId(String taskScheduleId) {
		this.taskScheduleId = taskScheduleId;
	}
	
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	@Length(min=1, max=6, message="指纹号长度必须介于 1 和 6 之间")
	public String getFingerNum() {
		return fingerNum;
	}

	public void setFingerNum(String fingerNum) {
		this.fingerNum = fingerNum;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String getFingerNumLabel() {
		return StaffHelper.buildFingerNum(areaId, fingerNum);
	}
}