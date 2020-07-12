package cn.jeefast.modules.fiacs.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.jeefast.common.validator.group.AddGroup;
import cn.jeefast.common.validator.group.Group;
import cn.jeefast.common.validator.group.UpdateGroup;

@XStreamAlias("ProtocolRoot")
@TableName("guard_equipment")
public class EquipEntity extends Model<EquipEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id",type=IdType.AUTO)
	private int id;
	
	@TableField(exist=false)
	@XStreamAlias("AuthorCode")
	private String authorCode;
	
	@TableField("control_pos")
	@XStreamAlias("EquipName")
	private String equipName;
	
	@TableField("serial_num")
	@XStreamAlias("EquipSN")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="设备序列号不能为空")
	@Length(groups= {updateEquip.class,SynEquip.class},max=16,min=16,message="系列号长度为16位")
	private String equipSn;
	
	@TableField("ip")
	@XStreamAlias("EquipIP")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="设备地址不能为空")
	@Pattern(groups= {updateEquip.class,SynEquip.class},message="ip格式不对",regexp="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
	private String equipIP;
	
	@TableField("port")
	@XStreamAlias("EquipPort")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="设备端口不能为空")
	@Range(groups= {updateEquip.class,SynEquip.class},min=0,max=65535,message="端口号范围0~65535")
	private String equipPort;
	
	@TableField("net_gate")
	@XStreamAlias("Gateway")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="网关地址不能为空")
	@Pattern(groups= {updateEquip.class,SynEquip.class},message="网关格式不对",regexp="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
	private String gateway;
	
	@TableField("net_mask")
	@XStreamAlias("SubnetMask")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="子网掩码不能为空")
	private String subnetMask;
	
	@TableField("upload_event_srv_ip")
	@XStreamAlias("CenterIP")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="中心地址不能为空")
	@Pattern(groups= {updateEquip.class,SynEquip.class},message="中心地址格式不对",regexp="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
	private String centerIP;
	
	@TableField("upload_event_srv_port")
	@XStreamAlias("CenterPort")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="中心端口不能为空")
	@Range(groups= {updateEquip.class,SynEquip.class},min=0,max=65535,message="端口号范围0~65535")
	private String centerPort;
	
	@TableField("print_server_ip")
	@XStreamAlias("PrinterIP")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="打印地址不能为空")
	@Pattern(groups= {updateEquip.class,SynEquip.class},message="打印地址格式不对",regexp="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
	private String printIP;
	
	@TableField("print_server_port")
	@XStreamAlias("PrinterPort")
	@NotBlank(groups= {updateEquip.class,SynEquip.class},message="打印端口不能为空")
	@Range(groups= {updateEquip.class,SynEquip.class},min=0,max=65535,message="端口号范围0~65535")
	private String printPort;
	
	@TableField("equip_type")
	@XStreamAlias("EquipType")
	private String equipType;
	
	@TableField(exist=false)
	@XStreamAlias("EquiType")
	private String equiType;
	
	@TableField(exist=false)
	@XStreamAlias("EquipTaskType")
	private String equipTaskType;
	
	@TableField("office_id")
	private String officeId;
	
	@TableField(exist=false)
	@NotBlank(message="网点名称不能为空",groups= {updateEquip.class})
	private String officeName;
	
	// 网点类型 0 营业网点 / 1中心金库
	@TableField("site_type")
	private String siteType;
	
	@TableField(exist=false)
	private String oldIP;
	
	@TableField(exist=false)
	private String oldPort;
	
	@TableField(exist=false)
	//0 app重启  1 系统重启  2 初始化
	private int resetType;
	
	@TableField("access_type")
	private Integer accessType;
	
	public EquipEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public EquipEntity(String officeId) {
		this.officeId = officeId;
	}
	
	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

	public int getResetType() {
		return resetType;
	}

	public void setResetType(int resetType) {
		this.resetType = resetType;
	}

	public String getOldIP() {
		return oldIP;
	}

	public void setOldIP(String oldIP) {
		this.oldIP = oldIP;
	}

	public String getOldPort() {
		return oldPort;
	}

	public void setOldPort(String oldPort) {
		this.oldPort = oldPort;
	}

	public String getEquiType() {
		return equiType;
	}

	public void setEquiType(String equiType) {
		this.equiType = equiType;
	}

	public String getEquipTaskType() {
		return equipTaskType;
	}

	public void setEquipTaskType(String equipTaskType) {
		this.equipTaskType = equipTaskType;
	}

	public String getAuthorCode() {
		return authorCode;
	}

	public void setAuthorCode(String authorCode) {
		this.authorCode = authorCode;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	@TableField(exist=false)
	private boolean isAddDataSource;
	
	@TableField(exist=false)
	private boolean isReset;
	
	public boolean isAddDataSource() {
		return isAddDataSource;
	}
	
	public void setAddDataSource(boolean isAddDataSource) {
		this.isAddDataSource = isAddDataSource;
	}
	public boolean isReset() {
		return isReset;
	}
	public void setReset(boolean isReset) {
		this.isReset = isReset;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	public String getEquipSn() {
		return equipSn;
	}
	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}
	public String getEquipIP() {
		return equipIP;
	}
	public void setEquipIP(String equipIP) {
		this.equipIP = equipIP;
	}
	public String getEquipPort() {
		return equipPort;
	}
	public void setEquipPort(String equipPort) {
		this.equipPort = equipPort;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getSubnetMask() {
		return subnetMask;
	}
	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}
	public String getCenterIP() {
		return centerIP;
	}
	public void setCenterIP(String centerIP) {
		this.centerIP = centerIP;
	}
	public String getCenterPort() {
		return centerPort;
	}
	public void setCenterPort(String centerPort) {
		this.centerPort = centerPort;
	}
	public String getPrintIP() {
		return printIP;
	}
	public void setPrintIP(String printIP) {
		this.printIP = printIP;
	}
	public String getPrintPort() {
		return printPort;
	}
	public void setPrintPort(String printPort) {
		this.printPort = printPort;
	}
	public String getEquipType() {
		return equipType;
	}
	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}
	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,Object> toMap(){
		Map<String, Object> map = new HashMap<>();
		map.put("EquipName", this.equipName);
		map.put("EquipSN", this.equipSn);
		map.put("EquipIP", this.equipIP);
		map.put("EquipPort", this.equipPort);
		map.put("Gateway", this.gateway);
		map.put("SubnetMask", this.subnetMask);
		map.put("CenterIP", this.centerIP);
		map.put("CenterPort", this.centerPort);
		map.put("PrinterIP", this.printIP);
		map.put("PrinterPort", this.printPort);
		map.put("EquipType", this.equipType);
		map.put("SocketIP", StringUtils.isBlank(this.oldIP)?this.equipIP:this.oldIP);
		map.put("SocketPort", StringUtils.isBlank(this.oldPort)?this.equipPort:this.oldPort);
		return map;
	}
	
	public interface SynEquip{
		
	}
	
	public interface updateEquip{
		
	}
}
