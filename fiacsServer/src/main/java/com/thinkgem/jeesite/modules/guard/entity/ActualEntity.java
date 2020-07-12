package com.thinkgem.jeesite.modules.guard.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.HashMap;
import java.util.Map;

public class ActualEntity extends DataEntity<ActualEntity> {

    private String type;

    private String eventId;

    private String equipId;

    private String time;

    private String carId;

    private String personId;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
