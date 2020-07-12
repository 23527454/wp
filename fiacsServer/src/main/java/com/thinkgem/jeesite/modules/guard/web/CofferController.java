package com.thinkgem.jeesite.modules.guard.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*import com.sun.xml.internal.ws.api.server.SDDocumentFilter;
import com.thinkgem.jeesite.common.service.ServiceException;*/
import com.thinkgem.jeesite.common.utils.AssertUtil;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.guard.dao.CarEventDao;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDao;
import com.thinkgem.jeesite.modules.guard.dao.MoneyBoxEventDetailDao;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEventParam;
import com.thinkgem.jeesite.modules.guard.entity.Equipment;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Line;
import com.thinkgem.jeesite.modules.guard.entity.LineNodes;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxAllot;
import com.thinkgem.jeesite.modules.guard.entity.MoneyBoxEvent;
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
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;

/**
 * 金库出入库Controller
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/coffer")
public class CofferController {

	@Autowired
	private MoneyBoxAllotService allotService;
	@Autowired
	private MoneyBoxReturnService returnService;
	@Autowired
	private MoneyBoxOrderService orderService;
	@Autowired
	private CofferConnectService cofferConnectService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private LineService lineService;
	@Autowired
	private EventDetailService eventDetailService;
	@Autowired
	private CarEventService carEventService;
	@Autowired
	private ConnectEventService connectEventService;

	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private CommissionerEventDao commissionerEventDao;
	@Autowired
	private MoneyBoxEventDetailDao moneyBoxEventDetailDao;
	@Autowired
	private MoneyBoxEventDao moneyBoxEventDao;
	@Autowired
	private CarEventDao carEventDao;
	@Autowired
	private EventDetailDao eventDetailDao;

	/**
	 * 统计款箱出入库信息
	 */
	@Autowired
	private SysConfigService sysConfigService;

	//@RequiresPermissions("guard:coffer:view")
	@RequestMapping(value = { "summary" })
	public String list(@RequestParam(required = false) String lineId, HttpServletRequest request,
			HttpServletResponse response, Model model) {

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
							/*for (MoneyBoxAllot moneyBoxAllot : allotList2) {
								orderList2.add(moneyBoxOrder);
								// 预约优先调拨
								if (moneyBoxOrder.getCardNum().equals(moneyBoxAllot.getCardNum())) {
									allotList2.remove(moneyBoxAllot);
								}
							}*/
							for(int j=0;j<allotList2.size();j++) {
								MoneyBoxAllot moneyBoxAllot = allotList2.get(j);
								orderList2.add(moneyBoxOrder);
								// 预约优先调拨
								if (moneyBoxOrder.getCardNum().equals(moneyBoxAllot.getCardNum())) {
									allotList2.remove(j);
									j--;
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
		if(StringUtils.isNotBlank(lineId)) {
			for(Map<String,Object> totalMap : totalList) {
				Line mapLine = (Line)totalMap.get("line");
				if(mapLine.getId().equals(lineId)) {
					List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
					returnList.add(totalMap);
					model.addAttribute("data", returnList);
					break;
				}
			}
		}else {
			model.addAttribute("data", totalList);
		}
		
		return "modules/guard/cofferStock";
	
		/*List<Map<String, Object>> totalList = (List<Map<String, Object>>)CacheUtils.get("cofferStock");
		if(totalList==null||totalList.size()==0) {
			List<Line> lineList = lineService.findList(new Line());
			for(Line line2 : lineList) {
				Map<String, Object> sumMap = new HashMap<String, Object>();
				List<MoneyBoxAllot> allotList = new ArrayList<MoneyBoxAllot>();
				List<MoneyBoxReturn> returnList = new ArrayList<MoneyBoxReturn>();
				List<MoneyBoxOrder> orderList = new ArrayList<MoneyBoxOrder>();
				List<MoneyBoxEventDetail> inList = new ArrayList<MoneyBoxEventDetail>();
				List<MoneyBoxEventDetail> outList = new ArrayList<MoneyBoxEventDetail>();
				sumMap.put("line", line2);
				sumMap.put("allot", allotList);
				sumMap.put("order", orderList);
				sumMap.put("returnBox", returnList);
				sumMap.put("inBox", inList);
				sumMap.put("outBox", outList);
				totalList = new ArrayList<Map<String, Object>>();
				totalList.add(sumMap);
			}
		}
		//model.addAttribute("data", totalList);
		if(StringUtils.isNotBlank(lineId)) {
			for(Map<String,Object> totalMap : totalList) {
				Line mapLine = (Line)totalMap.get("line");
				if(mapLine.getId().equals(lineId)) {
					List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
					returnList.add(totalMap);
					model.addAttribute("data", returnList);
					break;
				}
			}
		}else {
			model.addAttribute("data", totalList);
		}
		return "modules/guard/cofferStock";*/
	}

	@RequiresPermissions("guard:coffer:view")
	@RequestMapping(value = { "index", "" })
	public String index(Equipment equipment, Model model) {
		return "modules/guard/cofferIndex";
	}

	@RequiresPermissions("guard:coffer:view")
	@RequestMapping(value = { "connectIndex", "" })
	public String connectIndex(Equipment equipment, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// MoneyBoxEventDetail moneyBoxEventDetailRequest = new MoneyBoxEventDetail();
		// moneyBoxEventDetailRequest.setEquipmentId(equipment.getId());
		// moneyBoxEventDetailDao.findMoneyBoxDateList(moneyBoxEventDetailRequest);
		// model.addAttribute("", attributeValue)
		return connect(null, null, null, request, response, model);
	}

	// @RequiresPermissions("guard:coffer:connect")
	@RequestMapping(value = { "connect" })
	public String connect(@RequestParam(value = "officeId", required = true) String officeId,
			@RequestParam(value = "date", required = false) String eventDate,
			@RequestParam(value = "lineId", required = false) String lineId, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		String isFromSearchBtn = request.getParameter("isFromSearchBtn");// 是否是查询按钮提交的请求
		String taskType = request.getParameter("taskType");// 任务类型
		Date currentDate = new Date();

		if (!"1".equals(isFromSearchBtn)) {// 是点击菜单的请求
			eventDate = DateUtils.formatDate(currentDate);
		}
		String dtMoneyBoxDeliveryEndStr = eventDate + " " + sysConfigService.getDtTaskCut();
		Date dtMoneyBoxDeliveryEnd = DateUtils.parseDate(dtMoneyBoxDeliveryEndStr);
		String dtMoneyBoxRecycleStartStr = eventDate + " " + sysConfigService.getDtTaskCut();
		Date dtMoneyBoxRecycleStart = DateUtils.parseDate(dtMoneyBoxRecycleStartStr);
		if (!"1".equals(isFromSearchBtn)) {// 是点击菜单的请求
			if (currentDate.before(dtMoneyBoxDeliveryEnd)) {
				taskType = ConnectEvent.TASK_TYPE_SEND;
			} else if (currentDate.after(dtMoneyBoxRecycleStart)) {
				taskType = ConnectEvent.TASK_TYPE_RECOVERY;
			}
		}

		List<Office> officeList = officeService.findZxjkOfficeList(false);
		if (null == officeId) {
			if (!CollectionUtils.isEmpty(officeList)) {
				officeId = officeList.get(0).getId();
			}
		}
		if (null == officeId) {
			return "modules/guard/cofferConnect";
		}
		model.addAttribute("zxjkOfficeList", officeList);
		model.addAttribute("date", eventDate);
		model.addAttribute("officeId", officeId);
		model.addAttribute("taskType", taskType);

		// Office o = officeService.get(officeId);
		Equipment equipment = equipmentService.findOne(new Equipment(new Office(officeId)));
		String equipmentId = equipment.getId();
		// 车辆
		List<CarEvent> carList = null;

		if (ConnectEvent.TASK_TYPE_SEND.equals(taskType)) {
			carList = cofferConnectService.getCarEventList(equipmentId, lineId, dtMoneyBoxDeliveryEndStr, null,
					eventDate);
		} else if (ConnectEvent.TASK_TYPE_RECOVERY.equals(taskType)) {
			carList = cofferConnectService.getCarEventList(equipmentId, lineId, null, dtMoneyBoxRecycleStartStr,
					eventDate);
		} else {
			carList = cofferConnectService.getCarEventList(equipmentId, lineId, null, null, eventDate);
		}

		// 专员
		List<CommissionerEvent> commissionerList = null;// cofferConnectService.getCofferCommissionerList(equipmentId,
														// eventDate, CommissionerEvent.EVENT_TYPE_SUPER_GO_EVENT);

		if (ConnectEvent.TASK_TYPE_SEND.equals(taskType)) {
			commissionerList = cofferConnectService.getCofferCommissionerList(equipmentId, lineId, eventDate,
					dtMoneyBoxDeliveryEndStr, null, CommissionerEvent.EVENT_TYPE_SUPER_GO_EVENT);
		} else if (ConnectEvent.TASK_TYPE_RECOVERY.equals(taskType)) {
			commissionerList = cofferConnectService.getCofferCommissionerList(equipmentId, lineId, eventDate, null,
					dtMoneyBoxRecycleStartStr, CommissionerEvent.EVENT_TYPE_SUPER_GO_EVENT);
		} else {
			commissionerList = cofferConnectService.getCofferCommissionerList(equipmentId, lineId, eventDate, null,
					null, CommissionerEvent.EVENT_TYPE_SUPER_GO_EVENT);
		}

		// 押款员
		List<EventDetail> eventDetailList = null;// cofferConnectService.getCofferEventDetailList(equipmentId,
													// eventDate, EventDetail.EVENT_TYPE_SUPER_GO_EVENT);
		if (ConnectEvent.TASK_TYPE_SEND.equals(taskType)) {
			eventDetailList = cofferConnectService.getCofferEventDetailList(equipmentId, lineId, eventDate,
					dtMoneyBoxDeliveryEndStr, null, EventDetail.EVENT_TYPE_SUPER_GO_EVENT);
		} else if (ConnectEvent.TASK_TYPE_RECOVERY.equals(taskType)) {
			eventDetailList = cofferConnectService.getCofferEventDetailList(equipmentId, lineId, eventDate, null,
					dtMoneyBoxRecycleStartStr, EventDetail.EVENT_TYPE_SUPER_GO_EVENT);
		} else {
			eventDetailList = cofferConnectService.getCofferEventDetailList(equipmentId, lineId, eventDate, null, null,
					EventDetail.EVENT_TYPE_SUPER_GO_EVENT);
		}

		// 款箱
		List<MoneyBoxEventDetail> moneyBoxEventDetailList = null;
		if (ConnectEvent.TASK_TYPE_SEND.equals(taskType)) {
			moneyBoxEventDetailList = cofferConnectService.getCofferMoneyBoxDetailList(equipmentId, lineId, eventDate,
					dtMoneyBoxDeliveryEndStr, null, MoneyBoxEventDetail.EVENT_TYPE_CONNECT_EVENT);
		} else if (ConnectEvent.TASK_TYPE_RECOVERY.equals(taskType)) {
			moneyBoxEventDetailList = cofferConnectService.getCofferMoneyBoxDetailList(equipmentId, lineId, eventDate,
					null, dtMoneyBoxRecycleStartStr, MoneyBoxEventDetail.EVENT_TYPE_CONNECT_EVENT);
		} else {
			moneyBoxEventDetailList = cofferConnectService.getCofferMoneyBoxDetailList(equipmentId, lineId, eventDate,
					null, null, MoneyBoxEventDetail.EVENT_TYPE_CONNECT_EVENT);
		}
		Line line = new Line();
		line.setArea(equipment.getArea());
		List<Line> lineList = lineService.findList(line);

		model.addAttribute("carList", carList);
		model.addAttribute("commissionerList", commissionerList);
		model.addAttribute("eventDetailList", eventDetailList);
		model.addAttribute("moneyBoxEventDetailList", moneyBoxEventDetailList);
		model.addAttribute("lineList", lineList);
		return "modules/guard/cofferConnect";
	}

	@RequiresPermissions("guard:coffer:view")
	@RequestMapping(value = "save")
	@ResponseBody
	@Transactional(readOnly = false)
	public String save(ConnectEventParam connect, Model model, RedirectAttributes redirectAttributes) {
		AssertUtil.assertConnectionNotEmpty(connect.getEventDetailList(), "EventDetailList");
		AssertUtil.assertConnectionNotEmpty(connect.getCommissionerList(), "CommissionerList");
		AssertUtil.assertConnectionNotEmpty(connect.getMoneyBoxList(), "moneyBoxList");
		AssertUtil.assertNotBlank(connect.getOfficeId(), "OfficeId");

		// if(connect.getMoneyBoxList().size() != 1){
		// throw new ServiceException("MoneyBoxList必须选一个");
		// }
		// if(connect.getCarList().size() != 1){
		// throw new ServiceException("CarList必须选一个");
		// }

		if (connect.getEventDetailList().size() != sysConfigService.getSuperGoNumInt()) {
			String message = "押款员数量必须等于" + sysConfigService.getSuperGoNumInt();
			return "{\"result\":\"ERROR\", \"message\": \"" + message + "\"}";
		}

		if (connect.getCommissionerList().size() != sysConfigService.getInterNumInt()) {
			String message = "专员数量必须等于" + sysConfigService.getInterNumInt();
			return "{\"result\":\"ERROR\", \"message\": \"" + message + "\"}";
		}
		String recordId = cofferConnectService.getNewRecordSeq();
		ConnectEvent connectEvent = new ConnectEvent();
		connectEvent.setRecordId(recordId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// connectEvent.setTime(DateUtils.getDateTime());

		for (MoneyBoxEventDetail moneyBoxEventDetail : connect.getMoneyBoxList()) {
			MoneyBoxEventDetail newBoxEventDetail = moneyBoxEventDetailDao.get(moneyBoxEventDetail);
			Long t = 0L;
			try {
				if (null != newBoxEventDetail.getTime() && null == connectEvent.getTime()) {
					t = sdf.parse(newBoxEventDetail.getTime()).getTime();
					connectEvent.setTime(sdf.format(new Date(t)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			newBoxEventDetail.setId(null);
			newBoxEventDetail.setRecordId(recordId);
			newBoxEventDetail.setEventType(EventDetail.EVENT_TYPE_CONNECT_EVENT);
			newBoxEventDetail.setFlag(0);
			newBoxEventDetail.setConnectFlag(2);
			newBoxEventDetail.setTime(connectEvent.getTime());
			moneyBoxEventDetailDao.insert(newBoxEventDetail);

			// 逻辑删除款箱
			newBoxEventDetail.setId(moneyBoxEventDetail.getId());
			moneyBoxEventDetailDao.delflag(newBoxEventDetail);

		}

		for (int i = 0; connect.getEventDetailList() != null && i < connect.getEventDetailList().size(); i++) {
			EventDetail record = eventDetailService.get(connect.getEventDetailList().get(i));

			record.setId(null);
			record.setRecordId(recordId);
			record.setEventType(EventDetail.EVENT_TYPE_CONNECT_EVENT);
			record.setTime(connectEvent.getTime());
			eventDetailService.save(record);
			record.setId(connect.getEventDetailList().get(i).getId());
			eventDetailDao.delflag(record);
			/*
			 * if(null!=record.getTime()) { connectEvent.setTime(record.getTime()); }
			 */
		}

		for (CommissionerEvent commissionerEvent : connect.getCommissionerList()) {
			CommissionerEvent newCommissionerEvent = commissionerEventDao.get(commissionerEvent);
			// commissionerEvent.setId(null);
			newCommissionerEvent.setRecordId(recordId);
			newCommissionerEvent.setEventType(EventDetail.EVENT_TYPE_CONNECT_EVENT);
			newCommissionerEvent.setTime(connectEvent.getTime());
			newCommissionerEvent.setFlag(0);
			;
			commissionerEventDao.insert(newCommissionerEvent);
			newCommissionerEvent.setId(commissionerEvent.getId());
			// 逻辑删除专员
			commissionerEventDao.delflag(newCommissionerEvent);
			/*
			 * if(null!=commissionerEvent.getTime()) {
			 * connectEvent.setTime(commissionerEvent.getTime()); }
			 */
		}

		Equipment equipment = equipmentService.getByOfficeId(connect.getOfficeId());
		// 生成事件错误（guard_connect_event表缺card_num,money_box_event_id错误,
		MoneyBoxEvent moneyBoxEvent = new MoneyBoxEvent();
		moneyBoxEvent.setRecordId(recordId);
		moneyBoxEvent.setEquipmentId(equipment.getId());
		moneyBoxEvent.setEquipSn(equipment.getSerialNum());
		moneyBoxEvent.setTime(connectEvent.getTime());
		moneyBoxEvent.setTaskId("0");
		moneyBoxEvent.setClassTaskId("0");
		moneyBoxEventDao.insert(moneyBoxEvent);

		if (null != connect.getCarList()) {

			CarEvent carEvent = carEventService.get(connect.getCarList().get(0));
			carEvent.setId(null);
			carEvent.setRecordId(recordId);
			carEvent.setTime(connectEvent.getTime());
			carEvent.setFlag(1);
			carEventService.save(carEvent);
			carEvent.setId(connect.getCarList().get(0).getId());
			carEventDao.delflag(carEvent);

			connectEvent.setCarId(carEvent.getCarId());
			connectEvent.setCardNum(carEvent.getCardNum());
			connectEvent.setTaskId(carEvent.getTaskId());
			connectEvent.setTaskType(connect.getTaskType());
			connectEvent.setEquipmentId(carEvent.getEquipmentId());
			connectEvent.setEquipSn(carEvent.getEquipSn());
			connectEvent.setMoneyBoxEventId(moneyBoxEvent.getId());
			connectEventService.save(connectEvent);
		} else {
			connectEvent.setTaskId("0");
			connectEvent.setCarId("-1");
			connectEvent.setTaskType(connect.getTaskType());
			connectEvent.setEquipmentId(moneyBoxEvent.getEquipmentId());
			connectEvent.setEquipSn(moneyBoxEvent.getEquipSn());
			connectEvent.setMoneyBoxEventId(moneyBoxEvent.getId());
			connectEvent.setCardNum(" ");
			connectEventService.save(connectEvent);
		}
		return "{\"result\":\"SUCCESS\"}";
	}
}
