/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 交接事件Entity
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
public class ConnectEvent extends DataEntity<ConnectEvent> {

	private static final long serialVersionUID = 1L;
	private String equipmentId; // 设备编号
	private String recordId; // 事件序号
	private String taskType; // 任务类型
	public static final String TASK_TYPE_SEND= "0";//派送
	public static final String TASK_TYPE_RECOVERY= "1";//回收
	public static final String TASK_TYPE_TEMPORARY_SEND= "2";//临时派送
	public static final String TASK_TYPE_TEMPORARY_RECOVERY= "3";//临时回收
	public static final String TASK_TYPE_PRECIOUS_METAL_SEND= "4";//贵金属派送
	
	private String equipSn; // 设备序列号
	private String time; // 时间
	private String taskId; // 任务ID
	private String lineId; //班组ID
	private String carId; // 车辆编号
	private String moneyBoxEventId; // 款箱事件ID
	private String officeName; // 网点
	private String officeType; // 网点类型
	private String officeId;
	
	private String securityStaff; // 押款员
	private String commissioner; // 交接专员
	private String carplate; // 车牌号
	private String cardNum; // 车辆卡号
	private String imagePath; // 车辆照片
	private String taskName; // 班组
	private String areaName; // 区域
	private String areaId; // 区域ID
	private String allotDate; // 排班日期
	private String allotTime; // 排班时间
	private String taskTime;//执行时间
	private MoneyBoxEvent moneyBoxEvent; // 款箱事件
	private String type;
	private String nodes;
	private List<CommissionerEvent> commissionerEventList = Lists.newArrayList(); // 子表列表
	private List<EventDetail> eventDetailList = Lists.newArrayList(); // 子表列表
	private List<MoneyBoxEventDetail> moneyBoxEventDetailList = Lists.newArrayList(); // 子表列表
	private List<CarEvent> carEventList = Lists.newArrayList(); // 子表列表

	private String sort; // 是否升序标记
	private String sortType; // 排序类型
	private String taskDateTwo;

	private String commissionerNames;

	private String eventDetailNames;

	private String moneyboxNums;

	private String companyName;//车辆所属公司

	private String carAdmin;//负责人

	private String carPhone;//联系方式

	public String getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCarAdmin() {
		return carAdmin;
	}

	public void setCarAdmin(String carAdmin) {
		this.carAdmin = carAdmin;
	}

	public String getCarPhone() {
		return carPhone;
	}

	public void setCarPhone(String carPhone) {
		this.carPhone = carPhone;
	}

	@ExcelField(title="专员",align=1,sort=11)
	public String getCommissionerNames() {
		return commissionerNames;
	}

	public void setCommissionerNames(String commissionerNames) {
		this.commissionerNames = commissionerNames;
	}

	@ExcelField(title="押解员",align=1,sort=12)
	public String getEventDetailNames() {
		return eventDetailNames;
	}

	public void setEventDetailNames(String eventDetailNames) {
		this.eventDetailNames = eventDetailNames;
	}

	@ExcelField(title="款箱卡号",align=1,sort=13)
	public String getMoneyboxNums() {
		return moneyboxNums;
	}

	public void setMoneyboxNums(String moneyboxNums) {
		this.moneyboxNums = moneyboxNums;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	
	public ConnectEvent(LineEvent lineEvent){
		if(lineEvent != null)
			this.taskId = lineEvent.getId();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	
	public String getTaskDateTwo() {
		return taskDateTwo;
	}

	public void setTaskDateTwo(String taskDateTwo) {
		this.taskDateTwo = taskDateTwo;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public List<MoneyBoxEventDetail> getMoneyBoxEventDetailList() {
		return moneyBoxEventDetailList;
	}

	public void setMoneyBoxEventDetailList(List<MoneyBoxEventDetail> moneyBoxEventDetailList) {
		this.moneyBoxEventDetailList = moneyBoxEventDetailList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(String allotDate) {
		this.allotDate = allotDate;
	}

	public String getAllotTime() {
		return allotTime;
	}

	public void setAllotTime(String allotTime) {
		this.allotTime = allotTime;
	}

	public List<CommissionerEvent> getCommissionerEventList() {
		return commissionerEventList;
	}

	public void setCommissionerEventList(List<CommissionerEvent> commissionerEventList) {
		this.commissionerEventList = commissionerEventList;
	}

	public List<CarEvent> getCarEventList() {
		return carEventList;
	}

	public void setCarEventList(List<CarEvent> carEventList) {
		this.carEventList = carEventList;
	}

	public MoneyBoxEvent getMoneyBoxEvent() {
		return moneyBoxEvent;
	}

	public void setMoneyBoxEvent(MoneyBoxEvent moneyBoxEvent) {
		this.moneyBoxEvent = moneyBoxEvent;
	}

	public ConnectEvent() {
		super();
	}

	public ConnectEvent(String id) {
		super(id);
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@ExcelField(title = "任务类型",  align = 2, sort = 7)
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
	
	@ExcelField(title = "时间",  align = 1, sort = 2)
	@Length(min = 1, max = 20, message = "时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	@ExcelField(title = "班次",  align = 2, sort = 9)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getMoneyBoxEventId() {
		return moneyBoxEventId;
	}

	public void setMoneyBoxEventId(String moneyBoxEventId) {
		this.moneyBoxEventId = moneyBoxEventId;
	}

	public List<EventDetail> getEventDetailList() {
		return eventDetailList;
	}

	public void setEventDetailList(List<EventDetail> eventDetailList) {
		this.eventDetailList = eventDetailList;
	}
	
	@ExcelField(title = "网点",  align = 2, sort = 1)
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getSecurityStaff() {
		return securityStaff;
	}

	public void setSecurityStaff(String securityStaff) {
		this.securityStaff = securityStaff;
	}

	public String getCommissioner() {
		return commissioner;
	}

	public void setCommissioner(String commissioner) {
		this.commissioner = commissioner;
	}

	@ExcelField(title = "车牌号",  align = 2, sort = 5)
	public String getCarplate() {
		return carplate;
	}

	public void setCarplate(String carplate) {
		this.carplate = carplate;
	}
	
	@ExcelField(title = "车辆卡号",  align = 2, sort = 6)
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	@ExcelField(title = "区域",  align = 2, sort = 10)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}