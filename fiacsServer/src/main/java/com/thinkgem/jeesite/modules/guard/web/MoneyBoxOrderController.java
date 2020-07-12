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
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxOrder;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxOrderService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 款箱预约Controller
 * @author Jumbo
 * @version 2017-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/moneyBoxOrder")
public class MoneyBoxOrderController extends BaseController {

	@Autowired
	private MoneyBoxOrderService moneyBoxOrderService;
	
	@ModelAttribute
	public MoneyBoxOrder get(@RequestParam(required=false) String id) {
		MoneyBoxOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moneyBoxOrderService.get(id);
		}
		if (entity == null){
			entity = new MoneyBoxOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:moneyBoxOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(MoneyBoxOrder moneyBoxOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MoneyBoxOrder> page = moneyBoxOrderService.findPage(new Page<MoneyBoxOrder>(request, response), moneyBoxOrder); 
		model.addAttribute("page", page);
		model.addAttribute("moneyBoxOrder", moneyBoxOrder);
		return "modules/guard/moneyBoxOrderList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param moneyBoxOrder
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:moneyBoxOrder:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(MoneyBoxOrder moneyBoxOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			moneyBoxOrder.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, MoneyBoxOrder.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(moneyBoxOrder, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<MoneyBoxOrder> page = moneyBoxOrderService.findPage(new Page<MoneyBoxOrder>(request, response), moneyBoxOrder); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:moneyBoxOrder:view")
	@RequestMapping(value = "form")
	public String form(MoneyBoxOrder moneyBoxOrder, Model model) {
		model.addAttribute("moneyBoxOrder", moneyBoxOrder);
		return "modules/guard/moneyBoxOrderForm";
	}

	@RequiresPermissions("guard:moneyBoxOrder:edit")
	@RequestMapping(value = "save")
	public String save(MoneyBoxOrder moneyBoxOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, moneyBoxOrder)){
			return form(moneyBoxOrder, model);
		}
		moneyBoxOrderService.save(moneyBoxOrder);
		addMessage(redirectAttributes, "保存款箱预约成功");
		return "redirect:"+Global.getAdminPath()+"/guard/moneyBoxOrder/?repage";
	}
	
	@RequiresPermissions("guard:moneyBoxOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(MoneyBoxOrder moneyBoxOrder, RedirectAttributes redirectAttributes) {
		moneyBoxOrderService.delete(moneyBoxOrder);
		addMessage(redirectAttributes, "取消款箱预约成功");
		return "redirect:"+Global.getAdminPath()+"/guard/moneyBoxOrder/?repage";
	}

}