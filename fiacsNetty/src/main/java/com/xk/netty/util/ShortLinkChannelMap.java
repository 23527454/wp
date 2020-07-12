package com.xk.netty.util;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ShortLinkChannelMap {
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static void addChannel(String equipSn, String equipId, Channel channel) {
		channel.attr(AttributeUtil.get("equipSn")).set(equipSn);
		channel.attr(AttributeUtil.get("equipId")).set(equipId);
		group.add(channel);
	}
	
	public static void removeChannel(Channel channel) {
		group.remove(channel);
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
}
