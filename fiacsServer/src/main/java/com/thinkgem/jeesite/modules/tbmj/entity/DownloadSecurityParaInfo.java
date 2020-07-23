/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.List;

/**
 * 防盗参数同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadSecurityParaInfo extends DownloadEntity<DownloadSecurityParaInfo> {

	private static final long serialVersionUID = 1L;
	private String securityParaInfoId;		// 防盗参数ID
	private String equipmentId;		// 设备ID

	private SecurityParaInfo securityParaInfo;
	private Equipment equipment;
	private Office office;

	private String downloadTimeTwo;
	private List<String> officeIds;

	public String getSecurityParaInfoId() {
		return securityParaInfoId;
	}

	public void setSecurityParaInfoId(String securityParaInfoId) {
		this.securityParaInfoId = securityParaInfoId;
	}

	public SecurityParaInfo getSecurityParaInfo() {
		return securityParaInfo;
	}

	public void setSecurityParaInfo(SecurityParaInfo securityParaInfo) {
		this.securityParaInfo = securityParaInfo;
	}

	public DownloadSecurityParaInfo() {
		super();
	}

	public DownloadSecurityParaInfo(String id){
		super(id);
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getDownloadTimeTwo() {
		return downloadTimeTwo;
	}

	public void setDownloadTimeTwo(String downloadTimeTwo) {
		this.downloadTimeTwo = downloadTimeTwo;
	}

	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
	}
}