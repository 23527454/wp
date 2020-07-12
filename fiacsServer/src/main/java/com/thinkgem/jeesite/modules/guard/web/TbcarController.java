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
import com.thinkgem.jeesite.modules.guard.entity.Tbcar;
import com.thinkgem.jeesite.modules.guard.service.TbcarService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 车辆管理生成Controller
 * @author Jumbo
 * @version 2017-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/tbcar")
public class TbcarController extends BaseController {

	@Autowired
	private TbcarService tbcarService;

	@ModelAttribute
	public Tbcar get(@RequestParam(required=false) String id) {
		Tbcar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tbcarService.get(id);
		}
		if (entity == null){
			entity = new Tbcar();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:tbcar:view")
	@RequestMapping(value = {"list", ""})
	public String list(Tbcar tbcar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tbcar> page = tbcarService.findPage(new Page<Tbcar>(request, response), tbcar); 
		model.addAttribute("page", page);
		return "modules/guard/tbcarList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param tbcar
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:tbcar:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Tbcar tbcar, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			tbcar.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, Tbcar.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(tbcar, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<Tbcar> page = tbcarService.findPage(new Page<Tbcar>(request, response), tbcar); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
	        		if(e.has("szcarmodel")){
	        			String s = DictUtils.getDictLabel(e.getString("szcarmodel"),"car_model", "");
	        			e.put("szcarmodel",s);
					}
	        		if(e.has("nworkstatus")){
	        			String s = DictUtils.getDictLabel(e.getString("nworkstatus"),"work_status", "");
	        			e.put("nworkstatus",s);
					}
	        		if(e.has("carbrand")){
	        			String s = DictUtils.getDictLabel(e.getString("carbrand"),"car_brand", "");
	        			e.put("carbrand",s);
					}
	        		if(e.has("delFlag")){
	        			String s = DictUtils.getDictLabel(e.getString("delFlag"),"del_flag", "");
	        			e.put("delFlag",s);
					}
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:tbcar:view")
	@RequestMapping(value = "form")
	public String form(Tbcar tbcar, Model model) {
		model.addAttribute("tbcar", tbcar);
		return "modules/guard/tbcarForm";
	}

	@RequiresPermissions("guard:tbcar:edit")
	@RequestMapping(value = "save")
	public String save(Tbcar tbcar, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tbcar)){
			return form(tbcar, model);
		}
		tbcarService.save(tbcar);
		addMessage(redirectAttributes, "保存车辆信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbcar/?repage";
	}
	
	@RequiresPermissions("guard:tbcar:edit")
	@RequestMapping(value = "delete")
	public String delete(Tbcar tbcar, RedirectAttributes redirectAttributes) {
		tbcarService.delete(tbcar);
		addMessage(redirectAttributes, "删除车辆信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbcar/?repage";
	}

}