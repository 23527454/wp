package com.thinkgem.jeesite.config;


import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.thinkgem.jeesite.common.persistence.BaseEntity;

/**
 * 数据库配置
 * 1、数据源
 * 2、事务管理器
 * 3、sqlSessionFactory
 */
@Configuration
public class DataSourceConfig {

    // 数据源驱动类可不写，Druid默认会自动根据URL识别DriverClass
    @Value("${jdbc.driver}")
    private String driverClassName;

    // 基本属性 url、user、password
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    // 配置初始化大小、最小、最大
    @Value("${jdbc.pool.init}")
    private int initialSize;
    @Value("${jdbc.pool.minIdle}")
    private int minIdle;
    @Value("${jdbc.pool.maxActive}")
    private int maxActive;

    @Value("${jdbc.testSql}")
    private String testSql;

    /**
     * 数据源（连接池）
     */
    @Bean
    public DataSource dataSource() {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);

        dataSource.setValidationQuery(testSql);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000L);
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000L);

        try {
            dataSource.setFilters("stat");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource;
    }

    /**
     * 事务管事配置
     *
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * SqlSessionFactory配置
     *
     * @param dataSource
     * @return
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        VFS.addImplClass(SpringBootVFS.class);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.thinkgem.jeesite");
        sqlSessionFactoryBean.setTypeAliasesSuperType(BaseEntity.class);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:/mappings/**/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }
}
