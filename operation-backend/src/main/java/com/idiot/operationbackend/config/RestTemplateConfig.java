package com.idiot.operationbackend.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplateConfig 配置 配置多个区别微服务调用和http 域名调用
 * @author wang xiao
 * @date Created in 17:31 2020/9/11
 */
@Configuration
public class RestTemplateConfig {

    @Bean("rpcRestTemplate")
    public RestTemplate buildRpc (RestTemplateBuilder builder) {
      return   builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

}
