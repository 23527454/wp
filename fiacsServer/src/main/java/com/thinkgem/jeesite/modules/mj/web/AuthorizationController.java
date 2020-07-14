/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.thinkgem.jeesite.modules.mj.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
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
	 * 查询列表
	 */
	@RequiresPermissions("mj:authorization:view")
	@RequestMapping(value = {"list", ""})
	public String list(Authorization authorization, String accessParaInfoId,String name,String workNum,HttpServletRequest request, HttpServletResponse response, Model model) {
		/*String accessParaInfoId=request.getParameter("accessParaInfoId");
		String name=request.getParameter("name");
		String weekNum=request.getParameter("weekNum");*/
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
			isOK=true;
			authorization2.setStaffName(name);
		}
		if(workNum!=null && !workNum.equals("")){
			isOK=true;
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

				System.out.println("office:"+page.getList().get(i).getOffice().getName());
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
			model.addAttribute("authorization",authorization2);
		}else if((authorization!=null && authorization.getAccessParaInfoId()!=null && !authorization.getAccessParaInfoId().equals("")) || (accessParaInfoId!=null && !accessParaInfoId.equals(""))){
<<<<<<< HEAD
			model.addAttribute("accessParaInfoId",accessParaInfoId);
=======
			//添加

			Authorization authorization2=new Authorization();
			authorization2.setAccessParaInfoId(accessParaInfoId);
			authorization2.setWorkDayNum("");
			authorization2.setCheckPom("1");
			model.addAttribute("authorization",authorization2);
>>>>>>> temp
		}else{
			addMessage(redirectAttributes, "请选择一个门！");
			return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
		}

		List<Staff> staffs=staffService.findAll();
		//List<Office> offices=officeService.findAll();
		/*List<AccessParaInfo> accessParaInfos=accessParaInfoService.findAll();
		model.addAttribute("accessParaInfos", accessParaInfos);*/
		model.addAttribute("staffs", staffs);
		//model.addAttribute("offices", offices);
		model.addAttribute("isNew",isNew);
		return "modules/mj/authorizationForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("mj:authorization:edit")
	@PostMapping(value = "save")
	public String save(@Validated Authorization authorization, HttpServletRequest request,BindingResult result, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		String id=request.getParameter("id");
		String accessParaInfoId=request.getParameter("accessParaInfoId");
		String staffId=request.getParameter("staffId");
		String timezoneInfoNum=request.getParameter("timezoneInfoNum");
		String workDayNum=request.getParameter("workDayNum");
		String staffGroup=request.getParameter("staffGroup");
		String checkPom=request.getParameter("checkPom");
		String remarks=request.getParameter("remarks");

		AccessParaInfo accessParaInfo=null;
		try{
			authorization=new Authorization(timezoneInfoNum,staffId,accessParaInfoId,staffGroup,checkPom,workDayNum);
			authorization.setId(id);
			authorization.setStaff(staffService.get(staffId));
			authorization.setValidityDate(authorization.getStaff().getEndDate());
			accessParaInfo=accessParaInfoService.get(accessParaInfoId);
			authorization.setOffice(officeService.get(authorization.getStaff().getId()));

			if(authorizationService.getCountBySId(authorization.getStaffId(),authorization.getAccessParaInfoId())>=1){
				addMessage(redirectAttributes, "该用户已经存在");
				return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
			}
			System.out.println("______________________________________"+accessParaInfoId);

			authorizationService.save(authorization);
		}catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		TimezoneInfo timezoneInfo=new TimezoneInfo();
		timezoneInfo.setAccessParaInfo(accessParaInfo);
		timezoneInfo.setTimeZoneNum(timezoneInfoNum);
		Integer count=timezoneInfoService.findCountByNum(accessParaInfoId,timezoneInfoNum);
		if(count<=0){
			addMessage(redirectAttributes, "保存授权信息成功，请修改默认时区信息！");

			return "redirect:" + Global.getAdminPath() + "/mj/timezoneInfo/list?timezoneInfo="+timezoneInfo;
		}else{
			addMessage(redirectAttributes, "保存授权信息成功");
			return "redirect:" + Global.getAdminPath() + "/mj/authorization/?repage";
		}
	}

	private String deleteById(Authorization authorization, Model model) {
		model.addAttribute("authorization", authorization);
		return "modules/mj/authorizationForm";
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