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
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessParaInfo;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadAccessParaInfo;
import com.thinkgem.jeesite.modules.tbmj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.tbmj.service.DownloadAccessParaInfoService;
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
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * access_para_infoController
 * @author demo
 * @version 2020-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/accessParaInfo")
public class AccessParaInfoController extends BaseController {

	@Autowired
	private AccessParaInfoService accessParaInfoService;
	@Autowired
	private DictService dictService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private DownloadAccessParaInfoService downloadAccessParaInfoService;

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

	@RequiresPermissions("tbmj:accessParaInfo:view")
	@RequestMapping("index")
	public String index(AccessParaInfo accessParaInfo, Model model) {
		return "modules/tbmj/accessParalnfoIndex";
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
	@RequiresPermissions("tbmj:accessParaInfo:view")
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
		return "redirect:" + adminPath + "/tbmj/accessParaInfo/list?repage";
	}

	@RequiresPermissions("tbmj:accessParaInfo:edit")
	@RequestMapping(value = "download")
	public String download(AccessParaInfo accessParaInfo, String officeId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		accessParaInfo=accessParaInfoService.get(accessParaInfo.getId());

		DownloadAccessParaInfo downloadAccessParaInfo=new DownloadAccessParaInfo();
		downloadAccessParaInfo.setAccessParaInfoId(accessParaInfo.getId());
		downloadAccessParaInfo.setEquipmentId(String.valueOf(accessParaInfo.getEquipmentId()));
		downloadAccessParaInfo.setIsDownload("0");
		downloadAccessParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
		downloadAccessParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
		if(downloadAccessParaInfoService.countByEntity(downloadAccessParaInfo)==0){
			downloadAccessParaInfoService.save(downloadAccessParaInfo);
		}

		addMessage(redirectAttributes, "门禁同步成功");
		return backListPage(accessParaInfo.getId(), model);
	}

