/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.mj.entity.DownloadAccessParaInfo;
import com.thinkgem.jeesite.modules.mj.service.DownloadAccessParaInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
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
 * 门禁同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/downloadAccessParaInfo")
public class DownloadAccessParaInfoController extends BaseController {

	@Autowired
	private DownloadAccessParaInfoService downloadAccessParaInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public DownloadAccessParaInfo get(@RequestParam(required=false) String id) {
		DownloadAccessParaInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadAccessParaInfoService.get(id);
		}
		if (entity == null){
			entity = new DownloadAccessParaInfo();
		}
		return entity;
	}
	

	@RequiresPermissions("mj:downloadAccessParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadAccessParaInfo downloadAccessParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadAccessParaInfo> p = new Page<DownloadAccessParaInfo>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadAccessParaInfo.setOfficeIds(officeids);
		Page<DownloadAccessParaInfo> page = downloadAccessParaInfoService.findPage(p, downloadAccessParaInfo); 
		model.addAttribute("page", page);
		return "modules/mj/downloadAccessParaInfoList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadAccessParaInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mj:downloadAccessParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadAccessParaInfo downloadAccessParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadAccessParaInfo.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadAccessParaInfo.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadAccessParaInfo, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadAccessParaInfo> page = downloadAccessParaInfoService.findPage(new Page<DownloadAccessParaInfo>(request, response), downloadAccessParaInfo); 
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

	@RequiresPermissions("mj:downloadAccessParaInfo:view")
	@RequestMapping(value = "form")
	public String form(DownloadAccessParaInfo downloadAccessParaInfo, Model model) {
		model.addAttribute("downloadAccessParaInfo", downloadAccessParaInfo);
		return "modules/mj/downloadAccessParaInfoForm";
	}

	@RequiresPermissions("mj:downloadAccessParaInfo:edit")
	@RequestMapping(value = "save")
	public String save(DownloadAccessParaInfo downloadAccessParaInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadAccessParaInfo)){
			return form(downloadAccessParaInfo, model);
		}
		downloadAccessParaInfoService.save(downloadAccessParaInfo);
		addMessage(redirectAttributes, "保存门禁同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/mj/downloadAccessParaInfo/?repage";
	}
	
	@RequiresPermissions("mj:downloadAccessParaInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadAccessParaInfo downloadAccessParaInfo, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadAccessParaInfo.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/mj/downloadAccessParaInfo/?repage";
		}
		
		downloadAccessParaInfoService.delete(downloadAccessParaInfo);
		addMessage(redirectAttributes, "删除门禁同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/mj/downloadAccessParaInfo/?repage";
	}

}