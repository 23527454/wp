/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 款箱调拨同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadMoneyBoxAllot extends DownloadEntity<DownloadMoneyBoxAllot> {
	
	private static final long serialVersionUID = 1L;
	private String moneyBoxId;		// 钱箱ID
	private String equipmentId;		// 设备ID
	private String classTaskId;		// 班组编号
	private String taskScheduleId;		// 任务ID
	private Office office;		// 网点
	private MoneyBox moneyBox;		// 款箱
	private String downloadTimeTwo;		// 下载时间
	private List<String> officeIds;
	
	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
	}

	public DownloadMoneyBoxAllot() {
		super();
	}

	public DownloadMoneyBoxAllot(String id){
		super(id);
	}

	public String getDownloadTimeTwo() {
		return downloadTimeTwo;
	}

	public void setDownloadTimeTwo(String downloadTimeTwo) {
		this.downloadTimeTwo = downloadTimeTwo;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public MoneyBox getMoneyBox() {
		return moneyBox;
	}

	public void setMoneyBox(MoneyBox moneyBox) {
		this.moneyBox = moneyBox;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	
	public String getMoneyBoxId() {
		return moneyBoxId;
	}

	public void setMoneyBoxId(String moneyBoxId) {
		this.moneyBoxId = moneyBoxId;
	}

	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}

	public String getTaskScheduleId() {
		return taskScheduleId;
	}

	public void setTaskScheduleId(String taskScheduleId) {
		this.taskScheduleId = taskScheduleId;
	}

}