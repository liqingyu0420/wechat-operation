package com.idiot.operationbackend;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 微信公众号运营平台
 * @author wangxiao
 * @date 2020-09-09
 */
@EnableScheduling
@SpringBootApplication
@MapperScan("com.idiot.operationbackend.mappers")
public class OperationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationBackendApplication.class, args);
    }

}
