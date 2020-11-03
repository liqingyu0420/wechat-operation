package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.ArticleStat;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 15:39 2020/9/17
 */
public interface ArticleStatService extends IService<ArticleStat> {

    /**
     *  查询图文统计
     * @author wangxiao
     * @date 15:44 2020/9/17
     * @param accountId
     * @param startDate
     * @param endDate
     * @return java.util.List<com.idiot.operationbackend.entity.ArticleStat>
     */
    List<ArticleStat> queryArticleStatByDate (String accountId,String startDate,String endDate);


    /**
     *  同步微信图文数据
     * @author wangxiao
     * @date 16:13 2020/9/17
     * @param accountId
     * @return void
     */
    void syncArticleStatData (String accountId);


    /**
     *  查询统计信息
     * @author wangxiao
     * @date 16:22 2020/9/17
     * @param accountId
     * @param msgId
     * @return com.idiot.operationbackend.entity.ArticleStat
     */
    ArticleStat queryByAccountIdAndMsgId(String accountId,String msgId);
}
