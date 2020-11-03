package com.idiot.operationbackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 异步线程 但是丢弃任务会使得用户信息同步失败
 * @author wang xiao
 * @date Created in 18:23 2020/9/14
 */
@EnableAsync
@Configuration
public class AsyncPoolConfig {

    private final Logger logger = LoggerFactory.getLogger(AsyncPoolConfig.class);

    @Bean(name = "asyncExecutor")
    public Executor  asyncExecutor () {
        logger.warn("创建异步任务调度线程池-----------------> start");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("asyncExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        logger.warn("创建异步任务调度线程池-----------------> end");
        return executor;
    }

    @Bean(name = "taskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        logger.warn("创建定时任务调度线程池-----------------> start");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(3);
        scheduler.setThreadNamePrefix("taskExecutor--");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setRemoveOnCancelPolicy(true);
        logger.warn("创建定时任务调度线程池----------------->  end");
        return scheduler;
    }
}
