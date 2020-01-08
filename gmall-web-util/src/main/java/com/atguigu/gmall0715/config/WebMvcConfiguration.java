package com.atguigu.gmall0715.config;

import com.atguigu.gmall0715.intercepter.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author sujie
 * @date 2020-01-07-23:27
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    /**
        springmvc.mxl、
            // 表示拦截所有
            <mvc:interceptors>
                <bean class="com.atguigu.gmall0715.config.AuthInterceptor">
            </mvc:interceptors>

             <mvc:interceptors>
                 // 表示拦截所有
                 // <bean class="com.atguigu.gmall0715.config.AuthInterceptor">
                    <mvc: interceptor>
                          <mvc:mapping path="/**">
                          <bean class="com.atguigu.gmall0715.config.AuthInterceptor">
                    </mvc: interceptor>
            </mvc:interceptors>
 */
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
