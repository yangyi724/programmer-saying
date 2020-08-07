package com.june.project.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author June
 * @date 2020/7/2 - 13:16
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    /*
    * 添加Spring MVC生命周期拦截器，用于控制器方法调用的预处理和后处理。
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有地址都经过拦截器处理
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }
}
