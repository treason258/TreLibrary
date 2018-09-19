/**
 * 
 */
package com.app.base.service.android.db;

import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * 数据库服务的抽像类,主要是一些通用方法。
 * 
 * @author tianyu912@yeah.net
 */
public abstract class AbstractDBService {

	protected final SQLiteDatabase myDatabase;
	protected final Context context;

	/**
	 * SQLiteStatement缓存。key=>sql语句，value=>SQLiteStatement
	 */
	private final HashMap<String, SQLiteStatement> myStatements = new HashMap<String, SQLiteStatement>();

	public AbstractDBService(Context context, String dbName) {

		myDatabase = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE,
				null);
		this.context = context;
		migrate();
	}

	/**
	 * 升级的相关操作都在这个方法中完成，构造函数在执行的时候会调用这个方法。
	 */
	public abstract void migrate();

	public void executeAsTransaction(Runnable actions) {

		boolean transactionStarted = false;
		try {
			myDatabase.beginTransaction();
			transactionStarted = true;
		} catch (Throwable t) {
		}

		try {
			actions.run();
			if (transactionStarted) {
				myDatabase.setTransactionSuccessful();
			}
		} finally {
			if (transactionStarted) {
				myDatabase.endTransaction();
			}
		}
	}

	/**
	 * 
	 * @param sql
	 * @return
	 */
	protected SQLiteStatement get(String sql) {
		SQLiteStatement statement = myStatements.get(sql);
		if (statement == null) {
			statement = myDatabase.compileStatement(sql);
			myStatements.put(sql, statement);
		}
		return statement;
	}

	@Override
	public void finalize() {
		myDatabase.close();
	}

	// --------------------------------------------------------

	public static void bindString(SQLiteStatement statement, int index,
			String value) {

		if (value != null) {
			statement.bindString(index, value);
		} else {
			statement.bindNull(index);
		}
	}

	public static void bindLong(SQLiteStatement statement, int index, Long value) {
		if (value != null) {
			statement.bindLong(index, value);
		} else {
			statement.bindNull(index);
		}
	}

	public static void bindDate(SQLiteStatement statement, int index, Date value) {
		if (value != null) {
			statement.bindLong(index, value.getTime());
		} else {
			statement.bindNull(index);
		}
	}

	public static Date getDate(Cursor cursor, int index) {
		if (cursor.isNull(index)) {
			return null;
		}
		return new Date(cursor.getLong(index));
	}
}
