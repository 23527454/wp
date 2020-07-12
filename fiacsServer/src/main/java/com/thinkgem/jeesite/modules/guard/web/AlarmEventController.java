/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEvent;
import com.thinkgem.jeesite.modules.guard.service.AlarmEventService;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 异常报警事件Controller
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/alarmEvent")
public class AlarmEventController extends BaseController {

	@Autowired
	private AlarmEventService alarmEventService;

	@Autowired
	private TtsSettingService ttsService;

	@ModelAttribute
	public AlarmEvent get(@RequestParam(required = false) String id) {
		AlarmEvent entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = alarmEventService.get(id);
		}
		if (entity == null) {
			entity = new AlarmEvent();
		}
		return entity;
	}

	@RequiresPermissions("guard:alarmEvent:view")
	@RequestMapping(value = { "index" })
	public String index(AlarmEvent alarmEvent, Model model) {
		return "modules/guard/alarmEventIndex";
	}

	@RequiresPermissions("guard:alarmEvent:view")
	@RequestMapping(value = { "list", "" })
	public String list(AlarmEvent alarmEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AlarmEvent> p = new Page<AlarmEvent>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(1000);
		Page<AlarmEvent> page = alarmEventService.findPage(p, alarmEvent);
//		if (!"".equals(alarmEvent.getType()) && alarmEvent.getType() != null) {
//			List<AlarmEvent> list = page.getList();
//			List<AlarmEvent> LL = new ArrayList<AlarmEvent>();
//			String[] tempArr = alarmEvent.getNodes().split(",");
//			for (int i = 0; i < list.size(); i++) {
//				for (int n = 0; n < tempArr.length; n++) {
//					String eID = list.get(i).getEquipmentId();
//					String tID = tempArr[n];
//					if (eID.equals(tID)) {
//						LL.add(list.get(i));
//					}
//				}
//			}
//			page.setList(LL);
//		}
		model.addAttribute("page", page);
		model.addAttribute("nodes", alarmEvent.getNodes());
		List<TtsSetting> ttss = ttsService.getByType("alarm");
		if (ttss.size() > 0) {
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		return "modules/guard/alarmEventList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param alarmEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:alarmEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(AlarmEvent alarmEvent, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			alarmEvent.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, AlarmEvent.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(alarmEvent, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<AlarmEvent> page = alarmEventService.findPage(new Page<AlarmEvent>(request, response), alarmEvent);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:alarmEvent:view")
	@RequestMapping(value = "form")
	public String form(AlarmEvent alarmEvent, Model model) {
		model.addAttribute("alarmEvent", alarmEvent);
		return "modules/guard/alarmEventForm";
	}

	@RequiresPermissions("guard:alarmEvent:edit")
	@RequestMapping(value = "save")
	public String save(AlarmEvent alarmEvent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, alarmEvent)) {
			return form(alarmEvent, model);
		}
		alarmEventService.save(alarmEvent);
		addMessage(redirectAttributes, "保存异常报警事件成功");
		return "redirect:" + Global.getAdminPath() + "/guard/alarmEvent/?repage";
	}

	@RequiresPermissions("guard:alarmEvent:edit")
	@RequestMapping(value = "delete")
	public String delete(AlarmEvent alarmEvent, RedirectAttributes redirectAttributes) {
		alarmEventService.delete(alarmEvent);
		addMessage(redirectAttributes, "删除异常报警事件成功");
		return "redirect:" + Global.getAdminPath() + "/guard/alarmEvent/?repage";
	}

}