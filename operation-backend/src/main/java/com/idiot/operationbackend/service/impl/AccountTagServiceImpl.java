package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.AccountTag;
import com.idiot.operationbackend.mappers.AccountTagMapper;
import com.idiot.operationbackend.service.facade.AccountTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author wang xiao
 */

@Service
public class AccountTagServiceImpl  extends ServiceImpl<AccountTagMapper,AccountTag>
        implements AccountTagService {

    private final Logger logger = LoggerFactory.getLogger(AccountTagServiceImpl.class);


    @Override
    public boolean overlayAccountTag(String accountId, List<AccountTag> newTags) {
        logger.info("同步粉丝标签 保存/删除数据库 --------start");
        List<AccountTag> preData = queryAccountTag(accountId);
        List<AccountTag> addTag = newTags.stream().filter(e-> have(preData, e.getWxId())).collect(Collectors.toList());
        List<AccountTag> delTag = preData.stream().filter(e-> have(newTags, e.getWxId())).collect(Collectors.toList());
        boolean ifAdd = addAccountTag(addTag);
        boolean ifDel = delAccountTag(delTag);
        logger.info("同步粉丝标签 保存/删除数据库 ---------end,add 结果：{},del 结果：{}",ifAdd,ifDel);
        return ifAdd & ifDel;
    }


    @Override
    public List<AccountTag> queryAccountTag(String accountId) {
        return list(Wrappers.<AccountTag>lambdaQuery().eq(AccountTag::getAccountId,accountId));
    }


    @Override
    public boolean addAccountTag(List<AccountTag> accountTags) {
        if (CollectionUtils.isEmpty(accountTags)) {
            return false;
        }
        return saveBatch(accountTags);
    }

    @Override
    public boolean delAccountTag(List<AccountTag> accountTags) {
        if (CollectionUtils.isEmpty(accountTags)) {
            return false;
        }
        List<String> ids = accountTags.stream().map(AccountTag::getId).collect(Collectors.toList());
        return removeByIds(ids);
    }

    private boolean have(List<AccountTag> accountTags, Integer wxId) {
        if (CollectionUtils.isEmpty(accountTags)) {
            return true;
        }
        return accountTags.stream().noneMatch(e -> wxId.equals(e.getWxId()));
    }

    @Override
    public List<AccountTag> getTagByAccountId(String accountId) {
        return list(Wrappers.<AccountTag>lambdaQuery().eq(AccountTag::getAccountId,accountId));
    }

    @Override
    public boolean addTag(String label, String accountId, int wxId) {
        return save(new AccountTag(wxId,accountId,label));
    }


    @Override
    public boolean removeTag(String accountId, int wxId) {
        return remove(Wrappers.<AccountTag>lambdaQuery()
                .eq(AccountTag::getAccountId,accountId).eq(AccountTag::getWxId,wxId));
    }

    @Override
    public AccountTag getAccountTag(String accountId, int wxId) {
        return getOne(Wrappers.<AccountTag>lambdaQuery()
                .eq(AccountTag::getAccountId,accountId)
                .eq(AccountTag::getWxId,wxId),false);
    }

    @Override
    public boolean addTagSize(String accountId, int wxId, int size) {

        AccountTag accountTag = getAccountTag(accountId,wxId);
        if (null == accountTag){
            return false;
        }
        accountTag.setFansCount(accountTag.getFansCount()+size);
        return updateById(accountTag);
    }

    @Override
    public boolean subTagSize(String accountId, int wxId, int size) {
        AccountTag accountTag = getAccountTag(accountId,wxId);
        if (null == accountTag){
            return false;
        }
        int var =accountTag.getFansCount()-size;
        accountTag.setFansCount(Math.max(var, 0));
        return updateById(accountTag);
    }


    @Override
    public List<String> queryTagName(String tagId) {

        if (StringUtils.isBlank(tagId)) {
            return new ArrayList<>();
        }
        List<String> ids =JSONArray.parseArray(tagId,String.class);
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<AccountTag> accountTags = list(Wrappers.<AccountTag>lambdaQuery().in(AccountTag::getId,ids));
        if (CollectionUtils.isNotEmpty(accountTags)) {
           return accountTags.stream().map(AccountTag::getName).collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }

    }
}
