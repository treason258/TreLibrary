package com.haoyang.lovelyreader.tre.helper;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.BookStoreBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.SharedUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xin on 18/9/26.
 */

public class DBHelper {

    private static final String KEY_USER_BEAN = "key_user_bean";
    private static final String KEY_BOOK_STORE_BEAN = "key_book_store_bean";

    // ******************************** UserBean ********************************

    public static void setUserBean(UserBean userBean) {
        String data = "";
        if (userBean != null) {
            data = GsonHelper.get().toJson(userBean);
        }
        SharedUtils.get().setCommon(KEY_USER_BEAN, data);
    }

    public static UserBean getUserBean() {
        String data = SharedUtils.get().getCommon(KEY_USER_BEAN);
        if (!TextUtils.isEmpty(data)) {
            return GsonHelper.get().fromJson(data, UserBean.class);
        } else {
            // 如果当前没有用户，则设置默认用户
            return UserBean.getDefault();
        }
    }

    // ******************************** BookStoreBean ********************************

    /**
     * getBookStoreBean
     */
    public static BookStoreBean getBookStoreBean() {
        try {
            String data = SharedUtils.get().getCommon(KEY_BOOK_STORE_BEAN);
            if (!TextUtils.isEmpty(data)) {
                return GsonHelper.get().fromJson(data, BookStoreBean.class);
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
        return null;
    }

    /**
     * setBookStoreBean
     */
    public static void setBookStoreBean(BookStoreBean bookStore) {
        try {
            if (bookStore != null) {
                String data = GsonHelper.get().toJson(bookStore);
                if (!TextUtils.isEmpty(data)) {
                    SharedUtils.get().setCommon(KEY_BOOK_STORE_BEAN, data);
                }
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
    }

    // ******************************** List<BookBean> ********************************

    /**
     * containUid
     */
    public static boolean containUid() {
        return false;
    }

    /**
     * getBookBeanList
     */
    public static List<BookBean> getBookBeanList(String uid) {
        BookStoreBean bookStore = getBookStoreBean();
        if (bookStore != null && bookStore.getData() != null) {
            if (bookStore.getData().containsKey(uid)) {
                return bookStore.getData().get(uid);
            }
        }
        return new ArrayList<>();
    }

    /**
     * setBookBeanList
     */
    public static void setBookBeanList(String uid, List<BookBean> bookBeanList) {
        BookStoreBean bookStoreBean = getBookStoreBean();
        if (bookStoreBean == null) {
            bookStoreBean = new BookStoreBean();
            HashMap<String, List<BookBean>> data = new HashMap<>();
            data.put(uid, bookBeanList);
            bookStoreBean.setData(data);
            setBookStoreBean(bookStoreBean);
        } else {
            HashMap<String, List<BookBean>> data = bookStoreBean.getData();
            if (data == null) {
                data = new HashMap<>();
            } else {
                if (data.containsKey(uid)) {
                    data.remove(uid);
                }
            }
            data.put(uid, bookBeanList);
            bookStoreBean.setData(data);
            setBookStoreBean(bookStoreBean);
        }
    }

    // ******************************** BookBean ********************************

    /**
     * containBookBean
     */
    public static boolean containBookBean(String uid, BookBean bookBean) {
        return false;
    }

    /**
     * addBookBean
     */
    public static void addBookBean(String uid, BookBean bookBean) {
        List<BookBean> bookBeanList = getBookBeanList(uid);
        if (bookBeanList == null) {
            bookBeanList = new ArrayList<>();
        }
        bookBeanList.add(bookBean);
        setBookBeanList(uid, bookBeanList);
    }

    /**
     * delBookBean
     */
    public static void delBookBean(String uid, BookBean bookBean) {
    }

}
