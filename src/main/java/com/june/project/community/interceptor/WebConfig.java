package com.june.project.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author June
 * @date 2020/7/2 - 13:16
 * 实现自定义拦截器只需要3步
 * 1、创建我们自己的拦截器类并实现 HandlerInterceptor 接口。
 * 2、创建一个Java类实现WebMvcConfigurer，并重写 addInterceptors 方法。
 * 3、实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）。
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
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**"); // 我们希望所有地址都经过拦截器处理
    }
}
