/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * access_door_timezoneEntity
 * @author demo
 * @version 2020-07-01
 */

public class TimezoneInfo extends DataEntity<TimezoneInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;	//归属设备
	private String doorPos;		// 门号 1 外门(1 号门) -- 2 内门(2 号门) 3 3 号门 -- 4 4 号门
	private String timeZoneType;		// 时区类型,1 :人员时区 2: 设备时区
	private String timeZoneNum;		// 时区号
	private String mon;				//周一
	private String tue;				//周二
	private String wed;				//周三
	private String thu;				//周四
	private String fri;				//周五
	private String sat;				//周六
	private String sun;				//周日

	private String equipmentId;
	private String accessParaInfoId;
	private AccessParaInfo accessParaInfo;	//归属门禁
	private List<Map<String,String>> times;

	private String weekNum;
	private String time2;

	public String getTime2() {
		return time2;
	}

	public void setTime2(String time2) {
		this.time2 = time2;
	}

	public String getWeekNum() {
		return weekNum;
	}

	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}

	public List<Map<String, String>> getTimes() {
		return times;
	}

	public void setTimes(List<Map<String, String>> times) {
		this.times = times;
	}

	public String getAccessParaInfoId() {
		return accessParaInfoId;
	}

	public void setAccessParaInfoId(String accessParaInfoId) {
		this.accessParaInfoId = accessParaInfoId;
	}

	public AccessParaInfo getAccessParaInfo() {
		return accessParaInfo;
	}

	public void setAccessParaInfo(AccessParaInfo accessParaInfo) {
		this.accessParaInfo = accessParaInfo;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
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

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public String getTue() {
		return tue;
	}

	public void setTue(String tue) {
		this.tue = tue;
	}

	public String getWed() {
		return wed;
	}

	public void setWed(String wed) {
		this.wed = wed;
	}

	public String getThu() {
		return thu;
	}

	public void setThu(String thu) {
		this.thu = thu;
	}

	public String getFri() {
		return fri;
	}

	public void setFri(String fri) {
		this.fri = fri;
	}

	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat = sat;
	}

	public String getSun() {
		return sun;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}
}