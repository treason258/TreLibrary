package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/10/24.
 */

public class CategoryParam extends BaseParam {

    private String categoryName; // 分类名称
    private String parentId; // 父分类ID

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
