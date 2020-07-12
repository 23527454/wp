package cn.jeefast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import cn.jeefast.modules.equipment.util.UDPUtil;

@SpringBootApplication
public class JeeFastSystemApplication extends SpringBootServletInitializer {
	
	private static Logger log = LoggerFactory.getLogger(JeeFastSystemApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(JeeFastSystemApplication.class, args);
		log.info("znzj_commserver启动成功，端口号："+UDPUtil.port);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JeeFastSystemApplication.class);
	}
	
	
}
