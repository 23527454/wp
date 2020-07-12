package com.xk.netty.util;

import com.alibaba.druid.util.StringUtils;
import com.xk.netty.entity.EquipEntity;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 建立连接时  还注册设备
 */
public class LongLinkChannelNoDataMap {
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	//已连接 未加入数据库的设备列表
	public static ConcurrentHashMap<String, EquipEntity> equipList = new ConcurrentHashMap<String, EquipEntity>();

	public static int maxChnanel = Integer.MAX_VALUE;
	
	public static synchronized boolean addChannel(String equipSn, Channel channel,Map<String,Object> map) {
		channel.attr(AttributeUtil.get("equipSn")).set(equipSn);
		channel.attr(AttributeUtil.get("EquipName")).set(String.valueOf(map.get("EquipName")));
		channel.attr(AttributeUtil.get("HardVersion")).set(String.valueOf(map.get("HardVersion")));
		if(validMaxChannel()) {
			if(group.add(channel)) {
				addEquip(equipSn, map, channel);
				return true;
			}
		}
		return false;
	}

	public static Map<String,Object> getInfomationMap(Channel channel){
		Map<String,Object> map = new HashMap<>();
		map.put("EquipName",channel.attr(AttributeUtil.get("EquipName")).get());
		map.put("HardVersion",channel.attr(AttributeUtil.get("HardVersion")).get());
		return map;
	}

	public static void removeEquipSn(Channel channel) {
			String equipSn = getEquipSn(channel);
			if(!StringUtils.isEmpty(equipSn)) {
				if(equipList.get(equipSn)!=null) {
					equipList.remove(equipSn);
					group.remove(channel);
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

	public static String getEquipSn(Channel channel) {
		return channel.attr(AttributeUtil.get("equipSn")).get();
	}
	
	public static boolean validMaxChannel() {
		return maxChnanel > (group.size()+LongLinkChannelMap.group.size());
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
		equip.setVersion(equipVersion);
		equip.setOperateType(2);
		equipList.put(equipSn, equip);
		
		WebSocketChannelMap.tellNoDataAdd(equip);
	}

}
