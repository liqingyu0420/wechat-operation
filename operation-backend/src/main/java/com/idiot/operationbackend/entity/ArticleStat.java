package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 图文阅读统计
 * @author wang xiao
 * @date Created in 15:32 2020/9/17
 */
@TableName("t_article_stat")
public class ArticleStat {

    @TableId
    private String id;

    private String accountId;

    private String statDate;

    private String msgId;

    private String title;

    private Long intPageReadUser;

    private Long intPageReadCount;

    private Long oriPageReadUser;

    private Long oriPageReadCount;

    private Long shareUser;

    private Long shareCount;

    private Long addToFavUser;

    private Long addToFavCount;

    private Long targetUser;

    private String createTime;



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

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getIntPageReadUser() {
        return intPageReadUser;
    }

    public void setIntPageReadUser(Long intPageReadUser) {
        this.intPageReadUser = intPageReadUser;
    }

    public Long getIntPageReadCount() {
        return intPageReadCount;
    }

    public void setIntPageReadCount(Long intPageReadCount) {
        this.intPageReadCount = intPageReadCount;
    }

    public Long getOriPageReadUser() {
        return oriPageReadUser;
    }

    public void setOriPageReadUser(Long oriPageReadUser) {
        this.oriPageReadUser = oriPageReadUser;
    }

    public Long getOriPageReadCount() {
        return oriPageReadCount;
    }

    public void setOriPageReadCount(Long oriPageReadCount) {
        this.oriPageReadCount = oriPageReadCount;
    }

    public Long getShareUser() {
        return shareUser;
    }

    public void setShareUser(Long shareUser) {
        this.shareUser = shareUser;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getAddToFavUser() {
        return addToFavUser;
    }

    public void setAddToFavUser(Long addToFavUser) {
        this.addToFavUser = addToFavUser;
    }

    public Long getAddToFavCount() {
        return addToFavCount;
    }

    public void setAddToFavCount(Long addToFavCount) {
        this.addToFavCount = addToFavCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Long targetUser) {
        this.targetUser = targetUser;
    }
}
