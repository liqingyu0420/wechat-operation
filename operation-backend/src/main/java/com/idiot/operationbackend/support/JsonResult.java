package com.idiot.operationbackend.support;

/**
 * 返回json 统一实体类
 * @author wang xiao
 * @date Created in 14:35 2020/9/10
 */
public class JsonResult<T> {

    /**
     * 状态码
     */
    private int code;
    /**
     * 提示消息
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    public JsonResult<T> code (int status){
        this.code = status;
        return this;
    }
    public JsonResult<T> message (String message){
        this.message = message;
        return this;
    }
    public JsonResult<T> data (T data){
        this.data = data;
        return this;
    }

    private JsonResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> JsonResult<T> build (int code) {
        return new JsonResult<>(code);
    }

    public static <T> JsonResult<T> build (int code,T data,String message) {
        JsonResult<T> jsonResult  = JsonResult.build(code);
        return jsonResult.data(data).message(message);
    }

    public static <T> JsonResult<T> success (T data) {
        JsonResult<T> jsonResult  = JsonResult.build(200);
        return jsonResult.data(data);
    }
    public static <T> JsonResult<T> success (int code, T data) {
        JsonResult<T> jsonResult  = JsonResult.build(code);
        return jsonResult.data(data);
    }
    public static <T> JsonResult<T> success (T data, String message) {
        JsonResult<T> jsonResult  = JsonResult.build(200);
        return jsonResult.data(data).message(message);
    }

    public static <T> JsonResult<T> fail (String message) {
        JsonResult<T> jsonResult  = JsonResult.build(500);
        return jsonResult.message(message);
    }

    public static <T> JsonResult<T> fail (String message, T data) {
        JsonResult<T> jsonResult  = JsonResult.build(500);
        return jsonResult.message(message).data(data);
    }





}
