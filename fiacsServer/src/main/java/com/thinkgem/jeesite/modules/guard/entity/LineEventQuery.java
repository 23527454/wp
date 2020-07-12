/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 排班明细Entity
 * @author Jumbo
 * @version 2017-07-20
 */
public class LineEventQuery extends DataEntity<LineEventQuery> {
	
	private static final long serialVersionUID = 1L;
	private Line line;		// 线路ID
	private String nodesId;		// 节点ID	
	private Office office;		// 设备ID
	private String eventId;		// 事件序号
	private String equipSn;		// 设备序号
	private String time;		// 时间
	private TaskScheduleInfo taskScheduleInfo; // 任务ID
	private ClassTaskInfo classTaskfo; // 班组ID
	private Area area; // 区域
	private Car car;
	
	public LineEventQuery() {
		super();
	}

	public LineEventQuery(String id){
		super(id);
	}
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public ClassTaskInfo getClassTaskfo() {
		return classTaskfo;
	}

	public void setClassTaskfo(ClassTaskInfo classTaskfo) {
		this.classTaskfo = classTaskfo;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public TaskScheduleInfo getTaskScheduleInfo() {
		return taskScheduleInfo;
	}

	public void setTaskScheduleInfo(TaskScheduleInfo taskScheduleInfo) {
		this.taskScheduleInfo = taskScheduleInfo;
	}

	public String getNodesId() {
		return nodesId;
	}

	public void setNodesId(String nodesId) {
		this.nodesId = nodesId;
	}
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	@Length(min=1, max=32, message="设备序号长度必须介于 1 和 32 之间")
	public String getEquipSn() {
		return equipSn;
	}

	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}
	
	@Length(min=1, max=20, message="时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}