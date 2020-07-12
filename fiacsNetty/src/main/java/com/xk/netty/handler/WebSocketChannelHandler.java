package com.xk.netty.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

@Component
public class WebSocketChannelHandler extends ChannelInitializer<SocketChannel> {
    
	@Resource
	private WebSocketServerHandler webSocketServerHandler;
	
    @Override
	protected void initChannel(SocketChannel e) throws Exception {
    	 e.pipeline().addLast("http-codec", new HttpServerCodec())
         .addLast("aggregator", new HttpObjectAggregator(65536)) //定义缓冲大小
         .addLast("http-chunked", new ChunkedWriteHandler())
         .addLast("handler", webSocketServerHandler);
	}

}
