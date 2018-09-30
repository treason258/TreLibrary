package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/9/30.
 */

public class UpdateBean {

//    {
//        "statusCode": 900,
//        "data": {
//            "hasNewApp": true,
//            "terminal": "ANDROID_PHONE",
//            "version": "1.1.0",
//            "appUrl": "http://localhost:9001/repo/appfile/2018-09-26/d75f7e08f1b6443188795ce44d46b550.eddx",
//            "upgradeType": 1,
//            "appName": "安卓1.1.0",
//            "desc": "安卓1.1.0"
//        },
//        "timestamp": 1538291873015,
//        "msg": null
//    }

    private String hasNewApp;
    private String terminal;
    private String version;
    private String appUrl;
    private String upgradeType;
    private String appName;
    private String desc;

    public String getHasNewApp() {
        return hasNewApp;
    }

    public void setHasNewApp(String hasNewApp) {
        this.hasNewApp = hasNewApp;
    }

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

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
