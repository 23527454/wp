/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 车辆同步信息Entity
 * @author Jumbo
 * @version 2017-06-28
 */
public class DownloadCar extends DownloadEntity<DownloadCar> {
	
	private static final long serialVersionUID = 1L;
	private String carId;		// 车辆ID
	private String equipId;		// 设备ID

	private Car car;
	private Office office;
	private String downloadTimeTwo;
	private List<String> officeIds;
	
	
	public List<String> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<String> officeIds) {
		this.officeIds = officeIds;
	}

	public DownloadCar() {
		super();
	}

	public DownloadCar(String id){
		super(id);
	}
	

	public String getDownloadTimeTwo() {
		return downloadTimeTwo;
	}

	public void setDownloadTimeTwo(String downloadTimeTwo) {
		this.downloadTimeTwo = downloadTimeTwo;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}
	
	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	
	
}