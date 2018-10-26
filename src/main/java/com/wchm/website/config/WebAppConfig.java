package com.wchm.website.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

/**
 * spring boot 配置文件上传大小限制
 */
@Configuration
public class    WebAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大KB,MB
        factory.setMaxFileSize("10MB");
        // 设置总上传数据总大小
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }
}