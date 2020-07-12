/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 车辆事件Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class CarEvent extends DataEntity<CarEvent> {
	
	private static final long serialVersionUID = 1L;
	private String carId;		// 车辆ID
	private String cardNum;		// 车辆卡号
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件序号
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private String taskId;		// 任务ID
	private String officeName;		// 网点
	private String carplate;		// 车牌号
	private String admin;		// 负责人
	private String phone;		// 联系人
	private String nodes;
	private String type;
	private String imagePath;		// 照片
	private String classTaskId; //班组
	private String beforeEqDate;//
	private String afterEqDate; //

	private String allotTime;
	private String taskTime;
	private String lineId;


	private String date; //中心金库随机时间过滤使用
	
	private Integer flag;


	public String getAllotTime() {
		return allotTime;
	}

	public void setAllotTime(String allotTime) {
		this.allotTime = allotTime;
	}

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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public CarEvent() {
		super();
	}

	public CarEvent(String id){
		super(id);
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
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

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	@Length(min=1, max=32, message="设备序列号长度必须介于 1 和 32 之间")
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
	
	public String getAdmin() {
		return admin;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}