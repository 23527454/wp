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
 * @author Jumbo
 * @version 2017-07-19
 */
public class CommissionerQuery extends DataEntity<CommissionerQuery> {
	
	private static final long serialVersionUID = 1L;
	private ConnectEvent connectEvent;		// 事件ID
	private String fingerId;		// 专员指纹号
	private String time;		// 时间
	private TaskScheduleInfo taskScheduleInfo; // 任务ID
	private ClassTaskInfo classTaskfo; // 班组
	private Area area; // 区域
	private Office office; // 设备ID
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
	
	public CommissionerQuery() {
		super();
	}

	public CommissionerQuery(String id){
		super(id);
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

	public StaffImage getStaffImage() {
		return staffImage;
	}

	public void setStaffImage(StaffImage staffImage) {
		this.staffImage = staffImage;
	}

	public EventDetail getEventDetail() {
		return eventDetail;
	}

	public void setEventDetail(EventDetail eventDetail) {
		this.eventDetail = eventDetail;
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

	public ClassTaskInfo getClassTaskfo() {
		return classTaskfo;
	}

	public void setClassTaskfo(ClassTaskInfo classTaskfo) {
		this.classTaskfo = classTaskfo;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public ConnectEvent getConnectEvent() {
		return connectEvent;
	}

	public void setConnectEvent(ConnectEvent connectEvent) {
		this.connectEvent = connectEvent;
	}

	public String getFingerId() {
		return fingerId;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}
	
	@Length(min=1, max=20, message="时间长度必须介于 1 和 20 之间")
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
	
	
	
	
}