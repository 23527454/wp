/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.thinkgem.jeesite.modules.guard.entity.AlarmEvent;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.service.AlarmEventService;

/**
 * 交接事件Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/concurrency/alarm")
public class ConcurrencyAlarmController extends BaseController {

	@Autowired
	private AlarmEventService alarmEventService;
	
	@RequestMapping(value = "listByLatestId")
	@ResponseBody
	public List<AlarmEvent> listByLatestId(@RequestParam("latestId")String latestId,
			@RequestParam("latestTime")String latestTime,
			@RequestParam("nodes")String nodes)
			throws JsonGenerationException, JsonMappingException, IOException {
 		List<AlarmEvent> list = alarmEventService.getFeeds(latestId, latestTime, nodes);
		return list;
	}
	
//	@RequestMapping(value="")
//	@ResponseBody
//	public void eventAsync(AtmosphereResource atmosphereResource) throws JsonGenerationException, JsonMappingException, IOException {
//	
//		this.suspend(atmosphereResource);
//		BroadcasterFactory factory = this.getBroadcastFactory();
//		
//		boolean found = false;
//		if(factory!=null && factory.lookup("AlarmEvent",true).getAtmosphereResources()!=null){
//			
//			for(AtmosphereResource res : factory.lookup("AlarmEvent",true).getAtmosphereResources()){
//				if(res.uuid().equals(atmosphereResource.uuid())){
//					found = true;
//					break;
//				}
//			}
//		}
//		if(!found && factory!=null){
//			factory.lookup("AlarmEvent",true).addAtmosphereResource(atmosphereResource);
//		}
//	}
//	
//	public static BroadcasterFactory getBroadcastFactory() {
//
//        // TODO: replace deprecated getServletContext()....with what?
//        ServletContext servletContext = ServletContextFactory.getDefault().getServletContext();
//        if(servletContext!=null){
//	        AtmosphereFramework framework = (AtmosphereFramework) servletContext.getAttribute("alarm");
//	        if(framework!=null){
//	        	 BroadcasterFactory broadcasterFactory = framework.getBroadcasterFactory();
//	 	        return broadcasterFactory;
//	        }
//        }
//        return null;
//
//    }
//	
//	private void suspend(final AtmosphereResource resource) {
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//	        resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
//	            @Override
//	            public void onSuspend(AtmosphereResourceEvent event) {
//	                countDownLatch.countDown();
//	                resource.removeEventListener(this);
//	            }
//	        });
//	        resource.suspend();
//	        try {
//	            countDownLatch.await();
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//    	}
//
//	@PostConstruct
//	private void executeJob() {
//		
//	    scheduler.scheduleAtFixedRate(new Runnable() {
//	        @Override
//	        public void run() {
//	        	BroadcasterFactory factory = ConcurrencyAlarmController.getBroadcastFactory();
//	        	if(factory==null){
//	        		ConcurrencyAlarmController.topRecord = null;
//	        		return;
//	        	}	
//
//	        	if(factory.lookup("AlarmEvent")==null||factory.lookup("AlarmEvent").getAtmosphereResources()==null || factory.lookup("AlarmEvent").getAtmosphereResources().size() == 0){
//	         		ConcurrencyAlarmController.topRecord = null;
//	         		return;
//	        	}
//	         	JSONObject json = new JSONObject();
//	         	if(ConcurrencyAlarmController.topRecord==null){
//	         		ConcurrencyAlarmController.topRecord = alarmEventService.getTop();
//	         	}
//	         	if(topRecord!=null){
//	         		List<AlarmEvent> list = alarmEventService.getFeeds(topRecord.getId());
//	         		if(list.size()>0){
//		         		ConcurrencyAlarmController.topRecord = list.get(list.size() -1);
//		         		json.put("count", list.size());
//			         	json.put("list", JSONArray.fromObject(JsonMapper.toJsonString(list)));
//			         	factory.lookup("AlarmEvent").broadcast(json.toString());
//		         	}
//	         	}
//	        }
//	    }, 1000*10);
//	}
}