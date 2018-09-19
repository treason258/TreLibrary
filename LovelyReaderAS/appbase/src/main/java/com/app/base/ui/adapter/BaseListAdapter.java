package com.app.base.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.app.base.ui.UILayoutService;

/**
 * ListView使用的Adapter的基类，理论上所有的ListView Adapter都继承这个Adapter.
 * 
 * @author Tianyu<br>
 *
 * email:xjzx_tianyu@yeah.net<br>
 * date :2014年6月19日
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

	//private final String TAG = this.getClass().getName();

	protected Activity context;
	protected List<T> data;
	protected int totalPage = 1;
	protected int currentPage = 1;
	protected LinearLayout footerView;
	protected int moreLayoutID = -1, endLayoutID = -1;
	public int widthLimit;

	/**
	 * 构造方法.
	 * 
	 * @param activity
	 * @param data
	 *            数据
	 * @param totalPage
	 *            总页数，不分页传1
	 * @param footerView
	 *            列表脚布局对象
	 * @param moreLayout
	 *            列表脚布局对象
	 * @param endLayout
	 *            列表脚布局对象
	 */
	public BaseListAdapter(Activity activity, List<T> data, int totalPage,
			LinearLayout footerView, int moreLayout, int endLayout) {
		this(activity, data, totalPage, footerView);
		this.moreLayoutID = moreLayout;
		this.endLayoutID = endLayout;
	}

	public BaseListAdapter(Activity activity, List<T> data, int totalPage,
			LinearLayout footerView) {
		this(activity, data, totalPage);
		this.footerView = footerView;
	}

	public BaseListAdapter(Activity activity, List<T> data, int totalPage) {
		this.context = activity;
		this.data = data;
		this.totalPage = totalPage;
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		if (null != data && data.size() > 0)
			return data.size();
		else
			return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getRowView(position, convertView, parent);
	}
//
//	private View getFootView(LinearLayout.LayoutParams layoutParams, int layoutId) {
//		View moreView = UILayoutService.getViewFromLayout(context, layoutId);
//		footerView.addView(moreView, layoutParams);
//		return footerView;
//	}

	/**
	 * 获取每一行显示的内容.(子类实现)
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getRowView(int position, View convertView,
			ViewGroup parent);

}
