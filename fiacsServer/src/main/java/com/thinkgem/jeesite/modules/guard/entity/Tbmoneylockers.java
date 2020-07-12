/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Area;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 款箱管理表单Entity
 * @author Jumbo
 * @version 2017-06-20
 */
public class Tbmoneylockers extends DataEntity<Tbmoneylockers> {
	
	private static final long serialVersionUID = 1L;
	private String szlockername;		// 款箱名称
	private String nlockersn;		// 款箱序号
	private String szcardnumber;		// 款箱卡号
	private String szlockercode;		// 款箱编码
	private Area area;		// 关联网点
	private String uclockertype;		// 款箱类型
	private String uclockerstatus;		// 款箱状态
	private String uclockersysstatus;		// 款箱系统状态
	
	public Tbmoneylockers() {
		super();
	}

	public Tbmoneylockers(String id){
		super(id);
	}

	@Length(min=0, max=48, message="款箱名称长度必须介于 0 和 48 之间")
	public String getSzlockername() {
		return szlockername;
	}

	public void setSzlockername(String szlockername) {
		this.szlockername = szlockername;
	}
	
	public String getNlockersn() {
		return nlockersn;
	}

	public void setNlockersn(String nlockersn) {
		this.nlockersn = nlockersn;
	}
	
	@Length(min=1, max=12, message="款箱卡号长度必须介于 1 和 12 之间")
	public String getSzcardnumber() {
		return szcardnumber;
	}

	public void setSzcardnumber(String szcardnumber) {
		this.szcardnumber = szcardnumber;
	}
	
	@Length(min=0, max=16, message="款箱编码长度必须介于 0 和 16 之间")
	public String getSzlockercode() {
		return szlockercode;
	}

	public void setSzlockercode(String szlockercode) {
		this.szlockercode = szlockercode;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	public String getUclockertype() {
		return uclockertype;
	}

	public void setUclockertype(String uclockertype) {
		this.uclockertype = uclockertype;
	}
	
	public String getUclockerstatus() {
		return uclockerstatus;
	}

	public void setUclockerstatus(String uclockerstatus) {
		this.uclockerstatus = uclockerstatus;
	}
	
	public String getUclockersysstatus() {
		return uclockersysstatus;
	}

	public void setUclockersysstatus(String uclockersysstatus) {
		this.uclockersysstatus = uclockersysstatus;
	}
	
}