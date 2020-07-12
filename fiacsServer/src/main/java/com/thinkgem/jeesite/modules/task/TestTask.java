package com.thinkgem.jeesite.modules.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.modules.sys.utils.SuperDogUtil;

@Lazy(false)
@Service
public class TestTask {
	
	private static Logger log = LoggerFactory.getLogger(TestTask.class);
	
	@Scheduled(initialDelay = 600000,fixedDelay = 600000)
	public void test() {
		log.info("==============定时加密狗检测================");
		if (SuperDogUtil.ReadSqData() && SuperDogUtil.CheckDogSqDate()) {
			//SuperDogUtil.cacheConfineNumber();
		}else {
			System.exit(-1);
		}
	}
}
