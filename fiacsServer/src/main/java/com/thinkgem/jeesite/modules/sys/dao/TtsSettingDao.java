/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;

/**
 * 语音配置信息DAO接口
 * @author Jumbo
 * @version 2017-07-15
 */
@MyBatisDao
public interface TtsSettingDao extends CrudDao<TtsSetting> {
	public List<TtsSetting> getByType(TtsSetting ttsSetting);
}