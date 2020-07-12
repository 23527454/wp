/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupCol;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备信息Entity
 *
 * @author Jumbo
 * @version 2017-06-27
 */
public class Equipment extends DataEntity<Equipment> {

	private static final long serialVersionUID = 1L;
	private Office office; // 归属部门ID
	private String equipType; // 设备类型
	private String controlPos; // 设备位置
	private String ip; // 设备IP地址
	private String port = "8001"; // 设备端口号
	private String serialNum; // 设备序列号
	private String uploadEventSrvIp = "192.168.1.100"; // 事件主动上传服务器的IP
	private String uploadEventSrvPort = "10001"; // 事件主动上传服务器的端口
	private String netGate="192.168.1.1"; // 网关
	private String netMask="255.255.255.0"; // 子网掩码
	private String status = "1"; // 设备状态
	private String siteType; // 网点类型 0 营业网点 / 1中心金库  ->门禁系统这里改为（门控类型）：0 金库门，1 联动门 2防护隔离门
	private String equipment_ids;
	private Area area;
	private String printServerIp = "192.168.1.100"; // 打印服务器IP地址
	private String printServerPort = "15000"; // 打印服务器端口

	private String equ_office_id;   //用于判断是否存在于设备
	private String equ_office_type;   // 是否可以添加为设备的标记
	
	private List<TbAccessParameters> accessParametersList;
	
	private List<TbDoorTimeZone> doorTimeZonesList;
	
	public static final String SITE_TYPE_YYWD= "0";//营业网店

	public static final String SITE_TYPE_ZXJK= "1";//中心金库
	
	private Integer accessType;
	
	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

	public List<TbAccessParameters> getAccessParametersList() {
		return accessParametersList;
	}

	public void setAccessParametersList(List<TbAccessParameters> accessParametersList) {
		this.accessParametersList = accessParametersList;
	}

	public List<TbDoorTimeZone> getDoorTimeZonesList() {
		return doorTimeZonesList;
	}

	public void setDoorTimeZonesList(List<TbDoorTimeZone> doorTimeZonesList) {
		this.doorTimeZonesList = doorTimeZonesList;
	}

	public Equipment() {
		super();
	}

	public Equipment(String id) {
		super(id);
	}
	
	public Equipment(Office office) {
		this.office = office;
	}
	
	

	public String getEqu_office_id() {
		return equ_office_id;
	}

	public void setEqu_office_id(String equ_office_id) {
		this.equ_office_id = equ_office_id;
	}

	public String getEqu_office_type() {
		return equ_office_type;
	}

	public void setEqu_office_type(String equ_office_type) {
		this.equ_office_type = equ_office_type;
	}

	public String getEquipment_ids() {
		return equipment_ids;
	}

	public void setEquipment_ids(String equipment_ids) {
		this.equipment_ids = equipment_ids;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@SupCol(isUnique = "true", isHide = "true")
	public String getId() {
		return id;
	}

	@ExcelField(title = "所属网点", align = 2, sort = 1)
	private String getOfficeName() {
		return office.getName();
	}

	@NotNull(message = "所属网点不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min = 1, max = 1, message = "设备类型长度必须介于 1 和 1 之间")
	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	@Length(min = 1, max = 64, message = "设备位置长度必须介于 1 和 64 之间")
	public String getControlPos() {
		return controlPos;
	}

	public void setControlPos(String controlPos) {
		this.controlPos = controlPos;
	}

	@Length(min = 1, max = 32, message = "设备IP地址长度必须介于 1 和 32 之间")
	@ExcelField(title = "IP地址", align = 2, sort = 2)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@ExcelField(title = "端口号", align = 2, sort = 3)
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Length(min = 0, max = 20, message = "设备序列号长度必须介于 0 和 20 之间")
	@ExcelField(title = "序列号", align = 2, sort = 10)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@Length(min = 1, max = 32, message = "事件主动上传服务器的IP长度必须介于 1 和 32 之间")
	@ExcelField(title = "中心上传IP", align = 2, sort = 25)
	public String getUploadEventSrvIp() {
		return uploadEventSrvIp;
	}

	public void setUploadEventSrvIp(String uploadEventSrvIp) {
		this.uploadEventSrvIp = uploadEventSrvIp;
	}
	@ExcelField(title = "中心上传端口", align = 2, sort = 30)
	public String getUploadEventSrvPort() {
		return uploadEventSrvPort;
	}

	public void setUploadEventSrvPort(String uploadEventSrvPort) {
		this.uploadEventSrvPort = uploadEventSrvPort;
	}

	@Length(min = 0, max = 32, message = "网关长度必须介于 0 和 32 之间")
	@ExcelField(title = "网关", align = 2, sort = 20)
	public String getNetGate() {
		return netGate;
	}

	public void setNetGate(String netGate) {
		this.netGate = netGate;
	}

	@Length(min = 0, max = 32, message = "子网掩码长度必须介于 0 和 32 之间")
	@ExcelField(title = "子网掩码", align = 2, sort = 15)
	public String getNetMask() {
		return netMask;
	}

	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}

	@Length(min = 1, max = 1, message = "设备状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	//门禁系统这里设置为门控类型，注意了
	@Length(min = 1, max = 1, message = "网点类型长度必须介于 1 和 1 之间")
	//@ExcelField(title = "网点类型", align = 2, sort = 8, dictType = "site_type")
	@ExcelField(title = "门控类型", align = 2, sort = 8, dictType = "site_type")
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	@Length(min = 1, max = 32, message = "打印服务器IP地址长度必须介于 1 和 32 之间")
	@ExcelField(title = "打印服务器IP", align = 2, sort = 32)
	public String getPrintServerIp() {
		return printServerIp;
	}

	public void setPrintServerIp(String printServerIp) {
		this.printServerIp = printServerIp;
	}

	@ExcelField(title = "打印服务器IP端口", align = 2, sort = 35)
	public String getPrintServerPort() {
		return printServerPort;
	}

	public void setPrintServerPort(String printServerPort) {
		this.printServerPort = printServerPort;
	}

}