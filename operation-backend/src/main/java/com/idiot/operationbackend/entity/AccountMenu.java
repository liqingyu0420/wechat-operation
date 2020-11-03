package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 工作号 菜单
 * @author wang xiao
 * @date Created in 20:24 2020/9/21
 */
@TableName("t_account_menu")
public class AccountMenu {

    @TableId
    private String id;

    private String accountId;

    private String type;

    private String name;

    private String key;

    private String parentId;

    private String param1;

    private String param2;

    @TableField(exist = false)
    private List<AccountMenu> subMenu;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<AccountMenu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<AccountMenu> subMenu) {
        this.subMenu = subMenu;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
