/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 款箱事件详情Entity
 * @author Jumbo
 * @version 2017-08-01
 */
public class MoneyBoxEventDetail extends DataEntity<MoneyBoxEventDetail> {
	
	private static final long serialVersionUID = 1L;
	private String moneyBoxId;		// 款箱ID
	private String eventType; // 0 交接事件,1报警事件,2中心金库随机
	
	public static final String EVENT_TYPE_CONNECT_EVENT = "0";//交接事件
	public static final String EVENT_TYPE_ALARM_EVENT = "1";//报警事件
//	public static final String EVENT_TYPE_SUPER_GO_EVENT = "2";//中心金库随机
	
	private String recordId;		// 记录序号
	private String equipmentId;		// 设备ID
	private String equipSN;
	private Office office;//发生设备的网点
	private String time;		// 时间
	private String cardNum;		// 款箱卡号
	//add 2019-9-29
	private String boxCode; //款箱编码
	
	private MoneyBox moneyBox; //款箱
	
	private String date; //中心 金库随机事件过滤用
	
	private String beforeEqDate;//
	private String afterEqDate; //
	private Integer flag; //
	private Integer connectFlag; //
	
	private String equId;

	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	public String getEquId() {
		return equId;
	}

	public void setEquId(String equId) {
		this.equId = equId;
	}

	public Integer getConnectFlag() {
		return connectFlag;
	}

	public void setConnectFlag(Integer connectFlag) {
		this.connectFlag = connectFlag;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getBeforeEqDate() {
		return beforeEqDate;
	}

	public void setBeforeEqDate(String beforeEqDate) {
		this.beforeEqDate = beforeEqDate;
	}

	public String getAfterEqDate() {
		return afterEqDate;
	}

	public void setAfterEqDate(String afterEqDate) {
		this.afterEqDate = afterEqDate;
	}

	public MoneyBoxEventDetail() {
		super();
	}

	public MoneyBoxEventDetail(String id){
		super(id);
	}
	
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public MoneyBoxEventDetail(ConnectEvent connectEvent){
		this.recordId = connectEvent.getRecordId();
		this.equipmentId = connectEvent.getEquipmentId();
		this.time = connectEvent.getTime();
		AssertUtil.assertNotNull(this.recordId, "recordId");
		AssertUtil.assertNotNull(this.recordId, "equipmentId");
		AssertUtil.assertNotNull(this.recordId, "time");
	}

	public MoneyBoxEventDetail(AlarmEvent alarmEvent){
		this.recordId = alarmEvent.getRecordId();
		this.equipmentId = alarmEvent.getEquipmentId();
		this.time = alarmEvent.getTime();
		AssertUtil.assertNotNull(this.recordId, "recordId");
		AssertUtil.assertNotNull(this.recordId, "equipmentId");
		AssertUtil.assertNotNull(this.recordId, "time");
	}
	public MoneyBoxEventDetail(AlarmEventQuery alarmEventQuery){
		this.recordId = alarmEventQuery.getRecordId();
 		this.equipmentId = alarmEventQuery.getEquipmentId();
		this.time = alarmEventQuery.getTime();
		AssertUtil.assertNotNull(this.recordId, "recordId");
		AssertUtil.assertNotNull(this.recordId, "equipmentId");
		AssertUtil.assertNotNull(this.recordId, "time");
	}
	
	
	public String getMoneyBoxId() {
		return moneyBoxId;
	}

	public void setMoneyBoxId(String moneyBoxId) {
		this.moneyBoxId = moneyBoxId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	@Length(min=1, max=20, message="时间长度必须介于 1 和 20 之间")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	@Length(min=1, max=20, message="款箱卡号长度必须介于 1 和 20 之间")
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public MoneyBox getMoneyBox() {
		return moneyBox;
	}

	public void setMoneyBox(MoneyBox moneyBox) {
		this.moneyBox = moneyBox;
	}

	public String getEquipSN() {
		return equipSN;
	}

	public void setEquipSN(String equipSN) {
		this.equipSN = equipSN;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
}