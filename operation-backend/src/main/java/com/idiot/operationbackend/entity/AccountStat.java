package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

/**
 * 公众号粉丝数据统计
 * @author wang xiao
 * @date Created in 13:57 2020/9/15
 */
@TableName("t_account_stat")
public class AccountStat {

    @TableId
    private String id;

    private String accountId;

    /**
     * 新增
     */
    private  Long newNum;

    /**
     * 取关
     */
    private  Long cancelNum;

    /**
     * 活跃
     */
    private  Long inactiveNum;

    /**
     * 总粉丝数
     */
    private  Long totalFansNum;

    /**
     * 净增
     */
    private  Long addNum;

    /**
     * 阅读数量
     */
    private  Long pageReadNum;


    /**
     * 新增比例
     */
    private BigDecimal newRate;


    /**
     * 取关比例比例
     */
    private BigDecimal cancelRate;


    /**
     * 活跃比例
     */
    private BigDecimal inactiveRate;

    /**
     * 总粉丝
     */
    private BigDecimal totalFansRate;

    /**
     * 净增比例
     */
    private BigDecimal addRate;


    /**
     * 阅读比例
     */
    private BigDecimal pageReadRate;

    /**
     * 七天活跃
     */
    private Long sevenNum;

    /**
     * 15天活跃
     */
    private Long fifteenNum;

    /**
     * 统计日期 yyyy-MM-dd
     */
    private String statDate;

    /**
     * 创建时间
     */
    private String createTime;

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

    public Long getNewNum() {
        return newNum;
    }

    public void setNewNum(Long newNum) {
        this.newNum = newNum;
    }

    public Long getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(Long cancelNum) {
        this.cancelNum = cancelNum;
    }

    public Long getInactiveNum() {
        return inactiveNum;
    }

    public void setInactiveNum(Long inactiveNum) {
        this.inactiveNum = inactiveNum;
    }

    public Long getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(Long totalFansNum) {
        this.totalFansNum = totalFansNum;
    }

    public Long getAddNum() {
        return addNum;
    }

    public void setAddNum(Long addNum) {
        this.addNum = addNum;
    }

    public Long getPageReadNum() {
        return pageReadNum;
    }

    public void setPageReadNum(Long pageReadNum) {
        this.pageReadNum = pageReadNum;
    }

    public BigDecimal getNewRate() {
        return newRate;
    }

    public void setNewRate(BigDecimal newRate) {
        this.newRate = newRate;
    }

    public BigDecimal getCancelRate() {
        return cancelRate;
    }

    public void setCancelRate(BigDecimal cancelRate) {
        this.cancelRate = cancelRate;
    }

    public BigDecimal getInactiveRate() {
        return inactiveRate;
    }

    public void setInactiveRate(BigDecimal inactiveRate) {
        this.inactiveRate = inactiveRate;
    }

    public BigDecimal getTotalFansRate() {
        return totalFansRate;
    }

    public void setTotalFansRate(BigDecimal totalFansRate) {
        this.totalFansRate = totalFansRate;
    }

    public BigDecimal getAddRate() {
        return addRate;
    }

    public void setAddRate(BigDecimal addRate) {
        this.addRate = addRate;
    }

    public BigDecimal getPageReadRate() {
        return pageReadRate;
    }

    public void setPageReadRate(BigDecimal pageReadRate) {
        this.pageReadRate = pageReadRate;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getSevenNum() {
        return sevenNum;
    }

    public void setSevenNum(Long sevenNum) {
        this.sevenNum = sevenNum;
    }

    public Long getFifteenNum() {
        return fifteenNum;
    }

    public void setFifteenNum(Long fifteenNum) {
        this.fifteenNum = fifteenNum;
    }
}
