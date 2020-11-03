package com.idiot.operationbackend.vo;

/**
 * 粉丝消息公众号总类 （不想在FansMsg里面创建太多属性）
 * @author wang xiao
 * @date Created in 14:15 2020/9/18
 */
public class AccountMsgData {

    private String accountId;

    private String accountName;

    private String accountUrl;

    private Long number;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    public void setAccountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "FansMsgData{" +
                "accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountUrl='" + accountUrl + '\'' +
                ", number=" + number +
                '}';
    }
}
