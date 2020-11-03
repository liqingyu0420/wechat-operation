package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 客服消息
 * @author wang xiao
 * @date Created in 13:55 2020/9/22
 */
@TableName("t_customer_msg")
public class CustomerMsg {

    @TableId
    private String id;

    @NotEmpty(message = "请选择客服消息公众号")
    private String accountId;

    @NotEmpty(message = "请填写客服消息名称")
    private String label;

    @NotEmpty(message = "请填写客服消息内容")
    private String content;

    @NotEmpty(message = "请填写客服消息发送时间")
    private String sendTime;


    /**
     * 0 条件筛选 1 全部
     */
    @NotNull(message = "请选择客服消息类型")
    private Integer type;

    private Integer selectSex;

    private String  selectSubscribeTime;

    private String selectProvince;

    private String selectCity;

    private String selectTag;

    private Long successNum;

    private Long preSuccessNum;

    private Integer status;

    private String createTime;

    private String authId;



    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String headImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSelectSex() {
        return selectSex;
    }

    public void setSelectSex(Integer selectSex) {
        this.selectSex = selectSex;
    }

    public String getSelectSubscribeTime() {
        return selectSubscribeTime;
    }

    public void setSelectSubscribeTime(String selectSubscribeTime) {
        this.selectSubscribeTime = selectSubscribeTime;
    }

    public String getSelectProvince() {
        return selectProvince;
    }

    public void setSelectProvince(String selectProvince) {
        this.selectProvince = selectProvince;
    }

    public String getSelectCity() {
        return selectCity;
    }

    public void setSelectCity(String selectCity) {
        this.selectCity = selectCity;
    }

    public String getSelectTag() {
        return selectTag;
    }

    public void setSelectTag(String selectTag) {
        this.selectTag = selectTag;
    }

    public Long getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Long successNum) {
        this.successNum = successNum;
    }

    public Long getPreSuccessNum() {
        return preSuccessNum;
    }

    public void setPreSuccessNum(Long preSuccessNum) {
        this.preSuccessNum = preSuccessNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "CustomerMsg{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", label='" + label + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", type=" + type +
                ", selectSex=" + selectSex +
                ", selectSubscribeTime='" + selectSubscribeTime + '\'' +
                ", selectProvince='" + selectProvince + '\'' +
                ", selectCity='" + selectCity + '\'' +
                ", selectTag='" + selectTag + '\'' +
                ", successNum=" + successNum +
                ", preSuccessNum=" + preSuccessNum +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", authId='" + authId + '\'' +
                '}';
    }
}
