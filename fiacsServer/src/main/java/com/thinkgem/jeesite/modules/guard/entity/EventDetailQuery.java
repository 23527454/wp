/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 人员交接明细Entity
 * 
 * @author Jumbo
 * @version 2017-07-20
 */
public class EventDetailQuery extends DataEntity<EventDetailQuery> {

	private static final long serialVersionUID = 1L;
	private String fingerId;		// 指纹ID
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件ID
	private String personId;		// 事件ID
	private byte[] imageData;		// 抓拍图片数据
	private String imageSize;		// 抓拍图片数据长度
	private String time;		// 记录时间
	private String taskId;		// 任务ID
	private Staff staff;
	private String staffType; // 人员类型
	private String areaId;
	private String areaName;
	private String officeId;
	private String officeName;
	private String className;
	private String taskType;
	
	private String text = "1";     // 第一个默认值
	private String textS;          //搜索的是什么
	private String sort;   // 是否升序标记
	private String sortType; //排序类型
	private String timeTwo;
	private String staff_id;

	
	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public EventDetailQuery() {
		super();
	}

	public EventDetailQuery(String id) {
		super(id);
	}
	
	
	@ExcelField(title = "网点", type = 1, align = 2, sort = 1)
	public String getExcelOfficeName() {
		return officeName;
	}
	
	@ExcelField(title = "时间",  align = 2, sort = 2)
	public String getExcelTime() {
		return time;
	}
	
	@ExcelField(title = "押款员",  align = 2, sort = 3)
	public String getExcelStaff() {
		return staff.getName();
	}
	
	@ExcelField(title = "任务类型",  align = 2, sort = 4, dictType = "task_type")
	public String getExcelTaskType() {
		return taskType;
	}
	
	@ExcelField(title = "班组",  align = 2, sort = 5)
	public String getExcelClassTaskInfo() {
		return className;
	}
	
	@ExcelField(title = "班次",  align = 2, sort = 6)
	public String getExcelTaskScheduleInfo() {
		return taskId;
	}
	
	@ExcelField(title = "区域",  align = 2, sort = 7)
	public String getExcelArea() {
		return areaName;
	}
	
	

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextS() {
		return textS;
	}

	public void setTextS(String textS) {
		this.textS = textS;
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

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}

	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	@Length(min = 1, max = 20, message = "记录时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}