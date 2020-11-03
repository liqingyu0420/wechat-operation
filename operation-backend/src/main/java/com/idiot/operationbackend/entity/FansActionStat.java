package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.idiot.operationbackend.support.Constants;

import java.time.LocalDateTime;

/**
 * 粉丝动作统计
 * @author wang xiao
 * @date Created in 11:17 2020/9/16
 */
@TableName("t_fans_action_stat")
public class FansActionStat {

    @TableId
    private String id;

    private String accountId;

    private String openId;

    private Integer action;

    private Long createTime;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public FansActionStat() {
    }

    public FansActionStat(String accountId, String openId, Integer action) {
        this.accountId = accountId;
        this.openId = openId;
        this.action = action;
        this.createTime = LocalDateTime.now().toEpochSecond(Constants.DEFAULT_ZONE);
    }
}
