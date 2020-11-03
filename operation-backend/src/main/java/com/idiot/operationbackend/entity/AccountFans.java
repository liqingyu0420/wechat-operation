package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 公众号粉丝
 * @author wang xiao
 * @date Created in 18:51 2020/9/10
 */
@TableName("t_account_fans")
public class AccountFans {

    @TableId
    private String id;

    private String accountId;

    private String openId;

    private String nickName;

    private int sex;

    private String city;

    private String province;

    private String country;

    private String headImgUrl;

    private Integer subscribe;

    private Long subscribeTime;

    private String subscribeScene;

    private String subscribeSceneLabel;

    private String tagIdList;

    private String tags;

    private String remark;

    private String unionId;

    private Integer groupId;

    private Long lastInactiveTime;

    private Long createTime;

    private Boolean state;



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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getSubscribeScene() {
        return subscribeScene;
    }

    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
    }

    public String getSubscribeSceneLabel() {
        return subscribeSceneLabel;
    }

    public void setSubscribeSceneLabel(String subscribeSceneLabel) {
        this.subscribeSceneLabel = subscribeSceneLabel;
    }

    public String getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(String tagIdList) {
        this.tagIdList = tagIdList;
    }

    public Long getLastInactiveTime() {
        return lastInactiveTime;
    }

    public void setLastInactiveTime(Long lastInactiveTime) {
        this.lastInactiveTime = lastInactiveTime;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    public  String  initTags () {
        String tagIdList = this.getTagIdList();
        return tagIdList.replace("[","").replace("]","");
    }
}
