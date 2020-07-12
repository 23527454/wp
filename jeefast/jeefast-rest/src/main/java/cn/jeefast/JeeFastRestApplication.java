package cn.jeefast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import cn.jeefast.rest.util.UDPUtil;

@SpringBootApplication
public class JeeFastRestApplication extends SpringBootServletInitializer {
	
	private static Logger log = LoggerFactory.getLogger(JeeFastRestApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(JeeFastRestApplication.class, args);
		log.info("znzj_api服务启动成功，端口号："+UDPUtil.port);
	} 

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JeeFastRestApplication.class);
	}
}
