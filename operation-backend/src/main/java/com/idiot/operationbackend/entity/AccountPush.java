package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 公众号推送
 * @author wang xiao
 * @date Created in 16:45 2020/9/18
 */
@TableName("t_account_push")
public class AccountPush {

    @TableId
    private String id;

    private String accountId;

    private String nikeName;

    private String headImage;

    private Integer pushType;

    private String pushTimer;

    private String quiet;

    private String pushTrigger;

    private Integer pushLimit;

    private Boolean enable;

    private String content;

    private String createTime;

    @TableField(exist = false)
    private boolean setUp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getPushTimer() {
        return pushTimer;
    }

    public void setPushTimer(String pushTimer) {
        this.pushTimer = pushTimer;
    }

    public String getQuiet() {
        return quiet;
    }

    public void setQuiet(String quiet) {
        this.quiet = quiet;
    }

    public String getPushTrigger() {
        return pushTrigger;
    }

    public void setPushTrigger(String pushTrigger) {
        this.pushTrigger = pushTrigger;
    }

    public Integer getPushLimit() {
        return pushLimit;
    }

    public void setPushLimit(Integer pushLimit) {
        this.pushLimit = pushLimit;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isSetUp() {
        return setUp;
    }

    public void setSetUp(boolean setUp) {
        this.setUp = setUp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AccountPush{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", pushType=" + pushType +
                ", pushTimer='" + pushTimer + '\'' +
                ", quiet='" + quiet + '\'' +
                ", trigger=" + pushTrigger +
                ", pushLimit=" + pushLimit +
                ", enable=" + enable +
                ", contents='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", setUp=" + setUp +
                '}';
    }
}
