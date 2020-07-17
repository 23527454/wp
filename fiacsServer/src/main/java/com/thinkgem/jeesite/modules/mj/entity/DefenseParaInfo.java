/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * access_defense_infoEntity
 * @author demo
 * @version 2020-07-03
 */

public class DefenseParaInfo extends DataEntity<DefenseParaInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;		// 设备编号
	private Integer defensePos;		// 防区号
	private Integer defenseAreaType;		// 防区类型
	private String defenseAreaBypass;		// 防区旁路
	private Integer defenseAreaAttr;		// 防区属性
	private Integer alarmDelayTime;		// 报警延时时间
	private String timeStart1;		// 时段一开始
	private String timeEnd1;		// 时段一结束
	private String timeStart2;		// 时段二开始
	private String timeEnd2;		// 时段二结束
	private String timeStart3;		// 时段三开始
	private String timeEnd3;		// 时段三结束
	private String timeStart4;		// 时段四开始
	private String timeEnd4;		// 时段四结束
	private String delFlag;		// 删除标记
	private Integer id2;

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}

	public DefenseParaInfo() {
		super();
	}

	public DefenseParaInfo(String id){
		super(id);
	}

	public DefenseParaInfo(Integer defensePos, Integer defenseAreaType, String defenseAreaBypass, Integer defenseAreaAttr, Integer alarmDelayTime, String timeStart1, String timeEnd1, String timeStart2, String timeEnd2, String timeStart3, String timeEnd3, String timeStart4, String timeEnd4) {
		this.defensePos = defensePos;
		this.defenseAreaType = defenseAreaType;
		this.defenseAreaBypass = defenseAreaBypass;
		this.defenseAreaAttr = defenseAreaAttr;
		this.alarmDelayTime = alarmDelayTime;
		this.timeStart1 = timeStart1;
		this.timeEnd1 = timeEnd1;
		this.timeStart2 = timeStart2;
		this.timeEnd2 = timeEnd2;
		this.timeStart3 = timeStart3;
		this.timeEnd3 = timeEnd3;
		this.timeStart4 = timeStart4;
		this.timeEnd4 = timeEnd4;
	}

	@ExcelField(title = "网点名称", align = 2, sort = 1)
	public String getEquipmentControlPos(){
		return equipment.getControlPos();
	}

	@NotNull(message = "所属设备不能为空")
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@NotNull(message="防区号不能为空")
	@ExcelField(title = "防区号", align = 2, sort = 10)
	public Integer getDefensePos() {
		return defensePos;
	}

	public void setDefensePos(Integer defensePos) {
		this.defensePos = defensePos;
	}
	
	@NotNull(message="防区类型不能为空")
	@ExcelField(title = "防区类型", align = 2, sort = 13)
	public Integer getDefenseAreaType() {
		return defenseAreaType;
	}

	public void setDefenseAreaType(Integer defenseAreaType) {
		this.defenseAreaType = defenseAreaType;
	}
	
	@NotBlank(message="防区旁路不能为空")
	@ExcelField(title = "防区旁路", align = 2, sort = 15,dictType = "yes_no")
	public String getDefenseAreaBypass() {
		return defenseAreaBypass;
	}

	public void setDefenseAreaBypass(String defenseAreaBypass) {
		this.defenseAreaBypass = defenseAreaBypass;
	}
	
	@NotNull(message="防区属性不能为空")
	@ExcelField(title = "防区属性", align = 2, sort = 18,dictType = "yes_no")
	public Integer getDefenseAreaAttr() {
		return defenseAreaAttr;
	}

	public void setDefenseAreaAttr(Integer defenseAreaAttr) {
		this.defenseAreaAttr = defenseAreaAttr;
	}
	
	@NotNull(message="报警延时时间不能为空")
	@ExcelField(title = "报警延时时间", align = 2, sort = 20)
	public Integer getAlarmDelayTime() {
		return alarmDelayTime;
	}

	public void setAlarmDelayTime(Integer alarmDelayTime) {
		this.alarmDelayTime = alarmDelayTime;
	}
	
	@NotBlank(message="时段一开始不能为空")
	@ExcelField(title = "时段一起始时间", align = 2, sort = 23)
	@Length(min=0, max=5, message="时段一开始长度不能超过 5 个字符")
	public String getTimeStart1() {
		return timeStart1;
	}

	public void setTimeStart1(String timeStart1) {
		this.timeStart1 = timeStart1;
	}
	
	@NotBlank(message="时段一结束不能为空")
	@ExcelField(title = "时段一结束时间", align = 2, sort = 24)
	@Length(min=0, max=5, message="时段一结束长度不能超过 5 个字符")
	public String getTimeEnd1() {
		return timeEnd1;
	}

	public void setTimeEnd1(String timeEnd1) {
		this.timeEnd1 = timeEnd1;
	}
	
	@NotBlank(message="时段二开始不能为空")
	@Length(min=0, max=5, message="时段二开始长度不能超过 5 个字符")
	@ExcelField(title = "时段二起始时间", align = 2, sort = 26)
	public String getTimeStart2() {
		return timeStart2;
	}

	public void setTimeStart2(String timeStart2) {
		this.timeStart2 = timeStart2;
	}
	
	@NotBlank(message="时段二结束不能为空")
	@Length(min=0, max=5, message="时段二结束长度不能超过 5 个字符")
	@ExcelField(title = "时段二结束时间", align = 2, sort = 27)
	public String getTimeEnd2() {
		return timeEnd2;
	}

	public void setTimeEnd2(String timeEnd2) {
		this.timeEnd2 = timeEnd2;
	}
	
	@NotBlank(message="时段三开始不能为空")
	@Length(min=0, max=5, message="时段三开始长度不能超过 5 个字符")
	@ExcelField(title = "时段三起始时间", align = 2, sort = 29)
	public String getTimeStart3() {
		return timeStart3;
	}

	public void setTimeStart3(String timeStart3) {
		this.timeStart3 = timeStart3;
	}
	
	@NotBlank(message="时段三结束不能为空")
	@Length(min=0, max=5, message="时段三结束长度不能超过 5 个字符")
	@ExcelField(title = "时段三结束时间", align = 2, sort = 30)
	public String getTimeEnd3() {
		return timeEnd3;
	}

	public void setTimeEnd3(String timeEnd3) {
		this.timeEnd3 = timeEnd3;
	}
	
	@NotBlank(message="时段四开始不能为空")
	@Length(min=0, max=5, message="时段四开始长度不能超过 5 个字符")
	@ExcelField(title = "时段四起始时间", align = 2, sort = 32)
	public String getTimeStart4() {
		return timeStart4;
	}

	public void setTimeStart4(String timeStart4) {
		this.timeStart4 = timeStart4;
	}
	
	@NotBlank(message="时段四结束不能为空")
	@Length(min=0, max=5, message="时段四结束长度不能超过 5 个字符")
	@ExcelField(title = "时段四结束时间", align = 2, sort = 23)
	public String getTimeEnd4() {
		return timeEnd4;
	}

	public void setTimeEnd4(String timeEnd4) {
		this.timeEnd4 = timeEnd4;
	}
	
	@NotBlank(message="删除标记不能为空")
	@Length(min=0, max=1, message="删除标记长度不能超过 1 个字符")
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}