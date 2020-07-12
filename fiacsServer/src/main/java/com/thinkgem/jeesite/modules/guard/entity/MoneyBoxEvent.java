/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 款箱事件Entity
 * @author Jumbo
 * @version 2017-08-01
 */
public class MoneyBoxEvent extends DataEntity<MoneyBoxEvent> {
	
	private static final long serialVersionUID = 1L;
	private String recordId;		// 记录序号
	private String equipmentId;		// 设备ID
	private String equipSn;		// 设备序列号
	private String time;		// 时间
	private String classTaskId;		// 时间
	private String taskId;		// 任务ID
	
	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}

	private List<MoneyBoxEventDetail> moneyBoxEventDetailList = Lists.newArrayList();		// 子表列表
	
	public List<MoneyBoxEventDetail> getMoneyBoxEventDetailList() {
		return moneyBoxEventDetailList;
	}

	public void setMoneyBoxEventDetailList(
			List<MoneyBoxEventDetail> moneyBoxEventDetailList) {
		this.moneyBoxEventDetailList = moneyBoxEventDetailList;
	}

	public MoneyBoxEvent() {
		super();
	}

	public MoneyBoxEvent(String id){
		super(id);
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	//@Length(min=1, max=32, message="设备序列号长度必须介于 1 和 32 之间")
	
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
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}