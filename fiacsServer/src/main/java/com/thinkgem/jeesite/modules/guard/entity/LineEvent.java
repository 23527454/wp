/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 线路监控Entity
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
public class LineEvent extends DataEntity<LineEvent> {

	private static final long serialVersionUID = 1L;
	private String classTaskId; // 班组ID
	private String verifyCar ; // 车辆验证
	private String allotDate; // 排班日期
	private String allotTime; // 排班时间
	private String verifyInterMan; // 专员验证
	private String verifyMoneyBox; // 款箱验证
	private String taskDate; // 任务执行日期
	private String patrolManNum ; // 押款员数量
	private String taskTime; // 任务执行时间
	private String interManNum ; // 专员数量
	private String taskTimeout ; // 任务超时
	private String taskType; // 任务执行类型
	private String taskTimeClass; // 任务时间
	private List<TaskCarInfo> taskCarInfoList = Lists.newArrayList(); // 子表列表
	private List<TaskLineInfo> taskLineInfoList = Lists.newArrayList(); // 子表列表
	private List<TaskPersonInfo> taskPersonInfoList = Lists.newArrayList(); // 子表列表
	private List<ConnectEvent> connectEventList = Lists.newArrayList(); // 子表列表
	private ClassTaskInfo classTaskInfo;
	private String lineId;
	private String lineName;
	private String nodes;
	private String type;

	public LineEvent() {
		super();
	}

	public LineEvent(String id) {
		super(id);
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getClassTaskId() {
		return classTaskId;
	}

	public List<ConnectEvent> getConnectEventList() {
		return connectEventList;
	}

	public void setConnectEventList(List<ConnectEvent> connectEventList) {
		this.connectEventList = connectEventList;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}

	public String getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(String allotDate) {
		this.allotDate = allotDate;
	}

	public String getAllotTime() {
		return allotTime;
	}

	public void setAllotTime(String allotTime) {
		this.allotTime = allotTime;
	}

	public String getVerifyMoneyBox() {
		return verifyMoneyBox;
	}

	public void setVerifyMoneyBox(String verifyMoneyBox) {
		this.verifyMoneyBox = verifyMoneyBox;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getPatrolManNum() {
		return patrolManNum;
	}

	public void setPatrolManNum(String patrolManNum) {
		this.patrolManNum = patrolManNum;
	}

	public String getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	public String getInterManNum() {
		return interManNum;
	}

	public void setInterManNum(String interManNum) {
		this.interManNum = interManNum;
	}

	public String getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(String taskTimeout) {
		this.taskTimeout = taskTimeout;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskTimeClass() {
		return taskTimeClass;
	}

	public void setTaskTimeClass(String taskTimeClass) {
		this.taskTimeClass = taskTimeClass;
	}

	public List<TaskCarInfo> getTaskCarInfoList() {
		return taskCarInfoList;
	}

	public void setTaskCarInfoList(List<TaskCarInfo> taskCarInfoList) {
		this.taskCarInfoList = taskCarInfoList;
	}

	public List<TaskLineInfo> getTaskLineInfoList() {
		return taskLineInfoList;
	}

	public void setTaskLineInfoList(List<TaskLineInfo> taskLineInfoList) {
		this.taskLineInfoList = taskLineInfoList;
	}

	public List<TaskPersonInfo> getTaskPersonInfoList() {
		return taskPersonInfoList;
	}

	public void setTaskPersonInfoList(List<TaskPersonInfo> taskPersonInfoList) {
		this.taskPersonInfoList = taskPersonInfoList;
	}

	public ClassTaskInfo getClassTaskInfo() {
		return classTaskInfo;
	}

	public void setClassTaskInfo(ClassTaskInfo classTaskInfo) {
		this.classTaskInfo = classTaskInfo;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getVerifyCar() {
		return verifyCar;
	}

	public void setVerifyCar(String verifyCar) {
		this.verifyCar = verifyCar;
	}

	public String getVerifyInterMan() {
		return verifyInterMan;
	}

	public void setVerifyInterMan(String verifyInterMan) {
		this.verifyInterMan = verifyInterMan;
	}

}