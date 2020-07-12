/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.persistence.TreeEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 第三方公司Entity
 * @author Jumbo
 * @version 2017-07-19
 */
public class Company extends TreeEntity<Company> {
	
	private static final long serialVersionUID = 1L;
	private String companyCode;		// 公司编码
	private String companyType;		// 公司类型
	private String shortName;		// 简称
	private String fullName;		// 全称
	private String address;		// 公司地址
	private String contact;		// 联系人
	private String phone;		// 联系方式
	private String fax;		// 传真
	private String email;		// 电子邮箱
	private Area area;
	private List<CompanyEx> companyExList = Lists.newArrayList();		// 子表列表
	
	public Company() {
		super();
	}

	public Company(String id){
		super(id);
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min=1, max=1, message="公司类型长度必须介于 1 和 1 之间")
	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	
	@Length(min=1, max=32, message="简称长度必须介于 1 和 32 之间")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	@Length(min=0, max=128, message="全称长度必须介于 0 和 128 之间")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Length(min=0, max=128, message="公司地址长度必须介于0 和 128 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=32, message="联系人长度必须介于 0 和 32 之间")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	@Length(min=0, max=18, message="联系方式长度必须介于 0 和 18 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=32, message="传真长度必须介于 0 和 32 之间")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Length(min=0, max=32, message="电子邮箱长度必须介于 0 和 32 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<CompanyEx> getCompanyExList() {
		return companyExList;
	}

	public void setCompanyExList(List<CompanyEx> companyExList) {
		this.companyExList = companyExList;
	}

	@Override
	public Company getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public void setParent(Company parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
	}
}