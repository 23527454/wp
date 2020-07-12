/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import java.util.List;
import com.google.common.collect.Lists;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 排班信息Entity
 * @author Junbo
 * @version 2017-08-01
 */
public class TaskScheduleInfo extends DataEntity<TaskScheduleInfo> {
	
	private static final long serialVersionUID = 1L;
	private String classTaskId;		// 班组ID
	private String verifyCar="1";		// 车辆验证
	private String allotDate;		// 排班日期
	private String allotTime;		// 排班时间
	private String verifyInterMan="1";		// 专员验证
	private String verifyMoneyBox="1";		// 款箱验证
	private String taskDate;		// 任务执行日期
	private String patrolManNum="2";		// 押款员数量
	private String taskTime;		// 任务执行时间
	private String interManNum="1";		// 专员数量
	private String taskTimeout="60";		// 任务超时
	private String taskType;		// 任务执行类型
	private String taskTimeClass;		// 任务时间
	private List<TaskCarInfo> taskCarInfoList = Lists.newArrayList();		// 子表列表
	private List<TaskLineInfo> taskLineInfoList = Lists.newArrayList();		// 子表列表
	private List<TaskPersonInfo> taskPersonInfoList = Lists.newArrayList();		// 押款员子表列表
	private ClassTaskInfo classTaskInfo;
	private String lineId;
	private String lineName;
	
	public static final String TASK_TYPE_PAISONG = "0";//派送
	public static final String TASK_TYPE_HUISHOU = "1";//回收
	public static final String TASK_TYPE_LINSHIPAISONG = "2";//临时派送
	public static final String TASK_TYPE_LINSHIHUISHOU = "3";//临时回收
	public static final String TASK_TYPE_GUIJINSHUPAISONG = "4"; //贵金属派送
	
	public TaskScheduleInfo() {
		super();
	}

	public TaskScheduleInfo(String id){
		super(id);
	}

	@ExcelField(title = "班次", type = 1, align = 2, sort = 1)
	public String getId() {
		return id;
	}
	
	@ExcelField(title = "班组名称", align = 2, sort = 7)
	public String getClassTaskfoName() {
		return classTaskInfo.getName();
	}
	
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getClassTaskId() {
		return classTaskId;
	}

	public ClassTaskInfo getClassTaskInfo() {
		return classTaskInfo;
	}

	public void setClassTaskInfo(ClassTaskInfo classTaskInfo) {
		this.classTaskInfo = classTaskInfo;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}
	
	@ExcelField(title = "车辆验证", align = 2, sort = 19,dictType = "inter_man")
	@Length(min=1, max=1, message="车辆验证长度必须介于 1 和 1 之间")
	public String getVerifyCar() {
		return verifyCar;
	}

	public void setVerifyCar(String verifyCar) {
		this.verifyCar = verifyCar;
	}
	
	@ExcelField(title = "排班日期", align = 2, sort = 3)
	@Length(min=1, max=12, message="排班日期长度必须介于 1 和 12 之间")
	public String getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(String allotDate) {
		this.allotDate = allotDate;
	}
	
	@ExcelField(title = "排班时间", align = 2, sort = 5)
	@Length(min=1, max=8, message="排班时间长度必须介于 1 和 8 之间")
	public String getAllotTime() {
		return allotTime;
	}

	public void setAllotTime(String allotTime) {
		this.allotTime = allotTime;
	}
	
	@ExcelField(title = "专员验证", align = 2, sort = 21,dictType = "inter_man")
	@Length(min=1, max=1, message="专员验证长度必须介于 1 和 1 之间")
	public String getVerifyInterMan() {
		return verifyInterMan;
	}

	public void setVerifyInterMan(String verifyInterMan) {
		this.verifyInterMan = verifyInterMan;
	}
	
	@ExcelField(title = "款箱验证", align = 2, sort = 23,dictType = "inter_man")
	@Length(min=1, max=1, message="款箱验证长度必须介于 1 和 1 之间")
	public String getVerifyMoneyBox() {
		return verifyMoneyBox;
	}

	public void setVerifyMoneyBox(String verifyMoneyBox) {
		this.verifyMoneyBox = verifyMoneyBox;
	}
	@ExcelField(title = "任务执行日期", align = 2, sort = 9)
	@Length(min=1, max=12, message="任务执行日期长度必须介于 1 和 12 之间")
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
	
	@ExcelField(title = "任务执行时间", align = 2, sort = 11)
	@Length(min=1, max=8, message="任务执行时间长度必须介于 1 和 8 之间")
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
	
	@ExcelField(title = "班组任务超时", align = 2, sort = 17)
	public String getTaskTimeoutFZ() {
		return taskTimeout+"分钟";
	}
	
	public String getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(String taskTimeout) {
		this.taskTimeout = taskTimeout;
	}
	
	@ExcelField(title = "任务类型", align = 2, sort = 13 , dictType = "task_type")
	@Length(min=1, max=1, message="任务执行类型长度必须介于 1 和 1 之间")
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	@ExcelField(title = "任务时间", align = 2, sort = 15 , dictType = "task_time_class")
	@Length(min=1, max=1, message="任务时间长度必须介于 1 和 1 之间")
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

}