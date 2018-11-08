package com.haoyang.lovelyreader.tre.helper;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.store.BookStore;
import com.haoyang.lovelyreader.tre.bean.store.LastSyncDateStore;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.store.CategoryStore;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.SharedUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 18/9/26.
 */

public class DBHelper {

    private static final String KEY_USER_BEAN = "key_user_bean";
    private static final String KEY_BOOK_STORE = "key_book_store";
    private static final String KEY_CATEGORY_STORE = "key_category_store";
    private static final String KEY_LAST_SYNC_DATE_STORE = "key_last_sync_date_store";

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

    // ******************************** KEY_BOOK_STORE ********************************

    public static void setBookStore(BookStore bookStore) {
        String data = "";
        if (bookStore != null) {
            data = JsonParser.get().toJson(bookStore);
        }
        SharedUtils.get().setCommon(KEY_BOOK_STORE, data);
    }

    public static BookStore getBookStore() {
        String data = SharedUtils.get().getCommon(KEY_BOOK_STORE);
        if (!TextUtils.isEmpty(data)) {
            return JsonParser.get().toObject(data, BookStore.class);
        } else {
            return null;
        }
    }

    // ******************************** KEY_CATEGORY_STORE ********************************

    public static void setCategoryStore(CategoryStore categoryStore) {
        String data = "";
        if (categoryStore != null) {
            data = JsonParser.get().toJson(categoryStore);
        }
        SharedUtils.get().setCommon(KEY_CATEGORY_STORE, data);
    }

    public static CategoryStore getCategoryStore() {
        String data = SharedUtils.get().getCommon(KEY_CATEGORY_STORE);
        if (!TextUtils.isEmpty(data)) {
            return JsonParser.get().toObject(data, CategoryStore.class);
        } else {
            return null;
        }
    }

    // ******************************** KEY_LAST_SYNC_DATE_STORE ********************************

    public static void setLastSyncDateStore(LastSyncDateStore lastSyncDateStore) {
        String data = "";
        if (lastSyncDateStore != null) {
            data = JsonParser.get().toJson(lastSyncDateStore);
        }
        SharedUtils.get().setCommon(KEY_LAST_SYNC_DATE_STORE, data);
    }

