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
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxReturn;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxReturnService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 款箱上缴Controller
 * @author Jumbo
 * @version 2017-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/moneyBoxReturn")
public class MoneyBoxReturnController extends BaseController {

	@Autowired
	private MoneyBoxReturnService moneyBoxReturnService;
	
	@ModelAttribute
	public MoneyBoxReturn get(@RequestParam(required=false) String id) {
		MoneyBoxReturn entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moneyBoxReturnService.get(id);
		}
		if (entity == null){
			entity = new MoneyBoxReturn();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:moneyBoxReturn:view")
	@RequestMapping(value = {"list", ""})
	public String list(MoneyBoxReturn moneyBoxReturnRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MoneyBoxReturn> page = moneyBoxReturnService.findPage(new Page<MoneyBoxReturn>(request, response), moneyBoxReturnRequest); 
		model.addAttribute("page", page);
		model.addAttribute("moneyBoxReturn", moneyBoxReturnRequest);
		return "modules/guard/moneyBoxReturnList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param moneyBoxReturn
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:moneyBoxReturn:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(MoneyBoxReturn moneyBoxReturn, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			moneyBoxReturn.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, MoneyBoxReturn.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(moneyBoxReturn, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<MoneyBoxReturn> page = moneyBoxReturnService.findPage(new Page<MoneyBoxReturn>(request, response), moneyBoxReturn); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:moneyBoxReturn:view")
	@RequestMapping(value = "form")
	public String form(MoneyBoxReturn moneyBoxReturn, Model model) {
		model.addAttribute("moneyBoxReturn", moneyBoxReturn);
		return "modules/guard/moneyBoxReturnForm";
	}

	@RequiresPermissions("guard:moneyBoxReturn:edit")
	@RequestMapping(value = "save")
	public String save(MoneyBoxReturn moneyBoxReturn, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, moneyBoxReturn)){
			return form(moneyBoxReturn, model);
		}
		moneyBoxReturnService.save(moneyBoxReturn);
		addMessage(redirectAttributes, "保存款箱上缴信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/moneyBoxReturn/?repage";
	}
	
	@RequiresPermissions("guard:moneyBoxReturn:edit")
	@RequestMapping(value = "delete")
	public String delete(MoneyBoxReturn moneyBoxReturn, RedirectAttributes redirectAttributes) {
		moneyBoxReturnService.delete(moneyBoxReturn);
		addMessage(redirectAttributes, "删除款箱上缴信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/moneyBoxReturn/?repage";
	}

}