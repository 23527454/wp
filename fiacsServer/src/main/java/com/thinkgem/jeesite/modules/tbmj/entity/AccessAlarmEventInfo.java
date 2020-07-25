/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 门禁报警事件Entity
 * @author zgx
 * @version 2018-12-29
 */
public class AccessAlarmEventInfo extends DataEntity<AccessAlarmEventInfo> {

	private static final long serialVersionUID = 1L;
	private String recordId;        // 事件序号
	private String equipmentId;        // 设备ID
	private String equipSn;        // 设备序列号
	private String doorPos;        //门号
	private String eventDate;        //事件时间
	private String eventType;        //事件类型
	private Integer alarmStatus;    //报警状态
	private Integer handleStatus;    //处理状态
	private String handleTime;        //处理时间
	private String handleUserNameId;    //处理人ID
	private Integer handleMode;        //处理方法

	private Equipment equipment;        //设备
	private User handleUser;        //处理人

	private String officeName;		//网点名
	private String nodes;			//选择的节点

	public AccessAlarmEventInfo() {
		super();
	}

	public AccessAlarmEventInfo(String id) {
		super(id);
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipSn() {
		return equipSn;
	}

	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}

	public String getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(String doorPos) {
		this.doorPos = doorPos;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Integer getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(Integer alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}

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

	public Integer getHandleMode() {
		return handleMode;
	}

	public void setHandleMode(Integer handleMode) {
		this.handleMode = handleMode;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public User getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(User handleUser) {
		this.handleUser = handleUser;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
}