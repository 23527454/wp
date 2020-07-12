/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班信息Entity
 * 
 * @author Junbo
 * @version 2017-08-01
 */
public class TaskCarInfo extends DataEntity<TaskCarInfo> {

	private static final long serialVersionUID = 1L;
	private String taskScheduleId; // 班次ID 父类
	private String carId; // 车辆ID
	private String cardNum; // 车辆卡号
	private String name;
	private String carplate;
	private String delete = "1";

	public TaskCarInfo() {
		super();
	}

	public TaskCarInfo(String id) {
		super(id);
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarplate() {
		return carplate;
	}

	public void setCarplate(String carplate) {
		this.carplate = carplate;
	}

	public TaskCarInfo(TaskScheduleInfo taskScheduleId) {
		if (taskScheduleId != null)
			this.taskScheduleId = taskScheduleId.getId();
	}

	public TaskCarInfo(LineEvent lineEvent) {
		if (lineEvent != null)
			this.taskScheduleId = lineEvent.getId();
	}
	
	public String getTaskScheduleId() {
		return taskScheduleId;
	}

	public void setTaskScheduleId(String taskScheduleId) {
		this.taskScheduleId = taskScheduleId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	@Length(min = 1, max = 20, message = "车辆卡号长度必须介于 1 和 20 之间")
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

}