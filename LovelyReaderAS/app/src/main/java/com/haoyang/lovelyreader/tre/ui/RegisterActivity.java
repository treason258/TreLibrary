package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.bean.api.UserRegisterParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.haoyang.lovelyreader.tre.util.TokenUtils;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.mjiayou.trecorelib.http.RequestSender;
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
    private TextView tvTitle;
    private EditText etPhone;
    private EditText etCode;
    private TextView tvCode;
    private LinearLayout llNickname;
    private EditText etNickname;
    private EditText etPassword;
    private EditText etPasswordConfirm;
//    private LinearLayout llChannel;
//    private EditText etChannel;
    private TextView tvSubmit;
    private TextView tvProtocol;

    private boolean isCountting;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        // findViewById
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCode = (EditText) findViewById(R.id.etCode);
        tvCode = (TextView) findViewById(R.id.tvCode);
        llNickname = (LinearLayout) findViewById(R.id.llNickname);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
//        llChannel = (LinearLayout) findViewById(R.id.llChannel);
//        etChannel = (EditText) findViewById(R.id.etChannel);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvProtocol = (TextView) findViewById(R.id.tvProtocol);

        // mPageType
        if (getIntent() != null) {
            mPageType = getIntent().getIntExtra(EXTRA_PAGE_TYPE, PAGE_TYPE_REGISTER);
        }

        initView();
    }

    @Override
    public void initView() {
        super.initView();

        switch (mPageType) {
            case PAGE_TYPE_REGISTER: // 注册
                tvTitle.setText("注册");
                tvSubmit.setText("注册");
                llNickname.setVisibility(View.VISIBLE);
//                llChannel.setVisibility(View.VISIBLE);
                tvProtocol.setVisibility(View.GONE);
                tvProtocol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtils.show("注册协议");
                    }
                });
                break;
            case PAGE_TYPE_FIND_PWD: // 找回密码
                tvTitle.setText("找回密码");
                tvSubmit.setText("确定");
                llNickname.setVisibility(View.GONE);
//                llChannel.setVisibility(View.GONE);
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
                if (!Utils.isMobileNO(phone)) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }

                TokenUtils.getTempToken(new TokenUtils.OnGetTempTokenListener() {
                    @Override
                    public void onGetTempToken(String tempToken) {
                        CommonParam commonParam = new CommonParam();
                        commonParam.setData(phone);

                        String url = UrlConfig.apiSmsSendrigstersms;
                        if (mPageType == PAGE_TYPE_FIND_PWD) { // 找回密码
                            url = UrlConfig.apiSmsSendchangepwdsms;
                        }
                        MyRequestEntity myRequestEntity = new MyRequestEntity(url);
                        myRequestEntity.setContentWithHeader(ApiRequest.getContent(commonParam), tempToken);
                        RequestSender.get().send(myRequestEntity, new RequestCallback<Object>() {
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
                String phone = etPhone.getText().toString();
                String code = etCode.getText().toString();
                String nickname = etNickname.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
//                String channel = etChannel.getText().toString();


                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }
                if (!Utils.isMobileNO(phone)) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.show("请输入验证码");
                    return;
                }
                if (mPageType == PAGE_TYPE_REGISTER) { // 注册才需要输入昵称
                    if (TextUtils.isEmpty(nickname)) {
                        ToastUtils.show("请输入昵称");
                        return;
                    }
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(passwordConfirm)) {
                    ToastUtils.show("请输入确认密码");
                    return;
                }
//                if (mPageType == PAGE_TYPE_REGISTER) { // 注册才需要输入渠道码
//                    if (TextUtils.isEmpty(channel)) {
//                        ToastUtils.show("请输入渠道码");
//                        return;
//                    }
//                }

                UserRegisterParam userRegisterParam = new UserRegisterParam();
                if (mPageType == PAGE_TYPE_REGISTER) { // 注册才需要输入昵称、注册才需要输入渠道码
                    userRegisterParam.setNickName(nickname);
//                    userRegisterParam.setChannel(channel);
                }
                userRegisterParam.setPhone(phone);
                userRegisterParam.setSmsCode(code);
                userRegisterParam.setPwd(password);
                userRegisterParam.setConfirmPwd(passwordConfirm);
                String content = ApiRequest.getContent(userRegisterParam);

                switch (mPageType) {
                    case PAGE_TYPE_REGISTER: {
                        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiUserRegister);
                        myRequestEntity.setContentWithHeader(content);
                        RequestSender.get().send(myRequestEntity, new RequestCallback<UserBean>() {
                            @Override
                            public void onStart() {
                                showLoading(true);
                            }

                            @Override
                            public void onSuccess(int code, UserBean bean) {
                                showLoading(false);
                                if (bean != null) {
                                    ToastUtils.show("注册成功");

                                    // 保存登录的用户名和密码，下次自动填充
                                    SharedUtils.get().setAccountUsername(phone);
                                    SharedUtils.get().setAccountPassword(password);
                                    // 保存用户信息
                                    DBHelper.setUserBean(bean);
                                    // 通知登录成功
                                    UserUtils.doLogin(bean.getToken());

                                    // 页面跳转
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                showLoading(false);
                                ToastUtils.show(msg);
                            }
                        });
                        break;
                    }
                    case PAGE_TYPE_FIND_PWD: {
                        TokenUtils.getTempToken(new TokenUtils.OnGetTempTokenListener() {
                            @Override
                            public void onGetTempToken(String tempToken) {
                                MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiUserFindPwd);
                                myRequestEntity.setContentWithHeader(content, tempToken);
                                RequestSender.get().send(myRequestEntity, new RequestCallback<Object>() {
                                    @Override
                                    public void onStart() {
                                        showLoading(true);
                                    }

                                    @Override
                                    public void onSuccess(int code, Object bean) {
                                        showLoading(false);
                                        ToastUtils.show("找回密码成功");
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
