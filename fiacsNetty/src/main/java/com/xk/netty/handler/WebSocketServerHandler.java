package com.xk.netty.handler;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xk.netty.util.LongLinkChannelNoDataMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.xk.netty.entity.EquipEntity;
import com.xk.netty.util.LongLinkChannelMap;
import com.xk.netty.util.WebSocketChannelMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

@Component
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	/**
	 * 日志
	 */
	private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

	@Value("${netty.longLinkServer.port}")
	private int serverPort;
	/**
	 * 全局websocket
	 */
	private WebSocketServerHandshaker handshaker;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 普通HTTP接入
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) { // websocket帧类型 已连接
			// BinaryWebSocketFrame CloseWebSocketFrame ContinuationWebSocketFrame
			// PingWebSocketFrame PongWebSocketFrame TextWebScoketFrame
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
		// 如果http解码失败 则返回http异常 并且判断消息头有没有包含Upgrade字段(协议升级)

		/*userId = findUserIdByUri(request.uri());

		// 判断用户权限，通过加入到连接集合
		if (userId != null) {
			boolean idAccess = true;
			if (idAccess) {
				AlarmTemp.addChannel(userId, ctx.channel());
			}
		}*/
		WebSocketChannelMap.group.add(ctx.channel());
		if (!request.decoderResult().isSuccess() || (!"websocket".equalsIgnoreCase(String.valueOf(request.headers().get("Upgrade"))))) {
			sendHttpResponse(ctx, request,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		// 构造握手响应返回
		WebSocketServerHandshakerFactory ws = new WebSocketServerHandshakerFactory("", null, false);
		handshaker = ws.newHandshaker(request);
		if (handshaker == null) {
			// 版本不支持
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), request);
		}
	}

	/**
	 * websocket帧
	 * 
	 * @param ctx
	 * @param frame
	 */
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 判断是否关闭链路指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否Ping消息 -- ping/pong心跳包
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本程序仅支持文本消息， 不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}

		// 返回应答消息 text文本帧
		String request = ((TextWebSocketFrame) frame).text();

		if (request.equals("HeartBeat")) {
			TextWebSocketFrame tws = new TextWebSocketFrame("response heart");
			ctx.channel().writeAndFlush(tws);
		}
		if (request.equals("serverPort")) {
			TextWebSocketFrame tws = new TextWebSocketFrame("serverPort"+serverPort);
			ctx.channel().writeAndFlush(tws);
		}
		// 打印日志
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(String.format("%s received %s", ctx.channel(), request));
		}
		// 发送到客户端websocket
		/*ctx.channel().write(
				new TextWebSocketFrame(request + ", 欢迎使用Netty WebSocket服务， 现在时刻:" + new java.util.Date().toString()));*/
		
		System.out.println("链接设备数："+LongLinkChannelMap.equipList.size());
		for(EquipEntity entity : LongLinkChannelMap.equipList.values()) {
			ctx.channel().writeAndFlush(
					new TextWebSocketFrame(JSON.toJSONString(entity)));
		}
		for(EquipEntity entity : LongLinkChannelNoDataMap.equipList.values()) {
			ctx.channel().writeAndFlush(
					new TextWebSocketFrame(JSON.toJSONString(entity)));
		}
	}

	/**
	 * response
	 * 
	 * @param ctx
	 * @param request
	 * @param response
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request,
			FullHttpResponse response) {
		// 返回给客户端
		if (response.status().code() != HttpResponseStatus.OK.code()) {
			ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(buf);
			buf.release();
			HttpHeaderUtil.setContentLength(response, response.content().readableBytes());
		}
		// 如果不是keepalive那么就关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(response);
		if (!HttpHeaderUtil.isKeepAlive(response) || response.status().code() != HttpResponseStatus.OK.code()) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	/**
	 * 异常 出错
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		WebSocketChannelMap.group.remove(ctx.channel());
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//WebSocketChannelMap.group.add(ctx.channel());
	}

	/*// 通过Uri获取用户Id uri中包含userId
	private String findUserIdByUri(String uri) {
		String userId = "";
		try {
			String tokens = uri.substring(uri.indexOf("userId=") + 7);
			// trim()去掉字符序列左边和右边的空格
			if (tokens != null && tokens.trim() != null && tokens.trim().length() > 0) {
				tokens = tokens.trim();
				String[] t = DesUtil.decrypt(userId, DesUtil.DESCRET).split("-");
				if(t.length==2) {
					return t[0];
				}
			}
		} catch (Exception e) {
		}
		return userId;
	}*/
}