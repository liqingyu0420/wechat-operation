package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.AuthUser;
import com.idiot.operationbackend.mappers.AccountMapper;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.AuthUserService;
import com.idiot.operationbackend.support.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wang xiao
 * @date Created in 17:56 2020/9/10
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
        implements AccountService {


    @Autowired
    private AuthUserService userService;

    @Override
    public boolean saveOrUpdateAccount(Account account) {
        Account temp = getOne(Wrappers.<Account>lambdaQuery().eq(Account::getAuthorizerAppId,account.getAuthorizerAppId()));
        if (Objects.isNull(temp)) {
            return save(account);
        }else {
            account.setId(temp.getId());
            return saveOrUpdate(account);
        }
    }


    @Override
    public boolean updateUserIdByAccount(String accountId, String userId) {
        return update(Wrappers.<Account>lambdaUpdate()
                .set(Account::getCreateUserId,userId)
	.set(Account::getState,1)
                .eq(Account::getId,accountId));
    }


    @Override
    public List<Account> queryAccountByUserId(String userId) {

        List<AuthUser> authUsers =  userService.querySubAuthUser(userId);
        List<String> userIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authUsers)){
            userIds =authUsers.stream().map(AuthUser::getId).collect(Collectors.toList());
        }
        userIds.add(userId);
        /**
         * fixme 暂定权限是 用户能够查询所属子账号下所有公众号，即无权限
         */
        List<Account> accounts = queryAccountBuSubUserIds(userIds);
        if (CollectionUtils.isEmpty(accounts)){
            throw new CustomException(501,"当前帐号和子账号下没有认证的公众号，请前往公众号管理中心添加认证公众号");
        }
       return  accounts;
    }


    @Override
    public List<Account> queryAccountBuSubUserIds(List<String> userIds) {
        return list(Wrappers.<Account>lambdaQuery().in(Account::getCreateUserId,userIds));
    }

    @Override
    public List<Account> queryAccount() {
        return list(Wrappers.<Account>lambdaQuery().eq(Account::getState,"1"));
    }

    @Override
    public Account queryByAppId(String appId) {
        return getOne(Wrappers.<Account>lambdaQuery().eq(Account::getAuthorizerAppId,appId),false);
    }


    @Override
    public String queryAccountName(String accountIds) {
        String id = accountIds.split(",")[0];
        return getById(id).getNickName();
    }
}
