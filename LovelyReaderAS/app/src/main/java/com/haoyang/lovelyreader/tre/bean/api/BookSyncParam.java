package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/10/24.
 */

public class BookSyncParam extends BaseParam {

//    "lastSyncDate": 0,
//    "syncType": 0

    private String lastSyncDate; // 上次同步时间
    private String syncType; // 同步电子书类型.1，同步全部;2，按时间同步

    public String getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
}
