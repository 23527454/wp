/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.entity;


import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * access_para_infoEntity
 * @author demo
 * @version 2020-06-29
 */
public class AccessParaInfo extends DataEntity<AccessParaInfo> {
	
	private static final long serialVersionUID = 1L;
	private Equipment equipment;	//归属设备id
	private String doorPos;		// 门号 1 外门(1 号门) -- 2 内门(2 号门) 3 3 号门 -- 4 4 号门
	private Integer doorRelayTime;		// 门继电器动作时间（秒）
	private Long doorDelayTime;		// 门开延时报警时间（秒）
	private Long enterOperaTime;		// 入库操作时间（分）
	private Long checkOperaTime;		// 查库操作时间（分）
	private Long outTipsTime;		// 提醒出库时间（分）
	private Long alarmIntervalTime;		// 验证人员之间间隔时间（分）
	private Long remoteOverTime;		// 远程授权等待超时时间（分）
	private String authType;		// 验证方式 1 指纹 2 人脸 3 密码 4 人脸+指纹 5 指纹+密码 6 人脸或指纹或密码
	private Long centerPermit;		// 中心授权（管控）
	private Integer combNum;		// 组合开门数量（2 重）
	private String base1;		// 基本组合1
	private String base2;		// 基本组合2
	private String base3;		// 基本组合3
	private String base4;		// 基本组合4
	private String base5;		// 基本组合5
	private String base6;		// 基本组合6
	private String workTime1;		// 非工作时间段组合1
	private String workTime2;		// 非工作时间段组合2
	private String netOutAge1;		// TCP 断网组合1
	private String netOutAge2;		// TCP 断网组合2

	private Integer equipmentId;


	@Length(min=1, max=100, message="基本组合1长度不能超过 100 个字符")
	public String getBase1() {
		return base1;
	}

	public void setBase1(String base1) {
		this.base1 = base1;
	}

	@Length(min=1, max=100, message="基本组合2长度不能超过 100 个字符")
	public String getBase2() {
		return base2;
	}

	public void setBase2(String base2) {
		this.base2 = base2;
	}

	@Length(min=1, max=100, message="基本组合3长度不能超过 100 个字符")
	public String getBase3() {
		return base3;
	}

	public void setBase3(String base3) {
		this.base3 = base3;
	}

	@Length(min=1, max=100, message="基本组合4长度不能超过 100 个字符")
	public String getBase4() {
		return base4;
	}

	public void setBase4(String base4) {
		this.base4 = base4;
	}

	@Length(min=1, max=100, message="基本组合5长度不能超过 100 个字符")
	public String getBase5() {
		return base5;
	}

	public void setBase5(String base5) {
		this.base5 = base5;
	}

	@Length(min=1, max=100, message="基本组合6长度不能超过 100 个字符")
	public String getBase6() {
		return base6;
	}

	public void setBase6(String base6) {
		this.base6 = base6;
	}

	@Length(min=1, max=100, message="非工作时间段组合1长度不能超过 100 个字符")
	public String getWorkTime1() {
		return workTime1;
	}

	public void setWorkTime1(String workTime1) {
		this.workTime1 = workTime1;
	}

	@Length(min=1, max=100, message="非工作时间段组合2长度不能超过 100 个字符")
	public String getWorkTime2() {
		return workTime2;
	}

	public void setWorkTime2(String workTime2) {
		this.workTime2 = workTime2;
	}

	@Length(min=1, max=100, message="TCP 断网组合1长度不能超过 100 个字符")
	public String getNetOutAge1() {
		return netOutAge1;
	}

	public void setNetOutAge1(String netOutAge1) {
		this.netOutAge1 = netOutAge1;
	}

	@Length(min=1, max=100, message="TCP 断网组合2长度不能超过 100 个字符")
	public String getNetOutAge2() {
		return netOutAge2;
	}

	public void setNetOutAge2(String netOutAge2) {
		this.netOutAge2 = netOutAge2;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	public AccessParaInfo() {
		this(null);
	}

	public AccessParaInfo(String id){
		super(id);
	}

	@NotNull(message = "所属设备不能为空")
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@Length(min=1, max=100)
	public String getDoorPos() {
		return doorPos;
	}

	public void setDoorPos(String doorPos) {
		this.doorPos = doorPos;
	}
	
	@NotNull(message="门继电器动作时间不能为空")
	public Integer getDoorRelayTime() {
		return doorRelayTime;
	}

	public void setDoorRelayTime(Integer doorRelayTime) {
		this.doorRelayTime = doorRelayTime;
	}
	
	@NotNull(message="门开延时报警时间不能为空")
	public Long getDoorDelayTime() {
		return doorDelayTime;
	}

	public void setDoorDelayTime(Long doorDelayTime) {
		this.doorDelayTime = doorDelayTime;
	}

	@NotNull(message="入库操作时间不能为空")
	public Long getEnterOperaTime() {
		return enterOperaTime;
	}

	public void setEnterOperaTime(Long enterOperaTime) {
		this.enterOperaTime = enterOperaTime;
	}

	@NotNull(message="查库操作时间不能为空")
	public Long getCheckOperaTime() {
		return checkOperaTime;
	}

	public void setCheckOperaTime(Long checkOperaTime) {
		this.checkOperaTime = checkOperaTime;
	}
	
	@NotNull(message="提醒出库时间不能为空")
	public Long getOutTipsTime() {
		return outTipsTime;
	}

	public void setOutTipsTime(Long outTipsTime) {
		this.outTipsTime = outTipsTime;
	}
	
	@NotNull(message="验证人员之间间隔时间不能为空")
	public Long getAlarmIntervalTime() {
		return alarmIntervalTime;
	}

	public void setAlarmIntervalTime(Long alarmIntervalTime) {
		this.alarmIntervalTime = alarmIntervalTime;
	}
	
	@NotNull(message="远程授权等待超时时间不能为空")
	public Long getRemoteOverTime() {
		return remoteOverTime;
	}

	public void setRemoteOverTime(Long remoteOverTime) {
		this.remoteOverTime = remoteOverTime;
	}
	
	//验证方式 1 指纹 2 人脸 3 密码 4 人脸+指纹 5 指纹+密码 6 人脸或指纹或密码不能为空
	@Length(min=1, max=100)
	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
	@NotNull(message="中心授权不能为空")
	public Long getCenterPermit() {
		return centerPermit;
	}

	public void setCenterPermit(Long centerPermit) {
		this.centerPermit = centerPermit;
	}
	
	@NotNull(message="组合开门数量不能为空")
	public Integer getCombNum() {
		return combNum;
	}

	public void setCombNum(Integer combNum) {
		this.combNum = combNum;
	}
	
}