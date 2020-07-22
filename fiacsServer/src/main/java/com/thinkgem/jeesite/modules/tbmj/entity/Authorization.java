/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.Date;
import java.util.List;

/**
 * power_authorizationEntity
 * @author demo
 * @version 2020-07-04
 */

public class Authorization extends DataEntity<Authorization> {
	
	private static final long serialVersionUID = 1L;
	private Staff staff;		// 用户ID
	private Equipment equipment;		//设备ID
	private String cardNum;		// 卡号
	private String timezoneInfoNum;		// 通行时区号
	private String workdayNum;			//工作日号
	private String permissionGroup;		//人员权限分组
	private String checkPom;			//是否允许入库
	private String doorPos;

	private String staffId;
	private String officeId;
	private String staffName;
	private String workNum;
	private String equipmentId;
	private String accessParaInfoId;
	private Office office;		// 机构ID
	private AccessParaInfo accessParaInfo;		// 门禁ID
	private Date validityDate;		// 门禁有效期
	private List<TimezoneInfo> timezoneInfos;


	public List<TimezoneInfo> getTimezoneInfos() {
		return timezoneInfos;
	}

	public void setTimezoneInfos(List<TimezoneInfo> timezoneInfos) {
		this.timezoneInfos = timezoneInfos;
	}

	public Authorization(String timezoneInfoNum, String staffId, String accessParaInfoId, String permissionGroup, String checkPom, String workdayNum) {
		this.timezoneInfoNum = timezoneInfoNum;
		this.staffId = staffId;
		this.accessParaInfoId = accessParaInfoId;
		this.permissionGroup = permissionGroup;
		this.checkPom = checkPom;
		this.workdayNum = workdayNum;
	}

	public Authorization() {
		super();
	}

	public Authorization(String id){
		super(id);
	}

	public Authorization(Office office) {
		this.office = office;
	}

	public AccessParaInfo getAccessParaInfo() {
		return accessParaInfo;
	}

	public void setAccessParaInfo(AccessParaInfo accessParaInfo) {
		this.accessParaInfo = accessParaInfo;
	}

	@ExcelField(title = "网点名称", align = 2, sort = 1)
	public String getOfficeName(){
		return office.getName();
	}

	@ExcelField(title = "设备名称", align = 2, sort = 2,dictType = "site_type")
	public String getEquipmentName(){
		return accessParaInfo.getEquipment().getSiteType();
	}

	@ExcelField(title = "门号", align = 2, sort = 3,dictType = "door_pos")
	public String getDoorPos(){
		return  doorPos;
	}
	public void setDoorPos(String doorPos) {
		this.doorPos = doorPos;
	}

	@ExcelField(title = "员工工号", align = 2, sort = 4)
	public String getStaffWorkNum(){
		return staff.getWorkNum();
	}

	@ExcelField(title = "员工姓名", align = 2, sort = 5)
	public String getStaffName2(){
		return staff.getName();
	}

	@ExcelField(title = "是否允许查库",align = 2,sort = 13,dictType = "yes_no")
	public String getCheckPom() {
		return checkPom;
	}

	public void setCheckPom(String checkPom) {
		this.checkPom = checkPom;
	}

	public Staff getStaff() {
		return staff;
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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	@ExcelField(title = "通信时区",align = 2,sort = 15,dictType = "time_zone_num")
	public String getTimezoneInfoNum() {
		return timezoneInfoNum;
	}

	public void setTimezoneInfoNum(String timezoneInfoNum) {
		this.timezoneInfoNum = timezoneInfoNum;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getWorkNum() {
		return workNum;
	}

	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}

	public String getAccessParaInfoId() {
		return accessParaInfoId;
	}

	public void setAccessParaInfoId(String accessParaInfoId) {
		this.accessParaInfoId = accessParaInfoId;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public String getWorkdayNum() {
		return workdayNum;
	}

	public void setWorkdayNum(String workdayNum) {
		this.workdayNum = workdayNum;
	}

	public String getPermissionGroup() {
		return permissionGroup;
	}

	public void setPermissionGroup(String permissionGroup) {
		this.permissionGroup = permissionGroup;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
}