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
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadDefenseParaInfo;
import com.thinkgem.jeesite.modules.tbmj.service.DownloadDefenseParaInfoService;
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
 * 防区参数同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/downloadDefenseParaInfo")
public class DownloadDefenseParaInfoController extends BaseController {

	@Autowired
	private DownloadDefenseParaInfoService downloadDefenseParaInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public DownloadDefenseParaInfo get(@RequestParam(required=false) String id) {
		DownloadDefenseParaInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadDefenseParaInfoService.get(id);
		}
		if (entity == null){
			entity = new DownloadDefenseParaInfo();
		}
		return entity;
	}
	

	@RequiresPermissions("tbmj:downloadDefenseParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadDefenseParaInfo downloadDefenseParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadDefenseParaInfo> p = new Page<DownloadDefenseParaInfo>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadDefenseParaInfo.setOfficeIds(officeids);
		Page<DownloadDefenseParaInfo> page = downloadDefenseParaInfoService.findPage(p, downloadDefenseParaInfo);
		model.addAttribute("page", page);
		return "modules/tbmj/downloadDefenseParaInfoList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadDefenseParaInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("tbmj:downloadDefenseParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadDefenseParaInfo downloadDefenseParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadDefenseParaInfo.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadDefenseParaInfo.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadDefenseParaInfo, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadDefenseParaInfo> page = downloadDefenseParaInfoService.findPage(new Page<DownloadDefenseParaInfo>(request, response), downloadDefenseParaInfo);
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

	@RequiresPermissions("tbmj:downloadDefenseParaInfo:view")
	@RequestMapping(value = "form")
	public String form(DownloadDefenseParaInfo downloadDefenseParaInfo, Model model) {
		model.addAttribute("downloadDefenseParaInfo", downloadDefenseParaInfo);
		return "modules/tbmj/downloadDefenseParaInfoForm";
	}

	@RequiresPermissions("tbmj:downloadDefenseParaInfo:edit")
	@RequestMapping(value = "save")
	public String save(DownloadDefenseParaInfo downloadDefenseParaInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadDefenseParaInfo)){
			return form(downloadDefenseParaInfo, model);
		}
		downloadDefenseParaInfoService.save(downloadDefenseParaInfo);
		addMessage(redirectAttributes, "保存工作日同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/tbmj/downloadDefenseParaInfo/?repage";
	}
	
	@RequiresPermissions("tbmj:downloadDefenseParaInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadDefenseParaInfo downloadDefenseParaInfo, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadDefenseParaInfo.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/tbmj/downloadDefenseParaInfo/?repage";
		}
		
		downloadDefenseParaInfoService.delete(downloadDefenseParaInfo);
		addMessage(redirectAttributes, "删除工作日同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/tbmj/downloadDefenseParaInfo/?repage";
	}

}