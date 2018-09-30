package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/9/29.
 */

public class AppUpgradeParam {

//    {
//        "terminal": "string",
//        "version": "string"
//    }

    private String terminal;
    private String version;

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
