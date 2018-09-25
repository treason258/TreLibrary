package com.haoyang.lovelyreader.tre;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.mjiayou.trecorelib.base.TCAdapter;
import com.mjiayou.trecorelib.base.TCViewHolder;

import java.util.List;

/**
 * Created by xin on 18/9/22.
 */

public class HomeAdapter extends TCAdapter {

    private Context mContext;
    private List<BookBean> mList;
    private LayoutInflater mLayoutInflater;

    HomeAdapter(Context context, List<BookBean> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
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
            convertView = mLayoutInflater.inflate(R.layout.item_home, null);
            viewHolder = new ViewHolder();
            viewHolder.findView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList != null && mList.size() > position && mList.get(position) != null) {
            viewHolder.initView(mList.get(position));
        }
        return convertView;
    }

    private class ViewHolder extends TCViewHolder<BookBean> {
        private ImageView ivBook;
        private TextView tvBook;

        @Override
        protected void findView(View view) {
            ivBook = (ImageView) view.findViewById(R.id.ivBook);
            tvBook = (TextView) view.findViewById(R.id.tvBook);
        }

        @Override
        protected void initView(BookBean bean) {
            if (bean != null) {
                // ivBook
                if (!TextUtils.isEmpty(bean.getCover())) {
                    Bitmap bitmap = BitmapFactory.decodeFile(bean.getCover());
                    if (bitmap != null) {
                        ivBook.setImageBitmap(bitmap);
                    }
                } else {
                    ivBook.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
                }
                // tvBook
                if (!TextUtils.isEmpty(bean.getName())) {
                    tvBook.setText(bean.getName());
                }
            }
        }
    }
}
