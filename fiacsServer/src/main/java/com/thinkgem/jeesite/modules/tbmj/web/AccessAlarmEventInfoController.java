/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessAlarmEventInfo;
import com.thinkgem.jeesite.modules.tbmj.service.AccessAlarmEventInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * tbmj_access_event_infoController
 * @author demo
 * @version 2020-07-25
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/accessAlarmEventInfo")
public class AccessAlarmEventInfoController extends BaseController {

	@Autowired
	private AccessAlarmEventInfoService accessAlarmEventInfoService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AccessAlarmEventInfo get(String id) {
		return accessAlarmEventInfoService.get(id);
	}

	@RequiresPermissions("tbmj:accessAlarmEventInfo:view")
	@RequestMapping(value = { "index" })
	public String index(AccessAlarmEventInfo accessAlarmEventInfo, Model model) {
		return "modules/tbmj/accessAlarmEventInfoIndex";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:accessAlarmEventInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(AccessAlarmEventInfo accessAlarmEventInfo,String nodes, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(accessAlarmEventInfo==null){
			accessAlarmEventInfo=new AccessAlarmEventInfo();
			if(nodes==null || nodes.equals("")){
				nodes="-1";
			}
			accessAlarmEventInfo.setNodes(nodes);
		}
		Page<AccessAlarmEventInfo> p = new Page<AccessAlarmEventInfo>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(15);
		Page<AccessAlarmEventInfo> page = accessAlarmEventInfoService.findPage(p, accessAlarmEventInfo);

		model.addAttribute("page", page);
		model.addAttribute("nodes", accessAlarmEventInfo.getNodes());
		/*List<TtsSetting> ttss = ttsService.getByType("doorControl");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}*/
		model.addAttribute("accessAlarmEventInfo", accessAlarmEventInfo);
		return "modules/tbmj/accessAlarmEventInfoList";
	}

	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("tbmj:accessAlarmEventInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessAlarmEventInfo> listData(AccessAlarmEventInfo accessAlarmEventInfo, HttpServletRequest request, HttpServletResponse response) {
		accessAlarmEventInfo.setPage(new Page<>(request, response));
		Page<AccessAlarmEventInfo> page = accessAlarmEventInfoService.findPage(accessAlarmEventInfo);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:accessAlarmEventInfo:view")
	@RequestMapping(value = "form")
	public String form(AccessAlarmEventInfo accessAlarmEventInfo, Model model) {
		model.addAttribute("accessAlarmEventInfo", accessAlarmEventInfo);
		return "modules/tbmj/accessAlarmEventInfoForm";
	}

	/**
	 * 保存数据
	 */
	/*@RequiresPermissions("tbmj:accessAlarmEventInfo:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AccessAlarmEventInfo accessAlarmEventInfo) {
		accessAlarmEventInfoService.save(accessAlarmEventInfo);
		return renderResult(Global.TRUE, text("保存tbmj_access_event_info成功！"));
	}*/

	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("tbmj:accessAlarmEventInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessAlarmEventInfo accessAlarmEventInfo) {
		accessAlarmEventInfoService.delete(accessAlarmEventInfo);
		return renderResult(Global.TRUE, text("删除tbmj_access_event_info成功！"));
	}*/

}