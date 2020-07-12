package com.thinkgem.jeesite.modules.guard.service;

import com.fiacs.common.util.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.BigDataDao;
import com.thinkgem.jeesite.modules.guard.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.TtsSetting;
import com.thinkgem.jeesite.modules.sys.service.TtsSettingService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;

@Service
public class BigDataService {
    @Resource
    private BigDataDao bigDataDao;
    @Autowired
    private ConnectEventService connectEventService;
    @Autowired
    private  AlarmEventService alarmEventService;
    @Autowired
    private LineService lineService;
    @Autowired
    private TtsSettingService ttsService;
    @Autowired
    private CarEventService carEventService;
    @Autowired
    private SafeGuardEventService safeGuardEventService;
    @Autowired
    private DoorInOutEventService doorInOutEventService;
    @Autowired
    private DoorAlarmEventService doorAlarmEventService;
    /**
     * 派送、回收完成率及车辆准时率
     * @return
     */
    public Map<String,Integer> caculateRate(){
        //派送总次数 排班中查询  如果没有排班计划 某人网点每天1次派送和回收(没有排班的 默认完成)
        //派送排班统计
        Map<String,Integer> returnMap = new HashMap<>();

        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("taskType","0");
        queryMap.put("taskTimeClass","0");
        int psTaskTotal = bigDataDao.countTaskClass(queryMap);
        int psTaskOfficeTotal = bigDataDao.countTaskClassOfficeId(queryMap);
        int officeTotal = bigDataDao.countOfficeId();

        //已完成的派送(根据交接事件派送类型统计)
        Map<String,String> queryFinishMap = new HashMap<>();
        queryFinishMap.put("taskType","0");
        //交接事件中已完成的派送网点
        int psFinishTaskTotal = bigDataDao.finishTaskClass(queryFinishMap);
        queryFinishMap.put("count","ALL");
        int psAllFinishTaskToal = bigDataDao.finishTaskClass(queryFinishMap);
        returnMap.put("pstotal",psTaskTotal+(officeTotal-psTaskOfficeTotal)+(psAllFinishTaskToal-psFinishTaskTotal));
        returnMap.put("ps",psAllFinishTaskToal);

        //回收排班统计
        queryMap.put("taskType","1");
        int hsTaskTotal = bigDataDao.countTaskClass(queryMap);
        int hsTaskOfficeTotal = bigDataDao.countTaskClassOfficeId(queryMap);

        queryFinishMap.put("taskType","1");
        int hsAllFinishTaskTotal = bigDataDao.finishTaskClass(queryFinishMap);

        queryFinishMap.remove("count");
        int hsFinishTaskTotal = bigDataDao.finishTaskClass(queryFinishMap);

        returnMap.put("hstotal",hsTaskTotal+(officeTotal-hsTaskOfficeTotal)+(hsAllFinishTaskTotal-hsFinishTaskTotal));
        returnMap.put("hs",hsAllFinishTaskTotal);

        //车辆准时率(已完成车辆达到事件的计划去查询车辆达到时间是否准时)
        List<CarEvent> carConnectEvent = bigDataDao.selectToDayCarArriveEvent();
        Map<String,Object> dataMap = new HashMap<>();
        int normalFinish = 0;
        for(CarEvent event : carConnectEvent){
            //查询排班线路 计算应到时间点
            Line line = lineService.get(event.getLineId());
            if(line==null){
                normalFinish++;
                continue;
            }
            List<LineNodes> lineNodes = line.getLineNodesList();
            LineNodes[] minu = new LineNodes[lineNodes.size()];
            for(LineNodes lineNode : lineNodes){
                minu[Integer.valueOf(lineNode.getNodeSn())-1] = lineNode;
            }
            for(int i=0;i<minu.length;i++){
                LineNodes lineNode = minu[i];
                if(lineNode.getEquipmentId().equals(event.getEquipmentId())){
                    Date arrive = null;
                    Date allot = null;
                    try {
                        allot = DateUtils.parseDate(event.getTaskTime(),"HH:mm:ss");
                        arrive = DateUtils.parseDate(event.getTime().substring(11),"HH:mm:ss");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //有序线路
                    if(line.getLineOrder().equals("1")){
                        for(int j=0;j<=i;j++){
                            allot = DateUtils.addMinutes(allot,Integer.valueOf(minu[j].getNodeNextGap()));
                        }
                        //无序线路
                    }else{
                        allot = DateUtils.addMinutes(allot,Integer.valueOf(minu[i].getNodeNextGap()));
                    }
                    //比较时间 判断是否准时
                    if(allot.getTime()>=arrive.getTime()){
                        normalFinish++;
                    }
                    break;
                }
            }
        }
        returnMap.put("ddtotal",carConnectEvent.size());
        returnMap.put("dd",normalFinish);

        return returnMap;
    }

    /**
     * 款箱统计
     * @return
     */
    public Map<String,Integer> caculateMoneyBox(){
        //派送总数
        Map<String,Integer> returnMap = new HashMap<>();
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("taskType","0");
        int pstotal = bigDataDao.countMoneyBox(queryMap);
        //派送早晚款箱
        queryMap.put("moneyBoxType","0");
        int pszwtotal = bigDataDao.countMoneyBox(queryMap);

        returnMap.put("pstotal",pstotal);
        returnMap.put("pszwtotal",pszwtotal);

        //回收的早晚款箱
        queryMap.put("moneyBoxType","1");
        queryMap.put("taskType","1");
        int hszwtotal = bigDataDao.countMoneyBox(queryMap);
        queryMap.remove("moneyBoxType");
        int hstotal = bigDataDao.countMoneyBox(queryMap);
        returnMap.put("hszwtotal",hszwtotal);
        returnMap.put("hstotal",hstotal);
        return returnMap;
    }

    /**
     * 款箱次数统计
     * @return
     */
    public Map<String,Integer> countMoneyBoxTimes(){
        Map<String,Integer> map = new HashMap<>();
        map.put("yy",bigDataDao.countMoneyBoxOrder());
        map.put("sj",bigDataDao.countMoneyBoxReturn());
        map.put("db",bigDataDao.countMoneyBoxAllot());
        return map;
    }

    public Map<String,Integer> countAuthorType(){
        Map<String,Integer> map = new HashMap<>();
        map.put("zw",0);
        map.put("rl",0);
        map.put("mm",0);
        map.put("zwrl",0);
        map.put("zwmm",0);
        //事件详情识别统计
        List<Map<String,Object>> eventDetailMaps = bigDataDao.countEventDetailAuthType();
        for(Map<String,Object> eventDetailMap : eventDetailMaps){
            int type = Integer.valueOf(String.valueOf(eventDetailMap.get("type")));
            int total = Long.valueOf(String.valueOf(eventDetailMap.get("total"))).intValue();
            if(type==1){
                map.put("zw",total);
            }else  if(type==2){
                map.put("rl",total);
            }else  if(type==3){
                map.put("mm",total);
            }else  if(type==4){
                map.put("zwrl",total);
            }else  if(type==5){
                map.put("zwmm",total);
            }
        }
        List<Map<String,Object>> commissonerMap = bigDataDao.countCommissionerAuthType();
        for(Map<String,Object> eventDetailMap : commissonerMap){
            int type = Integer.valueOf(String.valueOf(eventDetailMap.get("type")));
            int total = Long.valueOf(String.valueOf(eventDetailMap.get("total"))).intValue();
            if(type==1){
                map.put("zw",total);
            }else  if(type==2){
                map.put("rl",total);
            }else  if(type==3){
                map.put("mm",total);
            }else  if(type==4){
                map.put("zwrl",total);
            }else  if(type==5){
                map.put("zwmm",total);
            }
        }
        return map;
    }

    public Map<String,Integer> countAlarmEvent(){
        Map<String,Integer> map = new HashMap<>();
        map.put("zero",0);
        map.put("one",0);
        map.put("two",0);
        map.put("three",0);
        map.put("four",0);
        map.put("five",0);
       /* map.put("six",0);
        map.put("seven",0);*/
        //事件详情识别统计
        List<Map<String,Object>> eventDetailMaps = bigDataDao.countAlarmEventTimes();
        for(Map<String,Object> eventDetailMap : eventDetailMaps){
            int type = Integer.valueOf(String.valueOf(eventDetailMap.get("type")));
            int total = Long.valueOf(String.valueOf(eventDetailMap.get("total"))).intValue();
            if(type==1){
                map.put("one",total);
            }else  if(type==2){
                map.put("two",total);
            }else  if(type==3){
                map.put("three",total);
            }else  if(type==4){
                map.put("four",total);
            }else  if(type==5){
                map.put("five",total);
            }/*else  if(eventDetailMap.get("type")==6){
                map.put("six",total);
            }else  if(eventDetailMap.get("type")==7){
                map.put("seven",total);
            }*/else  if(type==0){
                map.put("zero",total);
            }
        }
        return map;
    }

    public List<ConnectEvent> findConnectEventList(int size){
        Page<ConnectEvent> p = new Page<>();
        p.setOrderBy("a.id desc");
        p.setPageSize(size);
        return connectEventService.findPage(p,new ConnectEvent()).getList();
    }

    public Map<String,String> queryEventDetail(String eventId){
        Map<String,String> map = new HashMap<>();
        ConnectEvent event =  connectEventService.get(eventId);
        if(event==null){
            return map;
        }
        map.put("officeName",event.getOfficeName());
        map.put("areaName",event.getAreaName());
        map.put("taskName",event.getTaskName());
        map.put("taskId",event.getTaskId());
        map.put("taskType",event.getTaskType());
        map.put("time",event.getTime());
        map.put("carId",event.getCarId());
        map.put("carPlate",event.getCarplate());
        String moneyBoxCodes = "";
        for(MoneyBoxEventDetail mdetal : event.getMoneyBoxEventDetailList()){
            moneyBoxCodes= moneyBoxCodes + mdetal.getBoxCode()+",";
        }
        map.put("moneyBoxCodes",moneyBoxCodes);

        for(int i=0;i<event.getCommissionerEventList().size();i++){
            CommissionerEvent cevent = event.getCommissionerEventList().get(i);
            if(i==0){
                map.put("commissionerOneName",cevent.getStaff().getName()+"("+ DictUtils.getDictLabel(cevent.getAuthorType(),"door_open_type","")+")");
                map.put("commissionerOneImageId",cevent.getStaff().getStaffImageList().get(0).getId());
            }else if(i==1){
                map.put("commissionerTwoName",cevent.getStaff().getName()+"("+ DictUtils.getDictLabel(cevent.getAuthorType(),"door_open_type","")+")");
                map.put("commissionerTwoImageId",cevent.getStaff().getStaffImageList().get(0).getId());
            }
        }

        for(int i=0;i<event.getEventDetailList().size();i++){
            EventDetail cevent = event.getEventDetailList().get(i);
            if(i==0){
                map.put("safeGuardOneName",cevent.getStaff().getName()+"("+ DictUtils.getDictLabel(cevent.getAuthorType(),"door_open_type","")+")");
                map.put("safeGuardOneImageId",cevent.getStaff().getStaffImageList().get(0).getId());
                map.put("safeGuardOneZImageId",cevent.getId());//抓拍
            }else if(i==1){
                map.put("safeGuardTwoName",cevent.getStaff().getName()+"("+ DictUtils.getDictLabel(cevent.getAuthorType(),"door_open_type","")+")");
                map.put("safeGuardTwoImageId",cevent.getStaff().getStaffImageList().get(0).getId());
                map.put("safeGuardTwoZImageId",cevent.getId());//抓拍
            }
        }

        return map;
    }

    public List<AlarmEvent> findAlarmEventList(){
       /* Page<AlarmEvent> p = new Page<>();
        p.setOrderBy("a.id desc");
        p.setPageSize(10);
        AlarmEvent queryAlarm = new AlarmEvent();
        queryAlarm.setHandleUserNameId("0");//查询未处理的报警事件*/
        return alarmEventService.findList(new AlarmEvent());
    }

    public Map<String,Object> actualRefresh(ActualEntity actualEntity){
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("type",actualEntity.getType());
        if(actualEntity.getType().equals(Constants.CONNECT_EVENT_TYPE)){
            TtsSetting ttsSetting = ttsService.getByType("connect").get(0);
            ConnectEvent queryEvent = new ConnectEvent();
            queryEvent.setRecordId(actualEntity.getEventId());
            queryEvent.setEquipmentId(actualEntity.getEquipId());
            queryEvent.setTime(actualEntity.getTime());
            ConnectEvent resultConnectEvent = connectEventService.findOne(queryEvent);
            //获取押款员和专员姓名
            resultConnectEvent = connectEventService.get(resultConnectEvent.getId());
            String commissionerNames = "";
            for(CommissionerEvent commissionerEvent : resultConnectEvent.getCommissionerEventList()){
                commissionerNames += commissionerEvent.getStaff().getName()+",";
            }
            resultConnectEvent.setCommissioner(commissionerNames);

            String eventDetailNames = "";
            for(EventDetail eventDetail : resultConnectEvent.getEventDetailList()){
                eventDetailNames += eventDetail.getStaff().getName()+",";
            }
            resultConnectEvent.setSecurityStaff(eventDetailNames);
            returnMap.put("tts",replaceFieldVal(resultConnectEvent,ttsSetting.getVoiceConfig()));
            returnMap.put("data",resultConnectEvent);
        }else if(actualEntity.getType().equals(Constants.ALARM_EVENT_TYPE)){
            TtsSetting ttsSetting = ttsService.getByType("alarm").get(0);
            AlarmEvent queryEvent = new AlarmEvent();
            queryEvent.setRecordId(actualEntity.getEventId());
            queryEvent.setEquipmentId(actualEntity.getEquipId());
            queryEvent.setTime(actualEntity.getTime());
            AlarmEvent resultAlarmEvent = alarmEventService.findOne(queryEvent);
            returnMap.put("tts",replaceFieldVal(resultAlarmEvent,ttsSetting.getVoiceConfig()));
            returnMap.put("data",resultAlarmEvent);
        }else if(actualEntity.getType().equals(Constants.CAR_ARRIVE_EVENT_TYPE)){
            TtsSetting ttsSetting = ttsService.getByType("car").get(0);
            CarEvent queryEvent = new CarEvent();
            queryEvent.setRecordId(actualEntity.getEventId());
            queryEvent.setEquipmentId(actualEntity.getEquipId());
            queryEvent.setCarId(actualEntity.getCarId());
            queryEvent.setTime(actualEntity.getTime());
            CarEvent resultCarEvent = carEventService.findOne(queryEvent);
            returnMap.put("tts",replaceFieldVal(resultCarEvent,ttsSetting.getVoiceConfig()));
        }else if(actualEntity.getType().equals(Constants.SAFEGUARD_EVENT_TYPE)){
            TtsSetting ttsSetting = ttsService.getByType("safeGuard").get(0);
            SafeGuardEvent queryEvent = new SafeGuardEvent();
            queryEvent.setRecordId(actualEntity.getEventId());
            queryEvent.setEquipmentId(actualEntity.getEquipId());
            queryEvent.setPersonId(actualEntity.getPersonId());
            queryEvent.setTime(actualEntity.getTime());
            SafeGuardEvent resultCarEvent = safeGuardEventService.findOne(queryEvent);
            returnMap.put("tts",replaceFieldVal(resultCarEvent,ttsSetting.getVoiceConfig()));
        }else if(actualEntity.getType().equals(Constants.ACCESS_EVENT_TYPE)){
            TtsSetting ttsSetting = ttsService.getByType("doorControl").get(0);
            DoorInOutEvent queryEvent = new DoorInOutEvent();
            queryEvent.setRecordId(actualEntity.getEventId());
            queryEvent.setEquipmentId(actualEntity.getEquipId());
            queryEvent.setPersonId(Integer.valueOf(actualEntity.getPersonId()));
            queryEvent.setTime(actualEntity.getTime());
            DoorInOutEvent resultCarEvent = doorInOutEventService.findOne(queryEvent);
            returnMap.put("tts",replaceFieldVal(resultCarEvent,ttsSetting.getVoiceConfig()));
        }else if(actualEntity.getType().equals(Constants.ACCESS_ALARM_TYPE)){
            TtsSetting ttsSetting = ttsService.getByType("doorAlarm").get(0);
            DoorAlarmEvent queryEvent = new DoorAlarmEvent();
            queryEvent.setRecordId(actualEntity.getEventId());
            queryEvent.setEquipmentId(actualEntity.getEquipId());
            queryEvent.setPersonId(actualEntity.getPersonId());
            queryEvent.setTime(actualEntity.getTime());
            DoorAlarmEvent resultCarEvent = doorAlarmEventService.findOne(queryEvent);
            returnMap.put("tts",replaceFieldVal(resultCarEvent,ttsSetting.getVoiceConfig()));
        }
        return returnMap;
    }

    public void dealAlarmEvent(AlarmEvent alarmEvent){
        alarmEvent.setHandleTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        alarmEvent.setHandleUserNameId(alarmEvent.getCurrentUser().getId());

        AlarmEvent oldAlarmEvemt = alarmEventService.get(alarmEvent.getId());
        if(StringUtils.isEmpty(oldAlarmEvemt.getHandleTime())){
            alarmEventService.save(alarmEvent);
        }
    }

    private String replaceFieldVal(Object t,String temlateStr){
        if (null == t || null == temlateStr || temlateStr == ""){
            return "";
        }

        String fieldName,fieldVal;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields){
            field.setAccessible( true );
            try {
                fieldName = field.getName();
                fieldVal = null == field.get(t) ? "" : field.get(t) + "";
                if (temlateStr.contains(fieldName)){
                    String nameStr = "{" + fieldName + "}";
                    temlateStr = temlateStr.replace(nameStr, fieldVal);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return temlateStr;
    }
}
