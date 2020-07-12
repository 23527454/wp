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
import com.thinkgem.jeesite.modules.guard.entity.Tbequipment;
import com.thinkgem.jeesite.modules.guard.service.TbequipmentService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 网点列表表单Controller
 * 
 * @author Jumbo
 * @version 2017-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/tbequipment")
public class TbequipmentController extends BaseController {

	@Autowired
	private TbequipmentService tbequipmentService;
	
	@ModelAttribute
	public Tbequipment get(@RequestParam(required=false) String id) {
		Tbequipment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tbequipmentService.get(id);
		}
		if (entity == null){
			entity = new Tbequipment();
		}
		return entity;
	}
	
	
	@RequiresPermissions("guard:tbequipment:view")
	@RequestMapping(value = {"index"})
	public String index(User user, Model model) {
		return "modules/guard/tbequipmentIndex";
	}


	@RequiresPermissions("guard:tbequipment:view")
	@RequestMapping(value = { "list", "" })
	public String list(Tbequipment tbequipment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tbequipment> page = tbequipmentService.findPage(new Page<Tbequipment>(request, response), tbequipment);
		model.addAttribute("page", page);
		return "modules/guard/tbequipmentList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param tbequipment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:tbequipment:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Tbequipment tbequipment, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			tbequipment.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, Tbequipment.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(tbequipment, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<Tbequipment> page = tbequipmentService.findPage(new Page<Tbequipment>(request, response), tbequipment);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("nequiptype")) {
				String s = DictUtils.getDictLabel(e.getString("nequiptype"), "equip_type", "");
				e.put("nequiptype", s);
			}
			if (e.has("nsitetype")) {
				String s = DictUtils.getDictLabel(e.getString("nsitetype"), "site_type", "");
				e.put("nsitetype", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:tbequipment:view")
	@RequestMapping(value = "form")
	public String form(Tbequipment tbequipment, Model model) {
		model.addAttribute("tbequipment", tbequipment);
		return "modules/guard/tbequipmentForm";
	}

	@RequiresPermissions("guard:tbequipment:edit")
	@RequestMapping(value = "save")
	public String save(Tbequipment tbequipment, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tbequipment)) {
			return form(tbequipment, model);
		}
		tbequipmentService.save(tbequipment);
		addMessage(redirectAttributes, "保存设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/tbequipment/?repage";
	}

	@RequiresPermissions("guard:tbequipment:edit")
	@RequestMapping(value = "delete")
	public String delete(Tbequipment tbequipment, RedirectAttributes redirectAttributes) {
		tbequipmentService.delete(tbequipment);
		addMessage(redirectAttributes, "删除设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/tbequipment/?repage";
	}

	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:tbequipment:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Tbequipment tbequipment, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "设备数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Tbequipment> page = tbequipmentService.findPage(new Page<Tbequipment>(request, response,-1), tbequipment);
			new ExportExcel("设备数据", Tbequipment.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出设备失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/tbequipment/list?repage";
	}
}