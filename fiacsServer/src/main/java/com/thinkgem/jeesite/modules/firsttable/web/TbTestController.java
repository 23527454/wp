/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.firsttable.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.firsttable.entity.TbTest;
import com.thinkgem.jeesite.modules.firsttable.service.TbTestService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试Controller
 * @author 张胜利
 * @version 2020-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/firsttable/tbTest")
public class TbTestController extends BaseController {

	@Autowired
	private TbTestService tbTestService;
	
	@ModelAttribute
	public TbTest get(@RequestParam(required=false) String id) {
		TbTest entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tbTestService.get(id);
		}
		if (entity == null){
			entity = new TbTest();
		}
		return entity;
	}

	@RequiresPermissions("firsttable:tbTest:view")
	@RequestMapping(value = { "index" })
	public String index() {
		return "modules/firsttable/tbTestIndex";
	}

	@RequiresPermissions("firsttable:tbTest:view")
	@RequestMapping(value = {"list", ""})
	public String list(TbTest tbTest, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TbTest> page = tbTestService.findPage(new Page<>(request, response), tbTest);
		model.addAttribute("page", page);
		return "modules/firsttable/tbTestList";
	}

	@RequiresPermissions("firsttable:tbTest:view")
	@RequestMapping(value = "form")
	public String form(TbTest tbTest, Model model) {
		model.addAttribute("tbTest", tbTest);
		return "modules/access/tbTestForm";
	}

	@RequiresPermissions("firsttable:tbTest:edit")
	@RequestMapping(value = "save")
	public String save(TbTest tbTest, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tbTest)){
			return form(tbTest, model);
		}
		tbTestService.save(tbTest);
		addMessage(redirectAttributes, "保存测试成功");
		return "redirect:"+Global.getAdminPath()+"/firsttable/tbTest/?repage";
	}
	
	@RequiresPermissions("firsttable:tbTest:edit")
	@RequestMapping(value = "delete")
	public String delete(TbTest tbTest, RedirectAttributes redirectAttributes) {
		tbTestService.delete(tbTest);
		addMessage(redirectAttributes, "删除测试成功");
		return "redirect:"+Global.getAdminPath()+"/firsttable/tbTest/?repage";
	}

}