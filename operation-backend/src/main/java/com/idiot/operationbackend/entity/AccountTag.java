package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.idiot.operationbackend.support.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * 公众号标签
 * @author wang xiao
 * @date Created in 19:31 2020/9/10
 */
@Valid
@TableName("t_account_tag")
public class AccountTag {

    @TableId
    @NotEmpty(message = "id不允许为空")
    private String id;

    private Integer wxId;

    private String accountId;
    @NotEmpty(message = "名称不能为空")
    private String name;

    private Integer fansCount;

    private String createTime;

    public AccountTag() {
    }

    public AccountTag(Integer wxId, String accountId, String name) {
        this.wxId = wxId;
        this.accountId = accountId;
        this.name = name;
        this.createTime = LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER);
        this.fansCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWxId() {
        return wxId;
    }

    public void setWxId(Integer wxId) {
        this.wxId = wxId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