	private String backListPage(String accessParaInfoId,Model model) {
		model.addAttribute("accessParaInfo.id", accessParaInfoId);
		if(!StringUtils.isBlank(accessParaInfoId)){
			return "redirect:" + Global.getAdminPath() + "/tbmj/accessParaInfo/list?id="+accessParaInfoId;
		}
		return "redirect:" + Global.getAdminPath() + "/tbmj/accessParaInfo/?repage";
	}



	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:accessParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(AccessParaInfo accessParaInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(accessParaInfo!=null && accessParaInfo.getId()!=null && !accessParaInfo.getId().equals("")){
			accessParaInfo=accessParaInfoService.get(accessParaInfo.getId());
			Equipment equipment=equipmentService.get(String.valueOf(accessParaInfo.getEquipmentId()));
		}

		model.addAttribute("accessParaInfo",accessParaInfo);
		return "modules/tbmj/accessParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("tbmj:accessParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessParaInfo> listData(AccessParaInfo accessParaInfo, HttpServletRequest request, HttpServletResponse response) {
		accessParaInfo.setPage(new Page<>(request, response));
		Page<AccessParaInfo> page = accessParaInfoService.findPage(accessParaInfo);
		return page;
	}*/

	/**
	 * 粘贴数据
	 */
	@RequiresPermissions("tbmj:accessParaInfo:edit")
	@PostMapping(value = "paste")
	//@ResponseBody
	public String paste(AccessParaInfo accessParaInfo, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();

		AccessParaInfo copy_accessParaInfo=(AccessParaInfo)session.getAttribute("copy_accessParaInfo");
		if(copy_accessParaInfo!=null){
			
			accessParaInfo.setDoorRelayTime(copy_accessParaInfo.getDoorRelayTime());
			accessParaInfo.setDoorDelayTime(copy_accessParaInfo.getDoorDelayTime());
			accessParaInfo.setEnterOperaTime(copy_accessParaInfo.getEnterOperaTime());
			accessParaInfo.setCheckOperaTime(copy_accessParaInfo.getCheckOperaTime());
			accessParaInfo.setOutTipsTime(copy_accessParaInfo.getOutTipsTime());
			accessParaInfo.setAlarmIntervalTime(copy_accessParaInfo.getAlarmIntervalTime());
			accessParaInfo.setRemoteOverTime(copy_accessParaInfo.getRemoteOverTime());
			accessParaInfo.setAuthType(copy_accessParaInfo.getAuthType());
			accessParaInfo.setCenterPermit(copy_accessParaInfo.getCenterPermit());
			accessParaInfo.setCombNum(copy_accessParaInfo.getCombNum());
			accessParaInfo.setBase1(copy_accessParaInfo.getBase1());
			accessParaInfo.setBase2(copy_accessParaInfo.getBase2());
			accessParaInfo.setBase3(copy_accessParaInfo.getBase3());
			accessParaInfo.setBase4(copy_accessParaInfo.getBase4());
			accessParaInfo.setBase5(copy_accessParaInfo.getBase5());
			accessParaInfo.setBase6(copy_accessParaInfo.getBase6());
			accessParaInfo.setWorkTime1(copy_accessParaInfo.getWorkTime1());
			accessParaInfo.setWorkTime2(copy_accessParaInfo.getWorkTime2());
			accessParaInfo.setNetOutAge1(copy_accessParaInfo.getNetOutAge1());
			accessParaInfo.setNetOutAge2(copy_accessParaInfo.getNetOutAge2());
			addMessage(model,"粘贴成功!");
		}else{
			addMessage(model,"暂未复制内容!");
		}

		List<Dict> hglist=dictService.findListByType2("high_group","lead_group");
		model.addAttribute("hglist", hglist);
		model.addAttribute("accessParaInfo", accessParaInfo);
		return "modules/tbmj/accessParaInfoForm";
	}

	/**
	 * 复制数据
	 */
	@RequiresPermissions("tbmj:accessParaInfo:edit")
	@PostMapping(value = "copy")
	//@ResponseBody
	public String copy(AccessParaInfo accessParaInfo,HttpServletRequest request,HttpServletResponse response,Model model) {
		HttpSession session=request.getSession();

		int i=0;
		if(!accessParaInfo.getBase1().equals("0")){
			i++;
		}
		if(!accessParaInfo.getBase2().equals("0")){
			i++;
		}
		if(!accessParaInfo.getBase3().equals("0")){
			i++;
		}
		if(!accessParaInfo.getBase4().equals("0")){
			i++;
		}
		if(!accessParaInfo.getBase5().equals("0")){
			i++;
		}
		if(!accessParaInfo.getBase6().equals("0")){
			i++;
		}
		if(i>2||i==0){
			i=2;
		}
		accessParaInfo.setCombNum(i);

		AccessParaInfo copy_accessParaInfo=new AccessParaInfo();
		copy_accessParaInfo.setDoorRelayTime(accessParaInfo.getDoorRelayTime());
		copy_accessParaInfo.setDoorDelayTime(accessParaInfo.getDoorDelayTime());
		copy_accessParaInfo.setEnterOperaTime(accessParaInfo.getEnterOperaTime());
		copy_accessParaInfo.setCheckOperaTime(accessParaInfo.getCheckOperaTime());
		copy_accessParaInfo.setOutTipsTime(accessParaInfo.getOutTipsTime());
		copy_accessParaInfo.setAlarmIntervalTime(accessParaInfo.getAlarmIntervalTime());
		copy_accessParaInfo.setRemoteOverTime(accessParaInfo.getRemoteOverTime());
		copy_accessParaInfo.setAuthType(accessParaInfo.getAuthType());
		copy_accessParaInfo.setCenterPermit(accessParaInfo.getCenterPermit());
		copy_accessParaInfo.setCombNum(accessParaInfo.getCombNum());
		copy_accessParaInfo.setBase1(accessParaInfo.getBase1());
		copy_accessParaInfo.setBase2(accessParaInfo.getBase2());
		copy_accessParaInfo.setBase3(accessParaInfo.getBase3());
		copy_accessParaInfo.setBase4(accessParaInfo.getBase4());
		copy_accessParaInfo.setBase5(accessParaInfo.getBase5());
		copy_accessParaInfo.setBase6(accessParaInfo.getBase6());
		copy_accessParaInfo.setWorkTime1(accessParaInfo.getWorkTime1());
		copy_accessParaInfo.setWorkTime2(accessParaInfo.getWorkTime2());
		copy_accessParaInfo.setNetOutAge1(accessParaInfo.getNetOutAge1());
		copy_accessParaInfo.setNetOutAge2(accessParaInfo.getNetOutAge2());

		session.setAttribute("copy_accessParaInfo",copy_accessParaInfo);

		addMessage(model,"复制成功!");
		model.addAttribute("accessParaInfo",copy_accessParaInfo);
		return "modules/tbmj/accessParaInfoForm";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:accessParaInfo:view")
	@RequestMapping(value = "form")
	public String form(AccessParaInfo accessParaInfo,HttpServletRequest request, Model model) {
		HttpSession session=request.getSession();
		List<Dict> hglist=dictService.findListByType2("high_group","lead_group");
		model.addAttribute("hglist", hglist);
		accessParaInfo=accessParaInfoService.get(accessParaInfo.getId());
		model.addAttribute("accessParaInfo", accessParaInfo);
		return "modules/tbmj/accessParaInfoForm";
	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("tbmj:accessParaInfo:edit")
	@PostMapping(value = "save")
	public String save(@Validated AccessParaInfo accessParaInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		try {
			int i=0;
			if(!accessParaInfo.getBase1().equals("0")){
				i++;
			}
			if(!accessParaInfo.getBase2().equals("0")){
				i++;
			}
			if(!accessParaInfo.getBase3().equals("0")){
				i++;
			}
			if(!accessParaInfo.getBase4().equals("0")){
				i++;
			}
			if(!accessParaInfo.getBase5().equals("0")){
				i++;
			}
			if(!accessParaInfo.getBase6().equals("0")){
				i++;
			}
			if(i>2||i==0){
				i=2;
			}
			accessParaInfo.setCombNum(i);
			accessParaInfoService.save(accessParaInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/tbmj/accessParaInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("tbmj:accessParaInfo:edit")
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