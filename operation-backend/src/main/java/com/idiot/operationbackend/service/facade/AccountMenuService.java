package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.AccountMenu;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 20:37 2020/9/21
 */
public interface AccountMenuService extends IService<AccountMenu> {

    /**
     *  查询公众号菜单
     * @author wangxiao
     * @date 10:44 2020/9/22
     * @param accountId
     * @return java.util.List<com.idiot.operationbackend.entity.AccountMenu>
     */
    List<AccountMenu> queryMenuByAccountId (String accountId);
}
