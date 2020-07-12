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
import com.thinkgem.jeesite.modules.guard.entity.AlarmEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.ConnectPersonnel;
import com.thinkgem.jeesite.modules.guard.service.ConnectPersonnelService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 人员交接明细Controller
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/connectPersonnel")
public class ConnectPersonnelController extends BaseController {

	@Autowired
	private ConnectPersonnelService connectPersonnelService;

	@ModelAttribute
	public ConnectPersonnel get(@RequestParam(required = false) String id, String staff_id) {
		ConnectPersonnel entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = connectPersonnelService.getTwo(id, staff_id);
		}
		if (entity == null) {
			entity = new ConnectPersonnel();
		}
		return entity;
	}

	@RequiresPermissions("guard:connectPersonnel:view")
	@RequestMapping(value = { "list", "" })
	public String list(ConnectPersonnel connectPersonnel, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ConnectPersonnel> p = new Page<ConnectPersonnel>(request, response);
		if (connectPersonnel != null) {
			if ("1".equals(connectPersonnel.getSort()) || connectPersonnel.getSort() == "1") {
				
				String type = connectPersonnel.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a3.name asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("a.task_type asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("a2.name asc");
				} else if ("6".equals(type)) {
					p.setOrderBy("a2.id asc");
				}else if ("7".equals(type)) {
					p.setOrderBy("o2.id asc");
				}
			}
			if (connectPersonnel.getSortType() != null) {
				if ("-1".equals(connectPersonnel.getSort()) || connectPersonnel.getSort() == "-1"
						|| connectPersonnel.getSort() == null) {
					String type = connectPersonnel.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("a3.name desc");
					} else if ("4".equals(type)) {
						p.setOrderBy("a.task_type desc");
					} else if ("5".equals(type)) {
						p.setOrderBy("a2.name desc");
					} else if ("6".equals(type)) {
						p.setOrderBy("a2.id desc");
					}else if ("7".equals(type)) {
						p.setOrderBy("o2.id desc");
					}
				}
			}
		}

		Page<ConnectPersonnel> page = connectPersonnelService.findPage(p, connectPersonnel);
		model.addAttribute("page", page);
		return "modules/guard/connectPersonnelList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param connectPersonnel
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:connectPersonnel:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(ConnectPersonnel connectPersonnel, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			connectPersonnel.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, ConnectPersonnel.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(connectPersonnel, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<ConnectPersonnel> page = connectPersonnelService.findPage(new Page<ConnectPersonnel>(request, response),
				connectPersonnel);
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

	@RequiresPermissions("guard:connectPersonnel:view")
	@RequestMapping(value = "form")
	public String form(ConnectPersonnel connectPersonnel, Model model) {
		model.addAttribute("connectPersonnel", connectPersonnel);
		return "modules/guard/connectPersonnelForm";
	}

	@RequiresPermissions("guard:connectPersonnel:view")
	@RequestMapping(value = "forms")
	public String forms(ConnectPersonnel connectPersonnel, Model model) {
		model.addAttribute("connectPersonnel", connectPersonnel);
		return "modules/guard/connectPersonnelForm";
	}

	@RequiresPermissions("guard:connectPersonnel:edit")
	@RequestMapping(value = "save")
	public String save(ConnectPersonnel connectPersonnel, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, connectPersonnel)) {
			return form(connectPersonnel, model);
		}
		connectPersonnelService.save(connectPersonnel);
		addMessage(redirectAttributes, "保存交接事件查询成功");
		return "redirect:" + Global.getAdminPath() + "/guard/connectPersonnel/?repage";
	}

	@RequiresPermissions("guard:connectPersonnel:edit")
	@RequestMapping(value = "delete")
	public String delete(ConnectPersonnel connectPersonnel, RedirectAttributes redirectAttributes) {
		connectPersonnelService.delete(connectPersonnel);
		addMessage(redirectAttributes, "删除交接事件查询成功");
		return "redirect:" + Global.getAdminPath() + "/guard/connectPersonnel/?repage";
	}

}