    public static LastSyncDateStore getLastSyncDateStore() {
        String data = SharedUtils.get().getCommon(KEY_LAST_SYNC_DATE_STORE);
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
    public static void setBookBeanList(String uid, LinkedHashMap<String, BookBean> bookBeanMap) {
        BookStore bookStore = getBookStore();
        if (bookStore == null) {
            bookStore = new BookStore();
            HashMap<String, LinkedHashMap<String, BookBean>> data = new HashMap<>();
            data.put(uid, bookBeanMap);
            bookStore.setData(data);
        } else {
            HashMap<String, LinkedHashMap<String, BookBean>> data = bookStore.getData();
            if (data == null) {
                data = new HashMap<>();
            } else {
                if (data.containsKey(uid)) {
                    data.remove(uid);
                }
            }
            data.put(uid, bookBeanMap);
            bookStore.setData(data);
        }
        setBookStore(bookStore);
    }

    /**
     * getBookBeanList
     */
    public static LinkedHashMap<String, BookBean> getBookBeanList(String uid) {
        BookStore bookStore = getBookStore();
        if (bookStore != null && bookStore.getData() != null) {
            if (bookStore.getData().containsKey(uid)) {
                return bookStore.getData().get(uid);
            }
        }
        return new LinkedHashMap<>();
    }

    /**
     * containUidInBookStore
     */
    public static boolean containsUid(String uid) {
        BookStore bookStore = getBookStore();
        if (bookStore != null && bookStore.getData() != null) {
            if (bookStore.getData().containsKey(uid)) {
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
        LinkedHashMap<String, BookBean> bookBeanMap = getBookBeanList(uid);
        if (bookBeanMap == null) {
            bookBeanMap = new LinkedHashMap<>();
        }
        bookBeanMap.put(bookBean.getBookId(), bookBean);
        setBookBeanList(uid, bookBeanMap);
    }

    /**
     * delBookBean
     */
    public static void delBookBean(String uid, String bookId) {
        LinkedHashMap<String, BookBean> bookBeanMap = getBookBeanList(uid);
        if (bookBeanMap.containsKey(bookId)) {
            bookBeanMap.remove(bookId);
        }
        setBookBeanList(uid, bookBeanMap);
    }

    /**
     * modifyBookBean
     */
    public static void modifyBookBean(String uid, BookBean bookBean) {
        LinkedHashMap<String, BookBean> bookBeanMap = getBookBeanList(uid);
        if (bookBeanMap.containsKey(bookBean.getBookId())) {
            bookBeanMap.put(bookBean.getBookId(), bookBean);
        }
        setBookBeanList(uid, bookBeanMap);
    }

    /**
     * containBookBean
     */
    public static boolean containsBookBean(String uid, BookBean bookBean) {
        LinkedHashMap<String, BookBean> bookBeanMap = getBookBeanList(uid);
        if (bookBeanMap.containsKey(bookBean.getBookId())) {
            return true;
        }
        return false;
    }

    // ******************************** searchBook ********************************

    /**
     * 搜索书
     */
    public static LinkedHashMap<String, BookBean> getBookBeanListByKey(String uid, String key) {
        LinkedHashMap<String, BookBean> searchResult = new LinkedHashMap<>();
        if (!TextUtils.isEmpty(key)) {
            LinkedHashMap<String, BookBean> bookBeanMap = getBookBeanList(uid);
            for (Map.Entry<String, BookBean> entry : bookBeanMap.entrySet()) {
                BookBean bookBean = entry.getValue();
                if (bookBean != null && bookBean.getBookName() != null && bookBean.getBookName().contains(key)) {
                    searchResult.put(bookBean.getBookId(), bookBean);
                }
            }
        }
        return searchResult;
    }

    // ******************************** syncGuestBook ********************************

//    /**
//     * 对登录用户同步本设备游客添加的书籍
//     */
//    public static void syncGuestBook() {
//
//        // 游客用户
//        UserBean userBeanDefault = UserBean.getDefault();
//        // 游客用户添加的书
//        List<BookBean> bookBeanListDefault = getBookBeanList(userBeanDefault.getUid());
//
//        // 当前用户
//        UserBean userBean = getUserBean();
//        // 当前用户添加的书
//        List<BookBean> bookBeanList = getBookBeanList(userBean.getUid());
//
//        // 合并
//        for (int i = 0; i < bookBeanListDefault.size(); i++) {
//            BookBean bookBeanDefault = bookBeanListDefault.get(i);
//            boolean hasThisBook = false;
//            for (int j = 0; j < bookBeanList.size(); j++) {
//                if (bookBeanDefault.getLocalBookPath().equals(bookBeanList.get(j).getLocalBookPath())) {
//                    hasThisBook = true;
//                    break;
//                }
//            }
//            if (!hasThisBook) {
//                bookBeanList.add(bookBeanDefault);
//            }
//        }
//
//        // 清空游客数据
//        DBHelper.setBookBeanList(userBeanDefault.getUid(), new ArrayList<>());
//        // 同步当前用户数据
//        DBHelper.setBookBeanList(userBean.getUid(), bookBeanList);
//    }

    // ******************************** setLastSyncDate ********************************

    /**
     * setLastSyncDate
     */
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

    /**
     * getLastSyncDate
     */
    public static long getLastSyncDate(String uid) {
        LastSyncDateStore lastSyncDateStore = getLastSyncDateStore();
        if (lastSyncDateStore != null && lastSyncDateStore.getData() != null) {
            if (lastSyncDateStore.getData().containsKey(uid)) {
                return lastSyncDateStore.getData().get(uid);
            }
        }
        return 0;
    }

    // ******************************** setCategoryBeanList ********************************

    /**
     * setCategoryBeanList
     */
    public static void setCategoryBeanList(String uid, List<CategoryBean> categoryBeanList) {
        CategoryStore categoryStore = getCategoryStore();
        if (categoryStore == null) {
            categoryStore = new CategoryStore();
            HashMap<String, List<CategoryBean>> data = new HashMap<>();
            data.put(uid, categoryBeanList);
            categoryStore.setData(data);
        } else {
            HashMap<String, List<CategoryBean>> data = categoryStore.getData();
            if (data == null) {
                data = new HashMap<>();
            } else {
                if (data.containsKey(uid)) {
                    data.remove(uid);
                }
            }
            data.put(uid, categoryBeanList);
            categoryStore.setData(data);
        }
        setCategoryStore(categoryStore);
    }

    /**
     * getCategoryBeanList
     */
    public static List<CategoryBean> getCategoryBeanList(String uid) {
        CategoryStore categoryStore = getCategoryStore();
        if (categoryStore != null && categoryStore.getData() != null) {
            if (categoryStore.getData().containsKey(uid)) {
                return categoryStore.getData().get(uid);
            }
        }
        return new ArrayList<>();
    }
}
