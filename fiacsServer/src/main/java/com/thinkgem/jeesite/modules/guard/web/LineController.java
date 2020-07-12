/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.dao.ClassTaskInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.LineNodesDao;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.service.LineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 线路信息Controller
 * 
 * @author Jumbo
 * @version 2017-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/line")
public class LineController extends BaseController {

	@Autowired
	private LineService lineService;
	@Autowired
	private ClassTaskInfoDao classTaskInfoDao;
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private LineNodesDao lineNodesDao;
	@Autowired
	private AreaService areaService;

	@ModelAttribute
	public Line get(@RequestParam(required = false) String id) {
		Line entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = lineService.get(id);
		}
		if (entity == null) {
			entity = new Line();
		}
		return entity;
	}
	

	// 判断数据存在设备表中
	private boolean que(String s, List<Equipment> equipment) {
		for (int j = 0; j < equipment.size(); j++) {
			Equipment l = equipment.get(j);
			String n = l.getOffice().getId();
			if (n.equals(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取机构JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade
	 *            显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "officeTreeData")
	public List<Map<String, Object>> officeTreeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll,	
			@RequestParam(required = true) String areaId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Area area = areaService.get(areaId);
		
		Set<String> officeInLineNodeSet = lineNodesDao.findOfficeInLineNodeByArea(areaId);
		
		List<Office> list = officeService.findList(false, new Office());
		List<Equipment> equipment = officeService.equipmentList();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if(area.getParent().getId().equals("0")){
				if(officeInLineNodeSet.contains(e.getId())){//根节点
					continue;
				}
			}else{
				if(!areaId.equals(e.getArea().getId()) || officeInLineNodeSet.contains(e.getId())){//不是本区域，或者已经被增加的
					continue;
				}
			}
			
			String s = e.getId();
			if (que(s, equipment) == false) {
				if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
						&& e.getParentIds().indexOf("," + extId + ",") == -1))
						&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
						&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
						&& Global.YES.equals(e.getUseable())) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("pIds", e.getParentIds());
					map.put("status", "office");
					map.put("name", e.getName());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}


	@RequiresPermissions("guard:line:view")
	@RequestMapping(value = { "index" })
	public String index(Line line, Model model) {
		return "modules/guard/lineIndex";
	}

	@RequiresPermissions("guard:line:view")
	@RequestMapping(value = { "list", "" })
	public String list(Line line, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Line> page = lineService.findPage(new Page<Line>(request, response), line);
		model.addAttribute("page", page);
		model.addAttribute("line", line);
		return "modules/guard/lineList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param line
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:line:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Line line, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			line.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, Line.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(line, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<Line> page = lineService.findPage(new Page<Line>(request, response), line);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("lineOrder")) {
				String s = DictUtils.getDictLabel(e.getString("lineOrder"), "line_order", "");
				e.put("lineOrder", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:line:view")
	@RequestMapping(value = "form")
	public String form(Line line, 
			@RequestParam("selectedAreaId")String selectedAreaId,
			Model model) {
		if(line.getArea()!=null && "0".equals(line.getArea().getId())){
			line.getArea().setName("所有区域");
		}
		return backForm(line, selectedAreaId, model);
	}

	private String backForm(Line line, String selectedAreaId,Model model) {
		model.addAttribute("line", line);
		model.addAttribute("selectedAreaId", selectedAreaId);
		return "modules/guard/lineForm";
		
	}

	@RequiresPermissions("guard:line:edit")
	@RequestMapping(value = "save")
	public String save(Line line, BindingResult result, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedAreaId")String selectedAreaId) {
		if (!beanValidator(model, line)) {
			return form(line, selectedAreaId, model);
		}
		
		if(lineService.countByLineName(line.getId(), line.getLineName()) > 0) {
			result.rejectValue("lineName", "duplicate", "线路名称已经存在");
		}
		
		if(result.hasErrors()) {
			return backForm(line,selectedAreaId,  model);
		}
		try {
			lineService.save(line);
		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存线路信息成功");
		if(null != selectedAreaId && selectedAreaId.equals(line.getArea().getId())){
			return "redirect:" + Global.getAdminPath() + "/guard/line/list?area.id="+selectedAreaId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/line/?repage";
		}
	}

	@RequiresPermissions("guard:line:edit")
	@RequestMapping(value = "delete")
	public String delete(Line line, RedirectAttributes redirectAttributes,
			@RequestParam("selectedAreaId")String selectedAreaId) {
		if (deletePD(line)) {
			lineService.delete(line);
			addMessage(redirectAttributes, "删除线路信息成功");
		} else {
			addMessage(redirectAttributes, "不能删除在排班的线路");
		}
		if(!StringUtils.isBlank(selectedAreaId)){
			return "redirect:" + Global.getAdminPath() + "/guard/line/list?area.id="+selectedAreaId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/line/?repage";
		}
	}

	private boolean deletePD(Line line) {
		List<ClassTaskInfo> classTaskInfoList = classTaskInfoDao.findList(new ClassTaskInfo());
		for (int i = 0; i < classTaskInfoList.size(); i++) {
			if (classTaskInfoList.get(i).getLine().getId().equals(line.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取设备JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String type,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Line> list = lineService.findList(isAll);
		Map<String, Object> maps = Maps.newHashMap();
		if (list.size() > 0) {
			maps.put("id", "0");
			maps.put("pId", "0");
			maps.put("pIds", "0,,");
			maps.put("name", "所有线路");
			mapList.add(maps);
		}
		for (int i = 0; i < list.size(); i++) {
			Line e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("pIds", "0,,");
			map.put("name", e.getLineName());
			mapList.add(map);
			if(i==list.size()-1) {
				Map<String, Object> map2 = Maps.newHashMap();
				map2.put("id", "0");
				map2.put("pId", "0");
				map2.put("pIds", "0,,");
				map2.put("name", "中心金库");
				mapList.add(map2);
			}
		}
		
/*		for (int i = 0; i < list.size(); i++) {
			Line e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("pIds", "0,1,");
			map.put("name", e.getLineName());
			mapList.add(map);
		}
*/		return mapList;
	}
	

	
}