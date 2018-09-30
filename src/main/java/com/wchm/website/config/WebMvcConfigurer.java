package com.wchm.website.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Value("${wchm.update-image-relative}")
    private String relative;

    @Value("${wchm.update-image-absolutely}")
    private String absolutely;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        /**
//         * @Description: 对文件的路径进行配置, 创建一个虚拟路径/templates/** ，即只要在<img src="/templates/picName.jpg" />便可以直接引用图片
//         *这是图片的物理路径  "file:/+本地图片的地址"
//         */
//        registry.addResourceHandler("/templates/**").addResourceLocations
//                ("file:/E:/IdeaProjects/gaygserver/src/main/resources/static/");
////        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/static/");
//        super.addResourceHandlers(registry);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        // 上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8081/OTA/d3cf0281-bb7f-40e0-ab77-406db95ccf2c.jpg
        // 其中OTA表示访问的前缀。"file:D:/OTA/"是文件真实的存储路径
        registry.addResourceHandler(relative + "**").addResourceLocations("file:" + absolutely);
    }
}