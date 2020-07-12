package com.xk.netty.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.activerecord.Model;

/**
 * 排班信息
 * 
 * @author zgx
 *
 */
public class TaskClassEntity extends Model<TaskClassEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer downLoadId;

	private String equipId;

	private String operateType;

	private Integer taskId;

	private Integer groupId;

	private Integer taskClass;

	private Integer workPersonComfirm;

	private Integer checkCarCard;

	private Integer cashBoxComfirm;

	private Integer taskValidate;

	private Integer waitTimeout;

	private Integer superNum;

	private Integer interNum;

	private String taskDate;
	
	private String startDate;
	
	private String startTime;
	
	private List<String> carNumList;
	
	private List<String> fingerNumList;
	
	public List<String> getCarNumList() {
		return carNumList;
	}

	public void setCarNumList(List<String> carNumList) {
		this.carNumList = carNumList;
	}

	public List<String> getFingerNumList() {
		return fingerNumList;
	}

	public void setFingerNumList(List<String> fingerNumList) {
		this.fingerNumList = fingerNumList;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getDownLoadId() {
		return downLoadId;
	}

	public void setDownLoadId(Integer downLoadId) {
		this.downLoadId = downLoadId;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getTaskClass() {
		return taskClass;
	}

	public void setTaskClass(Integer taskClass) {
		this.taskClass = taskClass;
	}

	public Integer getWorkPersonComfirm() {
		return workPersonComfirm;
	}

	public void setWorkPersonComfirm(Integer workPersonComfirm) {
		this.workPersonComfirm = workPersonComfirm;
	}

	public Integer getCheckCarCard() {
		return checkCarCard;
	}

	public void setCheckCarCard(Integer checkCarCard) {
		this.checkCarCard = checkCarCard;
	}

	public Integer getCashBoxComfirm() {
		return cashBoxComfirm;
	}

	public void setCashBoxComfirm(Integer cashBoxComfirm) {
		this.cashBoxComfirm = cashBoxComfirm;
	}

	public Integer getTaskValidate() {
		return taskValidate;
	}

	public void setTaskValidate(Integer taskValidate) {
		this.taskValidate = taskValidate;
	}

	public Integer getWaitTimeout() {
		return waitTimeout;
	}

	public void setWaitTimeout(Integer waitTimeout) {
		this.waitTimeout = waitTimeout;
	}

	public Integer getSuperNum() {
		return superNum;
	}

	public void setSuperNum(Integer superNum) {
		this.superNum = superNum;
	}

	public Integer getInterNum() {
		return interNum;
	}

	public void setInterNum(Integer interNum) {
		this.interNum = interNum;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("OperateType", this.operateType);
		map.put("TaskID", this.taskId);
		map.put("GroupID", this.groupId);
		map.put("TaskClass", this.taskClass);
		map.put("TaskID", this.taskId);
		map.put("TaskDate", this.startDate+" "+this.startTime);
		map.put("CheckCarCard", this.checkCarCard);
		map.put("WorkPersonComfirm", this.workPersonComfirm);
		map.put("WaitTimeout", this.waitTimeout);
		map.put("CashboxComfirm", this.cashBoxComfirm);
		map.put("SuperNum", this.superNum);
		map.put("InterNum", this.interNum);
		map.put("TaskValidate", this.taskValidate);
		map.put("carList", this.carNumList);
		map.put("superCarGoList", this.fingerNumList);
		return map;
	}

}
