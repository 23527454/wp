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
import com.thinkgem.jeesite.modules.guard.entity.TbDownloadAccessParameters;
import com.thinkgem.jeesite.modules.guard.service.TbDownloadAccessParametersService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 门禁参数同步信息Controller
 * @author zgx
 * @version 2019-01-18
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/tbDownloadAccessParameters")
public class TbDownloadAccessParametersController extends BaseController {

	@Autowired
	private TbDownloadAccessParametersService tbDownloadAccessParametersService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public TbDownloadAccessParameters get(@RequestParam(required=false) String id) {
		TbDownloadAccessParameters entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tbDownloadAccessParametersService.get(id);
		}
		if (entity == null){
			entity = new TbDownloadAccessParameters();
		}
		return entity;
	}
	

	@RequiresPermissions("guard:tbDownloadAccessParameters:view")
	@RequestMapping(value = {"list", ""})
	public String list(TbDownloadAccessParameters tbDownloadAccessParameters, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TbDownloadAccessParameters> p = new Page<TbDownloadAccessParameters>(request, response);
		p.setOrderBy("a.id desc");
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		tbDownloadAccessParameters.setOfficeIds(officeids);
		Page<TbDownloadAccessParameters> page = tbDownloadAccessParametersService.findPage(p, tbDownloadAccessParameters); 
		model.addAttribute("page", page);
		return "modules/guard/tbDownloadAccessParametersList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param TbDownloadAccessParameters
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:tbDownloadAccessParameters:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(TbDownloadAccessParameters tbDownloadAccessParameters, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			tbDownloadAccessParameters.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, TbDownloadAccessParameters.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(tbDownloadAccessParameters, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<TbDownloadAccessParameters> page = tbDownloadAccessParametersService.findPage(new Page<TbDownloadAccessParameters>(request, response), tbDownloadAccessParameters); 
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

	@RequiresPermissions("guard:tbDownloadAccessParameters:view")
	@RequestMapping(value = "form")
	public String form(TbDownloadAccessParameters TbDownloadAccessParameters, Model model) {
		model.addAttribute("TbDownloadAccessParameters", TbDownloadAccessParameters);
		return "modules/guard/tbDownloadAccessParametersForm";
	}

	@RequiresPermissions("guard:tbDownloadAccessParameters:edit")
	@RequestMapping(value = "save")
	public String save(TbDownloadAccessParameters tbDownloadAccessParameters, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tbDownloadAccessParameters)){
			return form(tbDownloadAccessParameters, model);
		}
		tbDownloadAccessParametersService.save(tbDownloadAccessParameters);
		addMessage(redirectAttributes, "保存门禁参数同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbDownloadAccessParameters/?repage";
	}
	
	@RequiresPermissions("guard:tbDownloadAccessParameters:edit")
	@RequestMapping(value = "delete")
	public String delete(TbDownloadAccessParameters tbDownloadAccessParameters, RedirectAttributes redirectAttributes) {
		if("1".equals(tbDownloadAccessParameters.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/tbDownloadAccessParameters/?repage";
		}
		
		tbDownloadAccessParametersService.delete(tbDownloadAccessParameters);
		addMessage(redirectAttributes, "删除门禁参数同步信息成功");
		return "redirect:"+Global.getAdminPath()+"/guard/tbDownloadAccessParameters/?repage";
	}

}