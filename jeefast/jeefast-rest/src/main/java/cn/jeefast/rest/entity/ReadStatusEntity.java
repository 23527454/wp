package cn.jeefast.rest.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ReadStatusEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//闸机运行模式
	private String zjWorkModel;
	/**
	 * 红外输入状态
	 * 
	 * 3字节 bit   低位在前
	 */
	private String redLine;
	/**
	 * 霍尔状态
	 * 
	 * 1字节 bit  主霍尔占用bit4-6；从霍尔占用bit0-2
	 */
	private String heStatus;
	//主闸机状态
	private String zzjStatus;
	//从闸机状态
	private String czjStatus;
	//主驱动异常
	private String zDriverStatus;
	//从驱动异常
	private String cDriverStatus;
	//闸机通行状态
	private String zjCrossStatus;
	//控制器状态
	private String kzqStatus;
	//场内人员计数
	private String personTotal;
	public String getZjWorkModel() {
		return zjWorkModel;
	}
	public void setZjWorkModel(String zjWorkModel) {
		this.zjWorkModel = zjWorkModel;
	}
	
	public String getRedLine() {
		return redLine;
	}
	public void setRedLine(String redLine) {
		this.redLine = redLine;
	}
	public String getHeStatus() {
		return heStatus;
	}
	public void setHeStatus(String heStatus) {
		this.heStatus = heStatus;
	}
	public String getZzjStatus() {
		return zzjStatus;
	}
	public void setZzjStatus(String zzjStatus) {
		this.zzjStatus = zzjStatus;
	}
	public String getCzjStatus() {
		return czjStatus;
	}
	public void setCzjStatus(String czjStatus) {
		this.czjStatus = czjStatus;
	}
	public String getzDriverStatus() {
		return zDriverStatus;
	}
	public void setzDriverStatus(String zDriverStatus) {
		this.zDriverStatus = zDriverStatus;
	}
	public String getcDriverStatus() {
		return cDriverStatus;
	}
	public void setcDriverStatus(String cDriverStatus) {
		this.cDriverStatus = cDriverStatus;
	}
	public String getZjCrossStatus() {
		return zjCrossStatus;
	}
	public void setZjCrossStatus(String zjCrossStatus) {
		this.zjCrossStatus = zjCrossStatus;
	}
	
	public String getKzqStatus() {
		return kzqStatus;
	}
	public void setKzqStatus(String kzqStatus) {
		this.kzqStatus = kzqStatus;
	}
	public String getPersonTotal() {
		return personTotal;
	}
	public void setPersonTotal(String personTotal) {
		this.personTotal = personTotal;
	}
	
}
