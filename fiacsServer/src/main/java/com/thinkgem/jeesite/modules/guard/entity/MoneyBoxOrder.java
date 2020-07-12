/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 款箱预约Entity
 * @author Jumbo
 * @version 2017-07-29
 */
public class MoneyBoxOrder extends DataEntity<MoneyBoxOrder> {
	
	private static final long serialVersionUID = 1L;
	private String moneyBoxId;		// 款箱ID
	private String cardNum;
	private String equipmentId;		// 设备ID
	private String moneyBoxOrderId;		// 预约ID
	private String orderType;		// 预约类型
	private Date allotReturnTime;		// 回送日期
	private Date orderTime;		// 预约时间
	private Date uploadTime;		// 上传时间
	private String orderSysStatus;		// 预约状态
	private Office office;
	private MoneyBox moneyBox;
	private String  time;
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private Date orderTimeTo;		// 预约时间
	public MoneyBoxOrder() {
		super();
	}

	public MoneyBoxOrder(String id){
		super(id);
	}
	
	

	public Date getOrderTimeTo() {
		return orderTimeTo;
	}

	public void setOrderTimeTo(Date orderTimeTo) {
		this.orderTimeTo = orderTimeTo;
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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
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
	
	public String getMoneyBoxOrderId() {
		return moneyBoxOrderId;
	}

	public void setMoneyBoxOrderId(String moneyBoxOrderId) {
		this.moneyBoxOrderId = moneyBoxOrderId;
	}
	
	@Length(min=1, max=1, message="预约类型长度必须介于 1 和 1 之间")
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="回送日期不能为空")
	public Date getAllotReturnTime() {
		return allotReturnTime;
	}

	public void setAllotReturnTime(Date allotReturnTime) {
		this.allotReturnTime = allotReturnTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="预约时间不能为空")
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="上传时间不能为空")
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	@Length(min=1, max=1, message="预约状态长度必须介于 1 和 1 之间")
	public String getOrderSysStatus() {
		return orderSysStatus;
	}

	public void setOrderSysStatus(String orderSysStatus) {
		this.orderSysStatus = orderSysStatus;
	}
	
}