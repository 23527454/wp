/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * access_door_timezoneEntity
 * @author demo
 * @version 2020-07-01
 */

public class TimezoneInfo extends DataEntity<TimezoneInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;	//归属设备
	private AccessParaInfo accessParaInfo;	//归属门禁
	private String doorPos;		// 门号 1 外门(1 号门) -- 2 内门(2 号门) 3 3 号门 -- 4 4 号门
	private String timeZoneType;		// 时区类型,1 :人员时区 2: 设备时区
	private String timeZoneNum;		// 时区号
	private Integer weekNumber;		//星期几
	private String timeStart1;		// 时段1开始
	private String timeEnd1;		// 时段1结束
	private String timeStart2;		// 时段2开始
	private String timeEnd2;		// 时段2结束
	private String timeStart3;		// 时段3开始
	private String timeEnd3;		// 时段3结束
	private String timeStart4;		// 时段4开始
	private String timeEnd4;		// 时段4结束
	private Integer equipmentId;

	public AccessParaInfo getAccessParaInfo() {
		return accessParaInfo;
	}

	public void setAccessParaInfo(AccessParaInfo accessParaInfo) {
		this.accessParaInfo = accessParaInfo;
	}

	public Integer getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	public TimezoneInfo() {
		this(null);
	}

	public TimezoneInfo(String id){
		super(id);
	}

	@NotNull(message = "所属设备不能为空")
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@ExcelField(title = "网点名称", align = 2, sort = 1)
	public String getEquipmentControlPos(){
		return equipment.getControlPos();
	}

	@ExcelField(title = "门控类型", align = 2, sort = 2,dictType = "site_type")
	public String getEquipmentSiteType(){
		return equipment.getSiteType();
	}


	@ExcelField(title = "门号", align = 2, sort = 10, dictType = "door_pos")
	public String getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(String doorPos) {
		this.doorPos = doorPos;
	}

	@ExcelField(title = "时区号", align = 2, sort = 15, dictType = "time_zone_type")
	public String getTimeZoneType() {
		return timeZoneType;
	}

	public void setTimeZoneType(String timeZoneType) {
		this.timeZoneType = timeZoneType;
	}

	@ExcelField(title = "时区号", align = 2, sort = 20, dictType = "time_zone_num")
	public String getTimeZoneNum() {
		return timeZoneNum;
	}

	public void setTimeZoneNum(String timeZoneNum) {
		this.timeZoneNum = timeZoneNum;
	}
	
	@NotBlank(message="时段1开始不能为空")
	@Length(min=0, max=5, message="时段1开始长度不能超过 5 个字符")
	@ExcelField(title = "时段1开始时间", align = 2, sort = 25)
	public String getTimeStart1() {
		return timeStart1;
	}

	public void setTimeStart1(String timeStart1) {
		this.timeStart1 = timeStart1;
	}
	
	@NotBlank(message="时段1结束不能为空")
	@Length(min=0, max=5, message="时段1结束长度不能超过 5 个字符")
	@ExcelField(title = "时段1开始时间", align = 2, sort = 30)
	public String getTimeEnd1() {
		return timeEnd1;
	}

	public void setTimeEnd1(String timeEnd1) {
		this.timeEnd1 = timeEnd1;
	}
	
	@NotBlank(message="时段2开始不能为空")
	@Length(min=0, max=5, message="时段2开始长度不能超过 5 个字符")
	@ExcelField(title = "时段2开始时间", align = 2, sort = 35)
	public String getTimeStart2() {
		return timeStart2;
	}

	public void setTimeStart2(String timeStart2) {
		this.timeStart2 = timeStart2;
	}
	
	@NotBlank(message="时段2结束不能为空")
	@Length(min=0, max=5, message="时段2结束长度不能超过 5 个字符")
	@ExcelField(title = "时段2结束时间", align = 2, sort = 40)
	public String getTimeEnd2() {
		return timeEnd2;
	}

	public void setTimeEnd2(String timeEnd2) {
		this.timeEnd2 = timeEnd2;
	}
	
	@NotBlank(message="时段3开始不能为空")
	@Length(min=0, max=5, message="时段3开始长度不能超过 5 个字符")
	@ExcelField(title = "时段3开始时间", align = 2, sort = 45)
	public String getTimeStart3() {
		return timeStart3;
	}

	public void setTimeStart3(String timeStart3) {
		this.timeStart3 = timeStart3;
	}
	
	@NotBlank(message="时段3结束不能为空")
	@Length(min=0, max=5, message="时段3结束长度不能超过 5 个字符")
	@ExcelField(title = "时段3结束时间", align = 2, sort = 50)
	public String getTimeEnd3() {
		return timeEnd3;
	}

	public void setTimeEnd3(String timeEnd3) {
		this.timeEnd3 = timeEnd3;
	}
	
	@NotBlank(message="时段4开始不能为空")
	@Length(min=0, max=5, message="时段4开始长度不能超过 5 个字符")
	@ExcelField(title = "时段4开始时间", align = 2, sort = 55)
	public String getTimeStart4() {
		return timeStart4;
	}

	public void setTimeStart4(String timeStart4) {
		this.timeStart4 = timeStart4;
	}
	
	@NotBlank(message="时段4结束不能为空")
	@Length(min=0, max=5, message="时段4结束长度不能超过 5 个字符")
	@ExcelField(title = "时段4结束时间", align = 2, sort = 60)
	public String getTimeEnd4() {
		return timeEnd4;
	}

	public void setTimeEnd4(String timeEnd4) {
		this.timeEnd4 = timeEnd4;
	}
}