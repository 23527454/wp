package com.thinkgem.jeesite.config;

import com.thinkgem.jeesite.common.filter.KickoutSessionControlFilter;
import com.thinkgem.jeesite.common.filter.SysUserFilter;
import com.thinkgem.jeesite.common.security.shiro.session.CacheSessionDAO;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.security.shiro.session.SessionManager;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro的控制类
 * 下面方法的顺序不能乱
 * Created by zhao.weiwei
 * create on 2017/1/11 10:59
 * the email is zhao.weiwei@jyall.com.
 */
@Component
public class ShiroConfig {

    /**
     * 全局的环境变量的设置
     * shiro的拦截
     *
     * @param adminPath
     * @return
     */
    @Bean(name = "shiroFilterChainDefinitions")
    public String shiroFilterChainDefinitions(@Value("${adminPath}") String adminPath) {
        //Global.resolver = new RelaxedPropertyResolver(environment);
        String string = adminPath + "/servlet/validateCodeServlet = anon\n";
        string += "/static/** = anon\n";
        string += "/userfiles/** = anon\n";
        string += "/images/** = anon\n";
        string += adminPath + "/login = authc\n";
        string += adminPath + "/logout = logout\n";
        string += adminPath + "/** = user\n";
        string += "/act/editor/** = user\n";
        string += "/ReportServer/** = user\n";
        string += "/** = user,kickout\n";
        return string;
    }

    @Bean(name = "shiroCacheManager")
    public org.apache.shiro.cache.ehcache.EhCacheManager shiroCacheManager(CacheManager cacheManager) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    /*@Bean
    public org.apache.shiro.cache.CacheManager cacheManager(org.springframework.cache.CacheManager springCacheManager) {
        SpringCacheManagerWrapper springCacheManagerWrapper = new SpringCacheManagerWrapper();
        springCacheManagerWrapper.setCacheManager(springCacheManager);
        return springCacheManager;
    }*/

    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(org.apache.shiro.cache.CacheManager shiroCacheManager,
                                                                   SessionManager sessionManager) {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCacheManager(shiroCacheManager);
        kickoutSessionControlFilter.setSessionManager(sessionManager);
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setLoginUrl("/login?kickout=1");
        kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionControlFilter;
    }

    @Bean
    public SysUserFilter sysUserFilter() {
        return new SysUserFilter();
    }

    @Bean(name = "casFilter")
    public CasFilter casFilter(@Value("${adminPath}") String adminPath) {
        CasFilter casFilter = new CasFilter();
        casFilter.setFailureUrl(adminPath + "/login");
        return casFilter;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(
            @Value("${adminPath}") String adminPath,
            KickoutSessionControlFilter kickoutSessionControlFilter,
            FormAuthenticationFilter formAuthenticationFilter,
            DefaultWebSecurityManager securityManager,
            @Qualifier("shiroFilterChainDefinitions") String shiroFilterChainDefinitions) {
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", formAuthenticationFilter);
//        filters.put("sysUser", sysUserFilter());
        filters.put("kickout", kickoutSessionControlFilter);
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(adminPath + "/login");
        shiroFilterFactoryBean.setSuccessUrl(adminPath + "?login");
        shiroFilterFactoryBean.setFilterChainDefinitions(shiroFilterChainDefinitions);

        return shiroFilterFactoryBean;

    }

    @Bean("sessionDAO")
    public SessionDAO sessionDAO(org.apache.shiro.cache.ehcache.EhCacheManager shiroCacheManager,
                                 SessionIdGenerator idGen) {
        CacheSessionDAO sessionDAO = new CacheSessionDAO();
        sessionDAO.setCacheManager(shiroCacheManager);
        sessionDAO.setSessionIdGenerator(idGen);
        sessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        return sessionDAO;
    }


    @Bean(name = "sessionManager")
    public SessionManager sessionManager(/*@Qualifier("sessionDAO") CacheSessionDAO sessionDAO,*/
            @Value("${session.sessionTimeout}") Long sessionTimeOut,
            @Value("${session.sessionTimeoutClean}") Long sessionValidationInterval) {
        SessionManager sessionManager = new SessionManager();
        /*sessionManager.setSessionDAO(sessionDAO);*/
        sessionManager.setGlobalSessionTimeout(sessionTimeOut);
        sessionManager.setSessionValidationInterval(sessionValidationInterval);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookie(new SimpleCookie("jeesite.session.id"));
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(
            SystemAuthorizingRealm systemAuthorizingRealm,
            SessionManager sessionManager,
            EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager);
        defaultWebSecurityManager.setCacheManager(ehCacheManager);
        defaultWebSecurityManager.setRealm(systemAuthorizingRealm);
        return defaultWebSecurityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(2);
        return filterRegistration;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
