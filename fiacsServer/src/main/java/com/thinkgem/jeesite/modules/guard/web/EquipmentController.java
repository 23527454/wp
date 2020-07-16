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
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.mj.entity.*;
import com.thinkgem.jeesite.modules.mj.service.*;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
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
	public String download(Equipment equipment, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, equipment)) {
			return form(equipment, model, redirectAttributes);
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

				//根据门禁ID删除授权信息
				List<AccessParaInfo> accessParaInfos=accessParaInfoService.findListById(equipment.getId());
				for(AccessParaInfo a:accessParaInfos){
					authorizationService.deleteByAId(a.getId());
				}

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

					for(int j=1;j<=7;j++){
						TimezoneInfo timezoneInfo =new TimezoneInfo();
						timezoneInfo.setEquipment(equipment);
						timezoneInfo.setAccessParaInfo(accessParaInfo);
						timezoneInfo.setDoorPos("1");
						timezoneInfo.setTimeZoneType("1");
						timezoneInfo.setTimeZoneNum("1");
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
				}

				SecurityParaInfo securityParaInfo =new SecurityParaInfo(60,60,120,60,5,"1","1","1","1","1","1","1","1","1","1","1");
				securityParaInfo.setEquipment(equipment);
				securityParaInfoService.save(securityParaInfo);

				DefenseParaInfo defenseParaInfo =new DefenseParaInfo(1,1,"1",1,005,"00:00","23:59","00:00","00:00","00:00","00:00","00:00","00:00");
				defenseParaInfo.setEquipment(equipment);
				defenseParaInfoService.save(defenseParaInfo);

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
					workdayParaInfo.setEquipment(equipment);
					workdayParaInfo.setYear(year);
					workdayParaInfo.setMonth((i+1)+"");
					workdayParaInfo.setDay(sb.toString());
					workdayParaInfoService.save(workdayParaInfo);
				}
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
		//根据门禁ID删除授权信息
		List<AccessParaInfo> accessParaInfos=accessParaInfoService.findListById(equipment.getId());
		for(AccessParaInfo a:accessParaInfos){
			authorizationService.deleteByAId(a.getId());
		}

		//先删除时区表accessDoorTimezone、防盗信息表access_antitheft、防区信息表access_defense_info数据
		timezoneInfoService.deleteByEId(equipment.getId());
		securityParaInfoService.deleteByEId(equipment.getId());
		defenseParaInfoService.deleteByEId(equipment.getId());
		//再删除门禁信息表accessParaInfo数据
		accessParaInfoService.deleteByEId(equipment.getId());

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
	

}