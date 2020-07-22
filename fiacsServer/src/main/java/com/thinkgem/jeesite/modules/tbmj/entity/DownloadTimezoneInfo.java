/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.List;

/**
 * 时区同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadTimezoneInfo extends DownloadEntity<DownloadTimezoneInfo> {

	private static final long serialVersionUID = 1L;
	private String timezoneInfoId;		// 时区ID
	private String accessParaInfoId;		// 门禁ID

	private TimezoneInfo timezoneInfo;
	private AccessParaInfo accessParaInfo;
	private Equipment equipment;
	private Office office;

	private String downloadTimeTwo;
	private List<String> officeIds;

	public DownloadTimezoneInfo() {
		super();
	}

	public DownloadTimezoneInfo(String id) {
		super(id);
	}

	public String getTimezoneInfoId() {
		return timezoneInfoId;
	}

	public void setTimezoneInfoId(String timezoneInfoId) {
		this.timezoneInfoId = timezoneInfoId;
	}

	public String getAccessParaInfoId() {
		return accessParaInfoId;
	}

	public void setAccessParaInfoId(String accessParaInfoId) {
		this.accessParaInfoId = accessParaInfoId;
	}

	public TimezoneInfo getTimezoneInfo() {
		return timezoneInfo;
	}

	public void setTimezoneInfo(TimezoneInfo timezoneInfo) {
		this.timezoneInfo = timezoneInfo;
	}

	public AccessParaInfo getAccessParaInfo() {
		return accessParaInfo;
	}

	public void setAccessParaInfo(AccessParaInfo accessParaInfo) {
		this.accessParaInfo = accessParaInfo;
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