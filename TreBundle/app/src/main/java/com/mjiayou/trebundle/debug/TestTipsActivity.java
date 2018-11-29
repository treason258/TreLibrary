package com.mjiayou.trebundle.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mjiayou.trebundle.R;
import com.mjiayou.trecore.base.TCActivity;
import com.mjiayou.trecore.util.SpannableStringUtil;

import butterknife.InjectView;

public class TestTipsActivity extends TCActivity {

    @InjectView(R.id.et_account)
    EditText mEtAcctount;
    @InjectView(R.id.et_password)
    EditText mEtPassword;
    @InjectView(R.id.tv_show)
    TextView mTvShow;

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, TestTipsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_tips;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTitleBar().setTitle("TestTipsActivity");

        initView();
    }

    @Override
    public void initView() {
        super.initView();

        mTvShow.setText(SpannableStringUtil.getDemo(mContext));
        mTvShow.setMovementMethod(LinkMovementMethod.getInstance());
        mTvShow.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 关闭硬件加速

        mEtAcctount.setText(SpannableStringUtil.getDemo(mContext));
        mEtAcctount.setMovementMethod(LinkMovementMethod.getInstance());
        mEtAcctount.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 关闭硬件加速
    }
}
