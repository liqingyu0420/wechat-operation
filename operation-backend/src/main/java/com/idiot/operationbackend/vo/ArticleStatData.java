package com.idiot.operationbackend.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.idiot.operationbackend.entity.ArticleStat;

import java.util.List;
import java.util.Objects;

/**
 * 图文影响力信息
 * @author wang xiao
 * @date Created in 15:48 2020/9/17
 */
public class ArticleStatData {

    private String statDate;

    private Long targetUser;

    private Long intPageReadUser;

    private Long intPageReadCount;

    private Long oriPageReadUser;

    private Long oriPageReadCount;

    private Long shareUser;

    private Long shareCount;

    private Long addToFavUser;

    private Long addToFavCount;

    private Integer size;

    @TableField(exist = false)
    private List<ArticleStat> list;

    public ArticleStatData() {
    }

    public ArticleStatData(String statDate, Integer size) {
        this.statDate = statDate;
        this.size = size;
    }

    public ArticleStatData(String statDate, List<ArticleStat> list) {
        this.statDate = statDate;
        this.size = Objects.isNull(list)?0:list.size();
        this.list = list;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Long getIntPageReadUser() {
        return intPageReadUser;
    }

    public void setIntPageReadUser(Long intPageReadUser) {
        this.intPageReadUser = intPageReadUser;
    }

    public Long getIntPageReadCount() {
        return intPageReadCount;
    }

    public void setIntPageReadCount(Long intPageReadCount) {
        this.intPageReadCount = intPageReadCount;
    }

    public Long getOriPageReadUser() {
        return oriPageReadUser;
    }

    public void setOriPageReadUser(Long oriPageReadUser) {
        this.oriPageReadUser = oriPageReadUser;
    }

    public Long getOriPageReadCount() {
        return oriPageReadCount;
    }

    public void setOriPageReadCount(Long oriPageReadCount) {
        this.oriPageReadCount = oriPageReadCount;
    }

    public Long getShareUser() {
        return shareUser;
    }

    public void setShareUser(Long shareUser) {
        this.shareUser = shareUser;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getAddToFavUser() {
        return addToFavUser;
    }

    public void setAddToFavUser(Long addToFavUser) {
        this.addToFavUser = addToFavUser;
    }

    public Long getAddToFavCount() {
        return addToFavCount;
    }

    public void setAddToFavCount(Long addToFavCount) {
        this.addToFavCount = addToFavCount;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<ArticleStat> getList() {
        return list;
    }

    public void setList(List<ArticleStat> list) {
        this.list = list;
    }

    public Long getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Long targetUser) {
        this.targetUser = targetUser;
    }
}
