package com.idiot.operationbackend.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfiguration
 * @author wang xiao
 * @date Created in 14:19 2020/9/10
 */
@Configuration
@EnableKnife4j
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("backend")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.idiot.operationbackend.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("微信公众号运营平台 RESTFUL APIs")
                .description("weChat-Operation 微信公众号深度运营管理平台，提升用户体验感！")
                .termsOfServiceUrl("http://127.0.0.1:38090/wxoperate")
                .contact(new Contact("wang xiao","www.github.com/wangxiao1002","Eo_xiao@163.com"))
                .version("1.0")
                .build();
    }
}
