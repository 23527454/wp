/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.util.ServletContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.CommissionerEventDao;
import com.thinkgem.jeesite.modules.guard.dao.EventDetailDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffDao;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.service.ConnectEventService;
import com.thinkgem.jeesite.modules.guard.service.StaffService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 交接事件Controller
 * 
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/concurrency/connect")
public class ConcurrencyConnectController extends BaseController {

	@Autowired
	private ConnectEventService connectEventService;
	@Autowired
	private CommissionerEventDao commissionerEventDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private EventDetailDao eventDetailDao;
	
	@RequestMapping(value = "listByLatestId")
	@ResponseBody
	public List<ConnectEvent> listByLatestId(@RequestParam("latestId")String latestId,
			@RequestParam("latestTime")String latestTime,
			@RequestParam("nodes")String nodes)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<ConnectEvent> list = connectEventService.getFeeds(latestId, latestTime, nodes);
		
		for (ConnectEvent connectEvent : list) {
			Set<String> commSta = new HashSet<String>();
			Set<String> secuSta = new HashSet<String>();
			
			List<CommissionerEvent> commissionerEventList = commissionerEventDao.findList(new CommissionerEvent(connectEvent));
			for (int j = 0; j < commissionerEventList.size(); j++) {
				Staff comm = staffService.getWithDel(commissionerEventList.get(j).getPersonId());
				if(null != comm){
					commSta.add(comm.getName());
				}
			}
			List<EventDetail> eventDetailList = eventDetailDao.findList(new EventDetail(connectEvent));
			for (int j = 0; j < eventDetailList.size(); j++) {
				Staff secu = staffService.getWithDel(eventDetailList.get(j).getPersonId());
				if(null != secu){
					secuSta.add(secu.getName());
				}
			}
			connectEvent.setCommissioner(String.join(",", commSta));
			connectEvent.setSecurityStaff(String.join(",", secuSta));
		}
		return list;
	}

//	private TaskScheduler scheduler = new ConcurrentTaskScheduler();
//	public static ConnectEvent topRecord;
//	@RequestMapping(value = "")
//	@ResponseBody
//	public void eventAsync(AtmosphereResource atmosphereResource)
//			throws JsonGenerationException, JsonMappingException, IOException {
//
//		this.suspend(atmosphereResource);
//		BroadcasterFactory factory = this.getBroadcastFactory();
//
//		boolean found = false;
//		if (factory != null && factory.lookup("ConnectEvent", true).getAtmosphereResources() != null) {
//
//			for (AtmosphereResource res : factory.lookup("ConnectEvent", true).getAtmosphereResources()) {
//				if (res.uuid().equals(atmosphereResource.uuid())) {
//					found = true;
//					break;
//				}
//			}
//		}
//		if (!found && factory != null) {
//			factory.lookup("ConnectEvent", true).addAtmosphereResource(atmosphereResource);
//		}
//	}
//
//	public static BroadcasterFactory getBroadcastFactory() {
//
//		// TODO: replace deprecated getServletContext()....with what?
//		ServletContext servletContext = ServletContextFactory.getDefault().getServletContext();
//		if (servletContext != null) {
//			AtmosphereFramework framework = (AtmosphereFramework) servletContext.getAttribute("connect");
//			if (framework != null) {
//				BroadcasterFactory broadcasterFactory = framework.getBroadcasterFactory();
//				return broadcasterFactory;
//			}
//		}
//		return null;
//
//	}
//
//	private void suspend(final AtmosphereResource resource) {
//		final CountDownLatch countDownLatch = new CountDownLatch(1);
//		resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
//			@Override
//			public void onSuspend(AtmosphereResourceEvent event) {
//				countDownLatch.countDown();
//				resource.removeEventListener(this);
//			}
//		});
//		resource.suspend();
//		try {
//			countDownLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@PostConstruct
//	private void executeJob() {
//
//		scheduler.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				BroadcasterFactory factory = EventController.getBroadcastFactory();
//				if (factory == null) {
//					EventController.topRecord = null;
//					return;
//				}
//
//				if (factory.lookup("ConnectEvent") == null
//						|| factory.lookup("ConnectEvent").getAtmosphereResources() == null
//						|| factory.lookup("ConnectEvent").getAtmosphereResources().size() == 0) {
//					EventController.topRecord = null;
//					return;
//				}
//				JSONObject json = new JSONObject();
//				if (EventController.topRecord == null) {
//					EventController.topRecord = connectEventService.getTop();
//				}
//				List<ConnectEvent> list = connectEventService.getFeeds(topRecord.getId());
//				List<String> commSta = new ArrayList<String>();
//				List<String> secuSta = new ArrayList<String>();
//				if (list.size() > 0) {
//					for (int i = 0; i < list.size(); i++) {
//						List<CommissionerEvent> commissionerEventList = commissionerEventDao
//								.findAllList(new CommissionerEvent(list.get(i)));
//						for (int j = 0; j < commissionerEventList.size(); j++) {
//							Staff comm = staffService.getWithDel(commissionerEventList.get(j).getPersonId());
//							if(null != comm){
//								commSta.add(comm.getName());
//							}
//						}
//						List<EventDetail> eventDetailList = eventDetailDao
//								.findAllList(new EventDetail(list.get(i)));
//						for (int j = 0; j < eventDetailList.size(); j++) {
//							Staff secu = staffService.getWithDel(eventDetailList.get(j).getPersonId());
//							if(null != secu){
//								secuSta.add(secu.getName());
//							}
//						}
//						list.get(i).setCommissioner(String.join(",", commSta));
//						list.get(i).setSecurityStaff(String.join(",", secuSta));
//					}
//
//					EventController.topRecord = list.get(list.size() - 1);
//					json.put("count", list.size());
//					json.put("list", JSONArray.fromObject(JsonMapper.toJsonString(list)));
//
//					factory.lookup("ConnectEvent").broadcast(json.toString());
//				}
//			}
//		}, 1000 * 10);
//	}
}