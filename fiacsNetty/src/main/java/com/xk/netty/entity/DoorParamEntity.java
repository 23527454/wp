package com.xk.netty.entity;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DoorParamEntity  implements  Serializable{

    private String downLoadId;

    private String equipId;

    private String accessParmetersSysStatus;

    private String doorPos;

    private String relayActionTime;

    private String delayCloseTime;

    private String alarmTime;

    private String timeZoneNum;

    private String conmbinationNum;

    private String centerPermit;

    private String openType;

    public String getDownLoadId() {
        return downLoadId;
    }

    public void setDownLoadId(String downLoadId) {
        this.downLoadId = downLoadId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getAccessParmetersSysStatus() {
        return accessParmetersSysStatus;
    }

    public void setAccessParmetersSysStatus(String accessParmetersSysStatus) {
        this.accessParmetersSysStatus = accessParmetersSysStatus;
    }

    public String getDoorPos() {
        return doorPos;
    }

    public void setDoorPos(String doorPos) {
        this.doorPos = doorPos;
    }

    public String getRelayActionTime() {
        return relayActionTime;
    }

    public void setRelayActionTime(String relayActionTime) {
        this.relayActionTime = relayActionTime;
    }

    public String getDelayCloseTime() {
        return delayCloseTime;
    }

    public void setDelayCloseTime(String delayCloseTime) {
        this.delayCloseTime = delayCloseTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getTimeZoneNum() {
        return timeZoneNum;
    }

    public void setTimeZoneNum(String timeZoneNum) {
        this.timeZoneNum = timeZoneNum;
    }

    public String getConmbinationNum() {
        return conmbinationNum;
    }

    public void setConmbinationNum(String conmbinationNum) {
        this.conmbinationNum = conmbinationNum;
    }

    public String getCenterPermit() {
        return centerPermit;
    }

    public void setCenterPermit(String centerPermit) {
        this.centerPermit = centerPermit;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }


    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("AccessParametersSysStatus",this.accessParmetersSysStatus);
        map.put("nDoor_pos",this.doorPos);
        map.put("nRelayactiontime",this.relayActionTime);
        map.put("nDelayclosetime",this.delayCloseTime);
        map.put("nAlarmtime",this.alarmTime);
        map.put("nCombinationnumber",this.conmbinationNum);
        map.put("nCenterpermit",this.centerPermit);
        map.put("nTimeZonenumber",this.timeZoneNum);
        map.put("OpenType",this.openType);

        return map;
    }
}
