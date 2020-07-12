/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 人员信息管理Entity
 * @author Jumbo
 * @version 2017-06-27
 */
public class StaffExWork extends DataEntity<StaffExWork> {
	
	private static final long serialVersionUID = 1L;
	private String staffId;		// 人员ID 父类
	private String workName;		// 工作单位
	private String workTime;		// 工作时间段
	private String dept;		// 部门
	private String dupt;		// 职务
	private String certifier;		// 证明人
	
	public StaffExWork() {
		super();
	}

	public StaffExWork(String id){
		super(id);
	}

	public StaffExWork(Staff staffId){
		this.staffId = staffId.getId();
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	@Length(min=1, max=32, message="工作单位长度必须介于 1 和 32 之间")
	@ExcelField(title = "工作单位", align = 2, sort = 40)
	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}
	
	@Length(min=0, max=64, message="工作时间段长度必须介于 0 和 64 之间")
	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	
	@Length(min=0, max=64, message="部门长度必须介于 0 和 64 之间")
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	
	@Length(min=0, max=64, message="职务长度必须介于 0 和 64 之间")
	public String getDupt() {
		return dupt;
	}

	public void setDupt(String dupt) {
		this.dupt = dupt;
	}
	
	@Length(min=0, max=64, message="证明人长度必须介于 0 和 64 之间")
	public String getCertifier() {
		return certifier;
	}

	public void setCertifier(String certifier) {
		this.certifier = certifier;
	}
	
}