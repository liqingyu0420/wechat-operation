package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 关注回复
 * @author wang xiao
 * @date Created in 17:12 2020/9/21
 */
@TableName("t_account_follow_reply")
public class FollowReply {

    @TableId
    private String id;

    private String accountId;

    private String nikeName;

    private String headImage;

    private Integer pushType;

    private Boolean enable;

    private String content;

    private String createTime;

    @TableField(exist = false)
    private Boolean setUp;

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

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
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

    public Boolean getSetUp() {
        return setUp;
    }

    public void setSetUp(Boolean setUp) {
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
        return "FollowReply{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", pushType=" + pushType +
                ", enable=" + enable +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", setUp=" + setUp +
                '}';
    }
}
