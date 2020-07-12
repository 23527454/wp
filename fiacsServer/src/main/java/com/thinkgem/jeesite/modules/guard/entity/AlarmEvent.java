/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 异常报警事件Entity
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
public class AlarmEvent extends DataEntity<AlarmEvent> {

	private static final long serialVersionUID = 1L;
	private String equipmentId; // 设备ID
	private String recordId; // 事件序号
	private String eventType; // 事件类型
	private String equipSn; // 设备序列号
	private String time; // 时间
	private String taskId; // 任务ID
	private String officeName; // 网点
	private String taskName; // 班组
	private String areaName; // 区域
	private String officeID; // 网点
	private String nodes;
	private String type;
	private String handleTime;//处理时间
	private String handleUserNameId;//处理人ID
	private String handleUserName;//处理人姓名

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleUserNameId() {
		return handleUserNameId;
	}

	public void setHandleUserNameId(String handleUserNameId) {
		this.handleUserNameId = handleUserNameId;
	}

	public String getHandleUserName() {
		return handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	private List<MoneyBoxEventDetail> moneyBoxEventDetailList = Lists.newArrayList();		// 子表列表
	public AlarmEvent() {
		super();
	}

	public AlarmEvent(String id) {
		super(id);
	}
	
	public List<MoneyBoxEventDetail> getMoneyBoxEventDetailList() {
		return moneyBoxEventDetailList;
	}

	public void setMoneyBoxEventDetailList(List<MoneyBoxEventDetail> moneyBoxEventDetailList) {
		this.moneyBoxEventDetailList = moneyBoxEventDetailList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getOfficeID() {
		return officeID;
	}

	public void setOfficeID(String officeID) {
		this.officeID = officeID;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Length(min = 0, max = 1, message = "事件类型长度必须介于 0 和 1 之间")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAreaName() {
		return areaName;
	}

}