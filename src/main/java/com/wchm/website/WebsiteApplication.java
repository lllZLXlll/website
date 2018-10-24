package com.wchm.website;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wchm.website.mapper")
@EnableEncryptableProperties
@EnableScheduling
public class WebsiteApplication {
    private final static Logger log = LoggerFactory.getLogger(WebsiteApplication.class);

    public static void main(String[] args) {
        log.info("-------------- websiteApplication start ... --------------");
        SpringApplication.run(WebsiteApplication.class, args);
        log.info("-------------- websiteApplication start success--------------");
    }
}
