/**
 * 
 */
package com.app.base.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * @author Administrator
 *
 */
public abstract class AbstractDataPublisher implements DataPublisher {

	private Activity activity;

	/**
	 * 显示数据的布局id.
	 */
	private Integer rowLayoutId;

	/**
	 * 布局中显示行号的viewID。
	 */
	private Integer numberId;

	private View.OnClickListener clickListener;

	/**
	 * 用来标识是否执行过findViewById，如果执行过就不在执行了。
	 */
	protected boolean isFoundViewById;

	public AbstractDataPublisher(Integer rowLayoutId, Integer numberId,
			Activity activity) {
		super();
		this.rowLayoutId = rowLayoutId;
		this.activity = activity;
		if (numberId == null) {
			this.numberId = -1;
		} else {
			this.numberId = numberId;
		}
	}

	@Override
	public View publishToView(int position, View view, Adapter adapter) {

		if (this.rowLayoutId == null) {
			return view;
		}

		View rowView = view;

		ViewHolder viewHolder;
		if (null == rowView) {
			LayoutInflater inflater = activity.getLayoutInflater();
			viewHolder = new ViewHolder();
			rowView = inflater.inflate(this.rowLayoutId, null);
			if (this.numberId != -1) {
				viewHolder.number = (TextView) rowView
						.findViewById(this.numberId);
			}
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}

		if (viewHolder.number != null) {
			viewHolder.number.setText(String.valueOf(position + 1));
		}
		this.publish(position, rowView, adapter, viewHolder);

		return rowView;
	}

	public void setClickListenerForView(View view) {
		view.setOnClickListener(this.clickListener);
	}

	/**
	 * @param clickListener
	 *            the clickListener to set
	 */
	public void setClickListener(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public abstract void publish(int position, View view, Adapter adapter,
			ViewHolder viewHolder);

}
