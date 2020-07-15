/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.mj.entity.WorkdayParaInfo;
import com.thinkgem.jeesite.modules.mj.service.WorkdayParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
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
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(String eId, String mon, HttpServletRequest request, Model model) throws ParseException {
		WorkdayParaInfo workdayParaInfo=new WorkdayParaInfo();

		System.out.println(mon+"join.................................."+eId);

		workdayParaInfo.setMonth(mon);
		if(eId!=null && !eId.equals("")){
			workdayParaInfo.setEquipment(equipmentService.get(eId));
			System.out.println("workdayParaInfo.getEquipment().toString()++++++++++++++++++++++++++++++++++++++++++++++++"+workdayParaInfo.getEquipment().toString());
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			workdayParaInfo.setYear(year);
			List<WorkdayParaInfo> list=workdayParaInfoService.findAllByEIdAndYear(workdayParaInfo);
			List<Map<String, String>> restDay = new ArrayList<Map<String, String>>();
			for(int i=0;i<list.size();i++){
				String day=list.get(i).getDay();
				String id=list.get(i).getId();
				char a[]=day.toCharArray();
				for(int j=0;j<a.length;j++){
					String str=year+"-"+list.get(i).getMonth()+"-"+(j+1);
					if(String.valueOf(a[j]).equals("0")){
						Map<String,String> map = new HashMap();
						map.put("date",str);
						map.put("num",String.valueOf(j+1));
						map.put("id",id);
						restDay.add(map);
					}
				}
			}

			model.addAttribute("restDay", restDay);
			System.out.println("workdayParaInfo.toString()+++++++++++++++++++++++++++++++++++++++++++"+workdayParaInfo.toString());
			System.out.println("list.toString()+++++++++++++++++++++++++++++++++++++++++++"+list.toString());
		}

		model.addAttribute("eId", eId);
		model.addAttribute("mon", mon);
		return "modules/mj/workdayParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:workdayParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessWorkday> listData(AccessWorkday accessWorkday, HttpServletRequest request, HttpServletResponse response) {
		accessWorkday.setPage(new Page<>(request, response));
		Page<AccessWorkday> page = accessWorkdayService.findPage(accessWorkday);
		return page;
	}*/

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
	/*@RequiresPermissions("mj:workdayParaInfo:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AccessWorkday accessWorkday) {
		accessWorkdayService.save(accessWorkday);
		return renderResult(Global.TRUE, text("保存access_workday成功！"));
	}*/
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("mj:workdayParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessWorkday accessWorkday) {
		accessWorkdayService.delete(accessWorkday);
		return renderResult(Global.TRUE, text("删除access_workday成功！"));
	}*/
	
}