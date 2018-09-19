/**
 * 
 */
package com.app.base.service.android;

import android.app.Activity;


/**
 * 
 * 
 * 版本比较服务。
 * 
 * @author tianyu912@yeah.net
 */
public class VersionCompareService extends AbstractAndroidService {

	public VersionCompareService(Activity activity) {
		super(activity);
	}

	/**
	 * 比较服务器端版本和本地的版本，
	 * 
	 * @param lastestVersion
	 *            给定的版本号。
	 * @return false：本地的版本高或者与给定的版本号一样;true: 给定的版本高。
	 */
	public boolean compare(String lastestVersion) {

		if (lastestVersion == null) { // 给定的版本号失败的时候，
			return false;
		}
		int version = -1;
		try {
			version = Integer.parseInt(lastestVersion);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}

		AndroidAppService androidAppService = new AndroidAppService(this.activity);
		int localVersion = androidAppService.getVersionCode();

		if (localVersion == -1) { // 取版本号失败的时候，

			return false;
		}

		if (localVersion >= version) {
			return false;
		}

		return true;

	}

}
