/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.LineNodesDao;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.tbmj.entity.*;
import com.thinkgem.jeesite.modules.tbmj.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
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
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备信息Controller
 * 
 * @author Jumbo
 * @version 2017-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/equipment")
public class EquipmentController extends BaseController {

	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private LineNodesDao lineNodesDao;
	
	@Autowired
	private OfficeService officeService;

	@Autowired
	private AccessParaInfoService accessParaInfoService;
	@Autowired
	private TimezoneInfoService timezoneInfoService;
	@Autowired
	private SecurityParaInfoService securityParaInfoService;
	@Autowired
	private DefenseParaInfoService defenseParaInfoService;
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private WorkdayParaInfoService workdayParaInfoService;

	@Autowired
	private DownloadAccessParaInfoService downloadAccessParaInfoService;
	@Autowired
	private DownloadTimezoneInfoService downloadTimezoneInfoService;
	@Autowired
	private DownloadWorkdayParaInfoService downloadWorkdayParaInfoService;
	@Autowired
	private DownloadSecurityParaInfoService downloadSecurityParaInfoService;
	@Autowired
	private DownloadDefenseParaInfoService downloadDefenseParaInfoService;
	@Autowired
	private DownloadAuthorizationService downloadAuthorizationService;


	@ModelAttribute
	public Equipment get(@RequestParam(required = false) String id) {
		Equipment entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = equipmentService.get(id);
		}
		if (entity == null) {
			entity = new Equipment();
		}
		return entity;
	}

	@RequiresPermissions("guard:equipment:view")
	@RequestMapping(value = { "index" })
	public String index(Equipment equipment, Model model) {
		return "modules/guard/equipmentIndex";
	}

	@RequiresPermissions("guard:equipment:view")
	@RequestMapping(value = { "list", "" })
	public String list(Equipment equipment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Equipment> page = equipmentService.findPage(new Page<Equipment>(request, response), equipment);
		List<Office> officeList = officeService.findList(new Office());
		for (Office office : officeList) {
			for (Equipment equipment2 : page.getList()) {

				if(equipment2.getOffice().getId().equals(office.getId())) {
					equipment2.setOffice(office);
				}
			}

		}
		model.addAttribute("page", page);
		model.addAttribute("equipment", equipment);
		return "modules/guard/equipmentList";
	}

	/**
	 * 获取列表数据（JSON）
	 *
	 * @param equipment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:equipment:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(Equipment equipment, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			equipment.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, Equipment.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(equipment, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}
		}

		Page<Equipment> page = equipmentService.findPage(new Page<Equipment>(request, response), equipment);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
			if (e.has("equipType")) {
				String s = DictUtils.getDictLabel(e.getString("equipType"), "equipType", "");
				e.put("equipType", s);
			}
			if (e.has("status")) {
				String s = DictUtils.getDictLabel(e.getString("status"), "sb_status", "");
				e.put("status", s);
			}
			if (e.has("siteType")) {
				String s = DictUtils.getDictLabel(e.getString("siteType"), "site_type", "");
				e.put("siteType", s);
			}
			if (e.has("delFlag")) {
				String s = DictUtils.getDictLabel(e.getString("delFlag"), "del_flag", "");
				e.put("delFlag", s);
			}
		}
		return jo.toString();
	}

	/**
	 * 粘贴数据
	 */
	@RequiresPermissions("guard:equipment:edit")
	@GetMapping(value = "paste")
	//@ResponseBody
	public String paste(Equipment equipment, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		HttpSession session=request.getSession();
		Equipment copy_equipment=(Equipment)session.getAttribute("copy_equipment");
		if(copy_equipment!=null){
			equipment.setSerialNum(copy_equipment.getSerialNum());
			equipment.setSiteType(copy_equipment.getSiteType());
			equipment.setAccessType(copy_equipment.getAccessType());
			equipment.setEquipType(copy_equipment.getEquipType());
			equipment.setNetMask(copy_equipment.getNetMask());
			equipment.setNetGate(copy_equipment.getNetGate());
			equipment.setIp(copy_equipment.getIp());
			equipment.setPort(copy_equipment.getPort());
			equipment.setPrintServerIp(copy_equipment.getPrintServerIp());
			equipment.setPrintServerPort(copy_equipment.getPrintServerPort());
			equipment.setUploadEventSrvIp(copy_equipment.getUploadEventSrvIp());
			equipment.setUploadEventSrvPort(copy_equipment.getUploadEventSrvPort());
			equipment.setRemarks(copy_equipment.getRemarks());
			addMessage(redirectAttributes,"粘贴成功!");
		}else{
			addMessage(redirectAttributes,"暂未复制内容!");
		}
		model.addAttribute("equipment", equipment);

		return backForm(equipment, model);
	}


	/**
	 * 复制数据
	 */
	@RequiresPermissions("guard:equipment:edit")
	@PostMapping(value = "copy")
	@ResponseBody
	public boolean copy(Equipment equipment, HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session=request.getSession();
		session.setAttribute("copy_equipment",equipment);
		return true;
	}

	@RequiresPermissions("guard:equipment:view")
	@RequestMapping(value = "form")
	public String form(Equipment equipment, Model model, RedirectAttributes redirectAttributes) {
		if (equipment.getIsNewRecord()) {
			Equipment equ = equipmentService.getByOfficeId(equipment.getEqu_office_id());
			if (null != equ) {
				addMessage(redirectAttributes, "已存关联设备");
				return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
			}
			if (!Office.TYPE_ZXJK.equals(equipment.getEqu_office_type()) && !Office.TYPE_YYWD.equals(equipment.getEqu_office_type()) && !Office.TYPE_ZYYWD.equals(equipment.getEqu_office_type())) {
				addMessage(redirectAttributes, "只有中心金库、营业网点、营业子网点才能关联设备");
				return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
			}

			Office office = officeService.get(equipment.getEqu_office_id());
			equipment.setOffice(office);

			equipment.setPort("8001");
			equipment.setIp("192.168.1.118");
			equipment.setUploadEventSrvIp("192.168.1.100");
			equipment.setUploadEventSrvPort("10001");
			equipment.setNetGate("192.168.1.1");
			equipment.setNetMask("255.255.255.0");
			equipment.setPrintServerIp("192.168.1.100");
			equipment.setPrintServerPort("15000");
		}

		return backForm(equipment, model);
	}

	private String backForm(Equipment equipment, Model model) {
		model.addAttribute("equipment", equipment);
		return "modules/guard/equipmentForm";
	}

	@RequiresPermissions("guard:equipment:view")
	@RequestMapping(value = "paramSetting")
	public String doorParamSetting(Equipment equipment, Model model, RedirectAttributes redirectAttributes) {
		Equipment equ = equipmentService.getByOfficeId(equipment.getEqu_office_id());
		if (!Office.TYPE_ZXJK.equals(equipment.getEqu_office_type()) && !Office.TYPE_YYWD.equals(equipment.getEqu_office_type()) && !Office.TYPE_ZYYWD.equals(equipment.getEqu_office_type())) {
			addMessage(redirectAttributes, "只有中心金库、营业网点、营业子网点才能关联设备");
			return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
		}
		if (null == equ) {
			addMessage(redirectAttributes, "无关联设备，不可进行参数设置");
			return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
		}
		model.addAttribute("equipment", equipmentService.get(equ.getId()));
		return "modules/guard/doorParamSettingForm";
	}

	@RequiresPermissions("guard:equipment:edit")
	@RequestMapping(value = "download")
	@Transactional
	public String download(Equipment equipment, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, equipment)) {
			return form(equipment, model, redirectAttributes);
		}

		//同步该设备下所有门禁参数
		List<AccessParaInfo> accessParaInfos=accessParaInfoService.getByEId(equipment.getId());
		for(AccessParaInfo a:accessParaInfos){
			DownloadAccessParaInfo downloadAccessParaInfo=new DownloadAccessParaInfo();
			downloadAccessParaInfo.setAccessParaInfoId(a.getId());
			downloadAccessParaInfo.setEquipmentId(String.valueOf(a.getEquipmentId()));
			downloadAccessParaInfo.setIsDownload("0");
			downloadAccessParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadAccessParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadAccessParaInfoService.countByEntity(downloadAccessParaInfo)==0){
				downloadAccessParaInfoService.save(downloadAccessParaInfo);
			}
		}

		//同步该设备下所有时区参数
		List<TimezoneInfo> timezoneInfos=timezoneInfoService.getByEId(equipment.getId());
		for(TimezoneInfo t:timezoneInfos){
			DownloadTimezoneInfo downloadTimezoneInfo=new DownloadTimezoneInfo();
			downloadTimezoneInfo.setTimezoneParaInfoId(t.getId());
			downloadTimezoneInfo.setEquipmentId(String.valueOf(equipment.getId()));
			downloadTimezoneInfo.setIsDownload("0");
			downloadTimezoneInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadTimezoneInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadTimezoneInfoService.countByEntity(downloadTimezoneInfo)==0){
				downloadTimezoneInfoService.save(downloadTimezoneInfo);
			}
		}

		//同步该设备下所有工作日参数
		List<WorkdayParaInfo> workdayParaInfos=workdayParaInfoService.getByEId(equipment.getId());
		for(WorkdayParaInfo w:workdayParaInfos){
			DownloadWorkdayParaInfo downloadWorkdayParaInfo=new DownloadWorkdayParaInfo();
			downloadWorkdayParaInfo.setWorkdayParaInfoId(w.getId());
			downloadWorkdayParaInfo.setEquipmentId(String.valueOf(equipment.getId()));
			downloadWorkdayParaInfo.setIsDownload("0");
			downloadWorkdayParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadWorkdayParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadWorkdayParaInfoService.countByEntity(downloadWorkdayParaInfo)==0){
				downloadWorkdayParaInfoService.save(downloadWorkdayParaInfo);
			}
		}

		//同步该设备下所有防盗参数
		List<SecurityParaInfo> securityParaInfos=securityParaInfoService.getByEId(equipment.getId());
		for(SecurityParaInfo s:securityParaInfos){
			DownloadSecurityParaInfo downloadSecurityParaInfo=new DownloadSecurityParaInfo();
			downloadSecurityParaInfo.setSecurityParaInfoId(s.getId());
			downloadSecurityParaInfo.setEquipmentId(String.valueOf(equipment.getId()));
			downloadSecurityParaInfo.setIsDownload("0");
			downloadSecurityParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadSecurityParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadSecurityParaInfoService.countByEntity(downloadSecurityParaInfo)==0){
				downloadSecurityParaInfoService.save(downloadSecurityParaInfo);
			}
		}

		//同步该设备下所有防区参数
		List<DefenseParaInfo> defenseParaInfos=defenseParaInfoService.getByEId(equipment.getId());
		for(DefenseParaInfo d:defenseParaInfos){
			DownloadDefenseParaInfo downloadDefenseParaInfo=new DownloadDefenseParaInfo();
			downloadDefenseParaInfo.setDefenseParaInfoId(d.getId());
			downloadDefenseParaInfo.setEquipmentId(String.valueOf(equipment.getId()));
			downloadDefenseParaInfo.setIsDownload("0");
			downloadDefenseParaInfo.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadDefenseParaInfo.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadDefenseParaInfoService.countByEntity(downloadDefenseParaInfo)==0){
				downloadDefenseParaInfoService.save(downloadDefenseParaInfo);
			}
		}

		//同步该设备下所有权限参数
		List<Authorization> authorizations=authorizationService.getByEId(equipment.getId());
		for(Authorization a:authorizations){
			DownloadAuthorization downloadAuthorization=new DownloadAuthorization();
			downloadAuthorization.setAuthorizationId(a.getId());
			downloadAuthorization.setEquipmentId(String.valueOf(equipment.getId()));
			downloadAuthorization.setIsDownload("0");
			downloadAuthorization.setRegisterTime(DateUtils.formatDateTime(new Date()));
			downloadAuthorization.setDownloadType(DownloadEntity.DOWNLOAD_TYPE_ADD);
			if(downloadAuthorizationService.countByEntity(downloadAuthorization)==0){
				downloadAuthorizationService.save(downloadAuthorization);
			}
		}



		equipmentService.insentDownload(equipment);
		addMessage(redirectAttributes, "设备同步成功");
		return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
	}

	@Transactional
	@RequiresPermissions("guard:equipment:edit")
	@RequestMapping(value = "save")
	public String save(Equipment equipment,BindingResult result,  Model model, RedirectAttributes redirectAttributes) {
		//判断门控类型与互锁方式是否匹配
		if((equipment.getSiteType().equals("0")||equipment.getSiteType().equals("2"))&&(equipment.getAccessType()==1||equipment.getAccessType()==2)){
			result.rejectValue("siteType", "duplicate", "金库门和防护隔离门只有单门！");
		}
		if(equipment.getSiteType().equals("1") && equipment.getAccessType()==0){
			result.rejectValue("siteType", "duplicate", "联动门只有双门和四门！");
		}
		if (!beanValidator(model, equipment)) {
			return form(equipment, model, redirectAttributes);
		}
		List<Equipment> equipmentList = equipmentService.findList(new Equipment());
		if (equipment.getIsNewRecord()) {
			if (equipmentList.size() >= (Integer) UserUtils.getCache(UserUtils.CACHE_FUNCTION_EQU)) {
				addMessage(redirectAttributes, "最多添加" + UserUtils.getCache(UserUtils.CACHE_FUNCTION_EQU) + "台设备");
				return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
			}
		}

		if(equipmentService.countByIp(equipment.getId(), equipment.getIp()) > 0) {
			result.rejectValue("ip", "duplicate", "Ip地址已经存在");
		}

		if(equipmentService.countBySerialNum(equipment.getId(), equipment.getSerialNum()) > 0) {
			result.rejectValue("serialNum", "duplicate", "序列号已经存在");
		}

		if(result.hasErrors()) {
			return backForm(equipment, model);
		}
		try {
			Equipment e2=equipmentService.get(equipment.getId());
			equipmentService.save(equipment);

			//如果是第一次添加设备，或者是在进行修改时将设备类型和开锁方式修改了，进入方法删除门禁
			if (e2==null||(e2!=null && (!equipment.getSiteType().equals(e2.getSiteType()) || equipment.getAccessType()!=e2.getAccessType()))){

				//根据设备ID删除授权信息
				authorizationService.deleteByEId(equipment.getId());

				accessParaInfoService.deleteByEId(equipment.getId());
				timezoneInfoService.deleteByEId(equipment.getId());
				securityParaInfoService.deleteByEId(equipment.getId());
				defenseParaInfoService.deleteByEId(equipment.getId());
				workdayParaInfoService.deleteAllByEId(equipment.getId());
				int num=equipment.getAccessType()==0?1:equipment.getAccessType()==1?2:4;
				for(int i=1;i<=num;i++){
					AccessParaInfo accessParaInfo=new AccessParaInfo();
					accessParaInfo.setEquipment(equipment);
					accessParaInfo.setDoorPos(String.valueOf(i));
					accessParaInfo.setDoorRelayTime(5);
					accessParaInfo.setDoorDelayTime(120L);
					accessParaInfo.setEnterOperaTime(10L);
					accessParaInfo.setCheckOperaTime(30L);
					accessParaInfo.setOutTipsTime(5L);
					accessParaInfo.setAlarmIntervalTime(5L);
					accessParaInfo.setRemoteOverTime(15L);
					accessParaInfo.setAuthType("6");
					accessParaInfo.setCenterPermit(0L);
					accessParaInfo.setCombNum(2);
					accessParaInfo.setBase1("00");
					accessParaInfo.setBase2("00");
					accessParaInfo.setBase3("00");
					accessParaInfo.setBase4("00");
					accessParaInfo.setBase5("00");
					accessParaInfo.setBase6("00");
					accessParaInfo.setWorkTime1("00");
					accessParaInfo.setWorkTime2("00");
					accessParaInfo.setNetOutAge1("00");
					accessParaInfo.setNetOutAge2("00");
					accessParaInfoService.save(accessParaInfo);

					TimezoneInfo timezoneInfo =new TimezoneInfo();
					timezoneInfo.setEquipment(equipment);
					timezoneInfo.setEquipmentId(equipment.getId());
					timezoneInfo.setAccessParaInfo(accessParaInfo);
					timezoneInfo.setDoorPos(accessParaInfo.getDoorPos());
					timezoneInfo.setTimeZoneType("1");
					timezoneInfo.setTimeZoneNum("1");
					timezoneInfo.setMon("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo.setTue("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo.setWed("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo.setThu("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo.setFri("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo.setSat("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo.setSun("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfoService.save(timezoneInfo);

					TimezoneInfo timezoneInfo2 =new TimezoneInfo();
					timezoneInfo2.setEquipment(equipment);
					timezoneInfo2.setEquipmentId(equipment.getId());
					timezoneInfo2.setAccessParaInfo(accessParaInfo);
					timezoneInfo2.setDoorPos(accessParaInfo.getDoorPos());
					timezoneInfo2.setTimeZoneType("1");
					timezoneInfo2.setTimeZoneNum("2");
					timezoneInfo2.setMon("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo2.setTue("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo2.setWed("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo2.setThu("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo2.setFri("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo2.setSat("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo2.setSun("00:00-23:59;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfoService.save(timezoneInfo2);

					TimezoneInfo timezoneInfo3 =new TimezoneInfo();
					timezoneInfo3.setEquipment(equipment);
					timezoneInfo3.setEquipmentId(equipment.getId());
					timezoneInfo3.setAccessParaInfo(accessParaInfo);
					timezoneInfo3.setDoorPos(accessParaInfo.getDoorPos());
					timezoneInfo3.setTimeZoneType("2");
					timezoneInfo3.setTimeZoneNum("2");
					timezoneInfo3.setMon("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo3.setTue("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo3.setWed("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo3.setThu("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo3.setFri("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo3.setSat("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo3.setSun("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfoService.save(timezoneInfo3);

					TimezoneInfo timezoneInfo4 =new TimezoneInfo();
					timezoneInfo4.setEquipment(equipment);
					timezoneInfo4.setEquipmentId(equipment.getId());
					timezoneInfo4.setAccessParaInfo(accessParaInfo);
					timezoneInfo4.setDoorPos(accessParaInfo.getDoorPos());
					timezoneInfo4.setTimeZoneType("2");
					timezoneInfo4.setTimeZoneNum("3");
					timezoneInfo4.setMon("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo4.setTue("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo4.setWed("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo4.setThu("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo4.setFri("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo4.setSat("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfo4.setSun("00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
					timezoneInfoService.save(timezoneInfo4);

				}

				SecurityParaInfo securityParaInfo =new SecurityParaInfo(60,60,120,60,5,"1","1","1","1","1","1","1","1","1","1","1");
				securityParaInfo.setEquipment(equipment);
				securityParaInfo.setEquipmentId(equipment.getId());
				securityParaInfoService.save(securityParaInfo);

				DefenseParaInfo defenseParaInfo =new DefenseParaInfo(1,1,"1",1,005,"00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;");
				defenseParaInfo.setEquipment(equipment);
				defenseParaInfoService.save(defenseParaInfo);

				//添加工作日表信息
				StringBuffer sb=new StringBuffer("");
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c2 = Calendar.getInstance();
				String year = String.valueOf(c2.get(Calendar.YEAR));
				Date date = new Date();
				WorkdayParaInfo workdayParaInfo =new WorkdayParaInfo();
				workdayParaInfo.setEquipment(equipment);
				workdayParaInfo.setEquipmentId(equipment.getId());
				workdayParaInfo.setYear(year);
				Integer maxNum=workdayParaInfoService.selMaxNum();
				workdayParaInfo.setWorkdayNum(maxNum==null?1:maxNum+1);
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
					setWorkParaInfoDays(String.valueOf((i+1)),sb.toString(),workdayParaInfo);
				}
				workdayParaInfoService.save(workdayParaInfo);
			}

		} catch (Exception e) {
			throw new ServiceException("保存数据", e);
		}
		addMessage(redirectAttributes, "保存设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
	}

	@RequiresPermissions("guard:equipment:edit")
	@RequestMapping(value = "saveSetting")
	public String saveSetting(Equipment equipment,BindingResult result,  Model model, RedirectAttributes redirectAttributes) {
		
		equipmentService.saveSetting(equipment);
		
		addMessage(redirectAttributes, "保存门禁设置成功");
		return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
	}

	@Transactional
	@RequiresPermissions("guard:equipment:edit")
	@RequestMapping(value = "delete")
	public String delete(Equipment equipment, RedirectAttributes redirectAttributes) {

		List<LineNodes> lineNodesList = lineNodesDao.findList();
		for (int i = 0; i < lineNodesList.size(); i++) {
			if (lineNodesList.get(i).getOffice() != null
					&& equipment.getId().equals(lineNodesList.get(i).getOffice().getId())) {
				addMessage(redirectAttributes, "不可删除在线路节点中的设备");
				return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
			}
		}
		//根据设备ID删除授权信息
		authorizationService.deleteByEId(equipment.getId());

		//先删除时区表accessDoorTimezone、防盗信息表access_antitheft、防区信息表access_defense_info数据
		timezoneInfoService.deleteByEId(equipment.getId());
		securityParaInfoService.deleteByEId(equipment.getId());
		defenseParaInfoService.deleteByEId(equipment.getId());
		//再删除门禁信息表accessParaInfo数据
		accessParaInfoService.deleteByEId(equipment.getId());
		workdayParaInfoService.deleteAllByEId(equipment.getId());
		//删除设备信息
		equipmentService.delete(equipment);
		addMessage(redirectAttributes, "删除设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/guard/equipment/?repage";
	}

	/**
	 * 导出设备数据
	 * 
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:equipment:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Equipment equipment, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		try {
			String fileName = "设备数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Equipment> page = equipmentService.findPage(new Page<Equipment>(request, response, -1), equipment);
			new ExportExcel("设备数据", Equipment.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出设备失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/equipment/list?repage";
	}

	@RequiresPermissions("guard:equipment:edit")
	@RequestMapping(value = "getNumber")
	@ResponseBody
	public String getNumber(Equipment equipment, Model model, RedirectAttributes redirectAttributes) {
		Equipment equ = equipmentService.get(equipment.getId());
		JSONObject obj = new JSONObject();
		if ("".equals(equ) || equ == null) {
			obj.put("number", "0");
		} else {
			obj.put("number", "1");
		}
		return obj.toString();
	}
	
	
	@RequiresPermissions("guard:equipment:edit")
	@RequestMapping(value = "parametersDownload")
	@ResponseBody
	public String parametersDownload(@RequestParam Map<String, Object> params) {
		try {
			equipmentService.parametersDownload(params);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
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
}