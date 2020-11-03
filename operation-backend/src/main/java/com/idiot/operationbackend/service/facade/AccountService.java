package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.Account;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:56 2020/9/10
 */
public interface AccountService extends IService<Account> {

    /**
     *  根据授权方appId 保存或更新
     * @author wangxiao
     * @date 14:49 2020/9/12
     * @param account account
     * @return int
     */
    boolean saveOrUpdateAccount(Account account);


    /**
     *  更新公众号的userId
     * @author wangxiao
     * @date 17:05 2020/9/12
     * @param accountId accountId
     * @param userId userId
     * @return boolean
     */
    boolean updateUserIdByAccount(String accountId,String userId);



    /**
     *  查询当前账号下的公众号 （不包含子账号）
     * @author wangxiao
     * @date 17:57 2020/9/12
     * @param userId userId
     * @return java.util.List<com.idiot.operationbackend.entity.Account>
     */
    List<Account> queryAccountByUserId(String userId);


    /**
     *  查询一批账号下的公众号 （不包含子账号）
     * @author wangxiao
     * @date 17:59 2020/9/12
     * @param userIds userIds
     * @return java.util.List<com.idiot.operationbackend.entity.Account>
     */
    List<Account> queryAccountBuSubUserIds(List<String> userIds);

    /**
     *  查询所有已经认证成功的公众号
     * @author wangxiao
     * @date 17:43 2020/9/15
     * @return java.util.List<com.idiot.operationbackend.entity.Account>
     */
    List<Account> queryAccount();

    /**
     *  查询公众号
     * @author wangxiao
     * @date 17:19 2020/9/24
     * @param appId appId
     * @return com.idiot.operationbackend.entity.Account
     */
    Account queryByAppId (String appId);


    /**
     *  query  first account name
     * @author wangxiao
     * @date 19:57 2020/10/22
     * @param accountIds accountIds
     * @return java.lang.String
     */
    String queryAccountName(String accountIds);
}
