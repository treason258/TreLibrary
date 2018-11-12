///**
// *
// */
//package com.haoyang.lovelyreader.db;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteStatement;
//
//import com.app.base.service.android.db.AbstractDBService;
//import com.haoyang.lovelyreader.entity.UserBook;
//import com.haoyang.lovelyreader.entity.User;
//
///**
// * 图书信息入库与出库的服务类。
// *
// * @author tianyu912@yeah.net
// */
//public class BookDBService extends AbstractDBService {
//
//  public final static String dbName = "UserBook.db";
//
//  public BookDBService(Context context) {
//    super(context, dbName);
//  }
//
//  /**
//   * 根据图书的Id来获取图书信息。
//   *
//   * @param id 图书的Id，TODO 将来可能会让后台或C++端生成。
//   */
//  public int countBook() {
//
//    String sql = "SELECT count(*) as c FROM book";
//    final Cursor cursor = myDatabase.rawQuery(sql, null);
//
//    try {
//      if (cursor.moveToNext()) {
//        int c = cursor.getInt(0);
//        return c;
//      }
//    } finally {
//      cursor.close();
//    }
//
//    return 0;
//  }
//
//  public List<UserBook> loadBookByTimes(User user) {
//
//    String sql = "SELECT bookId, userId, bookName, path, coverPath, type, times FROM book WHERE userId = \""
//        + user.userId + "\" order by times desc";
//
//    // String sql = "SELECT bookId, bookName, path, type, times FROM book";
//
//    List<UserBook> books = new ArrayList<UserBook>();
//
//    final Cursor cursor = myDatabase.rawQuery(sql, null);
//
//    try {
//      while (cursor.moveToNext()) {
//        UserBook book = createBook(cursor);
//        books.add(book);
//      }
//    } finally {
//      cursor.close();
//    }
//
//    return books;
//  }
//
//  public UserBook loadBookByBookID(String bookId) {
//
//    String sql = "SELECT bookId, userId, bookName, path, coverPath, type, times FROM book WHERE bookId = \""
//        + bookId + "\"";
//
//    // String sql = "SELECT bookId, bookName, path, type, times FROM book";
//
//    final Cursor cursor = myDatabase.rawQuery(sql, null);
//
//    try {
//      if (cursor.moveToNext()) {
//        UserBook book = createBook(cursor);
//        return book;
//      }
//    } finally {
//      cursor.close();
//    }
//
//    return null;
//  }
//
//  public UserBook loadBookByPath(String path) {
//
//    String sql = "SELECT bookId, userId, bookName, path, coverPath, type, times FROM book WHERE path = \"";
//    final Cursor cursor = myDatabase.rawQuery(sql + path + "\"", null);
//
//    try {
//      if (cursor.moveToNext()) {
//        UserBook book = createBook(cursor);
//        return book;
//      }
//    } finally {
//      cursor.close();
//    }
//
//    return null;
//  }
//
//  /**
//   * 保存图书信息。
//   */
//  public void saveBook(UserBook book) {
//
//    final SQLiteStatement statement = get("INSERT OR REPLACE INTO book (id, bookId, userId, bookName, path, coverPath, type, times)"
//        + " VALUES (null, ?, ?, ?, ?, ?, ?, ?)");
//
//    synchronized (statement) {
//
//      int index = 1;
//
//      String value = book.bookId != null ? book.bookId : "";
//      statement.bindString(index++, value);
//      statement.bindString(index++, book.userId);
//      statement.bindString(index++, book.bookName != null ? book.bookName
//          : "");
//      statement.bindString(index++, book.bookPath != null ? book.bookPath
//          : "");
//
//      statement.bindString(index++,
//          book.coverPath != null ? book.coverPath : "");
//
//      statement.bindString(index++, book.type != null ? book.type : "");
//
//      statement.bindLong(index++, book.times);
//
//      statement.execute();
//    }
//  }
//
//  @Override
//  public void migrate() {
//    final int version = myDatabase.getVersion();
//    final int currentVersion = 1;
//    if (version >= currentVersion) {
//      return;
//    }
//
//    myDatabase.beginTransaction();
//
//    switch (version) {
//      case 0:
//        createTables();
//      case 1:
//
//      case 2:
//    }
//    myDatabase.setTransactionSuccessful();
//    myDatabase.setVersion(currentVersion);
//    myDatabase.endTransaction();
//
//    myDatabase.execSQL("VACUUM");
//  }
//
//  private void createTables() {
//
//    myDatabase.execSQL("CREATE TABLE IF NOT EXISTS book("
//        + "id INTEGER PRIMARY KEY autoincrement,"
//        + "userId TEXT NOT NULL, " + "bookId TEXT NOT NULL,"
//        + "bookName TEXT NOT NULL," + "path TEXT," + "coverPath TEXT,"
//        + "type TEXT," + "times INTEGER)");
//  }
//
//  private UserBook createBook(Cursor cursor) {
//
//    UserBook book = new UserBook();
//
//    int index = 0;
//    book.bookId = cursor.getString(index++);
//    book.userId = cursor.getString(index++);
//
//    book.bookName = cursor.getString(index++);
//    book.bookPath = cursor.getString(index++);
//    book.coverPath = cursor.getString(index++);
//
//    book.type = cursor.getString(index++);
//    book.times = cursor.getLong(index++);
//
//    return book;
//  }
//}
