/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 门禁报警事件Entity
 * @author zengguoxiang
 * @version 2018-12-29
 */
public class DoorAlarmEvent extends DataEntity<DoorAlarmEvent> {
	
	private static final long serialVersionUID = 1L;
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件序号
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private String fingerId;
	private String personId;
	private String fingerNumLabel;
	private String officeName;		// 网点
	private String nodes;
	private String type;
	private Integer doorPos;
	private String eventType;
	private Integer alarmStatus;
	private Integer handleStatus;
	private String handleTime;
	private String handleUserNameId;
	private String handleUserName;
	private Integer handleMode;
	private String remark;
	private String beforeEqDate;//
	private String afterEqDate; //
	private String date;
	private String personName;
	private String eventTypeName;
	private String handleModeName;
	private String handleStatusName;
	private String alarmStatusName;
	private String doorPosName;
	
	public String getDoorPosName() {
		return doorPosName;
	}
	
	public String getHandleModeName() {
		return handleModeName;
	}

	public String getHandleStatusName() {
		return handleStatusName;
	}

	public String getAlarmStatusName() {
		return alarmStatusName;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getHandleUserName() {
		return handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getBeforeEqDate() {
		return beforeEqDate;
	}
	public void setBeforeEqDate(String beforeEqDate) {
		this.beforeEqDate = beforeEqDate;
	}
	public String getAfterEqDate() {
		return afterEqDate;
	}
	public void setAfterEqDate(String afterEqDate) {
		this.afterEqDate = afterEqDate;
	}
	private Integer flag;
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
	public String getEquipSn() {
		return equipSn;
	}
	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFingerId() {
		return fingerId;
	}
	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFingerNumLabel() {
		return fingerNumLabel;
	}

	public void setFingerNumLabel(String fingerNumLabel) {
		this.fingerNumLabel = fingerNumLabel;
	}

	public Integer getDoorPos() {
		return doorPos;
	}
	public void setDoorPos(Integer doorPos) {
		this.doorPos = doorPos;
		this.doorPosName = DictUtils.getDictLabel(String.valueOf(doorPos), "door_pos", "");
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
		this.eventTypeName = DictUtils.getDictLabel(eventType, "door_alarm_event_type", "");
	}
	public Integer getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(Integer alarmStatus) {
		this.alarmStatus = alarmStatus;
		this.alarmStatusName = DictUtils.getDictLabel(String.valueOf(alarmStatus), "door_alarm_status", "");
	}
	public Integer getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
		this.handleStatusName = DictUtils.getDictLabel(String.valueOf(handleStatus), "door_handle_status", "");
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
		this.handleModeName = DictUtils.getDictLabel(String.valueOf(handleMode), "door_alarm_handle_mode", "");
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}