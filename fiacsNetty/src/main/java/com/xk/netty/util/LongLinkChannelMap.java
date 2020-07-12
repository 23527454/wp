package com.xk.netty.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.druid.util.StringUtils;
import com.xk.netty.entity.EquipEntity;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class LongLinkChannelMap {
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public static ConcurrentHashMap<String, EquipEntity> equipList = new ConcurrentHashMap<String, EquipEntity>();

	public static int maxChnanel = Integer.MAX_VALUE;
	
	public static synchronized boolean addChannel(String equipSn, String equipId, Channel channel,Map<String,Object> map) {
		channel.attr(AttributeUtil.get("equipSn")).set(equipSn);
		channel.attr(AttributeUtil.get("equipId")).set(equipId);
		if(validMaxChannel()) {
			if(group.add(channel)) {
				addEquip(equipSn, map, channel);
				return true;
			}
		}
		return false;
	}
	
	public static void removeEquipSn(Channel channel) {
			String equipSn = getEquipSn(channel);
			if(!StringUtils.isEmpty(equipSn)) {
				if(equipList.get(equipSn)!=null) {
					WebSocketChannelMap.tellDelete(equipList.get(equipSn));
					equipList.remove(equipSn);
				}
			}
	}

	public static Channel getChannel(String equipSn) {
		Object[] channels = group.toArray();
		for(Object channel : channels) {
			Channel c = (Channel)channel; 
			String equip = c.attr(AttributeUtil.get("equipSn")).get();
			if(equipSn.equalsIgnoreCase(equip)) {
				return c;
			}
		}
		return null;
	}

	public static List<Channel> getChannelList() {
		List<Channel> returnList = new ArrayList<Channel>();
		Object[] channels = group.toArray();
		for(Object channel : channels) {
			returnList.add((Channel)channel);
		}
		return returnList;
	}
	
	public static String getEquipSn(Channel channel) {
		return channel.attr(AttributeUtil.get("equipSn")).get();
	}
	
	public static String getEquipId(Channel channel) {
		return channel.attr(AttributeUtil.get("equipId")).get();
	}
	
	public static boolean validMaxChannel() {
		return maxChnanel > group.size()+LongLinkChannelNoDataMap.group.size();
	}
	
	public static void addEquip(String equipSn,Map<String,Object> map,Channel channel) {
		String equipName = String.valueOf(map.get("EquipName"));
		String equipVersion = String.valueOf(map.get("HardVersion"));
		EquipEntity equip = new EquipEntity();
		equip.setEquipName(equipName);
		equip.setEquipSn(equipSn);
		Date d = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		equip.setStartTime(sdf.format(d));
		InetSocketAddress insocket = (InetSocketAddress) channel.remoteAddress();
		equip.setIp(insocket.getAddress().getHostAddress());
		equip.setPort(insocket.getPort());
		equip.setOperateType(1);
		equip.setVersion(equipVersion);
		try {
			equip.setCenterIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		equipList.put(equipSn, equip);
		
		WebSocketChannelMap.tellAdd(equip);
	}
}
