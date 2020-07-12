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
public class SafeGuardEventQuery extends DataEntity<SafeGuardEventQuery> {
	
	private static final long serialVersionUID = 1L;
	private String equipmentId;		// 设备ID
	private String recordId;		// 事件序号
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private Office office; // 网点
	private Area area; // 区域
	private String timeTwo;
	private String sortType;
	private String sort;
	private String personName;
	private String eventType;
	private String eventDetailId;
	private String fingerNumLabel;
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

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	public String getPersonName() {
		return personName;
	}
	
	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}
	public String getTimeTwo() {
		return timeTwo;
	}
	public SafeGuardEventQuery() {
		super();
	}

	public SafeGuardEventQuery(String id){
		super(id);
	}
	
	
	@ExcelField(title = "网点", type = 1, align = 2, sort = 1)
	public String getExcelOfficeName() {
		return office.getName();
	}
	
	@ExcelField(title = "时间",  align = 2, sort = 4)
	public String getExcelTime() {
		return time;
	}
	
	@ExcelField(title = "人员",  align = 2, sort = 2)
	public String getExcelPersonName() {
		return personName;
	}
	
	@ExcelField(title = "指纹号",  align = 2, sort = 3)
	private String getExcelFingerNumLabel() {
		return fingerNumLabel;
	}
	/*@ExcelField(title = "事件类型",  align = 2, sort = 3, dictType = "event_type")
	public String getExcelEventType() {
		return eventType;
	}*/
	
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

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	
}