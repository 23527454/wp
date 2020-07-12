/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 款箱同步信息Entity
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadMoneyBox extends DownloadEntity<DownloadMoneyBox> {

	private static final long serialVersionUID = 1L;
	private String moneyBoxId; // 款箱ID
	private String equipmentId; // 设备ID
	private String downloadTimeTwo;
	private Office office;
	private MoneyBox moneyBox;
	private List<String> officeIds;
	
	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
	}

	public DownloadMoneyBox() {
		super();
	}

	public DownloadMoneyBox(String id) {
		super(id);
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


	public String getDownloadTimeTwo() {
		return downloadTimeTwo;
	}

	public void setDownloadTimeTwo(String downloadTimeTwo) {
		this.downloadTimeTwo = downloadTimeTwo;
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
}