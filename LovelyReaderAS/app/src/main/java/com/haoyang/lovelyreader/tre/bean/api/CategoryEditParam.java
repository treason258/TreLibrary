package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/10/24.
 */

public class CategoryEditParam extends BaseParam {

    private String categoryId; // 分类ID
    private String categoryName; // 分类名称

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
