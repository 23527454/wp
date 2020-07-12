package cn.jeefast.modules.equipment.entity;

import java.io.Serializable;

public class EquipTree implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String name;
	
	private String parentId;
	
	private Integer type;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public EquipTree(String id,String name,String parentId,Integer type) {
		this.id=id;
		this.name=name;
		this.parentId=parentId;
		this.type=type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
