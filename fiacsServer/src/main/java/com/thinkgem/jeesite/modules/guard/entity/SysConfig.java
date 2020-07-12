/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 系统配置Entity
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
public class SysConfig extends DataEntity<SysConfig> {

	private static final long serialVersionUID = 1L;
	private String attribute; // 设备ID
	private String value; // 事件序号

	private String corporationName;  // 公司名称
	private String systemName;  // 系统名称
	private String resetUploadConnectItems;  // 交接事件再次上传的条数
	private String resetUploadAlarmItems;  // 异常报警再次上传的条数
	
	private String syncTime1;  // 同步时钟，时间点1
	private String syncTime2;  // 同步时钟，时间点2
	private String syncTime3;  // 同步时钟，时间点3
	private String isDogRun;  // 是否开启看门狗运行
	private String isLockProtect;  // 是否加锁保护
	private String isAutoLogin;  // 是否自动登录
	private String autoOpenWhenStartComputer;  // 开机启动
	private String versionNumber;  // 开机启动
	
	private String superGoNum;
	private String interNum;
	
	private String dtTaskCut; //任务切割时间 
	
	private String staffValidity; //任务切割时间 
	
	public String getStaffValidity() {
		return staffValidity;
	}

	public void setStaffValidity(String staffValidity) {
		this.staffValidity = staffValidity;
	}

	public SysConfig() {
		super();
	}

	public SysConfig(String id) {
		super(id);
	}

	
	public String getDtTaskCut() {
		return dtTaskCut;
	}

	public void setDtTaskCut(String dtTaskCut) {
		this.dtTaskCut = dtTaskCut;
	}

	public String getSuperGoNum() {
		return superGoNum;
	}

	public void setSuperGoNum(String superGoNum) {
		this.superGoNum = superGoNum;
	}

	public String getInterNum() {
		return interNum;
	}

	public void setInterNum(String interNum) {
		this.interNum = interNum;
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getResetUploadConnectItems() {
		return resetUploadConnectItems;
	}

	public void setResetUploadConnectItems(String resetUploadConnectItems) {
		this.resetUploadConnectItems = resetUploadConnectItems;
	}

	public String getResetUploadAlarmItems() {
		return resetUploadAlarmItems;
	}

	public void setResetUploadAlarmItems(String resetUploadAlarmItems) {
		this.resetUploadAlarmItems = resetUploadAlarmItems;
	}

	public String getSyncTime1() {
		return syncTime1;
	}

	public void setSyncTime1(String syncTime1) {
		this.syncTime1 = syncTime1;
	}

	public String getSyncTime2() {
		return syncTime2;
	}

	public void setSyncTime2(String syncTime2) {
		this.syncTime2 = syncTime2;
	}

	public String getSyncTime3() {
		return syncTime3;
	}

	public void setSyncTime3(String syncTime3) {
		this.syncTime3 = syncTime3;
	}

	public String getIsDogRun() {
		return isDogRun;
	}

	public void setIsDogRun(String isDogRun) {
		this.isDogRun = isDogRun;
	}

	public String getIsLockProtect() {
		return isLockProtect;
	}

	public void setIsLockProtect(String isLockProtect) {
		this.isLockProtect = isLockProtect;
	}

	public String getIsAutoLogin() {
		return isAutoLogin;
	}

	public void setIsAutoLogin(String isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}

	public String getAutoOpenWhenStartComputer() {
		return autoOpenWhenStartComputer;
	}

	public void setAutoOpenWhenStartComputer(String autoOpenWhenStartComputer) {
		this.autoOpenWhenStartComputer = autoOpenWhenStartComputer;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}