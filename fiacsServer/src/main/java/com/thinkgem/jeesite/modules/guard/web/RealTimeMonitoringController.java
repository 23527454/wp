package com.thinkgem.jeesite.modules.guard.web;

import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${adminPath}/guard/monitoring")
public class RealTimeMonitoringController extends BaseController {

    @RequestMapping("/index")
    public String index(){
        return "modules/guard/realTimeMonitoring";
    }
}
