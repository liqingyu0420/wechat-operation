package com.idiot.operationbackend.support.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * @author wang xiao
 * @date Created in 18:59 2020/9/18
 */
public enum MediaType {


    /**
     * 图
     */
    IMAGE(0,"image"),
    /**
     * 音
     */
    VOICE(1,"voice"),
    /**
     * 视频
     */
    VIDEO(2,"video"),
    /**
     * 缩略图
     */
    THUMB(3,"thumb"),

    /**
     * 图文
     */
    NEWS(4,"news");

    @EnumValue
    private final int code;

    @JsonValue
    private final String label;

    MediaType(int code, String label) {
        this.code = code;
        this.label = label;
    }


    public String getLabel() {
        return label;
    }

    public Integer getCode () {
        return code;
    }

    public static MediaType labelOf(String label) {
        for (MediaType temp :MediaType.values()) {
            if (label.equals(temp.label)) {
                return temp;
            }
        }
        return null;
    }


    public static MediaType codeOf(int code) {
        for (MediaType temp :MediaType.values()) {
            if (code==temp.code) {
                return temp;
            }
        }
        return null;
    }



}

