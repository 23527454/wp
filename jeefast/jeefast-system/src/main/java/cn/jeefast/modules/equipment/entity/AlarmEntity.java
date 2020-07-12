package cn.jeefast.modules.equipment.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("equipment_alarm")
public class AlarmEntity  extends Model<AlarmEntity>{
	@TableId(value="id",type=IdType.AUTO)
	private Long id;
	@TableField
	private String mac;
	@TableField
	private String date;
	@TableField
	private Integer remark;
	@TableField
	private Integer resource;
	
	@TableField(exist=false)
	private String equipName;
	@TableField(exist=false)
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

	@TableField("exception_info")
	private Integer exceptionInfo;
	/**
	 * 预留字段
	 */
	@TableField("reserved")
	private Integer reserved;
	
	public Integer getReserved() {
		return reserved;
	}

	public void setReserved(Integer reserved) {
		this.reserved = reserved;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getRemark() {
		return remark;
	}

	public void setRemark(Integer remark) {
		this.remark = remark;
	}

	public Integer getResource() {
		return resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}

	public Integer getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(Integer exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
