package com.xk.netty.util;

import com.alibaba.fastjson.JSON;
import com.fiacs.common.util.Constants;
import com.xk.netty.entity.EquipEntity;
import com.xk.netty.handler.FiacsServerWebSocketServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class FiacsServerWebSocketChannelMap {

	private static final Logger logger = Logger.getLogger(FiacsServerWebSocketChannelMap.class.getName());

	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static void tellMessage(Map<String,Object> map,String equipId){
		Map<String,Object> requestMap = new HashMap<>();
		String requestType = String.valueOf(map.get("type"));
		requestMap.put("type",requestType);
		requestMap.put("eventId",map.get("EventID"));
		requestMap.put("equipId",equipId);
		requestMap.put("time",String.valueOf(map.get("EventDate")));
		if(Constants.CONNECT_EVENT_TYPE.equals(requestType)){
		}else if(Constants.ALARM_EVENT_TYPE.equals(requestType)){
		}else if(Constants.CAR_ARRIVE_EVENT_TYPE.equals(requestType)){
			requestMap.put("carId",map.get("CarID"));
		}else if(Constants.SAFEGUARD_EVENT_TYPE.equals(requestType)){
			requestMap.put("personId",map.get("PersonID"));
		}else if(Constants.ACCESS_ALARM_TYPE.equals(requestType)){
			requestMap.put("personId",map.get("PersonID"));
		}else if(Constants.ACCESS_EVENT_TYPE.equals(requestType)){
			requestMap.put("personId",map.get("PersonID"));
		}else{
			return;
		}
		group.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(requestMap)));
	}
}
