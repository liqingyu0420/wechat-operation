package com.idiot.operationbackend.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 模板消息
 * @author wang xiao
 * @date Created in 19:01 2020/9/22
 */
@TableName("t_template_msg")
public class TemplateMsg {

    @TableId
    private String id;

    @NotEmpty(message = "请选择公众号")
    private String accountId;

    @NotEmpty(message = "请填写模板消息名称")
    private String label;

    @NotEmpty(message = "请选择模板消息模板")
    private String templateName;

    @NotEmpty(message = "请选择模板消息模板")
    private String templateId;

    @NotEmpty(message = "请填写模板消息模板数据")
    private String templateData;

    @NotNull(message = "请选择跳转类型")
    private Integer linkType;

    private String linkUrl;

    private String linkAppId;

    private String linkAppPage;

    @NotEmpty(message = "请填写发送时间")
    private String sendTime;

    private Long sendNum;

    private Integer status;

    private String createTime;

    private String nickName;

    private String headImage;

    @NotNull(message = "请选择发送类型")
    private Boolean type;

    private Integer selectSex;

    private String  selectSubscribeTime;

    private String selectProvince;

    private String selectCity;

    private String selectTag;


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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public Integer getLinkType() {
        return linkType;
    }

    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkAppId() {
        return linkAppId;
    }

    public void setLinkAppId(String linkAppId) {
        this.linkAppId = linkAppId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Long getSendNum() {
        return sendNum;
    }

    public void setSendNum(Long sendNum) {
        this.sendNum = sendNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getLinkAppPage() {
        return linkAppPage;
    }

    public void setLinkAppPage(String linkAppPage) {
        this.linkAppPage = linkAppPage;
    }

    @Override
    public String toString() {
        return "TemplateMsg{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", label='" + label + '\'' +
                ", templateId='" + templateId + '\'' +
                ", templateData='" + templateData + '\'' +
                ", linkType=" + linkType +
                ", linkUrl='" + linkUrl + '\'' +
                ", linkAppId='" + linkAppId + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", sendNum=" + sendNum +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", type=" + type +
                ", selectSex=" + selectSex +
                ", selectSubscribeTime='" + selectSubscribeTime + '\'' +
                ", selectProvince='" + selectProvince + '\'' +
                ", selectCity='" + selectCity + '\'' +
                ", selectTag='" + selectTag + '\'' +
                '}';
    }
}
