/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.service.StaffHelper;
import org.hibernate.validator.constraints.Length;

/**
 * 维保员派遣Entity
 */
public class DispatchPersonInfo extends DataEntity<DispatchPersonInfo> {

	private static final long serialVersionUID = 1L;
	private String dispatchId;		// 派遣ID 父类
	private String staffId;		// 维保员ID
	private String fingerNum;		// 维保员指纹号
	private String name;     // 姓名
	private String identifyNumber;  // 证件号
	private String delete="1";    // 删除标记
	private String officeId;
	private String areaId;
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public DispatchPersonInfo() {
		super();
	}

	public DispatchPersonInfo(SafeGuardDispatch safeGuardDispatch){
		if(safeGuardDispatch != null) {
			this.dispatchId = safeGuardDispatch.getId();
		}
	}
	
	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getDispatchId() {
		return dispatchId;
	}

	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	@Length(min=1, max=6, message="押款员指纹号长度必须介于 1 和 6 之间")
	public String getFingerNum() {
		return fingerNum;
	}

	public void setFingerNum(String fingerNum) {
		this.fingerNum = fingerNum;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getFingerNumLabel() {
		//return StaffHelper.buildFingerNum(areaId, fingerNum);
		return fingerNum.length()==9 ? fingerNum : StaffHelper.buildFingerNum(areaId, fingerNum);
	}
}