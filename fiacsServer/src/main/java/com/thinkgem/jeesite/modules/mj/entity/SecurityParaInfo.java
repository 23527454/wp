/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;

/**
 * access_antitheftEntity
 * @author demo
 * @version 2020-07-02
 */

public class SecurityParaInfo extends DataEntity<SecurityParaInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;		//设备信息
	private Integer leaveRelayTime;		// 布防离开等待时间（秒）
	private Integer doorSensorTime;		// 门磁自动布防时间（秒）
	private Integer localTipsTime;		// 现场未布防提醒时间（秒）
	private Integer tipsAlarmTime;		// 现场未布防提醒告警时间（秒）
	private Integer alarmIntervalTime;		// 同类报警间隔时间（分）
	private String allowDoorSensorOpen;		// 允许门磁自动布防
	private String allowDoorButtonOpen;		// 允许出门按钮自动布防
	private String allowButtonOpen;		// 允许按钮布防
	private String allowRemoteOpen;		// 允许远程布防
	private String allowAuthClose;		// 允许进人员验证撤防
	private String allowButtonClose;		// 允许按钮撤防
	private String allowRemoteClose;		// 允许远程撤防
	private String allowAuthRelieve;		// 允许人员验证解除告警
	private String allowButtonRelieve;		// 允许按钮解除告警
	private String allowPowerAlarm;		// 允许市电断电
	private String allowBatteryAlarm;		// 允许电池低电压
	private String remarks;					//备注信息

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public SecurityParaInfo(Integer leaveRelayTime, Integer doorSensorTime, Integer localTipsTime, Integer tipsAlarmTime, Integer alarmIntervalTime, String allowDoorSensorOpen, String allowDoorButtonOpen, String allowButtonOpen, String allowRemoteOpen, String allowAuthClose, String allowButtonClose, String allowRemoteClose, String allowAuthRelieve, String allowButtonRelieve, String allowPowerAlarm, String allowBatteryAlarm) {
		this.leaveRelayTime = leaveRelayTime;
		this.doorSensorTime = doorSensorTime;
		this.localTipsTime = localTipsTime;
		this.tipsAlarmTime = tipsAlarmTime;
		this.alarmIntervalTime = alarmIntervalTime;
		this.allowDoorSensorOpen = allowDoorSensorOpen;
		this.allowDoorButtonOpen = allowDoorButtonOpen;
		this.allowButtonOpen = allowButtonOpen;
		this.allowRemoteOpen = allowRemoteOpen;
		this.allowAuthClose = allowAuthClose;
		this.allowButtonClose = allowButtonClose;
		this.allowRemoteClose = allowRemoteClose;
		this.allowAuthRelieve = allowAuthRelieve;
		this.allowButtonRelieve = allowButtonRelieve;
		this.allowPowerAlarm = allowPowerAlarm;
		this.allowBatteryAlarm = allowBatteryAlarm;
	}

	public SecurityParaInfo() {
		this(null);
	}

	public SecurityParaInfo(String id){
		super(id);
	}
	
	public Integer getLeaveRelayTime() {
		return leaveRelayTime;
	}

	public void setLeaveRelayTime(Integer leaveRelayTime) {
		this.leaveRelayTime = leaveRelayTime;
	}
	
	public Integer getDoorSensorTime() {
		return doorSensorTime;
	}

	public void setDoorSensorTime(Integer doorSensorTime) {
		this.doorSensorTime = doorSensorTime;
	}
	
	public Integer getLocalTipsTime() {
		return localTipsTime;
	}

	public void setLocalTipsTime(Integer localTipsTime) {
		this.localTipsTime = localTipsTime;
	}
	
	public Integer getTipsAlarmTime() {
		return tipsAlarmTime;
	}

	public void setTipsAlarmTime(Integer tipsAlarmTime) {
		this.tipsAlarmTime = tipsAlarmTime;
	}
	
	public Integer getAlarmIntervalTime() {
		return alarmIntervalTime;
	}

	public void setAlarmIntervalTime(Integer alarmIntervalTime) {
		this.alarmIntervalTime = alarmIntervalTime;
	}
	
	public String getAllowDoorSensorOpen() {
		return allowDoorSensorOpen;
	}

	public void setAllowDoorSensorOpen(String allowDoorSensorOpen) {
		this.allowDoorSensorOpen = allowDoorSensorOpen;
	}
	
	public String getAllowDoorButtonOpen() {
		return allowDoorButtonOpen;
	}

	public void setAllowDoorButtonOpen(String allowDoorButtonOpen) {
		this.allowDoorButtonOpen = allowDoorButtonOpen;
	}
	
	public String getAllowButtonOpen() {
		return allowButtonOpen;
	}

	public void setAllowButtonOpen(String allowButtonOpen) {
		this.allowButtonOpen = allowButtonOpen;
	}
	
	public String getAllowRemoteOpen() {
		return allowRemoteOpen;
	}

	public void setAllowRemoteOpen(String allowRemoteOpen) {
		this.allowRemoteOpen = allowRemoteOpen;
	}
	
	public String getAllowAuthClose() {
		return allowAuthClose;
	}

	public void setAllowAuthClose(String allowAuthClose) {
		this.allowAuthClose = allowAuthClose;
	}
	
	public String getAllowButtonClose() {
		return allowButtonClose;
	}

	public void setAllowButtonClose(String allowButtonClose) {
		this.allowButtonClose = allowButtonClose;
	}
	
	public String getAllowRemoteClose() {
		return allowRemoteClose;
	}

	public void setAllowRemoteClose(String allowRemoteClose) {
		this.allowRemoteClose = allowRemoteClose;
	}
	
	public String getAllowAuthRelieve() {
		return allowAuthRelieve;
	}

	public void setAllowAuthRelieve(String allowAuthRelieve) {
		this.allowAuthRelieve = allowAuthRelieve;
	}
	
	public String getAllowButtonRelieve() {
		return allowButtonRelieve;
	}

	public void setAllowButtonRelieve(String allowButtonRelieve) {
		this.allowButtonRelieve = allowButtonRelieve;
	}
	
	public String getAllowPowerAlarm() {
		return allowPowerAlarm;
	}

	public void setAllowPowerAlarm(String allowPowerAlarm) {
		this.allowPowerAlarm = allowPowerAlarm;
	}
	
	public String getAllowBatteryAlarm() {
		return allowBatteryAlarm;
	}

	public void setAllowBatteryAlarm(String allowBatteryAlarm) {
		this.allowBatteryAlarm = allowBatteryAlarm;
	}
}