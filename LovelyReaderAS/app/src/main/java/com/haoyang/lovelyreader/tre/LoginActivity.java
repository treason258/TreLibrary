package com.haoyang.lovelyreader.tre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.haoyang.lovelyreader.R;

/**
 * Created by xin on 18/9/22.
 */

public class LoginActivity extends BaseActivity {

  private EditText etPhone;
  private EditText etPassword;
  private TextView tvLogin;
  private TextView tvFindPwd;
  private TextView tvRegister;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initView();
  }

  @Override protected void initView() {
    super.initView();

    // findViewById
    etPhone = (EditText) findViewById(R.id.etPhone);
    etPassword = (EditText) findViewById(R.id.etPassword);
    tvLogin = (TextView) findViewById(R.id.tvLogin);
    tvFindPwd = (TextView) findViewById(R.id.tvFindPwd);
    tvRegister = (TextView) findViewById(R.id.tvRegister);

    // tvLogin
    tvLogin.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
          ToastUtil.show(mContext, "请输入手机号码");
          return;
        }
        if (TextUtils.isEmpty(password)) {
          ToastUtil.show(mContext, "请输入密码");
          return;
        }

        ToastUtil.show(mContext, "登录 | phone -> " + phone + " | password -> " + password);

        startActivity(new Intent(mContext, MainActivity.class));
        finish();
      }
    });
    // tvFindPwd
    tvFindPwd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(mContext, FindPwdActivity.class));
      }
    });
    // tvRegister
    tvRegister.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(mContext, RegisterActivity.class));
      }
    });
  }
}
