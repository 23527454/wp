/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 人员信息管理Entity
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
public class Staff extends DataEntity<Staff> {

	private static final long serialVersionUID = 1L;
	private String name; // 姓名
	private Date birthday; // 出生日期
	private Office office;   // 交接员对应网点ID(设备id), 网点ID
	private Company company; // 押款员/维保员对应公司ID, 所属机构ID
	private Area area; // 区域ID
	private String sex="0"; // 性别
	private String workNum; // 工号
	private String identifyType; // 证件类型
	private String identifyNumber; // 证件号码
	private String dept; // 部门
	private String dupt; // 职务
	private String work; // 岗位
	private String phone; // 联系方式
	private String nation; // 民族
	private String education; // 学历
	private String email; // 电子邮箱
	private String address; // 家庭住址
	private String staffType; // 人员类型
	private String workStatus = "1"; // 人员工作状态
	private String status = "0"; // 人员状态()
	private List<FingerInfo> fingerInfoList = Lists.newArrayList(); // 子表列表
	private List<StaffExFamily> staffExFamilyList = Lists.newArrayList(); // 子表列表
	private List<StaffImage> staffImageList = Lists.newArrayList(); // 子表列表
	private List<StaffExWork> staffExWorkList = Lists.newArrayList(); // 子表列表
	private String classTaskId;
	private String imagePath;
	private List<Company> companyIds;
 
	private List<String> compIds;

	public List<String> getCompIds() {
		return compIds;
	}

	public void setCompIds(List<String> compIds) {
		this.compIds = compIds;
	}

	public static final String STAFF_TYPE_SUPERCARGO = "0";//押款员 公司
	public static final String STAFF_TYPE_HANDOVER_CLERK = "1"; //交接员 网点
	public static final String STAFF_TYPE_MAINTENANCE_CLERK = "2";//维保员 公司

	
	
	public static final String STATUS_CREATED = "0";//新建
	public static final String STATUS_AUDITED = "1"; //审核通过
	public static final String STATUS_APPROVAL = "2"; //审批通过
	public static final String STATUS_NO_APPROVAL_REQUIRED = "3"; //不需要审批
	
	private Integer queryType;//查询类型
	
	
	
	public List<Company> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<Company> companyIds) {
		this.companyIds = companyIds;
	}

	public static final int queryTypeSuperGO= 1; //班组配置-押款员列表

	public Staff() {
		super();
	}

	public Staff(String id) {
		super(id);
	}
	
	

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Length(min = 1, max = 32, message = "姓名长度必须介于 1 和 32 之间")
	@ExcelField(title = "姓名", align = 2, sort = 1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@ExcelField(title = "所属区域", align = 2, sort = 30)
	private String getAreaName(){
		return area.getName();
	}
	
	@ExcelField(title = "所属网点", align = 2, sort = 40)
	private String getOfficeName(){
		return office.getName();
	}
	
	@ExcelField(title = "所属公司", align = 2, sort = 50)
	private String getCompanyName(){
		return company.getShortName();
	}
	
	@ExcelField(title = "指纹号", align = 2, sort = 60)
	private String getFingNum(){
		return fingerInfoList.get(0).getFingerNum();
	}
	
	@ExcelField(title = "起始时间", align = 2, sort = 120)
	private Date getStaDate(){
		return fingerInfoList.get(0).getStartDate();
	}
	
	@ExcelField(title = "截止时间", align = 2, sort = 130)
	public Date getEndDate(){
		return fingerInfoList.get(0).getEndDate();
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Length(min = 1, max = 1, message = "性别长度必须介于 1 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@NotNull(message = "网点ID不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@NotNull(message = "区域不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}

	@Length(min = 1, max = 10, message = "工号长度必须介于 1 和 10 之间")
	@ExcelField(title = "人员工号", align = 2, sort = 8)
	public String getWorkNum() {
		return workNum;
	}

	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}

	/*public String getZhiWen() {
		return fingerInfoList.get(0).getFingerNum();
	}*/

	@Length(min = 1, max = 1, message = "证件类型长度必须介于 1 和 1 之间")
	@ExcelField(title = "证件类型", align = 2, sort = 4, dictType = "identify_ype")
	public String getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	@Length(min = 0, max = 20, message = "证件号码长度必须介于 0 和 20 之间")
	@ExcelField(title = "证件号码", align = 2, sort = 6)
	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	@Length(min = 0, max = 10, message = "部门长度必须介于 0 和 10 之间")
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	@Length(min = 0, max = 64, message = "职务长度必须介于 0 和 64 之间")
	public String getDupt() {
		return dupt;
	}

	public void setDupt(String dupt) {
		this.dupt = dupt;
	}

	@Length(min = 0, max = 10, message = "岗位长度必须介于 0 和 10 之间")
	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	@Length(min = 0, max = 18, message = "联系方式长度必须介于 0 和 18 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min = 0, max = 12, message = "民族长度必须介于 0 和 12 之间")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	@Length(min = 0, max = 10, message = "学历长度必须介于 0 和 10 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Length(min = 0, max = 32, message = "电子邮箱长度必须介于 0 和 32 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min = 0, max = 64, message = "家庭住址长度必须介于 0 和 64 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min = 1, max = 1, message = "人员类型长度必须介于 1 和 1 之间")
	@ExcelField(title = "人员类型", align = 2, sort = 20,dictType = "staff_type")
	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	@Length(min = 1, max = 1, message = "人员工作状态长度必须介于 1 和 1 之间")
	@ExcelField(title = "工作状态", align = 2, sort = 110, dictType = "nWork_tatus")
	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	@Length(min = 1, max = 1, message = "人员状态长度必须介于 1 和 1 之间")
	@ExcelField(title = "人员状态", align = 2, sort = 100, dictType = "person_status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<FingerInfo> getFingerInfoList() {
		return fingerInfoList;
	}

	public void setFingerInfoList(List<FingerInfo> fingerInfoList) {
		this.fingerInfoList = fingerInfoList;
	}

	public List<StaffExFamily> getStaffExFamilyList() {
		return staffExFamilyList;
	}

	public void setStaffExFamilyList(List<StaffExFamily> staffExFamilyList) {
		this.staffExFamilyList = staffExFamilyList;
	}

	public List<StaffImage> getStaffImageList() {
		return staffImageList;
	}

	public void setStaffImageList(List<StaffImage> staffImageList) {
		this.staffImageList = staffImageList;
	}

	public List<StaffExWork> getStaffExWorkList() {
		return staffExWorkList;
	}

	public void setStaffExWorkList(List<StaffExWork> staffExWorkList) {
		this.staffExWorkList = staffExWorkList;
	}
	
	public boolean isApproved(){
		return STATUS_APPROVAL.equals(status) || STATUS_NO_APPROVAL_REQUIRED.equals(status);
	}
}