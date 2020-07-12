/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

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
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 语音配置信息Controller
 * @author Jumbo
 * @version 2017-07-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/ttsSetting")
public class TtsSettingController extends BaseController {

	@Autowired
	private TtsSettingService ttsSettingService;
	
	@ModelAttribute
	public TtsSetting get(@RequestParam(required=false) String id) {
		TtsSetting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ttsSettingService.get(id);
		}
		if (entity == null){
			entity = new TtsSetting();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:ttsSetting:view")
	@RequestMapping(value = {"list"})
	public String list(TtsSetting ttsSetting, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TtsSetting> page = ttsSettingService.findPage(new Page<TtsSetting>(request, response), ttsSetting); 
		model.addAttribute("page", page);
		return "modules/sys/ttsSettingList";
	}
	
	@RequiresPermissions("sys:ttsSetting:view")
	@RequestMapping(value = { "index", "" })
	public String index(TtsSetting ttsSetting, Model model) {
		return "modules/sys/ttsSettingIndex";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param ttsSetting
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:ttsSetting:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(TtsSetting ttsSetting, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			ttsSetting.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, TtsSetting.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(ttsSetting, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<TtsSetting> page = ttsSettingService.findPage(new Page<TtsSetting>(request, response), ttsSetting); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("sys:ttsSetting:view")
	@RequestMapping(value = "form")
	public String form(TtsSetting ttsSetting, Model model) {
		List<TtsSetting> list = ttsSettingService.getByType(ttsSetting.getVoiceType()); 
		List<Dict> dict = DictUtils.getDictList("tts_"+ttsSetting.getVoiceType());
		String json = DictUtils.getDictListJson("tts_"+ttsSetting.getVoiceType());
		if(list!=null && list.size() > 0){
			model.addAttribute("ttsSetting", list.get(0));
			model.addAttribute("json", json);
			model.addAttribute("dict", dict);
			return "modules/sys/ttsSettingForm";
		}
		return null;
	}

	@RequiresPermissions("sys:ttsSetting:edit")
	@RequestMapping(value = "save")
	public String save(TtsSetting ttsSetting, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ttsSetting)){
			return form(ttsSetting, model);
		}
		ttsSettingService.save(ttsSetting);
		addMessage(redirectAttributes, "保存语音配置成功");
		return "redirect:"+Global.getAdminPath()+"/sys/ttsSetting/form?voiceType="+ttsSetting.getVoiceType()+"&repage";
	}
	
	@RequiresPermissions("sys:ttsSetting:edit")
	@RequestMapping(value = "delete")
	public String delete(TtsSetting ttsSetting, RedirectAttributes redirectAttributes) {
		ttsSettingService.delete(ttsSetting);
		addMessage(redirectAttributes, "删除语音配置成功");
		return "redirect:"+Global.getAdminPath()+"/sys/ttsSetting/form?repage";
	}

}