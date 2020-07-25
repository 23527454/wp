/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.DoorInOutEvent;
import com.thinkgem.jeesite.modules.guard.service.DoorInOutEventService;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 车辆事件Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/doorInOutEvent")
public class DoorInOutEventController extends BaseController {

	@Autowired
	private DoorInOutEventService doorInOutEventService;
	
	@Autowired
	private TtsSettingService ttsService;
	
	@ModelAttribute
	public DoorInOutEvent get(@RequestParam(required=false) String id) {
		DoorInOutEvent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = doorInOutEventService.get(id);
		}
		if (entity == null){
			entity = new DoorInOutEvent();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:doorInOutEvent:view")
	@RequestMapping(value = { "index" })
	public String index(DoorInOutEvent doorInOutEvent, Model model) {
		return "modules/guard/doorInOutEventIndex";
	}
	
	@RequiresPermissions("guard:doorInOutEvent:view")
	@RequestMapping(value = {"list", ""})
	public String list(DoorInOutEvent doorInOutEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DoorInOutEvent> p = new Page<DoorInOutEvent>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(1000);
		Page<DoorInOutEvent> page = doorInOutEventService.findPage(p, doorInOutEvent);
		
		model.addAttribute("page", page);
		model.addAttribute("nodes", doorInOutEvent.getNodes());
		List<TtsSetting> ttss = ttsService.getByType("doorControl");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		return "modules/guard/doorInOutEventList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param doorInOutEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:doorInOutEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DoorInOutEvent doorInOutEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			doorInOutEvent.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DoorInOutEvent.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(doorInOutEvent, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DoorInOutEvent> page = doorInOutEventService.findPage(new Page<DoorInOutEvent>(request, response), doorInOutEvent); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:doorInOutEvent:view")
	@RequestMapping(value = "form")
	public String form(DoorInOutEvent doorInOutEvent, Model model) {
		model.addAttribute("doorInOutEvent", doorInOutEvent);
		return "modules/guard/doorInOutEventForm";
	}
	
	@RequestMapping(value = "listByLatestId")
	@ResponseBody
	public List<DoorInOutEvent> listByLatestId(@RequestParam("latestId")String latestId,
			@RequestParam("nodes")String nodes)
			throws JsonGenerationException, JsonMappingException, IOException {
 		List<DoorInOutEvent> list = doorInOutEventService.getFeeds(latestId, nodes);
		return list;
	}


	@RequestMapping(value = { "doorControlIndex" })
	public String doorControlIndex(Model model) {
		return "modules/guard/doorControlIndex";
	}

	@RequestMapping(value = "controlDoor")
	@ResponseBody
	public String controlDoor(@RequestParam("resetType")String resetType,
							  @RequestParam("nodes")String nodes){
		return JSON.toJSONString(doorInOutEventService.controlDoor(resetType,nodes));
	}

	@RequestMapping(value = "doorControlForm")
	public String doorControlForm(@RequestParam("nodes")String nodes,Model model){
		model.addAttribute("nodes",nodes);
		return  "modules/guard/doorControlForm";
	}
}