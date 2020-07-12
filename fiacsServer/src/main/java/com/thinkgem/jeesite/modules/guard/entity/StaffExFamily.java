/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 人员信息管理Entity
 * @author Jumbo
 * @version 2017-06-27
 */
public class StaffExFamily extends DataEntity<StaffExFamily> {
	
	private static final long serialVersionUID = 1L;
	private String staffId;		// 人员ID 父类
	private String name;		// 姓名
	private String age;		// 年龄
	private String relation;		// 关系
	private String work;		// 工作单位
	private String phone;		// 联系方式
	
	public StaffExFamily() {
		super();
	}

	public StaffExFamily(String id){
		super(id);
	}

	public StaffExFamily(Staff staffId){
		this.staffId = staffId.getId();
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	@Length(min=1, max=32, message="姓名长度必须介于 1 和 32 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	@Length(min=0, max=20, message="关系长度必须介于 0 和 20 之间")
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	@Length(min=0, max=64, message="工作单位长度必须介于 0 和 64 之间")
	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}
	
	@Length(min=0, max=18, message="联系方式长度必须介于 0 和 18 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}