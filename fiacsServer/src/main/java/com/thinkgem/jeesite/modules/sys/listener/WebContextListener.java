package com.thinkgem.jeesite.modules.sys.listener;

import com.thinkgem.jeesite.modules.sys.service.SystemService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebContextListener implements ServletContextListener {


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 判断机器是否有加密狗
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
