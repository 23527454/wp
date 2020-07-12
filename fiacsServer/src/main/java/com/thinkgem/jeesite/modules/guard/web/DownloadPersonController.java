/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.entity.DownloadPerson;
import com.thinkgem.jeesite.modules.guard.service.DownloadPersonService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 人员同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/downloadPerson")
public class DownloadPersonController extends BaseController {

	@Autowired
	private DownloadPersonService downloadPersonService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public DownloadPerson get(@RequestParam(required=false) String id) {
		DownloadPerson entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadPersonService.get(id);
		}
		if (entity == null){
			entity = new DownloadPerson();
		}
		return entity;
	}
	

	@RequiresPermissions("guard:downloadPerson:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadPerson downloadPerson, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadPerson> p = new Page<DownloadPerson>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadPerson.setOfficeIds(officeids);
		Page<DownloadPerson> page = downloadPersonService.findPage(p, downloadPerson); 
		model.addAttribute("page", page);
		return "modules/guard/downloadPersonList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadPerson
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:downloadPerson:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadPerson downloadPerson, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadPerson.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadPerson.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadPerson, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadPerson> page = downloadPersonService.findPage(new Page<DownloadPerson>(request, response), downloadPerson); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
	        		if(e.has("isDownload")){
	        			String s = DictUtils.getDictLabel(e.getString("isDownload"),"isDownload", "");
	        			e.put("isDownload",s);
					}
	        		if(e.has("downloadType")){
	        			String s = DictUtils.getDictLabel(e.getString("downloadType"),"downloadType", "");
	        			e.put("downloadType",s);
					}
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:downloadPerson:view")
	@RequestMapping(value = "form")
	public String form(DownloadPerson downloadPerson, Model model) {
		model.addAttribute("downloadPerson", downloadPerson);
		return "modules/guard/downloadPersonForm";
	}

	@RequiresPermissions("guard:downloadPerson:edit")
	@RequestMapping(value = "save")
	public String save(DownloadPerson downloadPerson, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadPerson)){
			return form(downloadPerson, model);
		}
		downloadPersonService.save(downloadPerson);
		addMessage(redirectAttributes, "保存人员同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
	}
	
	@RequiresPermissions("guard:downloadPerson:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadPerson downloadPerson, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadPerson.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
		}
		
		downloadPersonService.delete(downloadPerson);
		addMessage(redirectAttributes, "删除人员同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
	}

}