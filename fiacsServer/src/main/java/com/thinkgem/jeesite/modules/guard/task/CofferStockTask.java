package com.thinkgem.jeesite.modules.guard.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.CarEventDao;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEventDetail;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxOrder;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxReturn;
import com.thinkgem.jeesite.modules.guard.service.CarEventService;
import com.thinkgem.jeesite.modules.guard.service.CofferConnectService;
import com.thinkgem.jeesite.modules.guard.service.ConnectEventService;
import com.thinkgem.jeesite.modules.guard.service.EquipmentService;
import com.thinkgem.jeesite.modules.guard.service.EventDetailService;
import com.thinkgem.jeesite.modules.guard.service.LineService;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxAllotService;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxOrderService;
import com.thinkgem.jeesite.modules.guard.service.MoneyBoxReturnService;
import com.thinkgem.jeesite.modules.guard.service.SysConfigService;

//@Lazy(false)
@Service
public class CofferStockTask {
	
	@Autowired
	private MoneyBoxAllotService allotService;
	@Autowired
	private MoneyBoxReturnService returnService;
	@Autowired
	private MoneyBoxOrderService orderService;
	@Autowired
	private LineService lineService;
	@Autowired
	private ConnectEventService connectEventService;
	@Autowired
	private MoneyBoxEventDetailDao moneyBoxEventDetailDao;
	/**
	 * 统计款箱出入库信息
	 */
	@Autowired
	private SysConfigService sysConfigService;
	
