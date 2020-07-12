/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;

import org.hibernate.validator.constraints.Length;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 线路信息Entity
 * 
 * @author Jumbo
 * @version 2017-06-29
 */
public class Line extends DataEntity<Line> {

	private static final long serialVersionUID = 1L;
	private String lineName; // 线路名称
	private Area area; // 区域
	private String allowWrongTimeBeFore="0"; // 前误差时间
	private String allowWrongTimeAfter="0"; // 后误差时间
	private String lineOrder; // 线路顺序
	private String nodeIunterval="30"; // 节点时间间隔
	private List<LineNodes> lineNodesList = Lists.newArrayList(); // 子表列表

	public Line() {
		super();
	}

	public Line(String id) {
		super(id);
	}

	@Length(min = 1, max = 48, message = "线路名称长度必须介于 1 和 48 之间")
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	@NotNull(message = "区域不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAllowWrongTimeBeFore() {
		return allowWrongTimeBeFore;
	}

	public void setAllowWrongTimeBeFore(String allowWrongTimeBeFore) {
		this.allowWrongTimeBeFore = allowWrongTimeBeFore;
	}

	public String getAllowWrongTimeAfter() {
		return allowWrongTimeAfter;
	}

	public void setAllowWrongTimeAfter(String allowWrongTimeAfter) {
		this.allowWrongTimeAfter = allowWrongTimeAfter;
	}

	@Length(min = 1, max = 1, message = "线路顺序长度必须介于 1 和 1 之间")
	public String getLineOrder() {
		return lineOrder;
	}

	public void setLineOrder(String lineOrder) {
		this.lineOrder = lineOrder;
	}

	public String getNodeIunterval() {
		return nodeIunterval;
	}

	public void setNodeIunterval(String nodeIunterval) {
		this.nodeIunterval = nodeIunterval;
	}

	public List<LineNodes> getLineNodesList() {
		return lineNodesList;
	}

	public void setLineNodesList(List<LineNodes> lineNodesList) {
		this.lineNodesList = lineNodesList;
	}
}