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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.CarEventQuery;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.CarEventQueryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 车辆事件查询Controller
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/carEventQuery")
public class CarEventQueryController extends BaseController {

	@Autowired
	private CarEventQueryService carEventQueryService;
	
	@ModelAttribute
	public CarEventQuery get(@RequestParam(required=false) String id) {
		CarEventQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = carEventQueryService.get(id);
		}
		if (entity == null){
			entity = new CarEventQuery();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:carEventQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(CarEventQuery carEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<CarEventQuery> p = new Page<CarEventQuery>(request, response);
		if (carEventQuery != null) {
			if ("1".equals(carEventQuery.getSort()) || carEventQuery.getSort() == "1") {
				
				String type = carEventQuery.getSortType();
				if ("1".equals(type)) {
					p.setOrderBy("a.equipment_id asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("a.time asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("a9.card_num asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("a9.carplate asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("a9.admin asc");
				} 
			}

			if (carEventQuery.getSortType() != null) {
				if ("-1".equals(carEventQuery.getSort()) || carEventQuery.getSort() == "-1"
						|| carEventQuery.getSort() == null) {
					
					String type = carEventQuery.getSortType();
					if ("1".equals(type)) {
						p.setOrderBy("a.equipment_id desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("a.time desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("a9.card_num desc");
					} else if ("4".equals(type)) {
						p.setOrderBy("a9.carplate desc");
					} else if ("5".equals(type)) {
						p.setOrderBy("a9.admin desc");
					} 
				}
			}
		}
		Page<CarEventQuery> page = carEventQueryService.findPage(p, carEventQuery); 
		model.addAttribute("page", page);
		return "modules/guard/carEventQueryList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param carEventQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:carEventQuery:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(CarEventQuery carEventQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			carEventQuery.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, CarEventQuery.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(carEventQuery, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<CarEventQuery> page = carEventQueryService.findPage(new Page<CarEventQuery>(request, response), carEventQuery); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:carEventQuery:view")
	@RequestMapping(value = "form")
	public String form(CarEventQuery carEventQuery, Model model) {
		model.addAttribute("carEventQuery", carEventQuery);
		return "modules/guard/carEventQueryForm";
	}

	@RequiresPermissions("guard:carEventQuery:edit")
	@RequestMapping(value = "save")
	public String save(CarEventQuery carEventQuery, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, carEventQuery)){
			return form(carEventQuery, model);
		}
		carEventQueryService.save(carEventQuery);
		addMessage(redirectAttributes, "保存车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/carEventQuery/?repage";
	}
	
	@RequiresPermissions("guard:carEventQuery:edit")
	@RequestMapping(value = "delete")
	public String delete(CarEventQuery carEventQuery, RedirectAttributes redirectAttributes) {
		carEventQueryService.delete(carEventQuery);
		addMessage(redirectAttributes, "删除车辆事件查询成功");
		return "redirect:"+Global.getAdminPath()+"/guard/carEventQuery/?repage";
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
	@RequiresPermissions("guard:carEventQuery:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(CarEventQuery carEventQuery, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "车辆事件数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<CarEventQuery> page = carEventQueryService.findPage(new Page<CarEventQuery>(request, response, -1), carEventQuery);
			new ExportExcel("车辆事件数据", CarEventQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出车辆事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/carEventQuery/list?repage";
	}

	
}