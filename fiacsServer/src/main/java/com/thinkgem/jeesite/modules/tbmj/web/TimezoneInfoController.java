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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	 * 查看编辑表单，修改时进入
	 */
	@RequiresPermissions("tbmj:timezoneInfo:view")
	@RequestMapping(value = "form")
	public String form(TimezoneInfo timezoneInfo, HttpServletRequest request, Model model) {

		if (timezoneInfo.getId() != null && !timezoneInfo.getId().equals("")) {
			String weekNum=timezoneInfo.getWeekNum();
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
			if(timezoneInfo.equals("1")){
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
			}else if(timezoneInfo.equals("2")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","2");
				}
			}else if(timezoneInfo.equals("3")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","3");
				}
			}else if(timezoneInfo.equals("4")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","4");
				}
			}else if(timezoneInfo.equals("5")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","5");
				}
			}else if(timezoneInfo.equals("6")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","6");
				}
			}else if(timezoneInfo.equals("7")){
				String week=timezoneInfo.getMon();
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
	 * 查看编辑表单2,更换了星期进入
	 */
	@RequiresPermissions("tbmj:timezoneInfo:view")
	@RequestMapping(value = "form2")
	public String form2(TimezoneInfo timezoneInfo, Model model) {
		if (timezoneInfo.getId() != null && !timezoneInfo.getId().equals("")) {
			String weekNum=timezoneInfo.getWeekNum();
			timezoneInfo = timezoneInfoService.get(timezoneInfo.getId());
			if(timezoneInfo.equals("1")){
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
			}else if(timezoneInfo.equals("2")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","2");
				}
			}else if(timezoneInfo.equals("3")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","3");
				}
			}else if(timezoneInfo.equals("4")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","4");
				}
			}else if(timezoneInfo.equals("5")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","5");
				}
			}else if(timezoneInfo.equals("6")){
				String week=timezoneInfo.getMon();
				//根据;拆分为4个时段
				String[] sd=week.split(";");
				for(int j=0;j<sd.length;j++){
					//根据-拆分为每个时段的开始和结束时间
					String[] time=sd[j].split("-");
					model.addAttribute("timeStart"+(j+1),time[0]);
					model.addAttribute("timeEnd"+(j+1),time[1]);
					model.addAttribute("weekNum","6");
				}
			}else if(timezoneInfo.equals("7")){
				String week=timezoneInfo.getMon();
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
		model.addAttribute("id", timezoneInfo.getId());

		return "modules/tbmj/timezoneInfoForm";
	}

	@RequiresPermissions("tbmj:timezoneInfo:edit")
	@PostMapping(value = "copy")
	public String copy(TimezoneInfo timezoneInfo, HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		session.setAttribute("timezoneInfo",timezoneInfo);
		addMessage(redirectAttributes, "复制时区信息成功");
		return "redirect:" + Global.getAdminPath() + "/tbmj/timezoneInfo/";
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