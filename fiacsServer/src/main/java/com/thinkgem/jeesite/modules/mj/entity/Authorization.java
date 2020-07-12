/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.Date;

/**
 * power_authorizationEntity
 * @author demo
 * @version 2020-07-04
 */

public class Authorization extends DataEntity<Authorization> {
	
	private static final long serialVersionUID = 1L;
	private Staff staff;		// 用户ID
	private Office office;		// 机构ID
	private String cardNum;		// 卡号
	private Date validityDate;		// 门禁有效期
	private Integer timezoneInfoNum;		// 通行时区
	private String staffId;
	private String officeId;
	private String staffName;
	private String workNum;
	private String accessParaInfoId;
	private String staffGroup;
	private String checkPom;
	private String workDayNum;


	public Authorization( Integer timezoneInfoNum, String staffId, String accessParaInfoId, String staffGroup, String checkPom, String workDayNum) {
		this.timezoneInfoNum = timezoneInfoNum;
		this.staffId = staffId;
		this.accessParaInfoId = accessParaInfoId;
		this.staffGroup = staffGroup;
		this.checkPom = checkPom;
		this.workDayNum = workDayNum;
	}

	public Authorization() {
		super();
	}

	public Authorization(String id){
		super(id);
	}

	public Authorization(Office office) {
		this.office = office;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}

	public String getCheckPom() {
		return checkPom;
	}

	public void setCheckPom(String checkPom) {
		this.checkPom = checkPom;
	}

	public String getWorkDayNum() {
		return workDayNum;
	}

	public void setWorkDayNum(String workDayNum) {
		this.workDayNum = workDayNum;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public Integer getTimezoneInfoNum() {
		return timezoneInfoNum;
	}

	public void setTimezoneInfoNum(Integer timezoneInfoNum) {
		this.timezoneInfoNum = timezoneInfoNum;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getWorkNum() {
		return workNum;
	}

	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}

	public String getAccessParaInfoId() {
		return accessParaInfoId;
	}

	public void setAccessParaInfoId(String accessParaInfoId) {
		this.accessParaInfoId = accessParaInfoId;
	}
}