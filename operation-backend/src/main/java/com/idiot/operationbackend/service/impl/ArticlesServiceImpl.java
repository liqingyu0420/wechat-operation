package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.Articles;
import com.idiot.operationbackend.mappers.ArticlesMapper;
import com.idiot.operationbackend.service.facade.ArticlesService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 11:05 2020/9/21
 */
@Service
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles>
        implements ArticlesService {


    @Override
    public Page<Articles> pageArticles(String accountId, int page) {
        Page<Articles> articlesPage = new Page<>(page,8);
        LambdaQueryWrapper<Articles> queryWrapper = Wrappers.<Articles>lambdaQuery()
                .eq(Articles::getAccountId,accountId)
                .orderByDesc(Articles::getCreateTime);
        return page(articlesPage,queryWrapper);
    }


    @Override
    public Boolean addArticles(String wxJson, List<Articles> articles) {
        JSONObject jsonObject = JSON.parseObject(wxJson);
        if (StringUtils.isEmpty(wxJson) || Objects.isNull(articles)){
            return false;
        }
        String mediaId = jsonObject.getString("media_id");
        for (Articles article : articles) {
            article.setMediaId(mediaId);
            article.setRelease(true);
        }
        return saveBatch(articles);
    }

    @Override
    public List<Articles> queryArticlesByIds(String idsStr) {
        if (StringUtils.isEmpty(idsStr)) {
            return null;
        }
        List<String> ids = new ArrayList<>(Arrays.asList(idsStr.split(",")));
        return baseMapper.selectBatchIds(ids);
    }


    @Override
    public Boolean upArticles(String wxJson, List<Articles> articles) {
        JSONObject jsonObject = JSON.parseObject(wxJson);
        if (StringUtils.isEmpty(wxJson) || Objects.isNull(articles)){
            return false;
        }
        String mediaId = jsonObject.getString("media_id");
        for (Articles article : articles) {
            article.setRelease(true);
            article.setMediaId(mediaId);
        }
        return updateBatchById(articles);
    }


    @Override
    public List<Articles> queryByArticlesByMediaId(String mediaId) {
        return list(Wrappers.<Articles>lambdaQuery().eq(Articles::getMediaId,mediaId).eq(Articles::getRelease,false));
    }
}
