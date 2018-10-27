package com.haoyang.lovelyreader.tre.helper;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.BookStoreBean;
import com.haoyang.lovelyreader.tre.bean.LastSyncDateStore;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.mjiayou.trecorelib.json.JsonParser;
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
    private static final String KEY_LAST_SYNC_DATE = "key_last_sync_date";
    private static final String KEY_BOOK_UNUPLOAD = "key_book_unupload";

    // ******************************** KEY_USER_BEAN ********************************

    public static void setUserBean(UserBean userBean) {
        String data = "";
        if (userBean != null) {
            data = JsonParser.get().toJson(userBean);
        }
        SharedUtils.get().setCommon(KEY_USER_BEAN, data);
    }

    public static UserBean getUserBean() {
        String data = SharedUtils.get().getCommon(KEY_USER_BEAN);
        if (!TextUtils.isEmpty(data)) {
            return JsonParser.get().toObject(data, UserBean.class);
        } else {
            return UserBean.getDefault(); // 如果当前没有用户，则设置默认用户
        }
    }

    // ******************************** KEY_BOOK_STORE_BEAN ********************************

    public static void setBookStoreBean(BookStoreBean bookStoreBean) {
        String data = "";
        if (bookStoreBean != null) {
            data = JsonParser.get().toJson(bookStoreBean);
        }
        SharedUtils.get().setCommon(KEY_BOOK_STORE_BEAN, data);
    }

    public static BookStoreBean getBookStoreBean() {
        String data = SharedUtils.get().getCommon(KEY_BOOK_STORE_BEAN);
        if (!TextUtils.isEmpty(data)) {
            return JsonParser.get().toObject(data, BookStoreBean.class);
        } else {
            return null;
        }
    }

    // ******************************** KEY_LAST_SYNC_DATE ********************************

    public static void setLastSyncDateStore(LastSyncDateStore lastSyncDateStore) {
        String data = "";
        if (lastSyncDateStore != null) {
            data = JsonParser.get().toJson(lastSyncDateStore);
        }
        SharedUtils.get().setCommon(KEY_LAST_SYNC_DATE, data);
    }

    public static LastSyncDateStore getLastSyncDateStore() {
        String data = SharedUtils.get().getCommon(KEY_LAST_SYNC_DATE);
        if (!TextUtils.isEmpty(data)) {
            return JsonParser.get().toObject(data, LastSyncDateStore.class);
        } else {
            return null;
        }
    }

    // ******************************** setBookBeanList ********************************

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
        }
        setBookStoreBean(bookStoreBean);
    }

    /**
     * getBookBeanList
     */
    public static List<BookBean> getBookBeanList(String uid) {
        BookStoreBean bookStoreBean = getBookStoreBean();
        if (bookStoreBean != null && bookStoreBean.getData() != null) {
            if (bookStoreBean.getData().containsKey(uid)) {
                return bookStoreBean.getData().get(uid);
            }
        }
        return new ArrayList<>();
    }

    /**
     * containUid
     */
    public static boolean containUid(String uid) {
        BookStoreBean bookStoreBean = getBookStoreBean();
        if (bookStoreBean != null && bookStoreBean.getData() != null) {
            if (bookStoreBean.getData().containsKey(uid)) {
                return true;
            }
        }
        return false;
    }

    // ******************************** addBookBean ********************************

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
    public static void delBookBean(String uid, String bookId) {
        List<BookBean> bookBeanList = getBookBeanList(uid);
        for (int i = 0; i < bookBeanList.size(); i++) {
            if (!TextUtils.isEmpty(bookBeanList.get(i).getBookId()) && bookBeanList.get(i).getBookId().equals(bookId)) {
                bookBeanList.remove(i);
                break;
            }
        }
        setBookBeanList(uid, bookBeanList);
    }

    /**
     * modifyBookBean
     */
    public static void modifyBookBean(String uid, BookBean bookBeanNew) {
        List<BookBean> bookBeanList = getBookBeanList(uid);
        for (int i = 0; i < bookBeanList.size(); i++) {
            if (!TextUtils.isEmpty(bookBeanList.get(i).getBookId()) && bookBeanList.get(i).getBookId().equals(bookBeanNew.getBookId())) {
                bookBeanList.set(i, bookBeanNew);
                break;
            }
        }
        setBookBeanList(uid, bookBeanList);
    }

    /**
     * containBookBean
     */
    public static boolean containBookBean(String uid, BookBean bookBean) {
        return false;
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
                if (bookBean != null && bookBean.getBookName() != null && bookBean.getBookName().contains(key)) {
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

        // 游客用户
        UserBean userBeanDefault = UserBean.getDefault();
        // 游客用户添加的书
        List<BookBean> bookBeanListDefault = getBookBeanList(userBeanDefault.getUid());

        // 当前用户
        UserBean userBean = getUserBean();
        // 当前用户添加的书
        List<BookBean> bookBeanList = getBookBeanList(userBean.getUid());

        // 合并
        for (int i = 0; i < bookBeanListDefault.size(); i++) {
            BookBean bookBeanDefault = bookBeanListDefault.get(i);
            boolean hasThisBook = false;
            for (int j = 0; j < bookBeanList.size(); j++) {
                if (bookBeanDefault.getLocalBookPath().equals(bookBeanList.get(j).getLocalBookPath())) {
                    hasThisBook = true;
                    break;
                }
            }
            if (!hasThisBook) {
                bookBeanList.add(bookBeanDefault);
            }
        }

        // 清空游客数据
        DBHelper.setBookBeanList(userBeanDefault.getUid(), new ArrayList<>());
        // 同步当前用户数据
        DBHelper.setBookBeanList(userBean.getUid(), bookBeanList);
    }

    // ******************************** setLastSyncDate ********************************

    public static void setLastSyncDate(String uid, long timestamp) {
        LastSyncDateStore lastSyncDateStore = getLastSyncDateStore();
        if (lastSyncDateStore == null) {
            lastSyncDateStore = new LastSyncDateStore();
            HashMap<String, Long> data = new HashMap<>();
            data.put(uid, timestamp);
            lastSyncDateStore.setData(data);
        } else {
            HashMap<String, Long> data = lastSyncDateStore.getData();
            if (data == null) {
                data = new HashMap<>();
            } else {
                if (data.containsKey(uid)) {
                    data.remove(uid);
                }
            }
            data.put(uid, timestamp);
            lastSyncDateStore.setData(data);
        }
        setLastSyncDateStore(lastSyncDateStore);
    }

    public static long getLastSyncDate(String uid) {
        LastSyncDateStore lastSyncDateStore = getLastSyncDateStore();
        if (lastSyncDateStore != null && lastSyncDateStore.getData() != null) {
            if (lastSyncDateStore.getData().containsKey(uid)) {
                return lastSyncDateStore.getData().get(uid);
            }
        }
        return 0;
    }
}
