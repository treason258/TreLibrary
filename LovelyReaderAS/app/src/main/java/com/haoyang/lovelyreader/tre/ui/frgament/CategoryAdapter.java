package com.haoyang.lovelyreader.tre.ui.frgament;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.mjiayou.trecorelib.base.TCAdapter;
import com.mjiayou.trecorelib.base.TCViewHolder;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.util.List;

/**
 * Created by xin on 18/11/5.
 */

public class CategoryAdapter extends TCAdapter {

    private Context mContext;
    private List<CategoryBean> mList;
    private LayoutInflater mLayoutInflater;

    public CategoryAdapter(Context context, List<CategoryBean> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_category, null);
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

    private class ViewHolder extends TCViewHolder<CategoryBean> {
        private LinearLayout llBackground;
        private ImageView ivIcon;
        private TextView tvCategoryL1;
        private TextView tvCategoryL2;
        private TextView tvCategoryL3;
        private ImageView ivModify;
        private ImageView ivDelete;

        @Override
        protected void findView(View view) {
            llBackground = (LinearLayout) view.findViewById(R.id.llBackground);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            tvCategoryL1 = (TextView) view.findViewById(R.id.tvCategoryL1);
            tvCategoryL2 = (TextView) view.findViewById(R.id.tvCategoryL2);
            tvCategoryL3 = (TextView) view.findViewById(R.id.tvCategoryL3);
            ivModify = (ImageView) view.findViewById(R.id.ivModify);
            ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        }

        @Override
        protected void initView(CategoryBean categoryBean, int position) {
            // ivIcon,tvCategoryL1,tvCategoryL2,tvCategoryL3
            tvCategoryL1.setVisibility(View.GONE);
            tvCategoryL2.setVisibility(View.GONE);
            tvCategoryL3.setVisibility(View.GONE);
            switch (categoryBean.getLevel()) {
                default:
                case CategoryBean.LEVEL_1:
                    ivIcon.setVisibility(View.VISIBLE);
                    tvCategoryL1.setText(categoryBean.getCategoryName());
                    tvCategoryL1.setVisibility(View.VISIBLE);
                    break;
                case CategoryBean.LEVEL_2:
                    ivIcon.setVisibility(View.INVISIBLE);
                    tvCategoryL2.setText(categoryBean.getCategoryName());
                    tvCategoryL2.setVisibility(View.VISIBLE);
                    break;
                case CategoryBean.LEVEL_3:
                    ivIcon.setVisibility(View.INVISIBLE);
                    tvCategoryL3.setText(categoryBean.getCategoryName());
                    tvCategoryL3.setVisibility(View.VISIBLE);
                    break;
            }

            // ivModify
            ivModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show("编辑 | " + categoryBean.getCategoryId() + " | "+ categoryBean.getCategoryName());
                }
            });

            // ivDelete
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show("删除 | " + categoryBean.getCategoryId() + " | "+ categoryBean.getCategoryName());
                }
            });

            // 选中状态
            if(categoryBean.isSelected()) {
                llBackground.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tc_shape_rect_stroke_gray_corners));
                ivModify.setVisibility(View.VISIBLE);
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                llBackground.setBackgroundDrawable(null);
                ivModify.setVisibility(View.INVISIBLE);
                ivDelete.setVisibility(View.INVISIBLE);
            }
        }
    }
}