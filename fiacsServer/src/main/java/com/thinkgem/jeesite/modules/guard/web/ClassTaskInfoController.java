/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.TaskScheduleInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfoSearchRequest;
import com.thinkgem.jeesite.modules.guard.service.ClassTaskInfoService;
import com.thinkgem.jeesite.modules.guard.service.SysConfigService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 班组信息Controller
 * 
 * @author Jumbo
 * @version 2017-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/classTaskInfo")
public class ClassTaskInfoController extends BaseController {
	@Autowired
	private ClassTaskInfoService classTaskInfoService;
	@Autowired
	private TaskScheduleInfoDao taskScheduleInfoDao;
	
	@Autowired
	private SysConfigService sysConfigService;

	@ModelAttribute
	public ClassTaskInfo get(@RequestParam(required = false) String id) {
		ClassTaskInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = classTaskInfoService.get(id);
		}
		if (entity == null) {
			entity = new ClassTaskInfo();
		}
		return entity;
	}

	@RequiresPermissions("guard:classTaskInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(ClassTaskInfo classTaskInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ClassTaskInfo> page = classTaskInfoService.findPage(new Page<ClassTaskInfo>(request, response),
				classTaskInfo);
		model.addAttribute("page", page);
		return "modules/guard/classTaskInfoList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param classTaskInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:classTaskInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(ClassTaskInfo classTaskInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			classTaskInfo.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList<Field>();
			Reflections.getAllFields(fields, ClassTaskInfo.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(classTaskInfo, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<ClassTaskInfo> page = classTaskInfoService.findPage(new Page<ClassTaskInfo>(request, response),
				classTaskInfo);
//		List<ClassTaskInfo> classTaskInfoList = page.getList();
//		List<TaskScheduleInfo> taskInfoList = taskScheduleInfoDao.findList(new TaskScheduleInfoSearchRequest());
//		List<ClassTaskInfo> classTaskInfoL = new ArrayList<ClassTaskInfo>();
//		for (int i = 0; i < classTaskInfoList.size(); i++) {
//			if (taskListPD(classTaskInfoList, taskInfoList, i) || 
//					classTaskInfo.getId() != null) {
//				classTaskInfoL.add(classTaskInfoList.get(i));
//			}
//		}
//		page.setList(classTaskInfoL);

		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
	
		if(jo.get("list") != null){
			JSONArray ja = jo.getJSONArray("list");
			for (int i = 0; ja != null && i < ja.size(); i++) {
				JSONObject e = (JSONObject) ja.get(i);
				if (e.has("verifyCar")) {
					String s = DictUtils.getDictLabel(e.getString("verifyCar"), "verify_car", "");
					e.put("verifyCar", s);
				}
				if (e.has("verifyInterMan")) {
					String s = DictUtils.getDictLabel(e.getString("verifyInterMan"), "inter_man", "");
					e.put("verifyInterMan", s);
				}
				if (e.has("verifyLocker")) {
					String s = DictUtils.getDictLabel(e.getString("verifyLocker"), "verify_locker", "");
					e.put("verifyLocker", s);
				}
				if (e.has("delFlag")) {
					String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
					e.put("delFlag", s);
				}
			}
		}
		return jo.toString();
	}

	private boolean taskListPD(List<ClassTaskInfo> classList, List<TaskScheduleInfo> taskInfoList, int i) {
		for (int n = 0; n < taskInfoList.size(); n++) {
			if (classList.get(i).getId().equals(taskInfoList.get(n).getClassTaskId())) {
				return false;
			}
		}
		return true;
	}

	@RequiresPermissions("guard:classTaskInfo:view")
	@RequestMapping(value = "form")
	public String form(ClassTaskInfo classTaskInfo, Model model) {

		String classID = classTaskInfo.getId();
		if (!"".equals(classID) && classID != null) {
			TaskScheduleInfo taskScheduleInfo = new TaskScheduleInfo();
			taskScheduleInfo.setClassTaskId(classID);
			List<TaskScheduleInfo> taskList = taskScheduleInfoDao.findList(new TaskScheduleInfoSearchRequest());
			if (taskList.size() > 0) {
				classTaskInfo.setTasktype("true");
			}
			classTaskInfo.setInterManNum(sysConfigService.getInterNum());
			classTaskInfo.setPatrolManNum(sysConfigService.getSuperGoNum());
		}else{
			if(null == classTaskInfo.getInterManNum()){
				classTaskInfo.setInterManNum(sysConfigService.getInterNum());
			}
			if(null == classTaskInfo.getPatrolManNum()){
				classTaskInfo.setPatrolManNum(sysConfigService.getSuperGoNum());
			}
		}
		return backForm(classTaskInfo, model);
	}
	
	@RequiresPermissions("guard:classTaskInfo:view")
	@RequestMapping(value = "ajaxGetClassTaskInfo")
	@ResponseBody
	public ClassTaskInfo ajaxGetClassTaskInfo(ClassTaskInfo classTaskInfo, Model model) {
		return classTaskInfo;
	}

	private String backForm(ClassTaskInfo classTaskInfo, Model model) {
		model.addAttribute("classTaskInfo", classTaskInfo);
		return "modules/guard/classTaskInfoForm";
	}

	@RequiresPermissions("guard:classTaskInfo:edit")
	@RequestMapping(value = "save")
	public String save(ClassTaskInfo classTaskInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, classTaskInfo)) {
			return form(classTaskInfo, model);
		}
		
		if(classTaskInfo.getClassPersonInfoList().size() < sysConfigService.getSuperGoNumInt()){
			result.rejectValue("patrolManNum", "number", "押款员数量必须大于"+sysConfigService.getSuperGoNumInt() +"!");
		}
		
		if(classTaskInfoService.countByName(classTaskInfo.getId(), classTaskInfo.getName()) > 0) {
			result.rejectValue("name", "duplicate", "班组名称已经存在!");
		}
		
		if(!classTaskInfo.getInterManNum().equals(sysConfigService.getInterNumInt()+"")) {
			result.rejectValue("InterManNum", "InterManNum", "最少专员数量已更新为"+sysConfigService.getInterNumInt());
		}
		if(!classTaskInfo.getPatrolManNum().equals(sysConfigService.getSuperGoNumInt()+"")) {
			result.rejectValue("PatrolManNum", "PatrolManNum", "最少押款员数量已更新为"+sysConfigService.getSuperGoNumInt());
		}
		
		
		
		if(result.hasErrors()) {
			return backForm(classTaskInfo, model);
		}
		
		try {
			classTaskInfoService.save(classTaskInfo);
		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存班组信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/classTaskInfo/?repage";
	}

	@RequiresPermissions("guard:classTaskInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ClassTaskInfo classTaskInfo, RedirectAttributes redirectAttributes) {
		if (deletePD(classTaskInfo)) {
			classTaskInfoService.delete(classTaskInfo);
			addMessage(redirectAttributes, "删除班组信息成功");
		} else {
			addMessage(redirectAttributes, "不能删除在任务中的班组");
		}
		return "redirect:" + Global.getAdminPath() + "/guard/classTaskInfo/?repage";

	}

	private boolean deletePD(ClassTaskInfo classTaskInfo) {
		List<TaskScheduleInfo> classTaskInfoList = taskScheduleInfoDao.findList(new TaskScheduleInfoSearchRequest());
		for (int i = 0; i < classTaskInfoList.size(); i++) {
			if (classTaskInfoList.get(i).getClassTaskId().equals(classTaskInfo.getId())) {
				return false;
			}
		}
		return true;
	}
	

	/**
	 * 导出设备数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:taskScheduleInfo:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(ClassTaskInfo classTaskInfo, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "班组数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<ClassTaskInfo> page = classTaskInfoService
					.findPage(new Page<ClassTaskInfo>(request, response, -1), classTaskInfo);
			new ExportExcel("班组数据", ClassTaskInfo.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出班组失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/taskScheduleInfo/list?repage";
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
		List<ClassTaskInfo> list = classTaskInfoService.findList(isAll);
		Map<String, Object> maps = Maps.newHashMap();
		if (list.size() > 0) {
			maps.put("id", "0");
			maps.put("pId", "0");
			maps.put("pIds", "0,1,");
			maps.put("name", "所有班组");
			mapList.add(maps);
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			ClassTaskInfo e = list.get(i);
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("pIds", "0,1,");
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}

}