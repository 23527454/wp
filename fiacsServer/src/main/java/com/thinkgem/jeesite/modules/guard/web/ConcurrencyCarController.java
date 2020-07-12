/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.io.IOException;
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
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.CarEvent;
import com.thinkgem.jeesite.modules.guard.entity.CommissionerEvent;
import com.thinkgem.jeesite.modules.guard.entity.ConnectEvent;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.service.CarEventService;

/**
 * 交接事件Controller
 * @author Jumbo
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/concurrency/car")
public class ConcurrencyCarController extends BaseController {

	@Autowired
	private CarEventService carEventService;
	
//	private TaskScheduler scheduler = new ConcurrentTaskScheduler();
//	
//	public static CarEvent topRecord;
	
	@RequestMapping(value = "listByLatestId")
	@ResponseBody
	public List<CarEvent> listByLatestId(@RequestParam("latestId")String latestId,
			@RequestParam("nodes")String nodes)
			throws JsonGenerationException, JsonMappingException, IOException {
 		List<CarEvent> list = carEventService.getFeeds(latestId, nodes);
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
//		if(factory!=null && factory.lookup("CarEvent",true).getAtmosphereResources()!=null){
//			
//			for(AtmosphereResource res : factory.lookup("CarEvent",true).getAtmosphereResources()){
//				if(res.uuid().equals(atmosphereResource.uuid())){
//					found = true;
//					break;
//				}
//			}
//		}
//		if(!found && factory!=null){
//			factory.lookup("CarEvent",true).addAtmosphereResource(atmosphereResource);
//		}
//	}
//	
//	public static BroadcasterFactory getBroadcastFactory() {
//
//        // TODO: replace deprecated getServletContext()....with what?
//        ServletContext servletContext = ServletContextFactory.getDefault().getServletContext();
//        if(servletContext!=null){
//	        AtmosphereFramework framework = (AtmosphereFramework) servletContext.getAttribute("car");
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
//	        	BroadcasterFactory factory = ConcurrencyCarController.getBroadcastFactory();
//	        	if(factory==null){
//	        		ConcurrencyCarController.topRecord = null;
//	        		return;
//	        	}	
//
//	        	if(factory.lookup("CarEvent")==null||factory.lookup("CarEvent").getAtmosphereResources()==null || factory.lookup("CarEvent").getAtmosphereResources().size() == 0){
//	         		ConcurrencyCarController.topRecord = null;
//	         		return;
//	        	}
//	         	JSONObject json = new JSONObject();
//	         	if(ConcurrencyCarController.topRecord==null){
//	         		ConcurrencyCarController.topRecord = carEventService.getTop();
//	         	}
//	         	if(topRecord!=null){
//	         		List<CarEvent> list = carEventService.getFeeds(topRecord.getId());
//	         		if(list.size()>0){
//		         		ConcurrencyCarController.topRecord = list.get(list.size() -1);
//		         		json.put("count", list.size());
//			         	json.put("list", JSONArray.fromObject(JsonMapper.toJsonString(list)));
//			         	
//			         	factory.lookup("CarEvent").broadcast(json.toString());
//		         	}
//	         	}
//	         	
//	         	
//	         	
//	        }
//	    }, 1000*10);
//	}
}