package com.haoyang.lovelyreader.tre.bean;

import java.util.HashMap;

/**
 * Created by treason on 2018/10/27.
 */

public class LastSyncDateStore {

    private HashMap<String, Long> data;

    public HashMap<String, Long> getData() {
        return data;
    }

    public void setData(HashMap<String, Long> data) {
        this.data = data;
    }
}
