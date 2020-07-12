package com.xk.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FiacsServerWebSocketChannelHandler extends ChannelInitializer<SocketChannel> {
    
	@Resource
	private FiacsServerWebSocketServerHandler fiacsServerWebSocketServerHandler;
	
    @Override
	protected void initChannel(SocketChannel e) throws Exception {
    	 e.pipeline().addLast("http-codec", new HttpServerCodec())
         .addLast("aggregator", new HttpObjectAggregator(65536)) //定义缓冲大小
         .addLast("http-chunked", new ChunkedWriteHandler())
         .addLast("handler", fiacsServerWebSocketServerHandler);
	}

}
