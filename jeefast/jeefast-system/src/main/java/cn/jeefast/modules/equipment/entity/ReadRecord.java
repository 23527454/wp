package cn.jeefast.modules.equipment.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class ReadRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mac;
	//时间
	@NotNull(message="时间不能为空")
	private Date date;
	//备注 2通行记录  3 异常事件记录  4 报警记录
	@NotNull(message="备注不能为空")
	private String remark;
	//事件来源
	@NotNull(message="事件来源不能为空")
	private String resource;
	//异常事件详情
	private String exceptionInfo;
	
	private String equipName;
	
	private String equipReq;
	
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	public String getEquipReq() {
		return equipReq;
	}
	public void setEquipReq(String equipReq) {
		this.equipReq = equipReq;
	}
	public String getExceptionInfo() {
		return exceptionInfo;
	}
	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
}
