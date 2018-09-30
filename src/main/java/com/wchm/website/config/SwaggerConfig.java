package com.wchm.website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
@Profile({"default", "test"})
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wchm.website.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(headerToken());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("万呈海盟官网系统接口")
                .description("swagger")
                .termsOfServiceUrl("http://www.website.com")
                .version("1.0.0")
                .build();
    }

    private List<Parameter> headerToken() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        // 设置请求头部需要token参数
        parameterBuilder.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> parameters = new ArrayList<>();
//        parameters.add(parameterBuilder.build());
        return parameters;
    }
}
