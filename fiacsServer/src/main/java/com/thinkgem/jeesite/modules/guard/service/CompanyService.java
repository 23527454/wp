/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.CarDao;
import com.thinkgem.jeesite.modules.guard.dao.CompanyDao;
import com.thinkgem.jeesite.modules.guard.dao.CompanyExDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;
import com.thinkgem.jeesite.modules.guard.entity.Company;
import com.thinkgem.jeesite.modules.guard.entity.CompanyEx;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 第三方公司Service
 * 
 * @author Jumbo
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class CompanyService extends TreeService<CompanyDao, Company> {
	CompanyDao companyDao = SpringContextHolder.getBean(CompanyDao.class);
	StaffDao staffDao = SpringContextHolder.getBean(StaffDao.class);
	CarDao carDao = SpringContextHolder.getBean(CarDao.class);
	
	@Autowired
	private CompanyExDao companyExDao;

	public Company get(String id) {
		Company company = super.get(id);
		if (company == null) {
			return null;
		}
		company.setCompanyExList(companyExDao.findList(new CompanyEx(company)));
		return company;
	}
	
	@Transactional(readOnly = false)
	public List<Company> findParentIdsList(Company company) {
		if (company == null) {
			return new ArrayList<Company>();
		}
		return companyDao.findParentIdsList(company.getId());
	}

	public List<Company> findList(Company company) {
		return UserUtils.getCompanyList(company);
	}

	public Page<Company> findPage(Page<Company> page, Company company) {
		return super.findPage(page, company);
	}

	@Transactional(readOnly = false)
	public void save(Company company) {
		
		if(!company.getIsNewRecord()) {
			Company oldCompany = this.get(company.getId());
			//更新了区域 需要更新人员和车辆区域信息
			if(!oldCompany.getArea().getId().equals(company.getArea().getId())) {
				Map<String,Object> ofMap = new HashMap<>();
				ofMap.put("staffType", 0);
				ofMap.put("officeId", company.getId());
				List<String> staffIds = staffDao.findListByOfficeId(ofMap);
				if(staffIds!=null&&staffIds.size()>0) {
					Map<String,Object> upMap = new HashMap<>();
					upMap.put("areaId", company.getArea().getId());
					upMap.put("staffIds", staffIds);
					staffDao.updateStaffArea(upMap);
				}
				
				//更新车辆
				List<String> carIds = carDao.findListByCompanyIds(company.getId());
				if(carIds!=null&&carIds.size()>0) {
					Map<String,Object> upMap = new HashMap<>();
					upMap.put("areaId", company.getArea().getId());
					upMap.put("carIds", carIds);
					carDao.updateCarArea(upMap);
				}
			}
		}
		
		super.save(company);
		for (CompanyEx companyEx : company.getCompanyExList()) {
			if (companyEx.getId() == null) {
				continue;
			}
			if (CompanyEx.DEL_FLAG_NORMAL.equals(companyEx.getDelFlag())) {
				if (StringUtils.isBlank(companyEx.getId()) && companyEx.getImgData() != null) {
					companyEx.setCompanyId(company.getId());
					companyEx.preInsert();
					companyExDao.insert(companyEx);
				} else {
					companyEx.preUpdate();
					companyExDao.update(companyEx);
				}
			} else {
				companyExDao.delete(companyEx);
			}
		}
		UserUtils.removeCache(UserUtils.CACHE_COMPANY_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(Company company) {
		super.delete(company);
		companyExDao.delete(new CompanyEx(company));
		UserUtils.removeCache(UserUtils.CACHE_COMPANY_LIST);
	}

	public int countByShortName(String id, String shortName) {
		if(org.apache.commons.lang.StringUtils.isBlank(shortName)) {
			return 0;
		}
		Company c = new Company();
		c.setId(id);
		c.setShortName(shortName);
		return super.countByColumnExceptSelf(c);
	}
	

	public int countByFullName(String id, String fullName) {
		if(org.apache.commons.lang.StringUtils.isBlank(fullName)) {
			return 0;
		}
		Company c = new Company();
		c.setId(id);
		c.setFullName(fullName);;
		return super.countByColumnExceptSelf(c);
	}


	public int countByCompanyCode(String id, String companyCode) {
		if(org.apache.commons.lang.StringUtils.isBlank(companyCode)) {
			return 0;
		}
		Company c = new Company();
		c.setId(id);
		c.setCompanyCode(companyCode);
		return super.countByColumnExceptSelf(c);
	}

}