package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 公众号
 * @author wang xiao
 * @date Created in 17:51 2020/9/10
 */
@TableName("t_account")
public class Account {

    @TableId
    private String id;

    private  String authorizerAppId;

    private  String authorizerAccessToken;

    private  String authorizerRefreshToken;

    private int expiresIn;

    private String nickName;

    private String headImage;

    private String serviceTypeInfo;

    private String verifyTypeInfo;

    private String userName;

    private String principalName;

    private String alias;

    private String businessInfo;

    private String qrcodeUrl;

    private Integer state;

    private String createUserId;

    /**
     * 粉丝数量
     */
    @TableField(exist = false)
    private Integer fansNum;

    /**
     * 互动粉丝数量
     */
    @TableField(exist = false)
    private Integer interactFansNum;


    public Account() {
    }

    public Account(String authorizerAppId) {
        this.authorizerAppId = authorizerAppId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
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

    public String getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(String serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public String getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(String verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getAuthorizerAppId() {
        return authorizerAppId;
    }

    public void setAuthorizerAppId(String authorizerAppId) {
        this.authorizerAppId = authorizerAppId;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getInteractFansNum() {
        return interactFansNum;
    }

    public void setInteractFansNum(Integer interactFansNum) {
        this.interactFansNum = interactFansNum;
    }

    public void clearField () {
        this.setAuthorizerAppId("");
        this.setAuthorizerAccessToken("");
        this.setAuthorizerRefreshToken("");
    }
}
