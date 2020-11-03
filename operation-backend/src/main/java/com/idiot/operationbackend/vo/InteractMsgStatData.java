package com.idiot.operationbackend.vo;

/**
 * 互动消息 统计
 * @author wang xiao
 * @date Created in 17:10 2020/9/17
 */
public class InteractMsgStatData {

    private String dateLabel;

    private Long unSubscribe;

    private Long subscribe;

    private Long scan;

    private Long menu;

    private Long msg;

    private Long total;


    public String getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(String dateLabel) {
        this.dateLabel = dateLabel;
    }

    public Long getUnSubscribe() {
        return unSubscribe;
    }

    public void setUnSubscribe(Long unSubscribe) {
        this.unSubscribe = unSubscribe;
    }

    public Long getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Long subscribe) {
        this.subscribe = subscribe;
    }

    public Long getScan() {
        return scan;
    }

    public void setScan(Long scan) {
        this.scan = scan;
    }

    public Long getMenu() {
        return menu;
    }

    public void setMenu(Long menu) {
        this.menu = menu;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getMsg() {
        return msg;
    }

    public void setMsg(Long msg) {
        this.msg = msg;
    }
}
