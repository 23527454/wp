package com.xk.netty.util;

import java.util.Iterator;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.xk.netty.entity.EquipEntity;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class WebSocketChannelMap {

	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static void tellAdd(EquipEntity entity) {
		if (!group.isEmpty()) {
			Iterator<Channel> channels = group.iterator();

			while (channels.hasNext()) {
				Channel channel = channels.next();
				entity.setOperateType(1);
				channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(entity)));
			}
		}
	}

	public static void tellDelete(EquipEntity entity) {
		if (!group.isEmpty()) {
			Iterator<Channel> channels = group.iterator();

			while (channels.hasNext()) {
				Channel channel = channels.next();
				entity.setOperateType(0);
				channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(entity)));
			}
		}
	}

	public static void tellNoDataAdd(EquipEntity entity) {
		if (!group.isEmpty()) {
			Iterator<Channel> channels = group.iterator();

			while (channels.hasNext()) {
				Channel channel = channels.next();
				entity.setOperateType(2);
				channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(entity)));
			}
		}
	}
}
