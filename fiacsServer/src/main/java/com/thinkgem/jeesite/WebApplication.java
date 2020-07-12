package com.thinkgem.jeesite;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** 注解扫描路径配置 */
@SpringBootApplication(scanBasePackages = {"com.thinkgem.jeesite"})
@ComponentScan(lazyInit = true)
/** 加载配置文件 */
@PropertySource(value = {"classpath:config.properties"})
/** 开启注解事务 */
@EnableTransactionManagement(proxyTargetClass = true)
/** 开启代理 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ServletComponentScan("com.thinkgem.jeesite")
public class WebApplication extends SpringBootServletInitializer{
	
	private static Logger log = LoggerFactory.getLogger(WebApplication.class);

	public static void main(String[] args) {
		System. setProperty("java.awt.headless", "false");
		/*if (!SystemService.printKeyLoadMessage()){
			JOptionPane.showMessageDialog(null, "加密狗使用失败，详情请看日志", "失败", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}*/

		log.info("==============启动中===============");
		SpringApplication.run(WebApplication.class, args);

		/*Process process = null;
		BufferedReader reader =null;
		boolean su = false;
		try {
			System.out.println(ConstantUtil.databaseUsername+":"+ConstantUtil.databasePassword);
			ProcessBuilder p = new ProcessBuilder("mysqladmin", "-u" + ConstantUtil.databaseUsername, "-p" + ConstantUtil.databasePassword,
					"-P3306", "ping");
			p.redirectErrorStream(true);
			process = p.start();
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains("alive")) {
					System.out.println(line);
					su = true;
				}
			}
			if(su) {
				System.out.println("===========lianjie successs=========]");
			}else {
				JOptionPane.showMessageDialog(null, "数据库连接失败", "失败", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		} catch (IOException e1) {

		}finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(process!=null) {
				process.destroy();
			}
		}*/

		{
			new Thread(new Runnable() {
				public void run() {
					Socket socket = null;
					InputStream in = null;;
					OutputStream out = null;
					BufferedReader br = null;
					PrintWriter pw = null;
					try {
						ServerSocket serverSocket = new ServerSocket(10008);
						while(true) {
							socket = serverSocket.accept();
							in = socket.getInputStream();
							br = new BufferedReader(new InputStreamReader(in));

							out=socket.getOutputStream();
							pw=new PrintWriter(out);

							String info = null;
							while(!((info=br.readLine())==null)){
								System.out.println(info);
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
			});//.start();
			//new SpringApplicationBuilder(WebApplication.class).web(false).run(args);
			log.info("==============启动成功===============");
			System.out.println("=============================================启动成功=================================================================");
		}
			
	}
	
	
	/*@Override
	public void run(String... arg0) throws Exception */
}

