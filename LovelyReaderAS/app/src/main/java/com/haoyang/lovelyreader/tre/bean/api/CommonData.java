package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/9/29.
 */

public class CommonData {

    private String terminal;
    private String timestamp;

    public CommonData() {
    }

    public CommonData(String terminal, String timestamp) {
        this.terminal = terminal;
        this.timestamp = timestamp;
    }

    public static CommonData get() {
        CommonData commonData = new CommonData();
        commonData.terminal = "ANDROID_PHONE";
        commonData.timestamp = String.valueOf(System.currentTimeMillis());
        return commonData;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
