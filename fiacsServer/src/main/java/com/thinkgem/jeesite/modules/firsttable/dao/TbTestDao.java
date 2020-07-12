/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.firsttable.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.firsttable.entity.TbTest;

/**
 * 测试DAO接口
 * @author 张胜利
 * @version 2020-06-12
 */
@MyBatisDao
public interface TbTestDao extends CrudDao<TbTest> {
	
}