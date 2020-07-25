/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfoDetail;
import com.thinkgem.jeesite.modules.guard.service.StaffHelper;
import org.hibernate.validator.constraints.Length;

/**
 * 事件详情Entity
 * @author Jumbo
 * @version 2017-07-14
 */
public class AccessEventDetail extends DataEntity<AccessEventDetail> {
	
	private static final long serialVersionUID = 1L;
	private String fingerId;		// 指纹ID
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件ID
	private String personId;		// 事件ID
	private byte[] imageData;		// 抓拍图片数据
	private String imageSize;		// 抓拍图片数据长度
	private String time;		// 记录时间
	private String eventType; //0 交接事件,1报警事件,2中心金库随机
	
	private String beforeEqDate;//
	private String afterEqDate; //
	
	public static final String EVENT_TYPE_CONNECT_EVENT = "0";
	public static final String EVENT_TYPE_ALARM_EVENT = "1";
	public static final String EVENT_TYPE_SUPER_GO_EVENT = "2";
	private Staff staff;
	
	private String areaId;

	//add 2019-09-21
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

	//中心金库随机过滤用
	private String date;

	private String bigDataFlag;

	public void setBigDataFlag(String bigDataFlag) {
		this.bigDataFlag = bigDataFlag;
	}

	public String getBigDataFlag() {
		return bigDataFlag;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public AccessEventDetail() {
		super();
	}

	public AccessEventDetail(String id){
		super(id);
	}
	
	public AccessEventDetail(ConnectEvent connectEvent){
		this.recordId = connectEvent.getRecordId();
		this.time = connectEvent.getTime();
		this.equipmentId =connectEvent.getEquipmentId();
	}

	public AccessEventDetail(TaskScheduleInfoDetail taskScheduleInfoDetail){
		this.recordId = taskScheduleInfoDetail.getRecordId();
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
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	@JsonIgnore
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}
	
	@Length(min=1, max=20, message="记录时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getFingerNumLabel() {
		//return StaffHelper.buildFingerNum(areaId, fingerId);
		return fingerId.length()==9 ? fingerId : StaffHelper.buildFingerNum(areaId, fingerId);
	}
}