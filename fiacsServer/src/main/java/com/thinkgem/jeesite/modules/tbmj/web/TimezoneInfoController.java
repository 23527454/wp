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
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadTimezoneInfo;
import com.thinkgem.jeesite.modules.tbmj.entity.TimezoneInfo;
import com.thinkgem.jeesite.modules.tbmj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.tbmj.service.DownloadTimezoneInfoService;
import com.thinkgem.jeesite.modules.tbmj.service.TimezoneInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * access_door_timezoneController
 * @author demo
 * @version 2020-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/timezoneInfo")
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

	@RequiresPermissions("tbmj:timezoneInfo:view")
	@RequestMapping("index")
	public String index(TimezoneInfo timezoneInfo, Model model) {
		return "modules/tbmj/timezoneInfoIndex";
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
	@RequiresPermissions("tbmj:timezoneInfo:view")
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
		return "redirect:" + adminPath + "/tbmj/timezoneInfo/list?repage";
	}

	@RequiresPermissions("tbmj:downloadTimezoneInfo:edit")
	@RequestMapping(value = "download")
	@Transactional
	public String download(TimezoneInfo timezoneInfo, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		AccessParaInfo accessParaInfo=accessParaInfoService.get(timezoneInfo.getAccessParaInfoId());
		timezoneInfo.setAccessParaInfo(accessParaInfo);
		timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
		List<TimezoneInfo> list=timezoneInfoService.findList(timezoneInfo);
		for(int i=0;i<list.size();i++){
			TimezoneInfo timezoneInfo2=timezoneInfoService.get(list.get(i));
			DownloadTimezoneInfo downloadTimezoneInfo=new DownloadTimezoneInfo();
			downloadTimezoneInfo.setTimezoneParaInfoId(timezoneInfo2.getId());
			downloadTimezoneInfo.setAccessParaInfoId(timezoneInfo2.getAccessParaInfoId());
			downloadTimezoneInfo.setEquipment(equipmentService.get(String.valueOf(timezoneInfo2.getEquipmentId())));
			downloadTimezoneInfo.setEquipmentId(downloadTimezoneInfo.getEquipment().getId());
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

	private String backListPage(TimezoneInfo timezoneInfo, Model model) {
		model.addAttribute("timezoneInfo",timezoneInfo);
		if(timezoneInfo!=null){
			return "redirect:" + Global.getAdminPath() + "/tbmj/timezoneInfo/list?accessParaInfoId="+timezoneInfo.getAccessParaInfoId()+"&timeZoneType="+timezoneInfo.getTimeZoneType()+"&timeZoneNum="+timezoneInfo.getTimeZoneNum();
		}
		return "redirect:" + Global.getAdminPath() + "/tbmj/timezoneInfo/?repage";
	}

	/**
	 * 设备编号
	 * 门号
	 * 时区类型
	 * 时区号
	 *
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:timezoneInfo:view")
	@RequestMapping(value = {"list", ""})
	@Transactional
	public String list(TimezoneInfo timezoneInfo, Model model) {
		//通过tree传过来的门禁id查找这个门的信息
		if (timezoneInfo.getTimeZoneType()==null){
			timezoneInfo.setTimeZoneType("1");
		}
		if(timezoneInfo.getTimeZoneNum()==null){
			timezoneInfo.setTimeZoneNum("1");
		}

		List<TimezoneInfo> list=null;
		List<Map<String,String>> maps=new ArrayList<>();
		if((timezoneInfo.getEquipmentId()!=null && !timezoneInfo.getEquipmentId().equals("")) && (timezoneInfo.getAccessParaInfoId()!=null && !timezoneInfo.getAccessParaInfoId().equals(""))){
			//查询
			AccessParaInfo accessParaInfo=accessParaInfoService.get(timezoneInfo.getAccessParaInfoId());
			timezoneInfo.setAccessParaInfo(accessParaInfo);
			timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
			list= timezoneInfoService.findList(timezoneInfo);

			model.addAttribute("equipmentId", timezoneInfo.getEquipmentId());
			model.addAttribute("accessParaInfoId", timezoneInfo.getAccessParaInfoId());
		}

		model.addAttribute("timeZoneType", timezoneInfo.getTimeZoneType());
		model.addAttribute("timeZoneNum", timezoneInfo.getTimeZoneNum());
		model.addAttribute("timezone", list!=null&&list.size()!=0?list.get(0):null);
		return "modules/tbmj/timezoneInfoList";
	}

	/**
	 * 弹出添加时区的框
	 * @param accessParaInfoId
	 * @param timeZoneType
	 * @param modelAndView
	 * @param model
	 * @return
	 */
	@RequiresPermissions("tbmj:timezoneInfo:view")
	@RequestMapping(value = "toAddPage")
	public ModelAndView toAddPage(String accessParaInfoId,String timeZoneType, ModelAndView modelAndView, Model model) {
		model.addAttribute("accessParaInfoId",accessParaInfoId);
		model.addAttribute("timeZoneType",timeZoneType);
		modelAndView.setViewName("modules/tbmj/addTimezoneInfoPage");//跳转到这个jsp页面来渲染表格
		return modelAndView;
	}

	/**
	 * 添加时区
	 */
	@RequiresPermissions("tbmj:timezoneInfo:edit")
	@PostMapping(value = "save2")
	@Transactional
	@ResponseBody
	public Map<String,Object> save2(TimezoneInfo timezoneInfo,String timezoneName,HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map=new HashMap<>();
		AccessParaInfo accessParaInfo = accessParaInfoService.get(timezoneInfo.getAccessParaInfoId());
		Equipment equipment = equipmentService.get(String.valueOf(accessParaInfo.getEquipmentId()));

		Integer num = 0;
		String type = "";
		String description = "";
		Integer sort = 0;
		if (timezoneInfo.getTimeZoneType().equals("1")) {
			num = dictService.findMaxValueByType("time_zone_num_staff");
			type = "time_zone_num_staff";
			description = "人员时区号";
			sort = dictService.findMaxSortByType("time_zone_num_staff");
		} else {
			num = dictService.findMaxValueByType("time_zone_num_drvice");
			type = "time_zone_num_drvice";
			description = "设备时区号";
			sort = dictService.findMaxSortByType("time_zone_num_drvice");
		}
		try {
			Dict dict = new Dict();
			dict.setValue(String.valueOf(num + 1));
			dict.setLabel(timezoneName);
			dict.setType(type);
			dict.setDescription(description);
			dict.setSort(sort + 10);

			Integer count=dictService.findCountByTypeAndLabel(type,timezoneName);
			Integer count2=dictService.findCountByTypeAndLabel(type,null);
			if(count==0 && count2<10){
				dictService.save(dict);
				TimezoneInfo timezoneInfo2 = new TimezoneInfo();
				timezoneInfo2.setEquipment(equipment);
				timezoneInfo2.setEquipmentId(equipment.getId());
				timezoneInfo2.setAccessParaInfo(accessParaInfo);
				timezoneInfo2.setDoorPos(accessParaInfo.getDoorPos());
				timezoneInfo2.setTimeZoneType(timezoneInfo.getTimeZoneType());
				timezoneInfo2.setTimeZoneNum(dict.getValue());
				timezoneInfo2.setMon("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo2.setTue("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo2.setWed("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo2.setThu("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo2.setFri("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo2.setSat("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo2.setSun("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfoService.save(timezoneInfo2);

				addMessage(redirectAttributes, "保存时区信息成功");
				map.put("status","ok");
				map.put("msg","保存时区信息成功！");
			}else if(count2>=10){
				map.put("status","max");
				map.put("msg","此类型中的时区数量已达到上限！");
			}else if(count!=0){
				map.put("status","exist");
				map.put("msg","该时区名在此类型中已存在！");
			}
		} catch (Exception e) {
			map.put("status","exception");
			map.put("msg","保存时区信息失败！异常信息："+e.getMessage());
		}
		return map;
	}
	/**
	 * 查看编辑表单，修改时进入
	 */
	@RequiresPermissions("tbmj:timezoneInfo:view")
	@RequestMapping(value = "form")
	public String form(TimezoneInfo timezoneInfo, HttpServletRequest request, Model model) {

		if (timezoneInfo.getId() != null && !timezoneInfo.getId().equals("")) {
			String weekNum=timezoneInfo.getWeekNum();
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
			if(weekNum.equals("1")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","1");
				}
			}else if(weekNum.equals("2")){
				String week=timezoneInfo.getTue();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","2");
				}
			}else if(weekNum.equals("3")){
				String week=timezoneInfo.getWed();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","3");
				}
			}else if(weekNum.equals("4")){
				String week=timezoneInfo.getThu();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","4");
				}
			}else if(weekNum.equals("5")){
				String week=timezoneInfo.getFri();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","5");
				}
			}else if(weekNum.equals("6")){
				String week=timezoneInfo.getSat();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","6");
				}
			}else if(weekNum.equals("7")){
				String week=timezoneInfo.getSun();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","7");
				}
			}else{
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","1");
				}
			}
		}
		model.addAttribute("timezoneInfo",timezoneInfo);

		return "modules/tbmj/timezoneInfoForm";
	}

	/**
	 * 粘贴数据
	 */
	@RequiresPermissions("tbmj:timezoneInfo:edit")
	@PostMapping(value = "paste")
	//@ResponseBody
	public String paste(TimezoneInfo timezoneInfo, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		TimezoneInfo copy_timezoneInfo=(TimezoneInfo)session.getAttribute("copy_timezoneInfo");
		if(copy_timezoneInfo!=null){
			String weekNum=timezoneInfo.getWeekNum();
			if(weekNum.equals("1")){
				timezoneInfo.setMon(copy_timezoneInfo.getTime2());
			}else if(weekNum.equals("2")){
				timezoneInfo.setTue(copy_timezoneInfo.getTime2());
			}else if(weekNum.equals("3")){
				timezoneInfo.setWed(copy_timezoneInfo.getTime2());
			}else if(weekNum.equals("4")){
				timezoneInfo.setThu(copy_timezoneInfo.getTime2());
			}else if(weekNum.equals("5")){
				timezoneInfo.setFri(copy_timezoneInfo.getTime2());
			}else if(weekNum.equals("6")){
				timezoneInfo.setSat(copy_timezoneInfo.getTime2());
			}else if(weekNum.equals("7")){
				timezoneInfo.setSun(copy_timezoneInfo.getTime2());
			}
			timezoneInfo.setTime2(copy_timezoneInfo.getTime2());
			timezoneInfo.setRemarks(copy_timezoneInfo.getRemarks());

			String[] sd=timezoneInfo.getTime2().split(";");
			for(int i=0;i<sd.length;i++){
				//根据-拆分为每个时段的开始和结束时间
				String[] time=sd[i].split("-");
				model.addAttribute("timeStart"+(i+1),time[0]);
				model.addAttribute("timeEnd"+(i+1),time[1]);
			}
			addMessage(model,"粘贴成功!");
			model.addAttribute("weekNum",weekNum);
		}else{
			addMessage(model,"暂未复制内容!");
		}
		model.addAttribute("timezoneInfo", timezoneInfo);
		return "modules/tbmj/timezoneInfoForm";
	}


	/**
	 * 复制数据
	 */
	@RequiresPermissions("tbmj:timezoneInfo:edit")
	@PostMapping(value = "copy")
	//@ResponseBody
	public String copy(TimezoneInfo timezoneInfo,HttpServletRequest request,HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		String start1=request.getParameter("timeStart1");
		String start2=request.getParameter("timeStart2");
		String start3=request.getParameter("timeStart3");
		String start4=request.getParameter("timeStart4");
		String end1=request.getParameter("timeEnd1");
		String end2=request.getParameter("timeEnd2");
		String end3=request.getParameter("timeEnd3");
		String end4=request.getParameter("timeEnd4");
		String str=start1+"-"+end1+";"+start2+"-"+end2+";"+start3+"-"+end3+";"+start4+"-"+end4+";";
		String[] sd=str.split(";");
		for(int i=0;i<sd.length;i++){
			//根据-拆分为每个时段的开始和结束时间
			String[] time=sd[i].split("-");
			model.addAttribute("timeStart"+(i+1),time[0]);
			model.addAttribute("timeEnd"+(i+1),time[1]);
		}
		TimezoneInfo copy_timezoneInfo=new TimezoneInfo();
		copy_timezoneInfo.setTime2(str);
		copy_timezoneInfo.setRemarks(timezoneInfo.getRemarks());
		session.setAttribute("copy_timezoneInfo",copy_timezoneInfo);

		addMessage(model,"复制成功!");
		model.addAttribute("timezoneInfo",timezoneInfo);
		return "modules/tbmj/timezoneInfoForm";
		//return true;
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("tbmj:timezoneInfo:edit")
	@PostMapping(value = "save")
	public String save(TimezoneInfo timezoneInfo,HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		String start1=request.getParameter("timeStart1");
		String start2=request.getParameter("timeStart2");
		String start3=request.getParameter("timeStart3");
		String start4=request.getParameter("timeStart4");
		String end1=request.getParameter("timeEnd1");
		String end2=request.getParameter("timeEnd2");
		String end3=request.getParameter("timeEnd3");
		String end4=request.getParameter("timeEnd4");
		String remarks=request.getParameter("remarks");

		String str=start1+"-"+end1+";"+start2+"-"+end2+";"+start3+"-"+end3+";"+start4+"-"+end4+";";
		String weekNum=request.getParameter("weekNum");

		timezoneInfo=timezoneInfoService.get(timezoneInfo.getId());

		timezoneInfo.setRemarks(remarks);
		if(weekNum.equals("1")){
			timezoneInfo.setMon(str);
		}else if(weekNum.equals("2")){
			timezoneInfo.setTue(str);
		}else if(weekNum.equals("3")){
			timezoneInfo.setWed(str);
		}else if(weekNum.equals("4")){
			timezoneInfo.setThu(str);
		}else if(weekNum.equals("5")){
			timezoneInfo.setFri(str);
		}else if(weekNum.equals("6")){
			timezoneInfo.setSat(str);
		}else if(weekNum.equals("7")){
			timezoneInfo.setSun(str);
		}
		try {
			timezoneInfoService.save(timezoneInfo);
		}catch (Exception e){
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存时区信息成功");
		return "redirect:" + Global.getAdminPath() + "/tbmj/timezoneInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("tbmj:timezoneInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessDoorTimezone accessDoorTimezone) {
		accessDoorTimezoneService.delete(accessDoorTimezone);
		return renderResult(Global.TRUE, text("删除access_door_timezone成功！"));
	}*/
	
}