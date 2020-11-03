package com.idiot.operationbackend.support;

/**
 * @author wang xiao
 * @date Created in 15:40 2020/9/10
 */
public class CustomException  extends RuntimeException{
    private int code;

    private String message;


    public CustomException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public CustomException(String message) {
        this.code = 500;
        this.message = message;
    }


    /**
     * avoid the expensive and useless stack trace for api exceptions
     * 避免对堆栈无效追踪 导致性能下降严重
     */
    @Override
    public  Throwable fillInStackTrace() {
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
