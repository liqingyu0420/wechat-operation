package com.idiot.operationbackend.support;

/**
 * @author wang xiao
 * @date Created in 17:58 2020/9/24
 */
public class ScanQrInfo {

    private String openId;

    private String accountId;

    private String qrCodeId;

    private String qrContents;

    public ScanQrInfo() {
    }

    public ScanQrInfo(String accountId,String openId, String qrCodeId, String qrContents) {
        this.openId = openId;
        this.accountId = accountId;
        this.qrCodeId = qrCodeId;
        this.qrContents = qrContents;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getQrContents() {
        return qrContents;
    }

    public void setQrContents(String qrContents) {
        this.qrContents = qrContents;
    }


}
