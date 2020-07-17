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
import com.thinkgem.jeesite.modules.mj.entity.AccessParaInfo;
import com.thinkgem.jeesite.modules.mj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
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
 * access_para_infoController
 * @author demo
 * @version 2020-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/accessParaInfo")
public class AccessParaInfoController extends BaseController {

	@Autowired
	private AccessParaInfoService accessParaInfoService;
	@Autowired
	private DictService dictService;
	@Autowired
	private EquipmentService equipmentService;

	/**
	 * 获取数据
	 */@ModelAttribute
	public AccessParaInfo get(@RequestParam(required = false) String id) {
		AccessParaInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = accessParaInfoService.get(id);
		}
		if (entity == null) {
			entity = new AccessParaInfo();
		}
		return entity;
	}

	@RequiresPermissions("mj:accessParaInfo:view")
	@RequestMapping("index")
	public String index(AccessParaInfo accessParaInfo, Model model) {
		return "modules/mj/accessParalnfoIndex";
	}

	/**
	 * 导出门禁参数数据
	 *
	 * @param accessParaInfo
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("mj:accessParaInfo:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(AccessParaInfo accessParaInfo, HttpServletRequest request, HttpServletResponse response,
							 RedirectAttributes redirectAttributes) {
		try {
			String fileName = "门禁参数信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<AccessParaInfo> list=accessParaInfoService.findList(accessParaInfo);
			for(int i=0;i<list.size();i++){
				Equipment equipment=equipmentService.get(String.valueOf(list.get(i).getEquipmentId()));
				list.get(i).setEquipment(equipment);
			}
			new ExportExcel("门禁参数信息", AccessParaInfo.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门禁参数信息失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/mj/accessParaInfo/list?repage";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:accessParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(AccessParaInfo accessParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {

		if(accessParaInfo!=null && accessParaInfo.getId()!=null && !accessParaInfo.getId().equals("")){
			accessParaInfo=accessParaInfoService.get(accessParaInfo.getId());
		}

		model.addAttribute("accessParaInfo",accessParaInfo);
		return "modules/mj/accessParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:accessParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessParaInfo> listData(AccessParaInfo accessParaInfo, HttpServletRequest request, HttpServletResponse response) {
		accessParaInfo.setPage(new Page<>(request, response));
		Page<AccessParaInfo> page = accessParaInfoService.findPage(accessParaInfo);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("mj:accessParaInfo:view")
	@RequestMapping(value = "form")
	public String form(AccessParaInfo accessParaInfo, Model model) {
		accessParaInfo=accessParaInfoService.get(accessParaInfo.getId());
		List<Dict> hglist=dictService.findListByType2("high_group","lead_group");
		model.addAttribute("hglist", hglist);
		model.addAttribute("accessParaInfo", accessParaInfo);
		return "modules/mj/accessParaInfoForm";
	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("mj:accessParaInfo:edit")
	@PostMapping(value = "save")
	public String save(@Validated AccessParaInfo accessParaInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		try {
			int i=0;
			if(!accessParaInfo.getBase1().equals("00")){
				i++;
			}
			if(!accessParaInfo.getBase2().equals("00")){
				i++;
			}
			if(!accessParaInfo.getBase3().equals("00")){
				i++;
			}
			if(!accessParaInfo.getBase4().equals("00")){
				i++;
			}
			if(!accessParaInfo.getBase5().equals("00")){
				i++;
			}
			if(!accessParaInfo.getBase6().equals("00")){
				i++;
			}
			if(i>2){
				i=2;
			}
			accessParaInfo.setCombNum(i);
			accessParaInfoService.save(accessParaInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/mj/accessParaInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("mj:accessParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessParaInfo accessParaInfo, RedirectAttributes redirectAttributes,Model model) {
		accessParaInfoService.delete(accessParaInfo);
		addMessage(redirectAttributes, "删除门禁信息成功");
		//return renderResult(Global.TRUE, text("删除access_para_info成功！"));
	}*/

	/*private String backListPage(Model model) {
		model.addAttribute("selectedOfficeId", selectedOfficeId);
		model.addAttribute("selectedCompanyId", selectedCompanyId);
		if(!StringUtils.isBlank(selectedOfficeId)){
			return "redirect:" + Global.getAdminPath() + "/guard/staff/list?office.id="+selectedOfficeId;
		}else if(!StringUtils.isBlank(selectedCompanyId)){
			return "redirect:" + Global.getAdminPath() + "/guard/staff/list?company.id="+selectedCompanyId;
		}
		return "redirect:" + Global.getAdminPath() + "/guard/staff/?repage";
	}*/
	
}