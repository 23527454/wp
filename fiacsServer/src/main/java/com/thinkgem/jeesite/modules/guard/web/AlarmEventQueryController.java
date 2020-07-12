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
import com.thinkgem.jeesite.modules.guard.entity.AlarmEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.AlarmEventQueryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 异常报警事件查询Controller
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/alarmEventQuery")
public class AlarmEventQueryController extends BaseController {

	@Autowired
	private AlarmEventQueryService alarmEventQueryService;

	@ModelAttribute
	public AlarmEventQuery get(@RequestParam(required = false) String id) {
		AlarmEventQuery entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = alarmEventQueryService.get(id);
		}
		if (entity == null) {
			entity = new AlarmEventQuery();
		}
		return entity;
	}

	@RequiresPermissions("guard:alarmEventQuery:view")
	@RequestMapping(value = { "list", "" })
	public String list(AlarmEventQuery alarmEventQuery, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Page<AlarmEventQuery> p = new Page<AlarmEventQuery>(request, response);
		if (alarmEventQuery != null) {
			if ("1".equals(alarmEventQuery.getSort()) || alarmEventQuery.getSort() == "1") {
				String type = alarmEventQuery.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a2.name asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("a2.id asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("a.event_type asc");
				} else if ("6".equals(type)) {
					p.setOrderBy("o2.id asc");
				}
			}
			if (alarmEventQuery.getSortType() != null) {
				if ("-1".equals(alarmEventQuery.getSort()) || alarmEventQuery.getSort() == "-1"
						|| alarmEventQuery.getSort() == null) {
					String type = alarmEventQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("a2.name desc");
					} else if ("4".equals(type)) {
						p.setOrderBy("a2.id desc");

					} else if ("5".equals(type)) {
						p.setOrderBy("a.event_type desc");

					} else if ("6".equals(type)) {
						p.setOrderBy("o2.id desc");
					}
				}
			}
		}

		Page<AlarmEventQuery> page = alarmEventQueryService.findPage(p, alarmEventQuery);
		model.addAttribute("page", page);
		return "modules/guard/alarmEventQueryList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param alarmEventQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:alarmEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(AlarmEventQuery alarmEventQuery, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			alarmEventQuery.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, AlarmEventQuery.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(alarmEventQuery, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<AlarmEventQuery> page = alarmEventQueryService.findPage(new Page<AlarmEventQuery>(request, response),
				alarmEventQuery);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:alarmEventQuery:view")
	@RequestMapping(value = "form")
	public String form(AlarmEventQuery alarmEventQuery, Model model) {
		model.addAttribute("alarmEventQuery", alarmEventQuery);
		return "modules/guard/alarmEventQueryForm";
	}

	@RequiresPermissions("guard:alarmEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(AlarmEventQuery alarmEventQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, alarmEventQuery)) {
			return form(alarmEventQuery, model);
		}
		alarmEventQueryService.save(alarmEventQuery);
		addMessage(redirectAttributes, "保存异常报警事件查询成功");
		return "redirect:" + Global.getAdminPath() + "/guard/alarmEventQuery/?repage";
	}

	@RequiresPermissions("guard:alarmEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(AlarmEventQuery alarmEventQuery, RedirectAttributes redirectAttributes) {
		alarmEventQueryService.delete(alarmEventQuery);
		addMessage(redirectAttributes, "删除异常报警事件查询成功");
		return "redirect:" + Global.getAdminPath() + "/guard/alarmEventQuery/?repage";
	}

	/**
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:alarmEventQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(AlarmEventQuery alarmEventQuery, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "报警事件数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<AlarmEventQuery> page = alarmEventQueryService.findPage(new Page<AlarmEventQuery>(request, response, -1), alarmEventQuery);
			new ExportExcel("报警事件数据", AlarmEventQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出报警事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/alarmEventQuery/list?repage";
	}

	
	
}