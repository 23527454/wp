/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.TaskScheduleInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.SysConfig;
import com.thinkgem.jeesite.modules.guard.entity.TaskCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskLineInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfo;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfoSearchRequest;
import com.thinkgem.jeesite.modules.guard.service.ClassTaskInfoService;
import com.thinkgem.jeesite.modules.guard.service.ConnectEventService;
import com.thinkgem.jeesite.modules.guard.service.DownloadPersonService;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.guard.service.SysConfigService;
import com.thinkgem.jeesite.modules.guard.service.TaskScheduleInfoService;

/**
 * 排班信息Controller
 * 
 * @author Junbo
 * @version 2017-08-01
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/taskScheduleInfo")
public class TaskScheduleInfoController extends BaseController {

	@Autowired
	private TaskScheduleInfoService taskScheduleInfoService;

	@Autowired
	private ConnectEventService connectEventService;
	
	@Autowired
	private ClassTaskInfoService classTaskInfoService;
	
	@Autowired
	private EquipmentService equipmentService;

	@Autowired
	private SysConfigService sysConfigService;
	@ModelAttribute
	public TaskScheduleInfo get(@RequestParam(required = false) String id) {
		TaskScheduleInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = taskScheduleInfoService.get(id);
		}
		if (entity == null) {
			entity = new TaskScheduleInfo();
		}
		return entity;
	}

	@RequiresPermissions("guard:taskScheduleInfo:view")
	@RequestMapping(value = { "index" })
	public String index(TaskScheduleInfo taskScheduleInfo, Model model) {
		return "modules/guard/taskScheduleInfoIndex";
	}

	@RequiresPermissions("guard:taskScheduleInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(TaskScheduleInfoSearchRequest taskScheduleInfRequeset, HttpServletRequest request, HttpServletResponse response,
			Model model) {
//		List<ConnectEvent> connectEventList = connectEventService.findList(new ConnectEvent());
		taskScheduleInfRequeset.setNotInConnectEvent(true);
		Page<TaskScheduleInfo> page = taskScheduleInfoService.findPage(new Page<TaskScheduleInfo>(request, response), taskScheduleInfRequeset);

//		List<TaskScheduleInfo> LT = new ArrayList<TaskScheduleInfo>();
//		List<TaskScheduleInfo> taskList = page.getList();
//		for (int i = 0; i < taskList.size(); i++) {
//			if (taskListPD(connectList, page.getList(), i)) {
//				LT.add(page.getList().get(i));
//			}
//		}
//		page.setList(LT);
		model.addAttribute("page", page);
		model.addAttribute("taskScheduleInfo", taskScheduleInfRequeset);
		return "modules/guard/taskScheduleInfoList";
	}

//	// 排班不在交接事件中
//	private boolean taskListPD(List<ConnectEvent> connectList, List<TaskScheduleInfo> taskList, int i) {
//		for (int n = 0; n < connectList.size(); n++) {
//			if (connectList.get(n).getTaskId().equals(taskList.get(i).getId())) {
//				return false;
//			}
//		}
//		return true;
//	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param taskScheduleInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:taskScheduleInfo:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(TaskScheduleInfo taskScheduleInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			taskScheduleInfo.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, TaskScheduleInfo.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(taskScheduleInfo, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<TaskScheduleInfo> page = taskScheduleInfoService.findPage(new Page<TaskScheduleInfo>(request, response),
				taskScheduleInfo);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("verifyCar")) {
				String s = DictUtils.getDictLabel(e.getString("verifyCar"), "verify_car", "");
				e.put("verifyCar", s);
			}
			if (e.has("verifyInterMan")) {
				String s = DictUtils.getDictLabel(e.getString("verifyInterMan"), "inter_man", "");
				e.put("verifyInterMan", s);
			}
			if (e.has("verifyMoneyBox")) {
				String s = DictUtils.getDictLabel(e.getString("verifyMoneyBox"), "verify_locker", "");
				e.put("verifyMoneyBox", s);
			}
			if (e.has("taskType")) {
				String s = DictUtils.getDictLabel(e.getString("taskType"), "task_type", "");
				e.put("taskType", s);
			}
			if (e.has("taskTimeClass")) {
				String s = DictUtils.getDictLabel(e.getString("taskTimeClass"), "task_time_class", "");
				e.put("taskTimeClass", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}

	@RequiresPermissions("guard:taskScheduleInfo:view")
	@RequestMapping(value = "form")
	public String form(TaskScheduleInfo taskScheduleInfo, 
			@RequestParam("selectedClassTaskId")String selectedClassTaskId,
			@RequestParam("www")String www,
			Model model) {
		String interNum = "1";
		String SuperGoNum = "1";
		if(taskScheduleInfo.getIsNewRecord()){
			
//			$("#date1").val(dates);
//			$("#date2").val(dates);
//			$("#time1").val(time);
//			$("#time2").val(time);
			if(selectedClassTaskId != null){
				//根据选中的班组填充
				ClassTaskInfo classTaskInfo = classTaskInfoService.get(selectedClassTaskId);
				
				SysConfig sysConfig = new SysConfig();
				sysConfig.setAttribute("\'nInterNum\'");
				List<SysConfig> nInterNum = sysConfigService.findList(sysConfig);
				if(nInterNum!=null) {
					for (SysConfig sysConfig1 : nInterNum) {
						if("nInterNum".equals(sysConfig1.getAttribute())) {
							try {
								interNum = sysConfig1.getValue();
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								interNum = "1";
								e.printStackTrace();
							}
						}
					}
				}
				sysConfig.setAttribute("\'nSuperGoNum\'");
				List<SysConfig> nSuperGoNum = sysConfigService.findList(sysConfig);
				if(nInterNum!=null) {
					for (SysConfig sysConfig2 : nSuperGoNum) {
						if("nSuperGoNum".equals(sysConfig2.getAttribute())) {
							try {
								SuperGoNum = sysConfig2.getValue();
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								SuperGoNum = "1";
								e.printStackTrace();
							}
						}
					}
				}
				
//				model.addAttribute("selectedClassTaskInfo", classTaskInfo);
				taskScheduleInfo.setClassTaskInfo(classTaskInfo);
				taskScheduleInfo.setClassTaskId(classTaskInfo.getId());
				taskScheduleInfo.setVerifyCar(classTaskInfo.getVerifyCar());
				taskScheduleInfo.setVerifyMoneyBox(classTaskInfo.getVerifyLocker());
				/*taskScheduleInfo.setPatrolManNum(classTaskInfo.getPatrolManNum());
				taskScheduleInfo.setInterManNum(classTaskInfo.getInterManNum());*/
				taskScheduleInfo.setPatrolManNum(SuperGoNum);
				taskScheduleInfo.setInterManNum(interNum);
				taskScheduleInfo.setTaskTimeout(classTaskInfo.getTaskTimeout());
				
				List<ClassCarInfo> classCarInfoList = classTaskInfo.getClassCarInfoList();
				if(!CollectionUtils.isEmpty(classCarInfoList)){
					List<TaskCarInfo> taskCarInfoList = new ArrayList<TaskCarInfo>();
					TaskCarInfo taskCarInfo = null;
					for (ClassCarInfo classCarInfo : classCarInfoList) {
						taskCarInfo = new TaskCarInfo();
						taskCarInfo.setCarId(classCarInfo.getCarId());
						taskCarInfo.setCardNum(classCarInfo.getCardNum());
						taskCarInfo.setName(classCarInfo.getName());
						taskCarInfo.setCarplate(classCarInfo.getCarplate());
						taskCarInfoList.add(taskCarInfo);
					}
					taskScheduleInfo.setTaskCarInfoList(taskCarInfoList);
				}
				
				List<ClassPersonInfo> classPersonInfoList = classTaskInfo.getClassPersonInfoList();
				if(!CollectionUtils.isEmpty(classPersonInfoList)){
					List<TaskPersonInfo> taskPersonInfos = new ArrayList<TaskPersonInfo>();
					TaskPersonInfo taskPersonInfo = null;
					for (ClassPersonInfo classPersonInfo : classPersonInfoList) {
						taskPersonInfo = new TaskPersonInfo();
						taskPersonInfo.setAreaId(classPersonInfo.getAreaId());
						taskPersonInfo.setPersonId(classPersonInfo.getPersonId());
						taskPersonInfo.setFingerNum(classPersonInfo.getFingerNum());
						taskPersonInfo.setName(classPersonInfo.getName());
						taskPersonInfo.setIdentifyNumber(classPersonInfo.getIdentifyNumber());
						taskPersonInfos.add(taskPersonInfo);
					}
					taskScheduleInfo.setTaskPersonInfoList(taskPersonInfos);
				}
				List<LineNodes> lineNodeList = classTaskInfo.getLinNodesList();
				if(!CollectionUtils.isEmpty( lineNodeList)){
					List<TaskLineInfo> taskLineInfoList = new ArrayList<TaskLineInfo>();
					TaskLineInfo taskLineInfo = null;
					for (LineNodes lineNodes : lineNodeList) {
						taskLineInfo = new TaskLineInfo();
						Equipment equipment = equipmentService.getByOfficeId(lineNodes.getOffice().getId());
						taskLineInfo.setEquipmentId(equipment.getId());
						taskLineInfo.setName(lineNodes.getOffice().getName());
						taskLineInfoList.add(taskLineInfo);
					}
					taskScheduleInfo.setTaskLineInfoList(taskLineInfoList);
				}
			}
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 10);
			taskScheduleInfo.setTaskDate(DateUtils.formatDate(c.getTime()));
			taskScheduleInfo.setTaskTime(DateUtils.formatTime(c.getTime()));
		}
		if("1".equals(www)) {
			model.addAttribute("taskScheduleInfo", taskScheduleInfo);
			model.addAttribute("selectedClassTaskId", selectedClassTaskId);
			return "modules/guard/taskScheduleInfoForm1";
		}else {
		return backForm(taskScheduleInfo,selectedClassTaskId, model);
		}
	}
	
	private String backForm(TaskScheduleInfo taskScheduleInfo, String selectedClassTaskId, Model model) {
		model.addAttribute("taskScheduleInfo", taskScheduleInfo);
		model.addAttribute("selectedClassTaskId", selectedClassTaskId);
		return "modules/guard/taskScheduleInfoForm";
	}
	@RequestMapping(value = "form1")
	public String form1(TaskScheduleInfo taskScheduleInfo, 
			@RequestParam("selectedClassTaskId")String selectedClassTaskId,
			Model model) {
		if(taskScheduleInfo.getIsNewRecord()){
			
//			$("#date1").val(dates);
//			$("#date2").val(dates);
//			$("#time1").val(time);
//			$("#time2").val(time);
			if(selectedClassTaskId != null){
				//根据选中的班组填充
				ClassTaskInfo classTaskInfo = classTaskInfoService.get(selectedClassTaskId);
//				model.addAttribute("selectedClassTaskInfo", classTaskInfo);
				taskScheduleInfo.setClassTaskInfo(classTaskInfo);
				taskScheduleInfo.setClassTaskId(classTaskInfo.getId());
				taskScheduleInfo.setVerifyCar(classTaskInfo.getVerifyCar());
				taskScheduleInfo.setVerifyMoneyBox(classTaskInfo.getVerifyLocker());
				taskScheduleInfo.setPatrolManNum(classTaskInfo.getPatrolManNum());
				taskScheduleInfo.setInterManNum(classTaskInfo.getInterManNum());
				taskScheduleInfo.setTaskTimeout(classTaskInfo.getTaskTimeout());
				
				List<ClassCarInfo> classCarInfoList = classTaskInfo.getClassCarInfoList();
				if(!CollectionUtils.isEmpty(classCarInfoList)){
					List<TaskCarInfo> taskCarInfoList = new ArrayList<TaskCarInfo>();
					TaskCarInfo taskCarInfo = null;
					for (ClassCarInfo classCarInfo : classCarInfoList) {
						taskCarInfo = new TaskCarInfo();
						taskCarInfo.setCarId(classCarInfo.getCarId());
						taskCarInfo.setCardNum(classCarInfo.getCardNum());
						taskCarInfo.setName(classCarInfo.getName());
						taskCarInfo.setCarplate(classCarInfo.getCarplate());
						taskCarInfoList.add(taskCarInfo);
					}
					taskScheduleInfo.setTaskCarInfoList(taskCarInfoList);
				}
				
				List<ClassPersonInfo> classPersonInfoList = classTaskInfo.getClassPersonInfoList();
				if(!CollectionUtils.isEmpty(classPersonInfoList)){
					List<TaskPersonInfo> taskPersonInfos = new ArrayList<TaskPersonInfo>();
					TaskPersonInfo taskPersonInfo = null;
					for (ClassPersonInfo classPersonInfo : classPersonInfoList) {
						taskPersonInfo = new TaskPersonInfo();
						taskPersonInfo.setAreaId(classPersonInfo.getAreaId());
						taskPersonInfo.setPersonId(classPersonInfo.getPersonId());
						taskPersonInfo.setFingerNum(classPersonInfo.getFingerNum());
						taskPersonInfo.setName(classPersonInfo.getName());
						taskPersonInfo.setIdentifyNumber(classPersonInfo.getIdentifyNumber());
						taskPersonInfos.add(taskPersonInfo);
					}
					taskScheduleInfo.setTaskPersonInfoList(taskPersonInfos);
				}
				List<LineNodes> lineNodeList = classTaskInfo.getLinNodesList();
				if(!CollectionUtils.isEmpty( lineNodeList)){
					List<TaskLineInfo> taskLineInfoList = new ArrayList<TaskLineInfo>();
					TaskLineInfo taskLineInfo = null;
					for (LineNodes lineNodes : lineNodeList) {
						taskLineInfo = new TaskLineInfo();
						Equipment equipment = equipmentService.getByOfficeId(lineNodes.getOffice().getId());
						taskLineInfo.setEquipmentId(equipment.getId());
						taskLineInfo.setName(lineNodes.getOffice().getName());
						taskLineInfoList.add(taskLineInfo);
					}
					taskScheduleInfo.setTaskLineInfoList(taskLineInfoList);
				}
			}
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 10);
			taskScheduleInfo.setTaskDate(DateUtils.formatDate(c.getTime()));
			taskScheduleInfo.setTaskTime(DateUtils.formatTime(c.getTime()));
		}
		return backForm1(taskScheduleInfo,selectedClassTaskId, model);
	}
	
	private String backForm1(TaskScheduleInfo taskScheduleInfo, String selectedClassTaskId, Model model) {
		model.addAttribute("taskScheduleInfo", taskScheduleInfo);
		model.addAttribute("selectedClassTaskId", selectedClassTaskId);
		return "modules/guard/taskScheduleInfoDetailForm";
	}

	@RequiresPermissions("guard:taskScheduleInfo:edit")
	@RequestMapping(value = "save")
	public String save(TaskScheduleInfo taskScheduleInfo,
			BindingResult result, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedClassTaskId")String selectedClassTaskId) {
		if (!beanValidator(model, taskScheduleInfo)) {
			return backForm(taskScheduleInfo, selectedClassTaskId, model);
		}
		
		if(CollectionUtils.isEmpty(taskScheduleInfo.getTaskCarInfoList())){
			result.rejectValue("taskCarInfoList", "empty", "请选择车辆信息");
		}
		if(CollectionUtils.isEmpty(taskScheduleInfo.getTaskPersonInfoList())){
			result.rejectValue("taskPersonInfoList", "empty", "请选择人员信息");
		}
		
		if(result.hasErrors()){
			return backForm(taskScheduleInfo, selectedClassTaskId, model);
		}
		taskScheduleInfoService.save(taskScheduleInfo);
		addMessage(redirectAttributes, "保存排班信息成功");
		
		/*if("4".equals(taskScheduleInfo.getTaskType())) {*/
		//排班保存成功后，对车辆以及押款员进行同步
			download(taskScheduleInfo);
		//}
		if(null != selectedClassTaskId && selectedClassTaskId.equals(taskScheduleInfo.getClassTaskId())){
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/list?classTaskId="+selectedClassTaskId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/?repage";
		}
	}

	@RequiresPermissions("guard:taskScheduleInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(TaskScheduleInfo taskScheduleInfo, RedirectAttributes redirectAttributes,
			@RequestParam("selectedClassTaskId")String selectedClassTaskId) {

		taskScheduleInfoService.delete(taskScheduleInfo);
		addMessage(redirectAttributes, "删除排班信息成功");
		if(!StringUtils.isBlank(selectedClassTaskId)){
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/list?classTaskId="+selectedClassTaskId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/?repage";
		}
	}
	/*
	@RequiresPermissions("guard:taskScheduleInfo:edit")
	@RequestMapping(value = "download")
	public String download(TaskScheduleInfo taskScheduleInfo, 
			Model model, RedirectAttributes redirectAttributes,
			@RequestParam("selectedClassTaskId")String selectedClassTaskId) {
		if (!beanValidator(model, taskScheduleInfo)) {
			return backForm(taskScheduleInfo,selectedClassTaskId, model);
		}
		
		ClassTaskInfo classTaskInfo = classTaskInfoService.get(taskScheduleInfo.getClassTaskId());
		List<ClassPersonInfo> classPersonInfoList = classTaskInfo.getClassPersonInfoList();
		if(!CollectionUtils.isEmpty(classPersonInfoList)){
			List<TaskPersonInfo> taskPersonInfos = new ArrayList<TaskPersonInfo>();
			TaskPersonInfo taskPersonInfo = null;
			for (ClassPersonInfo classPersonInfo : classPersonInfoList) {
				taskPersonInfo = new TaskPersonInfo();
				taskPersonInfo.setAreaId(classPersonInfo.getAreaId());
				taskPersonInfo.setPersonId(classPersonInfo.getPersonId());
				taskPersonInfo.setFingerNum(classPersonInfo.getFingerNum());
				taskPersonInfo.setName(classPersonInfo.getName());
				taskPersonInfo.setIdentifyNumber(classPersonInfo.getIdentifyNumber());
				taskPersonInfos.add(taskPersonInfo);
			}
			taskScheduleInfo.setTaskPersonInfoList(taskPersonInfos);
		}
		List<LineNodes> lineNodeList = classTaskInfo.getLinNodesList();
		if(!CollectionUtils.isEmpty( lineNodeList)){
			List<TaskLineInfo> taskLineInfoList = new ArrayList<TaskLineInfo>();
			TaskLineInfo taskLineInfo = null;
			for (LineNodes lineNodes : lineNodeList) {
				taskLineInfo = new TaskLineInfo();
				Equipment equipment = equipmentService.getByOfficeId(lineNodes.getOffice().getId());
				taskLineInfo.setEquipmentId(equipment.getId());
				taskLineInfo.setName(lineNodes.getOffice().getName());
				taskLineInfoList.add(taskLineInfo);
			}
			taskScheduleInfo.setTaskLineInfoList(taskLineInfoList);
		}
		
		
		taskScheduleInfoService.insentDownload(taskScheduleInfo, DownloadEntity.DOWNLOAD_TYPE_ADD);
		addMessage(redirectAttributes, "排班同步成功");
		if(!StringUtils.isBlank(selectedClassTaskId)){
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/list?classTaskId="+selectedClassTaskId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/?repage";
		}
	}*/
	public void download(TaskScheduleInfo taskScheduleInfo) {
	/*	if (!beanValidator(model, taskScheduleInfo)) {
			return backForm(taskScheduleInfo,selectedClassTaskId, model);
		}*/
		
		ClassTaskInfo classTaskInfo = classTaskInfoService.get(taskScheduleInfo.getClassTaskId());
		List<ClassPersonInfo> classPersonInfoList = classTaskInfo.getClassPersonInfoList();
		if(!CollectionUtils.isEmpty(classPersonInfoList)){
			List<TaskPersonInfo> taskPersonInfos = new ArrayList<TaskPersonInfo>();
			TaskPersonInfo taskPersonInfo = null;
			for (ClassPersonInfo classPersonInfo : classPersonInfoList) {
				taskPersonInfo = new TaskPersonInfo();
				taskPersonInfo.setAreaId(classPersonInfo.getAreaId());
				taskPersonInfo.setPersonId(classPersonInfo.getPersonId());
				taskPersonInfo.setFingerNum(classPersonInfo.getFingerNum());
				taskPersonInfo.setName(classPersonInfo.getName());
				taskPersonInfo.setIdentifyNumber(classPersonInfo.getIdentifyNumber());
				taskPersonInfos.add(taskPersonInfo);
			}
			taskScheduleInfo.setTaskPersonInfoList(taskPersonInfos);
		}
		List<LineNodes> lineNodeList = classTaskInfo.getLinNodesList();
		if(!CollectionUtils.isEmpty( lineNodeList)){
			List<TaskLineInfo> taskLineInfoList = new ArrayList<TaskLineInfo>();
			TaskLineInfo taskLineInfo = null;
			for (LineNodes lineNodes : lineNodeList) {
				taskLineInfo = new TaskLineInfo();
				Equipment equipment = equipmentService.getByOfficeId(lineNodes.getOffice().getId());
				taskLineInfo.setEquipmentId(equipment.getId());
				taskLineInfo.setName(lineNodes.getOffice().getName());
				taskLineInfoList.add(taskLineInfo);
			}
			taskScheduleInfo.setTaskLineInfoList(taskLineInfoList);
		}
		
		
		taskScheduleInfoService.insentDownload(taskScheduleInfo, DownloadEntity.DOWNLOAD_TYPE_ADD);
		/*addMessage(redirectAttributes, "排班同步成功");*/
		/*if(!StringUtils.isBlank(selectedClassTaskId)){
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/list?classTaskId="+selectedClassTaskId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfo/?repage";
		}*/
	}
	/**
	 * 导出设备数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:taskScheduleInfo:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(TaskScheduleInfo taskScheduleInfo, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "排班数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TaskScheduleInfo> page = taskScheduleInfoService
					.findPage(new Page<TaskScheduleInfo>(request, response, -1), taskScheduleInfo);
			new ExportExcel("排班数据", TaskScheduleInfo.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出排班失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/taskScheduleInfo/list?repage";
	}

}