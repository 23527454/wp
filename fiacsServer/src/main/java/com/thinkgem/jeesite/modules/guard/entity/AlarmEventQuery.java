/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 异常报警事件查询Entity
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
public class AlarmEventQuery extends DataEntity<AlarmEventQuery> {

	private static final long serialVersionUID = 1L;
	private Office office; // 设备ID
	private String recordId; // 事件序号
	private String eventType; // 事件类型--0 交接事件,1报警事件,2中心金库随机
	private String equipmentId;
	private String equipSn; // 设备序列号
	private String time; // 时间
	private TaskScheduleInfo taskScheduleInfo; // 任务ID
	private ClassTaskInfo classTaskfo; // 班组
	private Area area; // 区域
	private String timeTwo; // 截止时间
	private String sortType; // 排序类型
	private String sort; // 排序方式
	private List<MoneyBoxEventDetail> moneyBoxEventDetailList = Lists.newArrayList();		// 子表列表
	public AlarmEventQuery() {
		super();
	}

	public AlarmEventQuery(String id) {
		super(id);
	}

	public List<MoneyBoxEventDetail> getMoneyBoxEventDetailList() {
		return moneyBoxEventDetailList;
	}

	public void setMoneyBoxEventDetailList(List<MoneyBoxEventDetail> moneyBoxEventDetailList) {
		this.moneyBoxEventDetailList = moneyBoxEventDetailList;
	}

	@ExcelField(title = "网点", type = 1, align = 2, sort = 1)
	public String getExcelOfficeName() {
		return office.getName();
	}

	@ExcelField(title = "时间", align = 2, sort = 2)
	public String getExcelTime() {
		return time;
	}

	@ExcelField(title = "班组", align = 2, sort = 3)
	public String getExcelClassTaskInfo() {
		return classTaskfo.getName();
	}

	@ExcelField(title = "班次", align = 2, sort = 4)
	public String getExcelTaskScheduleInfo() {
		return taskScheduleInfo.getId();
	}

	@ExcelField(title = "异常类型", align = 2, sort = 5, dictType = "event_type")
	public String getExcelTaskType() {
		return eventType;
	}

	@ExcelField(title = "区域", align = 2, sort = 6)
	public String getExcelArea() {
		return area.getName();
	}
	

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Length(min = 0, max = 1, message = "事件类型长度必须介于 0 和 1 之间")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
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

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

}