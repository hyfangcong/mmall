package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author: fangcong
 * @date: 2020/1/31
 */
//保证json序列化的时候，如果是null对象，key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore   //使之不在json序列化结果之中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return this.status;
    }

    public T getData(){
        return this.data;
    }

    public String getMsg(){
        return this.msg;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMsg(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMsg(String errorMsg){
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMsg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMsg(int errorCode, String errorMsg){
        return new ServerResponse<>(errorCode, errorMsg);
    }
}
