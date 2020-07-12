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
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.LineEvent;
import com.thinkgem.jeesite.modules.guard.entity.TaskLineInfo;
import com.thinkgem.jeesite.modules.guard.service.LineEventService;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 线路监控Controller
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/lineEvent")
public class LineEventController extends BaseController {

	@Autowired
	private LineEventService lineEventService;

	@Autowired
	private TtsSettingService ttsService;

	@ModelAttribute
	public LineEvent get(@RequestParam(required = false) String id) {
		LineEvent entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = lineEventService.get(id);
		}
		if (entity == null) {
			entity = new LineEvent();
		}
		return entity;
	}

	@RequiresPermissions("guard:lineEvent:view")
	@RequestMapping(value = { "index" })
	public String index(LineEvent lineEvent, Model model) {
		return "modules/guard/lineEventIndex";
	}

	@RequiresPermissions("guard:lineEvent:view")
	@RequestMapping(value = { "list", "" })
	public String list(LineEvent lineEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LineEvent> p = new Page<LineEvent>(request, response);
		p.setOrderBy("a.id desc");
		Page<LineEvent> page = lineEventService.findPage(p, lineEvent);

//		if (!"".equals(lineEvent.getType()) && lineEvent.getType() != null) {
//			List<LineEvent> list = page.getList();
//			List<LineEvent> LL = new ArrayList<LineEvent>();
//			String[] tempArr = lineEvent.getNodes().split(",");
//			for (int i = 0; i < list.size(); i++) {
//				for (int n = 0; n < tempArr.length; n++) {
//					String eID = list.get(i).getLineId();
//					String tID = tempArr[n];
//					if (eID.equals(tID)) {
//						LL.add(list.get(i));
//					}
//				}
//			}
//			page.setList(LL);
//		}
		model.addAttribute("nodes", lineEvent.getNodes());
		model.addAttribute("page", page);
		List<TtsSetting> ttss = ttsService.getByType("line");
		if (ttss.size() > 0) {
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		return "modules/guard/lineEventList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param lineEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:lineEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(LineEvent lineEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			lineEvent.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, LineEvent.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(lineEvent, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<LineEvent> page = lineEventService.findPage(new Page<LineEvent>(request, response), lineEvent);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:lineEvent:view")
	@RequestMapping(value = "form")
	public String form(LineEvent lineEvent, Model model) {
		List<ConnectEvent> connectEventList = lineEvent.getConnectEventList();
		List<TaskLineInfo> taskLineInfoList = lineEvent.getTaskLineInfoList();

		for (int n = 0; n < taskLineInfoList.size(); n++) {
			for (int i = 0; i < connectEventList.size(); i++) {
				if(connectEventList.get(i).getEquipmentId().equals(taskLineInfoList.get(n).getEquipmentId())){
					taskLineInfoList.get(n).setType("已完成");
					break;
				}
			}
		}

		model.addAttribute("lineEvent", lineEvent);

		return "modules/guard/lineEventForm";
	}

	@RequiresPermissions("guard:lineEvent:edit")
	@RequestMapping(value = "save")
	public String save(LineEvent lineEvent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lineEvent)) {
			return form(lineEvent, model);
		}
		lineEventService.save(lineEvent);
		addMessage(redirectAttributes, "保存线路监控成功");
		return "redirect:" + Global.getAdminPath() + "/guard/lineEvent/?repage";
	}

	@RequiresPermissions("guard:lineEvent:edit")
	@RequestMapping(value = "delete")
	public String delete(LineEvent lineEvent, RedirectAttributes redirectAttributes) {
		lineEventService.delete(lineEvent);
		addMessage(redirectAttributes, "删除线路监控成功");
		return "redirect:" + Global.getAdminPath() + "/guard/lineEvent/?repage";
	}

}