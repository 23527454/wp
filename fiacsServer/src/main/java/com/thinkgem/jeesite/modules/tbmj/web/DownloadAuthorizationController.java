/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadAuthorization;
import com.thinkgem.jeesite.modules.tbmj.service.DownloadAuthorizationService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 权限同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/downloadAuthorization")
public class DownloadAuthorizationController extends BaseController {

	@Autowired
	private DownloadAuthorizationService downloadAuthorizationService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public DownloadAuthorization get(@RequestParam(required=false) String id) {
		DownloadAuthorization entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadAuthorizationService.get(id);
		}
		if (entity == null){
			entity = new DownloadAuthorization();
		}
		return entity;
	}
	

	@RequiresPermissions("tbmj:downloadAuthorization:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadAuthorization downloadAuthorization, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadAuthorization> p = new Page<DownloadAuthorization>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadAuthorization.setOfficeIds(officeids);
		Page<DownloadAuthorization> page = downloadAuthorizationService.findPage(p, downloadAuthorization);
		model.addAttribute("page", page);
		return "modules/tbmj/downloadAuthorizationList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadAuthorization
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("tbmj:downloadAuthorization:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadAuthorization downloadAuthorization, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadAuthorization.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadAuthorization.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadAuthorization, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadAuthorization> page = downloadAuthorizationService.findPage(new Page<DownloadAuthorization>(request, response), downloadAuthorization);
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

	@RequiresPermissions("tbmj:downloadAuthorization:view")
	@RequestMapping(value = "form")
	public String form(DownloadAuthorization downloadAuthorization, Model model) {
		model.addAttribute("downloadAuthorization", downloadAuthorization);
		return "modules/tbmj/downloadAuthorizationForm";
	}

	@RequiresPermissions("tbmj:downloadAuthorization:edit")
	@RequestMapping(value = "save")
	public String save(DownloadAuthorization downloadAuthorization, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadAuthorization)){
			return form(downloadAuthorization, model);
		}
		downloadAuthorizationService.save(downloadAuthorization);
		addMessage(redirectAttributes, "保存门禁同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/tbmj/downloadAuthorization/?repage";
	}
	
	@RequiresPermissions("tbmj:downloadAuthorization:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadAuthorization downloadAuthorization, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadAuthorization.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/tbmj/downloadAuthorization/?repage";
		}
		
		downloadAuthorizationService.delete(downloadAuthorization);
		addMessage(redirectAttributes, "删除门禁同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/tbmj/downloadAuthorization/?repage";
	}

}