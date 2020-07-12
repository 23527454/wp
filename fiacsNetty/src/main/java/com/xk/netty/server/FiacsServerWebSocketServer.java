package com.xk.netty.server;

import com.xk.netty.handler.FiacsServerWebSocketChannelHandler;
import com.xk.netty.handler.WebSocketChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FiacsServerWebSocketServer implements Runnable{

    private static Logger log = LoggerFactory.getLogger(FiacsServerWebSocketServer.class);
    @Resource
	private FiacsServerWebSocketChannelHandler fiacsServerWebSocketChannelHandler;
	
	@Override
	public void run() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(fiacsServerWebSocketChannelHandler);
            
            ChannelFuture f = b.bind(10004).sync();
            log.info("fiacsServer============success");
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    
		
	}
}
