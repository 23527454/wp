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
public class DoorAlarmEventQuery extends DataEntity<DoorAlarmEventQuery> {
	
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
	private Integer alarmStatus;
	private Integer handleStatus;
	private String handleTime;
	private String handleUserNameId;
	private String handleUserName;
	private Integer handleMode;
	private Integer doorPos;
	private String eventType;
	private String fingerNumLabel;
	private String personName;
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getHandleUserName() {
		return handleUserName;
	}
	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}
	public String getFingerNumLabel() {
		return fingerNumLabel;
	}
	public void setFingerNumLabel(String fingerNumLabel) {
		this.fingerNumLabel = fingerNumLabel;
	}
	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}
	public String getTimeTwo() {
		return timeTwo;
	}
	public DoorAlarmEventQuery() {
		super();
	}

	public DoorAlarmEventQuery(String id){
		super(id);
	}
	
	
	@ExcelField(title = "网点", type = 1, align = 2, sort = 1)
	public String getExcelOfficeName() {
		return office.getName();
	}
	
	@ExcelField(title = "报警人",  align = 2, sort = 2)
	public String getExcelPersonName() {
		return personName;
	}
	
	@ExcelField(title = "指纹号",  align = 2, sort = 3)
	public String getExcelFingerNum() {
		return fingerNumLabel;
	}
	
	@ExcelField(title = "报警时间",  align = 2, sort = 4)
	public String getExcelTime() {
		return time;
	}
	
	@ExcelField(title = "门号",  align = 2, sort = 5, dictType = "door_pos")
	public String getExcelDoorPos() {
		return String.valueOf(doorPos);
	}
	
	@ExcelField(title = "事件类型",  align = 2, sort = 6, dictType = "door_alarm_event_type")
	public String getExcelEventType() {
		return eventType;
	}
	
	@ExcelField(title = "报警状态",  align = 2, sort = 7, dictType = "door_alarm_status")
	public String getExcelAlarmStatus() {
		return String.valueOf(alarmStatus);
	}
	
	@ExcelField(title = "处理状态",  align = 2, sort = 8, dictType = "door_handle_status")
	public String getExcelHandleStatus() {
		return String.valueOf(handleStatus);
	}
	
	@ExcelField(title = "处理人",  align = 2, sort = 9)
	public String getExcelHandleUserName() {
		return handleUserName;
	}
	
	@ExcelField(title = "处理方式",  align = 2, sort = 10, dictType = "door_alarm_handle_mode")
	public String getExcelHandleMode() {
		return String.valueOf(handleMode);
	}
	
	@ExcelField(title = "接警时间",  align = 2, sort = 11)
	public String getExcelHandleTime() {
		return handleTime;
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

	public Integer getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(Integer alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleUserNameId() {
		return handleUserNameId;
	}

	public void setHandleUserNameId(String handleUserNameId) {
		this.handleUserNameId = handleUserNameId;
	}

	public Integer getHandleMode() {
		return handleMode;
	}

	public void setHandleMode(Integer handleMode) {
		this.handleMode = handleMode;
	}

	public Integer getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(Integer doorPos) {
		this.doorPos = doorPos;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}