package com.idiot.operationbackend.vo;


/**
 * 粉丝 数据统计
 * @author wang xiao
 * @date Created in 17:01 2020/9/15
 */
public class GeneralStatData {

    private String dateLabel;

    private Long newNum;

    private Long cancelNum;

    private Long addNum;

    private Long inactiveNum;

    public String getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(String dateLabel) {
        this.dateLabel = dateLabel;
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

    public Long getAddNum() {
        return addNum;
    }

    public void setAddNum(Long addNum) {
        this.addNum = addNum;
    }

    public Long getInactiveNum() {
        return inactiveNum;
    }

    public void setInactiveNum(Long inactiveNum) {
        this.inactiveNum = inactiveNum;
    }
}
