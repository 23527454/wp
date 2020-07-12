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
import com.thinkgem.jeesite.modules.guard.entity.DownloadMoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.service.DownloadMoneyBoxAllotService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 款箱调拨同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/downloadMoneyBoxAllot")
public class DownloadMoneyBoxAllotController extends BaseController {

	@Autowired
	private DownloadMoneyBoxAllotService downloadMoneyBoxAllotService;
	@Autowired
	private OfficeService officeService;
	@ModelAttribute
	public DownloadMoneyBoxAllot get(@RequestParam(required=false) String id) {
		DownloadMoneyBoxAllot entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadMoneyBoxAllotService.get(id);
		}
		if (entity == null){
			entity = new DownloadMoneyBoxAllot();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:downloadMoneyBoxAllot:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadMoneyBoxAllot downloadMoneyBoxAllot, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadMoneyBoxAllot> p = new Page<DownloadMoneyBoxAllot>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadMoneyBoxAllot.setOfficeIds(officeids);
		Page<DownloadMoneyBoxAllot> page = downloadMoneyBoxAllotService.findPage(p, downloadMoneyBoxAllot); 
		model.addAttribute("page", page);
		return "modules/guard/downloadMoneyBoxAllotList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadMoneyBoxAllot
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:downloadMoneyBoxAllot:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadMoneyBoxAllot downloadMoneyBoxAllot, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadMoneyBoxAllot.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadMoneyBoxAllot.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadMoneyBoxAllot, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadMoneyBoxAllot> page = downloadMoneyBoxAllotService.findPage(new Page<DownloadMoneyBoxAllot>(request, response), downloadMoneyBoxAllot); 
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

	@RequiresPermissions("guard:downloadMoneyBoxAllot:view")
	@RequestMapping(value = "form")
	public String form(DownloadMoneyBoxAllot downloadMoneyBoxAllot, Model model) {
		model.addAttribute("downloadMoneyBoxAllot", downloadMoneyBoxAllot);
		return "modules/guard/downloadMoneyBoxAllotForm";
	}

	@RequiresPermissions("guard:downloadMoneyBoxAllot:edit")
	@RequestMapping(value = "save")
	public String save(DownloadMoneyBoxAllot downloadMoneyBoxAllot, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadMoneyBoxAllot)){
			return form(downloadMoneyBoxAllot, model);
		}
		downloadMoneyBoxAllotService.save(downloadMoneyBoxAllot);
		addMessage(redirectAttributes, "保存款箱调拨同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadMoneyBoxAllot/?repage";
	}
	
	@RequiresPermissions("guard:downloadMoneyBoxAllot:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadMoneyBoxAllot downloadMoneyBoxAllot, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadMoneyBoxAllot.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
		}
		
		downloadMoneyBoxAllotService.delete(downloadMoneyBoxAllot);
		addMessage(redirectAttributes, "删除款箱调拨同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/downloadMoneyBoxAllot/?repage";
	}

}