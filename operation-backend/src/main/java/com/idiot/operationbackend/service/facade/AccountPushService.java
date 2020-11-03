package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.AccountPush;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:21 2020/9/18
 */
public interface AccountPushService extends IService<AccountPush> {

    /**
     *  查询公告号推送
     * @author wangxiao
     * @date 17:31 2020/9/18
     * @param accountIds accountIds
     * @return java.util.List<com.idiot.operationbackend.entity.AccountPush>
     */
    List<AccountPush> queryAccountPush(List<String> accountIds);



    /**
     *  查询智能推送 （理论上只有一条）
     * @author wangxiao
     * @date 19:55 2020/9/24
     * @param accountId accountId
     * @return com.idiot.operationbackend.entity.AccountPush
     */
    AccountPush queryAccountPush (String accountId);


    /**
     * 更改开关
     * @author wangxiao
     * @date 17:10 2020/10/21
     * @param id id
     * @param var var
     * @return boolean
     */
    boolean upEnable (String id, boolean var);
}
