package com.thinkgem.jeesite.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.ckfinder.connector.FileUploadFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
import com.thinkgem.jeesite.common.filter.SessionFilter;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.supcan.treelist.TreeList;
import com.thinkgem.jeesite.common.supcan.treelist.cols.Col;
import com.thinkgem.jeesite.common.supcan.treelist.cols.Group;
import com.thinkgem.jeesite.common.utils.AtmosphereArgumentResolver;
import com.thinkgem.jeesite.modules.sys.interceptor.LogInterceptor;
import com.thinkgem.jeesite.modules.sys.interceptor.MobileInterceptor;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * web应用相关配置
 * 包括过滤器
 */
@Configuration
//@ImportResource(locations= {"classpath*:/WEB-INF/decorators.xml"})
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${web.view.prefix}")
    private String viewPrefix;

    @Value("${web.view.suffix}")
    private String viewSuffix;

    @Value("${adminPath}")
    private String adminPath;

    @Value("${web.view.index}")
    private String indexPage;

    @Value("${web.maxUploadSize}")
    private Long maxUploadSize;


    /**
     * 描述 : <资源访问处理器>. <br>
     * <p>
     * <可以在jsp中使用/static/**的方式访问/WEB-INF/static/下的内容>
     * </p>
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * REST中根据URL后缀自动判定Content-Type及相应的View
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        configurer.mediaTypes(mediaTypes);

        configurer.ignoreAcceptHeader(true);
        configurer.favorPathExtension(true);
    }

    /**
     * 定义视图文件解析（移入application.yml中）
     *
     * @param registry
     */
    /*@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix(viewPrefix);
        viewResolver.setSuffix(viewSuffix);
        registry.viewResolver(viewResolver);
    }*/

    /**
     * 描述 : <异常处理器>.
     * 系统运行时遇到指定的异常将会跳转到指定的页面
     *
     * @return
     */
    @Bean(name = "exceptionResolver")
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "error/403");
        properties.setProperty("java.lang.Throwable", "error/500");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }

    /**
     * 拦截器配置，拦截顺序：先执行后定义的，排在第一位的最后执行
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        LogInterceptor logInterceptor = new LogInterceptor();
        registry.addInterceptor(logInterceptor)
                .addPathPatterns(adminPath + "/**")
                .excludePathPatterns(adminPath + "/")
                .excludePathPatterns(adminPath + "/login")
                .excludePathPatterns(adminPath + "/sys/menu/tree")
                .excludePathPatterns(adminPath + "/sys/menu/treeData")
                .excludePathPatterns(adminPath + "/oa/oaNotify/self/count")
                .excludePathPatterns(adminPath + "/concurrency/*")
                .excludePathPatterns(adminPath + "/servlet/validateCodeServlet");

        // 手机视图拦截器
        MobileInterceptor mobileInterceptor = new MobileInterceptor();
        registry.addInterceptor(mobileInterceptor).addPathPatterns("/**");

        super.addInterceptors(registry);
    }

    /**
     * 定义无Controller的path<->view直接映射
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "redirect:" + indexPage);
        super.addViewControllers(registry);
    }

    /**
     * JSON格式转换工具包
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        JsonMapper jsonMapper = new JsonMapper();
        return jsonMapper;
    }

    /**
     * xml工具包
     *
     * @return
     */
    @Bean
    public Marshaller marshaller() {
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setStreamDriver(new StaxDriver());
        xStreamMarshaller.setAnnotatedClasses(BaseEntity.class, TreeList.class, Col.class, Group.class);
        return xStreamMarshaller;
    }

    /**
     * 参数解析器
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        AtmosphereArgumentResolver atmosphereArgumentResolver = new AtmosphereArgumentResolver();
        argumentResolvers.add(atmosphereArgumentResolver);
        super.addArgumentResolvers(argumentResolvers);
    }

    /**
     * 输出转换器
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 将StringHttpMessageConverter的默认编码设为UTF-8
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        // 将Jackson2HttpMessageConverter的默认格式化输出为false
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        jackson2HttpMessageConverter.setPrettyPrint(false);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper());
        // 使用XML格式输出数据
        mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_XML);
        MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter(marshaller());
        marshallingHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);
        converters.add(marshallingHttpMessageConverter);

        super.configureMessageConverters(converters);
    }


    /**
     * 整合siteMash（项目中好像没用到，暂时不配置）
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean siteMeshFilter(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        SiteMeshFilter siteMeshFilter = new SiteMeshFilter();
        filter.setFilter(siteMeshFilter);
        filter.addUrlPatterns("*", "/f/*");
        filter.setAsyncSupported(true);
        filter.setOrder(4);
        return filter;
    }

    /**
     *  配置 JSR303 Bean Validator 定义（可能是springboot默认配置，不需要再定义）
     * @return
     */
    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

   /* @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSizePerFile(maxUploadSize);
        return multipartResolver;
    }*/

    @Bean
    public FilterRegistrationBean sessionFilter(@Value("${adminPath}") String adminPath) {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        SessionFilter sessionFilter = new SessionFilter();
        sessionFilter.setValidateCodeUrl(adminPath + "/servlet/validateCodeServlet");
        filter.setFilter(sessionFilter);
        filter.addUrlPatterns("/*");
        filter.setOrder(3);
        return filter;
    }

    /**
     * ckfinder  FileUploadFilter配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean FileUploadFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        FileUploadFilter fileUploadFilter = new FileUploadFilter();
        registrationBean.setAsyncSupported(true);
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("sessionCookieName", "JSESSIONID");
        initParameters.put("sessionParameterName", "jsessionid");
        registrationBean.setInitParameters(initParameters);
        registrationBean.setFilter(fileUploadFilter);
        registrationBean.addUrlPatterns("/static/ckfinder/core/connector/java/connector.java");
        registrationBean.setOrder(10);
        return registrationBean;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (ConfigurableEmbeddedServletContainer container) -> {
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/WEB-INF/views/error/404.jsp");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/WEB-INF/views/error/500.jsp");
                container.addErrorPages(error404Page, error500Page);
            };
    }
}

