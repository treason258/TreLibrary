package com.mjiayou.trecore.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mjiayou.trecore.R;
import com.mjiayou.trecore.base.TCDialog;
import com.mjiayou.trecore.common.Caches;
import com.mjiayou.trecore.util.ViewUtil;
import com.mjiayou.trecorelib.bean.entity.TCUser;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by treason on 16/7/12.
 */
public class TCUserListDialog extends TCDialog {

    protected static final String TAG = "TCUserListDialog";
    private static TCUserListDialog mTCUserListDialog;

    // root
    private ViewGroup mViewRoot;
    // dialog
    private LinearLayout mLayoutDialog;
    // title
    private ImageView mIvBack;
    private TextView mTvTitle;
    private ListView mLvUser;

    private static String mUserId = "";
    private List<TCUser> mListUser = new ArrayList<>();

    public TCUserListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TCUserListDialog(Context context) {
        this(context, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_dialog_user_list);

        // findViewById
        // root
        mViewRoot = (ViewGroup) findViewById(R.id.view_root);
        // dialog
        mLayoutDialog = (LinearLayout) findViewById(R.id.layout_dialog);
        // title
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        // list
        mLvUser = (ListView) findViewById(R.id.lv_user);

        // 窗口宽度高度
        if (null != mViewRoot) {
            int width = Caches.get().getScreenWidth(WIDTH_RATIO_DEFAULT);
            int height = Caches.get().getScreenHeight(WIDTH_RATIO_DEFAULT);
            ViewUtil.setWidthAndHeight(mViewRoot, width, height);
        }

        try {
            // mIvBack
            mIvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(mContext, "mIvBack - onClick");
                    dismissDialog();
                }
            });
            // mLvUser
            mLvUser.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, new String[]{"111", "222", "333"}));
            mLvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ToastUtil.show(mContext, String.valueOf(parent.getAdapter().getItem(position)));
                }
            });
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }

    /**
     * 设置账号
     */
    public static void setUserId(String userId) {
        mUserId = userId;
    }

    // ******************************** TCUserListDialog ********************************

    /**
     * 创建 TCUserListDialog
     */

    public static TCUserListDialog createDialog(Context context, String userId) {
        setUserId(userId);

        boolean needCreate = false;

        // 当 mTCUserListDialog 为 null 时，需要重新创建
        if (null == mTCUserListDialog) {
            needCreate = true;
        }

        // 当 mTCUserListDialog 不为 null，但是 getContext 不是 context 时
        if (null != mTCUserListDialog && context != mTCUserListDialog.getContext()) {
            LogUtil.w(TAG, "此处引发窗体泄露 | origin context -> " + mTCUserListDialog.getContext() + " | now context -> " + context);
            dismissDialog();
            needCreate = true;
        }

        // 根据需要重新创建
        if (needCreate) {
            mTCUserListDialog = new TCUserListDialog(context);
        }

        return mTCUserListDialog;
    }

    /**
     * 释放 TCUserListDialog
     */
    public static void dismissDialog() {
        if (null == mTCUserListDialog) {
            return;
        }

        if (mTCUserListDialog.isShowing()) {
            try {
                mTCUserListDialog.dismiss();
                mTCUserListDialog = null;
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
            }
        }
    }
}
