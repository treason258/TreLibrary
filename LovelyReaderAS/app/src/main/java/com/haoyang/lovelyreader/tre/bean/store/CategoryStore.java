package com.haoyang.lovelyreader.tre.bean.store;

import com.haoyang.lovelyreader.tre.bean.CategoryBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xin on 18/11/5.
 */

public class CategoryStore {

    private HashMap<String, List<CategoryBean>> data;

    public HashMap<String, List<CategoryBean>> getData() {
        return data;
    }

    public void setData(HashMap<String, List<CategoryBean>> data) {
        this.data = data;
    }
}
