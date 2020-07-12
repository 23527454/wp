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
import com.thinkgem.jeesite.modules.guard.entity.CommissionerQuery;
import com.thinkgem.jeesite.modules.guard.entity.ConnectPersonnel;
import com.thinkgem.jeesite.modules.guard.service.CommissionerQueryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 人员交接明细Controller
 * @author Jumbo
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/commissionerQuery")
public class CommissionerQueryController extends BaseController {

	@Autowired
	private CommissionerQueryService commissionerQueryService;
	
	@ModelAttribute
	public CommissionerQuery get(@RequestParam(required=false) String id) {
		CommissionerQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = commissionerQueryService.get(id);
		}
		if (entity == null){
			entity = new CommissionerQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:commissionerQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(CommissionerQuery commissionerQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CommissionerQuery> p = new Page<CommissionerQuery>(request, response);
		if (commissionerQuery != null) {
			if ("1".equals(commissionerQuery.getSort()) || commissionerQuery.getSort() == "1") {
				String type = commissionerQuery.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a2.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a3.name asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("a2.task_type asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("a3.name asc");
				} else if ("6".equals(type)) {
					p.setOrderBy("a.task_id asc");
				}else if ("7".equals(type)) {
					p.setOrderBy("a5.name asc");
				}
			}
			if (commissionerQuery.getSortType() != null) {
				if ("-1".equals(commissionerQuery.getSort()) || commissionerQuery.getSort() == "-1"
						|| commissionerQuery.getSort() == null) {
					String type = commissionerQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a2.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("a3.name desc");
					} else if ("4".equals(type)) {
						p.setOrderBy("a2.task_type desc");
					} else if ("5".equals(type)) {
						p.setOrderBy("a3.name desc");
					} else if ("6".equals(type)) {
						p.setOrderBy("a.task_id desc");
					}else if ("7".equals(type)) {
						p.setOrderBy("a5.name desc");
					}
				}
			}
		}
		Page<CommissionerQuery> page = commissionerQueryService.findPage(p, commissionerQuery); 
		model.addAttribute("page", page);
		return "modules/guard/commissionerQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param commissionerQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:commissionerQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(CommissionerQuery commissionerQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			commissionerQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, CommissionerQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(commissionerQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
		}
		
        Page<CommissionerQuery> page = commissionerQueryService.findPage(new Page<CommissionerQuery>(request, response), commissionerQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:commissionerQuery:view")
	@RequestMapping(value = "form")
	public String form(CommissionerQuery commissionerQuery, Model model) {
		model.addAttribute("commissionerQuery", commissionerQuery);
		return "modules/guard/commissionerQueryForm";
	}

	@RequiresPermissions("guard:commissionerQuery:edit")
	@RequestMapping(value = "save")
	public String save(CommissionerQuery commissionerQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, commissionerQuery)){
			return form(commissionerQuery, model);
		}
		commissionerQueryService.save(commissionerQuery);
		addMessage(redirectAttributes, "保存人员交接明细成功");
		return "redirect:"+Global.getAdminPath()+"/guard/commissionerQuery/?repage";
	}
	
	@RequiresPermissions("guard:commissionerQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(CommissionerQuery commissionerQuery, RedirectAttributes redirectAttributes) {
		commissionerQueryService.delete(commissionerQuery);
		addMessage(redirectAttributes, "删除人员交接明细成功");
		return "redirect:"+Global.getAdminPath()+"/guard/commissionerQuery/?repage";
	}

}