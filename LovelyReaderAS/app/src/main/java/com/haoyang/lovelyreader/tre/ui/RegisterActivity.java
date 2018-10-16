package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.haoyang.lovelyreader.tre.bean.api.CommonData;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.bean.api.UserRegisterParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.util.TokenUtils;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.SharedUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

/**
 * Created by xin on 18/9/22.
 */

public class RegisterActivity extends BaseActivity {

    public static final String EXTRA_PAGE_TYPE = "extra_page_type";

    public static final int PAGE_TYPE_REGISTER = 0;
    public static final int PAGE_TYPE_FIND_PWD = 1;
    private int mPageType = PAGE_TYPE_REGISTER;

    private ImageView ivBack;
    private EditText etNickname;
    private EditText etPhone;
    private EditText etCode;
    private TextView tvCode;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private TextView tvSubmit;
    private TextView tvProtocol;

    private boolean isCountting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // findViewById
        ivBack = (ImageView) findViewById(R.id.ivBack);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCode = (EditText) findViewById(R.id.etCode);
        tvCode = (TextView) findViewById(R.id.tvCode);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvProtocol = (TextView) findViewById(R.id.tvProtocol);

        // mPageType
        if (getIntent() != null) {
            mPageType = getIntent().getIntExtra(EXTRA_PAGE_TYPE, PAGE_TYPE_REGISTER);
        }

        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        switch (mPageType) {
            case PAGE_TYPE_REGISTER: // 注册
                tvSubmit.setText("注册");
                etNickname.setVisibility(View.VISIBLE);
                tvProtocol.setVisibility(View.VISIBLE);
                tvProtocol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show("协议");
                    }
                });
                break;
            case PAGE_TYPE_FIND_PWD: // 找回密码
                tvSubmit.setText("确定");
                etNickname.setVisibility(View.GONE);
                tvProtocol.setVisibility(View.GONE);
                break;
        }

        // ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // tvCode
        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = etPhone.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }

                TokenUtils.getTempToken(new TokenUtils.OnGetTempTokenListener() {
                    @Override
                    public void onGetTempToken(String tempToken) {
                        CommonParam commonParam = new CommonParam();
                        commonParam.setData(phone);
                        ApiRequest apiRequest = new ApiRequest();
                        apiRequest.setCommonData(CommonData.get());
                        apiRequest.setParam(commonParam);
                        String content = JsonParser.get().toJson(apiRequest);

                        String url = UrlConfig.apiSmsSendrigstersms;
                        if (mPageType == PAGE_TYPE_FIND_PWD) { // 找回密码
                            url = UrlConfig.apiSmsSendchangepwdsms;
                        }
                        RequestEntity requestEntity = new RequestEntity(url);
                        requestEntity.setMethod(RequestMethod.POST_STRING);
                        requestEntity.setContent(content);
                        requestEntity.addHeader("token", tempToken);
                        requestEntity.addHeader("sign", EncodeHelper.getSign(content));
                        RequestBuilder.get().send(requestEntity, new RequestCallback<Object>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(int code, Object object) {
                                ToastUtils.show("验证码发送成功");
                                if (isCountting) { // 防止多次点击
                                    return;
                                }
                                isCountting = true;
                                mCountDownTimer.start();
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                ToastUtils.show(msg);
                            }
                        });
                    }
                });
            }
        });

        // tvSubmit
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = etNickname.getText().toString();
                final String phone = etPhone.getText().toString();
                String code = etCode.getText().toString();
                final String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if (mPageType == PAGE_TYPE_REGISTER) { // 注册才需要输入昵称
                    if (TextUtils.isEmpty(nickname)) {
                        ToastUtils.show("请输入昵称");
                        return;
                    }
                }
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(passwordConfirm)) {
                    ToastUtils.show("请输入确认密码");
                    return;
                }

                UserRegisterParam userRegisterParam = new UserRegisterParam();
                if (mPageType == PAGE_TYPE_REGISTER) { // 注册才需要输入昵称
                    userRegisterParam.setNickName(nickname);
                }
                userRegisterParam.setPhone(phone);
                userRegisterParam.setSmsCode(code);
                userRegisterParam.setPwd(password);
                userRegisterParam.setConfirmPwd(passwordConfirm);
                ApiRequest apiRequest = new ApiRequest();
                apiRequest.setCommonData(CommonData.get());
                apiRequest.setParam(userRegisterParam);
                final String content = JsonParser.get().toJson(apiRequest);

                switch (mPageType) {
                    case PAGE_TYPE_REGISTER: {
                        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUserRegister);
                        requestEntity.setMethod(RequestMethod.POST_STRING);
                        requestEntity.setContent(content);
                        requestEntity.addHeader("token", UserUtils.getToken());
                        requestEntity.addHeader("sign", EncodeHelper.getSign(content));
                        RequestBuilder.get().send(requestEntity, new RequestCallback<UserBean>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(int code, UserBean bean) {
                                if (bean != null) {
                                    ToastUtils.show("注册成功");

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
                        break;
                    }
                    case PAGE_TYPE_FIND_PWD: {
                        TokenUtils.getTempToken(new TokenUtils.OnGetTempTokenListener() {
                            @Override
                            public void onGetTempToken(String tempToken) {
                                RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUserFindPwd);
                                requestEntity.setMethod(RequestMethod.POST_STRING);
                                requestEntity.setContent(content);
                                requestEntity.addHeader("token", tempToken);
                                requestEntity.addHeader("sign", EncodeHelper.getSign(content));
                                RequestBuilder.get().send(requestEntity, new RequestCallback<Object>() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(int code, Object bean) {
                                        ToastUtils.show("找回密码成功");
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        ToastUtils.show(msg);
                                    }
                                });
                            }
                        });
                        break;
                    }
                }
            }
        });
    }

    /**
     * mCountDownTimer
     */
    private CountDownTimer mCountDownTimer = new CountDownTimer(30 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvCode.setEnabled(false);
            tvCode.setText(millisUntilFinished / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            isCountting = false;
            tvCode.setEnabled(true);
            tvCode.setText("获取验证码");
        }
    };
}