	@Scheduled(cron="* */2 * * * ?")
	public void calculateBox() {
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		List<Line> lineList = lineService.findList(new Line());
		List<LineNodes> lineNodesList = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleTime", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		map.put("returnTime",  DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		map.put("allotReturnTime", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		for (Line line2 : lineList) {
			Map<String, Object> sumMap = new HashMap<String, Object>();
			List<MoneyBoxAllot> allotList = new ArrayList<MoneyBoxAllot>();
			List<MoneyBoxReturn> returnList = new ArrayList<MoneyBoxReturn>();
			List<MoneyBoxOrder> orderList = new ArrayList<MoneyBoxOrder>();
			List<MoneyBoxEventDetail> inList = new ArrayList<MoneyBoxEventDetail>();
			List<MoneyBoxEventDetail> outList = new ArrayList<MoneyBoxEventDetail>();
			sumMap.put("line", line2);
			lineNodesList = lineService.get(line2.getId()).getLineNodesList();
			for (int i = 0; i < lineNodesList.size(); i++) {
				if (lineNodesList.size() > 0 && lineNodesList.get(i).getOffice() != null) {
					map.put("equipmentId", lineNodesList.get(i).getEquipmentId());

					List<MoneyBoxAllot> allotList1 = allotService.findListByArea(map);
					Set<String> temp = new HashSet<String>();
					List<MoneyBoxAllot> allotList2 = new ArrayList<MoneyBoxAllot>();
					for (MoneyBoxAllot moneyBoxAllot : allotList1) {
						if (temp.add(moneyBoxAllot.getCardNum())) {
							allotList2.add(moneyBoxAllot);
						}
					}

					List<MoneyBoxReturn> returnList1 = returnService.findListByArea(map);
					Set<String> temp2 = new HashSet<String>();
					List<MoneyBoxReturn> returnList2 = new ArrayList<MoneyBoxReturn>();
					for (MoneyBoxReturn moneyBoxReturn : returnList1) {
						if (temp2.add(moneyBoxReturn.getCardNum())) {
							returnList2.add(moneyBoxReturn);
						}
					}

					List<MoneyBoxOrder> orderList1 = orderService.findListByArea(map);

					Set<String> temp3 = new HashSet<String>();
					List<MoneyBoxOrder> orderList2 = new ArrayList<MoneyBoxOrder>();
					for (MoneyBoxOrder moneyBoxOrder : orderList1) {
						if (temp3.add(moneyBoxOrder.getCardNum())) {
							for (MoneyBoxAllot moneyBoxAllot : allotList2) {
								orderList2.add(moneyBoxOrder);
								// 预约优先调拨
								if (moneyBoxOrder.getCardNum().equals(moneyBoxAllot.getCardNum())) {
									allotList2.remove(moneyBoxAllot);
								}
							}
						}
					}
					allotList.addAll(allotList2);
					returnList.addAll(returnList2);
					orderList.addAll(orderList2);
					//totalList.add(sumMap);//得到对应线路的计划
					
					//得到该线路实际数据
					MoneyBoxEventDetail query = new MoneyBoxEventDetail();
					query.setEventType(EventDetail.EVENT_TYPE_CONNECT_EVENT);
					query.setDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
					query.setEquId(lineNodesList.get(i).getEquipmentId());
					List<MoneyBoxEventDetail> eventDetailList = moneyBoxEventDetailDao.findList(query);
					
					for(MoneyBoxEventDetail boxEvent : eventDetailList) {
						//
						ConnectEvent cenentQuery = new ConnectEvent();
						cenentQuery.setRecordId(boxEvent.getRecordId());
						cenentQuery.setEquipmentId(lineNodesList.get(i).getEquipmentId());
						ConnectEvent cevent = connectEventService.findOne(cenentQuery);
						if(cevent!=null) {//存在关联的事件任务类型
							//派送
							if(cevent.getType().equals(ConnectEvent.TASK_TYPE_PRECIOUS_METAL_SEND)||
									cevent.getType().equals(ConnectEvent.TASK_TYPE_SEND)||
										cevent.getType().equals(ConnectEvent.TASK_TYPE_TEMPORARY_SEND)) {
								int stop = 0;
								for(MoneyBoxAllot bAllot : allotList2) {
									if(bAllot.getCardNum().equals(boxEvent.getCardNum())) {
										outList.add(boxEvent);
										stop=1;
										break;
									}
								}
								for(MoneyBoxOrder box : orderList2) {
									if(stop==1) {
										break;
									}else {
										if(box.getCardNum().equals(boxEvent.getCardNum())) {
											outList.add(boxEvent);
											break;
										}
									}
								}
							}else {//派送
								for(MoneyBoxReturn boxRe : returnList2) {
									if(boxRe.getCardNum().equals(boxEvent.getCardNum())) {
										inList.add(boxEvent);
										break;
									}
								}
							}
						}else {//不存在的时候 根据time时间是否大于下午3点来判断
							//TODO
							String cutTime = DateUtils.formatDate(new Date(), "yyy-MM-dd ")+sysConfigService.getDtTaskCut();
							//下午3点前  为派送
							if(DateUtils.parseDate(cutTime).getTime()>=DateUtils.parseDate(boxEvent.getTime()).getTime()) {
								int stop = 0;
								for(MoneyBoxAllot bAllot : allotList2) {
									if(bAllot.getCardNum().equals(boxEvent.getCardNum())) {
										outList.add(boxEvent);
										stop=1;
										break;
									}
								}
								for(MoneyBoxOrder box : orderList2) {
									if(stop==1) {
										break;
									}else {
										if(box.getCardNum().equals(boxEvent.getCardNum())) {
											outList.add(boxEvent);
											break;
										}
									}
								}
							}else {
								for(MoneyBoxReturn boxRe : returnList2) {
									if(boxRe.getCardNum().equals(boxEvent.getCardNum())) {
										inList.add(boxEvent);
										break;
									}
								}
							}
						}
					}
					
				}
			}
			sumMap.put("allot", allotList);
			sumMap.put("order", orderList);
			sumMap.put("returnBox", returnList);
			sumMap.put("inBox", inList);
			sumMap.put("outBox", outList);
			
			totalList.add(sumMap);
		}
		CacheUtils.put("cofferStock", totalList);
	}
}
