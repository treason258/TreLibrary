package com.haoyang.lovelyreader.tre;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.BookBean;

import java.util.List;

/**
 * Created by xin on 18/9/22.
 */

public class HomeAdapter extends BaseAdapter {

    private Context mContext;
    private List<BookBean> mList;

    public HomeAdapter(Context context, List<BookBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home, null);
            viewHolder = new ViewHolder();
            viewHolder.findView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList != null && mList.get(position) != null) {
            viewHolder.initView(mList.get(position));
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView ivBook;
        private TextView tvBook;

        private void findView(View view) {
            ivBook = (ImageView) view.findViewById(R.id.ivBook);
            tvBook = (TextView) view.findViewById(R.id.tvBook);
        }

        private void initView(BookBean bookBean) {
            if (!TextUtils.isEmpty(bookBean.getName())) {
                tvBook.setText(bookBean.getName());
            }
        }
    }
}
