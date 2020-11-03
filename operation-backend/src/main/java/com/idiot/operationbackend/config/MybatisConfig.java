package com.idiot.operationbackend.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用mybatisplus-starter，
 * 当MybatisPlusAutoConfiguration生效时会添加分页interceptors
 * （前提DataSourceAutoConfiguration生效）
 *  ------------------------------------但是为啥有的方法可以分页有的不行------------------------------
 * @author wang xiao
 * @date Created in 14:42 2020/10/22
 */
@Configuration
public class MybatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


}
