package com.haoyang.lovelyreader.tre.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.FileBean;
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.base.TCViewHolder;

import java.util.List;

/**
 * Created by xin on 18/9/25.
 */

public class FileAdapter extends BaseAdapter {

    private Context mContext;
    private List<FileBean> mList;
    private LayoutInflater mLayoutInflater;
    private FileNameService mFileNameService;

    public FileAdapter(Context context, List<FileBean> bookList) {
        mContext = context;
        mList = bookList;
        mLayoutInflater = LayoutInflater.from(context);
        mFileNameService = new FileNameService();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_file, null);
            viewHolder = new ViewHolder();
            viewHolder.findView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList != null && mList.get(position) != null) {
            viewHolder.initView(mList.get(position), position);
        }
        return convertView;
    }

    private class ViewHolder extends TCViewHolder<FileBean> {
        private ImageView ivFile;
        private ImageView ivSelect;
        private TextView tvFile;

        @Override
        protected void findView(View view) {
            ivFile = (ImageView) view.findViewById(R.id.ivFile);
            ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
            tvFile = (TextView) view.findViewById(R.id.tvFile);
        }

        @Override
        protected void initView(FileBean bean, int position) {
            // ivFile
            if (bean.isFolder()) { // 是文件夹
                ivFile.setVisibility(View.VISIBLE);
                ivSelect.setVisibility(View.GONE);
            } else {
                ivFile.setVisibility(View.GONE);
                ivSelect.setVisibility(View.VISIBLE);
                if (bean.isSelected()) {
                    ivSelect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_file_selected));
                } else {
                    ivSelect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_file_unselected));
                }
            }
            // tvFile
            if (!TextUtils.isEmpty(bean.getPath())) {
                tvFile.setText(mFileNameService.getFileNameFromAddress(bean.getPath()));
            }
        }
    }

    /**
     * getList
     */
    public List<FileBean> getList() {
        return mList;
    }

    /**
     * setList
     */
    public void setList(List<FileBean> list) {
        mList = list;
        notifyDataSetChanged();
    }
}