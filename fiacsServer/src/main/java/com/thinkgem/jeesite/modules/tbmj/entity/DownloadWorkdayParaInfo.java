/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.List;

/**
 * 门禁同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadWorkdayParaInfo extends DownloadEntity<DownloadWorkdayParaInfo> {

	private static final long serialVersionUID = 1L;
	private String workdayParaInfoId;		// 工作日ID
	private String equipmentId;		// 设备ID

	private WorkdayParaInfo workdayParaInfo;
	private Equipment equipment;
	private Office office;

	private String downloadTimeTwo;
	private List<String> officeIds;

	public DownloadWorkdayParaInfo() {
		super();
	}

	public DownloadWorkdayParaInfo(String id){
		super(id);
	}

	public String getWorkdayParaInfoId() {
		return workdayParaInfoId;
	}

	public void setWorkdayParaInfoId(String workdayParaInfoId) {
		this.workdayParaInfoId = workdayParaInfoId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public WorkdayParaInfo getWorkdayParaInfo() {
		return workdayParaInfo;
	}

	public void setWorkdayParaInfo(WorkdayParaInfo workdayParaInfo) {
		this.workdayParaInfo = workdayParaInfo;
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