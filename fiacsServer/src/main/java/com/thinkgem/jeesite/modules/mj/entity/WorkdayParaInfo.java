/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * access_workdayEntity
 * @author demo
 * @version 2020-07-02
 */

public class WorkdayParaInfo extends DataEntity<WorkdayParaInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;	//设备
	private String year;		// 年
	private String month;		// 月
	private String day;		// 日

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public WorkdayParaInfo() {
		this(null);
	}

	public WorkdayParaInfo(String id){
		super(id);
	}
	
	@NotBlank(message="年不能为空")
	@Length(min=0, max=4, message="年长度不能超过 4 个字符")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@NotBlank(message="月不能为空")
	@Length(min=0, max=2, message="月长度不能超过 2 个字符")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	@NotBlank(message="日不能为空")
	@Length(min=0, max=2, message="日长度不能超过 2 个字符")
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}