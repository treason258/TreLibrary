package com.haoyang.lovelyreader.tre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonData;
import com.haoyang.lovelyreader.tre.bean.api.FeedbackAddParam;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.util.ArrayList;

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
        feedbackAdd();
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

    /**
     * 意见反馈
     */
    private void feedbackAdd() {
        FeedbackAddParam feedbackAddParam = new FeedbackAddParam();
        feedbackAddParam.setImgList(new ArrayList<String>());
        feedbackAddParam.setPhone("");
        feedbackAddParam.setProblemDesc("");
        feedbackAddParam.setUid("");
        feedbackAddParam.setUserName("");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setCommonData(CommonData.get());
        apiRequest.setParam(feedbackAddParam);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiFeedbackAdd);
        requestEntity.setMethod(RequestMethod.POST_STRING);
        requestEntity.setContent(apiRequest);
        RequestBuilder.get().send(requestEntity, new RequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int code, String object) {

            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(msg);
            }
        });
    }
}
