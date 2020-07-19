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
import com.thinkgem.jeesite.modules.mj.entity.DownloadWorkdayParaInfo;
import com.thinkgem.jeesite.modules.mj.service.DownloadWorkdayParaInfoService;
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
 * 工作日同步信息Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/downloadWorkdayParaInfo")
public class DownloadWorkdayParaInfoController extends BaseController {

	@Autowired
	private DownloadWorkdayParaInfoService downloadWorkdayParaInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public DownloadWorkdayParaInfo get(@RequestParam(required=false) String id) {
		DownloadWorkdayParaInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = downloadWorkdayParaInfoService.get(id);
		}
		if (entity == null){
			entity = new DownloadWorkdayParaInfo();
		}
		return entity;
	}
	

	@RequiresPermissions("mj:downloadWorkdayParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(DownloadWorkdayParaInfo downloadWorkdayParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {

		System.out.println("000000000000000000000000000000000000000000000000000");
		Page<DownloadWorkdayParaInfo> p = new Page<DownloadWorkdayParaInfo>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadWorkdayParaInfo.setOfficeIds(officeids);
		Page<DownloadWorkdayParaInfo> page = downloadWorkdayParaInfoService.findPage(p, downloadWorkdayParaInfo); 
		model.addAttribute("page", page);
		return "modules/mj/downloadWorkdayParaInfoList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param downloadWorkdayParaInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mj:downloadWorkdayParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadWorkdayParaInfo downloadWorkdayParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			downloadWorkdayParaInfo.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, DownloadWorkdayParaInfo.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(downloadWorkdayParaInfo, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<DownloadWorkdayParaInfo> page = downloadWorkdayParaInfoService.findPage(new Page<DownloadWorkdayParaInfo>(request, response), downloadWorkdayParaInfo); 
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

	@RequiresPermissions("mj:downloadWorkdayParaInfo:view")
	@RequestMapping(value = "form")
	public String form(DownloadWorkdayParaInfo downloadWorkdayParaInfo, Model model) {
		model.addAttribute("downloadWorkdayParaInfo", downloadWorkdayParaInfo);
		return "modules/mj/downloadWorkdayParaInfoForm";
	}

	@RequiresPermissions("mj:downloadWorkdayParaInfo:edit")
	@RequestMapping(value = "save")
	public String save(DownloadWorkdayParaInfo downloadWorkdayParaInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadWorkdayParaInfo)){
			return form(downloadWorkdayParaInfo, model);
		}
		downloadWorkdayParaInfoService.save(downloadWorkdayParaInfo);
		addMessage(redirectAttributes, "保存工作日同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/mj/downloadWorkdayParaInfo/?repage";
	}
	
	@RequiresPermissions("mj:downloadWorkdayParaInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadWorkdayParaInfo downloadWorkdayParaInfo, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadWorkdayParaInfo.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/mj/downloadWorkdayParaInfo/?repage";
		}
		
		downloadWorkdayParaInfoService.delete(downloadWorkdayParaInfo);
		addMessage(redirectAttributes, "删除工作日同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/mj/downloadWorkdayParaInfo/?repage";
	}

}