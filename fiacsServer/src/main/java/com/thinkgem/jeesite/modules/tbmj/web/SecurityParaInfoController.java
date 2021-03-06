/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;


import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadSecurityParaInfo;
import com.thinkgem.jeesite.modules.tbmj.entity.SecurityParaInfo;
import com.thinkgem.jeesite.modules.tbmj.service.DownloadSecurityParaInfoService;
import com.thinkgem.jeesite.modules.tbmj.service.SecurityParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * access_antitheftController
 * @author demo
 * @version 2020-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/securityParaInfo")
public class SecurityParaInfoController extends BaseController {

	@Autowired
	private SecurityParaInfoService securityParaInfoService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private DownloadSecurityParaInfoService downloadSecurityParaInfoService;
	
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

	@RequiresPermissions("tbmj:securityParaInfo:view")
	@RequestMapping("index")
	public String index(SecurityParaInfo securityParaInfo, Model model) {
		return "modules/tbmj/securityParaInfoIndex";
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
	@RequiresPermissions("tbmj:securityParaInfo:view")
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
		return "redirect:" + adminPath + "/tbmj/securityParaInfo/list?equipment.id="+eid;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:securityParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(String oId, Model model,RedirectAttributes redirectAttributes) {
		if(oId!=null && !oId.equals("")){
			Equipment equipment=equipmentService.getByOfficeId(oId);
			if(equipment!=null){
				List<SecurityParaInfo> list= securityParaInfoService.getByEId(equipment.getId());

				model.addAttribute("list", list);
				model.addAttribute("oid",oId);
			}else{
				addMessage(model,"该机构下暂未添加设备信息！");
			}
		}
		return "modules/tbmj/securityParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("tbmj:securityParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessAntitheft> listData(AccessAntitheft accessAntitheft, HttpServletRequest request, HttpServletResponse response) {
		accessAntitheft.setPage(new Page<>(request, response));
		Page<AccessAntitheft> page = accessAntitheftService.findPage(accessAntitheft);
		return page;
	}*/

	@RequiresPermissions("tbmj:securityParaInfo:edit")
	@RequestMapping(value = "download")
	@Transactional
	public String download(String oId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Equipment equipment=equipmentService.getByOfficeId(oId);
		String eid=equipment.getId();
		SecurityParaInfo securityParaInfo =new SecurityParaInfo();
		securityParaInfo.setEquipment(equipmentService.get(eid));
		List<SecurityParaInfo> list= securityParaInfoService.getByEId(eid);
		for(int i=0;i<list.size();i++){
			SecurityParaInfo securityParaInfo2 = securityParaInfoService.get(list.get(i));
			DownloadSecurityParaInfo downloadSecurityParaInfo=new DownloadSecurityParaInfo();
			downloadSecurityParaInfo.setSecurityParaInfoId(securityParaInfo2.getId());
			downloadSecurityParaInfo.setEquipmentId(eid);
			downloadSecurityParaInfo.setIsDownload("0");
			downloadSecurityParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadSecurityParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadSecurityParaInfoService.countByEntity(downloadSecurityParaInfo)==0){
				downloadSecurityParaInfoService.save(downloadSecurityParaInfo);
			}
		}
		addMessage(redirectAttributes, "防盗参数同步成功");
		return backListPage(oId, model);
	}

	private String backListPage(String oId,Model model) {
		model.addAttribute("oId",oId);
		if(oId!=null && !oId.equals("")){
			return "redirect:" + Global.getAdminPath() + "/tbmj/securityParaInfo/list?oId="+oId+"&repage";
		}
		return "redirect:" + Global.getAdminPath() + "/tbmj/securityParaInfo/?repage";
	}

	/**
	 * 粘贴数据
	 */
	@RequiresPermissions("tbmj:securityParaInfo:edit")
	@PostMapping(value = "paste")
	//@ResponseBody
	public String paste(SecurityParaInfo securityParaInfo, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		SecurityParaInfo copy_securityParaInfo=(SecurityParaInfo)session.getAttribute("copy_securityParaInfo");
		if(copy_securityParaInfo!=null){
			securityParaInfo.setLeaveRelayTime(copy_securityParaInfo.getLeaveRelayTime());
			securityParaInfo.setDoorSensorTime(copy_securityParaInfo.getDoorSensorTime());
			securityParaInfo.setLocalTipsTime(copy_securityParaInfo.getLocalTipsTime());
			securityParaInfo.setTipsAlarmTime(copy_securityParaInfo.getTipsAlarmTime());
			securityParaInfo.setAlarmIntervalTime(copy_securityParaInfo.getAlarmIntervalTime());
			securityParaInfo.setAllowRemoteClose(copy_securityParaInfo.getAllowRemoteClose());
			securityParaInfo.setAllowDoorSensorOpen(copy_securityParaInfo.getAllowDoorSensorOpen());
			securityParaInfo.setAllowDoorButtonOpen(copy_securityParaInfo.getAllowDoorButtonOpen());
			securityParaInfo.setAllowButtonOpen(copy_securityParaInfo.getAllowButtonOpen());
			securityParaInfo.setAllowAuthClose(copy_securityParaInfo.getAllowAuthClose());
			securityParaInfo.setAllowButtonClose(copy_securityParaInfo.getAllowButtonClose());
			securityParaInfo.setAllowAuthRelieve(copy_securityParaInfo.getAllowAuthRelieve());
			securityParaInfo.setAllowButtonRelieve(copy_securityParaInfo.getAllowButtonRelieve());
			securityParaInfo.setAllowPowerAlarm(copy_securityParaInfo.getAllowPowerAlarm());
			securityParaInfo.setAllowBatteryAlarm(copy_securityParaInfo.getAllowBatteryAlarm());
			securityParaInfo.setRemarks(copy_securityParaInfo.getRemarks());
			addMessage(model,"粘贴成功!");
		}else{
			addMessage(model,"暂未复制内容!");
		}
		model.addAttribute("accessAntitheft", securityParaInfo);
		return "modules/tbmj/securityParaInfoForm";
	}


	/**
	 * 复制数据
	 */
	@RequiresPermissions("tbmj:securityParaInfo:edit")
	@PostMapping(value = "copy")
	//@ResponseBody
	public String copy(SecurityParaInfo securityParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session=request.getSession();

		/*SecurityParaInfo copy_securityParaInfo=new SecurityParaInfo();
		copy_securityParaInfo.setDefensePos(securityParaInfo.getDefensePos());
		copy_securityParaInfo.setDefenseAreaType(securityParaInfo.getDefenseAreaType());
		copy_securityParaInfo.setDefenseAreaBypass(securityParaInfo.getDefenseAreaBypass());
		copy_securityParaInfo.setDefenseAreaAttr(securityParaInfo.getDefenseAreaAttr());
		copy_securityParaInfo.setAlarmDelayTime(securityParaInfo.getAlarmDelayTime());
		copy_securityParaInfo.setTimeframe(securityParaInfo.getTimeframe());
		copy_securityParaInfo.setRemarks(securityParaInfo.getRemarks());*/
		session.setAttribute("copy_securityParaInfo",securityParaInfo);
		addMessage(model,"复制成功!");
		model.addAttribute("securityParaInfo",securityParaInfo);
		return "modules/tbmj/securityParaInfoForm";
		//return true;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:securityParaInfo:view")
	@RequestMapping(value = "form")
	public String form(SecurityParaInfo securityParaInfo, Model model) {
		model.addAttribute("accessAntitheft", securityParaInfo);
		return "modules/tbmj/securityParaInfoForm";
	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("tbmj:securityParaInfo:edit")
	@PostMapping(value = "save")
	public String save(@Validated SecurityParaInfo securityParaInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		try {
			securityParaInfoService.save(securityParaInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改门禁信息成功");
		return "redirect:" + Global.getAdminPath() + "/tbmj/securityParaInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("tbmj:securityParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessAntitheft accessAntitheft) {
		accessAntitheftService.delete(accessAntitheft);
		return renderResult(Global.TRUE, text("删除access_antitheft成功！"));
	}*/
	
}