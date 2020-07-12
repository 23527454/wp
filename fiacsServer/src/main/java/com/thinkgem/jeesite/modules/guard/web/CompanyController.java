/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.Company;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.service.CarService;
import com.thinkgem.jeesite.modules.guard.service.CompanyService;
import com.thinkgem.jeesite.modules.guard.service.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 第三方公司Controller
 * 
 * @author Jumbo
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/company")
public class CompanyController extends BaseController {
	private String status = "";
	@Autowired
	private CompanyService companyService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private CarService carService;

	@Value("${projectPath.images}")
	private String projectPath;

	@ModelAttribute
	public Company get(@RequestParam(required = false) String id) {
		Company entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = companyService.get(id);
		}
		if (entity == null) {
			entity = new Company();
		}
		return entity;
	}

	@RequiresPermissions("guard:company:view")
	@RequestMapping(value = { "index" })
	public String index(Company company, Model model) {
		return "modules/guard/companyIndex";
	}

	@RequiresPermissions("guard:company:view")
	@RequestMapping(value = { "list", "" })
	public String list(Company company, HttpServletRequest request, HttpServletResponse response, Model model) {

		model.addAttribute("list", companyService.findList(company));
		model.addAttribute("status", status);
		status = "";
		return "modules/guard/companyList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param company
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:company:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Company company, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			company.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, Company.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(company, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<Company> page = companyService.findPage(new Page<Company>(request, response), company);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("companyType")) {
				String s = DictUtils.getDictLabel(e.getString("companyType"), "company_type", "");
				e.put("companyType", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:company:view")
	@RequestMapping(value = "form")
	public String form(Company company, Model model) {
		if (company.getParent() != null) {
			company.setParent(companyService.get(company.getParent().getId()));
		}
		if (company.getArea() == null) {
			if (company.getParent() != null) {
				if (company.getParent().getArea() != null) {
					company.setArea(company.getParent().getArea());
				} else {
					if (areaService.findList(new Area()).size()>0) {
						company.setArea(areaService.findList(new Area()).get(0));
					}
				}
			} else {
				if (areaService.findList(new Area()).size()>0) {
					company.setArea(areaService.findList(new Area()).get(0));
				}
			}
		}

		return backForm(company, model);
	}

	private String backForm(Company company, Model model) {
		model.addAttribute("company", company);
		return "modules/guard/companyForm";
	}

	@RequiresPermissions("guard:company:edit")
	@RequestMapping(value = "save")
	public String save(Company company, BindingResult result, Model model, RedirectAttributes redirectAttributes) throws IOException {
		if (!beanValidator(model, company)) {
			return form(company, model);
		}
		
		if(companyService.countByShortName(company.getId(), company.getShortName()) > 0) {
			result.rejectValue("shortName", "duplicate", "简称已经存在");
		}
		
		if(companyService.countByFullName(company.getId(), company.getFullName()) > 0) {
			result.rejectValue("fullName", "duplicate", "全称已经存在");
		}
		
		if(companyService.countByCompanyCode(company.getId(), company.getCompanyCode()) > 0) {
			result.rejectValue("companyCode", "duplicate", "公司编码已经存在");
		}
		
		if(result.hasErrors()) {
			return backForm(company, model);
		}

		if (company.getCompanyExList() != null && company.getCompanyExList().size() > 0) {
			for (int i = 0; i < company.getCompanyExList().size(); i++) {
				if (company.getCompanyExList().get(i).getFile() != null) {
					// 车辆照片处理
					String slack = File.separator;
					String rootPath = getJarPath()+projectPath;
					String uuid = IdGen.uuid();
					File root = new File(rootPath);
					root.mkdirs();
					String coverPath = slack + "company" + slack + uuid + slack
							+ company.getCompanyExList().get(i).getFile().getOriginalFilename();
					String absPath = rootPath + coverPath;

					if (company.getCompanyExList().get(i).getFile().getSize() > 0) {
						// 复制文件
						File newFile = new File(absPath);
						newFile.mkdirs();
						newFile.delete();
						
						byte[] companyByte =company.getCompanyExList().get(i).getFile().getBytes();
						
						company.getCompanyExList().get(i).getFile().transferTo(newFile);

						coverPath = "/images/company/" + uuid + "/"
								+ company.getCompanyExList().get(i).getFile().getOriginalFilename();
						
						company.getCompanyExList().get(i).setImgData(companyByte);
						company.getCompanyExList().get(i).setImagePath(coverPath);
					}
				}
			}
		}

		companyService.save(company);
		addMessage(redirectAttributes, "保存第三方公司成功");
		status = "company";
		return "redirect:" + Global.getAdminPath() + "/guard/company/?repage";
	}

	@RequiresPermissions("guard:company:edit")
	@RequestMapping(value = "delete")
	public String delete(Company company, RedirectAttributes redirectAttributes) {

		List<Company> companyList = companyService.findParentIdsList(company);
		Staff staffReq = new Staff();
		staffReq.setCompany(company);
		List<Staff> staffList = staffService.findList(staffReq);
		
		for (Staff staff : staffList) {
			if(Staff.STAFF_TYPE_SUPERCARGO.equals(staff.getStaffType()) || Staff.STAFF_TYPE_MAINTENANCE_CLERK.equals(staff.getStaffType())){
				addMessage(redirectAttributes, "不可删除存在人员的公司");
				return "redirect:" + Global.getAdminPath() + "/guard/company/?repage";
			}
		}

		Car carRequest = new Car();
		carRequest.setCompany(company);
		List<Car> carList = carService.findList(carRequest);
		if (!carList.isEmpty()) {
			addMessage(redirectAttributes, "不可删除存在车辆的公司");
			return "redirect:" + Global.getAdminPath() + "/guard/company/?repage";
		}

		companyService.delete(company);
		addMessage(redirectAttributes, "删除第三方公司成功");
		status = "company";
		return "redirect:" + Global.getAdminPath() + "/guard/company/?repage";
	}

	/**
	 * @param extId
	 *            排除的ID
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String type,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Company> list = companyService.findList(new Company());
		Map<String, Object> maps = Maps.newHashMap();
		if (list.size() > 0) {
			maps.put("id", "0");
			maps.put("pId", "0");
			maps.put("pIds", "0,");
			maps.put("status", "company");
			maps.put("name", "所有公司");
			mapList.add(maps);
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			Company e = list.get(i);
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("pIds", e.getParentIds());
			map.put("status", "company");
			map.put("name", e.getShortName());
			mapList.add(map);
		}
		return mapList;
	}

	
	@RequestMapping(value = "getArea")
	@ResponseBody
	public String getArea(Company company, Model model, RedirectAttributes redirectAttributes) {
		Company com = companyService.get(company.getId());
		JSONObject obj = new JSONObject();
		if (!"".equals(com) || com != null) {
			obj.put("areaId", com.getArea().getId());
		}
		return obj.toString();
	}
	
	private String getJarPath() {
		ApplicationHome h = new ApplicationHome(getClass());
		File jarF = h.getSource();
		return jarF.getParentFile().toString();
	}
}