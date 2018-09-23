package com.haoyang.lovelyreader.tre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.UserRegisterRequest;
import com.haoyang.lovelyreader.tre.bean.UserRegisterResponse;
import com.haoyang.lovelyreader.tre.http.RequestBuilder;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.haoyang.lovelyreader.tre.http.RequestEntity;
import com.haoyang.lovelyreader.tre.http.UrlConfig;
import com.mjiayou.trecorelib.util.ToastUtils;

/**
 * Created by xin on 18/9/22.
 */

public class RegisterActivity extends BaseActivity {

  private ImageView ivBack;
  private EditText etNickname;
  private EditText etPhone;
  private EditText etCode;
  private TextView tvCode;
  private EditText etPassword;
  private EditText etPasswordConfirm;
  private TextView tvRegister;
  private TextView tvProtocol;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    initView();
  }

  @Override protected void initView() {
    super.initView();

    // findViewById
    ivBack = (ImageView) findViewById(R.id.ivBack);
    etNickname = (EditText) findViewById(R.id.etNickname);
    etPhone = (EditText) findViewById(R.id.etPhone);
    etCode = (EditText) findViewById(R.id.etCode);
    tvCode = (TextView) findViewById(R.id.tvCode);
    etPassword = (EditText) findViewById(R.id.etPassword);
    etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
    tvRegister = (TextView) findViewById(R.id.tvRegister);
    tvProtocol = (TextView) findViewById(R.id.tvProtocol);

    // ivBack
    ivBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    // tvCode
    tvCode.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtils.show("获取验证码");
      }
    });
    // tvRegister
    tvRegister.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String nickname = etNickname.getText().toString();
        String phone = etPhone.getText().toString();
        String code = etCode.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();

        if (TextUtils.isEmpty(nickname)) {
          ToastUtils.show("请输入昵称");
          return;
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

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserName(nickname);
        userRegisterRequest.setPhone(phone);
        userRegisterRequest.setSmsCode(code);
        userRegisterRequest.setPwd(password);
        userRegisterRequest.setConfirmPwd(passwordConfirm);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUserRegister);
        requestEntity.setRequestBody(userRegisterRequest);
        RequestBuilder.get().send(requestEntity, new RequestCallback<UserRegisterResponse>() {
          @Override public void onStart() {

          }

          @Override public void onSuccess(int code, UserRegisterResponse bean) {
            if (bean != null) {
              ToastUtils.show(bean.getUid());
              startActivity(new Intent(mContext, MainActivity.class));
              finish();
            }
          }

          @Override public void onFailure(int code, String msg) {
            ToastUtils.show(msg);
          }
        });
      }
    });
    // tvProtocol
    tvProtocol.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtils.show("协议");
      }
    });
  }
}
