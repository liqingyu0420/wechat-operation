package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.AccountPush;
import com.idiot.operationbackend.mappers.AccountPushMapper;
import com.idiot.operationbackend.service.facade.AccountPushService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:22 2020/9/18
 */
@Service
public class AccountPushServiceImpl extends ServiceImpl<AccountPushMapper, AccountPush>
        implements AccountPushService {


    @Override
    public List<AccountPush> queryAccountPush(List<String> accountIds) {
        return list(Wrappers.<AccountPush>lambdaQuery().in(AccountPush::getAccountId,accountIds));
    }


    @Override
    public AccountPush queryAccountPush(String accountId) {
        return getOne(Wrappers.<AccountPush>lambdaQuery()
                .eq(AccountPush::getAccountId,accountId)
                .eq(AccountPush::getEnable,1),false);
    }

    @Override
    public boolean upEnable(String id, boolean var) {
        LambdaUpdateWrapper<AccountPush> wrapper = Wrappers.<AccountPush>lambdaUpdate();
        wrapper.set(AccountPush::getEnable,var).eq(AccountPush::getId,id);
        return update(wrapper);
    }
}
