/**
 * 
 */
package com.app.base.service.android;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.app.base.common.util.Size;
import com.app.base.exception.DeviceException;

/**
 * 
 * 获取系统及各个组件（包括硬件）的信息
 * 
 * @author tianyu
 * 
 *         email:tianyu912@yeah.net
 * 
 *         date:May 5, 2014
 */
public final class AndroidInfoService {

	/**
	 * 获取屏幕尺寸。
	 * @param context
	 * @return
	 */
	public Size getScreenSize(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Point p = new Point();
		wm.getDefaultDisplay().getSize(p);
		
		return new Size(p.x, p.y);
	}

	/**
	 * 存储卡是否可用。
	 * 
	 * @return
	 */
	public boolean isAvailableExternalStorage() {

		String externalStorageState = Environment.getExternalStorageState();

		return Environment.MEDIA_MOUNTED.equals(externalStorageState);
	}

	/**
	 * 获取SD卡剩余空间.
	 * 
	 * @return 剩余空间的字节数。
	 */
	public long getSDCardAvailableBlocks() {

		if (isAvailableExternalStorage()) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			return sf.getAvailableBlocks() * sf.getBlockSize();
		}
		return 0;
	}

	/**
	 * 获取屏幕密度。
	 * 
	 * @param a
	 * @return
	 */
	public float getDensity(Activity a) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.density;
	}

    public int getDpi(Activity a) {
		
    	DisplayMetrics outMetrics = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.densityDpi;
	}
    
	/**
	 * 获取系统版本
	 * 
	 * @return
	 */
	public String getSystemRelase() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * 获取下载文件存放的路径,路径后边带文件分隔符。
	 * 
	 * @param context
	 * @return　有null的可能。
	 */
	public String getDownLoadPath(Context context) throws DeviceException {

		return getPathByType(context, Environment.DIRECTORY_DOWNLOADS);
	}

	/**
	 * 
	 * 获取下载文件存放的路径,路径后边带文件分隔符。
	 * 
	 * @param context
	 * @return　有null的可能。
	 */
	public String getDocumentPath()  {
		
		return Environment.getDataDirectory().getAbsolutePath();
	}

	/**
	 * 
	 * 获取图片文件存放的路径,路径后边带文件分隔符。
	 * 
	 * @param context
	 * @return 有null的可能。
	 */
	public String getPicturePath(Context context) throws DeviceException {

		return getPathByType(context, Environment.DIRECTORY_PICTURES);
	}

	/**
	 * 
	 * 获取指定类型的路径,如果路径不存在就创建它，路径后边带文件分隔符。
	 * 
	 * @param context
	 * @param type
	 * @see Environment
	 * @return
	 */
	private String getPathByType(Context context, String type)
			throws DeviceException {

		File file;
		if (isAvailableExternalStorage()) {
			file = context.getExternalFilesDir(type);
			if (file == null) {
				throw new DeviceException("存储卡没有被正确使用!");
			}
		} else {
			file = context.getFilesDir();
		}

		if (!file.exists()) {
			file.mkdir();
		}

		return file.getAbsolutePath() + File.separator;
	}

	public String getPath(Context context) {

		File file;
		if (isAvailableExternalStorage()) {
			file = context.getFilesDir();
			if (file == null) {
				return null;
			}
		} else {
			file = context.getFilesDir();
		}

		if (!file.exists()) {
			file.mkdir();
		}

		return file.getAbsolutePath() + File.separator;
	}

	/**
	 * 指定的应用是否正在运行。
	 * 
	 * @param context
	 * @param runningPackageName
	 *            应用的包名。
	 */
	public boolean isRunning(Context context, String runningPackageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(runningPackageName)
					&& info.baseActivity.getPackageName().equals(
							runningPackageName)) {
				return true;
			}
		} // end for
		return false;
	}

}