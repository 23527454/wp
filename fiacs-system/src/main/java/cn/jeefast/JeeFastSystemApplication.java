package cn.jeefast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

import cn.jeefast.modules.fiacs.util.UDPSend;

@SpringBootApplication
@PropertySource(value = {"classpath:config.properties"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class JeeFastSystemApplication extends SpringBootServletInitializer {
	
	private static Logger log = LoggerFactory.getLogger(JeeFastSystemApplication.class);

	
	public static void main(String[] args) throws IOException {
		try {
			SpringApplication.run(JeeFastSystemApplication.class, args);
			log.info("智能通讯服务平台启动成功，端口号："+UDPSend.port);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Socket socket = null;
					InputStream in = null;;
					OutputStream out = null;
					 BufferedReader br = null;
					 PrintWriter pw = null;
					try {
						ServerSocket serverSocket = new ServerSocket(10006);
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
			}).start();

			
		}catch (Exception e) {
			// TODO: handle exception
			if(e.getMessage().contains("mysql")||e.getMessage().contains("sqlserver")) {
				log.error("链接数据库失败");
			//	Runtime.getRuntime().exec("mshta vbscript:msgbox(\"链接数据库失败！\",64,\"失败\")(window.close)");
				System. setProperty("java.awt.headless", "false");
				JOptionPane.showMessageDialog(null, "链接数据库失败", "失败", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
 
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JeeFastSystemApplication.class);
	}
}
