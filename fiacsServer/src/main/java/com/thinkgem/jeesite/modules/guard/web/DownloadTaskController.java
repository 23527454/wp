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
import com.thinkgem.jeesite.modules.guard.entity.DownloadTask;
import com.thinkgem.jeesite.modules.guard.service.DownloadTaskService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 排班同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/downloadTask")
public class DownloadTaskController extends BaseController {

	@Autowired
	private DownloadTaskService downloadTaskService;
	@Autowired
	private OfficeService officeService;
	@ModelAttribute
	public DownloadTask get(@RequestParam(required=false) String id) {
		DownloadTask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadTaskService.get(id);
		}
		if (entity == null){
			entity = new DownloadTask();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:downloadTask:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadTask downloadTask, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadTask> p = new Page<DownloadTask>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadTask.setOfficeIds(officeids);
		Page<DownloadTask> page = downloadTaskService.findPage(p, downloadTask);
		model.addAttribute("page", page);
		return "modules/guard/downloadTaskList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadTask
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:downloadTask:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadTask downloadTask, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadTask.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadTask.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadTask, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadTask> page = downloadTaskService.findPage(new Page<DownloadTask>(request, response), downloadTask); 
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

	@RequiresPermissions("guard:downloadTask:view")
	@RequestMapping(value = "form")
	public String form(DownloadTask downloadTask, Model model) {
		model.addAttribute("downloadTask", downloadTask);
		return "modules/guard/downloadTaskForm";
	}

	@RequiresPermissions("guard:downloadTask:edit")
	@RequestMapping(value = "save")
	public String save(DownloadTask downloadTask, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadTask)){
			return form(downloadTask, model);
		}
		downloadTaskService.save(downloadTask);
		addMessage(redirectAttributes, "保存排班同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadTask/?repage";
	}
	
	@RequiresPermissions("guard:downloadTask:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadTask downloadTask, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadTask.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
		}
		downloadTaskService.delete(downloadTask);
		addMessage(redirectAttributes, "删除排班同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadTask/?repage";
	}

}