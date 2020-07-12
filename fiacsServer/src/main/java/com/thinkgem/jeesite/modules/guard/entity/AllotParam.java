/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 车辆管理生成Entity
 * @author Jumbo
 * @version 2017-06-26
 */
public class AllotParam extends DataEntity<AllotParam> {
	
	private static final long serialVersionUID = 1L;
	private String tatal;		// 记录条数
	private List<MoneyBox> rows;		// 款箱
	private String equipmentId;		// 车牌号

	private ClassTaskInfo classTaskInfo;
	private String alloType;//调拨类型
	private String scheduleDate;
	 
	public AllotParam() {
		super();
	}

	public AllotParam(String id){
		super(id);
	}

	public String getTotal() {
		return tatal;
	}

	public void setTotal(String tatal) {
		this.tatal = tatal;
	}
	
	public ClassTaskInfo getClassTaskInfo() {
		return classTaskInfo;
	}

	public void setClassTaskInfo(ClassTaskInfo classTaskInfo) {
		this.classTaskInfo = classTaskInfo;
	}
	
	


	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getTatal() {
		return tatal;
	}

	public void setTatal(String tatal) {
		this.tatal = tatal;
	}

	public String getAlloType() {
		return alloType;
	}

	public void setAlloType(String alloType) {
		this.alloType = alloType;
	}

	public List<MoneyBox> getRows() {
		return rows;
	}

	public void setRows(List<MoneyBox> rows) {
		this.rows = rows;
	}
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
}