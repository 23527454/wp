/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 班组Entity
 * 
 * @author Jumbo
 * @version 2017-07-29
 */
public class ClassCarInfo extends DataEntity<ClassCarInfo> {

	private static final long serialVersionUID = 1L;
	private String classTaskId; // 班组ID 父类
	private String carId; // 车辆ID
	private String cardNum; // 车辆卡号
	private String name;  // 车辆名称
	private String carplate;   //车牌号
	private String delete="1";

	public ClassCarInfo() {
		super();
	}

	public ClassCarInfo(String id) {
		super(id);
	}

	public ClassCarInfo(ClassTaskInfo classTaskId) {
		if (classTaskId != null) {
			this.classTaskId = classTaskId.getId();
			if(classTaskId.getCar()!=null){
				this.name = classTaskId.getCar().getName();
			}
		}
		
	}
	
	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarplate() {
		return carplate;
	}

	public void setCarplate(String carplate) {
		this.carplate = carplate;
	}

	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	@Length(min = 1, max = 20, message = "车牌号长度必须介于 1 和 20 之间")
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

}