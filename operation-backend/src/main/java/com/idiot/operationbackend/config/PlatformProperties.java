package com.idiot.operationbackend.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信第三方平台配置类
 * @author wang xiao
 * @date Created in 13:46 2020/9/11
 */
@Component
@ConfigurationProperties(prefix = "wechat.platform")
public class PlatformProperties {

    /**
     * appId
     */
    private String appId;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 授权事件接收URL(用于接受验证票据)
     */
    private String ticketUrl;

    /**
     * 授权回调地址
     */
    private String authCallBack;

    /**
     * 消息接受地址
     */
    private String msgCallBack;

    /**
     * 加解密key
     */
    private String secret;

    /**
     * 开发平台上面配置的token
     */
    private String token;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getAuthCallBack() {
        return authCallBack;
    }

    public void setAuthCallBack(String authCallBack) {
        this.authCallBack = authCallBack;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsgCallBack() {
        return msgCallBack;
    }

    public void setMsgCallBack(String msgCallBack) {
        this.msgCallBack = msgCallBack;
    }
}
