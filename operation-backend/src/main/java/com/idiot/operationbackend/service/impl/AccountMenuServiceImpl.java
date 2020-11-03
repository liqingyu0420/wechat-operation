package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.AccountMenu;
import com.idiot.operationbackend.mappers.AccountMenuMapper;
import com.idiot.operationbackend.service.facade.AccountMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wang xiao
 * @date Created in 20:37 2020/9/21
 */
@Service
public class AccountMenuServiceImpl extends ServiceImpl<AccountMenuMapper, AccountMenu>
        implements AccountMenuService {


    @Override
    public List<AccountMenu> queryMenuByAccountId(String accountId) {
        List<AccountMenu> accountMenus = list(Wrappers.<AccountMenu>lambdaQuery()
                .eq(AccountMenu::getAccountId,accountId)
                .orderByAsc(AccountMenu::getId));
        if (CollectionUtils.isEmpty(accountMenus)) {
            return accountMenus;
        }
        accountMenus = findMenu(accountMenus);
        return accountMenus;
    }

    /**
     *  微信 菜单子层级查询 只有两级 不需要递归
     * @author wangxiao
     * @date 10:54 2020/9/22
     * @param menuList menuList
     * @return java.util.List<com.idiot.operationbackend.entity.AccountMenu>
     */
    private List<AccountMenu> findMenu (List<AccountMenu> menuList){
        List<AccountMenu> topMenus = menuList.stream()
                .filter(e-> Objects.isNull(e.getParentId()))
                .collect(Collectors.toList());
        for (AccountMenu topMenu : topMenus) {
            List<AccountMenu> subMenu = menuList.stream()
                    .filter(e->topMenu.getId().equals(e.getParentId()))
                    .collect(Collectors.toList());
            topMenu.setSubMenu(subMenu);
        }
        return topMenus;
    }
}
