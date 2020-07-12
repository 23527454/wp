/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import javax.validation.constraints.NotNull;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 款箱信息Entity
 * @author Jumbo
 * @version 2017-06-27
 */
public class MoneyBox extends DataEntity<MoneyBox> {
	
	private static final long serialVersionUID = 1L;
	private String boxCode;		// 款箱编码
	private Office office;		// 归属部门
	private String boxType;		// 款箱类型
	private String cardNum;		// 款箱卡号
	private String doorStatus;		// 箱门状态
	private String powerInfo;		// 电量信息
	private String closePwd;		// 设封密钥
	private String alarmStatus;		// 异常状态
	private Date alarmDate;		// 异常时间
	private Date closeDate;		// 设封时间
	private String sensorStatus;		// 传感器状态
	private String boxOrder;          // 去除表中与款箱预约表中重复数据
	private Area area;
	
	
	public static final String BOXTYPE_MORNING_AND_EVENING = "0";//早晚款箱
	public static final String BOXTYPE_ALLOCATION = "1";//调拨款箱
	
	public MoneyBox() {
		super();
	}
	
	public MoneyBox(MoneyBoxEventDetail moneyBoxEventDetail){
		this.cardNum = moneyBoxEventDetail.getCardNum();
	}

	public MoneyBox(String id){
		super(id);
	}
	
	public String getBoxOrder() {
		return boxOrder;
	}

	public void setBoxOrder(String boxOrder) {
		this.boxOrder = boxOrder;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min=1, max=16, message="款箱编码长度必须介于 1 和 16 之间")
	@ExcelField(title = "款箱编码", align = 2, sort = 1)
	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}
	
	@ExcelField(title = "所属网点", align = 2, sort = 4)
	public String getOfficeName(){
		return office.getName();
	}
	
	@NotNull(message="归属部门不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=1, max=1, message="款箱类型长度必须介于 1 和 1 之间")
	@ExcelField(title = "款箱类型", align = 2, sort = 3)
	public String getBoxType() {
		return boxType;
	}

	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}
	
	@Length(min=1, max=12, message="款箱卡号长度必须介于 1 和 12 之间")
	@ExcelField(title = "款箱卡号", align = 2, sort = 2)
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	@Length(min=0, max=1, message="箱门状态长度必须介于 0 和 1 之间")
	public String getDoorStatus() {
		return doorStatus;
	}

	public void setDoorStatus(String doorStatus) {
		this.doorStatus = doorStatus;
	}
	
	public String getPowerInfo() {
		return powerInfo;
	}

	public void setPowerInfo(String powerInfo) {
		this.powerInfo = powerInfo;
	}
	
	public String getClosePwd() {
		return closePwd;
	}

	public void setClosePwd(String closePwd) {
		this.closePwd = closePwd;
	}
	
	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
	public String getSensorStatus() {
		return sensorStatus;
	}

	public void setSensorStatus(String sensorStatus) {
		this.sensorStatus = sensorStatus;
	}
	
}