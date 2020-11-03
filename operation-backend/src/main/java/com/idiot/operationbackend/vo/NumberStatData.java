package com.idiot.operationbackend.vo;

/**
 * @author wang xiao
 * @date Created in 10:41 2020/9/17
 */
public class NumberStatData {

    private String key;

    private Long number;

    private String type;


    public NumberStatData() {
    }

    public NumberStatData(String key, Long number, String type) {
        this.key = key;
        this.number = number;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
