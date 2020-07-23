/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.tbmj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.guard.service.StaffService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.tbmj.entity.AccessParaInfo;
import com.thinkgem.jeesite.modules.tbmj.entity.Authorization;
import com.thinkgem.jeesite.modules.tbmj.entity.TimezoneInfo;
import com.thinkgem.jeesite.modules.tbmj.service.AccessParaInfoService;
import com.thinkgem.jeesite.modules.tbmj.service.AuthorizationService;
import com.thinkgem.jeesite.modules.tbmj.service.TimezoneInfoService;
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
import java.text.ParseException;
import java.util.List;

/**
 * power_authorizationController
 * @author demo
 * @version 2020-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/tbmj/authorization")
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

	@RequiresPermissions("tbmj:authorization:view")
	@RequestMapping(value = { "index" })
	public String index(Authorization authorization, Model model) {
		return "modules/tbmj/authorizationIndex";
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
	@RequiresPermissions("tbmj:authorization:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Authorization authorization, String accessParaInfoId, String name, String workNum, HttpServletRequest request, HttpServletResponse response,
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
		return "redirect:" + adminPath + "/tbmj/authorization/list?authorization="+authorization;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("tbmj:authorization:view")
	@RequestMapping(value = {"list", ""})
	public String list(Authorization authorization, String accessParaInfoId, String name, String workNum, HttpServletRequest request, HttpServletResponse response, Model model) {
		boolean isOK=false;

		if(authorization!=null){
			isOK=true;
			System.out.println("映射进实体类了.....................................................");
		}

		Authorization authorization2 = new Authorization();
		if(accessParaInfoId!=null && !accessParaInfoId.equals("")){
			isOK=true;
			authorization2.setAccessParaInfoId(accessParaInfoId);
			AccessParaInfo accessParaInfo=accessParaInfoService.get(accessParaInfoId);
			authorization2.setEquipmentId(String.valueOf(accessParaInfo.getEquipmentId()));
			authorization2.setDoorPos(accessParaInfo.getDoorPos());
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

				AccessParaInfo accessParaInfo=accessParaInfoService.get(accessParaInfoId);
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
		return "modules/tbmj/authorizationList";
	}
	
	/**
	 * 查询列表数据
	 */
	/*@RequiresPermissions("tbmj:authorization:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<authorization> listData(authorization authorization, HttpServletRequest request, HttpServletResponse response) {
		authorization.setPage(new Page<>(request, response));
		Page<authorization> page = authorizationService.findPage(authorization);
		return page;
	}*/

	/**
	 * 粘贴数据
	 */
	@RequiresPermissions("tbmj:authorization:edit")
	@PostMapping(value = "paste")
	//@ResponseBody
	public String paste(Authorization authorization, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		Authorization copy_authorization=(Authorization)session.getAttribute("copy_authorization");
		if(copy_authorization!=null){
			authorization.setTimezoneInfoNum(copy_authorization.getTimezoneInfoNum());
			authorization.setWorkdayNum(copy_authorization.getWorkdayNum());
			authorization.setPermissionGroup(copy_authorization.getPermissionGroup());
			authorization.setCheckPom(copy_authorization.getCheckPom());
			authorization.setRemarks(copy_authorization.getRemarks());
			addMessage(redirectAttributes,"粘贴成功!");
		}else{
			addMessage(redirectAttributes,"暂未复制内容!");
		}
		model.addAttribute("authorization", authorization);
		return "modules/tbmj/authorizationForm";
	}


	/**
 	* 复制数据
	 */
	@RequiresPermissions("tbmj:authorization:edit")
	@PostMapping(value = "copy")
	@ResponseBody
	public boolean copy(Authorization authorization,HttpServletRequest request,HttpServletResponse response,Model model) {
		HttpSession session=request.getSession();
		Authorization copy_authorization=new Authorization();
		copy_authorization.setTimezoneInfoNum(authorization.getTimezoneInfoNum());
		copy_authorization.setWorkdayNum(authorization.getWorkdayNum());
		copy_authorization.setPermissionGroup(authorization.getPermissionGroup());
		copy_authorization.setCheckPom(authorization.getCheckPom());
		copy_authorization.setRemarks(authorization.getRemarks());
		session.setAttribute("copy_authorization",copy_authorization);
		return true;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("tbmj:authorization:view")
	@RequestMapping(value = "form")
	public String form(Authorization authorization, String id, String accessParaInfoId,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		authorization=authorizationService.get(authorization.getId());
		authorization.setAccessParaInfoId(accessParaInfoId);
		model.addAttribute("authorization", authorization);
		return "modules/tbmj/authorizationForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("tbmj:authorization:edit")
	@PostMapping(value = "save")
	@Transactional
	@ResponseBody
	public String save(@Validated Authorization authorization, HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		String accessParaInfoId = request.getParameter("accessParaInfoId");
		String selStaffIds=request.getParameter("selStaffIds");
		String timezoneInfoNum = request.getParameter("timezoneInfoNum");
		String workDayNum = request.getParameter("workDayNum");
		String permissionGroup = request.getParameter("permissionGroup");
		String checkPom = request.getParameter("checkPom");
		String remarks = request.getParameter("remarks");

		String[] ids=selStaffIds.split(",");

		try {
			AccessParaInfo accessParaInfo =accessParaInfoService.get(accessParaInfoId);;
			for(String s:ids){
				authorization = new Authorization(timezoneInfoNum, s, accessParaInfoId, permissionGroup, checkPom, workDayNum);
				authorization.setRemarks(remarks);
				authorization.setStaff(staffService.get(s));
				Integer eid=accessParaInfoService.findEId(accessParaInfoId);
				Equipment equipment=equipmentService.get(eid.toString());
				authorization.setEquipment(equipment);
				authorization.setDoorPos(accessParaInfo.getDoorPos());
				if (authorizationService.getCountBySId(authorization.getStaffId(), String.valueOf(authorization.getEquipmentId()),authorization.getDoorPos()) >= 1) {
					addMessage(redirectAttributes, "该用户已经存在");
					return "3";
				}
				authorizationService.save(authorization);
			}

			Integer count=timezoneInfoService.findCountByNum(String.valueOf(authorization.getEquipment().getId()),authorization.getDoorPos(),authorization.getTimezoneInfoNum(),"1");
			if(count<=0){
				TimezoneInfo timezoneInfo =new TimezoneInfo();
				Integer eid=accessParaInfoService.findEId(accessParaInfoId);
				Equipment equipment=equipmentService.get(String.valueOf(accessParaInfo.getEquipmentId()));
				timezoneInfo.setEquipment(equipment);
				timezoneInfo.setAccessParaInfo(accessParaInfo);
				timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
				timezoneInfo.setTimeZoneType("1");
				timezoneInfo.setTimeZoneNum(timezoneInfoNum);
				timezoneInfo.setMon("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setTue("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setWed("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setThu("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setFri("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setSat("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setSun("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfoService.save(timezoneInfo);

				addMessage(redirectAttributes, "保存授权信息成功，请修改默认时区信息！");
				return "2";
			}else{
				addMessage(redirectAttributes, "保存授权信息成功");
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  "4";
		}

	}

	/**
	 * 修改数据
	 */
	@RequiresPermissions("tbmj:authorization:edit")
	@PostMapping(value = "modify")
	@Transactional
	@ResponseBody
	public String modify(@Validated Authorization authorization, HttpServletRequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		String id = request.getParameter("id");
		String accessParaInfoId = request.getParameter("accessParaInfoId");
		String timezoneInfoNum = request.getParameter("timezoneInfoNum");
		String workDayNum = request.getParameter("workDayNum");
		String permissionGroup = request.getParameter("permissionGroup");
		String checkPom = request.getParameter("checkPom");
		String remarks = request.getParameter("remarks");

		authorization=authorizationService.get(id);
		authorization.setTimezoneInfoNum(timezoneInfoNum);
		authorization.setWorkdayNum(workDayNum);
		authorization.setPermissionGroup(permissionGroup);
		authorization.setCheckPom(checkPom);
		authorization.setRemarks(remarks);

		try{
			authorizationService.save(authorization);

			AccessParaInfo accessParaInfo = accessParaInfoService.get(accessParaInfoId);

			Integer count=timezoneInfoService.findCountByNum(String.valueOf(accessParaInfo.getEquipmentId()),authorization.getDoorPos(),timezoneInfoNum,"1");
			if(count<=0){
				TimezoneInfo timezoneInfo =new TimezoneInfo();
				Integer eid=accessParaInfoService.findEId(accessParaInfoId);
				Equipment equipment=equipmentService.get(String.valueOf(accessParaInfo.getEquipmentId()));
				timezoneInfo.setEquipment(equipment);
				timezoneInfo.setAccessParaInfo(accessParaInfo);
				timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
				timezoneInfo.setTimeZoneType("1");
				timezoneInfo.setTimeZoneNum(timezoneInfoNum);
				timezoneInfo.setMon("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setTue("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setWed("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setThu("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setFri("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setSat("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfo.setSun("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
				timezoneInfoService.save(timezoneInfo);

				addMessage(redirectAttributes, "修改授权信息成功，请修改默认时区信息！");
				return "2";
			}else{
				addMessage(redirectAttributes, "修改授权信息成功");
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  "4";
		}

	}

	/**
	 * 删除数据
	 */
	@RequiresPermissions("tbmj:authorization:edit")
	@RequestMapping(value = "delete")
	public String delete(Authorization authorization, RedirectAttributes redirectAttributes) {
		int result=authorizationService.deleteById(authorization);
		if (result==1){
			addMessage(redirectAttributes, "删除授权信息成功");
		}else{
			addMessage(redirectAttributes, "删除授权信息失败");
		}
		return "redirect:" + Global.getAdminPath() + "/tbmj/authorization/?repage";
	}
	
}