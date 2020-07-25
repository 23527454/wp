/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.FingerInfo;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 门禁出入事件Entity
 * @author zgx
 * @version 2018-12-29
 */
public class AccessEventInfo extends DataEntity<AccessEventInfo> {

	private static final long serialVersionUID = 1L;
	private String recordId;        // 事件序号
	private String equipmentId;        // 设备ID
	private String equipSn;        // 设备序列号
	private String doorPos;        //门号
	private String staffId;			//员工ID
	private String fingerInfoId;    //指纹号
	private String eventDate;        //事件时间
	private String authorType;        //认证类型
	private Integer accessStatus;    //进出类型
	private String eventType;        //事件类型
	private Integer handleStatus;    //处理状态
	private String handleTime;        //处理时间
	private String handleUserNameId;    //处理人ID
	private Integer handleMode;        //处理方法
	private String eventDetailId;

	private Equipment equipment;        //设备
	private Staff staff;        //员工
	private FingerInfo fingerInfo;    //指纹信息
	private User handleUser;        //处理人

	private String officeName;		//网点名
	private String nodes;			//选择的节点

	public String getEventDetailId() {
		return eventDetailId;
	}

	public void setEventDetailId(String eventDetailId) {
		this.eventDetailId = eventDetailId;
	}

	public AccessEventInfo() {
		super();
	}

	public AccessEventInfo(String id) {
		super(id);
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
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

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public FingerInfo getFingerInfo() {
		return fingerInfo;
	}

	public void setFingerInfo(FingerInfo fingerInfo) {
		this.fingerInfo = fingerInfo;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public Integer getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(Integer accessStatus) {
		this.accessStatus = accessStatus;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public User getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(User handleUser) {
		this.handleUser = handleUser;
	}

	public Integer getHandleMode() {
		return handleMode;
	}

	public void setHandleMode(Integer handleMode) {
		this.handleMode = handleMode;
	}

	public String getHandleUserNameId() {
		return handleUserNameId;
	}

	public void setHandleUserNameId(String handleUserNameId) {
		this.handleUserNameId = handleUserNameId;
	}

	public String getFingerInfoId() {
		return fingerInfoId;
	}

	public void setFingerInfoId(String fingerInfoId) {
		this.fingerInfoId = fingerInfoId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
}