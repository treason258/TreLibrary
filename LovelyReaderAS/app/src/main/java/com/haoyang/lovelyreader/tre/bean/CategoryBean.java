package com.haoyang.lovelyreader.tre.bean;

import java.util.List;

/**
 * Created by xin on 18/10/24.
 */

public class CategoryBean {

    public static final String CATEGORY_ROOT_ID = "-1";
    public static final String CATEGORY_ROOT_NAME = "所有电子书";

    public static final String CATEGORY_DEFAULT = "1";

    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;

//    "categoryId": 0,
//    "categoryName": "string",
//    "createDate": 0,
//    "parentId": 0

    private String categoryId;
    private String categoryName;
    private List<CategoryBean> childList;

    private int level; // 层级：1、2、3
    private boolean selected; // 选中状态

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<CategoryBean> getChildList() {
        return childList;
    }

    public void setChildList(List<CategoryBean> childList) {
        this.childList = childList;
    }

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
