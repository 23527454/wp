/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Area;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupCol;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 网点列表表单Entity
 * 
 * @author Jumbo
 * @version 2017-06-20
 */
public class Tbequipment extends DataEntity<Tbequipment> {

	private static final long serialVersionUID = 1L;
	private String nattrareaid; // 所属区域
	private String nequiptype; // 设备类型
	private String szip; // IP地址
	private String nport; // 端口
	private Area area; // 网点名称
	private String szserialnum; // 序列号
	private String szuploadeventsrvip; // 中心上传地址
	private String nuploadeventsrvport; // 中心端口
	private String ninitstate; // 初始状态
	private String sznetgate; // 网关
	private String sznetmask; // 子网掩码
	private String ndevicestatus; // 设备状态
	private String szprintserverip; // 打印服务器IP
	private String nprintserverport; // 打印服务器端口
	private String nsitetype; // 网点类型

	public Tbequipment() {
		super();
	}

	public Tbequipment(String id) {
		super(id);
	}

	@SupCol(isUnique = "true", isHide = "true")
	@ExcelField(title = "ID", type = 1, align = 2, sort = 1)
	public String getId() {
		return id;
	}

	public String getNattrareaid() {
		return nattrareaid;
	}

	public void setNattrareaid(String nattrareaid) {
		this.nattrareaid = nattrareaid;
	}

	@ExcelField(title = "设备类型", align = 2, sort = 40)
	public String getNequiptype() {
		return nequiptype;
	}

	public void setNequiptype(String nequiptype) {
		this.nequiptype = nequiptype;
	}

	@Length(min = 1, max = 32, message = "IP地址长度必须介于 1 和 32 之间")
	@ExcelField(title = "IP地址", align = 2, sort = 40)
	public String getSzip() {
		return szip;
	}

	public void setSzip(String szip) {
		this.szip = szip;
	}

	@ExcelField(title = "端口", align = 2, sort = 30)
	public String getNport() {
		return nport;
	}

	public void setNport(String nport) {
		this.nport = nport;
	}

	@ExcelField(title = "网点名称", align = 2, sort = 40)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Length(min = 1, max = 20, message = "序列号长度必须介于 1 和 20 之间")
	@ExcelField(title = "序列号", align = 2, sort = 40)
	public String getSzserialnum() {
		return szserialnum;
	}

	public void setSzserialnum(String szserialnum) {
		this.szserialnum = szserialnum;
	}

	@Length(min = 1, max = 32, message = "中心上传地址长度必须介于 1 和 32 之间")
	@ExcelField(title = "中心上传地址", align = 2, sort = 40)
	public String getSzuploadeventsrvip() {
		return szuploadeventsrvip;
	}

	public void setSzuploadeventsrvip(String szuploadeventsrvip) {
		this.szuploadeventsrvip = szuploadeventsrvip;
	}

	@ExcelField(title = "中心端口", align = 2, sort = 40)
	public String getNuploadeventsrvport() {
		return nuploadeventsrvport;
	}

	public void setNuploadeventsrvport(String nuploadeventsrvport) {
		this.nuploadeventsrvport = nuploadeventsrvport;
	}

	public String getNinitstate() {
		return ninitstate;
	}

	public void setNinitstate(String ninitstate) {
		this.ninitstate = ninitstate;
	}

	@Length(min = 0, max = 32, message = "网关长度必须介于 0 和 32 之间")
	@ExcelField(title = "网关", align = 2, sort = 40)
	public String getSznetgate() {
		return sznetgate;
	}

	public void setSznetgate(String sznetgate) {
		this.sznetgate = sznetgate;
	}

	@Length(min = 0, max = 32, message = "子网掩码长度必须介于 0 和 32 之间")
	@ExcelField(title = "子网掩码", align = 2, sort = 40)
	public String getSznetmask() {
		return sznetmask;
	}

	public void setSznetmask(String sznetmask) {
		this.sznetmask = sznetmask;
	}

	@ExcelField(title = "子网掩码", align = 2, sort = 40)
	public String getNdevicestatus() {
		return ndevicestatus;
	}

	public void setNdevicestatus(String ndevicestatus) {
		this.ndevicestatus = ndevicestatus;
	}

	@Length(min = 0, max = 32, message = "打印服务器IP长度必须介于 0 和 32 之间")
	@ExcelField(title = "打印服务器IP", align = 2, sort = 40)
	public String getSzprintserverip() {
		return szprintserverip;
	}

	public void setSzprintserverip(String szprintserverip) {
		this.szprintserverip = szprintserverip;
	}

	@ExcelField(title = "打印服务器端口", align = 2, sort = 40)
	public String getNprintserverport() {
		return nprintserverport;
	}

	public void setNprintserverport(String nprintserverport) {
		this.nprintserverport = nprintserverport;
	}

	@ExcelField(title = "网点类型", align = 2, sort = 40)
	public String getNsitetype() {
		return nsitetype;
	}

	public void setNsitetype(String nsitetype) {
		this.nsitetype = nsitetype;
	}
}