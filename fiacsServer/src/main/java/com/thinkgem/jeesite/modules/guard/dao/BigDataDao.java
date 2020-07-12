package com.thinkgem.jeesite.modules.guard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface BigDataDao extends CrudDao<Object> {
    //===============完成率====================

    /**
     * 统计排班计划所有路线网点数
     * @return
     */
    int countTaskClass(Map<String,String> map);

    /**
     * 已有排班计划的网点数
     * @return
     */
    int countTaskClassOfficeId(Map<String,String> map);

    /**
     * 所有网点个数 包括中心金库
     * @return
     */
    int countOfficeId();

    /**
     * 已完成的交接事件
     * @param map
     * @return
     */
    int finishTaskClass(Map<String,String> map);

    /**
     * 今日已完成的交接事件
     * @return
     */
    List<ConnectEvent> selectToDayConnectEvent();

    /**
     * 当天车辆达到事件
     * @return
     */
    List<CarEvent> selectToDayCarArriveEvent();

    //===============款箱个数统计==================
    int countMoneyBox(Map<String,String> map);

    //===============款箱次数统计=================

    /**
     * 当天款箱预约次数统计
     * @return
     */
    int countMoneyBoxOrder();

    /**
     * 当天款箱上缴次数统计
     * @return
     */
    int countMoneyBoxReturn();

    /**
     * 当天款箱调拨次数统计
     * @return
     */
    int countMoneyBoxAllot();



    //==================识别方式==========================

    List<Map<String,Object>> countEventDetailAuthType();

    List<Map<String,Object>> countCommissionerAuthType();

    //=================告警次数============================

    List<Map<String,Object>> countAlarmEventTimes();
}
