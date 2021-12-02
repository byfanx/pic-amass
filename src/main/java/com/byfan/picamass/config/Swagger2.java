package com.byfan.picamass.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/18 22:06
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.byfan.picamass.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Lists.newArrayList(apiKey()));
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("picamass接口文档")
                .description("https://byfan.xyz")
                .termsOfServiceUrl("http://127.0.0.1:9000")
                .version("1.0")
                .build();
    }

    private ApiKey apiKey(){
        return new ApiKey("Authorization","Authorization","header");
    }
}
