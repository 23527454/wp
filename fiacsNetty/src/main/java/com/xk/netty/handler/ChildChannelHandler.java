package com.xk.netty.handler;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	
	@Value("${netty.timeout}")
	private int timeout;
	
    @Resource
    private LongLinksServerHandler longLinksServerHandler;
    
    public void initChannel(SocketChannel socketChannel) throws Exception {
    	socketChannel.pipeline().addLast(new IdleStateHandler(0, 0, timeout));
    	socketChannel.pipeline().addLast(new LonglinkXmlDecodeHandler());
        socketChannel.pipeline().addLast(longLinksServerHandler);
    }
}
