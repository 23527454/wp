package com.thinkgem.jeesite.modules.guard.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.service.SynOtherSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/guard/synOtherSystem")
public class SynOtherSystemController extends BaseController {

    @Autowired
    private SynOtherSystemService synOtherSystemService;

    @ResponseBody
    @RequestMapping("/loginOtherSystem")
    public String loginOtherSystem(String userName,String password,String systemType){
        try {
            return synOtherSystemService.login(userName,password,systemType) ? "{}" :"验证码失败";
        } catch (Exception e) {
            return "{error}";
        }
    }
}
