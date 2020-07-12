/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.ConnectEventDao;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.FingerInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffImageDao;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.FingerInfo;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.entity.StaffExFamily;
import com.thinkgem.jeesite.modules.guard.entity.StaffExWork;
import com.thinkgem.jeesite.modules.guard.entity.StaffImage;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 交接事件Service
 *
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class ConnectEventService extends CrudService<ConnectEventDao, ConnectEvent> {

    @Autowired
    private CommissionerEventDao commissionerEventDao;
    @Autowired
    private EventDetailDao eventDetailDao;
    @Autowired
    private MoneyBoxEventDao moneyBoxEventDao;
    @Autowired
    private MoneyBoxEventDetailDao moneyBoxEventDetailDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private FingerInfoDao fingerInfoDao;
    @Autowired
    private StaffImageDao staffImageDao;

    @Autowired
    private MoneyBoxDao moneyBoxDao;

    public ConnectEvent get(String id) {
        ConnectEvent connectEvent = super.get(id);
        if (connectEvent == null) {
            return null;
        }
        //专员
        CommissionerEvent commissionerEventRequest = new CommissionerEvent(connectEvent);
        commissionerEventRequest.setEventType(CommissionerEvent.EVENT_TYPE_CONNECT_EVENT);
        connectEvent.setCommissionerEventList(commissionerEventDao.findList(commissionerEventRequest));
        for (CommissionerEvent commissionerEvent : connectEvent.getCommissionerEventList()) {
            Staff staff = staffDao.getWithDel(commissionerEvent.getPersonId());
            if (staff != null) {
                staff.setStaffImageList(staffImageDao.findList(new StaffImage(staff)));
            }
            commissionerEvent.setStaff(staff);
        }
        //押款员
        EventDetail eventDetailRequest = new EventDetail(connectEvent);
        eventDetailRequest.setEventType(EventDetail.EVENT_TYPE_CONNECT_EVENT);
        connectEvent.setEventDetailList(eventDetailDao.findList(eventDetailRequest));
        for (EventDetail eventDetail : connectEvent.getEventDetailList()) {
            Staff staff = staffDao.getWithDel(eventDetail.getPersonId());
            if (staff != null) {
                staff.setStaffImageList(staffImageDao.findList(new StaffImage(staff)));
            }
            eventDetail.setStaff(staff);
        }

        connectEvent.setMoneyBoxEvent(moneyBoxEventDao.get(connectEvent.getMoneyBoxEventId()));
        MoneyBoxEventDetail moneyBoxEventDetailRequest = new MoneyBoxEventDetail(connectEvent);//款箱
        moneyBoxEventDetailRequest.setEventType(MoneyBoxEventDetail.EVENT_TYPE_CONNECT_EVENT);
        connectEvent.setMoneyBoxEventDetailList(moneyBoxEventDetailDao.findList(moneyBoxEventDetailRequest));

        for (MoneyBoxEventDetail moneyBoxEventDetail : connectEvent.getMoneyBoxEventDetailList()) {
            moneyBoxEventDetail.setMoneyBox(moneyBoxDao.findOne(new MoneyBox(moneyBoxEventDetail)));
        }
        return connectEvent;
    }

    public List<ConnectEvent> findList(ConnectEvent connectEvent) {
        for (Role r : connectEvent.getCurrentUser().getRoleList()) {
            if (r.getDataScope().equals("2") || r.getDataScope().equals("3")) {
                connectEvent.getSqlMap().put("dsf", dataScopeFilter(connectEvent.getCurrentUser(), "h", ""));
            } else {
                connectEvent.getSqlMap().put("dsf", dataScopeFilter(connectEvent.getCurrentUser(), "b", ""));
            }
        }
        return super.findList(connectEvent);
    }

    public Page<ConnectEvent> findPage(Page<ConnectEvent> page, ConnectEvent connectEvent) {
        for (Role r : connectEvent.getCurrentUser().getRoleList()) {
            if (r.getDataScope().equals("3")) {
                connectEvent.getSqlMap().put("dsf", dataScopeFilter(connectEvent.getCurrentUser(), "b", ""));
                /*connectEvent.getSqlMap().put("dsf", dataScopeFilter(connectEvent.getCurrentUser(), "h", ""));*/
            } else if (r.getDataScope().equals("2")) {
                connectEvent.getSqlMap().put("dsf", dataScopeFilter(connectEvent.getCurrentUser(), "h", ""));
            } else {
                connectEvent.getSqlMap().put("dsf", dataScopeFilter(connectEvent.getCurrentUser(), "b", ""));
            }
        }

        Page<ConnectEvent> pages = super.findPage(page,connectEvent);
        if(pages.getCount()==0){
        	return pages;
		}
        List<ConnectEvent> connectEventList  = pages.getList();
        for(ConnectEvent event : connectEventList){
        	ConnectEvent nEvent = this.get(event.getId());
        	String commissionerNames = "";
        	for(CommissionerEvent commissionerEvent : nEvent.getCommissionerEventList()){
				commissionerNames += commissionerEvent.getStaff().getName()+",";
			}
        	event.setCommissionerNames(commissionerNames);

        	String eventDetailNames = "";
        	for(EventDetail eventDetail : nEvent.getEventDetailList()){
        		eventDetailNames += eventDetail.getStaff().getName()+",";
			}
        	event.setEventDetailNames(eventDetailNames);

        	String moneyBoxNums = "";
        	for(MoneyBoxEventDetail moneyBoxEventDetail : nEvent.getMoneyBoxEventDetailList()){
        		moneyBoxNums += moneyBoxEventDetail.getBoxCode()+",";
			}
        	event.setMoneyboxNums(moneyBoxNums);
		}
        return super.findPage(page, connectEvent);


    }

    public List<ConnectEvent> getFeeds(String latestId, String latestTime, String nodes) {
        ConnectEvent connectEvent = new ConnectEvent();
        connectEvent.setId(latestId);
        connectEvent.setTime(latestTime);
        connectEvent.setNodes(nodes);
        return super.dao.getFeeds(connectEvent);
    }

    @Transactional(readOnly = false)
    public void save(ConnectEvent connectEvent) {
        AssertUtil.assertNotNull(connectEvent.getTaskType(), "TaskType");
        super.save(connectEvent);

        for (EventDetail eventDetail : connectEvent.getEventDetailList()) {
            if (eventDetail.getId() == null) {
                continue;
            }
            if (EventDetail.DEL_FLAG_NORMAL.equals(eventDetail.getDelFlag())) {
                if (StringUtils.isBlank(eventDetail.getId())) {
                    eventDetail.setRecordId(connectEvent.getId());
                    eventDetail.setEventType(connectEvent.getTaskType());
                    eventDetail.preInsert();
                    eventDetailDao.insert(eventDetail);
                } else {
                    eventDetail.preUpdate();
                    eventDetailDao.update(eventDetail);
                }
            } else {
                eventDetailDao.delete(eventDetail);
            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(ConnectEvent connectEvent) {
        super.delete(connectEvent);
        eventDetailDao.delete(new EventDetail(connectEvent));
    }
}