package com.haoyang.lovelyreader.tre.bean.store;

import com.haoyang.lovelyreader.tre.bean.BookBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by treason on 2018/9/25.
 */

public class BookStore {

    private HashMap<String, List<BookBean>> data;

    public HashMap<String, List<BookBean>> getData() {
        return data;
    }

    public void setData(HashMap<String, List<BookBean>> data) {
        this.data = data;
    }
}
