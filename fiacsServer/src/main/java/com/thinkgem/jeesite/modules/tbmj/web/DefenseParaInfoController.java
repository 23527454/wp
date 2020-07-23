/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.tbmj.entity.DefenseParaInfo;
import com.thinkgem.jeesite.modules.tbmj.service.DefenseParaInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * access_defense_infoController
 * @author demo
 * @version 2020-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/defenseParaInfo")
public class DefenseParaInfoController extends BaseController {

	@Autowired
	private DefenseParaInfoService defenseParaInfoService;
	@Autowired
	private EquipmentService equipmentService;

	@RequiresPermissions("tbmj:defenseParaInfo:view")
	@RequestMapping("index")
	public String index(DefenseParaInfo defenseParaInfo, Model model) {
		return "modules/tbmj/defenseParaInfoIndex";
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DefenseParaInfo get(String id) {
		return defenseParaInfoService.get(id);
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
	@RequiresPermissions("tbmj:defenseParaInfo:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(String eid, HttpServletRequest request, HttpServletResponse response,
							 RedirectAttributes redirectAttributes) {
		try {
			String fileName = "防区参数信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<DefenseParaInfo> list=defenseParaInfoService.getByEId(eid);
			for(int i=0;i<list.size();i++){
				Equipment equipment=equipmentService.get(eid);
				list.get(i).setEquipment(equipment);
			}
			new ExportExcel("防区参数信息", DefenseParaInfo.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门禁参数信息失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/tbmj/defenseParaInfo/list?eid="+eid;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:defenseParaInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(String eid, Model model) {
		if(eid!=null && !eid.equals("")){
			List<DefenseParaInfo> list= defenseParaInfoService.getByEId(eid);
			model.addAttribute("list", list);
		}
		model.addAttribute("eid",eid);
		return "modules/tbmj/defenseParaInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("tbmj:defenseParaInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AccessDefenseInfo> listData(AccessDefenseInfo accessDefenseInfo, HttpServletRequest request, HttpServletResponse response) {
		accessDefenseInfo.setPage(new Page<>(request, response));
		Page<AccessDefenseInfo> page = accessDefenseInfoService.findPage(accessDefenseInfo);
		return page;
	}*/

	/**
	 * 粘贴数据
	 */
	@RequiresPermissions("tbmj:defenseParaInfo:edit")
	@PostMapping(value = "paste")
	//@ResponseBody
	public String paste(DefenseParaInfo defenseParaInfo, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		DefenseParaInfo copy_defenseParaInfo=(DefenseParaInfo)session.getAttribute("copy_defenseParaInfo");
		if(copy_defenseParaInfo!=null){
			defenseParaInfo.setDefensePos(copy_defenseParaInfo.getDefensePos());
			defenseParaInfo.setDefenseAreaType(copy_defenseParaInfo.getDefenseAreaType());
			defenseParaInfo.setDefenseAreaBypass(copy_defenseParaInfo.getDefenseAreaBypass());
			defenseParaInfo.setDefenseAreaAttr(copy_defenseParaInfo.getDefenseAreaAttr());
			defenseParaInfo.setAlarmDelayTime(copy_defenseParaInfo.getAlarmDelayTime());
			defenseParaInfo.setTimeframe(copy_defenseParaInfo.getTimeframe());
			defenseParaInfo.setRemarks(copy_defenseParaInfo.getRemarks());
			String[] sd=defenseParaInfo.getTimeframe().split(";");
			for(int i=0;i<sd.length;i++){
				//根据-拆分为每个时段的开始和结束时间
				String[] time=sd[i].split("-");
				model.addAttribute("timeStart"+(i+1),time[0]);
				model.addAttribute("timeEnd"+(i+1),time[1]);
			}
			addMessage(redirectAttributes,"粘贴成功!");
		}else{
			addMessage(redirectAttributes,"暂未复制内容!");
		}
		model.addAttribute("accessDefenseInfo", defenseParaInfo);
		return "modules/tbmj/defenseParaInfoForm";
	}


	/**
	 * 复制数据
	 */
	@RequiresPermissions("tbmj:defenseParaInfo:edit")
	@PostMapping(value = "copy")
	@ResponseBody
	public boolean copy(DefenseParaInfo defenseParaInfo,HttpServletRequest request,HttpServletResponse response,Model model) {
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

		defenseParaInfo.setTimeframe(str);


		DefenseParaInfo copy_defenseParaInfo=new DefenseParaInfo();
		copy_defenseParaInfo.setDefensePos(defenseParaInfo.getDefensePos());
		copy_defenseParaInfo.setDefenseAreaType(defenseParaInfo.getDefenseAreaType());
		copy_defenseParaInfo.setDefenseAreaBypass(defenseParaInfo.getDefenseAreaBypass());
		copy_defenseParaInfo.setDefenseAreaAttr(defenseParaInfo.getDefenseAreaAttr());
		copy_defenseParaInfo.setAlarmDelayTime(defenseParaInfo.getAlarmDelayTime());
		copy_defenseParaInfo.setTimeframe(defenseParaInfo.getTimeframe());
		copy_defenseParaInfo.setRemarks(defenseParaInfo.getRemarks());
		session.setAttribute("copy_defenseParaInfo",copy_defenseParaInfo);
		return true;
	}


	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:defenseParaInfo:view")
	@RequestMapping(value = "form")
	public String form(DefenseParaInfo defenseParaInfo, Model model) {
		defenseParaInfo=defenseParaInfoService.get(defenseParaInfo.getId());
		String[] sd=defenseParaInfo.getTimeframe().split(";");
		for(int i=0;i<sd.length;i++){
			//根据-拆分为每个时段的开始和结束时间
			String[] time=sd[i].split("-");
			model.addAttribute("timeStart"+(i+1),time[0]);
			model.addAttribute("timeEnd"+(i+1),time[1]);
		}
		model.addAttribute("accessDefenseInfo", defenseParaInfo);
		return "modules/tbmj/defenseParaInfoForm";
	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("tbmj:defenseParaInfo:edit")
	@PostMapping(value = "save")
	public String save(DefenseParaInfo defenseParaInfo,HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		String start1=request.getParameter("timeStart1");
		String start2=request.getParameter("timeStart2");
		String start3=request.getParameter("timeStart3");
		String start4=request.getParameter("timeStart4");
		String end1=request.getParameter("timeEnd1");
		String end2=request.getParameter("timeEnd2");
		String end3=request.getParameter("timeEnd3");
		String end4=request.getParameter("timeEnd4");

		String str=start1+"-"+end1+";"+start2+"-"+end2+";"+start3+"-"+end3+";"+start4+"-"+end4+";";
		try {
			defenseParaInfo.setTimeframe(str);
			defenseParaInfo.setId2(Integer.parseInt(defenseParaInfo.getId()));
			defenseParaInfoService.save(defenseParaInfo);
		}catch (Exception e){
			throw new ServiceException("修改数据", e);
		}
		addMessage(redirectAttributes, "修改防区信息成功");
		return "redirect:" + Global.getAdminPath() + "/tbmj/defenseParaInfo/";
	}
	
	/**
	 * 删除数据
	 */
	/*@RequiresPermissions("tbmj:defenseParaInfo:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AccessDefenseInfo accessDefenseInfo) {
		accessDefenseInfoService.delete(accessDefenseInfo);
		return renderResult(Global.TRUE, text("删除access_defense_info成功！"));
	}*/
	
}