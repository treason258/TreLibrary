package com.app.base.service.android.db;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

public final class SQLiteService {
	
	public static void bindString(SQLiteStatement statement, int index, String value) {
		
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


	public static void bindInteger(SQLiteStatement statement, int index, Integer value) {
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
	
	// ------------------------------------------------------------
}
