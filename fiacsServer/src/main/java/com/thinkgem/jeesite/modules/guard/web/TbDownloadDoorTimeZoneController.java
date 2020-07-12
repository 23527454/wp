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
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadDoorTimeZone;
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadDoorTimeZone;
import com.thinkgem.jeesite.modules.guard.service.TbDownloadDoorTimeZoneService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 门禁定时设置同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/tbDownloadDoorTimeZone")
public class TbDownloadDoorTimeZoneController extends BaseController {

	@Autowired
	private TbDownloadDoorTimeZoneService tbDownloadDoorTimeZoneService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public TbDownloadDoorTimeZone get(@RequestParam(required=false) String id) {
		TbDownloadDoorTimeZone entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tbDownloadDoorTimeZoneService.get(id);
		}
		if (entity == null){
			entity = new TbDownloadDoorTimeZone();
		}
		return entity;
	}
	

	@RequiresPermissions("guard:tbDownloadDoorTimeZone:view")
	@RequestMapping(value = {"list", ""})
	public String list(TbDownloadDoorTimeZone tbDownloadDoorTimeZone, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TbDownloadDoorTimeZone> p = new Page<TbDownloadDoorTimeZone>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		tbDownloadDoorTimeZone.setOfficeIds(officeids);
		Page<TbDownloadDoorTimeZone> page = tbDownloadDoorTimeZoneService.findPage(p, tbDownloadDoorTimeZone); 
		model.addAttribute("page", page);
		return "modules/guard/tbDownloadDoorTimeZoneList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param TbDownloadDoorTimeZone
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:tbDownloadDoorTimeZone:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(TbDownloadDoorTimeZone tbDownloadDoorTimeZone, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			tbDownloadDoorTimeZone.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, TbDownloadDoorTimeZone.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(tbDownloadDoorTimeZone, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<TbDownloadDoorTimeZone> page = tbDownloadDoorTimeZoneService.findPage(new Page<TbDownloadDoorTimeZone>(request, response), tbDownloadDoorTimeZone); 
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

	@RequiresPermissions("guard:tbDownloadDoorTimeZone:view")
	@RequestMapping(value = "form")
	public String form(TbDownloadDoorTimeZone tbDownloadDoorTimeZone, Model model) {
		model.addAttribute("tbDownloadDoorTimeZone", tbDownloadDoorTimeZone);
		return "modules/guard/tbDownloadDoorTimeZoneForm";
	}

	@RequiresPermissions("guard:tbDownloadDoorTimeZone:edit")
	@RequestMapping(value = "save")
	public String save(TbDownloadDoorTimeZone tbDownloadDoorTimeZone, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tbDownloadDoorTimeZone)){
			return form(tbDownloadDoorTimeZone, model);
		}
		tbDownloadDoorTimeZoneService.save(tbDownloadDoorTimeZone);
		addMessage(redirectAttributes, "保存门禁定时设置同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbDownloadDoorTimeZone/?repage";
	}
	
	@RequiresPermissions("guard:tbDownloadDoorTimeZone:edit")
	@RequestMapping(value = "delete")
	public String delete(TbDownloadDoorTimeZone tbDownloadDoorTimeZone, RedirectAttributes redirectAttributes) {
		if("1".equals(tbDownloadDoorTimeZone.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/tbDownloadDoorTimeZone/?repage";
		}
		
		tbDownloadDoorTimeZoneService.delete(tbDownloadDoorTimeZone);
		addMessage(redirectAttributes, "删除门禁定时设置同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbDownloadDoorTimeZone/?repage";
	}

}