/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.service.StaffHelper;

/**
 * 班组Entity
 * @author Jumbo
 * @version 2017-07-29
 */
public class ClassPersonInfo extends DataEntity<ClassPersonInfo> {
	
	private static final long serialVersionUID = 1L;
	private String classTaskId;		// 班组ID 父类
	private String personId;		// 押款员ID
	private String fingerNum;		// 押款员指纹号
	private String name;     // 姓名
	private String identifyNumber;  // 证件号
	private String delete="1";    // 删除标记
	
	private String areaId;

	private String workStatus;

	private String endDate;

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public ClassPersonInfo() {
		super();
	}

	public ClassPersonInfo(String id){
		super(id);
	}

	public ClassPersonInfo(ClassTaskInfo classTaskId){
		if(classTaskId != null) {
			this.classTaskId = classTaskId.getId();
			if(classTaskId.getStaffTwo()!=null) {
				this.name = classTaskId.getStaffTwo().getName();
			}
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

	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}
	
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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