/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;
import com.thinkgem.jeesite.modules.sys.dao.TtsSettingDao;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;

/**
 * 语音配置信息Service
 * @author Jumbo
 * @version 2017-07-15
 */
@Service
@Transactional(readOnly = true)
public class TtsSettingService extends CrudService<TtsSettingDao, TtsSetting> {
	
	public TtsSetting get(String id) {
		return super.get(id);
	}
	
	public List<TtsSetting> getByType(String type) {
		TtsSetting ttsSetting = new TtsSetting();
		ttsSetting.setVoiceType(type);
		return dao.getByType(ttsSetting);
	}
	
	public List<TtsSetting> findList(TtsSetting ttsSetting) {
		return super.findList(ttsSetting);
	}
	
	public Page<TtsSetting> findPage(Page<TtsSetting> page, TtsSetting ttsSetting) {
		return super.findPage(page, ttsSetting);
	}
	
	@Transactional(readOnly = false)
	public void save(TtsSetting ttsSetting) {
		super.save(ttsSetting);
	}
	
	@Transactional(readOnly = false)
	public void delete(TtsSetting ttsSetting) {
		super.delete(ttsSetting);
	}
	
}