package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 菜单类型
 * @author wang xiao
 * @date Created in 11:45 2020/9/22
 */
@TableName("t_menu_type")
public class MenuType {

    private String type;

    private String name;

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
}
