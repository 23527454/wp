/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;


import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.mj.entity.SecurityParaInfo;
import com.thinkgem.jeesite.modules.mj.service.SecurityParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * access_antitheftController
 * @author demo
 * @version 2020-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/securityParaInfo")
public class SecurityParaInfoController extends BaseController {

	@Autowired
	private SecurityParaInfoService securityParaInfoService;
	@Autowired
	private EquipmentService equipmentService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public SecurityParaInfo get(@RequestParam(required = false) String id) {
		SecurityParaInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = securityParaInfoService.get(id);
		}
		if (entity == null) {
			entity = new SecurityParaInfo();
		}
		return entity;
	}

	@RequiresPermissions("mj:securityParaInfo:view")
	@RequestMapping("index")
	public String index(SecurityParaInfo securityParaInfo, Model model) {
		return "modules/mj/securityParaInfoIndex";
	}

	/**
	 * 导出门禁参数数据
	 *
	 * @param eid
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("mj:securityParaInfo:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(String eid, HttpServletRequest request, HttpServletResponse response,
							 RedirectAttributes redirectAttributes) {
		try {
			String fileName = "防盗参数信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<SecurityParaInfo> list=securityParaInfoService.getByEId(eid);
			for(int i=0;i<list.size();i++){
				Equipment equipment=equipmentService.get(eid);
				list.get(i).setEquipment(equipment);
			}
			new ExportExcel("防盗参数信息", SecurityParaInfo.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门禁参数信息失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/mj/securityParaInfo/list?equipment.id="+eid;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:securityParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(SecurityParaInfo securityParaInfo, Model model) {
		if(securityParaInfo.getEquipment()!=null && securityParaInfo.getEquipment().getId()!=null && !securityParaInfo.getEquipment().getId().equals("")){
			List<SecurityParaInfo> list= securityParaInfoService.getByEId(securityParaInfo.getEquipment().getId());

			model.addAttribute("list", list);
			model.addAttribute("eid",securityParaInfo.getEquipment().getId());
		}
		return "modules/mj/securityParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:securityParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessAntitheft> listData(AccessAntitheft accessAntitheft, HttpServletRequest request, HttpServletResponse response) {
		accessAntitheft.setPage(new Page<>(request, response));
		Page<AccessAntitheft> page = accessAntitheftService.findPage(accessAntitheft);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("mj:securityParaInfo:view")
	@RequestMapping(value = "form")
	public String form(SecurityParaInfo securityParaInfo, Model model) {
		model.addAttribute("accessAntitheft", securityParaInfo);
		return "modules/mj/securityParaInfoForm";
	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("mj:securityParaInfo:edit")
	@PostMapping(value = "save")
	public String save(@Validated SecurityParaInfo securityParaInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		try {
			securityParaInfoService.save(securityParaInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改门禁信息成功");
		return "redirect:" + Global.getAdminPath() + "/mj/securityParaInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("mj:securityParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessAntitheft accessAntitheft) {
		accessAntitheftService.delete(accessAntitheft);
		return renderResult(Global.TRUE, text("删除access_antitheft成功！"));
	}*/
	
}