/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 交接事件查询Entity
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
public class ConnectEventQuery extends DataEntity<ConnectEventQuery> {

	private static final long serialVersionUID = 1L;
	private Office office ; // 设备编号
	private String eventId; // 事件序号
	private String taskType; // 任务类型
	private String equipSn; // 设备序列号
	private String time; // 时间
	private TaskScheduleInfo taskScheduleInfo; // 任务ID
	private Car car; // 车辆编号
	private String fingerNumOne; // 押款员1的指纹号
	private String fingerNumTwo; // 押款员2的指纹号
	private String imagePathOne; // 押款员1的照片
	private String imagePathTwo; // 押款员2的照片
	private String taskDateTwo;  //截止时间
	private String sort;   // 是否升序标记
	private String sortType; //排序类型
	private List<ConnectCommissioner> connectCommissionerList = Lists.newArrayList(); // 子表列表
	private List<EventDetail> eventDetailList = Lists.newArrayList(); // 子表列表

	public ConnectEventQuery() {
		super();
	}

	public ConnectEventQuery(String id) {
		super(id);
	}
	
	
	@ExcelField(title = "网点",  align = 2, sort = 1)
	public String getExcelOfficeName() {
		return office.getName();
	}
	
	@ExcelField(title = "时间",  align = 1, sort = 2)
	public String getExcelTime() {
		return time;
	}
	
	
	@ExcelField(title = "交接专员",  align = 2, sort = 4)
	public String getExcelZY() {
		return "无";
	}
	@ExcelField(title = "车牌号",  align = 2, sort = 5)
	public String getExcelCarplate() {
		return car.getCarplate();
	}
	@ExcelField(title = "车辆卡号",  align = 2, sort = 6)
	public String getExcelCarNum() {
		return car.getCardNum();
	}
	
	@ExcelField(title = "任务类型",  align = 2, sort = 7, dictType = "task_type")
	public String getExcelTaskType() {
		return taskType;
	}
	
	@ExcelField(title = "班次",  align = 2, sort = 9)
	public String getExcelTaskScheduleInfo() {
		return taskScheduleInfo.getId();
	}
	
	@ExcelField(title = "区域",  align = 2, sort = 10)
	public String getExcelArea() {
		return office.getArea().getName();
	}
	
	

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Length(min = 0, max = 1, message = "任务类型长度必须介于 0 和 1 之间")
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Length(min = 0, max = 32, message = "设备序列号长度必须介于 0 和 32 之间")
	public String getEquipSn() {
		return equipSn;
	}

	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}

	@Length(min = 1, max = 20, message = "时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTaskDateTwo() {
		return taskDateTwo;
	}

	public void setTaskDateTwo(String taskDateTwo) {
		this.taskDateTwo = taskDateTwo;
	}

	public TaskScheduleInfo getTaskScheduleInfo() {
		return taskScheduleInfo;
	}

	public void setTaskScheduleInfo(TaskScheduleInfo taskScheduleInfo) {
		this.taskScheduleInfo = taskScheduleInfo;
	}

	public String getFingerNumOne() {
		return fingerNumOne;
	}

	public void setFingerNumOne(String fingerNumOne) {
		this.fingerNumOne = fingerNumOne;
	}

	public String getFingerNumTwo() {
		return fingerNumTwo;
	}

	public void setFingerNumTwo(String fingerNumTwo) {
		this.fingerNumTwo = fingerNumTwo;
	}

	public String getImagePathOne() {
		return imagePathOne;
	}

	public void setImagePathOne(String imagePathOne) {
		this.imagePathOne = imagePathOne;
	}

	public String getImagePathTwo() {
		return imagePathTwo;
	}

	public void setImagePathTwo(String imagePathTwo) {
		this.imagePathTwo = imagePathTwo;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public List<ConnectCommissioner> getConnectCommissionerList() {
		return connectCommissionerList;
	}

	public void setConnectCommissionerList(List<ConnectCommissioner> connectCommissionerList) {
		this.connectCommissionerList = connectCommissionerList;
	}

	public List<EventDetail> getEventDetailList() {
		return eventDetailList;
	}

	public void setEventDetailList(List<EventDetail> eventDetailList) {
		this.eventDetailList = eventDetailList;
	}
}