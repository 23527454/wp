/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.mj.entity.WorkdayParaInfo;
import com.thinkgem.jeesite.modules.mj.service.WorkdayParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * access_workdayController
 * @author demo
 * @version 2020-07-02
 */
@Controller
	@RequestMapping(value = "${adminPath}/mj/workdayParaInfo")
public class WorkdayParaInfoController extends BaseController {

	@Autowired
	private WorkdayParaInfoService workdayParaInfoService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WorkdayParaInfo get(String id) {
		return workdayParaInfoService.get(id);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkdayParaInfo workdayParaInfo, Model model) {



		model.addAttribute("accessWorkday", workdayParaInfo);
		return "modules/mj/workdayParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessWorkday> listData(AccessWorkday accessWorkday, HttpServletRequest request, HttpServletResponse response) {
		accessWorkday.setPage(new Page<>(request, response));
		Page<AccessWorkday> page = accessWorkdayService.findPage(accessWorkday);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping(value = "form")
	public String form(WorkdayParaInfo workdayParaInfo, Model model) {
		model.addAttribute("accessWorkday", workdayParaInfo);
		return "modules/mj/workdayParaInfoForm";
	}

	/**
	 * 保存数据
	 */
	/*@RequiresPermissions("mj:workdayParaInfo:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AccessWorkday accessWorkday) {
		accessWorkdayService.save(accessWorkday);
		return renderResult(Global.TRUE, text("保存access_workday成功！"));
	}*/
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("mj:workdayParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessWorkday accessWorkday) {
		accessWorkdayService.delete(accessWorkday);
		return renderResult(Global.TRUE, text("删除access_workday成功！"));
	}*/
	
}