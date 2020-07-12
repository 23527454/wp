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
import com.thinkgem.jeesite.modules.guard.entity.Tbmoneylockers;
import com.thinkgem.jeesite.modules.guard.service.TbmoneylockersService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 款箱管理表单Controller
 * @author Jumbo
 * @version 2017-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/tbmoneylockers")
public class TbmoneylockersController extends BaseController {

	@Autowired
	private TbmoneylockersService tbmoneylockersService;
	
	@ModelAttribute
	public Tbmoneylockers get(@RequestParam(required=false) String id) {
		Tbmoneylockers entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tbmoneylockersService.get(id);
		}
		if (entity == null){
			entity = new Tbmoneylockers();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:tbmoneylockers:view")
	@RequestMapping(value = {"list", ""})
	public String list(Tbmoneylockers tbmoneylockers, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tbmoneylockers> page = tbmoneylockersService.findPage(new Page<Tbmoneylockers>(request, response), tbmoneylockers); 
		model.addAttribute("page", page);
		return "modules/guard/tbmoneylockersList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param tbmoneylockers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:tbmoneylockers:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Tbmoneylockers tbmoneylockers, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			tbmoneylockers.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, Tbmoneylockers.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(tbmoneylockers, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<Tbmoneylockers> page = tbmoneylockersService.findPage(new Page<Tbmoneylockers>(request, response), tbmoneylockers); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
	        		if(e.has("uclockertype")){
	        			String s = DictUtils.getDictLabel(e.getString("uclockertype"),"ulocker_type", "");
	        			e.put("uclockertype",s);
					}
	        		if(e.has("uclockerstatus")){
	        			String s = DictUtils.getDictLabel(e.getString("uclockerstatus"),"clocker_status", "");
	        			e.put("uclockerstatus",s);
					}
	        		if(e.has("delFlag")){
	        			String s = DictUtils.getDictLabel(e.getString("delFlag"),"del_flag", "");
	        			e.put("delFlag",s);
					}
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:tbmoneylockers:view")
	@RequestMapping(value = "form")
	public String form(Tbmoneylockers tbmoneylockers, Model model) {
		model.addAttribute("tbmoneylockers", tbmoneylockers);
		return "modules/guard/tbmoneylockersForm";
	}

	@RequiresPermissions("guard:tbmoneylockers:edit")
	@RequestMapping(value = "save")
	public String save(Tbmoneylockers tbmoneylockers, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tbmoneylockers)){
			return form(tbmoneylockers, model);
		}
		tbmoneylockersService.save(tbmoneylockers);
		addMessage(redirectAttributes, "保存款箱成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbmoneylockers/?repage";
	}
	
	@RequiresPermissions("guard:tbmoneylockers:edit")
	@RequestMapping(value = "delete")
	public String delete(Tbmoneylockers tbmoneylockers, RedirectAttributes redirectAttributes) {
		tbmoneylockersService.delete(tbmoneylockers);
		addMessage(redirectAttributes, "删除款箱成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbmoneylockers/?repage";
	}

}