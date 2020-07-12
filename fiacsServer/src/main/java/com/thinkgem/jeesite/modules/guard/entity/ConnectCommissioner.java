/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 交接事件Entity
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
public class ConnectCommissioner extends DataEntity<ConnectCommissioner> {

	private static final long serialVersionUID = 1L;
	private String recordId; // record_id 父类
	private String fingerId; // finger_id
	private String time; // time
	private String taskId; // task_id

	public ConnectCommissioner() {
		super();
	}

	public ConnectCommissioner(String id) {
		super(id);
	}

	public ConnectCommissioner(ConnectEvent recordId) {
		this.recordId = recordId.getId();
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getFingerId() {
		return fingerId;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}

	@Length(min = 1, max = 20, message = "time长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}