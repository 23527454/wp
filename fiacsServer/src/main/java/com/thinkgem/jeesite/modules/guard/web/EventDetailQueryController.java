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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerQuery;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.EventDetailQuery;
import com.thinkgem.jeesite.modules.guard.service.EventDetailQueryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 人员交接明细Controller
 * @author Jumbo
 * @version 2017-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/eventDetailQuery")
public class EventDetailQueryController extends BaseController {

	@Autowired
	private EventDetailQueryService eventDetailQueryService;
	
	@ModelAttribute
	public EventDetailQuery get(@RequestParam(required=false) String id) {
		EventDetailQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eventDetailQueryService.get(id);
		}
		if (entity == null){
			entity = new EventDetailQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:eventDetailQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(EventDetailQuery eventDetailQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EventDetailQuery> p = new Page<EventDetailQuery>(request, response);
		if (eventDetailQuery != null) {
			if ("1".equals(eventDetailQuery.getSort()) || eventDetailQuery.getSort() == "1") {
				String type = eventDetailQuery.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a2.name asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("c1.task_type asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("c2.name asc");
				} else if ("6".equals(type)) {
					p.setOrderBy("a.task_id asc");
				}else if ("7".equals(type)) {
					p.setOrderBy("o2.id asc");
				}else if ("8".equals(type)) {
					p.setOrderBy("a2.name asc");
				}else if ("9".equals(type)) {
					p.setOrderBy("a2.name asc");
				}
			}
			if (eventDetailQuery.getSortType() != null) {
				if ("-1".equals(eventDetailQuery.getSort()) || eventDetailQuery.getSort() == "-1"
						|| eventDetailQuery.getSort() == null) {
					String type = eventDetailQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("a2.name desc");
					} else if ("4".equals(type)) {
						p.setOrderBy("c1.task_type desc");
					} else if ("5".equals(type)) {
						p.setOrderBy("c2.name desc");
					} else if ("6".equals(type)) {
						p.setOrderBy("a.task_id desc");
					}else if ("7".equals(type)) {
						p.setOrderBy("o2.id desc");
					}else if ("8".equals(type)) {
						p.setOrderBy("a2.name desc");
					}else if ("9".equals(type)) {
						p.setOrderBy("a2.name desc");
					}
				}
			}
		}
		
		Page<EventDetailQuery> page = eventDetailQueryService.findPage(p, eventDetailQuery); 
		model.addAttribute("page", page);
		return "modules/guard/eventDetailQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param eventDetailQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:eventDetailQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(EventDetailQuery eventDetailQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			eventDetailQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, EventDetailQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(eventDetailQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<EventDetailQuery> page = eventDetailQueryService.findPage(new Page<EventDetailQuery>(request, response), eventDetailQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:eventDetailQuery:view")
	@RequestMapping(value = "form")
	public String form(EventDetailQuery eventDetailQuery, Model model) {
		model.addAttribute("eventDetailQuery", eventDetailQuery);
		return "modules/guard/eventDetailQueryForm";
	}

	@RequiresPermissions("guard:eventDetailQuery:edit")
	@RequestMapping(value = "save")
	public String save(EventDetailQuery eventDetailQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, eventDetailQuery)){
			return form(eventDetailQuery, model);
		}
		eventDetailQueryService.save(eventDetailQuery);
		addMessage(redirectAttributes, "保存人员交接明细成功");
		return "redirect:"+Global.getAdminPath()+"/guard/eventDetailQuery/?repage";
	}
	
	@RequiresPermissions("guard:eventDetailQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(EventDetailQuery eventDetailQuery, RedirectAttributes redirectAttributes) {
		eventDetailQueryService.delete(eventDetailQuery);
		addMessage(redirectAttributes, "删除人员交接明细成功");
		return "redirect:"+Global.getAdminPath()+"/guard/eventDetailQuery/?repage";
	}


	/**
	 * 导出设备数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:eventDetailQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(EventDetailQuery eventDetailQuery, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "人员交接明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<EventDetailQuery> page = eventDetailQueryService.findPage(new Page<EventDetailQuery>(request, response, -1), eventDetailQuery);
			new ExportExcel("人员交接明细", EventDetailQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出人员交接明细失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/eventDetailQuery/list?repage";
	}

	
}