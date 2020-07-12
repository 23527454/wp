/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.CarEventDao;
import com.thinkgem.jeesite.modules.guard.dao.ClassCarInfoDao;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.ConnectEventDao;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.RecordSeqDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffImageDao;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.ClassCarInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassPersonInfo;
import com.thinkgem.jeesite.modules.guard.entity.ClassTaskInfo;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBox;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEventDetail;
import com.thinkgem.jeesite.modules.guard.entity.RecordSeq;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.entity.StaffImage;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;

/**
 * 交接事件Service
 * @author Jumbo
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class CofferConnectService extends CrudService<ConnectEventDao, ConnectEvent> {

	@Autowired
	private CommissionerEventDao commissionerEventDao;
	@Autowired
	private EventDetailDao eventDetailDao;
	@Autowired
	private MoneyBoxEventDao moneyBoxEventDao;
	@Autowired
	private MoneyBoxEventDetailDao moneyBoxEventDetailDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private CarEventDao carEventDao;
	@Autowired
	private RecordSeqDao recordSeqDao;
	@Autowired
	private MoneyBoxDao moneyBoxDao;
	
	@Autowired
	private StaffImageDao staffImageDao;
	
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ClassTaskInfoService classTaskInfoService;
	@Autowired
	private LineService lineService;
	@Autowired
	private  ClassCarInfoDao classCarInfoDao;
	@Autowired
	private ClassPersonInfoService classPersonInfoService;
	
	public List<MoneyBoxEventDetail> getCofferMoneyBoxDetailList(String equipmentId, String lineId,
			String date, 
			String beforeEqDate,
			String afterEqDate, 
			String eventType) {
		Equipment equipment = equipmentService.get(equipmentId);
		Equipment equipmentReq = new Equipment();
		equipmentReq.setArea(equipment.getArea());
		List<Equipment> equipments = equipmentService.findList(equipment);
		MoneyBoxEventDetail moneyBoxEventDetailRequest = new MoneyBoxEventDetail();
		moneyBoxEventDetailRequest.setEquipmentId(equipmentId);
		moneyBoxEventDetailRequest.setEventType(eventType);
		moneyBoxEventDetailRequest.setDate(date);
		moneyBoxEventDetailRequest.setBeforeEqDate(beforeEqDate);
		moneyBoxEventDetailRequest.setAfterEqDate(afterEqDate);
		moneyBoxEventDetailRequest.setConnectFlag(1);
		List<MoneyBoxEventDetail>  list = moneyBoxEventDetailDao.findList(moneyBoxEventDetailRequest);
		for (Iterator<MoneyBoxEventDetail> iterator = list.iterator(); iterator.hasNext();) {
			MoneyBoxEventDetail moneyBoxEventDetail = (MoneyBoxEventDetail) iterator
					.next();
			MoneyBox moneyBoxRequest = new MoneyBox();
			moneyBoxRequest.setCardNum(moneyBoxEventDetail.getCardNum());
			
			//20180113中心金库款箱查询
			moneyBoxRequest.setId(moneyBoxEventDetail.getMoneyBoxId());
			MoneyBox moneyBox = moneyBoxDao.findOne(moneyBoxRequest);
			if(moneyBox == null){
				iterator.remove();
				continue;
			}
			boolean exists = false; 
			for (Equipment e: equipments) {
				if(e.getId().equals(moneyBoxEventDetail.getEquipmentId())){
					exists = true;
					break;
				}
			}
			if(!exists){
				iterator.remove();
				continue;
			}
			//根据所选线路过滤数据
			if(StringUtils.isNotBlank(lineId)) {
				//查询所选线路的节点  过滤数据
				Line line = lineService.get(lineId);
				for(LineNodes node : line.getLineNodesList()) {
					if(moneyBox.getOffice().getId()!=node.getOffice().getId()&&moneyBox.getOffice().getId()!=equipment.getOffice().getId()) {
						iterator.remove();
						continue;
					}
				}
			}
			
			moneyBox.setOffice(officeService.get(moneyBox.getOffice()));
			moneyBoxEventDetail.setMoneyBox(moneyBox);
		}
		return list;
	}
	
	public List<EventDetail> getCofferEventDetailList(String equipmentId,String lineId, String date, 
			String beforeEqDate,
			String afterEqDate, 
			String eventType) {
		EventDetail eventDetail = new EventDetail();
		eventDetail.setEquipmentId(equipmentId);
		eventDetail.setEventType(eventType);
		eventDetail.setDate(date);
		eventDetail.setBeforeEqDate(beforeEqDate);
		eventDetail.setAfterEqDate(afterEqDate);
		List<EventDetail> comList = eventDetailDao.findList(eventDetail);
		
		//根据线路查询是否有班组 根据班组过滤车辆信息
		if(StringUtils.isNotBlank(lineId)) {
			ClassTaskInfo info = new ClassTaskInfo();
			info.setLine(new Line(lineId));
			//查询班组
			List<ClassTaskInfo> classTaskInfoList = classTaskInfoService.findList(info);
			List<EventDetail> returnList = new ArrayList<EventDetail>();
			for(ClassTaskInfo task : classTaskInfoList) {
				//查询班组对应的车辆
				List<ClassPersonInfo> personList = classPersonInfoService.findList(new ClassPersonInfo(task));
				for(ClassPersonInfo personInfo : personList) {
					for(EventDetail event : comList) {
						if(personInfo.getPersonId().equals(event.getPersonId())) {
							returnList.add(event);
						}
					}
				}
			}
			if(returnList.size()>0) {
				comList.clear();
				comList.addAll(returnList);
			}
		}
		
		for(int i=0;comList!=null&&i<comList.size();i++){
			EventDetail c = comList.get(i);
			Staff staff = staffService.getWithDel(c.getPersonId());
			staff.setStaffImageList(staffImageDao.findList(new StaffImage(staff)));
			c.setStaff(staff);
		}
		return comList;
	}
	
	public List<CommissionerEvent> getCofferCommissionerList(String equipmentId, String lineId,String date,
			String beforeEqDate,
			String afterEqDate, 
			String eventType) {
		CommissionerEvent commissionerEvent = new CommissionerEvent();
		commissionerEvent.setEquipmentId(equipmentId);
		commissionerEvent.setDate(date);
		commissionerEvent.setEventType(eventType);
		commissionerEvent.setBeforeEqDate(beforeEqDate);
		commissionerEvent.setAfterEqDate(afterEqDate);
		List<CommissionerEvent> comList = commissionerEventDao.findList(commissionerEvent);
		//根据所选线路过滤数据
		/*if(StringUtils.isNotBlank(lineId)) {
			List<CommissionerEvent> comListTwo = new ArrayList<CommissionerEvent>();
			//查询所选线路的节点  过滤数据
			Line line = lineService.get(lineId);
			for(LineNodes node : line.getLineNodesList()) {
				for(CommissionerEvent comm : comList) {
					Staff sta = staffService.get(comm.getPersonId());
					if(node.getOffice().getId().equals(sta.getOffice().getId())) {
						comListTwo.add(comm);
					}
				}
			}
			comList.clear();
			comList.addAll(comListTwo);
		}*/
		for(int i=0;comList!=null&&i<comList.size();i++){
			CommissionerEvent c = comList.get(i);
			Staff staff = staffService.getWithDel(c.getPersonId());
			staff.setStaffImageList(staffImageDao.findList(new StaffImage(staff)));
			c.setStaff(staff);
		}
		return comList;
	}
	
	public List<CarEvent> getCarEventList(String equipmentId,String lineId,
			String beforeEqDate,
			String afterEqDate, 
			String date) {
		CarEvent carEvent = new CarEvent();
		carEvent.setEquipmentId(equipmentId);
		carEvent.setDate(date);
		carEvent.setBeforeEqDate(beforeEqDate);
		carEvent.setAfterEqDate(afterEqDate);
		
		List<CarEvent> queryList = carEventDao.findList(carEvent);
		//根据线路查询是否有班组 根据班组过滤车辆信息
		if(StringUtils.isNotBlank(lineId)) {
			ClassTaskInfo info = new ClassTaskInfo();
			info.setLine(new Line(lineId));
			//查询班组
			List<ClassTaskInfo> classTaskInfoList = classTaskInfoService.findList(info);
			List<CarEvent> returnList = new ArrayList<CarEvent>();
			for(ClassTaskInfo task : classTaskInfoList) {
				//查询班组对应的车辆
				List<ClassCarInfo> carList = classCarInfoDao.findList(new ClassCarInfo(task));
				for(ClassCarInfo carInfo : carList) {
					for(CarEvent event : queryList) {
						if(carInfo.getCardNum().equals(event.getCardNum())) {
							returnList.add(event);
							break;
						}
					}
				}
			}
			if(returnList.size()>0) {
				return returnList;
			}
		}
		return queryList;
	}
	
	@Transactional(readOnly = false)
	public String getNewRecordSeq(){
		RecordSeq record = new RecordSeq();
		record.preInsert();
		recordSeqDao.insert(record);
		return record.getId();
	}
	
}