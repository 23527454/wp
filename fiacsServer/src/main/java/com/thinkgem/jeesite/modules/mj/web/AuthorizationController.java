/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.guard.service.StaffService;
import com.thinkgem.jeesite.modules.mj.entity.AccessParaInfo;
import com.thinkgem.jeesite.modules.mj.entity.Authorization;
import com.thinkgem.jeesite.modules.mj.entity.TimezoneInfo;
import com.thinkgem.jeesite.modules.mj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.mj.service.AuthorizationService;
import com.thinkgem.jeesite.modules.mj.service.TimezoneInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * power_authorizationController
 * @author demo
 * @version 2020-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/mj/authorization")
public class AuthorizationController extends BaseController {

	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private AccessParaInfoService accessParaInfoService;
	@Autowired
	private TimezoneInfoService timezoneInfoService;
	@Autowired
	private EquipmentService equipmentService;

	@RequiresPermissions("mj:authorization:view")
	@RequestMapping(value = { "index" })
	public String index(Authorization authorization, Model model) {
		return "modules/mj/authorizationIndex";
	}


	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Authorization get(String id) {
		return authorizationService.get(id);
	}

	/**
	 * 导出门禁权限数据
	 *
	 * @param authorization
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("mj:authorization:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Authorization authorization, String accessParaInfoId,String name,String workNum, HttpServletRequest request, HttpServletResponse response,
							 RedirectAttributes redirectAttributes) {
		Authorization authorization2 = new Authorization();
		if(accessParaInfoId!=null && !accessParaInfoId.equals("")){
			authorization2.setAccessParaInfoId(accessParaInfoId);
		}
		if(name!=null && !name.equals("")){
			authorization2.setStaffName(name);
		}
		if(workNum!=null && !workNum.equals("")){
			authorization2.setWorkNum(workNum);
		}
		try {
			String fileName = "门禁权限信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<Authorization> list = authorizationService.findList(authorization2);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setStaff(staffService.get(list.get(i).getStaffId()));

				AccessParaInfo accessParaInfo=accessParaInfoService.get(list.get(i).getAccessParaInfoId());
				Equipment equipment=equipmentService.get(accessParaInfo.getEquipmentId().toString());
				Office office=officeService.get(equipment.getOffice().getId());

				list.get(i).setOffice(office);
				list.get(i).setAccessParaInfo(accessParaInfo);
				list.get(i).getAccessParaInfo().setEquipment(equipment);
			}
			new ExportExcel("门禁权限信息", Authorization.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出门禁权限信息失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/mj/authorization/list?authorization="+authorization;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("mj:authorization:view")
	@RequestMapping(value = {"list", ""})
	public String list(Authorization authorization, String accessParaInfoId,String name,String workNum,HttpServletRequest request, HttpServletResponse response, Model model) {
		boolean isOK=false;

		if(authorization!=null){
			isOK=true;
			System.out.println("映射进实体类了.....................................................");
		}

		Authorization authorization2 = new Authorization();
		if(accessParaInfoId!=null && !accessParaInfoId.equals("")){
			isOK=true;
			authorization2.setAccessParaInfoId(accessParaInfoId);
		}
		if(name!=null && !name.equals("")){
			authorization2.setStaffName(name);
		}
		if(workNum!=null && !workNum.equals("")){
			authorization2.setWorkNum(workNum);
		}
		if(isOK){
			Page<Authorization> page = authorizationService.findPage(new Page<Authorization>(request, response), authorization2);
			for (int i = 0; i < page.getList().size(); i++) {
				page.getList().get(i).setStaff(staffService.get(page.getList().get(i).getStaffId()));

				AccessParaInfo accessParaInfo=accessParaInfoService.get(page.getList().get(i).getAccessParaInfoId());
				Equipment equipment=equipmentService.get(accessParaInfo.getEquipmentId().toString());
				Office office=officeService.get(equipment.getOffice().getId());

				page.getList().get(i).setOffice(office);
			}
			model.addAttribute("page", page);
		}

		model.addAttribute("authorization", authorization);
		model.addAttribute("accessParaInfoId",accessParaInfoId);
		model.addAttribute("name",name);
		model.addAttribute("workNum",workNum);
		return "modules/mj/authorizationList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("mj:authorization:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<authorization> listData(authorization authorization, HttpServletRequest request, HttpServletResponse response) {
		authorization.setPage(new Page<>(request, response));
		Page<authorization> page = authorizationService.findPage(authorization);
		return page;
	}*/

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("mj:authorization:view")
	@RequestMapping(value = "form")
	public String form(Authorization authorization,String id,String accessParaInfoId, Model model, RedirectAttributes redirectAttributes) {
		if(authorization!=null){
			System.out.println("映射到实体类了。。。。。。。。。。。。。。。。。。。。。。。。");
			authorization=authorizationService.get(authorization.getId());
			model.addAttribute("authorization", authorization);
		}

		boolean isNew=true;
		if((authorization!=null && authorization.getId()!=null && !authorization.getId().equals("")) || (id!=null && !id.equals(""))){
			//修改

			isNew=false;
			Authorization authorization2=authorizationService.get(id);
			Staff staff=staffService.get(authorization2.getStaffId());

			model.addAttribute("selStaffIds",staff.getId());
			model.addAttribute("selStaffNames",staff.getName());
			model.addAttribute("authorization",authorization2);
		}else if((authorization!=null && authorization.getAccessParaInfoId()!=null && !authorization.getAccessParaInfoId().equals("")) || (accessParaInfoId!=null && !accessParaInfoId.equals(""))){
			//添加

			Authorization authorization2=new Authorization();
			authorization2.setAccessParaInfoId(accessParaInfoId);
			authorization2.setWorkDayNum("");
			authorization2.setCheckPom("1");
			model.addAttribute("authorization",authorization2);
		}else{
			addMessage(redirectAttributes, "请选择一个门！");
			return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
		}
		model.addAttribute("isNew",isNew);
		return "modules/mj/authorizationForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("mj:authorization:edit")
	@PostMapping(value = "save")
	public String save(@Validated Authorization authorization, HttpServletRequest request,BindingResult result, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		String id = request.getParameter("id");
		String accessParaInfoId = request.getParameter("accessParaInfoId");
		String timezoneInfoNum = request.getParameter("timezoneInfoNum");
		String workDayNum = request.getParameter("workDayNum");
		String staffGroup = request.getParameter("staffGroup");
		String checkPom = request.getParameter("checkPom");
		String remarks = request.getParameter("remarks");
		String selStaffIds=request.getParameter("selStaffIds");
		String[] ids=selStaffIds.split(",");

		AccessParaInfo accessParaInfo = null;
		try {
			for(String s:ids){
				authorization = new Authorization(timezoneInfoNum, s, accessParaInfoId, staffGroup, checkPom, workDayNum);
				authorization.setId(id);
				authorization.setStaff(staffService.get(s));
				accessParaInfo = accessParaInfoService.get(accessParaInfoId);
				authorization.setOffice(officeService.get(authorization.getStaff().getId()));
				authorization.setRemarks(remarks);

				if (id != null && !id.equals("")) {
					//修改
					Authorization authorization2 = authorizationService.get(id);

					if (s.equals(authorization2.getStaffId()) && accessParaInfoId.equals(authorization2.getAccessParaInfoId())) {

					} else if (authorizationService.getCountBySId(authorization.getStaffId(), authorization.getAccessParaInfoId()) >= 1) {
						addMessage(redirectAttributes, "该用户已经存在");
						return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
					}
				} else {
					//添加
					if (authorizationService.getCountBySId(authorization.getStaffId(), authorization.getAccessParaInfoId()) >= 1) {
						addMessage(redirectAttributes, "该用户已经存在");
						return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
					}
				}

				authorizationService.save(authorization);
			}


		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		TimezoneInfo timezoneInfo2 = new TimezoneInfo();
		timezoneInfo2.setAccessParaInfo(accessParaInfo);
		timezoneInfo2.setTimeZoneNum(timezoneInfoNum);
		Integer count=timezoneInfoService.findCountByNum(accessParaInfoId,timezoneInfoNum);
		if(count<=0){
			for(int j=1;j<=7;j++){
				TimezoneInfo timezoneInfo =new TimezoneInfo();
				Integer eid=accessParaInfoService.findEId(accessParaInfoId);
				Equipment equipment=equipmentService.get(eid.toString());
				timezoneInfo.setEquipment(equipment);
				timezoneInfo.setAccessParaInfo(accessParaInfo);
				timezoneInfo.setDoorPos("1");
				timezoneInfo.setTimeZoneType("1");
				timezoneInfo.setTimeZoneNum(timezoneInfoNum);
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

			addMessage(redirectAttributes, "保存授权信息成功，请修改默认时区信息！");
			return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/list?accessParaInfo.id="+accessParaInfoId+"&timeZoneNum="+timezoneInfoNum;
		}else{
			addMessage(redirectAttributes, "保存授权信息成功");
			return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
		}
	}

	/**
	 * 删除数据
	 */
	@RequiresPermissions("mj:authorization:edit")
	@RequestMapping(value = "delete")
	public String delete(Authorization authorization, RedirectAttributes redirectAttributes) {
		int result=authorizationService.deleteById(authorization);
		if (result==1){
			addMessage(redirectAttributes, "删除授权信息成功");
		}else{
			addMessage(redirectAttributes, "删除授权信息失败");
		}
		return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
	}
	
}