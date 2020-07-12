package com.xk.netty.server;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xk.netty.handler.ChildChannelHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class LongLinksServer implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(LongLinksServer.class);
	
	@Value("${netty.longLinkServer.port}")
	private int port;
	
	@Resource
	private ChildChannelHandler childChannelHandler;
	
	@Value("${netty.maxBossThread}")
	private int maxBossThread;
	
	@Value("${netty.maxWorkThread}")
	private int maxWorkThread;
	
	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(maxBossThread);
		EventLoopGroup workerGroup = new NioEventLoopGroup(maxWorkThread);
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128)
					.option(ChannelOption.SO_KEEPALIVE, true).childHandler(childChannelHandler);
			// 绑定端口，同步等待成功
			ChannelFuture f = bootstrap.bind(port).sync();
			log.info("监听数据同步端口成功：" + port);
			// 等待服务监听端口关闭
			f.channel().closeFuture().sync();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// 退出，释放线程资源
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	

	}
}
