package com.thinkgem.jeesite.modules.guard.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.InformationRelease;
import com.thinkgem.jeesite.modules.guard.entity.SafeGuardDispatch;
import com.thinkgem.jeesite.modules.guard.service.InformationReleaseService;
import com.thinkgem.jeesite.modules.guard.service.SafeGuardDispatchService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 维保员派遣controller
 */
@Controller
@RequestMapping(value="${adminPath}/guard/safeGuardDispatch")
public class SafeGuardDispatchController extends BaseController {

    @Autowired
    private SafeGuardDispatchService safeGuardDispatchService;

    @Autowired
    private OfficeService officeService;
    @ModelAttribute
    public SafeGuardDispatch get(@RequestParam(required = false) String id) {
        SafeGuardDispatch entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = safeGuardDispatchService.get(id);
        }
        if (entity == null) {
            entity = new SafeGuardDispatch();
        }
        return entity;
    }


    @RequiresPermissions("guard:staff:view")
    @RequestMapping(value= {"/list",""})
    public String list(SafeGuardDispatch safeGuardDispatch, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<SafeGuardDispatch> page = safeGuardDispatchService.findPage(new Page<SafeGuardDispatch>(request, response),
                safeGuardDispatch);
        model.addAttribute("page", page);
        model.addAttribute("safeGuardDispatch",safeGuardDispatch);
        return "modules/guard/safeGuardDispatchList";
    }

    @RequiresPermissions("guard:staff:view")
    @RequestMapping(value = "form")
    public String form(SafeGuardDispatch info, Model model) {
        return backForm(info, model);
    }

    private String backForm(SafeGuardDispatch safeGuardDispatch, Model model) {
        model.addAttribute("safeGuardDispatch", safeGuardDispatch);
        model.addAttribute("officeList", officeService.findAll(new Office()));
        return "modules/guard/safeGuardDispatchForm";
    }

    @RequestMapping(value = "save")
    public String save(SafeGuardDispatch safeGuardDispatch, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        safeGuardDispatchService.save(safeGuardDispatch);
        addMessage(redirectAttributes, "派遣任务成功");
        return "redirect:" + Global.getAdminPath() + "/guard/safeGuardDispatch/?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(SafeGuardDispatch safeGuardDispatch, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        safeGuardDispatchService.delete(safeGuardDispatch);
        addMessage(redirectAttributes, "删除派遣任务成功");
        return "redirect:" + Global.getAdminPath() + "/guard/safeGuardDispatch/?repage";
    }
}
