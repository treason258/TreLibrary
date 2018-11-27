package com.haoyang.lovelyreader.tre.ui;

import android.os.Bundle;
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
import com.haoyang.lovelyreader.tre.bean.api.FeedbackAddParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.mjiayou.trecorelib.http.RequestSender;
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
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
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
    public void initView() {
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

        // etPhone
        if (UserUtils.checkLoginStatus()) {
            UserBean userBean = DBHelper.getUserBean();
            if (userBean != null && !TextUtils.isEmpty(userBean.getPhone())) {
                etPhone.setText(userBean.getPhone());
            }
        }

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
                if (!Utils.isMobileNO(phone)) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }

                UserBean userBean = DBHelper.getUserBean();

                FeedbackAddParam feedbackAddParam = new FeedbackAddParam();
                feedbackAddParam.setImgList(new ArrayList<String>());
                feedbackAddParam.setPhone(phone);
                feedbackAddParam.setProblemDesc(problem);
                feedbackAddParam.setUid(userBean.getUid());
                feedbackAddParam.setUserName(userBean.getNickName());

                MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiFeedbackAdd);
                myRequestEntity.setContentWithHeader(ApiRequest.getContent(feedbackAddParam));
                RequestSender.get().send(myRequestEntity, new RequestCallback<Object>() {
                    @Override
                    public void onStart() {
                        showLoading(true);
                    }

                    @Override
                    public void onSuccess(int code, Object object) {
                        showLoading(false);
                        ToastUtils.show("提交成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        showLoading(false);
                        ToastUtils.show(msg);
                    }
                });
            }
        });
    }
}
