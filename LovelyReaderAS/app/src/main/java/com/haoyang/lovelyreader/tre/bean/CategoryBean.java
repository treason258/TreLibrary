package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/10/24.
 */

public class CategoryBean {

//    "categoryId": 0,
//    "categoryName": "string",
//    "createDate": 0,
//    "parentId": 0

    private String categoryId;
    private String categoryName;
    private String createDate;
    private String parentId;

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
