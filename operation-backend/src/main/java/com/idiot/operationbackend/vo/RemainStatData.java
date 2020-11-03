package com.idiot.operationbackend.vo;

/**
 * 粉丝留存
 * @author wang xiao
 * @date Created in 14:06 2020/9/17
 */
public class RemainStatData {

    private String statDate;

    /**
     * 当天粉丝数
     */
    private Long nowNum;

    /**
     * 第1~7 留存 粉丝数
     */
    private Long day1Num;

    private Long day2Num;

    private Long day3Num;

    private Long day4Num;

    private Long day5Num;

    private Long day6Num;

    private Long day7Num;

    public RemainStatData() {
    }

    public RemainStatData(String statDate) {
        this.statDate = statDate;
    }

    public RemainStatData(String statDate, Long nowNum) {
        this.statDate = statDate;
        this.nowNum = nowNum;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Long getNowNum() {
        return nowNum;
    }

    public void setNowNum(Long nowNum) {
        this.nowNum = nowNum;
    }

    public Long getDay1Num() {
        return day1Num;
    }

    public void setDay1Num(Long day1Num) {
        this.day1Num = day1Num;
    }

    public Long getDay2Num() {
        return day2Num;
    }

    public void setDay2Num(Long day2Num) {
        this.day2Num = day2Num;
    }

    public Long getDay3Num() {
        return day3Num;
    }

    public void setDay3Num(Long day3Num) {
        this.day3Num = day3Num;
    }

    public Long getDay4Num() {
        return day4Num;
    }

    public void setDay4Num(Long day4Num) {
        this.day4Num = day4Num;
    }

    public Long getDay5Num() {
        return day5Num;
    }

    public void setDay5Num(Long day5Num) {
        this.day5Num = day5Num;
    }

    public Long getDay6Num() {
        return day6Num;
    }

    public void setDay6Num(Long day6Num) {
        this.day6Num = day6Num;
    }

    public Long getDay7Num() {
        return day7Num;
    }

    public void setDay7Num(Long day7Num) {
        this.day7Num = day7Num;
    }
}
