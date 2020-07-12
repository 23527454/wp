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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.entity.LineEventQuery;
import com.thinkgem.jeesite.modules.guard.service.LineEventQueryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 排班明细Controller
 * @author Jumbo
 * @version 2017-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/lineEventQuery")
public class LineEventQueryController extends BaseController {

	@Autowired
	private LineEventQueryService lineEventQueryService;
	
	@ModelAttribute
	public LineEventQuery get(@RequestParam(required=false) String id) {
		LineEventQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = lineEventQueryService.get(id);
		}
		if (entity == null){
			entity = new LineEventQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:lineEventQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(LineEventQuery lineEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LineEventQuery> page = lineEventQueryService.findPage(new Page<LineEventQuery>(request, response), lineEventQuery); 
		model.addAttribute("page", page);
		return "modules/guard/lineEventQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param lineEventQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:lineEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(LineEventQuery lineEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			lineEventQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, LineEventQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(lineEventQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<LineEventQuery> page = lineEventQueryService.findPage(new Page<LineEventQuery>(request, response), lineEventQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:lineEventQuery:view")
	@RequestMapping(value = "form")
	public String form(LineEventQuery lineEventQuery, Model model) {
		model.addAttribute("lineEventQuery", lineEventQuery);
		return "modules/guard/lineEventQueryForm";
	}

	@RequiresPermissions("guard:lineEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(LineEventQuery lineEventQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lineEventQuery)){
			return form(lineEventQuery, model);
		}
		lineEventQueryService.save(lineEventQuery);
		addMessage(redirectAttributes, "保存排班明细成功");
		return "redirect:"+Global.getAdminPath()+"/guard/lineEventQuery/?repage";
	}
	
	@RequiresPermissions("guard:lineEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(LineEventQuery lineEventQuery, RedirectAttributes redirectAttributes) {
		lineEventQueryService.delete(lineEventQuery);
		addMessage(redirectAttributes, "删除排班明细成功");
		return "redirect:"+Global.getAdminPath()+"/guard/lineEventQuery/?repage";
	}

}