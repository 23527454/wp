/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.entity;


import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 事件序列Entity
 * @author Jumbo
 * @version 2017-08-02
 */
public class RecordSeq extends DataEntity<RecordSeq> {
	
	private static final long serialVersionUID = 1L;
	
	public RecordSeq() {
		super();
	}

	public RecordSeq(String id){
		super(id);
	}

}