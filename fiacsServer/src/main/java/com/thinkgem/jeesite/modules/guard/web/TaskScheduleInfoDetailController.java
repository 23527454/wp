/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectPersonnel;
import com.thinkgem.jeesite.modules.guard.entity.TaskScheduleInfoDetail;
import com.thinkgem.jeesite.modules.guard.service.TaskScheduleInfoDetailService;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 排班明细Controller
 * 
 * @author Jumbo
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/taskScheduleInfoDetail")
public class TaskScheduleInfoDetailController extends BaseController {

	@Autowired
	private TaskScheduleInfoDetailService taskScheduleInfoDetailService;

	@ModelAttribute
	public TaskScheduleInfoDetail get(@RequestParam(required = false) String id) {
		TaskScheduleInfoDetail entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = taskScheduleInfoDetailService.get(id);
		}
		if (entity == null) {
			entity = new TaskScheduleInfoDetail();
		}
		return entity;
	}

	@RequiresPermissions("guard:taskScheduleInfoDetail:view")
	@RequestMapping(value = { "list", "" })
	public String list(TaskScheduleInfoDetail taskScheduleInfoDetail, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<TaskScheduleInfoDetail> p = new Page<TaskScheduleInfoDetail>(request, response);
		p.setOrderBy("g.id desc");
		if (taskScheduleInfoDetail != null) {
			if ("1".equals(taskScheduleInfoDetail.getSort()) || taskScheduleInfoDetail.getSort() == "1") {

				String type = taskScheduleInfoDetail.getSortType();
				if ("4".equals(type)) {
					p.setOrderBy("f.name asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("g.allot_date asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("g.allot_time asc");
				} else if ("1".equals(type)) {
					p.setOrderBy("g.id asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("g.task_date asc");
				} else if ("8".equals(type)) {
					p.setOrderBy("g.task_time asc");
				} else if ("9".equals(type)) {
					p.setOrderBy("g.task_type asc");
				} else if ("10".equals(type)) {
					p.setOrderBy("g.task_time_class asc");
				} /*else if ("9".equals(type)) {
					p.setOrderBy("l.id asc");
				} else if ("10".equals(type)) {
					p.setOrderBy("g.verify_car asc");
				} else if ("11".equals(type)) {
					p.setOrderBy("g.verify_inter_man asc");
				}*/
/*				if ("1".equals(type)) {
					p.setOrderBy("f.name asc");
				} else if ("2".equals(type)) {
					p.setOrderBy("g.task_date asc");
				} else if ("3".equals(type)) {
					p.setOrderBy("g.task_time asc");
				} else if ("4".equals(type)) {
					p.setOrderBy("a.task_id asc");
				} else if ("5".equals(type)) {
					p.setOrderBy("a.task_type asc");
				} else if ("6".equals(type)) {
					p.setOrderBy("o3.name asc");
				} else if ("7".equals(type)) {
					p.setOrderBy("o4.name asc");
				} else if ("8".equals(type)) {
					p.setOrderBy("d.carplate asc");
				} else if ("9".equals(type)) {
					p.setOrderBy("l.id asc");
				} else if ("10".equals(type)) {
					p.setOrderBy("g.verify_car asc");
				} else if ("11".equals(type)) {
					p.setOrderBy("g.verify_inter_man asc");
				}
*/			}
			if (taskScheduleInfoDetail.getSortType() != null) {
				if ("-1".equals(taskScheduleInfoDetail.getSort()) || taskScheduleInfoDetail.getSort() == "-1"
						|| taskScheduleInfoDetail.getSort() == null) {
					String type = taskScheduleInfoDetail.getSortType();
					/*if ("1".equals(type)) {
						p.setOrderBy("f.name desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("g.task_date desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("g.task_time desc");
					} else if ("4".equals(type)) {
						p.setOrderBy("a.task_id desc");
					} else if ("5".equals(type)) {
						p.setOrderBy("a.task_type desc");
					} else if ("6".equals(type)) {
						p.setOrderBy("o3.name desc");
					} else if ("7".equals(type)) {
						p.setOrderBy("o4.name desc");
					} else if ("8".equals(type)) {
						p.setOrderBy("d.carplate desc");
					} else if ("9".equals(type)) {
						p.setOrderBy("l.id desc");
					} else if ("10".equals(type)) {
						p.setOrderBy("g.verify_car desc");
					} else if ("11".equals(type)) {
						p.setOrderBy("g.verify_inter_man desc");
					}*/
					if ("4".equals(type)) {
						p.setOrderBy("f.name desc");
					} else if ("2".equals(type)) {
						p.setOrderBy("g.allot_date desc");
					} else if ("3".equals(type)) {
						p.setOrderBy("g.allot_time desc");
					} else if ("1".equals(type)) {
						p.setOrderBy("g.id desc");
					} else if ("5".equals(type)) {
						p.setOrderBy("g.task_date desc");
					} else if ("8".equals(type)) {
						p.setOrderBy("g.task_time desc");
					} else if ("9".equals(type)) {
						p.setOrderBy("g.task_type desc");
					} else if ("10".equals(type)) {
						p.setOrderBy("g.task_time_class desc");
					} 
				}
			}
		}

		Page<TaskScheduleInfoDetail> page = taskScheduleInfoDetailService.findPage(p, taskScheduleInfoDetail);
		model.addAttribute("page", page);
		return "modules/guard/taskScheduleInfoDetailList";
	}

	/**
	 * 获取列表数据（JSON）
	 * 
	 * @param taskScheduleInfoDetail
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:taskScheduleInfoDetail:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(TaskScheduleInfoDetail taskScheduleInfoDetail, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			taskScheduleInfoDetail.setCreateBy(user);
		}

		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (StringUtils.isNoneBlank(filterRules)) {
			JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
			List<Field> fields = new ArrayList();
			Reflections.getAllFields(fields, TaskScheduleInfoDetail.class);
			if (filter != null && filter.size() > 0) {
				for (Object o : filter) {
					JSONObject oo = (JSONObject) o;
					for (int i = 0; i < fields.size(); i++) {
						if (fields.get(i).getName().equals(oo.get("field"))) {
							Reflections.invokeSetter(taskScheduleInfoDetail, fields.get(i).getName(), oo.get("value"));
						}
					}
				}
			}

		}

		Page<TaskScheduleInfoDetail> page = taskScheduleInfoDetailService
				.findPage(new Page<TaskScheduleInfoDetail>(request, response), taskScheduleInfoDetail);
		JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
		JSONArray ja = jo.getJSONArray("list");
		for (int i = 0; ja != null && i < ja.size(); i++) {
			JSONObject e = (JSONObject) ja.get(i);
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

	@RequiresPermissions("guard:taskScheduleInfoDetail:view")
	@RequestMapping(value = "form")
	public String form(TaskScheduleInfoDetail taskScheduleInfoDetail, Model model) {
		model.addAttribute("taskScheduleInfoDetail", taskScheduleInfoDetail);
		return "modules/guard/taskScheduleInfoDetailForm";
	}

	@RequiresPermissions("guard:taskScheduleInfoDetail:edit")
	@RequestMapping(value = "save")
	public String save(TaskScheduleInfoDetail taskScheduleInfoDetail, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, taskScheduleInfoDetail)) {
			return form(taskScheduleInfoDetail, model);
		}
		taskScheduleInfoDetailService.save(taskScheduleInfoDetail);
		addMessage(redirectAttributes, "保存排班明细成功");
		return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfoDetail/?repage";
	}

	@RequiresPermissions("guard:taskScheduleInfoDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(TaskScheduleInfoDetail taskScheduleInfoDetail, RedirectAttributes redirectAttributes) {
		taskScheduleInfoDetailService.delete(taskScheduleInfoDetail);
		addMessage(redirectAttributes, "删除排班明细成功");
		return "redirect:" + Global.getAdminPath() + "/guard/taskScheduleInfoDetail/?repage";
	}
	
	/**
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("guard:taskScheduleInfoDetail:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(TaskScheduleInfoDetail taskScheduleInfoDetail, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "排班明细数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TaskScheduleInfoDetail> page = taskScheduleInfoDetailService
					.findPage(new Page<TaskScheduleInfoDetail>(request, response, -1), taskScheduleInfoDetail);
			new ExportExcel("排班明细数据", TaskScheduleInfoDetail.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出交接事件失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/guard/taskScheduleInfoDetail/list?repage";
	}

}