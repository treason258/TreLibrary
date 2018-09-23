package com.haoyang.lovelyreader.tre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.UserLoginRequest;
import com.haoyang.lovelyreader.tre.bean.UserLoginResponse;
import com.haoyang.lovelyreader.tre.config.UrlConfig;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.http.okhttp.RequestEntity;
import com.mjiayou.trecorelib.util.ToastUtils;

/**
 * Created by xin on 18/9/22.
 */

public class LoginActivity extends BaseActivity {

    private EditText etPhone;
    private EditText etPassword;
    private TextView tvLogin;
    private TextView tvFindPwd;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        // findViewById
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvFindPwd = (TextView) findViewById(R.id.tvFindPwd);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        // tvLogin
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show("请输入密码");
                    return;
                }

                UserLoginRequest userLoginRequestBean = new UserLoginRequest();
                userLoginRequestBean.setPhone(phone);
                userLoginRequestBean.setPwd(password);

                RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUserLogin);
                requestEntity.setRequestBody(GsonHelper.get().toJson(userLoginRequestBean));
                RequestBuilder.get().send(requestEntity, new RequestCallback<UserLoginResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int code, UserLoginResponse bean) {
                        if (bean != null) {
                            ToastUtils.show(bean.getToken());
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.show(msg);
                    }
                });
            }
        });
        // tvFindPwd
        tvFindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FindPwdActivity.class));
            }
        });
        // tvRegister
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });
    }
}
