/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 车辆信息Entity
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
public class CarImage extends DataEntity<CarImage> {

	private static final long serialVersionUID = 1L;
	
	private String carId;

	//图片路径存放地址
	private String imagePath;
	
	private byte[] imgData;
	

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Length(min = 0, max = 255, message = "照片路径长度必须介于 0 和 255 之间")
	public byte[] getImgData() {
		return imgData;
	}
	
	public void setImgData(byte[] imgData) {
		this.imgData = imgData;
	}
	
	

}