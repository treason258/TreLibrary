/**
 * 
 */
package com.app.base.service.android;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


/**
 * 
 * 等待对话框.对话框中有个imageView控件，间隔一段时间更新imageView的图片源，以形成动画的效果。
 * 
 * @author tianyu912@yeah.net
 * 
 */
public class WaittingAnimationService extends AbstractAndroidService {

	private AlertDialog dialog;

	/**
	 * 等待提示框的布局id.
	 */
	private int styleLayoutId;

	/**
	 * 动画资源文件。
	 */
	private Drawable[] images;

	private ImageView imageView;
	
	private int delayMillis;
	
	private int index = 0;

	private AtomicBoolean flag = new AtomicBoolean(false);


	/**
	 * 
	 * @param activity 上下文。
	 * @param imageDrawables 动画的资源文件。
	 * @param delayMillis 每个动画文件切换的时间间隔。
	 */
	public WaittingAnimationService(Activity activity, Drawable[] imageDrawables, int delayMillis) {
		
		super(activity);
		this.images = imageDrawables;
		this.delayMillis = delayMillis;
		//this.styleLayoutId = R.layout.waiting_animation;
	}

	/**
	 * 等待框，全屏透明背景
	 * 
	 */
	private void createWaitintDialog() {

//		dialog = new AlertDialog.Builder(this.activity,
//				android.R.style.Theme_Translucent_NoTitleBar).create();
//
//		dialog.show();
//		dialog.setContentView(styleLayoutId);
//
//
//		dialog.setCanceledOnTouchOutside(false);
		
	}

	/**
	 * 展示等待对话框
	 */
	public void showWaitingDialog() {

		if (null == dialog) {
			createWaitintDialog();
		} else if (!dialog.isShowing()) {
			dialog.show();
		}
		start();
	}

	/**
	 * 关闭等待对话框
	 */
	public void closeWaitingDialog() {
		if (null != dialog && dialog.isShowing()) {
			
			flag.set(false);
			dialog.cancel();
		}
	}

	private Handler imageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) { // 切换msg切换图片

			imageView.setImageDrawable(images[msg.what]);

			if (flag.get()) {
				
				this.sendEmptyMessageDelayed(index % images.length, delayMillis);
				index++;
			}
		}
	};

	private void start() {

		flag.set(true);
		imageHandler.sendEmptyMessage(0);
	}

}
