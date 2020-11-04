package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 群发消息
 * @author wang xiao
 * @date Created in 16:38 2020/9/22
 */
@TableName("t_group_msg")
public class GroupMsg {

    @TableId
    private String id;

    @NotEmpty(message = "请选择公众号")
    private String accountId;

    @NotEmpty(message = "请编辑群发内容")
    private String content;

    @NotEmpty(message = "请选择群发时间")
    private String sendTime;

    private Long sendNum;

    /**
     * 0 条件筛选 1 全部
     */
    @NotNull(message = "请选择粉丝选择")
    private Boolean type;

    @NotNull(message = "请选择群发类型")
    private Integer msgType;

    private Integer selectSex;

    private String  selectSubscribeTime;

    private String selectProvince;

    private String selectCity;

    private String selectTag;

    private String phone;

    /**
     * 转载是否继续发送
     */
    private Integer repeatSend;

    private Integer status;

    private String createTime;

    private String nickName;

    private String headImage;

    private String msgId;

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

    public Long getSendNum() {
        return sendNum;
    }

    public void setSendNum(Long sendNum) {
        this.sendNum = sendNum;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRepeatSend() {
        return repeatSend;
    }

    public void setRepeatSend(Integer repeatSend) {
        this.repeatSend = repeatSend;
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

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "GroupMsg{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", sendNum=" + sendNum +
                ", type=" + type +
                ", selectSex=" + selectSex +
                ", selectSubscribeTime='" + selectSubscribeTime + '\'' +
                ", selectProvince='" + selectProvince + '\'' +
                ", selectCity='" + selectCity + '\'' +
                ", selectTag='" + selectTag + '\'' +
                ", phone='" + phone + '\'' +
                ", repeatSend=" + repeatSend +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                '}';
    }
}
