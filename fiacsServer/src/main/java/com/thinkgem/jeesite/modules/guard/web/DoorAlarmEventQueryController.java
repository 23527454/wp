/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEventQuery;
import com.thinkgem.jeesite.modules.guard.service.DoorAlarmEventQueryService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 车辆事件查询Controller
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/doorAlarmEventQuery")
public class DoorAlarmEventQueryController extends BaseController {

	@Autowired
	private DoorAlarmEventQueryService doorAlarmEventQueryService;
	
	@ModelAttribute
	public DoorAlarmEventQuery get(@RequestParam(required=false) String id) {
		DoorAlarmEventQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = doorAlarmEventQueryService.get(id);
		}
		if (entity == null){
			entity = new DoorAlarmEventQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:doorAlarmEventQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(DoorAlarmEventQuery doorAlarmEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<DoorAlarmEventQuery> p = new Page<DoorAlarmEventQuery>(request, response);
		if (doorAlarmEventQuery != null) {
			if ("1".equals(doorAlarmEventQuery.getSort()) || doorAlarmEventQuery.getSort() == "1") {
				
				String type = doorAlarmEventQuery.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a9.card_num asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("a9.carplate asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("a9.admin asc");
				} 
			}

			if (doorAlarmEventQuery.getSortType() != null) {
				if ("-1".equals(doorAlarmEventQuery.getSort()) || doorAlarmEventQuery.getSort() == "-1"
						|| doorAlarmEventQuery.getSort() == null) {
					
					String type = doorAlarmEventQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} 
				}
			}
		}
		Page<DoorAlarmEventQuery> page = doorAlarmEventQueryService.findPage(p, doorAlarmEventQuery); 
		model.addAttribute("page", page);
		return "modules/guard/doorAlarmEventQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param DoorAlarmEventQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:doorAlarmEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DoorAlarmEventQuery doorAlarmEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			doorAlarmEventQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DoorAlarmEventQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(doorAlarmEventQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DoorAlarmEventQuery> page = doorAlarmEventQueryService.findPage(new Page<DoorAlarmEventQuery>(request, response), doorAlarmEventQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:doorAlarmEventQuery:view")
	@RequestMapping(value = "form")
	public String form(DoorAlarmEventQuery doorAlarmEventQuery, Model model) {
		model.addAttribute("doorAlarmEventQuery", doorAlarmEventQuery);
		return "modules/guard/doorAlarmEventQueryForm";
	}

	@RequiresPermissions("guard:DoorAlarmEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(DoorAlarmEventQuery DoorAlarmEventQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, DoorAlarmEventQuery)){
			return form(DoorAlarmEventQuery, model);
		}
		doorAlarmEventQueryService.save(DoorAlarmEventQuery);
		addMessage(redirectAttributes, "保存车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/DoorAlarmEventQuery/?repage";
	}
	
	@RequiresPermissions("guard:DoorAlarmEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(DoorAlarmEventQuery DoorAlarmEventQuery, RedirectAttributes redirectAttributes) {
		doorAlarmEventQueryService.delete(DoorAlarmEventQuery);
		addMessage(redirectAttributes, "删除车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/DoorAlarmEventQuery/?repage";
	}
	/**
	 * 导出设备数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:doorAlarmEventQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(DoorAlarmEventQuery doorAlarmEventQuery, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "门禁警报事件数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<DoorAlarmEventQuery> page = doorAlarmEventQueryService.findPage(new Page<DoorAlarmEventQuery>(request, response, -1), doorAlarmEventQuery);
			new ExportExcel("门禁警报事件数据", DoorAlarmEventQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门禁警报事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/doorAlarmEventQuery/list?repage";
	}

	
}