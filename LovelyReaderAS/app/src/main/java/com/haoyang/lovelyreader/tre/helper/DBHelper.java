package com.haoyang.lovelyreader.tre.helper;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.BookStoreBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.mjiayou.trecorelib.json.JsonHelper;
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
            data = JsonHelper.get().toJson(userBean);
        }
        SharedUtils.get().setCommon(KEY_USER_BEAN, data);
    }

    public static UserBean getUserBean() {
        String data = SharedUtils.get().getCommon(KEY_USER_BEAN);
        if (!TextUtils.isEmpty(data)) {
            return JsonHelper.get().fromJson(data, UserBean.class);
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
                return JsonHelper.get().fromJson(data, BookStoreBean.class);
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
                String data = JsonHelper.get().toJson(bookStore);
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

    // ******************************** searchBook ********************************

    /**
     * 搜索书
     */
    public static List<BookBean> getBookBeanListByKey(String uid, String key) {
        List<BookBean> searchResult = new ArrayList<>();
        if (!TextUtils.isEmpty(key)) {
            List<BookBean> bookBeanList = getBookBeanList(uid);
            for (int i = 0; i < bookBeanList.size(); i++) {
                BookBean bookBean = bookBeanList.get(i);
                if (bookBean != null && bookBean.getBook() != null && bookBean.getBook().bookName != null && bookBean.getBook().bookName.contains(key)) {
                    searchResult.add(bookBean);
                }
            }
        }
        return searchResult;
    }

    // ******************************** syncGuestBook ********************************

    /**
     * 对登录用户同步本设备游客添加的书籍
     */
    public static void syncGuestBook() {
        // 游客添加的书籍
        List<BookBean> guestBookBeanList = getBookBeanList(UserBean.getDefault().getUid());
        // 当前用户
        UserBean userBean = getUserBean();
        // 当前用户的书
        List<BookBean> bookBeanList = getBookBeanList(userBean.getUid());
        // 合并
        for (int i = 0; i < guestBookBeanList.size(); i++) {
            BookBean guestBook = guestBookBeanList.get(i);
            boolean hasThisBook = false;
            for (int j = 0; j < bookBeanList.size(); j++) {
                if (guestBook.getPath().equals(bookBeanList.get(j).getPath())) {
                    hasThisBook = true;
                    break;
                }
            }
            if (!hasThisBook) {
                bookBeanList.add(guestBook);
            }
        }
        // 重新赋值
        DBHelper.setBookBeanList(userBean.getUid(), bookBeanList);
    }
}
