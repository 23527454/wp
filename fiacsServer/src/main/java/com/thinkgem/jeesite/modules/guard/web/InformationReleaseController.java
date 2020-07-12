package com.thinkgem.jeesite.modules.guard.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.InformationRelease;
import com.thinkgem.jeesite.modules.guard.service.InformationReleaseService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 信息发布controller
 */
@Controller
@RequestMapping(value="${adminPath}/guard/informationRelease")
public class InformationReleaseController  extends BaseController {

    @Autowired
    private InformationReleaseService informationReleaseService;

    @Autowired
    private OfficeService officeService;
    @ModelAttribute
    public InformationRelease get(@RequestParam(required = false) String id) {
        InformationRelease entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = informationReleaseService.get(id);
        }
        if (entity == null) {
            entity = new InformationRelease();
        }
        return entity;
    }


    @RequiresPermissions("guard:informationRelease:view")
    @RequestMapping(value= {"/list",""})
    public String list(InformationRelease info, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<InformationRelease> page = informationReleaseService.findPage(new Page<InformationRelease>(request, response),
                info);
        model.addAttribute("page", page);
        model.addAttribute("info",info);
        return "modules/guard/informationReleaseList";
    }

    @RequiresPermissions("guard:informationRelease:view")
    @RequestMapping(value = "form")
    public String form(InformationRelease info, Model model) {
        return backForm(info, model);
    }

    private String backForm(InformationRelease info, Model model) {
        model.addAttribute("info", info);
        model.addAttribute("officeList", officeService.findAll(new Office()));
        return "modules/guard/informationReleaseForm";
    }

    @RequiresPermissions("guard:informationRelease:view")
    @RequestMapping(value = "save")
    public String save(InformationRelease info, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        informationReleaseService.save(info);
        addMessage(redirectAttributes, "发布信息成功");
        return "redirect:" + Global.getAdminPath() + "/guard/informationRelease/?repage";
    }

    @RequiresPermissions("guard:informationRelease:view")
    @RequestMapping(value = "delete")
    public String delete(InformationRelease info, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        informationReleaseService.delete(info);
        addMessage(redirectAttributes, "删除发布信息成功");
        return "redirect:" + Global.getAdminPath() + "/guard/informationRelease/?repage";
    }
}
