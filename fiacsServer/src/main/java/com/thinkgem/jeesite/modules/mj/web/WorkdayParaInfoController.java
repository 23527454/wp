/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.mj.entity.WorkdayParaInfo;
import com.thinkgem.jeesite.modules.mj.service.WorkdayParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	@Autowired
	private EquipmentService equipmentService;

	@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping("index")
	public String index(WorkdayParaInfo workdayParaInfo, Model model) {
		return "modules/mj/workdayParaInfoIndex";
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public WorkdayParaInfo get(String id) {
		return workdayParaInfoService.get(id);
	}

	@RequiresPermissions("mj:workdayParaInfo:edit")
	@RequestMapping(value = "plan1")
	public ModelAndView plan1(String eId, ModelAndView modelAndView, Model model) {
		model.addAttribute("eId",eId);
		modelAndView.setViewName("modules/mj/workdayParaInfoForm");//跳转到这个jsp页面来渲染表格
		return modelAndView;
	}


	@RequiresPermissions("mj:workdayParaInfo:edit")
	@PostMapping(value = "insertDefault")
	@Transactional
	public String insertDefault(String eId, ModelAndView modelAndView, Model model,RedirectAttributes redirectAttributes){
		workdayParaInfoService.deleteAllByEId(eId);
		try{
			//添加工作日表信息
			StringBuffer sb=new StringBuffer("");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c2 = Calendar.getInstance();
			String year = String.valueOf(c2.get(Calendar.YEAR));
			Date date = new Date();
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
				WorkdayParaInfo workdayParaInfo=new WorkdayParaInfo();
				workdayParaInfo.setEquipment(equipmentService.get(eId));
				workdayParaInfo.setYear(year);
				workdayParaInfo.setMonth((i+1)+"");
				workdayParaInfo.setDay(sb.toString());
				workdayParaInfoService.save(workdayParaInfo);
			}
			addMessage(redirectAttributes, "恢复默认成功！");
		}catch (Exception e){
			addMessage(redirectAttributes, "恢复默认失败！");
		}
			return "redirect:" + Global.getAdminPath() + "/mj/workdayParaInfo/?repage";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(String eId, String mon,Integer pageNo,Integer pageSize, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		pageNo=pageNo==null?1:pageNo;
		pageSize=pageSize==null?15:pageSize;
		WorkdayParaInfo workdayParaInfo=new WorkdayParaInfo();
		workdayParaInfo.setMonth(mon);
		if(eId!=null && !eId.equals("")){
			workdayParaInfo.setEquipment(equipmentService.get(eId));
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			workdayParaInfo.setYear(year);
			List<WorkdayParaInfo> list = workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
			//List<WorkdayParaInfo> list=workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
			List<Map<String, String>> restDay = new ArrayList<Map<String, String>>();
			int count=0;
			//遍历月
			for(int i=0;i<list.size();i++){
				//0,1 格式,转换为数组进行遍历，获取真实的日
				String day=list.get(i).getDay();
				//获得id（这个一个设备今年的某月）
				String id=list.get(i).getId();
				char a[]=day.toCharArray();
				//遍历日
				for(int j=0;j<a.length;j++){
					//获得完整的日期
					String str=year+"-"+list.get(i).getMonth()+"-"+(j+1);
					if(String.valueOf(a[j]).equals("0")){
						count++;
						Map<String,String> map = new HashMap();
						map.put("date",str);
						map.put("restIndex",String.valueOf(j+1));
						map.put("id",id);
						restDay.add(map);
					}
				}
			}
			List<Map<String, String>> tempRestDay = new ArrayList<Map<String, String>>();
			int num=0;
			for(int i=(pageNo-1)*pageSize;i<restDay.size();i++){
				Map<String,String> map = restDay.get(i);
				tempRestDay.add(map);
				num++;
				if(num==pageSize){
					break;
				}
			}
			Page page=new Page(pageNo,pageSize,restDay.size(),tempRestDay);
			page.initialize();

			model.addAttribute("page", page);
			//model.addAttribute("restDay", restDay);
		}

		model.addAttribute("eId", eId);
		model.addAttribute("mon", mon);
		return "modules/mj/workdayParaInfoList";
	}


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
	@RequiresPermissions("mj:workdayParaInfo:edit")
	@PostMapping(value = "save")
	@Transactional
	public String save(String eId, String start,String end,Model model, RedirectAttributes redirectAttributes) throws ParseException {
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
		WorkdayParaInfo workdayParaInfo=new WorkdayParaInfo();
		Equipment equipment=equipmentService.get(eId);
		workdayParaInfo.setEquipment(equipment);
		workdayParaInfo.setYear(year);
		workdayParaInfo.setMonth(String.valueOf(sm));
		workdayParaInfo=workdayParaInfoService.findByEIdAndDate(workdayParaInfo);
		//获得数据库中的day
		StringBuilder ssb=new StringBuilder(workdayParaInfo.getDay());

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
					ssb=new StringBuilder(workdayParaInfo.getDay());
					//替换
					ssb.replace(sd-1,sday,str2);
					//保存到数据库
					workdayParaInfo.setDay(ssb.toString());
					workdayParaInfoService.modifyRestDayById(workdayParaInfo);
					str2="";

					//如果只相差1个月，遍历结束时间这一个月的剩余时间，0-结束日
					for(int j=0;j<ed;j++){
						str2+="0";
					}
					Equipment equipment2=equipmentService.get(eId);
					workdayParaInfo.setEquipment(equipment);
					workdayParaInfo.setYear(year);
					workdayParaInfo.setMonth(String.valueOf(sm+(i+1)));
					workdayParaInfo=workdayParaInfoService.findByEIdAndDate(workdayParaInfo);
					ssb=new StringBuilder(workdayParaInfo.getDay());
					//替换
					ssb.replace(0,ed,str2);
					//保存到数据库
					workdayParaInfo.setDay(ssb.toString());
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
					workdayParaInfo.setMonth(String.valueOf(sm+(i+1)));
					workdayParaInfo=workdayParaInfoService.findByEIdAndDate(workdayParaInfo);
					ssb=new StringBuilder(workdayParaInfo.getDay());
					//替换
					ssb.replace(0,eday,str2);
					//保存到数据库
					workdayParaInfo.setDay(ssb.toString());
					workdayParaInfoService.modifyRestDayById(workdayParaInfo);
				}
			}
		}else{
			//结束月等于起始月
			//计算相差天数：结束时间-(起始时间-1)
			int num=ed-(sd-1);
			String str="";
			//根据相差天数进行循环修改值
			for(int i=0;i<num;i++){
				str+="0";
			}
			//替换day，起始时间-1，结束时间
			ssb.replace(sd-1,ed,str);
			//保存到数据库
			workdayParaInfo.setDay(ssb.toString());
			workdayParaInfoService.modifyRestDayById(workdayParaInfo);
		}
		return "redirect:" + Global.getAdminPath() + "/mj/workdayParaInfo/?repage";
	}

	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("mj:workdayParaInfo:edit")
	@RequestMapping(value = "delete")
	@Transactional
	public String delete(String ids,RedirectAttributes redirectAttributes) {
		String[] arr=ids.split(",");
		StringBuilder sb=new StringBuilder("");
		try{
			for(int i=0;i<arr.length;i++){
				sb=new StringBuilder("");
				String[] info=arr[i].split("-");
				WorkdayParaInfo workdayParaInfo=workdayParaInfoService.get(info[0]);
				sb.append(workdayParaInfo.getDay());
				sb.replace(Integer.parseInt(info[1])-1,Integer.parseInt(info[1]),"1");
				workdayParaInfo.setDay(sb.toString());
				int result=workdayParaInfoService.modifyRestDayById(workdayParaInfo);
				if (result!=1){
					throw new Exception();
				}
			}
			addMessage(redirectAttributes, "删除假期成功");
		}catch (Exception e){
			addMessage(redirectAttributes, "删除假期失败");
		}
		return "redirect:" + Global.getAdminPath() + "/mj/workdayParaInfo/?repage";
	}
	
}