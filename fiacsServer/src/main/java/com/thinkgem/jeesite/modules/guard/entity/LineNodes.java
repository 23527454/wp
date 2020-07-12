/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 线路信息Entity
 * @author Jumbo
 * @version 2017-06-29
 */
public class LineNodes extends DataEntity<LineNodes> {
	
	private static final long serialVersionUID = 1L;
	private String lineId;		// 线路ID 父类
	private Office office;		// 设备ID
	private String nodeSn;		// 节点序号
	private String nodeNextGap;		// 与下节点间隔时间
	private String equipmentId;
	
	public LineNodes() {
		super();
	}

	public LineNodes(String id){
		super(id);
	}

	public LineNodes(Line line){
		this.lineId = line.getId();
	}

	public String getLineId() {
		return lineId;
	}

	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	@NotNull(message="设备ID不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public String getNodeSn() {
		return nodeSn;
	}

	public void setNodeSn(String nodeSn) {
		this.nodeSn = nodeSn;
	}
	
	public String getNodeNextGap() {
		return nodeNextGap;
	}

	public void setNodeNextGap(String nodeNextGap) {
		this.nodeNextGap = nodeNextGap;
	}
	
}