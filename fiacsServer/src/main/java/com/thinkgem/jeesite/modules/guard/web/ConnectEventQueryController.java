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
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.ConnectEventQueryService;
import com.thinkgem.jeesite.modules.guard.service.ConnectEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 交接事件查询Controller
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/connectEventQuery")
public class ConnectEventQueryController extends BaseController {

	@Autowired
	private ConnectEventService connectEventService;

	@ModelAttribute
	public ConnectEvent get(@RequestParam(required = false) String id) {
		ConnectEvent entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = connectEventService.get(id);
		}
		if (entity == null) {
			entity = new ConnectEvent();
		}
		return entity;
	}

	@RequiresPermissions("guard:connectEventQuery:view")
	@RequestMapping(value = { "list", "" })
	public String list(ConnectEvent connectEvent, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Page<ConnectEvent> p = new Page<ConnectEvent>(request, response);
		if (connectEvent != null) {
			if ("1".equals(connectEvent.getSort()) || connectEvent.getSort() == "1") {

				String type = connectEvent.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a3.id asc");   // 押款员
				} else if ("4".equals(type)) {
					p.setOrderBy("a10.finger_id asc");   // 交接员
				} else if ("5".equals(type)) {
					p.setOrderBy("d.carplate asc");
				} else if ("6".equals(type)) {
					p.setOrderBy("d.card_num asc");
				} else if ("7".equals(type)) {
					p.setOrderBy("a.task_type asc");
				} else if ("8".equals(type)) {
					p.setOrderBy("f.name asc");
				} else if ("9".equals(type)) {
					p.setOrderBy("a.task_id asc");
				} else if ("10".equals(type)) {
					p.setOrderBy("h.name asc");
				}
			}

			if (connectEvent.getSortType() != null) {
				if ("-1".equals(connectEvent.getSort()) || connectEvent.getSort() == "-1"
						|| connectEvent.getSort() == null) {
					String type = connectEvent.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("a3.id desc");   // 押款员
					} else if ("4".equals(type)) {
						p.setOrderBy("a10.finger_id desc");   // 交接员
					} else if ("5".equals(type)) {
						p.setOrderBy("d.carplate desc");
					} else if ("6".equals(type)) {
						p.setOrderBy("d.card_num desc");
					} else if ("7".equals(type)) {
						p.setOrderBy("a.task_type desc");
					} else if ("8".equals(type)) {
						p.setOrderBy("f.name desc");
					} else if ("9".equals(type)) {
						p.setOrderBy("a.task_id desc");
					} else if ("10".equals(type)) {
						p.setOrderBy("h.name desc");
					}
				}
			}
		}

		Page<ConnectEvent> page = connectEventService.findPage(p, connectEvent);
		model.addAttribute("page", page);
		return "modules/guard/connectEventQueryList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:connectEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(ConnectEvent connectEvent, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			connectEvent.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, ConnectEvent.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(connectEvent, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<ConnectEvent> page = connectEventService.findPage(new Page<ConnectEvent>(request, response),
				connectEvent);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("taskType")) {
				String s = DictUtils.getDictLabel(e.getString("taskType"), "taskType", "");
				e.put("taskType", s);
			}
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:connectEventQuery:view")
	@RequestMapping(value = "form")
	public String form(ConnectEvent connectEvent, Model model) {
		model.addAttribute("connectEvent", connectEvent);
		return "modules/guard/connectEventQueryForm";
	}

	@RequiresPermissions("guard:connectEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(ConnectEvent connectEvent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, connectEvent)) {
			return form(connectEvent, model);
		}
		connectEventService.save(connectEvent);
		addMessage(redirectAttributes, "保存交接事件查询成功");
		return "redirect:" + Global.getAdminPath() + "/guard/connectEventQuery/?repage";
	}

	@RequiresPermissions("guard:connectEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(ConnectEvent connectEvent, RedirectAttributes redirectAttributes) {
		connectEventService.delete(connectEvent);
		addMessage(redirectAttributes, "删除交接事件查询成功");
		return "redirect:" + Global.getAdminPath() + "/guard/connectEventQuery/?repage";
	}

	/**
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:connectEventQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(ConnectEvent connectEvent, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "交接事件数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<ConnectEvent> page = connectEventService
					.findPage(new Page<ConnectEvent>(request, response, -1), connectEvent);
			new ExportExcel("交接事件数据", ConnectEvent.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出交接事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/connectEventQuery/list?repage";
	}

}