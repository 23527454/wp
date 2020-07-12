/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

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
import com.thinkgem.jeesite.modules.guard.entity.InformationReleaseQuery;
import com.thinkgem.jeesite.modules.guard.service.DoorInOutEventQueryService;
import com.thinkgem.jeesite.modules.guard.service.InformationReleaseQueryService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 信息发布查询Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/informationReleaseQuery")
public class InformationReleaseQueryController extends BaseController {

	@Autowired
	private InformationReleaseQueryService informationReleaseQueryService;
	
	@ModelAttribute
	public InformationReleaseQuery get(@RequestParam(required=false) String id) {
		InformationReleaseQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = informationReleaseQueryService.get(id);
		}
		if (entity == null){
			entity = new InformationReleaseQuery();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(InformationReleaseQuery informationReleaseQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<InformationReleaseQuery> p = new Page<InformationReleaseQuery>(request, response);
		Page<InformationReleaseQuery> page = informationReleaseQueryService.findPage(p, informationReleaseQuery);
		model.addAttribute("page", page);
		return "modules/guard/informationReleaseQueryList";
	}

	/*@RequestMapping(value = "listData")
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
	}*/
	
	@RequestMapping(value = "delete")
	public String delete(InformationReleaseQuery informationReleaseQuery, RedirectAttributes redirectAttributes) {
		informationReleaseQueryService.delete(informationReleaseQuery);
		addMessage(redirectAttributes, "删除信息发布同步成功");
		return "redirect:"+Global.getAdminPath()+"/guard/informationReleaseQuery/?repage";
	}
	/**
	 * 导出设备数据
	 * 
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 *//*
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
*/
	
}