/**
 * 
 */
package com.app.base.service.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

/**
 * 
 * 对当前APP的一些相关操作。
 * 
 * @author tianyu912@yeah.net
 */
public class AndroidAppService {

	private PackageManager packageManager;

	private Context context;
	
	/**
	 * 
	 * @param activity
	 *            当前上下文。
	 */
	public AndroidAppService(Context context) {
		packageManager = context.getPackageManager();
		this.context = context;
	}

	/**
	 * 获得版本名称
	 * 
	 * @param context
	 *            当前上下文
	 * @return
	 */
	public String getVersionName() {
		try {
			PackageInfo pinfo = packageManager.getPackageInfo(
					context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionName;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得版本号
	 * 
	 * @param context
	 *            当前上下文
	 * @return
	 */
	public int getVersionCode() {
		try {

			PackageInfo pinfo = packageManager.getPackageInfo(
					context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionCode;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * 获取某个应用包是否有指定的权限。
	 * 
	 * @param context
	 *            当前上下文
	 * @return
	 */
	public boolean isHavePermission(String packageName, String permissionName) {

		return (PackageManager.PERMISSION_GRANTED == packageManager
				.checkPermission(permissionName, packageName));
	}

	/**
	 * 获取某个应用包是否有指定的权限。
	 * 
	 * @param context
	 *            当前上下文
	 * @return
	 */
	public boolean isHavePermission(String permissionName) {

		String packageName = context.getPackageName();
		return this.isHavePermission(packageName, permissionName);
	}

	public int getIdResource(String name) {

		return this.getResourceId(name, "id");
	}

	public int getDrawableResource(String name) {
		
		return this.getResourceId(name, "drawable");
	}

	public int getStyleableResource(String name) {
		
		return this.getResourceId(name, "styleable");
	}
	public int getLayoutResource(String name) {

		return this.getResourceId(name, "layout");
	}
	
	public int getStringResource(String name) {
		
		return this.getResourceId(name, "string");
	}
	
	public int getDimenResource(String name) {
		
		return this.getResourceId(name, "dimen");
	}
	
	public int getColorResource(String name) {

		return this.getResourceId(name, "color");
	}

	private int getResourceId(String name, String type) {

		Resources themeResources = null;
		String packageName = this.context.getPackageName();

		try {
			themeResources = this.packageManager.getResourcesForApplication(packageName);
			return themeResources.getIdentifier(name, type, packageName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
