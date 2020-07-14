/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.mj.entity.TimezoneInfo;
import com.thinkgem.jeesite.modules.mj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.mj.service.TimezoneInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		if(timezoneInfo.getAccessParaInfo()!=null && timezoneInfo.getAccessParaInfo().getId()!=null && !timezoneInfo.getAccessParaInfo().getId().equals("")){
			list= timezoneInfoService.findList(timezoneInfo);
			model.addAttribute("accessParaInfo.id", timezoneInfo.getAccessParaInfo().getId());
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
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessDoorTimezone> listData(AccessDoorTimezone accessDoorTimezone, HttpServletRequest request, HttpServletResponse response) {
		accessDoorTimezone.setPage(new Page<>(request, response));
		Page<AccessDoorTimezone> page = accessDoorTimezoneService.findPage(accessDoorTimezone);
		return page;
	}*/

	/**
	 * 查看编辑表单，修改时进入
	 */
	@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = "form")
	public String form(TimezoneInfo timezoneInfo, Model model) {
		if(timezoneInfo.getId()!=null && !timezoneInfo.getId().equals("")){
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
		}
		model.addAttribute("accessDoorTimezone", timezoneInfo);

		return "modules/mj/timezoneInfoForm";
	}

	/**
	 * 添加时进入
	 * @param accessParaInfoId
	 * @param model
	 * @return
	 */
	/*@RequiresPermissions("mj:timezoneInfo:view")
	@RequestMapping(value = "form3")
	public String form3(String accessParaInfoId , Model model) {

		System.out.println("______________________________________"+accessParaInfoId);

		TimezoneInfo timezoneInfo=new TimezoneInfo();
		if(accessParaInfoId!=null && !accessParaInfoId.equals("")) {
			AccessParaInfo accessParaInfo = accessParaInfoService.get(accessParaInfoId);
			Equipment equipment = equipmentService.get(accessParaInfo.getEquipment().getId());
			for (int j = 1; j <= 7; j++) {
				timezoneInfo.setEquipment(equipment);
				timezoneInfo.setAccessParaInfo(accessParaInfo);
				timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
				timezoneInfo.setTimeZoneType("1");
				timezoneInfo.setTimeZoneNum(timezoneInfo.getTimeZoneNum());
				timezoneInfo.setWeekNumber(j);
				timezoneInfo.setTimeStart1("00:00");
				timezoneInfo.setTimeEnd1("23:59");
				timezoneInfo.setTimeStart2("00:00");
				timezoneInfo.setTimeEnd2("00:00");
				timezoneInfo.setTimeStart3("00:00");
				timezoneInfo.setTimeEnd3("00:00");
				timezoneInfo.setTimeStart4("00:00");
				timezoneInfo.setTimeEnd4("00:00");
			}
		}


		*//*boolean isNew=true;
		if(timezoneInfo.getId()!=null && !timezoneInfo.getId().equals("")){
			isNew=false;
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
		}else{
			AccessParaInfo accessParaInfo=accessParaInfoService.get(timezoneInfo.getAccessParaInfo().getId());
			Equipment equipment=equipmentService.get(accessParaInfo.getEquipment().getId());
			for(int j=1;j<=7;j++){
				timezoneInfo.setEquipment(equipment);
				timezoneInfo.setAccessParaInfo(accessParaInfo);
				timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
				timezoneInfo.setTimeZoneType("1");
				timezoneInfo.setTimeZoneNum(timezoneInfo.getTimeZoneNum());
				timezoneInfo.setWeekNumber(j);
				timezoneInfo.setTimeStart1("00:00");
				timezoneInfo.setTimeEnd1("23:59");
				timezoneInfo.setTimeStart2("00:00");
				timezoneInfo.setTimeEnd2("00:00");
				timezoneInfo.setTimeStart3("00:00");
				timezoneInfo.setTimeEnd3("00:00");
				timezoneInfo.setTimeStart4("00:00");
				timezoneInfo.setTimeEnd4("00:00");
				timezoneInfoService.save(timezoneInfo);
			}
		}
		model.addAttribute("isNew",isNew);
		model.addAttribute("accessDoorTimezone", timezoneInfo);*//*

		return "modules/mj/timezoneInfoForm";
	}*/

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