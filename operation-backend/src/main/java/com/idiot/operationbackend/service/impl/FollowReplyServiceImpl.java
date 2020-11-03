package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.FollowReply;
import com.idiot.operationbackend.mappers.FollowReplyMapper;
import com.idiot.operationbackend.service.facade.FollowReplyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:36 2020/9/21
 */
@Service
public class FollowReplyServiceImpl extends ServiceImpl<FollowReplyMapper, FollowReply>
        implements FollowReplyService {

    @Override
    public List<FollowReply> queryFollowReply(List<String> ids) {
        return list(Wrappers.<FollowReply>lambdaQuery().in(FollowReply::getAccountId,ids));
    }

    @Override
    public FollowReply queryFollowReplyByAccountId(String accountId) {
        return getOne(Wrappers.<FollowReply>lambdaQuery()
                .eq(FollowReply::getEnable,"1")
                .eq(FollowReply::getAccountId,accountId),false);
    }


    @Override
    public boolean upEnable(String id, Boolean val) {
        LambdaUpdateWrapper<FollowReply> updateWrapper = Wrappers.<FollowReply>lambdaUpdate();
        updateWrapper.set(FollowReply::getEnable,val);
        updateWrapper.eq(FollowReply::getId,id);
        return update(updateWrapper);
    }
}
