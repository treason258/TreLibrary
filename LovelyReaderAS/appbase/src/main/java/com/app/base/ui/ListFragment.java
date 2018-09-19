/**
 * 
 */
package com.app.base.ui;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.app.base.ui.adapter.Adapter;

/**
 * @author tianyu
 * 
 *         email:xjzx_tianyu@yeah.net<br>
 *         Date :2014年6月18日
 */
public class ListFragment extends Fragment {

	/**
	 * 给当前fragment传递布局文件id参数用的key.
	 */
	public final static String layoutId = "layoutId";

	/**
	 * 给当前fragment传递显示内容的ViewId参数用的key.
	 */
	public final static String contentViewId = "contentViewId";

	/**
	 * 给当前fragment传递按钮id的参数key. 比如多个页面的布局差不多，只是显示的按 钮不一样，这样我们就做一个布局文件，里
	 * 面定义所有的按钮。然后不同的fragment 对象里那些按钮显示由这个参数的值来决定。
	 */
	public final static String buttonIds = "buttonIds";

	/**
	 * 基本功能和buttonIds差不多，但是他是用来指定按钮上显示的文字的。
	 */
	public final static String buttonTexts = "buttonTexts";

	private Adapter adapter;
	private View.OnClickListener clickListener;

	private AbsListView contentView;

	private Map<Integer, TextView> textViews = new HashMap<Integer, TextView>();

	/**
	 * @param clickListener
	 */
	public ListFragment() {
		super();
	}

	/**
	 * 覆盖此函数，先通过inflater inflate函数得到view最后返回
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = this.getArguments();
		if (bundle == null) {
			return new View(this.getActivity());
		}
		int layoutId = bundle.getInt(ListFragment.layoutId);
		int contentViewId = bundle.getInt(ListFragment.contentViewId);
		int[] ids = bundle.getIntArray(ListFragment.buttonIds);
		String[] buttonTexts = bundle.getStringArray(ListFragment.buttonTexts);
		View view = inflater.inflate(layoutId, container, false);
		contentView = (AbsListView) view.findViewById(contentViewId);
		if (contentView != null) {
			//contentView.setAdapter(this.getAdapter());
		}
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				TextView textView = (TextView) view.findViewById(ids[i]);
				if (textView != null) {
					textView.setVisibility(View.VISIBLE);
					textView.setOnClickListener(clickListener);
					if (buttonTexts != null && i < buttonTexts.length) {
						
						textView.setText(buttonTexts[i]);
					}
					textViews.put(ids[i], textView);
				}
			} // end for i
		}
		view.setOnClickListener(clickListener);
		/*
		View noData = this.getViewById(R.id.noData);
		if (noData != null) {
			noData.setVisibility(View.INVISIBLE);
		}
*/
		return view;
	}

	public Adapter getAdapter() {
		return adapter;
	}

	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * @param clickListener
	 *            the clickListener to set
	 */
	public void setClickListener(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public AbsListView getContentView() {
		return contentView;
	}
	
	/**
	 * 根据指定View的id返回对应的View对象.
	 * @param id
	 * @return
	 */
	public View getViewById(int id) {
		
		return this.textViews.get(id);
	}
}
