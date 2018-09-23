package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/9/23.
 */

public class BaseJsonBean<T> {

    private int statusCode;
    private String msg;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
