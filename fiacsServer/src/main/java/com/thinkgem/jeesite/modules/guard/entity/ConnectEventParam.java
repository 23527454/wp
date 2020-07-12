/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 车辆管理生成Entity
 * @author Jumbo
 * @version 2017-06-26
 */
public class ConnectEventParam extends DataEntity<ConnectEventParam> {
	
	private static final long serialVersionUID = 1L;

	private List<CarEvent> carList;						// 款箱
	private List<CommissionerEvent> commissionerList;	// 专员
	private List<EventDetail> eventDetailList;			// 押款员
	private List<MoneyBoxEventDetail> moneyBoxList;		// 款箱
	private List<MoneyBoxEvent> moneyBoxEventList;		// 款箱
	private List<Line> lineList;						// 线路
	
	private String officeId;
	
	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public List<MoneyBoxEvent> getMoneyBoxEventList() {
		return moneyBoxEventList;
	}
	public void setMoneyBoxEventList(List<MoneyBoxEvent> moneyBoxEventList) {
		this.moneyBoxEventList = moneyBoxEventList;
	}
	private String taskType;
	
	public List<CarEvent> getCarList() {
		return carList;
	}
	public void setCarList(List<CarEvent> carList) {
		this.carList = carList;
	}
	public List<CommissionerEvent> getCommissionerList() {
		return commissionerList;
	}
	public void setCommissionerList(List<CommissionerEvent> commissionerList) {
		this.commissionerList = commissionerList;
	}
	public List<EventDetail> getEventDetailList() {
		return eventDetailList;
	}
	public void setEventDetailList(List<EventDetail> eventDetailList) {
		this.eventDetailList = eventDetailList;
	}
	public List<MoneyBoxEventDetail> getMoneyBoxList() {
		return moneyBoxList;
	}
	public void setMoneyBoxList(List<MoneyBoxEventDetail> moneyBoxList) {
		this.moneyBoxList = moneyBoxList;
	}
	public List<Line> getLineList() {
		return lineList;
	}
	public void setLineList(List<Line> lineList) {
		this.lineList = lineList;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}


}