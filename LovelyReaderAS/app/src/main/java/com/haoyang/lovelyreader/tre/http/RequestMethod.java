package com.haoyang.lovelyreader.tre.http;

/**
 * Created by treason on 2016/10/28.
 */
public enum RequestMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    HEAD("HEAD"),
    MOVE("MOVE"),
    COPY("COPY"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
