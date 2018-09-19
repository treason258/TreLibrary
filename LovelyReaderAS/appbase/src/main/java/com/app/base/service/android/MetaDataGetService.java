/**
 * 
 */
package com.app.base.service.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


/**
 * 
 * 组件元数据服务类。
 * activity、application、service、receiver组件在AndroidManifest.xml文件中元数据的读取。
 * 
 * @author tianyu912@yeah.net
 */
public class MetaDataGetService extends AbstractAndroidService {


	public MetaDataGetService(Activity activity) {
		super(activity);
	}

	public String getMetaDataFromActivity(String key) {

		ActivityInfo info = null;
		try {
			info = this.activity.getPackageManager().getActivityInfo(
					this.activity.getComponentName(),
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (info != null) {
			return info.metaData.getString(key);
		}
		return null;
	}

	public String getMetaDataFromApplication(String key) {

		ApplicationInfo info = null;
		try {
			info = this.activity.getPackageManager().getApplicationInfo(
					this.activity.getPackageName(),
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (info != null) {
			return info.metaData.getString(key);
		}
		return null;
	}

	public String getMetaDataFromService(String key) {

//		ServiceInfo info = null;
//		try {
//			ComponentName cn = new ComponentName(this.activity,
//					MetaDataService.class);
//			info = this.activity.getPackageManager().getServiceInfo(cn,
//					PackageManager.GET_META_DATA);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		if (info != null) {
//			return info.metaData.getString(key);
//		}

		return null;
	}

	public String getMetaDataFromReceiver(String key) {

//		ActivityInfo info = null;
//		try {
//			ComponentName cn = new ComponentName(this.activity,
//					MetaDataReceiver.class);
//			info = this.activity.getPackageManager().getReceiverInfo(cn,
//					PackageManager.GET_META_DATA);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		if (info != null) {
//			return info.metaData.getString(key);
//		}

		return null;
	}

}
