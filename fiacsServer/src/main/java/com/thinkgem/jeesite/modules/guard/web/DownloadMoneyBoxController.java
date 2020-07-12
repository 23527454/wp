/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBox;
import com.thinkgem.jeesite.modules.guard.service.DownloadMoneyBoxService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 款箱同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/downloadMoneyBox")
public class DownloadMoneyBoxController extends BaseController {

	@Autowired
	private DownloadMoneyBoxService downloadMoneyBoxService;
	@Autowired
	private OfficeService officeService;
	@ModelAttribute
	public DownloadMoneyBox get(@RequestParam(required=false) String id) {
		DownloadMoneyBox entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadMoneyBoxService.get(id);
		}
		if (entity == null){
			entity = new DownloadMoneyBox();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:downloadMoneyBox:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadMoneyBox downloadMoneyBox, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadMoneyBox> p = new Page<DownloadMoneyBox>(request, response);
		p.setOrderBy("a.id desc");
		
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadMoneyBox.setOfficeIds(officeids);
		
		Page<DownloadMoneyBox> page = downloadMoneyBoxService.findPage(p, downloadMoneyBox); 
		model.addAttribute("page", page);
		return "modules/guard/downloadMoneyBoxList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadMoneyBox
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:downloadMoneyBox:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadMoneyBox downloadMoneyBox, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadMoneyBox.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadMoneyBox.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadMoneyBox, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadMoneyBox> page = downloadMoneyBoxService.findPage(new Page<DownloadMoneyBox>(request, response), downloadMoneyBox); 
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

	@RequiresPermissions("guard:downloadMoneyBox:view")
	@RequestMapping(value = "form")
	public String form(DownloadMoneyBox downloadMoneyBox, Model model) {
		model.addAttribute("downloadMoneyBox", downloadMoneyBox);
		return "modules/guard/downloadMoneyBoxForm";
	}

	@RequiresPermissions("guard:downloadMoneyBox:edit")
	@RequestMapping(value = "save")
	public String save(DownloadMoneyBox downloadMoneyBox, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadMoneyBox)){
			return form(downloadMoneyBox, model);
		}
		downloadMoneyBoxService.save(downloadMoneyBox);
		addMessage(redirectAttributes, "保存款箱同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadMoneyBox/?repage";
	}
	
	@RequiresPermissions("guard:downloadMoneyBox:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadMoneyBox downloadMoneyBox, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadMoneyBox.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
		}
		
		downloadMoneyBoxService.delete(downloadMoneyBox);
		addMessage(redirectAttributes, "删除款箱同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadMoneyBox/?repage";
	}

}