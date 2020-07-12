package com.thinkgem.jeesite.config;

import com.thinkgem.jeesite.common.servlet.UserfilesDownloadServlet;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.web.CKFinderConnectorServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置系统需要的Servlet组件
 */
@Configuration
public class ServletConfig {

    /**
     * 注册CKFinderConnectorServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean ckfinderRegistrationBean() {

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new CKFinderConnectorServlet());
        registrationBean.addInitParameter("XMLConfig", "classpath:/WEB-INF/ckfinder.xml");
        registrationBean.addInitParameter("debug", "false");
        registrationBean.addInitParameter("configuration", "com.thinkgem.jeesite.common.web.CKFinderConfig");
        registrationBean.setLoadOnStartup(1);
        ArrayList<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/static/ckfinder/core/connector/java/connector.java");
        registrationBean.setUrlMappings(urlPatterns);

        return registrationBean;

    }

    /**
     * 注册
     * @return
     */
    @Bean("userfilesDownloadServlet")
    public ServletRegistrationBean userfilesDownloadServlet() {

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new UserfilesDownloadServlet());

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/userfiles/*");
        registrationBean.setUrlMappings(urlPatterns);

        return registrationBean;
    }

    /**
     * 注册validateCodeServlet
     * @return
     */
    @Bean("validateCodeServlet")
    public ServletRegistrationBean validateCodeServlet() {

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new ValidateCodeServlet());

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/servlet/validateCodeServlet");
        registrationBean.setUrlMappings(urlPatterns);

        return registrationBean;
    }

}
