/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.tbmj.entity.DownloadWorkdayParaInfo;
import com.thinkgem.jeesite.modules.tbmj.entity.WorkdayParaInfo;
import com.thinkgem.jeesite.modules.tbmj.service.DownloadWorkdayParaInfoService;
import com.thinkgem.jeesite.modules.tbmj.service.WorkdayParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * access_workdayController
 * @author demo
 * @version 2020-07-02
 */
@Controller
	@RequestMapping(value = "${adminPath}/tbmj/workdayParaInfo")
public class WorkdayParaInfoController extends BaseController {

	@Autowired
	private WorkdayParaInfoService workdayParaInfoService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private DownloadWorkdayParaInfoService downloadWorkdayParaInfoService;

	@RequiresPermissions("tbmj:workdayParaInfo:view")
	@RequestMapping("index")
	public String index(WorkdayParaInfo workdayParaInfo, Model model) {
		return "modules/tbmj/workdayParaInfoIndex";
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WorkdayParaInfo get(String id) {
		return workdayParaInfoService.get(id);
	}

	@RequiresPermissions("tbmj:workdayParaInfo:edit")
	@RequestMapping(value = "plan1")
	public ModelAndView plan1(String eId, ModelAndView modelAndView, Model model) {
		model.addAttribute("eId",eId);
		modelAndView.setViewName("modules/tbmj/workdayParaInfoForm");//跳转到这个jsp页面来渲染表格
		return modelAndView;
	}


	@RequiresPermissions("tbmj:workdayParaInfo:edit")
	@PostMapping (value = "insertDefault")
	@Transactional
	@ResponseBody
	public boolean insertDefault(String eId, ModelAndView modelAndView, Model model,RedirectAttributes redirectAttributes){
		//workdayParaInfoService.deleteAllByEId(eId);
		try{
			//添加工作日表信息
			StringBuffer sb=new StringBuffer("");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c2 = Calendar.getInstance();
			String year = String.valueOf(c2.get(Calendar.YEAR));
			Date date = new Date();

			WorkdayParaInfo workdayParaInfo =new WorkdayParaInfo();
			workdayParaInfo.setEquipment(equipmentService.get(eId));
			workdayParaInfo.setEquipmentId(workdayParaInfo.getEquipment().getId());
			workdayParaInfo.setYear(year);
			workdayParaInfo=workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
			for(int i=0;i<12;i++){
				sb=new StringBuffer("");
				String str=year+"-"+(i+1)+"-"+1;
				Calendar c = Calendar.getInstance();
				date = format.parse(str);
				c.setTime(date);
				int day=c.getActualMaximum(Calendar.DAY_OF_MONTH);
				for(int j=0;j<day;j++){
					str=year+"-"+(i+1)+"-"+(j+1);
					date=format.parse(str);
					c.setTime(date);

					if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
						sb.append("0");
					} else{
						sb.append("1");
					}
				}
				switch ((i+1)){
					case 1:
						workdayParaInfo.setJan(sb.toString());
						break;
					case 2:
						workdayParaInfo.setFeb(sb.toString());
						break;
					case 3:
						workdayParaInfo.setMar(sb.toString());
						break;
					case 4:
						workdayParaInfo.setApr(sb.toString());
						break;
					case 5:
						workdayParaInfo.setMay(sb.toString());
						break;
					case 6:
						workdayParaInfo.setJun(sb.toString());
						break;
					case 7:
						workdayParaInfo.setJul(sb.toString());
						break;
					case 8:
						workdayParaInfo.setAug(sb.toString());
						break;
					case 9:
						workdayParaInfo.setSep(sb.toString());
						break;
					case 10:
						workdayParaInfo.setOct(sb.toString());
						break;
					case 11:
						workdayParaInfo.setNov(sb.toString());
						break;
					case 12:
						workdayParaInfo.setDec(sb.toString());
						break;
					default:
						workdayParaInfo.setJan(sb.toString());
						break;
				}
			}
			workdayParaInfoService.modifyRestDayById(workdayParaInfo);
			addMessage(redirectAttributes, "恢复默认成功！");
		}catch (Exception e){
			addMessage(redirectAttributes, "恢复默认失败！");
			return false;
		}
		//return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/?repage";
		return true;
	}

