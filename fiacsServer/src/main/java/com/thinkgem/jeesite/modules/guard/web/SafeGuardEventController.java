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
import com.thinkgem.jeesite.modules.guard.entity.SafeGuardEvent;
import com.thinkgem.jeesite.modules.guard.service.SafeGuardEventService;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 维保员事件Controller
 * @author zgx
 * @version 2018-12-31
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/safeGuardEvent")
public class SafeGuardEventController extends BaseController {

	@Autowired
	private SafeGuardEventService safeGuradEventService;
	
	@Autowired
	private TtsSettingService ttsService;
	
	@ModelAttribute
	public SafeGuardEvent get(@RequestParam(required=false) String id) {
		SafeGuardEvent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = safeGuradEventService.get(id);
		}
		if (entity == null){
			entity = new SafeGuardEvent();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:safeGuardEvent:view")
	@RequestMapping(value = { "index" })
	public String index(SafeGuardEvent SafeGuardEvent, Model model) {
		return "modules/guard/safeGuardEventIndex";
	}
	
	@RequiresPermissions("guard:safeGuardEvent:view")
	@RequestMapping(value = {"list", ""})
	public String list(SafeGuardEvent SafeGuardEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SafeGuardEvent> p = new Page<SafeGuardEvent>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(1000);
		Page<SafeGuardEvent> page = safeGuradEventService.findPage(p, SafeGuardEvent); 
//		if (!"".equals(SafeGuardEvent.getType()) && SafeGuardEvent.getType() != null) {
//			List<SafeGuardEvent> list = page.getList();
//			List<SafeGuardEvent> LL = new ArrayList<SafeGuardEvent>();
//			String[] tempArr = SafeGuardEvent.getNodes().split(",");
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
		model.addAttribute("nodes", SafeGuardEvent.getNodes());
		List<TtsSetting> ttss = ttsService.getByType("safeGuard");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		return "modules/guard/safeGuardEventList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param SafeGuardEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:safeGuardEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(SafeGuardEvent SafeGuardEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			SafeGuardEvent.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, SafeGuardEvent.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(SafeGuardEvent, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<SafeGuardEvent> page = safeGuradEventService.findPage(new Page<SafeGuardEvent>(request, response), SafeGuardEvent); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:safeGuardEvent:view")
	@RequestMapping(value = "form")
	public String form(SafeGuardEvent SafeGuardEvent, Model model) {
		model.addAttribute("safeGuardEvent", SafeGuardEvent);
		return "modules/guard/safeGuardEventForm";
	}
	
	@RequestMapping(value = "listByLatestId")
	@ResponseBody
	public List<SafeGuardEvent> listByLatestId(@RequestParam("latestId")String latestId,
			@RequestParam("nodes")String nodes)
			throws JsonGenerationException, JsonMappingException, IOException {
 		List<SafeGuardEvent> list = safeGuradEventService.getFeeds(latestId, nodes);
		return list;
	}
}