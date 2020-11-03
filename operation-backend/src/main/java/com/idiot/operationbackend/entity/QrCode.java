package com.idiot.operationbackend.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 二维码内容
 * @author wang xiao
 * @date Created in 18:05 2020/9/21
 */
@TableName("t_qr_code")
public class QrCode {

    @TableId
    private String  id;

    private String nikeName;

    private String headImage;

    @NotEmpty(message = "请选择公众号")
    private String accountId;

    @NotEmpty(message = "请填写二维码名称")
    private String label;

    @NotNull(message = "请选择生成类型")
    private Integer type;

    private Long  totalNum;

    private Long newNum;

    private Long followNum;

    private String url;

    private String ticket;

    private Integer pushType;

    private String content;

    private String createTime;

    private String expireTime;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getNewNum() {
        return newNum;
    }

    public void setNewNum(Long newNum) {
        this.newNum = newNum;
    }

    public Long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Long followNum) {
        this.followNum = followNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
