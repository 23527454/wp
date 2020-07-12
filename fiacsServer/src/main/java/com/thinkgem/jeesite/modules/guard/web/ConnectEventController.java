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
import com.thinkgem.jeesite.modules.guard.service.ConnectEventService;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 交接事件Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/connectEvent")
public class ConnectEventController extends BaseController {

	@Autowired
	private ConnectEventService connectEventService;
	
	@Autowired
	private TtsSettingService ttsService;
	
	@ModelAttribute
	public ConnectEvent get(@RequestParam(required=false) String id) {
		ConnectEvent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = connectEventService.get(id);
		}
		if (entity == null){
			entity = new ConnectEvent();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:connectEvent:view")
	@RequestMapping(value = { "index" })
	public String index(ConnectEvent connectEvent, Model model) {
		return "modules/guard/connectEventIndex";
	}
	
	@RequiresPermissions("guard:connectEvent:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConnectEvent connectEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ConnectEvent> p = new Page<ConnectEvent>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(1000);
		Page<ConnectEvent> page = connectEventService.findPage(p, connectEvent); 
	
//		if (!"".equals(connectEvent.getType()) && connectEvent.getType() != null) {
//			List<ConnectEvent> connectEvents = new ArrayList<ConnectEvent>();;
//			String[] selectedOfficeIds = connectEvent.getNodes().split(",");
//			for (ConnectEvent c : page.getList()) {
//				for (String selectedOfficeId : selectedOfficeIds) {
//					if (c.getOfficeId().equals(selectedOfficeId)) {
//						connectEvents.add(c);
//					}
//				}
//			}
//			page.setList(connectEvents);
//		}
		model.addAttribute("page", page);
		model.addAttribute("nodes", connectEvent.getNodes());
		List<TtsSetting> ttss = ttsService.getByType("connect");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		
		return "modules/guard/connectEventList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param connectEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:connectEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(ConnectEvent connectEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			connectEvent.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, ConnectEvent.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(connectEvent, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<ConnectEvent> page = connectEventService.findPage(new Page<ConnectEvent>(request, response), connectEvent); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
	        		if(e.has("taskType")){
	        			String s = DictUtils.getDictLabel(e.getString("taskType"),"taskType", "");
	        			e.put("taskType",s);
					}
        }
        return jo.toString();
	}
	
	@RequiresPermissions("guard:connectEvent:view")
	@RequestMapping(value = "detail")
	@ResponseBody
	public ConnectEvent detail(ConnectEvent connectEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		return get(connectEvent.getId());
	}

	@RequiresPermissions("guard:connectEvent:view")
	@RequestMapping(value = "form")
	public String form(ConnectEvent connectEvent, Model model) {
		model.addAttribute("connectEvent", connectEvent);
		return "modules/guard/connectEventForm";
	}

	@RequiresPermissions("guard:connectEvent:edit")
	@RequestMapping(value = "save")
	public String save(ConnectEvent connectEvent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, connectEvent)){
			return form(connectEvent, model);
		}
		connectEventService.save(connectEvent);
		addMessage(redirectAttributes, "保存交接事件成功");
		return "redirect:"+Global.getAdminPath()+"/guard/connectEvent/?repage";
	}
	
	@RequiresPermissions("guard:connectEvent:edit")
	@RequestMapping(value = "delete")
	public String delete(ConnectEvent connectEvent, RedirectAttributes redirectAttributes) {
		connectEventService.delete(connectEvent);
		addMessage(redirectAttributes, "删除交接事件成功");
		return "redirect:"+Global.getAdminPath()+"/guard/connectEvent/?repage";
	}

}