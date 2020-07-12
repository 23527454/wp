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
import com.thinkgem.jeesite.modules.guard.entity.DownloadCar;
import com.thinkgem.jeesite.modules.guard.service.DownloadCarService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 车辆同步信息Controller
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/downloadCar")
public class DownloadCarController extends BaseController {

	@Autowired
	private DownloadCarService downloadCarService;
	@Autowired
	private OfficeService officeService;
	@ModelAttribute
	public DownloadCar get(@RequestParam(required = false) String id) {
		DownloadCar entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = downloadCarService.get(id);
		}
		if (entity == null) {
			entity = new DownloadCar();
		}
		return entity;
	}

	@RequiresPermissions("guard:downloadCar:view")
	@RequestMapping(value = { "list", "" })
	public String list(DownloadCar downloadCar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DownloadCar> p = new Page<DownloadCar>(request, response);
		p.setOrderBy("a.id desc");
		
		List<String> officeids = new ArrayList<String>();
		List<Office> list = officeService.findList(false, new Office());
		for (Office office : list) {
			officeids.add(office.getId());
		}
		downloadCar.setOfficeIds(officeids);
		
		Page<DownloadCar> page = downloadCarService.findPage(p, downloadCar);
		model.addAttribute("page", page);
		return "modules/guard/downloadCarList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param downloadCar
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:downloadCar:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(DownloadCar downloadCar, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			downloadCar.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, DownloadCar.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(downloadCar, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<DownloadCar> page = downloadCarService.findPage(new Page<DownloadCar>(request, response), downloadCar);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("isDownload")) {
				String s = DictUtils.getDictLabel(e.getString("isDownload"), "isDownload", "");
				e.put("isDownload", s);
			}
			if (e.has("downloadType")) {
				String s = DictUtils.getDictLabel(e.getString("downloadType"), "downloadType", "");
				e.put("downloadType", s);
			}
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:downloadCar:view")
	@RequestMapping(value = "form")
	public String form(DownloadCar downloadCar, Model model) {
		model.addAttribute("downloadCar", downloadCar);
		return "modules/guard/downloadCarForm";
	}

	@RequiresPermissions("guard:downloadCar:edit")
	@RequestMapping(value = "save")
	public String save(DownloadCar downloadCar, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, downloadCar)) {
			return form(downloadCar, model);
		}
		downloadCarService.save(downloadCar);
		addMessage(redirectAttributes, "保存车辆同步信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/downloadCar/?repage";
	}

	@RequiresPermissions("guard:downloadCar:edit")
	@RequestMapping(value = "delete")
	public String delete(DownloadCar downloadCar, RedirectAttributes redirectAttributes) {
		if("1".equals(downloadCar.getIsDownload())){
			addMessage(redirectAttributes, "记录已经同步成功, 不能删除!");
			return "redirect:"+Global.getAdminPath()+"/guard/downloadPerson/?repage";
		}
		
		downloadCarService.delete(downloadCar);
		addMessage(redirectAttributes, "删除车辆同步信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/downloadCar/?repage";
	}

}