/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 人员同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadPerson extends DownloadEntity<DownloadPerson> {
	
	private static final long serialVersionUID = 1L;
	private String personId;		// 人员ID
	private String equipId;		// 设备ID
	private String downloadTimeTwo;
	
	private Staff staff;
	private Office office;
	private List<String> officeIds;
	
	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
	}

	public DownloadPerson() {
		super();
	}

	public DownloadPerson(String id){
		super(id);
	}
	
	public Staff getStaff() {
		return staff;
	}
	

	public String getDownloadTimeTwo() {
		return downloadTimeTwo;
	}

	public void setDownloadTimeTwo(String downloadTimeTwo) {
		this.downloadTimeTwo = downloadTimeTwo;
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	
	
}