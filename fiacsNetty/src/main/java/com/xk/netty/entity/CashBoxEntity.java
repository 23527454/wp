package com.xk.netty.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.activerecord.Model;

public class CashBoxEntity extends Model<CashBoxEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String equipId;

	private Integer downLoadId;

	private String operateType;

	private String boxId;

	private String boxCode;

	private String boxInnerNum;

	private String boxType;

	private String boxAddr;
	public String getOperateType() {
		return operateType;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public Integer getDownLoadId() {
		return downLoadId;
	}

	public void setDownLoadId(Integer downLoadId) {
		this.downLoadId = downLoadId;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	public String getBoxInnerNum() {
		return boxInnerNum;
	}

	public void setBoxInnerNum(String boxInnerNum) {
		this.boxInnerNum = boxInnerNum;
	}

	public String getBoxAddr() {
		return boxAddr;
	}

	public void setBoxAddr(String boxAddr) {
		this.boxAddr = boxAddr;
	}

	public String getBoxType() {
		return boxType;
	}

	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,String> toMap(){
		Map<String,String> map = new HashMap<>();
		map.put("OperateType", this.operateType);
		map.put("CashboxID", this.boxId);
		map.put("CashboxCode", this.boxCode);
		map.put("CashboxCard", this.boxInnerNum);
		map.put("CashboxType", this.boxType);
		map.put("CashboxAddr", this.boxAddr);
		return map;
	}
}
