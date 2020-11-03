package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.Articles;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 11:04 2020/9/21
 */
public interface ArticlesService extends IService<Articles> {

    /**
     *  查询图文
     * @author wangxiao
     * @date 15:24 2020/9/21
     * @param accountId accountId
     * @param page page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.Articles>
     */
    Page<Articles> pageArticles (String accountId,int page);


    /**
     *  新增图文
     * @author wangxiao
     * @date 15:48 2020/9/21
     * @param wxJson wxJson
     * @param articles articles
     * @return Boolean
     */
    Boolean addArticles(String wxJson,List<Articles> articles);


    /**
     *  更新图文
     * @author wangxiao
     * @date 15:48 2020/9/21
     * @param wxJson wxJson
     * @param articles articles
     * @return Boolean
     */
    Boolean upArticles(String wxJson,List<Articles> articles);


    /**
     *  批量查询
     * @author wangxiao
     * @date 16:59 2020/9/21
     * @param idsStr idsStr
     * @return java.util.List<com.idiot.operationbackend.entity.Articles>
     */
    List<Articles> queryArticlesByIds(String idsStr);


    /**
     *
     * @author wangxiao
     * @date 20:03 2020/10/27
     * @param mediaId
     * @return java.util.List<com.idiot.operationbackend.entity.Articles>
     */
    List<Articles> queryByArticlesByMediaId(String mediaId);
}
