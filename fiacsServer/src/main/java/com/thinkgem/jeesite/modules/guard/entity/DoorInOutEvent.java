/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 门禁出入事件Entity
 * @author zgx
 * @version 2018-12-29
 */
public class DoorInOutEvent extends DataEntity<DoorInOutEvent> {
	
	private static final long serialVersionUID = 1L;
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件序号
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private Integer personId;		// 任务ID
	private String officeName;	
	private String fingerId;// 网点
	private String personName;		// 车牌号
	private String nodes;
	private String eventType;
	private String imagePath;		// 照片
	private String doorPos; //班组
	private Integer accessStatus;
	private String handleTime;
	private String beforeEqDate;//
	private String afterEqDate; //
	private String handleUserNameId;
	private String handleUserName;
	private String fingerNumLabel;
	private Integer handleMode;
	private Integer handleStatus;
	private String remark;
	private String eventDetailId;
	private String eventTypeName;
	private String handleModeName;
	private String handleStatusName;
	private String accessStatusName;
	private String doorPosName;
	
	private String date; //中心金库随机时间过滤使用
	
	private Integer flag;

	private String authorType;


	public String getDoorPosName() {
		return doorPosName;
	}

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public String getHandleModeName() {
		return handleModeName;
	}

	public String getHandleStatusName() {
		return handleStatusName;
	}

	public String getAccessStatusName() {
		return accessStatusName;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}
	
	public String getEventDetailId() {
		return eventDetailId;
	}

	public void setEventDetailId(String eventDetailId) {
		this.eventDetailId = eventDetailId;
	}

	public String getFingerId() {
		return fingerId;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
		this.handleStatusName = DictUtils.getDictLabel(String.valueOf(handleStatus), "door_handle_status", "");
	}

	public String getHandleUserName() {
		return handleUserName;
	}

	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	public String getFingerNumLabel() {
		return fingerNumLabel;
	}

	public void setFingerNumLabel(String fingerNumLabel) {
		this.fingerNumLabel = fingerNumLabel;
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

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
		this.eventTypeName = DictUtils.getDictLabel(eventType, "door_control_event_type", "");
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(String doorPos) {
		this.doorPos = doorPos;
		this.doorPosName = DictUtils.getDictLabel(String.valueOf(doorPos), "door_pos", "");
	}

	public Integer getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(Integer accessStatus) {
		this.accessStatus = accessStatus;
		this.accessStatusName = DictUtils.getDictLabel(String.valueOf(accessStatus), "door_access_status", "");
	}

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
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
		this.handleModeName = this.eventType.equals("6")? DictUtils.getDictLabel(String.valueOf(handleMode), "door_control_handle_mode", ""):"";
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}