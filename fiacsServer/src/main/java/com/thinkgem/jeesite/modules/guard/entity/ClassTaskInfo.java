/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 班组信息Entity
 * 
 * @author Jumbo
 * @version 2017-07-04
 */
public class ClassTaskInfo extends DataEntity<ClassTaskInfo> {

	private static final long serialVersionUID = 1L;
	private String name; // 班组名称
	private Area area; // 区域
	private Staff staffOne; // 人员1
	private Staff staffTwo; // 人员2
	private Car car; // 车辆信息
	private Line line; // 线路信息
	private String verifyCar="1"; // 验证车辆
	private String verifyInterMan="1"; // 专员确认
	private String verifyLocker="1"; // 钱箱确认
	private String patrolManNum; // 押款员数量
	private String interManNum; // 交接专员数量
	private String taskTimeout; // 超时时间
	private List<ClassCarInfo> classCarInfoList = Lists.newArrayList();		// 子表列表
	private List<ClassPersonInfo> classPersonInfoList = Lists.newArrayList();		// 押款员子表列表
	private List<LineNodes> linNodesList = Lists.newArrayList();		// 子表列表
	
	private String tasktype;
	
	public ClassTaskInfo() {
		super();
	}

	public ClassTaskInfo(String id) {
		super(id);
	}
	
	
	@ExcelField(title = "线路名称", align = 2, sort = 3)
	private String lineName(){
		return line.getLineName();
	}
	
	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

	public List<LineNodes> getLinNodesList() {
		return linNodesList;
	}

	public void setLinNodesList(List<LineNodes> linNodesList) {
		this.linNodesList = linNodesList;
	}

	public List<ClassCarInfo> getClassCarInfoList() {
		return classCarInfoList;
	}

	public void setClassCarInfoList(List<ClassCarInfo> classCarInfoList) {
		this.classCarInfoList = classCarInfoList;
	}

	public List<ClassPersonInfo> getClassPersonInfoList() {
		return classPersonInfoList;
	}

	public void setClassPersonInfoList(List<ClassPersonInfo> classPersonInfoList) {
		this.classPersonInfoList = classPersonInfoList;
	}

	@Length(min = 1, max = 32, message = "班组名称长度必须介于 1 和 32 之间")
	@ExcelField(title = "班组名称", align = 2, sort = 1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
	
	@ExcelField(title = "所属区域", align = 2, sort = 2)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min = 1, max = 1, message = "验证车辆长度必须介于 1 和 1 之间")
	@ExcelField(title = "车辆确认", align = 2, sort = 5 ,dictType = "inter_man")
	public String getVerifyCar() {
		return verifyCar;
	}

	public void setVerifyCar(String verifyCar) {
		this.verifyCar = verifyCar;
	}

	@Length(min = 1, max = 1, message = "专员确认长度必须介于 1 和 1 之间")
	@ExcelField(title = "专员确认", align = 2, sort = 6 ,dictType = "inter_man")
	public String getVerifyInterMan() {
		return verifyInterMan;
	}

	public void setVerifyInterMan(String verifyInterMan) {
		this.verifyInterMan = verifyInterMan;
	}

	@Length(min = 1, max = 1, message = "款箱确认长度必须介于 1 和 1 之间")
	@ExcelField(title = "款箱确认", align = 2, sort = 7 ,dictType = "inter_man")
	public String getVerifyLocker() {
		return verifyLocker;
	}

	public void setVerifyLocker(String verifyLocker) {
		this.verifyLocker = verifyLocker;
	}

	public String getPatrolManNum() {
		return patrolManNum;
	}

	public void setPatrolManNum(String patrolManNum) {
		this.patrolManNum = patrolManNum;
	}

	public String getInterManNum() {
		return interManNum;
	}

	public void setInterManNum(String interManNum) {
		this.interManNum = interManNum;
	}
	@ExcelField(title = "超时时间", align = 2, sort = 10)
	public String getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(String taskTimeout) {
		this.taskTimeout = taskTimeout;
	}

	public Staff getStaffOne() {
		return staffOne;
	}

	public void setStaffOne(Staff staffOne) {
		this.staffOne = staffOne;
	}

	public Staff getStaffTwo() {
		return staffTwo;
	}

	public void setStaffTwo(Staff staffTwo) {
		this.staffTwo = staffTwo;
	}
}