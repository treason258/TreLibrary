package com.haoyang.lovelyreader.tre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;

/**
 * Created by xin on 18/9/26.
 */

public class FeedbackActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // findViewById
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        // tvTitle
        tvTitle.setText("意见反馈");
        // ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
