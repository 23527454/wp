package com.thinkgem.jeesite.modules.guard.service;

import com.google.gson.JsonArray;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SynOtherSystemService {
    @Autowired
    private MoneyBoxAllotService moneyBoxAllotService;
    @Autowired
    private MoneyBoxService moneyBoxService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private EquipmentService equipmentService;

    public boolean login(String username,String password,String systemType) throws Exception {
        boolean result = false;
        if("VMS".equals(systemType)){
            result =  HttpUtil.loginVms(username,password);

            if(!result){
                return false;
            }

            //登录成功 则去同步数据
            Map<String,String> map1 = new HashMap<>();
            map1.put("procVO.boxprocprocdat","20191025");
            map1.put("procVO.boxprocprocod","20191111");
            map1.put("procVO.boxprocbranch","319999");
            map1.put("procVO.boxprockind","02");
            map1.put("procVO.boxprocsts","2");
            map1.put("procVO.boxproctyp","4");
            map1.put("page.pageInfo.rowsOfPage","100");
            map1.put("page.pageInfo.currentPageNum","1");
            map1.put("order.sortOrder","desc");
            JSONObject jsonObject = HttpUtil.getHtmlHttpPostJsonWithToken(HttpUtil.VMS_URL+HttpUtil.VMS_BOX_EVENT,map1,systemType+"_"+username);
           /* if(jsonObject.getJSONObject("page").getJSONObject("pageInfo").getInt("totalRowCount")==0){
                return true;
            }*/
            JSONArray jsonArray = jsonObject.getJSONObject("page").getJSONArray("data");
            for(int i=0;i<jsonArray.size();i++){
                JSONObject eventJson = jsonArray.getJSONObject(i);
                Map<String,String> map2 = new HashMap<>();
                map2.put("procVO.boxprocid",eventJson.getString("boxprocid"));
                map2.put("procVO.boxprockind","02");
                map2.put("procVO.boxprocsts","2");
                map2.put("page.pageInfo.rowsOfPage","10000");
                map2.put("page.pageInfo.currentPageNum","1");
                map2.put("order.sortOrder","desc");

                JSONObject jsonObject1 = HttpUtil.getHtmlHttpPostJsonWithToken(HttpUtil.VMS_URL+HttpUtil.VMS_BOX_EVENT_DETAIL,map2,systemType+"_"+username);
               /* if(jsonObject1.getJSONObject("page").getJSONObject("pageInfo").getInt("totalRowCount")==0){
                    continue;
                }*/
                JSONArray jsonArray1 = jsonObject1.getJSONObject("page").getJSONArray("data");

                Office office = officeService.getByOfficeName(eventJson.getString("boxchkid3"));
                if(office==null){
                    continue;
                }
                Equipment equipment = equipmentService.getByOfficeId(office.getId());
                if(equipment==null){
                    continue;
                }
                for(int j=0;j<jsonArray1.size();j++){
                    JSONObject eventDetailJson = jsonArray1.getJSONObject(j);
                    MoneyBoxAllot moneyBoxAllot = new MoneyBoxAllot();
                    MoneyBox moneyBox = moneyBoxService.getByBoxCode(eventDetailJson.getString("boxprocdkey"));
                    if(moneyBox==null){
                        continue;
                    }
                    moneyBoxAllot.setMoneyBoxId(moneyBox.getId());
                    moneyBoxAllot.setCardNum(moneyBox.getCardNum());
                    moneyBoxAllot.setEquipmentId(equipment.getId());
                    String scheDate = eventJson.getString("boxprocdat");
                    moneyBoxAllot.setScheduleTime(DateUtils.parseDate(scheDate,"yyyy-MM-dd"));
                    moneyBoxAllot.setAllotType("0");
                    moneyBoxAllot.setTaskScheduleId("0");
                    moneyBoxAllot.setClassTaskInfo(new ClassTaskInfo(("0")));
                    //避免重复添加
                    if(moneyBoxAllotService.countByEntityCodition(moneyBoxAllot)==0){
                        moneyBoxAllotService.save(moneyBoxAllot);
                    }
                }
            }
        }else{
            result =  HttpUtil.loginSrcs(username, password);

            if(!result){
                return false;
            }

            Map<String,String> map = new HashMap<>();
            map.put("route_id","");
            map.put("pageInfo.currentPageNum","1");
            map.put("pageInfo.rowsOfPage","100");
            map.put("flowId","1");
            map.put("pwd_holder","");
            map.put("route_id_para","");
            map.put("returner_id","");
            map.put("nextFunc","e100007");

            JSONObject lineListJson = HttpUtil.getHtmlHttpPostJsonWithToken(HttpUtil.SCRS_URL+HttpUtil.SCRS_LINE_INFO,map,"SCRS_"+username);
            if(lineListJson.getJSONObject("pageResult").getInt("totalRowCount")==0){
                return true;
            }
            JSONArray lineArray = lineListJson.getJSONArray("data");
            for(int i=0;i<lineArray.size();i++){
                JSONObject lineJson = lineArray.getJSONObject(i);
                Map<String,String> map1 = new HashMap<>();
                map1.put("route_id",lineJson.getString("route_id"));
              //  map1.put("pageInfo.currentPageNum","1");
              //  map1.put("pageInfo.rowsOfPage","100");
                map1.put("flow_id","1");
              //  map1.put("pwd_holder","");
                map1.put("receiver",lineJson.getString("pwd_holder"));

                JSONObject boxInfoJson = HttpUtil.getHtmlHttpPostJsonWithToken(HttpUtil.SCRS_URL+HttpUtil.SCRS_TASK_BOX_INFO,map1,"SCRS_"+username);
                String boxListText = boxInfoJson.getString("task_box_list");
                if(StringUtils.isNotEmpty(boxListText)){
                    String[] boxArray = boxListText.split(";");
                    for(String boxCode : boxArray){
                        MoneyBox moneyBox = moneyBoxService.getByBoxCode(boxCode);
                        MoneyBoxAllot moneyBoxAllot = new MoneyBoxAllot();
                        if(moneyBox==null){
                            continue;
                        }
                        moneyBoxAllot.setMoneyBoxId(moneyBox.getId());
                        moneyBoxAllot.setCardNum(moneyBox.getCardNum());
                        moneyBoxAllot.setEquipmentId("1");
                        moneyBoxAllot.setScheduleTime(DateUtils.parseDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
                        moneyBoxAllot.setAllotType("0");
                        moneyBoxAllot.setTaskScheduleId("0");
                        moneyBoxAllot.setClassTaskInfo(new ClassTaskInfo(("0")));
                        if(moneyBoxAllotService.countByEntityCodition(moneyBoxAllot)==0){
                            moneyBoxAllotService.save(moneyBoxAllot);
                        }
                    }
                }
            }
        }


        return true;
    }
}
