/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.mj.entity.DefenseParaInfo;
import com.thinkgem.jeesite.modules.mj.service.DefenseParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * access_defense_infoController
 * @author demo
 * @version 2020-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/defenseParaInfo")
public class DefenseParaInfoController extends BaseController {

	@Autowired
	private DefenseParaInfoService defenseParaInfoService;



	@RequiresPermissions("mj:defenseParaInfo:view")
	@RequestMapping("index")
	public String index(DefenseParaInfo defenseParaInfo, Model model) {
		return "modules/mj/defenseParaInfoIndex";
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DefenseParaInfo get(String id) {
		return defenseParaInfoService.get(id);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:defenseParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(String eid, Model model) {
		if(eid!=null && !eid.equals("")){
			List<DefenseParaInfo> list= defenseParaInfoService.getByEId(eid);
			model.addAttribute("list", list);
		}
		return "modules/mj/defenseParaInfoList";
	}
	/*@RequiresPermissions("mj:defenseParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(AccessDefenseInfo accessDefenseInfo, Model model) {

		System.out.println("1-------------------------"+accessDefenseInfo.getEquipment()!=null);

		System.out.println("2-------------------------"+accessDefenseInfo.getEquipment().getId()!=null);

		System.out.println("3-------------------------"+(!accessDefenseInfo.getEquipment().getId().equals("")));

		if(accessDefenseInfo.getEquipment()!=null && accessDefenseInfo.getEquipment().getId()!=null && !accessDefenseInfo.getEquipment().getId().equals("")){
			List<AccessDefenseInfo> list=accessDefenseInfoService.getByEId(accessDefenseInfo.getEquipment().getId());

			model.addAttribute("list", list);
		}
		return "modules/mj/defenseParaInfoList";
	}*/
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:defenseParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessDefenseInfo> listData(AccessDefenseInfo accessDefenseInfo, HttpServletRequest request, HttpServletResponse response) {
		accessDefenseInfo.setPage(new Page<>(request, response));
		Page<AccessDefenseInfo> page = accessDefenseInfoService.findPage(accessDefenseInfo);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("mj:defenseParaInfo:view")
	@RequestMapping(value = "form")
	public String form(DefenseParaInfo defenseParaInfo, Model model) {
		model.addAttribute("accessDefenseInfo", defenseParaInfo);
		return "modules/mj/defenseParaInfoForm";
	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("mj:defenseParaInfo:edit")
	@PostMapping(value = "save")
	public String save(@Validated DefenseParaInfo defenseParaInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		try {
			defenseParaInfo.setId2(Integer.parseInt(defenseParaInfo.getId()));
			defenseParaInfoService.save(defenseParaInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改防区信息成功");
		return "redirect:" + Global.getAdminPath() + "/mj/defenseParaInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("mj:defenseParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessDefenseInfo accessDefenseInfo) {
		accessDefenseInfoService.delete(accessDefenseInfo);
		return renderResult(Global.TRUE, text("删除access_defense_info成功！"));
	}*/
	
}