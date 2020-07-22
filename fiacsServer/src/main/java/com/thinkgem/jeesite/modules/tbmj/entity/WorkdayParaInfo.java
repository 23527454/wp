/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * access_workdayEntity
 * @author demo
 * @version 2020-07-02
 */

public class WorkdayParaInfo extends DataEntity<WorkdayParaInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;	//设备
	private Integer workdayNum;		//工作日表号
	private String year;		// 年
	private String jan;				//1月
	private String feb;				//2月
	private String mar;				//3月
	private String apr;				//4月
	private String may;				//5月
	private String jun;				//6月
	private String jul;				//7月
	private String aug;				//8月
	private String sep;				//9月
	private String oct;				//10月
	private String nov;				//11月
	private String dec;				//12月


	private String month;		// 月
	private String day;		// 日，0休息  1工作
	private List<String> restDay;		//日，所有的休息日
	private Integer restIndex;			//休息日的下标，1开始，最大31，对应这个月的第几天
	private String equipmentId;

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Integer getRestIndex() {
		return restIndex;
	}

	public void setRestIndex(Integer restIndex) {
		this.restIndex = restIndex;
	}

	public List<String> getRestDay() {
		return restDay;
	}

	public void setRestDay(List<String> restDay) {
		this.restDay = restDay;
	}

	@ExcelField(title = "网点名称", align = 2, sort = 1)
	public String getEquipmentControlPos(){
		return equipment.getControlPos();
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public WorkdayParaInfo() {
		super();
	}

	public WorkdayParaInfo(String id){
		super(id);
	}
	
	@NotBlank(message="年不能为空")
	@Length(min=0, max=4, message="年长度不能超过 4 个字符")
	@ExcelField(title = "年", align = 2, sort = 2)
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@NotBlank(message="月不能为空")
	@Length(min=0, max=2, message="月长度不能超过 2 个字符")
	@ExcelField(title = "月", align = 2, sort = 3)
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@ExcelField(title = "日", align = 2, sort = 4)
	public String getDay2(){
		for(int i=1;i<=day.length();i++){
			return String.valueOf(i);
		}
		return "";
	}


	@ExcelField(title = "状态", align = 2, sort = 5)
	public String getStatus(){
		for(int i=1;i<=day.length();i++){
			if(day.substring(i-1,i).equals("1")){
				return "工作";
			}else{
				return "休息";
			}
		}
		return "";
	}

	@NotBlank(message="日不能为空")
	@Length(min=0, max=31, message="日长度不能超过 31 个字符")
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getWorkdayNum() {
		return workdayNum;
	}

	public void setWorkdayNum(Integer workdayNum) {
		this.workdayNum = workdayNum;
	}

	public String getJan() {
		return jan;
	}

	public void setJan(String jan) {
		this.jan = jan;
	}

	public String getFeb() {
		return feb;
	}

	public void setFeb(String feb) {
		this.feb = feb;
	}

	public String getMar() {
		return mar;
	}

	public void setMar(String mar) {
		this.mar = mar;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getMay() {
		return may;
	}

	public void setMay(String may) {
		this.may = may;
	}

	public String getJun() {
		return jun;
	}

	public void setJun(String jun) {
		this.jun = jun;
	}

	public String getJul() {
		return jul;
	}

	public void setJul(String jul) {
		this.jul = jul;
	}

	public String getAug() {
		return aug;
	}

	public void setAug(String aug) {
		this.aug = aug;
	}

	public String getSep() {
		return sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}

	public String getOct() {
		return oct;
	}

	public void setOct(String oct) {
		this.oct = oct;
	}

	public String getNov() {
		return nov;
	}

	public void setNov(String nov) {
		this.nov = nov;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
}