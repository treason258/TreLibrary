package com.haoyang.lovelyreader.tre.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 18/10/24.
 */

public class CategoryBean {

    public static final String CATEGORY_ROOT_ID = "-1";
    public static final String CATEGORY_ROOT_NAME = "所有电子书";

    public static final String CATEGORY_DEFAULT = "1";

    public static final int LEVEL_0 = 10; // 所有电子书
    public static final int LEVEL_1 = 11; // 一级分类
    public static final int LEVEL_2 = 12; // 二级分类
    public static final int LEVEL_3 = 13; // 三级分类

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

    /**
     * 分类数据转换
     */
    public static List<CategoryBean> convertToShow(List<CategoryBean> categoryBeanL1List) {
        List<CategoryBean> result = new ArrayList<>();
        CategoryBean categoryBeanL0 = new CategoryBean();
        categoryBeanL0.setCategoryId(CategoryBean.CATEGORY_ROOT_ID);
        categoryBeanL0.setCategoryName(CategoryBean.CATEGORY_ROOT_NAME);
        categoryBeanL0.setLevel(CategoryBean.LEVEL_0);
        result.add(categoryBeanL0);

        if (categoryBeanL1List != null && categoryBeanL1List.size() > 0) {
            for (int i = 0; i < categoryBeanL1List.size(); i++) {
                CategoryBean categoryBeanL1 = categoryBeanL1List.get(i);
                categoryBeanL1.setLevel(CategoryBean.LEVEL_1);
                result.add(categoryBeanL1);

                List<CategoryBean> categoryBeanL2List = categoryBeanL1.getChildList();
                if (categoryBeanL2List != null && categoryBeanL2List.size() > 0) {
                    for (int j = 0; j < categoryBeanL2List.size(); j++) {
                        CategoryBean categoryBeanL2 = categoryBeanL2List.get(j);
                        categoryBeanL2.setLevel(CategoryBean.LEVEL_2);
                        result.add(categoryBeanL2);

                        List<CategoryBean> categoryBeanL3List = categoryBeanL2.getChildList();
                        if (categoryBeanL3List != null && categoryBeanL3List.size() > 0) {
                            for (int k = 0; k < categoryBeanL3List.size(); k++) {
                                CategoryBean categoryBeanL3 = categoryBeanL3List.get(k);
                                categoryBeanL3.setLevel(CategoryBean.LEVEL_3);
                                result.add(categoryBeanL3);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
