/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.guard.service.StaffHelper;

/**
 * 专员事件Entity
 * @author Jumbo
 * @version 2017-08-01
 */
public class CommissionerEvent extends DataEntity<CommissionerEvent> {
	
	private static final long serialVersionUID = 1L;
	private String recordId;		// 记录序号
	private String personId;		// 专员ID
	private String fingerId;		// 专员指纹号
	private String equipmentId;		// 设备ID
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private Staff staff;
	private String taskId;
	private Integer flag;
	private String beforeEqDate;//
	private String afterEqDate; //
	//add 2019-9-21
	private String authorType;

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	private String date; //中心金库随机事件过滤使用
	
	private String eventType;
	
	public static final String EVENT_TYPE_CONNECT_EVENT = "0";
//	public static final String EVENT_TYPE_ALARM_EVENT = "1";
	public static final String EVENT_TYPE_SUPER_GO_EVENT = "2";

	private String areaId;
	
	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
	public CommissionerEvent() {
		super();
	}

	public CommissionerEvent(String id){
		super(id);
	}
	
	



	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public CommissionerEvent(ConnectEvent connectEvent){
		this.recordId = connectEvent.getRecordId();
		this.equipmentId = connectEvent.getEquipmentId();
		this.time = connectEvent.getTime();
		
		AssertUtil.assertNotNull(this.recordId, "recordId");
		AssertUtil.assertNotNull(this.equipmentId, "equipmentId");
		AssertUtil.assertNotNull(this.time, "time");
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getFingerId() {
		return fingerId;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
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
	
	@Length(min=1, max=20, message="时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getFingerNumLabel() {
		//return StaffHelper.buildFingerNum(areaId, fingerId);
		return fingerId.length()==9 ? fingerId : StaffHelper.buildFingerNum(areaId, fingerId);
	}
}