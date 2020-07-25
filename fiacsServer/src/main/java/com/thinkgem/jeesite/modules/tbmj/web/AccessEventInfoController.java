/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessEventInfo;
import com.thinkgem.jeesite.modules.tbmj.service.AccessEventInfoService;
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
@RequestMapping(value = "${adminPath}/tbmj/accessEventInfo")
public class AccessEventInfoController extends BaseController {

	@Autowired
	private AccessEventInfoService accessEventInfoService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AccessEventInfo get(String id) {
		return accessEventInfoService.get(id);
	}

	@RequiresPermissions("tbmj:accessEventInfo:view")
	@RequestMapping(value = { "index" })
	public String index(AccessEventInfo accessEventInfo, Model model) {
		return "modules/tbmj/accessEventInfoIndex";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:accessEventInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(AccessEventInfo accessEventInfo,String nodes, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(accessEventInfo==null){
			accessEventInfo=new AccessEventInfo();
			if(nodes==null || nodes.equals("")){
				nodes="-1";
			}
			accessEventInfo.setNodes(nodes);
		}
		Page<AccessEventInfo> p = new Page<AccessEventInfo>(request, response);
		p.setOrderBy("a.id desc");
		p.setPageSize(15);
		Page<AccessEventInfo> page = accessEventInfoService.findPage(p, accessEventInfo);

		model.addAttribute("page", page);
		model.addAttribute("nodes", accessEventInfo.getNodes());
		/*List<TtsSetting> ttss = ttsService.getByType("doorControl");
		if(ttss.size()>0){
			model.addAttribute("tts", ttss.get(0).getVoiceConfig());
		}*/
		model.addAttribute("accessEventInfo", accessEventInfo);
		return "modules/tbmj/accessEventInfoList";
	}

	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("tbmj:accessEventInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessEventInfo> listData(AccessEventInfo accessEventInfo, HttpServletRequest request, HttpServletResponse response) {
		accessEventInfo.setPage(new Page<>(request, response));
		Page<AccessEventInfo> page = accessEventInfoService.findPage(accessEventInfo);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:accessEventInfo:view")
	@RequestMapping(value = "form")
	public String form(AccessEventInfo accessEventInfo, Model model) {
		model.addAttribute("accessEventInfo", accessEventInfo);
		return "modules/tbmj/accessEventInfoForm";
	}

	/**
	 * 保存数据
	 */
	/*@RequiresPermissions("tbmj:accessEventInfo:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AccessEventInfo accessEventInfo) {
		accessEventInfoService.save(accessEventInfo);
		return renderResult(Global.TRUE, text("保存tbmj_access_event_info成功！"));
	}*/

	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("tbmj:accessEventInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessEventInfo accessEventInfo) {
		accessEventInfoService.delete(accessEventInfo);
		return renderResult(Global.TRUE, text("删除tbmj_access_event_info成功！"));
	}*/

}