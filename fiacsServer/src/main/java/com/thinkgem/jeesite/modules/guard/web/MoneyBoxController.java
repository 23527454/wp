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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxAllotService;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 款箱信息Controller
 * @author Jumbo
 * @version 2017-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/moneyBox")
public class MoneyBoxController extends BaseController {

	@Autowired
	private MoneyBoxService moneyBoxService;
	@Autowired
	private MoneyBoxAllotService moneyBoxAllotService;
	@Autowired
	private OfficeService officeService;
	
	
	private Office office = new Office();
	
	@ModelAttribute
	public MoneyBox get(@RequestParam(required=false) String id) {
		MoneyBox entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moneyBoxService.get(id);
		}
		if (entity == null){
			entity = new MoneyBox();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:moneyBox:view")
	@RequestMapping(value = { "index" })
	public String index(MoneyBox moneyBox, Model model) {
		return "modules/guard/moneyBoxIndex";
	}
	
	
	@RequiresPermissions("guard:moneyBox:view")
	@RequestMapping(value = {"list", ""})
	public String list(MoneyBox moneyBox, HttpServletRequest request, HttpServletResponse response, Model model) {
		

		if(moneyBox.getOffice()==null) {
			moneyBox.setOffice(office);
		}else {
			office = moneyBox.getOffice();
		}
		
		Page<MoneyBox> page = moneyBoxService.findPage(new Page<MoneyBox>(request, response), moneyBox); 
		model.addAttribute("page", page);
		model.addAttribute("moneyBox", moneyBox);
		return "modules/guard/moneyBoxList";
	}
	
	@RequiresPermissions("guard:moneyBox:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(MoneyBox moneyBox, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		try {
			String fileName = "款箱数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<MoneyBox> page = moneyBoxService.findPage(new Page<MoneyBox>(request, response, -1), moneyBox);
			new ExportExcel("款箱数据", MoneyBox.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出款箱数据失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/moneyBox/list?repage";
	}

	
	/**
	 * 获取列表数据（JSON）
	 * @param moneyBox
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:moneyBox:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(MoneyBox moneyBox, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			moneyBox.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, MoneyBox.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(moneyBox, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<MoneyBox> page = moneyBoxService.findPage(new Page<MoneyBox>(request, response), moneyBox); 
        //json数据
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
	        		if(e.has("boxType")){
	        			String s = DictUtils.getDictLabel(e.getString("boxType"),"box_type", "");
	        			e.put("boxType",s);
					}
	        		if(e.has("doorStatus")){
	        			String s = DictUtils.getDictLabel(e.getString("doorStatus"),"door_status", "");
	        			e.put("doorStatus",s);
					}
	        		if(e.has("delFlag")){
	        			String s = DictUtils.getDictLabel(e.getString("delFlag"),"del_flag", "");
	        			e.put("delFlag",s);
					}
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:moneyBox:view")
	@RequestMapping(value = "form")
	public String form(MoneyBox moneyBox, Model model) {
		if(null != moneyBox.getOffice() && !org.apache.commons.lang.StringUtils.isBlank(moneyBox.getOffice().getId())) {
			moneyBox.setOffice(officeService.get(moneyBox.getOffice().getId()));
		}
		return backForm(moneyBox, model);
	}

	private String backForm(MoneyBox moneyBox, Model model) {
		model.addAttribute("moneyBox", moneyBox);
		return "modules/guard/moneyBoxForm";
	}

	@RequiresPermissions("guard:moneyBox:edit")
	@RequestMapping(value = "download")
	public String download(MoneyBox moneyBox, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId")String selectedOfficeId) {
		if (!beanValidator(model, moneyBox)) {
			return form(moneyBox, model);
		}
		moneyBoxService.insentDownload(moneyBox, DownloadEntity.DOWNLOAD_TYPE_ADD);
		addMessage(redirectAttributes, "款箱同步成功");
		return backListPage(selectedOfficeId);
	}

	private String backListPage(String selectedOfficeId) {
		if(!StringUtils.isBlank(selectedOfficeId)){
			return "redirect:" + Global.getAdminPath() + "/guard/moneyBox/list?office.id="+selectedOfficeId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/moneyBox/?repage";
		}
	}
	
	@RequiresPermissions("guard:moneyBox:edit")
	@RequestMapping(value = "save")
	public String save(MoneyBox moneyBox,BindingResult result, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId")String selectedOfficeId) {
		if (!beanValidator(model, moneyBox)){
			return form(moneyBox, model);
		}
		if(moneyBoxService.countByBoxCode(moneyBox.getId(), moneyBox.getBoxCode()) > 0) {
			result.rejectValue("boxCode", "duplicate", "款箱编码已经存在");
		}
		if(moneyBoxService.countByCardNum(moneyBox.getId(), moneyBox.getCardNum()) > 0) {
			result.rejectValue("cardNum", "duplicate", "款箱卡号已经存在");
		}
		if(result.hasErrors()) {
			return backForm(moneyBox, model);
		}
		try {
			moneyBoxService.save(moneyBox);
		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存款箱信息成功");
		
		if(null != selectedOfficeId && selectedOfficeId.equals(moneyBox.getOffice().getId())){
			return "redirect:"+Global.getAdminPath()+"/guard/moneyBox/list?office.id=" + selectedOfficeId;
		}else{
			return "redirect:"+Global.getAdminPath()+"/guard/moneyBox/?repage";
		}
	}
	
	@RequiresPermissions("guard:moneyBox:edit")
	@RequestMapping(value = "delete")
	public String delete(MoneyBox moneyBox, RedirectAttributes redirectAttributes,
			@RequestParam("selectedOfficeId")String selectedOfficeId) {
		if(deletePD(moneyBox)){
			moneyBoxService.delete(moneyBox);
			addMessage(redirectAttributes, "删除款箱信息成功");
		}else{
			addMessage(redirectAttributes, "不可删除已调拨款箱");
		}
		return backListPage(selectedOfficeId);
	}
	
	private boolean deletePD(MoneyBox moneyBox) {
		List<MoneyBoxAllot> moneyBoxAllotList = moneyBoxAllotService.findListdel(new MoneyBoxAllot());
		for (int i = 0; i < moneyBoxAllotList.size(); i++) {
			if (moneyBoxAllotList.get(i).getMoneyBoxId().equals(moneyBox.getId())) {
				return false;
			}
		}
		return true;
	}
	

}