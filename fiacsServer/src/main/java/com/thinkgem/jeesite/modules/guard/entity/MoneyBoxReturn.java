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
 * 款箱上缴Entity
 * @author Jumbo
 * @version 2017-07-29
 */
public class MoneyBoxReturn extends DataEntity<MoneyBoxReturn> {
	
	private static final long serialVersionUID = 1L;
	private String moneyBoxId;		// 款箱ID
	private String cardNum;
	
	private String equipmentId;		// 设备ID
	private String moneyBoxReturnId;		// 上缴ID
	private String returnType;		// 上缴类型
	private Date returnTime;		// 上缴时间
	private Date uploadTime;		// 上传时间
	private String returnSysStatus;		// 上缴状态
	private Office office;
	private MoneyBox moneyBox;
	
	private Date returnTimeTo;		// 上缴时间截至
	
	public MoneyBoxReturn() {
		super();
	}

	public MoneyBoxReturn(String id){
		super(id);
	}
	
	
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Date getReturnTimeTo() {
		return returnTimeTo;
	}

	public void setReturnTimeTo(Date returnTimeTo) {
		this.returnTimeTo = returnTimeTo;
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
	
	public String getMoneyBoxReturnId() {
		return moneyBoxReturnId;
	}

	public void setMoneyBoxReturnId(String moneyBoxReturnId) {
		this.moneyBoxReturnId = moneyBoxReturnId;
	}
	
	@Length(min=1, max=1, message="上缴类型长度必须介于 1 和 1 之间")
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="上缴时间不能为空")
	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="上传时间不能为空")
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	@Length(min=1, max=1, message="上缴状态长度必须介于 1 和 1 之间")
	public String getReturnSysStatus() {
		return returnSysStatus;
	}

	public void setReturnSysStatus(String returnSysStatus) {
		this.returnSysStatus = returnSysStatus;
	}
	
}