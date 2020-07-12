package com.thinkgem.jeesite.modules.guard.entity;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class TbDoorTimeZone extends DataEntity<TbDoorTimeZone>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String equipmentId;
	
	private String equipSn;
	
	private Integer doorPos;
	
	private Integer timeZoneNumber;
	
	private Integer weekNumber;
	
	private String timeFrameStart1;
	
	private String timeFrameEnd1;
	
	private String timeFrameStart2;
	
	private String timeFrameEnd2;
	
	private String timeFrameStart3;
	
	private String timeFrameEnd3;
	
	private String timeFrameStart4;
	
	private String timeFrameEnd4;

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipSn() {
		return equipSn;
	}

	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}

	public Integer getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(Integer doorPos) {
		this.doorPos = doorPos;
	}
	
	public Integer getTimeZoneNumber() {
		return timeZoneNumber;
	}

	public void setTimeZoneNumber(Integer timeZoneNumber) {
		this.timeZoneNumber = timeZoneNumber;
	}
	
	public Integer getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}
	
	public String getTimeFrameStart1() {
		return timeFrameStart1;
	}

	public void setTimeFrameStart1(String timeFrameStart1) {
		this.timeFrameStart1 = timeFrameStart1;
	}
	
	public String getTimeFrameEnd1() {
		return timeFrameEnd1;
	}

	public void setTimeFrameEnd1(String timeFrameEnd1) {
		this.timeFrameEnd1 = timeFrameEnd1;
	}

	public String getTimeFrameStart2() {
		return timeFrameStart2;
	}

	public void setTimeFrameStart2(String timeFrameStart2) {
		this.timeFrameStart2 = timeFrameStart2;
	}

	public String getTimeFrameEnd2() {
		return timeFrameEnd2;
	}

	public void setTimeFrameEnd2(String timeFrameEnd2) {
		this.timeFrameEnd2 = timeFrameEnd2;
	}

	public String getTimeFrameStart3() {
		return timeFrameStart3;
	}

	public void setTimeFrameStart3(String timeFrameStart3) {
		this.timeFrameStart3 = timeFrameStart3;
	}

	public String getTimeFrameEnd3() {
		return timeFrameEnd3;
	}

	public void setTimeFrameEnd3(String timeFrameEnd3) {
		this.timeFrameEnd3 = timeFrameEnd3;
	}

	public String getTimeFrameStart4() {
		return timeFrameStart4;
	}

	public void setTimeFrameStart4(String timeFrameStart4) {
		this.timeFrameStart4 = timeFrameStart4;
	}

	public String getTimeFrameEnd4() {
		return timeFrameEnd4;
	}

	public void setTimeFrameEnd4(String timeFrameEnd4) {
		this.timeFrameEnd4 = timeFrameEnd4;
	}
}
