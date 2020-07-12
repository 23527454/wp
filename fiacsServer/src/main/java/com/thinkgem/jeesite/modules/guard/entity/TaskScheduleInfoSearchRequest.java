/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 排班信息集合查询
 * @author Jim
 * @version 2017-09-24
 */
public class TaskScheduleInfoSearchRequest extends TaskScheduleInfo {
	private static final long serialVersionUID = 1287660533462619215L;
	
	private boolean notInConnectEvent;

	public boolean isNotInConnectEvent() {
		return notInConnectEvent;
	}

	public void setNotInConnectEvent(boolean notInConnectEvent) {
		this.notInConnectEvent = notInConnectEvent;
	}
	
}