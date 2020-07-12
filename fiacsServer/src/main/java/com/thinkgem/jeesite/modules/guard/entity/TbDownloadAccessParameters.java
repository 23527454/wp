package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import com.thinkgem.jeesite.modules.sys.entity.Office;

public class TbDownloadAccessParameters extends DownloadEntity<TbDownloadAccessParameters>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String parametersId;
	
	private String equipmentId;
	
	private Office office;
	
	private String downloadTimeTwo;
	
	private List<String> officeIds;
	
	private String doorPos;
	
	public String getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(String doorPos) {
		this.doorPos = doorPos;
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

	
	public String getParametersId() {
		return parametersId;
	}

	public void setParametersId(String parametersId) {
		this.parametersId = parametersId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	
}
