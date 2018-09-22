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
        ToastUtil.show(mContext, "获取验证码");
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
          ToastUtil.show(mContext, "请输入昵称");
          return;
        }
        if (TextUtils.isEmpty(phone)) {
          ToastUtil.show(mContext, "请输入手机号码");
          return;
        }
        if (TextUtils.isEmpty(code)) {
          ToastUtil.show(mContext, "请输入验证码");
          return;
        }
        if (TextUtils.isEmpty(password)) {
          ToastUtil.show(mContext, "请输入密码");
          return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
          ToastUtil.show(mContext, "请输入确认密码");
          return;
        }

        ToastUtil.show(mContext, "注册 | nickname -> " + nickname + " | phone -> " + phone + " | code -> " + code + " | password -> " + password + " | passwordConfirm -> " + passwordConfirm);

        startActivity(new Intent(mContext, MainActivity.class));
        finish();
      }
    });
    // tvProtocol
    tvProtocol.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtil.show(mContext, "协议");
      }
    });
  }
}
