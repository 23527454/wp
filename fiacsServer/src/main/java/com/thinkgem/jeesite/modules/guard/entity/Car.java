/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 车辆信息Entity
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
public class Car extends DataEntity<Car> {

	private static final long serialVersionUID = 1L;
	private String name; // 车辆名称
	private Area area; // 区域
	private String cardNum; // 车辆卡号
	private String carplate; // 车牌号
	private String color="#000000"; // 车辆颜色
	private String admin; // 负责人
	private String phone; // 联系方式
	private String workStatus; // 工作状态
	private String classTaskId;
	private Company company;
	private CarImage carImage; //the image of car
	
	
	public CarImage getCarImage() {
		return carImage;
	}

	public void setCarImage(CarImage carImage) {
		this.carImage = carImage;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	private MultipartFile file;


	public Car() {
		super();
	}

	public Car(String id) {
		super(id);
	}

	@Length(min = 1, max = 32, message = "车辆名称长度必须介于 1 和 32 之间")
	@ExcelField(title = "车辆名称", align = 2, sort = 1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title = "所属公司", align = 2, sort = 2)
	public String getCompanyName(){
		return company.getName();
	}
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getClassTaskId() {
		return classTaskId;
	}

	public void setClassTaskId(String classTaskId) {
		this.classTaskId = classTaskId;
	}
	
	@ExcelField(title = "所属区域", align = 2, sort = 3)
	public String getAreaName(){
		return area.getName();
	}

	@NotNull(message = "区域不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min = 1, max = 20, message = "车辆卡号长度必须介于 1 和 20 之间")
	@ExcelField(title = "车辆卡号", align = 2, sort = 4)
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	@Length(min = 1, max = 10, message = "车牌号长度必须介于 1 和 10 之间")
	@ExcelField(title = "车牌号", align = 2, sort = 5)
	public String getCarplate() {
		return carplate;
	}

	public void setCarplate(String carplate) {
		this.carplate = carplate;
	}

	@Length(min = 0, max = 10, message = "车辆颜色长度必须介于 0 和 10 之间")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min = 1, max = 32, message = "负责人长度必须介于 1 和 32 之间")
	@ExcelField(title = "负责人", align = 2, sort = 6)
	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	@Length(min = 1, max = 18, message = "联系方式长度必须介于 1 和 18 之间")
	@ExcelField(title = "联系方式", align = 2, sort = 7)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min = 1, max = 1, message = "工作状态长度必须介于 1 和 1 之间")
	@ExcelField(title = "工作状态", align = 2, sort = 8)
	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

}