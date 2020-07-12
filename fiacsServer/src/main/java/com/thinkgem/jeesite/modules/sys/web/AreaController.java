/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.tools.internal.ws.wsdl.document.Binding;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Company;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.service.CompanyService;
import com.thinkgem.jeesite.modules.guard.service.LineService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * 
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private LineService lineService;
	@ModelAttribute("area")
	public Area get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return areaService.get(id);
		} else {
			return new Area();
		}
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = { "list", "" })
	public String list(Area area, Model model) {
		model.addAttribute("list", areaService.findList());
		return "modules/sys/areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent() == null || area.getParent().getId() == null) {
			area.setParent(UserUtils.getUser().getOffice().getArea());
		}
		if (area.getParent() != null) {
			area.setParent(areaService.get(area.getParent().getId()));
		}
		// // 自动获取排序号
		// if (StringUtils.isBlank(area.getId())){
		// int size = 0;
		// List<Area> list = areaService.findAll();
		// for (int i=0; i<list.size(); i++){
		// Area e = list.get(i);
		// if (e.getParent()!=null && e.getParent().getId()!=null
		// && e.getParent().getId().equals(area.getParent().getId())){
		// size++;
		// }
		// }
		// area.setCode(area.getParent().getCode() +
		// StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
		// }
		return backForm(area, model);
	}

	private String backForm(Area area, Model model) {
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		if (!beanValidator(model, area)) {
			return form(area, model);
		}
		
		if(areaService.countByName(area.getId(), area.getName()) > 0) {
			result.rejectValue("name", "duplicate", "区域名称已经存在");
		}
		
		if(areaService.countByCode(area.getId(), area.getCode())  > 0 ) {
			result.rejectValue("code", "duplicate", "区域编码已经存在");
		}
		
		if(result.hasErrors()) {
			return backForm(area, model);
		}
		
		if(area.getIsNewRecord() && area.getParent() == null || area.getParent().getId()==null){
			addMessage(redirectAttributes, "不能保存根区域");
			return "redirect:" + adminPath + "/sys/area/";
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(Area area, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		// if (Area.isRoot(id)){
		// addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
		// }else{
		List<Office> officeList = officeService.findList(new Office());
		List<Area> areaList = areaService.findParentIdsList(area);
		List<Company> companyList = companyService.findList(new Company());
		List<Line> lineList = lineService.findList(new Line());
		for (int i = 0; i < officeList.size(); i++) {
			for (int n = 0; n < areaList.size(); n++) {
				if (officeList.get(i).getArea() != null && areaList.get(n) != null
						&& officeList.get(i).getArea().getId().equals(areaList.get(n).getId())) {
					addMessage(redirectAttributes, "不可删除存在网点的区域");
					return "redirect:" + adminPath + "/sys/area/";
				}
			}
		}
		for (int i = 0; i < companyList.size(); i++) {
			for (int n = 0; n < areaList.size(); n++) {
				if (companyList.get(i).getArea() != null && areaList.get(n) != null
						&& companyList.get(i).getArea().getId().equals(areaList.get(n).getId())) {
					addMessage(redirectAttributes, "不可删除存在公司的区域");
					return "redirect:" + adminPath + "/sys/area/";
				}
			}
		}
		for (int i = 0; i < lineList.size(); i++) {
			for (int n = 0; n < areaList.size(); n++) {
				if (lineList.get(i).getArea() != null && areaList.get(n) != null
						&& lineList.get(i).getArea().getId().equals(areaList.get(n).getId())) {
					addMessage(redirectAttributes, "不可删除存在线路的区域");
					return "redirect:" + adminPath + "/sys/area/";
				}
			}
		}
		areaService.delete(area);
		addMessage(redirectAttributes, "删除区域成功");
		// }
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findList();
		Map<String, Object> maps = Maps.newHashMap();
//		if (list.size() > 0) {
//			maps.put("id", "0");
//			maps.put("pId", "0");
//			maps.put("pIds", "0,1,");
//			maps.put("name", "所有区域");
//			mapList.add(maps);
//		}
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
