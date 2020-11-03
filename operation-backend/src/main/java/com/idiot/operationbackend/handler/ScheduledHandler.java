package com.idiot.operationbackend.handler;


import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.service.facade.AccountStatService;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.ArticleStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:31 2020/9/15
 */
@Component
public class ScheduledHandler {


    @Autowired
    private AccountStatService fansStatService;


    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleStatService articleStatService;

    private final Logger logger = LoggerFactory.getLogger(ScheduledHandler.class);

    /**
     *  请求微信 同步用户统计数据
     * @author wangxiao
     * @date 12:09 2020/9/17
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void  scheduledAccount () {
        logger.info("开始同步微信用户分析数据----->start,时间:{}", LocalDateTime.now().toString());
        List<Account> accounts = accountService.queryAccount();
        if (null == accounts || accounts.isEmpty()) {
            logger.info("开始同步微信用户分析数据----->认证公众号信息为空,时间:{}", LocalDateTime.now().toString());
            return;
        }
        for (Account account : accounts) {
            logger.info("同步微信公众号:{}用户分析数据----->,时间:{}", account.getId(),LocalDateTime.now().toString());
            try {
                fansStatService.statAccountFansData(account.getId());
            }catch (Exception e){
                logger.info("同步微信公众号:{}用户分析数据----->,时间:{} is error ", account.getId(),LocalDateTime.now().toString());
            }

        }
        logger.info("开始同步微信用户分析数据----->end,时间:{}", LocalDateTime.now().toString());
    }

    /**
     *  请求微信 同步图文统计信息
     * @author wangxiao
     * @date 12:09 2020/9/17
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void  scheduledArticle () {
        logger.info("开始同步微信图文分析数据----->start,时间:{}", LocalDateTime.now().toString());
        List<Account> accounts = accountService.queryAccount();
        if (null == accounts || accounts.isEmpty()) {
            logger.info("开始同步微信图文分析数据----->认证公众号信息为空,时间:{}", LocalDateTime.now().toString());
            return;
        }
        for (Account account : accounts) {
            logger.info("同步微信公众号:{}用户分析数据----->,时间:{}", account.getId(),LocalDateTime.now().toString());
            try {
                articleStatService.syncArticleStatData(account.getId());
            }catch (Exception e){
                logger.info("同步微信公众号:{}用户分析数据----->,时间:{} is error", account.getId(),LocalDateTime.now().toString());
            }

        }
        logger.info("开始同步微信用户分析数据----->end,时间:{}", LocalDateTime.now().toString());
    }

}