	@RequiresPermissions("tbmj:workdayParaInfo:edit")
	@RequestMapping(value = "download")
	@Transactional
	public String download(String eId,String mon, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		/*if(workdayParaInfo==null){
			workdayParaInfo=new TimezoneInfo();
			AccessParaInfo accessParaInfo=accessParaInfoService.get(workdayParaInfo.getAccessParaInfoId());
			timezoneInfo.setAccessParaInfo(accessParaInfo);
		}*/
		WorkdayParaInfo workdayParaInfo =new WorkdayParaInfo();
		workdayParaInfo.setEquipment(equipmentService.get(eId));
		List<WorkdayParaInfo> list= workdayParaInfoService.findList(workdayParaInfo);
		for(int i=0;i<list.size();i++){
			WorkdayParaInfo workdayParaInfo2 = workdayParaInfoService.get(list.get(i));
			DownloadWorkdayParaInfo downloadWorkdayParaInfo=new DownloadWorkdayParaInfo();
			downloadWorkdayParaInfo.setWorkdayParaInfoId(workdayParaInfo2.getId());
			downloadWorkdayParaInfo.setEquipmentId(workdayParaInfo.getEquipment().getId());
			downloadWorkdayParaInfo.setWorkdayParaInfo(workdayParaInfoService.get(workdayParaInfo2.getId()));
			downloadWorkdayParaInfo.setEquipment(equipmentService.get(String.valueOf(workdayParaInfo.getEquipment().getId())));
			downloadWorkdayParaInfo.setIsDownload("0");
			downloadWorkdayParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadWorkdayParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadWorkdayParaInfoService.countByEntity(downloadWorkdayParaInfo)==0){
				downloadWorkdayParaInfoService.save(downloadWorkdayParaInfo);
			}
		}
		addMessage(redirectAttributes, "工作日同步成功");
		return backListPage(eId,mon, model);
	}

	private String backListPage(String eId,String mon,Model model) {
		model.addAttribute("eId",eId);
		model.addAttribute("mon",mon);
		if((eId!=null && !eId.equals(""))||(mon!=null && !mon.equals(""))){
			return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/list?eId="+eId+"&mon="+mon+"&repage";
		}else if(eId!=null && !eId.equals("")){
			return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/list?eId="+eId+"&repage";
		}else if(mon!=null && !mon.equals("")){
			return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/list?mon="+mon+"&repage";
		}
		return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/?repage";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:workdayParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(String eId, String mon,HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		Calendar date = Calendar.getInstance();
		//如果没选择月份那就用当前月
		mon=mon==null||mon.equals("")?String.valueOf(date.get(Calendar.MONTH)+1):mon;
		String year = String.valueOf(date.get(Calendar.YEAR));
		String days="";

		WorkdayParaInfo workdayParaInfo=new WorkdayParaInfo();
		workdayParaInfo.setYear(year);
		Equipment equipment=equipmentService.get(eId);
		workdayParaInfo.setEquipment(equipment);
		workdayParaInfo=workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
		if(workdayParaInfo!=null){
			switch (Integer.parseInt(mon)){
				case 1:
					days=workdayParaInfo.getJan();
					break;
				case 2:
					days=workdayParaInfo.getFeb();
					break;
				case 3:
					days=workdayParaInfo.getMar();
					break;
				case 4:
					days=workdayParaInfo.getApr();
					break;
				case 5:
					days=workdayParaInfo.getMay();
					break;
				case 6:
					days=workdayParaInfo.getJun();
					break;
				case 7:
					days=workdayParaInfo.getJul();
					break;
				case 8:
					days=workdayParaInfo.getAug();
					break;
				case 9:
					days=workdayParaInfo.getSep();
					break;
				case 10:
					days=workdayParaInfo.getOct();
					break;
				case 11:
					days=workdayParaInfo.getNov();
					break;
				case 12:
					days=workdayParaInfo.getDec();
					break;
				default:
					days=workdayParaInfo.getJan();
					break;
			}
			StringBuffer sb=new StringBuffer("");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date d = new Date();
			Calendar c = Calendar.getInstance();
			boolean isFirst=true;
			for(int i=0;i<days.length();i++){
				String dayStatus=days.substring(i,i+1);
				String str=year+"-"+mon+"-"+(i+1);
				d=format.parse(str);
				c.setTime(d);

				int week_index2 = c.get(Calendar.DAY_OF_WEEK) - 1;
				if(week_index2<=0){
					week_index2 = 7;
				}
				if(isFirst){
					isFirst=false;
					sb.append("<tr>");
					for(int l=1;l<week_index2;l++){
						//这个月第一天前面要空出多少格
						sb.append("<td></td>");
					}
				}
				if(week_index2==1 && !isFirst){
					sb.append("<tr>");
				}
				//这个月的日
				if(dayStatus.equals("0")){
					sb.append("<td class=\"restDay enableSel\">"+(i+1)+"</td>");
				}else{
					sb.append("<td class=\"enableSel\">"+(i+1)+"</td>");
				}
				if(i+1==days.length() || week_index2==7){
					if(i+1==days.length()){
						for (;week_index2<7;week_index2++){
							sb.append("<td></td>");
						}
					}
					sb.append("</tr>");
				}
			}

			model.addAttribute("mrl",sb.toString());
			model.addAttribute("id",workdayParaInfo.getId());
		}
		model.addAttribute("eId", eId);
		model.addAttribute("mon", mon);
		return "modules/tbmj/workdayParaInfoList";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:workdayParaInfo:view")
	@RequestMapping(value = "form")
	public String form(WorkdayParaInfo workdayParaInfo, Model model) {
		model.addAttribute("accessWorkday", workdayParaInfo);
		return "modules/tbmj/workdayParaInfoForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("tbmj:workdayParaInfo:edit")
	@PostMapping(value = "save")
	@Transactional
	@ResponseBody
	public boolean save(String eId, String start,String end,Model model, RedirectAttributes redirectAttributes) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date startTime = format.parse(start);
		Date endTime = format.parse(end);
		String year = String.valueOf(c.get(Calendar.YEAR));

		c.setTime(startTime);
		//起始月
		int sm=c.get(Calendar.MONTH)+1;
		//起始日
		int sd=c.get(Calendar.DAY_OF_MONTH);
		int sday=c.getActualMaximum(Calendar.DAY_OF_MONTH);

		c.setTime(endTime);
		//结束月
		int em=c.get(Calendar.MONTH)+1;
		//结束日
		int ed=c.get(Calendar.DAY_OF_MONTH);

		//获取起始月的数据
		WorkdayParaInfo workdayParaInfo =new WorkdayParaInfo();
		Equipment equipment=equipmentService.get(eId);
		workdayParaInfo.setEquipment(equipment);
		workdayParaInfo.setYear(year);
		workdayParaInfo = workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
		//获得数据库中的day
		StringBuilder ssb=new StringBuilder(getWorkParaInfoDays(String.valueOf(sm),workdayParaInfo));

		try {
			//判断起始月和结束月相差多少
			if(em>sm){
				//判断相差几个月
				int num2=em-sm;
				for(int i=0;i<num2;i++){
					//结束月比起始月大
					ssb=new StringBuilder("");
					String str2="";
					//判断当前相差是否为1个月，如起始：3，结束：4
					if(num2-i==1){
						//如果只相差1个月，遍历开始时间这一个月的剩余时间，0-结束日
						for(int j=sd;j<=sday;j++){
							str2+="0";
						}

						ssb=new StringBuilder(getWorkParaInfoDays(String.valueOf(sm),workdayParaInfo));

						//替换
						ssb.replace(sd-1,sday,str2);
						//保存到数据库

						setWorkParaInfoDays(String.valueOf((i+1)),ssb.toString(),workdayParaInfo);

						workdayParaInfoService.modifyRestDayById(workdayParaInfo);
						str2="";

						//如果只相差1个月，遍历结束时间这一个月的剩余时间，0-结束日
						for(int j=0;j<ed;j++){
							str2+="0";
						}
						Equipment equipment2=equipmentService.get(eId);
						workdayParaInfo.setEquipment(equipment);
						workdayParaInfo.setYear(year);
						workdayParaInfo = workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
						ssb=new StringBuilder(getWorkParaInfoDays(String.valueOf(em),workdayParaInfo));
						//替换
						ssb.replace(0,ed,str2);
						//保存到数据库
						setWorkParaInfoDays(String.valueOf((i+1)),ssb.toString(),workdayParaInfo);
						workdayParaInfoService.modifyRestDayById(workdayParaInfo);
					}else{
						Date date2=format.parse(year+"-"+(sm+(i+1))+"-"+1);
						Calendar c2 = Calendar.getInstance();
						c2.setTime(date2);
						int eday=c2.getActualMaximum(Calendar.DAY_OF_MONTH);
						for(int j=0;j<eday;j++){
							str2+="0";
						}
						Equipment equipment2=equipmentService.get(eId);
						workdayParaInfo.setEquipment(equipment);
						workdayParaInfo.setYear(year);
						ssb=new StringBuilder(getWorkParaInfoDays(String.valueOf(em),workdayParaInfo));
						//替换
						ssb.replace(0,eday,str2);
						//保存到数据库
						setWorkParaInfoDays(String.valueOf(em),ssb.toString(),workdayParaInfo);
						workdayParaInfoService.modifyRestDayById(workdayParaInfo);
					}
				}
			}else{
				//结束月等于起始月
				//计算相差天数：结束时间-(起始时间-1)
				int num=ed-(sd-1);
				String str="";
				ssb=new StringBuilder(getWorkParaInfoDays(String.valueOf(em),workdayParaInfo));
				//根据相差天数进行循环修改值
				for(int i=0;i<num;i++){
					str+="0";
				}
				//替换day，起始时间-1，结束时间
				ssb.replace(sd-1,ed,str);
				//保存到数据库
				setWorkParaInfoDays(String.valueOf(em),ssb.toString(),workdayParaInfo);
				workdayParaInfoService.modifyRestDayById(workdayParaInfo);
			}

			addMessage(redirectAttributes, "保存假期成功");
			//return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo?eId="+eId+"&repage";
			return true;
		}catch (Exception e){
			addMessage(redirectAttributes, "保存假期失败");
		}
		return false;
		//return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/?repage";
	}

	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("tbmj:workdayParaInfo:edit")
	@RequestMapping(value = "delete")
	@Transactional(readOnly = false)
	public String delete(String nums,String mon,String id,RedirectAttributes redirectAttributes) {
		String[] arr=nums.split(",");
		StringBuilder sb=new StringBuilder("");
		String eId="";
		try{
			WorkdayParaInfo workdayParaInfo = workdayParaInfoService.get(id);
			eId=workdayParaInfo.getEquipmentId();
			String days=getWorkParaInfoDays(mon,workdayParaInfo);
			sb.append(days);
			for(int i=0;i<arr.length;i++){
				sb.replace(Integer.parseInt(arr[i])-1,Integer.parseInt(arr[i]),"1");
			}
			setWorkParaInfoDays(mon,sb.toString(),workdayParaInfo);
			int result= workdayParaInfoService.modifyRestDayById(workdayParaInfo);
			if (result!=1){
				throw new Exception();
			}
			addMessage(redirectAttributes, "删除假期成功");
			return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo?eId="+eId+"&mon="+mon+"&repage";
		}catch (Exception e){
			addMessage(redirectAttributes, "删除假期失败");
		}
		return "redirect:" + Global.getAdminPath() + "/tbmj/workdayParaInfo/?repage";
	}


	public void setWorkParaInfoDays(String mon,String times,WorkdayParaInfo workdayParaInfo){
		switch (Integer.parseInt(mon)){
			case 1:
				workdayParaInfo.setJan(times);
				break;
			case 2:
				workdayParaInfo.setFeb(times);
				break;
			case 3:
				workdayParaInfo.setMar(times);
				break;
			case 4:
				workdayParaInfo.setApr(times);
				break;
			case 5:
				workdayParaInfo.setMay(times);
				break;
			case 6:
				workdayParaInfo.setJun(times);
				break;
			case 7:
				workdayParaInfo.setJul(times);
				break;
			case 8:
				workdayParaInfo.setAug(times);
				break;
			case 9:
				workdayParaInfo.setSep(times);
				break;
			case 10:
				workdayParaInfo.setOct(times);
				break;
			case 11:
				workdayParaInfo.setNov(times);
				break;
			case 12:
				workdayParaInfo.setDec(times);
				break;
			default:
				workdayParaInfo.setJan(times);
				break;
		}
	}

	public String getWorkParaInfoDays(String mon,WorkdayParaInfo workdayParaInfo){
		String days="";
		switch (Integer.parseInt(mon)){
			case 1:
				days=workdayParaInfo.getJan();
				break;
			case 2:
				days=workdayParaInfo.getFeb();
				break;
			case 3:
				days=workdayParaInfo.getMar();
				break;
			case 4:
				days=workdayParaInfo.getApr();
				break;
			case 5:
				days=workdayParaInfo.getMay();
				break;
			case 6:
				days=workdayParaInfo.getJun();
				break;
			case 7:
				days=workdayParaInfo.getJul();
				break;
			case 8:
				days=workdayParaInfo.getAug();
				break;
			case 9:
				days=workdayParaInfo.getSep();
				break;
			case 10:
				days=workdayParaInfo.getOct();
				break;
			case 11:
				days=workdayParaInfo.getNov();
				break;
			case 12:
				days=workdayParaInfo.getDec();
				break;
			default:
				days=workdayParaInfo.getJan();
				break;
		}
		return days;
	}
	
}