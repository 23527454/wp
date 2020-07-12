package com.xk.netty.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

@Component
public class ShortChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Resource
    private ShortLinksServerHandler shortLinksServerHandler;

    public void initChannel(SocketChannel socketChannel) throws Exception {
    	socketChannel.pipeline().addLast(new ShortlinkXmlDecodeHandler());
        socketChannel.pipeline().addLast(shortLinksServerHandler);
    }
}
