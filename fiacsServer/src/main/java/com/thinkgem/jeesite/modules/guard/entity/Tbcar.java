/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 车辆管理生成Entity
 * @author Jumbo
 * @version 2017-06-26
 */
public class Tbcar extends DataEntity<Tbcar> {
	
	private static final long serialVersionUID = 1L;
	private String ncarno;		// 车辆编号
	private String szcardnum;		// 卡号
	private String szcarplate;		// 车牌号
	private String szcarmodel;		// 车辆类型
	private String ncarcolor;		// 车辆颜色
	private Date tenterdate;		// 录入日期
	private String nworkstatus;		// 车辆状态
	private String ncarsysstatus;		// 车辆系统状态
	private String szadmin;		// 负责人
	private String szadminphone;		// 负责人联系方式
	private String carbrand;		// 车辆品牌
	private String szcarimage;		// 车辆图片
	
	public Tbcar() {
		super();
	}

	public Tbcar(String id){
		super(id);
	}

	public String getNcarno() {
		return ncarno;
	}

	public void setNcarno(String ncarno) {
		this.ncarno = ncarno;
	}
	
	@Length(min=1, max=20, message="卡号长度必须介于 1 和 20 之间")
	public String getSzcardnum() {
		return szcardnum;
	}

	public void setSzcardnum(String szcardnum) {
		this.szcardnum = szcardnum;
	}
	
	@Length(min=0, max=10, message="车牌号长度必须介于 0 和 10 之间")
	public String getSzcarplate() {
		return szcarplate;
	}

	public void setSzcarplate(String szcarplate) {
		this.szcarplate = szcarplate;
	}
	
	@Length(min=0, max=10, message="车辆类型长度必须介于 0 和 10 之间")
	public String getSzcarmodel() {
		return szcarmodel;
	}

	public void setSzcarmodel(String szcarmodel) {
		this.szcarmodel = szcarmodel;
	}
	
	@Length(min=0, max=10, message="车辆颜色长度必须介于 0 和 10 之间")
	public String getNcarcolor() {
		return ncarcolor;
	}

	public void setNcarcolor(String ncarcolor) {
		this.ncarcolor = ncarcolor;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTenterdate() {
		return tenterdate;
	}

	public void setTenterdate(Date tenterdate) {
		this.tenterdate = tenterdate;
	}
	
	public String getNworkstatus() {
		return nworkstatus;
	}

	public void setNworkstatus(String nworkstatus) {
		this.nworkstatus = nworkstatus;
	}
	
	public String getNcarsysstatus() {
		return ncarsysstatus;
	}

	public void setNcarsysstatus(String ncarsysstatus) {
		this.ncarsysstatus = ncarsysstatus;
	}
	
	@Length(min=0, max=32, message="负责人长度必须介于 0 和 32 之间")
	public String getSzadmin() {
		return szadmin;
	}

	public void setSzadmin(String szadmin) {
		this.szadmin = szadmin;
	}
	
	@Length(min=0, max=20, message="负责人联系方式长度必须介于 0 和 20 之间")
	public String getSzadminphone() {
		return szadminphone;
	}

	public void setSzadminphone(String szadminphone) {
		this.szadminphone = szadminphone;
	}
	
	@Length(min=0, max=32, message="车辆品牌长度必须介于 0 和 32 之间")
	public String getCarbrand() {
		return carbrand;
	}

	public void setCarbrand(String carbrand) {
		this.carbrand = carbrand;
	}
	
	@Length(min=0, max=255, message="车辆图片长度必须介于 0 和 255 之间")
	public String getSzcarimage() {
		return szcarimage;
	}

	public void setSzcarimage(String szcarimage) {
		this.szcarimage = szcarimage;
	}
	
}