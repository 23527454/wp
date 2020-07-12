package com.xk.netty;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.Resource;
import javax.swing.JOptionPane;

import com.xk.netty.server.FiacsServerWebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fiacs.common.util.Constants;
import com.xk.netty.server.LongLinksServer;
import com.xk.netty.server.ShortLinksServer;
import com.xk.netty.server.WebSocketServer;
import com.xk.netty.service.UploadInfoService;
import com.xk.netty.util.LongLinkChannelMap;
import com.xk.netty.util.SuperDogUtil;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = "com.xk")
@MapperScan("com.xk.netty.dao")
@EnableScheduling
public class NettyApplication implements CommandLineRunner {
	
	private static Logger log = LoggerFactory.getLogger(NettyApplication.class);
	
	@Resource
	private LongLinksServer longLinksServer;
	@Resource
	private ShortLinksServer shortLinksServer;
	@Resource
	private WebSocketServer webSocketServer;
	@Resource
	private FiacsServerWebSocketServer fiacsServerWebSocketServer;
	@Resource
	private UploadInfoService uploadInfoServiceImpl;
	
	public static void main(String[] args) { 
		try {
			System. setProperty("java.awt.headless", "false");
		SpringApplication.run(NettyApplication.class, args);
		}catch (Exception e) {
			if(e!=null&&e.getMessage()!=null&&!e.getMessage().contains("devtools")) {
				log.error("链接数据库失败");
				JOptionPane.showMessageDialog(null, "netty链接数据库失败", "失败", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		//通讯秘钥来源  application.properties里面配置
		try {
			if(!SuperDogUtil.disabled) {
				if(SuperDogUtil.checkDog()) {
					if(SuperDogUtil.readServerAttribute()) {
						if(!SuperDogUtil.checkDogSqDate()) {
							log.error("================已过有效期====================");
							log.error("================启动失败====================");
							throw new Exception("已过有效期");
						}
						int total = SuperDogUtil.readSqData();
						LongLinkChannelMap.maxChnanel=total;
					}else {
						log.error("=============未检测到服务器属性==================");
						throw new Exception("未检测到服务器属性");
					//	JOptionPane.showMessageDialog(null, "未检测到服务器属性", "失败", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					log.error("=============未检测到加密狗==================");
					log.error("================启动失败====================");
					//JOptionPane.showMessageDialog(null, "未检测到加密狗", "失败", JOptionPane.ERROR_MESSAGE);
					throw new Exception("未检测到加密狗");
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			log.error("=============未检测到加密狗或者加密狗异常==================");
			log.error("================启动失败====================");
			JOptionPane.showMessageDialog(null, "未检测到加密狗或者加密狗异常", "失败", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if(uploadInfoServiceImpl.queryIsAllowSuperDogCode()>0) {
			Constants.COMMUNICATION_KEY_FACTOR = SuperDogUtil.readSystemCode();
		}
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				longLinksServer.run();
			}
		});

		t1.start();
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				shortLinksServer.run();
			}
		});

		t2.start();
		
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				webSocketServer.run();
			}
		});

		t3.start();

		Thread t5 = new Thread(new Runnable() {
			public void run() {
				fiacsServerWebSocketServer.run();
			}
		});

		t5.start();


		Thread t4 = new Thread(new Runnable() {
			public void run() {
				Socket socket = null;
				InputStream in = null;;
				OutputStream out = null;
				 BufferedReader br = null;
				 PrintWriter pw = null;
				try {
					ServerSocket serverSocket = new ServerSocket(10007);
					while(true) {
						socket = serverSocket.accept();
						in = socket.getInputStream();
						br = new BufferedReader(new InputStreamReader(in));
						out=socket.getOutputStream();
			            pw=new PrintWriter(out);  
			            
						String info = null;
						while(!((info=br.readLine())==null)){
			                if("ping".equals(info)) {
			                	pw.write("success");
			                	pw.flush();
			                }
			            }
						if(pw!=null) {
							pw.close();
						}
						if(out!=null) {
							out.close();
						}
						if(br!=null) {
							br.close();
						}
						if(in!=null) {
							in.close();
						}
						if(socket!=null) {
							socket.close();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						if(pw!=null) {
							pw.close();
						}
						if(out!=null) {
							out.close();
						}
						if(br!=null) {
							br.close();
						}
						if(in!=null) {
							in.close();
						}
						if(socket!=null) {
							socket.close();
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		t4.start();
		
		Thread.sleep(200);
		log.info("=====================启动成功===========================");
	}

}
