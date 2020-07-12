/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.ClassTaskInfoDao;
import com.thinkgem.jeesite.modules.guard.entity.AllotParam;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.DownloadEntity;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.service.ClassTaskInfoService;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxAllotService;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 款箱调拨Controller
 * @author Jumbo
 * @version 2017-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/moneyBoxAllot")
public class MoneyBoxAllotController extends BaseController {

	@Autowired
	private MoneyBoxAllotService moneyBoxAllotService;
	
	@Autowired
	private MoneyBoxService moneyBoxService;
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ClassTaskInfoService classTaskInfoService;
	@Autowired
	private AreaService areaService;
	@ModelAttribute
	public MoneyBoxAllot get(@RequestParam(required=false) String id) {
		MoneyBoxAllot entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = moneyBoxAllotService.get(id);
		}
		if (entity == null){
			entity = new MoneyBoxAllot();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:moneyBoxAllot:view")
	@RequestMapping(value = {"list", ""})
	public String list(MoneyBoxAllot moneyBoxAllot, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MoneyBoxAllot> page = moneyBoxAllotService.findPage(new Page<MoneyBoxAllot>(request, response), moneyBoxAllot); 
		model.addAttribute("page", page);
		return "modules/guard/moneyBoxAllotList";
	}
	
	/**
	 * 获取列表数据（JSON）
	 * @param moneyBoxAllot
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:moneyBoxAllot:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(MoneyBoxAllot moneyBoxAllot, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			moneyBoxAllot.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, MoneyBoxAllot.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(moneyBoxAllot, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<MoneyBoxAllot> page = moneyBoxAllotService.findPage(new Page<MoneyBoxAllot>(request, response), moneyBoxAllot); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:moneyBoxAllot:view")
	@RequestMapping(value = { "index" })
	public String index(MoneyBoxAllot moneyBoxAllot, Model model) {
		return "modules/guard/moneyBoxAllotIndex";
	}
	
	@RequiresPermissions("guard:moneyBoxAllot:view")
	@RequestMapping(value = "form")
	public String form(@RequestParam(required=false) String officeId, 
			RedirectAttributes redirectAttributes,
			Model model) {
		if(StringUtils.isBlank(officeId)){
			return "error/403";
		}
		MoneyBox moneyBox = new MoneyBox();
		moneyBox.setOffice(new Office(officeId));
		moneyBox.setBoxType("0");
		List<MoneyBox> exclusive = moneyBoxService.findTmpList(moneyBox);
		moneyBox.setBoxType("1");
		List<MoneyBox> tmp = moneyBoxService.findTmpList(moneyBox);
		List<MoneyBox> parentTmp = moneyBoxService.findParentTmpList(moneyBox);
		model.addAttribute("exclusive", exclusive);
		if(tmp==null) {
			model.addAttribute("tmp", parentTmp);
		}else {
			tmp.addAll(parentTmp);
			model.addAttribute("tmp", tmp);
		}
		Office office = officeService.get(officeId);
		if(null != office && (Office.TYPE_YYWD.equals(office.getType()) || Office.TYPE_ZYYWD.equals(office.getType()))){
			Equipment eq = equipmentService.findOne(new Equipment(new Office(officeId)));
			if(null != eq){
				model.addAttribute("equipmentId", eq.getId());
				//默认选中区域
				model.addAttribute("classTaskInfo", this.getClassTaskInfoRetriveFromArea(eq.getArea().getId()));
			}else{
				model.addAttribute("warnningMsg", "只有关联了设备的网点才能款箱调拨");
			}
			model.addAttribute("office", office);
		}else{
			model.addAttribute("warnningMsg", "只有营业网点、营业子网点才能款箱调拨");
		}
		model.addAttribute("currentDate", DateUtils.getDate());
		
		return "modules/guard/moneyBoxAllotForm";
	}
	
	/**
	 * 先从当前区域找班组， 如果找不到就从跟区域找班组
	 * @param areaId
	 * @return
	 */
	public ClassTaskInfo getClassTaskInfoRetriveFromArea(String areaId){
		ClassTaskInfo classTaskInfo  = new ClassTaskInfo();
		classTaskInfo.setArea(new Area(areaId));
		//从当前区域找班组
		List<ClassTaskInfo> classTaskInfos = classTaskInfoService.findList(classTaskInfo);
		if(!CollectionUtils.isEmpty(classTaskInfos)){
			if(classTaskInfos.size() == 0){
				return classTaskInfos.get(0);
			}
		}else{
			//从跟区域找班组
			Area area = areaService.get(new Area(areaId));
			if(!"0".equals(area.getParent().getId())){
				Area rootArea = areaService.get(area.getParent().getId());
				if(null != rootArea){
					ClassTaskInfo classTaskInfo2  = new ClassTaskInfo();
					classTaskInfo2.setArea(rootArea);
					List<ClassTaskInfo> classTaskInfos2 = classTaskInfoService.findList(classTaskInfo2);
					if(!CollectionUtils.isEmpty(classTaskInfos2)){
						return classTaskInfos2.get(0);
					}
				}
			}
		}
		return null;
	}

	@RequiresPermissions("guard:moneyBoxAllot:edit")
	@RequestMapping(value = "save")
	public String save(MoneyBoxAllot moneyBoxAllot, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, moneyBoxAllot)){
			return "error/403";
		}
		moneyBoxAllotService.save(moneyBoxAllot);
		addMessage(redirectAttributes, "保存款箱调拨成功");
		return "redirect:"+Global.getAdminPath()+"/guard/moneyBoxAllot/?repage";
	}
	
	@RequiresPermissions("guard:moneyBoxAllot:edit")
	@RequestMapping(value = "saveAllot")
	@ResponseBody
	public String saveAllot(AllotParam allot, Model model, RedirectAttributes redirectAttributes) {
		moneyBoxAllotService.saveBoxAllots(allot);
		return "{}";
	}

	@RequiresPermissions("guard:moneyBoxAllot:edit")
	@RequestMapping(value = "download")
	public String download(MoneyBoxAllot moneyBoxAllot, Model model, RedirectAttributes redirectAttributes) {
		moneyBoxAllotService.insentDownload(moneyBoxAllot, DownloadEntity.DOWNLOAD_TYPE_ADD);
		addMessage(redirectAttributes, "款箱调拨同步成功");
		return "redirect:" + Global.getAdminPath() + "/guard/moneyBoxAllot/?repage";
	}
	
	@RequiresPermissions("guard:moneyBoxAllot:edit")
	@RequestMapping(value = "delete")
	public String delete(MoneyBoxAllot moneyBoxAllot, RedirectAttributes redirectAttributes) {
		moneyBoxAllotService.delete(moneyBoxAllot);
		addMessage(redirectAttributes, "删除款箱调拨成功");
		return "redirect:"+Global.getAdminPath()+"/guard/moneyBoxAllot/?repage";
	}

}