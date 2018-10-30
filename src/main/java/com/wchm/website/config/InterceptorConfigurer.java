package com.wchm.website.config;

import com.wchm.website.interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 */

@Configuration
public class InterceptorConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        // 配置拦截路径，和不拦截路径
        registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/login/**");

    }
}