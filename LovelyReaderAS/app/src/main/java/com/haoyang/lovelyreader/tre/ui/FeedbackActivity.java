package com.haoyang.lovelyreader.tre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonData;
import com.haoyang.lovelyreader.tre.bean.api.FeedbackAddParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

import java.util.ArrayList;

/**
 * Created by xin on 18/9/26.
 */

public class FeedbackActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private TextView tvTitle;
    private ImageView ivBack;

    private EditText etProblem;
    private EditText etPhone;
    private TextView tvSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // findViewById
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        etProblem = (EditText) findViewById(R.id.etProblem);
        etPhone = (EditText) findViewById(R.id.etPhone);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);

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

        // tvSubmit
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String problem = etProblem.getText().toString();
                final String phone = etPhone.getText().toString();

                if (TextUtils.isEmpty(problem)) {
                    ToastUtils.show("请描述您的问题");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }

                UserBean userBean = DBHelper.getUserBean();
                FeedbackAddParam feedbackAddParam = new FeedbackAddParam();
                feedbackAddParam.setImgList(new ArrayList<String>());
                feedbackAddParam.setPhone(phone);
                feedbackAddParam.setProblemDesc(problem);
                feedbackAddParam.setUid(userBean.getUid());
                feedbackAddParam.setUserName(userBean.getNickName());
                ApiRequest apiRequest = new ApiRequest();
                apiRequest.setCommonData(CommonData.get());
                apiRequest.setParam(feedbackAddParam);
                String content = JsonParser.get().toJson(apiRequest);

                RequestEntity requestEntity = new RequestEntity(UrlConfig.apiFeedbackAdd);
                requestEntity.setMethod(RequestMethod.POST_STRING);
                requestEntity.setContent(content);
                requestEntity.addHeader("token", UserUtils.getToken());
                requestEntity.addHeader("sign", EncodeHelper.getSign(content));
                RequestBuilder.get().send(requestEntity, new RequestCallback<Object>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(int code, Object object) {
                        ToastUtils.show("提交成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.show(msg);
                    }
                });
            }
        });
    }
}
