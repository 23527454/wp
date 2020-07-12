/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.guard.service.StaffHelper;

/**
 * 人员信息管理Entity
 * @author Jumbo
 * @version 2017-06-27
 */
public class FingerInfo extends DataEntity<FingerInfo> {
	
	private static final long serialVersionUID = 1L;
	private String staffId;		// 人员ID 父类
	private String fingerNum;		// 指纹号
	private String pwd;		// 密码
	private String coercePwd; //胁迫密码
	private String cardNum; //卡号
	private Date startDate;		// 起始时间
	private Date endDate;		// 截止时间
	private String areaId;		// 设备ID
	private byte[] fingerTemplate;		// 指纹模板数据
	private byte[] backupFp;		// 人员备份指纹
	private byte[] coerceTemplate; //人员胁迫指纹
	private String host;		// 主机信息
	private String beginFingerNum;		// 开始 指纹号
	private String endFingerNum;		// 结束 指纹号
	
	public FingerInfo() {
		super();
	}

	public FingerInfo(String id){
		super(id);
	}

	public FingerInfo(Staff staffId){
		if(staffId!=null)
			this.staffId = staffId.getId();
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	public String getFingerNum() {
		return fingerNum;
	}

	public void setFingerNum(String fingerNum) {
		this.fingerNum = fingerNum;
	}
	
	@Length(min=0, max=8, message="密码长度必须介于 0 和 8 之间")
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	@Length(min=0, max=8, message="密码长度必须介于 0 和 8 之间")
	public String getCoercePwd() {
		return coercePwd;
	}

	public void setCoercePwd(String coercePwd) {
		this.coercePwd = coercePwd;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="起始时间不能为空")
	@ExcelField(title = "起始时间", align = 2, sort = 40)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="截止时间不能为空")
	@ExcelField(title = "截止时间", align = 2, sort = 50)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public byte[] getFingerTemplate() {
		return fingerTemplate;
	}

	public void setFingerTemplate(byte[] fingerTemplate) {
		this.fingerTemplate = fingerTemplate;
	}
	
	public byte[] getBackupFp() {
		return backupFp;
	}

	public void setBackupFp(byte[] backupFp) {
		this.backupFp = backupFp;
	}

	public byte[] getCoerceTemplate() {
		return coerceTemplate;
	}

	public void setCoerceTemplate(byte[] coerceTemplate) {
		this.coerceTemplate = coerceTemplate;
	}

	@Length(min=0, max=64, message="主机信息长度必须介于 0 和 64 之间")
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getBeginFingerNum() {
		return beginFingerNum;
	}

	public void setBeginFingerNum(String beginFingerNum) {
		this.beginFingerNum = beginFingerNum;
	}
	
	public String getEndFingerNum() {
		return endFingerNum;
	}

	public void setEndFingerNum(String endFingerNum) {
		this.endFingerNum = endFingerNum;
	}
		
	
	public String getFingerNumLabel() {
		//return StaffHelper.buildFingerNum(areaId, fingerNum);
		return fingerNum.length()==9 ? fingerNum : StaffHelper.buildFingerNum(areaId, fingerNum);
	}

}