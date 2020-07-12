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
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEventQuery;
import com.thinkgem.jeesite.modules.guard.service.DoorInOutEventQueryService;
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
@RequestMapping(value = "${adminPath}/guard/doorInOutEventQuery")
public class DoorInOutEventQueryController extends BaseController {

	@Autowired
	private DoorInOutEventQueryService doorInOutEventQueryService;
	
	@ModelAttribute
	public DoorInOutEventQuery get(@RequestParam(required=false) String id) {
		DoorInOutEventQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = doorInOutEventQueryService.get(id);
		}
		if (entity == null){
			entity = new DoorInOutEventQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:doorInOutEventQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(DoorInOutEventQuery doorInOutEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<DoorInOutEventQuery> p = new Page<DoorInOutEventQuery>(request, response);
		if (doorInOutEventQuery != null) {
			if ("1".equals(doorInOutEventQuery.getSort()) || doorInOutEventQuery.getSort() == "1") {
				
				String type = doorInOutEventQuery.getSortType();
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

			if (doorInOutEventQuery.getSortType() != null) {
				if ("-1".equals(doorInOutEventQuery.getSort()) || doorInOutEventQuery.getSort() == "-1"
						|| doorInOutEventQuery.getSort() == null) {
					
					String type = doorInOutEventQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} 
				}
			}
		}
		Page<DoorInOutEventQuery> page = doorInOutEventQueryService.findPage(p, doorInOutEventQuery); 
		model.addAttribute("page", page);
		return "modules/guard/doorInOutEventQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:doorInOutEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DoorInOutEventQuery doorInOutEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			doorInOutEventQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DoorInOutEventQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(doorInOutEventQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DoorInOutEventQuery> page = doorInOutEventQueryService.findPage(new Page<DoorInOutEventQuery>(request, response), doorInOutEventQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:doorInOutEventQuery:view")
	@RequestMapping(value = "form")
	public String form(DoorInOutEventQuery doorInOutEventQuery, Model model) {
		model.addAttribute("doorInOutEventQuery", doorInOutEventQuery);
		return "modules/guard/doorInOutEventQueryForm";
	}

	@RequiresPermissions("guard:DoorInOutEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(DoorInOutEventQuery DoorInOutEventQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, DoorInOutEventQuery)){
			return form(DoorInOutEventQuery, model);
		}
		doorInOutEventQueryService.save(DoorInOutEventQuery);
		addMessage(redirectAttributes, "保存车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/DoorInOutEventQuery/?repage";
	}
	
	@RequiresPermissions("guard:DoorInOutEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(DoorInOutEventQuery DoorInOutEventQuery, RedirectAttributes redirectAttributes) {
		doorInOutEventQueryService.delete(DoorInOutEventQuery);
		addMessage(redirectAttributes, "删除车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/DoorInOutEventQuery/?repage";
	}
	/**
	 * 导出设备数据
	 * 
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:doorInOutEventQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(DoorInOutEventQuery doorInOutEventQuery, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "门禁警报事件数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<DoorInOutEventQuery> page = doorInOutEventQueryService.findPage(new Page<DoorInOutEventQuery>(request, response, -1), doorInOutEventQuery);
			new ExportExcel("门禁警报事件数据", DoorInOutEventQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门禁警报事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/doorInOutEventQuery/list?repage";
	}

	
}