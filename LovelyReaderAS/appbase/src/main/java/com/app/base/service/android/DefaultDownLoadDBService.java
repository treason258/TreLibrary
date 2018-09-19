/**
 * 
 */
package com.app.base.service.android;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.app.base.service.android.db.DBHelper;
import com.java.common.download.DownDBService;
import com.java.common.download.DownLoadInfo;
import com.java.common.download.DownloadThreadInfo;


/**
 * 
 * 默认下载服务类。 创建了存储下载信息的表结构。 提供了获取下载前、下载中、下载后的一些信息的功能。
 *  开始执行操作之前需要先执行open方法.
 * 退出应用之前要执行close方法.
 * 
 * @author tianyu
 * 
 *         Email: xjzx_tianyu@yeah.net
 * 
 *         Apr 23, 2014 3:59:53 PM
 */
public class DefaultDownLoadDBService implements DownDBService {

	private final static String DOWNLOAD_TABLE_NAME = "download";
	private final static String DOWNLOAD_THREAD_TABLE_NAME = "threadinfo";

	private DBHelper dbHelper;
	private static DefaultDownLoadDBService instance;

	// 存放下载数据的表结构。
	private final static String[] COLUMNS = { "id", "url", "savepath",
			"saveFile", "status", "threadNum", "totalSize", "blockSize",
			"downLoadSize", "error", "times", "fileName", "imageUrl", "user",
			"coursewareId", "type" };
	private final static String[] columns_thread = new String[] { "threadId",
			"downloadId", "downLength", "startPos" };

	private final static String[] CREATE_TABLES = new String[] {
			"create table IF NOT EXISTS download ("
					+ "id INTEGER PRIMARY KEY, " + "url VARCHAR, "
					+ "savepath varchar," + "saveFile varchar, "
					+ "status INTEGER, " + "threadNum INTEGER, "
					+ "totalSize INTEGER," + "blockSize INTEGER,"
					+ "downLoadSize INTEGER," + "error varchar, "
					+ "times INTEGER," + "fileName varchar,"
					+ "imageUrl varchar, " + "user varchar, "
					+ "coursewareId varchar, " + "type varchar);",
			"create table IF NOT EXISTS threadinfo ("
					+ "id INTEGER PRIMARY KEY, " + "threadId INTEGER, "
					+ "downloadId INTEGER, " + "startPos INTEGER,"
					+ " downLength INTEGER);" };

	/**
	 * 更新表sql
	 */
	private static final String[] UPDATE_TABLE_SQL_ARRAY = new String[] {
			"DROP TABLE IF EXISTS download", "DROP TABLE IF EXISTS threadinfo" };
	
	public static DefaultDownLoadDBService getInstance(Context context) {
		if (instance == null) {

			DBHelper downloadDBHelper = new DBHelper(context, CREATE_TABLES,
					UPDATE_TABLE_SQL_ARRAY, "DownladDB");

			instance = new DefaultDownLoadDBService(downloadDBHelper);
			instance.open();
		}
		return instance;
	}

	private DefaultDownLoadDBService(DBHelper dbHelper) {
		super();
		this.dbHelper = dbHelper;
	}

	public void open() {
		this.dbHelper.open();
	}

	public void close() {
		this.dbHelper.close();
	}

	@Override
	public synchronized int deleteDownLoadInfo(String where, String[] whereArgs) {
		int i = this.dbHelper.delete(DOWNLOAD_TABLE_NAME, where, whereArgs);
		return i;
	}

	@Override
	public synchronized int deleteDownLoadThreadInfo(String where,
			String[] whereArgs) {
		return this.dbHelper.delete(DOWNLOAD_THREAD_TABLE_NAME, where,
				whereArgs);
	}

	@Override
	public synchronized void saveDownloadThreadInfo(
			DownloadThreadInfo downloadThreadInfo) {
		ContentValues values = new ContentValues();

		values.put("threadId", downloadThreadInfo.id);
		values.put("downloadId", downloadThreadInfo.downloadId);
		values.put("downLength", downloadThreadInfo.downLength);
		values.put("startPos", downloadThreadInfo.startPos);
		this.dbHelper.insert(DOWNLOAD_THREAD_TABLE_NAME, values);
	}

