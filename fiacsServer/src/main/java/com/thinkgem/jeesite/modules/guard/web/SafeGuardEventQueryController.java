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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.SafeGuardEventQuery;
import com.thinkgem.jeesite.modules.guard.service.SafeGuardEventQueryService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 车辆事件查询Controller
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/safeGuardEventQuery")
public class SafeGuardEventQueryController extends BaseController {

	@Autowired
	private SafeGuardEventQueryService safeGuardEventQueryService;
	
	@ModelAttribute
	public SafeGuardEventQuery get(@RequestParam(required=false) String id) {
		SafeGuardEventQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = safeGuardEventQueryService.get(id);
		}
		if (entity == null){
			entity = new SafeGuardEventQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:safeGuardEventQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(SafeGuardEventQuery safeGuardEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<SafeGuardEventQuery> p = new Page<SafeGuardEventQuery>(request, response);
		if (safeGuardEventQuery != null) {
			if ("1".equals(safeGuardEventQuery.getSort()) || safeGuardEventQuery.getSort() == "1") {
				
				String type = safeGuardEventQuery.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				}
			}

			if (safeGuardEventQuery.getSortType() != null) {
				if ("-1".equals(safeGuardEventQuery.getSort()) || safeGuardEventQuery.getSort() == "-1"
						|| safeGuardEventQuery.getSort() == null) {
					
					String type = safeGuardEventQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} 
				}
			}
		}
		Page<SafeGuardEventQuery> page = safeGuardEventQueryService.findPage(p, safeGuardEventQuery); 
		model.addAttribute("page", page);
		return "modules/guard/safeGuardEventQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param safeGuardEventQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:safeGuardEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(SafeGuardEventQuery safeGuardEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			safeGuardEventQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, SafeGuardEventQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(safeGuardEventQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<SafeGuardEventQuery> page = safeGuardEventQueryService.findPage(new Page<SafeGuardEventQuery>(request, response), safeGuardEventQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:safeGuardEventQuery:view")
	@RequestMapping(value = "form")
	public String form(SafeGuardEventQuery safeGuardEventQuery, Model model) {
		model.addAttribute("safeGuardEventQuery", safeGuardEventQuery);
		return "modules/guard/safeGuardEventQueryForm";
	}

	@RequiresPermissions("guard:safeGuardEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(SafeGuardEventQuery safeGuardEventQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, safeGuardEventQuery)){
			return form(safeGuardEventQuery, model);
		}
		safeGuardEventQueryService.save(safeGuardEventQuery);
		addMessage(redirectAttributes, "保存车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/safeGuardEventQuery/?repage";
	}
	
	@RequiresPermissions("guard:safeGuardEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(SafeGuardEventQuery safeGuardEventQuery, RedirectAttributes redirectAttributes) {
		safeGuardEventQueryService.delete(safeGuardEventQuery);
		addMessage(redirectAttributes, "删除车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/safeGuardEventQuery/?repage";
	}
	/**
	 * 导出设备数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:safeGuardEventQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(SafeGuardEventQuery safeGuardEventQuery, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "维保员事件数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<SafeGuardEventQuery> page = safeGuardEventQueryService.findPage(new Page<SafeGuardEventQuery>(request, response, -1), safeGuardEventQuery);
			new ExportExcel("维保员事件数据", SafeGuardEventQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出维保员事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/safeGuardEventQuery/list?repage";
	}

	
}