/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.guard.entity.FingerInfo;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
/**
 * 区域Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findList(){
		return UserUtils.getAreaList();
	}
	

	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public List<Area> findParentIdsList(Area area) {
		if (area == null) {
			return new ArrayList<Area>();
		}
		return dao.findParentIdsList(area.getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	public int countByName(String id, String name) {
		if(org.apache.commons.lang.StringUtils.isBlank(name)) {
			return 0;
		}
		Area a = new Area();
		a.setId(id);
		a.setName(name);
		return super.countByColumnExceptSelf(a);
	}

	public int countByCode(String id, String code) {
		if(org.apache.commons.lang.StringUtils.isBlank(code)) {
			return 0;
		}
		Area a = new Area();
		a.setId(id);
		a.setCode(code);
		return super.countByColumnExceptSelf(a);
	}
	
}
