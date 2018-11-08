package com.haoyang.lovelyreader.tre.bean.store;

import com.haoyang.lovelyreader.tre.bean.BookBean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by treason on 2018/9/25.
 */

public class BookStore {

    private HashMap<String, LinkedHashMap<String, BookBean>> data; // 用户id，书id，书信息

    public HashMap<String, LinkedHashMap<String, BookBean>> getData() {
        return data;
    }

    public void setData(HashMap<String, LinkedHashMap<String, BookBean>> data) {
        this.data = data;
    }
}
