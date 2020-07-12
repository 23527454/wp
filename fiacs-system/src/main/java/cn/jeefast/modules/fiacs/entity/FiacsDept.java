package cn.jeefast.modules.fiacs.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 网点设备信息
 * @author zgx
 *
 */
@TableName("sys_office")
public class FiacsDept extends Model<FiacsDept> {

    private static final long serialVersionUID = 1L;
    
    @TableId(value="id",type=IdType.AUTO)
	private Long deptId;
    /**
     * 上级部门ID，一级部门为0
     */
    @TableField("parent_id")
	private Long parentId;
    /**
     * 部门名称
     */
    @TableField
	private String name;
    @TableField
    private String type;// 机构类型（0：一级支行；1：一级分行；2：中心金库, 3:营业网点, 4:子营业网点）
    /**
     * 排序
     */
    @TableField("sort")
	private Integer orderNum;
    /**
     * 是否删除  -1：已删除  0：正常
     */
    @TableField("del_flag")
	private Integer delFlag;
	
	/**
	 * 上级部门名称
	 */
    @TableField(exist=false)
	private String parentName;
	/**
	 * ztree属性
	 */
    @TableField(exist=false)
	private Boolean open;
    @TableField(exist=false)
	private List<?> list;
	
	/**
	 * 机构码
	 */
    @TableField(exist=false)
	private String deptCode;
    @TableField(exist=false)
	private String equipName;
    @TableField(exist=false)
	private String equipSn;
    @TableField(exist=false)
	private String equipIP;
    @TableField(exist=false)
	private String equipPort;
    @TableField(exist=false)
	private String gateway;
    @TableField(exist=false)
	private String subnetMask;
    @TableField(exist=false)
	private String centerIP;
    @TableField(exist=false)
	private String centerPort;
    @TableField(exist=false)
	private String printIP;
    @TableField(exist=false)
	private String printPort;
    @TableField(exist=false)
	private String equipType;
	
    @TableField(exist=false)
    private	String areaName;		// 归属区域
    @TableField(exist=false)
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
    @TableField(exist=false)
	private String address; // 联系地址
    @TableField(exist=false)
	private String zipCode; // 邮政编码
    @TableField(exist=false)
	private String master; 	// 负责人
    @TableField(exist=false)
	private String phone; 	// 电话
    @TableField(exist=false)
	private String fax; 	// 传真
    @TableField(exist=false)
	private String email; 	// 邮箱
    @TableField(exist=false)
	private Integer siteType;
	
	public Integer getSiteType() {
		return siteType;
	}

	public void setSiteType(Integer siteType) {
		this.siteType = siteType;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	@Override
	protected Serializable pkVal() {
		return this.deptId;
	}

	@Override
	public String toString() {
		return "SysDept{" +
			", deptId=" + deptId +
			", parentId=" + parentId +
			", name=" + name +
			", orderNum=" + orderNum +
			", delFlag=" + delFlag +
			"}";
	}
}
