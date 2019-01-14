package com.hxlc.backstageapp.config;

import com.hxlc.backstageapp.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.MultipartConfigElement;
import java.io.File;


@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Value("${fileDir.projectFile}")
    private String projectFile;
    @Value("${fileDir.disLicense}")
    private String disLicense;
    @Value("${fileDir.appNews}")
    private String appNews;
    @Value("${fileDir.tempFile}")
    private String tempFile;

    /**
     * 不需要登录拦截的url:登录注册和验证码
     */
    final String[] notLoginInterceptPaths = {"/userLogin","/salesLogin","/project/**","/user/**","/news/**"};//"/", "/login/**", "/person/**", "/register/**", "/validcode", "/captchaCheck", "/file/**", "/contract/htmltopdf", "/questions/**", "/payLog/**", "/error/**" };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器
        //registry.addInterceptor(logInterceptor).addPathPatterns("/**");
        // 登录拦截器
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(notLoginInterceptPaths);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login.html");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/privacy_policy").setViewName("privacy_policy.html");
        super.addViewControllers(registry);
    }

    /**
     * 项目文件路径地址
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectFilePre = projectFile.substring(projectFile.lastIndexOf("/") + 1, projectFile.length());
        String disLicensePre = disLicense.substring(disLicense.lastIndexOf("/") + 1, disLicense.length());
        String appNewsPre = appNews.substring(appNews.lastIndexOf("/") + 1, appNews.length());
        registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/" + projectFilePre + "/**").addResourceLocations("file:" + projectFile + "/");
        registry.addResourceHandler("/" + disLicensePre + "/**").addResourceLocations("file:" + disLicense + "/");
        registry.addResourceHandler("/" + appNewsPre + "/**").addResourceLocations("file:" + appNews + "/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    /**
     * 文件上传临时路径
     *
     * @return
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        File file = new File(tempFile);
        if (!file.exists()){
            file.mkdirs();
        }
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(tempFile);
        return factory.createMultipartConfig();
    }
}
