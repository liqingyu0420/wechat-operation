package com.idiot.operationbackend.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.ArticleStat;
import com.idiot.operationbackend.mappers.ArticleStatMapper;
import com.idiot.operationbackend.service.facade.ArticleStatService;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 15:40 2020/9/17
 */
@Service
public class ArticleStatServiceImpl  extends ServiceImpl<ArticleStatMapper, ArticleStat>
        implements ArticleStatService {


    @Autowired
    private WeChatService weChatService;


    @Override
    public List<ArticleStat> queryArticleStatByDate(String accountId, String startDate, String endDate) {
        return list(Wrappers.<ArticleStat>lambdaQuery()
                .eq(ArticleStat::getAccountId,accountId)
                .ge(ArticleStat::getStatDate,startDate)
                .le(ArticleStat::getStatDate,endDate));
    }

    @Override
    public void syncArticleStatData(String accountId) {
        String yeDay = LocalDate.now().plusDays(-1).format(Constants.DATE_FORMATTER);
        String jsonStr = weChatService.getArticleSummary(accountId,yeDay,yeDay);
        JSONArray jsonArray = JSONObject.parseObject(jsonStr).getJSONArray("list");
        if (CollectionUtils.isEmpty(jsonArray)) {
            return;
        }
        int size = jsonArray.size();
        JSONObject var = null;
        String msgId = null;
        ArticleStat articleStat = null;
        List<ArticleStat> varArray = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            articleStat = new ArticleStat();
            var = jsonArray.getJSONObject(i);
            msgId = var.getString("msgid");
            ArticleStat temp = queryByAccountIdAndMsgId(accountId,msgId);
            if (Objects.nonNull(temp)){
                articleStat.setId(temp.getId());
            }
            articleStat.setAccountId(accountId);
            articleStat.setMsgId(msgId);
            // 预设值
            articleStat.setAddToFavCount(var.getLong("add_to_fav_count"));
            articleStat.setAddToFavUser(var.getLong("add_to_fav_user"));
            articleStat.setShareCount(var.getLong("share_count"));
            articleStat.setShareUser(var.getLong("share_user"));
            articleStat.setOriPageReadCount(var.getLong("ori_page_read_count"));
            articleStat.setOriPageReadUser(var.getLong("ori_page_read_user"));
            articleStat.setIntPageReadCount(var.getLong("int_page_read_count"));
            articleStat.setIntPageReadUser(var.getLong("int_page_read_user"));
            articleStat.setTargetUser(var.getLong("target_user"));
            articleStat.setTitle(var.getString("title"));
            articleStat.setStatDate(var.getString("ref_date"));
            varArray.add(articleStat);
        }
        saveOrUpdateBatch(varArray);
    }


    @Override
    public ArticleStat queryByAccountIdAndMsgId(String accountId, String msgId) {
        return getOne(Wrappers.<ArticleStat>lambdaQuery()
                .eq(ArticleStat::getAccountId,accountId)
                .eq(ArticleStat::getMsgId,msgId),false);
    }
}
