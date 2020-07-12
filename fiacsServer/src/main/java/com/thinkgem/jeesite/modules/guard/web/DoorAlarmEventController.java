/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.DoorAlarmEvent;
import com.thinkgem.jeesite.modules.guard.service.DoorAlarmEventService;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 车辆事件Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/doorAlarmEvent")
public class DoorAlarmEventController extends BaseController {

	@Autowired
	private DoorAlarmEventService doorAlarmEventService;
	
	@Autowired
	private TtsSettingService ttsService;
	
	@ModelAttribute
	public DoorAlarmEvent get(@RequestParam(required=false) String id) {
		DoorAlarmEvent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = doorAlarmEventService.get(id);
		}
		if (entity == null){
			entity = new DoorAlarmEvent();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:doorAlarmEvent:view")
	@RequestMapping(value = { "index" })
	public String index(DoorAlarmEvent DoorAlarmEvent, Model model) {
		return "modules/guard/doorAlarmEventIndex";
	}
	
	@RequiresPermissions("guard:doorAlarmEvent:view")
	@RequestMapping(value = {"list", ""})
	public String list(DoorAlarmEvent DoorAlarmEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DoorAlarmEvent> p = new Page<DoorAlarmEvent>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(1000);
		Page<DoorAlarmEvent> page = doorAlarmEventService.findPage(p, DoorAlarmEvent); 
//		if (!"".equals(DoorAlarmEvent.getType()) && DoorAlarmEvent.getType() != null) {
//			List<DoorAlarmEvent> list = page.getList();
//			List<DoorAlarmEvent> LL = new ArrayList<DoorAlarmEvent>();
//			String[] tempArr = DoorAlarmEvent.getNodes().split(",");
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
		model.addAttribute("nodes", DoorAlarmEvent.getNodes());
		List<TtsSetting> ttss = ttsService.getByType("doorAlarm");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		return "modules/guard/doorAlarmEventList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param DoorAlarmEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:doorAlarmEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DoorAlarmEvent DoorAlarmEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			DoorAlarmEvent.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DoorAlarmEvent.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(DoorAlarmEvent, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DoorAlarmEvent> page = doorAlarmEventService.findPage(new Page<DoorAlarmEvent>(request, response), DoorAlarmEvent); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:doorAlarmEvent:view")
	@RequestMapping(value = "form")
	public String form(DoorAlarmEvent DoorAlarmEvent, Model model) {
		model.addAttribute("DoorAlarmEvent", DoorAlarmEvent);
		return "modules/guard/DoorAlarmEventForm";
	}
	
	@RequestMapping(value = "listByLatestId")
	@ResponseBody
	public List<DoorAlarmEvent> listByLatestId(@RequestParam("latestId")String latestId,
			@RequestParam("nodes")String nodes)
			throws JsonGenerationException, JsonMappingException, IOException {
 		List<DoorAlarmEvent> list = doorAlarmEventService.getFeeds(latestId, nodes);
		return list;
	}
}