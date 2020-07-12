/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 人员交接明细Entity
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
public class ConnectPersonnel extends DataEntity<ConnectPersonnel> {

	private static final long serialVersionUID = 1L;
	private String equipmentId; // 设备编号
	private String eventId; // 事件序号
	private String taskType; // 任务类型
	private String equipSn; // 设备序列号
	private String time; // 时间
	private TaskScheduleInfo taskScheduleInfo; // 任务ID
	private String carId; // 车辆编号
	private Office office; // 网点
	private ClassTaskInfo classTaskfo; // 班组ID
	private Area area; // 区域
	private Staff staff; // 人员
	private FingerInfo fingerInfo; // 指纹
	private StaffImage staffImage; // 人员照片
	private EventDetail eventDetail; // 抓拍图片
	private String text = "1";     // 第一个默认值
	private String textS;          //搜索的是什么
	private String sort;   // 是否升序标记
	private String sortType; //排序类型
	private String timeTwo;
	private String staff_id;
	public ConnectPersonnel() {
		super();
	}

	public ConnectPersonnel(String id) {
		super(id);
	}
	
	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
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

	public String getTextS() {
		return textS;
	}

	public void setTextS(String textS) {
		this.textS = textS;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public EventDetail getEventDetail() {
		return eventDetail;
	}

	public void setEventDetail(EventDetail eventDetail) {
		this.eventDetail = eventDetail;
	}

	public StaffImage getStaffImage() {
		return staffImage;
	}

	public void setStaffImage(StaffImage staffImage) {
		this.staffImage = staffImage;
	}

	public FingerInfo getFingerInfo() {
		return fingerInfo;
	}

	public void setFingerInfo(FingerInfo fingerInfo) {
		this.fingerInfo = fingerInfo;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public ClassTaskInfo getClassTaskfo() {
		return classTaskfo;
	}

	public void setClassTaskfo(ClassTaskInfo classTaskfo) {
		this.classTaskfo = classTaskfo;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

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

	public TaskScheduleInfo getTaskScheduleInfo() {
		return taskScheduleInfo;
	}

	public void setTaskScheduleInfo(TaskScheduleInfo taskScheduleInfo) {
		this.taskScheduleInfo = taskScheduleInfo;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

}