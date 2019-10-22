package com.jyou.thinker.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 功能描述：添加请求拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }

    /**
     * 功能描述：设置项目默认跳转页面为index
     */
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    /**
     *功能描述:添加资源配置，设置静态资源缓存
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/fine/**").addResourceLocations("classpath:/static/fine/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/libs/**").addResourceLocations("classpath:/static/libs/");
        registry.addResourceHandler("/plugins/**").addResourceLocations("classpath:/static/plugins/");
//        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(259200);
//        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/").setCachePeriod(259200);
//        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/").setCachePeriod(259200);
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(259200);
//        registry.addResourceHandler("/libs/**").addResourceLocations("classpath:/static/libs/").setCachePeriod(259200);
//        registry.addResourceHandler("/plugins/**").addResourceLocations("classpath:/static/plugins/").setCachePeriod(259200);
        super.addResourceHandlers(registry);
    }

    @Bean
    @Autowired
    public freemarker.template.Configuration freeMarkerConfig(ServletContext servletContext) throws IOException,
            TemplateException {
        FreeMarkerConfigurer freemarkerConfig = configFreeMarkerConfigurer(servletContext);
        return freemarkerConfig.getConfiguration();
    }

    @Bean
    @Autowired
    public TaglibFactory taglibFactory(ServletContext servletContext) throws IOException, TemplateException {
        FreeMarkerConfigurer freemarkerConfig = configFreeMarkerConfigurer(servletContext);
        TaglibFactory taglibFactory = freemarkerConfig.getTaglibFactory();
        taglibFactory.setObjectWrapper(freemarker.template.Configuration.getDefaultObjectWrapper(freemarker.template.Configuration.getVersion()));
        return taglibFactory;
    }

    @Autowired
    @Bean
    public FreeMarkerConfig springFreeMarkerConfig(ServletContext servletContext) throws IOException, TemplateException {
        return new MyFreeMarkerConfig(freeMarkerConfig(servletContext), taglibFactory(servletContext));
    }

    private static FreeMarkerConfigurer configFreeMarkerConfigurer(ServletContext servletContext) throws IOException,
            TemplateException {
        FreeMarkerConfigurer freemarkerConfig = new FreeMarkerConfigurer();
        freemarkerConfig.setPreTemplateLoaders(new ClassTemplateLoader(WebMvcConfig.class, "/templates/"));
        ServletContext servletContextProxy = (ServletContext) Proxy.newProxyInstance(
                ServletContextResourceHandler.class.getClassLoader(),
                new Class<?>[] { ServletContext.class },
                new ServletContextResourceHandler(servletContext));
        freemarkerConfig.setServletContext(servletContextProxy);
        Properties settings = new Properties();
        settings.put("default_encoding", "UTF-8");
        settings.put("number_format", "0.###");
        settings.put("date_format", "yyyy-MM-dd");
        settings.put("time_format", "HH:mm:ss");
        settings.put("datetime_format", "yyyy-MM-dd HH:mm:ss");
        freemarkerConfig.setFreemarkerSettings(settings);
        freemarkerConfig.afterPropertiesSet();
        return freemarkerConfig;
    }

    @Bean
    public FreeMarkerViewResolver viewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setCache(false);
        viewResolver.setSuffix(".ftl");
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }


    private static class ServletContextResourceHandler implements InvocationHandler {

        private final ServletContext target;

        private ServletContextResourceHandler(ServletContext target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getResourceAsStream".equals(method.getName())) {
                Object result = method.invoke(target, args);
                if (result == null) {
                    result = WebMvcConfig.class.getResourceAsStream((String) args[0]);
                }
                return result;
            } else if ("getResource".equals(method.getName())) {
                Object result = method.invoke(target, args);
                if (result == null) {
                    result = WebMvcConfig.class.getResource((String) args[0]);
                }
                return result;
            }

            return method.invoke(target, args);
        }
    }

    private static class MyFreeMarkerConfig implements FreeMarkerConfig {

        private final freemarker.template.Configuration configuration;
        private final TaglibFactory taglibFactory;

        private MyFreeMarkerConfig(freemarker.template.Configuration configuration, TaglibFactory taglibFactory) {
            this.configuration = configuration;
            this.taglibFactory = taglibFactory;
        }

        @Override
        public freemarker.template.Configuration getConfiguration() {
            return configuration;
        }

        @Override
        public TaglibFactory getTaglibFactory() {
            return taglibFactory;
        }
    }

}
