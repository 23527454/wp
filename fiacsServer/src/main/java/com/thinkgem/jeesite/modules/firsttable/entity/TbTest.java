/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.firsttable.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 测试Entity
 * @author 张胜利
 * @version 2020-06-12
 */
public class TbTest extends DataEntity<TbTest> {
	
	private static final long serialVersionUID = 1L;
	private String phone;		// phone
	private String name;		// name
	private Integer age;		// age
	private Office office;		// office_id

	private String equipment_ids;
	
	public TbTest() {
		super();
	}

	public TbTest(String id){
		super(id);
	}

	@Length(min=1, max=255, message="phone长度必须介于 1 和 255 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=1, max=255, message="name长度必须介于 1 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="age不能为空")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	@NotNull(message="office_id不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getEquipment_ids() {
		return equipment_ids;
	}

	public void setEquipment_ids(String equipment_ids) {
		this.equipment_ids = equipment_ids;
	}
}