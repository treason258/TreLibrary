package com.haoyang.lovelyreader.tre.bean;

import android.text.TextUtils;

import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.SharedUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by treason on 2018/9/25.
 */

public class BookDataHelper {

    private static final String KEY_BOOK_STORE = "key_book_store";

    // ******************************** BookStore ********************************

    /**
     * getBookStore
     */
    public static BookData getBookData() {
        try {
            String data = SharedUtils.get().getCommon(KEY_BOOK_STORE);
            if (!TextUtils.isEmpty(data)) {
                return GsonHelper.get().fromJson(data, BookData.class);
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
        return null;
    }

    /**
     * setBookData
     */
    public static void setBookData(BookData bookStore) {
        try {
            if (bookStore != null) {
                String data = GsonHelper.get().toJson(bookStore);
                if (!TextUtils.isEmpty(data)) {
                    SharedUtils.get().setCommon(KEY_BOOK_STORE, data);
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
        BookData bookStore = getBookData();
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
        BookData bookData = getBookData();
        if (bookData == null) {
            bookData = new BookData();
            HashMap<String, List<BookBean>> data = new HashMap<>();
            data.put(uid, bookBeanList);
            bookData.setData(data);
            setBookData(bookData);
        } else {
            HashMap<String, List<BookBean>> data = bookData.getData();
            if (data == null) {
                data = new HashMap<>();
            } else {
                if (data.containsKey(uid)) {
                    data.remove(uid);
                }
            }
            data.put(uid, bookBeanList);
            bookData.setData(data);
            setBookData(bookData);
        }
    }

    // ******************************** test ********************************

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
