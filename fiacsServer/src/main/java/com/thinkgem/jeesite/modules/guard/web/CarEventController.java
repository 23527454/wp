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
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.service.CarEventService;
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
@RequestMapping(value = "${adminPath}/guard/carEvent")
public class CarEventController extends BaseController {

	@Autowired
	private CarEventService carEventService;
	
	@Autowired
	private TtsSettingService ttsService;
	
	@ModelAttribute
	public CarEvent get(@RequestParam(required=false) String id) {
		CarEvent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = carEventService.get(id);
		}
		if (entity == null){
			entity = new CarEvent();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:carEvent:view")
	@RequestMapping(value = { "index" })
	public String index(CarEvent carEvent, Model model) {
		return "modules/guard/carEventIndex";
	}
	
	@RequiresPermissions("guard:carEvent:view")
	@RequestMapping(value = {"list", ""})
	public String list(CarEvent carEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CarEvent> p = new Page<CarEvent>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(1000);
		Page<CarEvent> page = carEventService.findPage(p, carEvent); 
//		if (!"".equals(carEvent.getType()) && carEvent.getType() != null) {
//			List<CarEvent> list = page.getList();
//			List<CarEvent> LL = new ArrayList<CarEvent>();
//			String[] tempArr = carEvent.getNodes().split(",");
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
		model.addAttribute("nodes", carEvent.getNodes());
		List<TtsSetting> ttss = ttsService.getByType("car");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}
		return "modules/guard/carEventList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param carEvent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:carEvent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(CarEvent carEvent, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			carEvent.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, CarEvent.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(carEvent, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<CarEvent> page = carEventService.findPage(new Page<CarEvent>(request, response), carEvent); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:carEvent:view")
	@RequestMapping(value = "form")
	public String form(CarEvent carEvent, Model model) {
		model.addAttribute("carEvent", carEvent);
		return "modules/guard/carEventForm";
	}

	@RequiresPermissions("guard:carEvent:edit")
	@RequestMapping(value = "save")
	public String save(CarEvent carEvent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, carEvent)){
			return form(carEvent, model);
		}
		carEventService.save(carEvent);
		addMessage(redirectAttributes, "保存车辆事件成功");
		return "redirect:"+Global.getAdminPath()+"/guard/carEvent/?repage";
	}
	
	@RequiresPermissions("guard:carEvent:edit")
	@RequestMapping(value = "delete")
	public String delete(CarEvent carEvent, RedirectAttributes redirectAttributes) {
		carEventService.delete(carEvent);
		addMessage(redirectAttributes, "删除车辆事件成功");
		return "redirect:"+Global.getAdminPath()+"/guard/carEvent/?repage";
	}

}