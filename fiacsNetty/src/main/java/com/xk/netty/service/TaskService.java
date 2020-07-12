package com.xk.netty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xk.netty.NettyApplication;
import com.xk.netty.util.LongLinkChannelMap;
import com.xk.netty.util.SuperDogUtil;

@Component
public class TaskService {
	private static Logger log = LoggerFactory.getLogger(TaskService.class);

	@Scheduled(initialDelay = 600000,fixedDelay = 600000)
	public void check() {
		if(!SuperDogUtil.disabled) {
			log.info("======定时检测加密狗==========");
			try {
				if(SuperDogUtil.checkDog()) {
					if(SuperDogUtil.readServerAttribute()) {
						if(!SuperDogUtil.checkDogSqDate()) {
							log.error("=================已过有效期===================");
							System.exit(-1);
						}
						LongLinkChannelMap.maxChnanel=SuperDogUtil.readSqData();
					}else {
						log.error("=============未检测到服务器属性==================");
						System.exit(-1);
					}
				}else {
					log.error("=============未检测到加密狗==================");
					System.exit(-1);
				}
			}catch (Exception e) {
				log.error("=============未检测到加密狗或者加密狗异常==================");
				System.exit(-1);
			}
			
		}
	}
}
