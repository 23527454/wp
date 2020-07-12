/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 排班明细Entity
 * 
 * @author Jumbo
 * @version 2017-07-10
 * 修改时间2017-10-26 
 * 修改作者 wangyanaki
 */
public class TaskScheduleInfoDetail extends DataEntity<TaskScheduleInfoDetail> {

	private static final long serialVersionUID = 1L;
	
	private Integer classTaskId; // 班组id
	private Integer verifyCar; // 
	private Integer verifyInterMan; // 
	private Integer verifyMoneyBox; // 
	private Integer patrolManNum; // 
	private Integer interManNum; // 
	private Integer taskTimeout; // 
	private String allotDate; // 排班日期
	private String allotTime; // 排班时间
	private String taskDate; // 
	private String taskTime; // 
	private String taskType; // 任务类型
	private Integer taskTimeClass;
	private String createtor;
	private String updatetor;
	private Date createDate;
	private Date updateDate;
	private String remarks;
	private Integer delFlag2;
	
	
	



	public String getCreatetor() {
		return createtor;
	}

	public void setCreatetor(String createtor) {
		this.createtor = createtor;
	}

	public String getUpdatetor() {
		return updatetor;
	}

	public void setUpdatetor(String updatetor) {
		this.updatetor = updatetor;
	}

	public Integer getDelFlag2() {
		return delFlag2;
	}

	public void setDelFlag2(Integer delFlag2) {
		this.delFlag2 = delFlag2;
	}

	public Integer getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(Integer classTaskId) {
		this.classTaskId = classTaskId;
	}

	public Integer getVerifyCar() {
		return verifyCar;
	}

	public void setVerifyCar(Integer verifyCar) {
		this.verifyCar = verifyCar;
	}

	public Integer getVerifyInterMan() {
		return verifyInterMan;
	}

	public void setVerifyInterMan(Integer verifyInterMan) {
		this.verifyInterMan = verifyInterMan;
	}

	public Integer getVerifyMoneyBox() {
		return verifyMoneyBox;
	}

	public void setVerifyMoneyBox(Integer verifyMoneyBox) {
		this.verifyMoneyBox = verifyMoneyBox;
	}

	public Integer getPatrolManNum() {
		return patrolManNum;
	}

	public void setPatrolManNum(Integer patrolManNum) {
		this.patrolManNum = patrolManNum;
	}

	public Integer getInterManNum() {
		return interManNum;
	}

	public void setInterManNum(Integer interManNum) {
		this.interManNum = interManNum;
	}

	public Integer getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(Integer taskTimeout) {
		this.taskTimeout = taskTimeout;
	}

	public String getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	public Integer getTaskTimeClass() {
		return taskTimeClass;
	}

	public void setTaskTimeClass(Integer taskTimeClass) {
		this.taskTimeClass = taskTimeClass;
	}



	

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	private String equipmentId; // 设备编号
	private String recordId; // 事件序号
	
	private String equipSn; // 设备序列号
	private String time; // 时间
	private String taskId; // 任务ID
	private String carId; // 车辆编号
	private String moneyBoxEventId; // 款箱事件ID
	private String officeName; // 网点
	private String officeType; // 网点类型

	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	private String securityStaff; // 押款员
	private String commissioner; // 交接专员
	private String carplate; // 车牌号
	private String cardNum; // 车辆卡号
	private String imagePath; // 车辆照片
	private String taskName; // 班组
	private String classId; // 班组ID
	private String lineName; // 线路名称
	private String lineId;   // 线路ID
	private String areaName; // 区域
	private String areaId; // 区域ID
	
	private MoneyBoxEvent moneyBoxEvent; // 款箱事件
	private String type;
	private String nodes;
	private List<CommissionerEvent> commissionerEventList = Lists.newArrayList(); // 子表列表
	private List<EventDetail> eventDetailList = Lists.newArrayList(); // 子表列表
	private List<MoneyBoxEventDetail> moneyBoxEventDetailList = Lists.newArrayList(); // 子表列表
	private List<CarEvent> carEventList = Lists.newArrayList(); // 子表列表
	private List<TaskLineInfo> taskLineInfoList = Lists.newArrayList();		// 子表列表
	private String sort; // 是否升序标记
	private String sortType; // 排序类型
	private String taskDateTwo;

	
	public List<TaskLineInfo> getTaskLineInfoList() {
		return taskLineInfoList;
	}

	public void setTaskLineInfoList(List<TaskLineInfo> taskLineInfoList) {
		this.taskLineInfoList = taskLineInfoList;
	}



	private TaskScheduleInfo taskScheduleInfo; // 任务

	public TaskScheduleInfoDetail() {
		super();
	}

	public TaskScheduleInfoDetail(String id) {
		super(id);
	}
	
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	@ExcelField(title = "车辆确认", align = 2, sort = 15,dictType = "message_confirm")
	public String getCarYes() {
		return taskScheduleInfo.getVerifyCar();
	}
	@ExcelField(title = "专员确认", align = 2, sort = 17,dictType = "message_confirm")
	public String getManYes() {
		return taskScheduleInfo.getVerifyInterMan();
	}
	
	@ExcelField(title = "线路名称", align = 2, sort = 13)
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public TaskScheduleInfo getTaskScheduleInfo() {
		return taskScheduleInfo;
	}

	public void setTaskScheduleInfo(TaskScheduleInfo taskScheduleInfo) {
		this.taskScheduleInfo = taskScheduleInfo;
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
	@ExcelField(title = "排班日期",  align = 2, sort = 3)
	public String getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(String allotDate) {
		this.allotDate = allotDate;
	}
	@ExcelField(title = "启动时间",  align = 2, sort = 5)
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
	
	@ExcelField(title = "任务类型",  align = 2, sort = 9)
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

	@ExcelField(title = "班次",  align = 2, sort = 7)
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

	@ExcelField(title = "网点", align = 2, sort = 1)
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
	
	@ExcelField(title = "车牌号", align = 2, sort = 11)
	public String getCarplate() {
		return carplate;
	}

	public void setCarplate(String carplate) {
		this.carplate = carplate;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	@ExcelField(title = "班组", type = 1, align = 2, sort = 1)
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}