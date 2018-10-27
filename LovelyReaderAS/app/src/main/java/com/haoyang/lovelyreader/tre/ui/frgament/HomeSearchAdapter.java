package com.haoyang.lovelyreader.tre.ui.frgament;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.mjiayou.trecorelib.base.TCAdapter;
import com.mjiayou.trecorelib.base.TCViewHolder;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.util.List;

/**
 * Created by treason on 2018/9/27.
 */

public class HomeSearchAdapter extends TCAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater mLayoutInflater;

    HomeSearchAdapter(Context context, List<String> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_home_search, null);
            viewHolder = new ViewHolder();
            viewHolder.findView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList != null && mList.size() > position && mList.get(position) != null) {
            viewHolder.initView(mList.get(position), position);
        }
        return convertView;
    }

    private class ViewHolder extends TCViewHolder<String> {
        private TextView tvHistory;
        private ImageView ivDelete;

        @Override
        protected void findView(View view) {
            tvHistory = (TextView) view.findViewById(R.id.tvHistory);
            ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        }

        @Override
        protected void initView(String bean, int position) {
            if (bean != null) {
                // tvHistory
                tvHistory.setText(bean);
                // ivDelete
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show("删除");
                    }
                });
            }
        }
    }
}