	@Override
	/**
	 * 如果没有指定where和wherreArgs参数则默认用downloadId和id 做更新的条件.
	 */
	public synchronized boolean updateDownLoadThreadInfo(
			DownloadThreadInfo downloadThreadInfo, String where,
			String[] whereArgs) {

		ContentValues values = new ContentValues();
		values.put("downLength", downloadThreadInfo.downLength);
		values.put("startPos", downloadThreadInfo.startPos);

		if (where == null) {
			where = "downloadId = ? and threadId = ?";
			whereArgs = new String[] {
					String.valueOf(downloadThreadInfo.downloadId),
					String.valueOf(downloadThreadInfo.id) };
		}

		return this.dbHelper.update(DOWNLOAD_THREAD_TABLE_NAME, values, where,
				whereArgs);
	}

	@Override
	public synchronized boolean updateDownloadInfo(DownLoadInfo downLoadInfo,
			String where, String[] whereArgs) {

		ContentValues values = new ContentValues();
		values.put("status", downLoadInfo.status.get());
		values.put("totalSize", downLoadInfo.totalSize);
		values.put("blockSize", downLoadInfo.blockSize);
		values.put("downLoadSize", downLoadInfo.downLoadSize.get());
		values.put("error", downLoadInfo.error);
		values.put("times", downLoadInfo.times.getTime());

		if (where == null) {
			where = "url = ?  and user=?";
			whereArgs = new String[] { downLoadInfo.url, downLoadInfo.user };
		}

		return dbHelper.update(DOWNLOAD_TABLE_NAME, values, where, whereArgs);
	}

