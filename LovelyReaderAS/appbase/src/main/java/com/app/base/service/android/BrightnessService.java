/**
 * 
 */
package com.app.base.service.android;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

/**
 * @author tianyu
 *
 */
public class BrightnessService extends AbstractAndroidService {

	public BrightnessService(Activity activity) {
		super(activity);
	}

	/**
	 * 判断是否开启了自动亮度调节
	 * 
	 * @param aContext
	 * @return
	 */
	public boolean isAutoBrightness(ContentResolver aContentResolver) {
		boolean automicBrightness = false;
		try {
			automicBrightness = Settings.System.getInt(aContentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return automicBrightness;
	}

	/**
	 * 获取屏幕的亮度
	 * 
	 * @param activity
	 * @return
	 */
	public int getScreenBrightness() {
		int nowBrightnessValue = 0;
		ContentResolver resolver = super.activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/**
	 * 设置亮度
	 * 
	 * @param activity
	 * @param brightness
	 */
	public void setBrightness(float brightness) {

		WindowManager.LayoutParams lp = super.activity.getWindow().getAttributes();
		lp.screenBrightness = brightness * (1f / 255f);
		super.activity.getWindow().setAttributes(lp);
	}

	/**
	 * 停止自动亮度调节
	 * 
	 * @param activity
	 */
	public void stopAutoBrightness() {
		Settings.System.putInt(super.activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * 开启亮度自动调节
	 * 
	 * @param activity
	 */
	public void startAutoBrightness() {
		
		final WindowManager.LayoutParams attrs = this.activity.getWindow().getAttributes();
		attrs.screenBrightness = -1.0f;
		this.activity.getWindow().setAttributes(attrs);
		
//		
//		Settings.System.putInt(super.activity.getContentResolver(),
//				Settings.System.SCREEN_BRIGHTNESS_MODE,
//				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
}
