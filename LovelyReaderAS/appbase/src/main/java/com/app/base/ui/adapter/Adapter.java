package com.app.base.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;


/**
 * 
 * 通用的适配器
 * 
 * @author tianyu
 *
 *         email:xjzx_tianyu@yeah.net<br>
 *         Date :2014年6月19日
 * @param <T>
 */
public class Adapter<T> extends BaseListAdapter<T> implements OnScrollListener {

	private boolean isBusy = false;
	private DataPublisher dataPublisher;

	public Adapter(Activity activity, List<T> data, int totalPage,
			LinearLayout footerView) {
		super(activity, data, totalPage, footerView);
	}
	 
	public View getRowView(int position, View convertView, ViewGroup parent) {

		if (this.dataPublisher == null) {
			return convertView;
		}

		View publishToView = this.dataPublisher.publishToView(position,
				convertView, this);
		return publishToView;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {

		case OnScrollListener.SCROLL_STATE_IDLE:// 也就是ListView不滚动时，
			if (isBusy) {
				//notifyDataSetChanged();
				isBusy = false;
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			isBusy = false;
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			isBusy = true;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	public void setDataPublisher(DataPublisher dataPublisher) {
		this.dataPublisher = dataPublisher;
	}
}
