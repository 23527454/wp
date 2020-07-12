package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import com.thinkgem.jeesite.modules.sys.entity.Office;

public class TbDownloadDoorTimeZone extends DownloadEntity<TbDownloadDoorTimeZone>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String timeZoneId;
	
	private String equipmentId;
	
	private Office office;
	
	private String downloadTimeTwo;
	
	private List<String> officeIds;
	
	private Integer doorPos;
	
	private Integer weekNumber;
	
	public Integer getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(Integer doorPos) {
		this.doorPos = doorPos;
	}

	public Integer getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}

	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
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

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

}
