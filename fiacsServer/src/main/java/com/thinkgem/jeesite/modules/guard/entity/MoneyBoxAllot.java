/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 款箱调拨Entity
 * @author Jumbo
 * @version 2017-07-29
 */
public class MoneyBoxAllot extends DataEntity<MoneyBoxAllot> {
	
	private static final long serialVersionUID = 1L;
	private String moneyBoxId;		// 款箱ID
	private String cardNum;//款箱卡号
	private String equipmentId;		// 设备ID
	private String taskScheduleId;		// 调拨ID
	private ClassTaskInfo classTaskInfo;
	private Date scheduleTime;		// 派送日期
	private String allotType;		// 调拨类型
	private Date downloadTime;		// 下载时间
	private String allotTimes;		
	private String allotTimeTwo;
	private Office office;
	private MoneyBox moneyBox;
	
	
	public MoneyBoxAllot() {
		super();
	}

	public MoneyBoxAllot(String id){
		super(id);
	}
	
	
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
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

	public String getAllotTimes() {
		return allotTimes;
	}

	public void setAllotTimes(String allotTimes) {
		this.allotTimes = allotTimes;
	}

	public ClassTaskInfo getClassTaskInfo() {
		return classTaskInfo;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public void setClassTaskInfo(ClassTaskInfo classTaskInfo) {
		this.classTaskInfo = classTaskInfo;
	}

	public String getAllotTimeTwo() {
		return allotTimeTwo;
	}

	public void setAllotTimeTwo(String allotTimeTwo) {
		this.allotTimeTwo = allotTimeTwo;
	}

	public String getMoneyBoxId() {
		return moneyBoxId;
	}

	public void setMoneyBoxId(String moneyBoxId) {
		this.moneyBoxId = moneyBoxId;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	public String getTaskScheduleId() {
		return taskScheduleId;
	}

	public void setTaskScheduleId(String taskScheduleId) {
		this.taskScheduleId = taskScheduleId;
	}
	
	@Length(min=1, max=1, message="调拨类型长度必须介于 1 和 1 之间")
	public String getAllotType() {
		return allotType;
	}

	public void setAllotType(String allotType) {
		this.allotType = allotType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="下载时间不能为空")
	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}
	
}