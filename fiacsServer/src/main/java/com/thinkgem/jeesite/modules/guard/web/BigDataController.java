package com.thinkgem.jeesite.modules.guard.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.entity.ActualEntity;
import com.thinkgem.jeesite.modules.guard.entity.AlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.service.BigDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value= "${adminPath}/guard/bigData")
public class BigDataController extends BaseController {

    @Resource
    private BigDataService bigDataService;

    @RequestMapping("/index")
    public String index(){
        return "modules/guard/realTimeMonitoring";
    }
    /**
     * 派送、回收完成率及车辆准时率
     * @return
     */
    @ResponseBody
    @RequestMapping("/calculateRate")
    public String calculateRate(){
        return JSON.toJSONString(bigDataService.caculateRate());
    }

    /**
     * 款箱个数统计
     * @return
     */
    @ResponseBody
    @RequestMapping("/caculateMoneyBox")
    public String caculateMoneyBox(){
        return JSON.toJSONString(bigDataService.caculateMoneyBox());
    }

    /**
     * 款箱预约 调拨 上缴次数
     * @return
     */
    @ResponseBody
    @RequestMapping("/countMoneyBoxTimes")
    public String countMoneyBoxTimes(){
        return JSON.toJSONString(bigDataService.countMoneyBoxTimes());
    }

    /**
     * 识别方式
     * @return
     */
    @ResponseBody
    @RequestMapping("/countAuthorType")
    public String countAuthorType(){
        return JSON.toJSONString(bigDataService.countAuthorType());
    }

    /**
     * 告警次数
     *
     */
    @ResponseBody
    @RequestMapping("/countAlarmEventType")
    public String countAlarmEventType(){
        return JSON.toJSONString(bigDataService.countAlarmEvent());
    }

    /**
     * 交接事件
     * @param size 条数
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryConnectEventList")
    public List<ConnectEvent> queryConnectEventList(@RequestParam("size") String size){
        return bigDataService.findConnectEventList(Integer.valueOf(size));
    }

    /**
     * 交接事件详情
     * @param eventId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryEventDetail")
    public String queryEventDetail(@RequestParam("eventId")String eventId){
        return JSON.toJSONString(bigDataService.queryEventDetail(eventId));
    }

    /**
     * 报警事件列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryAlarmEventList")
    public List<AlarmEvent> queryAlarmEventList(){
        return bigDataService.findAlarmEventList();
    }

    @ResponseBody
    @RequestMapping("/actualTimeRefresh")
    public String actualTimeRefresh(ActualEntity actualEntity){
        System.out.println(actualEntity.getType());
        return JSON.toJSONString(bigDataService.actualRefresh(actualEntity));
    }

    @ResponseBody
    @RequestMapping("/dealAlarmEvent")
    public String dealAlarmEvent(AlarmEvent alarmEvent){
        try{
            bigDataService.dealAlarmEvent(alarmEvent);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

    }
}
