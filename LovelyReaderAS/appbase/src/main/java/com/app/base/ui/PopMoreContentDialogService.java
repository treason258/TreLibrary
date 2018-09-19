/**
 * 
 */
package com.app.base.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.app.base.service.android.AbstractAndroidService;
import com.app.base.service.android.ViewService;

/**
 * 
 * 显示内容可以根据指定布局来设定的弹框. 这个类主要是构建弹出框界面，并把指定id的view控件放到map中。
 * 
 * @author tianyu912@yeah.net
 */
public class PopMoreContentDialogService extends AbstractAndroidService {

	/**
	 * 当前弹出框对象。
	 */
	private AlertDialog appUpdateNoticeDlg;

	/**
	 * 需要使用这个弹出框中组件的时候，通过id到这个map中取。
	 */
	private Map<Integer, View> viewMap;

	/**
	 * 当前弹出框的布局。
	 */
	private View layoutView;

	private int layoutId;
	private int[] ids;
	private View.OnClickListener clickListener;

	/**
	 * 
	 * @param activity 当前上下文。
	 * @param layoutId 指定的布局id
	 * @param ids 需要用到的布局中view的id集合，并对这些view做click事件监听的。
	 * @param clickListener 对view做click监听的事件。
	 */
	public PopMoreContentDialogService(Activity activity, int layoutId,
			int[] ids, View.OnClickListener clickListener) {
		super(activity);
		this.layoutId = layoutId;
		this.ids = ids;
		this.clickListener = clickListener;

	}

	/**
	 * 显示对话框。
	 */
	public void show() {

		appUpdateNoticeDlg = new AlertDialog.Builder(activity).create();
		appUpdateNoticeDlg.show();

		LayoutInflater factory = LayoutInflater.from(activity);
		layoutView = factory.inflate(layoutId, null);

		ViewService viewService = new ViewService(layoutView);
		viewMap = new HashMap<Integer, View>();
		for (Integer id : ids) {

			View v = viewService.getViewById(id, clickListener);
			this.viewMap.put(id, v);
		}

		appUpdateNoticeDlg.getWindow().setContentView(layoutView);

	}

	/**
	 * 销毁对话框。
	 * 
	 * dismiss方法和hide的区别： dismiss方法会释放对话框占用的资源, hide方法只是隐藏对话框，不释放资源
	 * 
	 */
	public void dismiss() {

		appUpdateNoticeDlg.dismiss();

	}

	/**
	 * 获取当前弹出框中的子视图控件。
	 * 
	 * @param id
	 *            子视图控件的id.
	 * @return
	 */
	public View getView(Integer id) {

		if (this.viewMap != null) {
			return this.viewMap.get(id);
		}
		return null;
	}

	/**
	 * 获取当前弹出框的view.
	 * 
	 * @return the layoutView
	 */
	public View getLayoutView() {
		return layoutView;
	}

}
