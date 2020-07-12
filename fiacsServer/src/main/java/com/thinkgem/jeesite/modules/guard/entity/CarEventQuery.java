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
 * 车辆事件查询Entity
 * @author Jumbo
 * @version 2017-07-10
 */
public class CarEventQuery extends DataEntity<CarEventQuery> {
	
	private static final long serialVersionUID = 1L;
	private Car car;		// 事件ID
	private String equipmentId;		// 设备ID
	private String eventId;		// 事件序号
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private String timeTwo;
	private TaskScheduleInfo taskScheduleInfo; // 任务ID
	private Office office; // 网点
	private Area area; // 区域
	private String sortType;
	private String sort;
	public CarEventQuery() {
		super();
	}

	public CarEventQuery(String id){
		super(id);
	}
	
	
	@ExcelField(title = "网点", type = 1, align = 2, sort = 1)
	public String getExcelOfficeName() {
		return office.getName();
	}
	
	@ExcelField(title = "时间",  align = 2, sort = 2)
	public String getExcelTime() {
		return time;
	}
	
	@ExcelField(title = "车辆卡号",  align = 2, sort = 3)
	public String getExcelCarNum() {
		return car.getCardNum();
	}
	
	@ExcelField(title = "车牌号",  align = 2, sort = 4)
	public String getExcelCarplate() {
		return car.getCarplate();
	}
	
	@ExcelField(title = "负责人",  align = 2, sort = 5)
	public String getExcelAdmin() {
		return car.getAdmin();
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

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
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
	
	
}