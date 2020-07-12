/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 维保员事件Entity
 * @author zengguoxiang
 * @version 2018-12-31
 */
public class SafeGuardEvent extends DataEntity<SafeGuardEvent> {
	
	private static final long serialVersionUID = 1L;
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件序号
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private String fingerId;
	private String fingerNumLabel;
	private String personId;
	private String personName;
	private String officeName;		// 网点
	private String nodes;
	private String eventType;
	private String beforeEqDate;//
	private String afterEqDate; //
	private String date;
	private String eventDetailId;
	private String authorType;

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public String getFingerNumLabel() {
		return fingerNumLabel;
	}

	public void setFingerNumLabel(String fingerNumLabel) {
		this.fingerNumLabel = fingerNumLabel;
	}

	public String getEventDetailId() {
		return eventDetailId;
	}

	public void setEventDetailId(String eventDetailId) {
		this.eventDetailId = eventDetailId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}