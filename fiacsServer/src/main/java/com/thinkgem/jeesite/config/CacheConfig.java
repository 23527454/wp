package com.thinkgem.jeesite.config;

import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * 配置ehcache缓存
 */
@Configuration
public class CacheConfig {

	@Value("${ehcache.configFile}")
	private String cacheFilePath;

	/**
	 * 这个配置好像是没用到的与原项目相比
	 * @param bean
	 * @return
	 */
	/*@Bean(name = "ehcacheManager")
	public CacheManager cacheManager(EhCacheManagerFactoryBean bean) {
		return bean.getObject();
	}*/

	@Bean
	public org.springframework.cache.CacheManager springCacheManager() {
		EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
		ehCacheCacheManager.setCacheManager(ehCacheManager(ehCacheManagerFactoryBean()));
		return ehCacheCacheManager;
	}

	@Primary
	@Bean(name = "ehCacheManager")
	public CacheManager ehCacheManager(EhCacheManagerFactoryBean bean) {
		return bean.getObject();
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		cacheManagerFactoryBean.setConfigLocation(new ClassPathResource(cacheFilePath));
		cacheManagerFactoryBean.setShared(true);
		// 设置完属性后，cacheManagerFactoryBean会执行afterProertiesSet()方法，
		// 所以不能在这里直接执行cacheManagerFactoryBean.getObject(),直接执行的话，因为在afterPropertiesSet()方法之前执行，所以：getObject()会得到null值
		return cacheManagerFactoryBean;
	}
}