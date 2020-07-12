package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class TbAccessParameters extends DataEntity<TbAccessParameters>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String equipmentId;
	
	private String equipSn;
	
	private Integer doorPos=0;

	private Integer openType;

	private Integer relayActionTime=50;
	
	private Integer delayCloseTime=50;
	
	private Integer alarmTime=50;
	
	private Integer timeZoneNumber=1;
	
	private Integer combinationNumber=1;
	
	private Integer centerPermit=0;

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

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

	public Integer getRelayActionTime() {
		return relayActionTime;
	}

	public void setRelayActionTime(Integer relayActionTime) {
		this.relayActionTime = relayActionTime;
	}

	public Integer getDelayCloseTime() {
		return delayCloseTime;
	}

	public void setDelayCloseTime(Integer delayCloseTime) {
		this.delayCloseTime = delayCloseTime;
	}

	public Integer getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Integer alarmTime) {
		this.alarmTime = alarmTime;
	}

	public Integer getTimeZoneNumber() {
		return timeZoneNumber;
	}

	public void setTimeZoneNumber(Integer timeZoneNumber) {
		this.timeZoneNumber = timeZoneNumber;
	}

	public Integer getCombinationNumber() {
		return combinationNumber;
	}

	public void setCombinationNumber(Integer combinationNumber) {
		this.combinationNumber = combinationNumber;
	}

	public Integer getCenterPermit() {
		return centerPermit;
	}

	public void setCenterPermit(Integer centerPermit) {
		this.centerPermit = centerPermit;
	}
	
	
}