	@Override
	public DownLoadInfo loadDownLoadInfo(DownLoadInfo paramDownLoadInfo) {

		String where = "url = ? and user = ?";
		String[] whereArgs = new String[] { paramDownLoadInfo.url,
				paramDownLoadInfo.user };
		Cursor cursor = this.dbHelper.loadByWhere(DOWNLOAD_TABLE_NAME, COLUMNS,
				where, whereArgs);
		DownLoadInfo downLoadInfo = new DownLoadInfo();
		if (cursor != null && cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		try {
			if (cursor != null && cursor.getCount() != 0) {
				downLoadInfo = getDownloadInfoFromCursor(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return downLoadInfo;
	}

	public int loadDownLoadInfoStatus(String url, String user) {

		String where = "url = ? and user = ?";
		String[] whereArgs = new String[] { url, user };
		Cursor cursor = this.dbHelper.loadByWhere(DOWNLOAD_TABLE_NAME,
				new String[] { "status" }, where, whereArgs);
		if (cursor != null && cursor.getCount() == 0) {
			cursor.close();
			return 0;
		}
		try {
			if (cursor != null && cursor.getCount() != 0) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return 0;
	}

	private DownLoadInfo getDownloadInfoFromCursor(Cursor cursor) {
		DownLoadInfo downLoadInfo = new DownLoadInfo();
		downLoadInfo.id = cursor.getLong(0);
		downLoadInfo.url = cursor.getString(1);
		downLoadInfo.savePath = cursor.getString(2);
		downLoadInfo.saveFileName = cursor.getString(3);
		downLoadInfo.status.set(cursor.getInt(4));
		downLoadInfo.threadNum = cursor.getInt(5);
		downLoadInfo.totalSize = cursor.getLong(6);
		downLoadInfo.blockSize = cursor.getLong(7);
		downLoadInfo.downLoadSize.set(cursor.getLong(8));
		downLoadInfo.error = cursor.getString(9);
		downLoadInfo.times = new Timestamp(cursor.getLong(10));
		downLoadInfo.fileName = cursor.getString(11);
		downLoadInfo.imageUrl = cursor.getString(12);
		downLoadInfo.user = cursor.getString(13);
		downLoadInfo.coursewareId = cursor.getString(14);
		downLoadInfo.type = cursor.getString(15);
		return downLoadInfo;
	}

	@Override
	public void saveDownLoadInfo(DownLoadInfo downLoadInfo) {
		ContentValues values = new ContentValues();
		values.put("url", downLoadInfo.url);
		values.put("savepath", downLoadInfo.savePath);
		values.put("saveFile", downLoadInfo.saveFileName);
		values.put("status", downLoadInfo.status.get());
		values.put("threadNum", downLoadInfo.threadNum);
		values.put("totalSize", downLoadInfo.totalSize);
		values.put("blockSize", downLoadInfo.blockSize);
		values.put("downLoadSize", downLoadInfo.downLoadSize.get());
		values.put("error", downLoadInfo.error);
		values.put("times", downLoadInfo.times.getTime());
		values.put("fileName", downLoadInfo.fileName);
		values.put("imageUrl", downLoadInfo.imageUrl);
		values.put("user", downLoadInfo.user);
		values.put("coursewareId", downLoadInfo.coursewareId);
		values.put("type", downLoadInfo.type);
		this.dbHelper.insert(DOWNLOAD_TABLE_NAME, values);
	}

	public void updateDownLoadInfoStatus(String url, int status) {
		ContentValues values = new ContentValues();
		values.put("status", status);
		this.dbHelper.update(DOWNLOAD_TABLE_NAME, values, " url = '" + url
				+ "'");
	}

	@Override
	public DownloadThreadInfo loadDownLoadThreadInfo(String where,
			String[] whereArgs) {

		Cursor cursor = this.dbHelper.loadByWhere(DOWNLOAD_THREAD_TABLE_NAME,
				columns_thread, where, whereArgs);
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}

		DownloadThreadInfo downloadThreadInfo = new DownloadThreadInfo();
		try {
			downloadThreadInfo.id = cursor.getLong(0);
			downloadThreadInfo.downloadId = cursor.getLong(1);
			downloadThreadInfo.downLength = cursor.getLong(2);
			downloadThreadInfo.startPos = cursor.getLong(3);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return downloadThreadInfo;
	}

	@Override
	public List<DownloadThreadInfo> loadThreadInfoByDownloadId(long downloadId) {
		Cursor cursor = this.dbHelper.loadByWhere(DOWNLOAD_THREAD_TABLE_NAME,
				columns_thread, "downloadId = ?",
				new String[] { String.valueOf(downloadId) });
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}

		List<DownloadThreadInfo> downloadThreadInfos = new ArrayList<DownloadThreadInfo>();
		do {
			DownloadThreadInfo downloadThreadInfo = new DownloadThreadInfo();
			downloadThreadInfo.id = cursor.getLong(0);
			downloadThreadInfo.downloadId = cursor.getLong(1);
			downloadThreadInfo.downLength = cursor.getLong(2);
			downloadThreadInfo.startPos = cursor.getLong(3);
			downloadThreadInfos.add(downloadThreadInfo);
		} while (cursor.moveToNext());
		return downloadThreadInfos;
	}

	@Override
	public List<DownLoadInfo> getDownLoadInfosByUser(
			DownLoadInfo paramDownLoadInfo) {
		List<DownLoadInfo> downLoadInfos = new ArrayList<DownLoadInfo>();

		Cursor cursor = this.dbHelper.loadByWhere(DOWNLOAD_TABLE_NAME, COLUMNS,
				"user=? order by id desc",
				new String[] { paramDownLoadInfo.user });
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();
			return downLoadInfos;
		}

		try {
			do {
				downLoadInfos.add(getDownloadInfoFromCursor(cursor));
			} while (cursor.moveToNext());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return downLoadInfos;
	}
	
	// 所有下载的信息都在这里记载。
	private List<DownLoadInfo> downLoadInfos;
	/**
	 * 根据指定的用户来加载对应的已经进入下载管理中的资源信息.
	 * @param context
	 */
	public  List<DownLoadInfo> loadDownLoads( String username) {

		if (downLoadInfos != null) {
			return downLoadInfos;
		}
		
		DownLoadInfo downLoadInfo = new DownLoadInfo();
		downLoadInfo.user = username;
		downLoadInfos = this.getDownLoadInfosByUser(downLoadInfo);
		return  downLoadInfos;
	}

	@Override
	public synchronized void deleteDownloadInfo(DownLoadInfo downLoadInfo) {

		DownLoadInfo dli = downLoadInfo;
		if (downLoadInfo.id == -1) {
			dli = loadDownLoadInfo(downLoadInfo);
		}

		// 删除下载的数据。
		String where = "url = ?  and user=?";
		String[] whereArgs = new String[] { dli.url, dli.user };
		this.dbHelper.delete(DOWNLOAD_TABLE_NAME, where, whereArgs);

		// 删除下载对应的线程信息。
		where = "downloadId = ? ";
		whereArgs = new String[] { String.valueOf(dli.id) };
		this.dbHelper.delete(DOWNLOAD_THREAD_TABLE_NAME, where, whereArgs);

	}

	@Override
	public DownLoadInfo getDownloadInfoByCoursewareId(String coursewareId,
			String user) {

		String where = "coursewareId = ? and user = ?";
		String[] whereArgs = new String[] { coursewareId, user };
		Cursor cursor = this.dbHelper.loadByWhere(DOWNLOAD_TABLE_NAME, COLUMNS,
				where, whereArgs);
		DownLoadInfo downLoadInfo = new DownLoadInfo();
		if (cursor != null && cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		try {
			if (cursor != null && cursor.getCount() != 0) {
				downLoadInfo = getDownloadInfoFromCursor(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return downLoadInfo;
	}
}
