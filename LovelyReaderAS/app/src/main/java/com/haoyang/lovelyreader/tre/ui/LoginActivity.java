package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.UserLoginParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.callback.RequestCallback;
import com.mjiayou.trecorelib.util.SharedUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

/**
 * Created by xin on 18/9/22.
 */

public class LoginActivity extends BaseActivity {

    private ImageView ivBack;
    private EditText etPhone;
    private EditText etPassword;
    private TextView tvLogin;
    private TextView tvFindPwd;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // findViewById
        ivBack = (ImageView) findViewById(R.id.ivBack);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvFindPwd = (TextView) findViewById(R.id.tvFindPwd);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        // 如果保存登录信息，则自动填充
        String lastUsername = SharedUtils.get().getAccountUsername();
        String lastPassword = SharedUtils.get().getAccountPassword();
        if (!TextUtils.isEmpty(lastUsername)) {
            etPhone.setText(lastUsername);
        }
        if (!TextUtils.isEmpty(lastPassword)) {
            etPassword.setText(lastPassword);
        }

        // ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // tvLogin
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = etPhone.getText().toString();
                final String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show("请输入密码");
                    return;
                }

                UserLoginParam userLoginParamBean = new UserLoginParam();
                userLoginParamBean.setPhone(phone);
                userLoginParamBean.setPwd(password);
                String content = ApiRequest.getContent(userLoginParamBean);

                MyRequestEntity requestEntity = new MyRequestEntity(UrlConfig.apiUserLogin);
                requestEntity.setContentWithHeader(content);
                RequestSender.get().send(requestEntity, new RequestCallback<UserBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int code, UserBean bean) {
                        if (bean != null) {
                            ToastUtils.show("登录成功");

                            // 保存登录的用户名和密码，下次自动填充
                            SharedUtils.get().setAccountUsername(phone);
                            SharedUtils.get().setAccountPassword(password);
                            // 保存用户信息
                            DBHelper.setUserBean(bean);
                            // 同步游客书籍
                            DBHelper.syncGuestBook();
                            // 通知登录成功
                            UserUtils.doLogin(bean.getToken());

                            // 页面跳转
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

        // tvRegister
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mContext, RegisterActivity.class);
                mIntent.putExtra(RegisterActivity.EXTRA_PAGE_TYPE, RegisterActivity.PAGE_TYPE_REGISTER);
                startActivity(mIntent);
            }
        });

        // tvFindPwd
        tvFindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mContext, RegisterActivity.class);
                mIntent.putExtra(RegisterActivity.EXTRA_PAGE_TYPE, RegisterActivity.PAGE_TYPE_FIND_PWD);
                startActivity(mIntent);
            }
        });
    }
}
