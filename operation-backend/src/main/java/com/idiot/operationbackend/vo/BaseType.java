package com.idiot.operationbackend.vo;

/**
 * 无聊才会建个这个类 json 不香嘛？？？？？？？
 * @author wang xiao
 * @date Created in 15:34 2020/9/22
 */
public class BaseType {

    private Integer code;

    private String label;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public BaseType() {
    }

    public BaseType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String toString() {
        return "BaseType{" +
                "code='" + code + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
