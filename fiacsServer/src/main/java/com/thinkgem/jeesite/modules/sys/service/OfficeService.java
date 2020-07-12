/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.guard.dao.EquipmentDao;
import com.thinkgem.jeesite.modules.guard.dao.LineNodesDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构Service
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {
	LineNodesDao lineNodesDao = SpringContextHolder.getBean(LineNodesDao.class);
	EquipmentDao equimentDao = SpringContextHolder.getBean(EquipmentDao.class);
	StaffDao staffDao = SpringContextHolder.getBean(StaffDao.class);

	@Autowired
	private OfficeDao officeDao;


	public List<Office> findAll(){return officeDao.findAll();}

	public Office getByOfficeName(String officeName){
		return super.dao.getByOfficeName(officeName);
	}

	public List<Office> findAll(Office office) {
		return UserUtils.getOfficeList(office);
	}
	public List<Office> findList(Boolean isAll, Office office) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList(office);
		}
	}
	
	public List<Office> findZxjkOfficeList(Boolean isAll) {
		List<Office> offices = null;
		if (isAll != null && isAll) {
			offices =  UserUtils.getOfficeAllList();
		} else {
			offices =  UserUtils.getOfficeList(new Office());
		}
		
		List<Office> zxjkOffices = new ArrayList<Office>();
		for (Office office : offices) {
			if(Office.TYPE_ZXJK.equals(office.getType())){
				Equipment equipment = equimentDao.findOne(new Equipment(new Office(office.getId())));
				if(null != equipment){
					zxjkOffices.add(office);
				}
			}
		}
		
		return zxjkOffices;
	}

	public List<LineNodes> lineNodeList() {
		List<LineNodes> lineNodesList = lineNodesDao.findAllList(new LineNodes());
		return lineNodesList;
	}

	public List<Equipment> equipmentList() {
		List<Equipment> equipmentList = equimentDao.findAllList(new Equipment());
		return equipmentList;
	}

	public List<Office> findParentIDList(Office office) {
		AssertUtil.assertNotNull(office.getId(), "Id");
		return dao.findByIdAndParent(office.getId());
	}

	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		return UserUtils.getOfficeList(office);
	}
	
	@Transactional(readOnly = true)
	public List<Office> findListWithSelf(Office office) {
		return UserUtils.getOfficeListNoCache(office);
	}

	@Transactional(readOnly = false)
	public void save(Office office) {
		if(!office.getIsNewRecord()) {
			Equipment equ = new Equipment();
			equ.setOffice(office);
			Equipment resultEqu = equimentDao.findOne(equ);
			if(resultEqu!=null) {
				int update = 0;
				if(!office.getName().equals(resultEqu.getControlPos())) {
					resultEqu.setControlPos(office.getName());
					update=1;
				}
				if(office.TYPE_ZXJK.equals(office.getType())&&!"1".equals(resultEqu.getSiteType())) {
					resultEqu.setSiteType("1");
					update=1;
				}else if(!office.TYPE_ZXJK.equals(office.getType())&&"1".equals(resultEqu.getSiteType())) {
					resultEqu.setSiteType("0");
					update=1;
				}
				if(update==1) {
					equimentDao.update(resultEqu);
				}
				
				//如果更新网点下人员的区域信息
				Office oldOffice = this.get(office.getId());
				if(!oldOffice.getArea().getId().equals(office.getArea().getId())) {
					Map<String,Object> ofMap = new HashMap<>();
					ofMap.put("staffType", 1);
					ofMap.put("officeId", office.getId());
					List<String> staffIds = staffDao.findListByOfficeId(ofMap);
					if(staffIds!=null&&staffIds.size()>0) {
						Map<String,Object> upMap = new HashMap<>();
						upMap.put("areaId", office.getArea().getId());
						upMap.put("staffIds", staffIds);
						staffDao.updateStaffArea(upMap);
					}
					
					//车辆更新
					//carDao.
				}
			}
		}
			super.save(office);
			UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(Office office) {
		List<Office> officeList = dao.findByIdAndParent(office.getId());
		for (int i = 0; i < officeList.size(); i++) {
			Equipment equipment = new Equipment();
			equipment.setId(officeList.get(i).getId());
			equimentDao.delete(equipment);
		}
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	public int countByName(String id, String name, Area area) {
		if(org.apache.commons.lang.StringUtils.isBlank(name) || null == area || StringUtils.isBlank(area.getId())) {
			return 0;
		}
		Office o = new Office();
		o.setId(id);
		o.setName(name);
		o.setArea(area);
		return super.countByColumnExceptSelf(o);
	}

	public int countByCode(String id, String code) {
		if(org.apache.commons.lang.StringUtils.isBlank(code)) {
			return 0;
		}
		Office o = new Office();
		o.setId(id);
		o.setCode(code);
		return super.countByColumnExceptSelf(o);
	}

	/**
	 * 统计中心金库
	 * @param id
	 * @param area
	 * @return
	 */
	public int countZxjkByAreaId(String id, Area area) {
		Office o = new Office();
		o.setId(id);
		o.setArea(area);
		o.setType(Office.TYPE_ZXJK);
		return super.countByColumnExceptSelf(o);
	}
}
