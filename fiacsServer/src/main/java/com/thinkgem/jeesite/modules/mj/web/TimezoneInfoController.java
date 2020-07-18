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
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.mj.entity.AccessParaInfo;
import com.thinkgem.jeesite.modules.mj.entity.DownloadTimezoneInfo;
import com.thinkgem.jeesite.modules.mj.entity.TimezoneInfo;
import com.thinkgem.jeesite.modules.mj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.mj.service.DownloadTimezoneInfoService;
import com.thinkgem.jeesite.modules.mj.service.TimezoneInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
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
 * access_door_timezoneController
 * @author demo
 * @version 2020-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/timezoneInfo")
public class TimezoneInfoController extends BaseController {

	@Autowired
	private TimezoneInfoService timezoneInfoService;
	@Autowired
	private AccessParaInfoService accessParaInfoService;
	@Autowired
	private DictService dictService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private DownloadTimezoneInfoService downloadTimezoneInfoService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TimezoneInfo get(@RequestParam(required = false) String id) {
		TimezoneInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = timezoneInfoService.get(id);
		}
		if (entity == null) {
			entity = new TimezoneInfo();
		}
		return entity;
	}

	@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping("index")
	public String index(TimezoneInfo timezoneInfo, Model model) {
		return "modules/mj/timezoneInfoIndex";
	}

	/**
	 * 导出门禁时区数据
	 *
	 * @param timezoneInfo
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(TimezoneInfo timezoneInfo, HttpServletRequest request, HttpServletResponse response,
							 RedirectAttributes redirectAttributes) {
		try {
			String fileName = "时区信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<TimezoneInfo> list=timezoneInfoService.findList(timezoneInfo);
			for(int i=0;i<list.size();i++){
				Equipment equipment=equipmentService.get(String.valueOf(list.get(i).getEquipmentId()));
				list.get(i).setEquipment(equipment);
			}
			new ExportExcel("时区信息", TimezoneInfo.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出时区信息失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/mj/timezoneInfo/list?repage";
	}

	@RequiresPermissions("mj:timezoneInfo:edit")
	@RequestMapping(value = "download")
	@Transactional
	public String download(TimezoneInfo timezoneInfo, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if(timezoneInfo==null){
			timezoneInfo=new TimezoneInfo();
			AccessParaInfo accessParaInfo=accessParaInfoService.get(timezoneInfo.getAccessParaInfoId());
			timezoneInfo.setAccessParaInfo(accessParaInfo);
		}
		List<TimezoneInfo> list=timezoneInfoService.findList(timezoneInfo);
		for(int i=0;i<list.size();i++){
			TimezoneInfo timezoneInfo2=timezoneInfoService.get(list.get(i));
			DownloadTimezoneInfo downloadTimezoneInfo=new DownloadTimezoneInfo();
			downloadTimezoneInfo.setTimezoneInfoId(timezoneInfo2.getId());
			downloadTimezoneInfo.setAccessParaInfoId(timezoneInfo2.getAccessParaInfoId());
			downloadTimezoneInfo.setEquipment(equipmentService.get(String.valueOf(timezoneInfo2.getEquipmentId())));
			downloadTimezoneInfo.setIsDownload("0");
			downloadTimezoneInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadTimezoneInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadTimezoneInfoService.countByEntity(downloadTimezoneInfo)==0){
				downloadTimezoneInfoService.save(downloadTimezoneInfo);
			}
		}
		addMessage(redirectAttributes, "时区同步成功");
		return backListPage(timezoneInfo, model);
	}

	private String backListPage(TimezoneInfo timezoneInfo,Model model) {
		model.addAttribute("timezoneInfo",timezoneInfo);
		if(timezoneInfo!=null){
			return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/list?accessParaInfoId="+timezoneInfo.getAccessParaInfoId()+"&timeZoneType="+timezoneInfo.getTimeZoneType()+"&timeZoneNum="+timezoneInfo.getTimeZoneNum();
		}
		return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/?repage";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(TimezoneInfo timezoneInfo, Model model) {
		//通过tree传过来的门禁id查找这个门的信息
		if (timezoneInfo.getTimeZoneType()==null){
			timezoneInfo.setTimeZoneType("1");
		}
		if(timezoneInfo.getTimeZoneNum()==null){
			timezoneInfo.setTimeZoneNum("1");
		}

		List<TimezoneInfo> list=null;
		if(timezoneInfo.getAccessParaInfoId()!=null && !timezoneInfo.getAccessParaInfoId().equals("")){
			list= timezoneInfoService.findList(timezoneInfo);
			model.addAttribute("accessParaInfoId", timezoneInfo.getAccessParaInfoId());
		}

		Dict dict=new Dict();
		dict.setType("time_zone_type");
		List<Dict> timeZoneType=dictService.findList(dict);
		dict.setType("time_zone_num");
		List<Dict> timeZoneNum=dictService.findList(dict);

		model.addAttribute("dictService", dictService);
		model.addAttribute("dictService", dictService);
		model.addAttribute("list", list);
		return "modules/mj/timezoneInfoList";
	}

	/**
	 * 查看编辑表单，修改时进入
	 */
	@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = "form")
	public String form(TimezoneInfo timezoneInfo, HttpServletRequest request, Model model) {
		/*HttpSession session = request.getSession();

		if (timezoneInfo.getId() != null && !timezoneInfo.getId().equals("")) {
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
		}
		if(session.getAttribute("timezoneInfo")!=null){
			TimezoneInfo timezoneInfo2=(TimezoneInfo)session.getAttribute("timezoneInfo");
			timezoneInfo.setTimeStart1(timezoneInfo2.getTimeStart1());
			timezoneInfo.setTimeStart2(timezoneInfo2.getTimeStart2());
			timezoneInfo.setTimeStart3(timezoneInfo2.getTimeStart3());
			timezoneInfo.setTimeStart4(timezoneInfo2.getTimeStart4());
			timezoneInfo.setTimeEnd1(timezoneInfo2.getTimeEnd1());
			timezoneInfo.setTimeEnd2(timezoneInfo2.getTimeEnd2());
			timezoneInfo.setTimeEnd3(timezoneInfo2.getTimeEnd3());
			timezoneInfo.setTimeEnd4(timezoneInfo2.getTimeEnd4());
		}
*/
		if (timezoneInfo.getId() != null && !timezoneInfo.getId().equals("")) {
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
		}
		model.addAttribute("accessDoorTimezone", timezoneInfo);

		return "modules/mj/timezoneInfoForm";
	}

	/**
	 * 查看编辑表单2,更换了星期进入
	 */
	@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = "form2")
	public String form2(TimezoneInfo timezoneInfo, Model model) {
		TimezoneInfo timezoneInfo2=null;
		if(timezoneInfo.getWeekNumber()!=null && timezoneInfo.getId()!=null && !timezoneInfo.getId().equals("")){

			String aid= timezoneInfoService.findAIdById(timezoneInfo.getId());

			timezoneInfo.setId(aid);
			//修改时更换了星期
			timezoneInfo2 = timezoneInfoService.getByWdAndAid(timezoneInfo);
		}

		model.addAttribute("accessDoorTimezone", timezoneInfo2);
		return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/form?id="+timezoneInfo2.getId();
		//return "modules/mj/timezoneInfoForm";
	}

	@RequiresPermissions("mj:timezoneInfo:edit")
	@PostMapping(value = "copy")
	public String copy(TimezoneInfo timezoneInfo, HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		session.setAttribute("timezoneInfo",timezoneInfo);
		addMessage(redirectAttributes, "复制时区信息成功");
		return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("mj:timezoneInfo:edit")
	@PostMapping(value = "save")
	public String save(@Validated TimezoneInfo timezoneInfo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		try {
			timezoneInfoService.save(timezoneInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改时区信息成功");
		return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("mj:timezoneInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessDoorTimezone accessDoorTimezone) {
		accessDoorTimezoneService.delete(accessDoorTimezone);
		return renderResult(Global.TRUE, text("删除access_door_timezone成功！"));
	}*/
	
}