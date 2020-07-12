/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.util.ServletContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.SysConfig;
import com.thinkgem.jeesite.modules.guard.service.AlarmEventService;
import com.thinkgem.jeesite.modules.guard.service.SysConfigService;

/**
 * 系统设置Controller
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/sysConfig")
public class SysConfigController extends BaseController {

	@Autowired
	private SysConfigService sysConfigService;

	@RequiresPermissions("guard:sysConfig:view")
	@RequestMapping(value = { "list", "" })
	public String list(SysConfig sysConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SysConfig> sysConfigList = sysConfigService.findList(sysConfig);

		for (int i = 0; i < sysConfigList.size(); i++) {
			String attribute = sysConfigList.get(i).getAttribute();
			String value = sysConfigList.get(i).getValue();
			if (attribute != null && !"".equals(attribute)) {
				if ("corporation_name".equals(attribute)) {
					sysConfig.setCorporationName(value);
				} else if ("system_name".equals(attribute)) {
					sysConfig.setSystemName(value);
				} else if ("reset_upload_connect_items".equals(attribute)) {
					sysConfig.setResetUploadConnectItems(value);
				} else if ("reset_upload_alarm_items".equals(attribute)) {
					sysConfig.setResetUploadAlarmItems(value);
				} else if ("sync_time1".equals(attribute)) {
					sysConfig.setSyncTime1(value);
				} else if ("sync_time2".equals(attribute)) {
					sysConfig.setSyncTime2(value);
				} else if ("sync_time3".equals(attribute)) {
					sysConfig.setSyncTime3(value);
				} else if ("is_dog_run".equals(attribute)) {
					sysConfig.setIsDogRun(value);
				} else if ("is_lock_protect".equals(attribute)) {
					sysConfig.setIsLockProtect(value);
				} else if ("is_auto_login".equals(attribute)) {
					sysConfig.setIsAutoLogin(value);
				} else if ("auto_open_when_start_computer".equals(attribute)) {
					sysConfig.setAutoOpenWhenStartComputer(value);
				} else if ("version_number".equals(attribute)) {
					sysConfig.setVersionNumber(value);
				} else if("nSuperGoNum".equals(attribute)){//押款员数量
					sysConfig.setSuperGoNum(value);
				} else if("nInterNum".equals(attribute)){
					sysConfig.setInterNum(value);//专员数量
				} else if("dt_task_cut".equals(attribute)){//款箱派送/回收切割时间 
					sysConfig.setDtTaskCut(value);
				}else if("staff_validity".equals(attribute)){//有效期 
					sysConfig.setStaffValidity(value);
				}  
			}
		}
		model.addAttribute("sysConfig", sysConfig);
		model.addAttribute("list", sysConfigList);
		return "modules/guard/sysConfigForm";
	}

	
	@RequiresPermissions("guard:sysConfig:view")
	@RequestMapping(value = "form")
	public String save(SysConfig sysConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysConfig)) {
			return "error/403";
		}
		sysConfigService.save(sysConfig);
		addMessage(redirectAttributes, "系统设置成功");
		return "redirect:" + Global.getAdminPath() + "/guard/sysConfig/list?repage";
	}

}