/**
 *
 */
package com.haoyang.lovelyreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.app.base.service.business.BusinessJsonResultListener;
import com.app.base.service.business.Error;
import com.google.gson.JsonObject;
import com.haoyang.lovelyreader.entity.User;
import com.haoyang.lovelyreader.service.UserService;

/**
 *
 */
public class RegisterActivity extends Activity implements
    BusinessJsonResultListener {

  private EditText userName = null;
  private EditText password = null;
  private EditText mobileCode = null;
  private EditText pictureCode = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 去掉TitleBar
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.file_browse);

    TextView submit = (TextView) this.findViewById(0);
    submit.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {
        submit();
      }
    });
  }

  private boolean checkData(User user) {

    String userName = this.userName.getText().toString();

    if (userName == null || "".equals(userName)) {

      // 让编缉框振动

      return false;
    }
    String mobileCode = this.mobileCode.getText().toString();

    if (mobileCode == null || "".equals(mobileCode)) {

      // 让编缉框振动

      return false;
    }
    String pictureCode = this.pictureCode.getText().toString();

    if (pictureCode == null || "".equals(pictureCode)) {

      // 让编缉框振动

      return false;
    }

    String code = this.password.getText().toString();

    if (code == null || "".equals(code)) {

      // 让编缉框振动

      return false;
    }

    // String password = this.password.getText().toString();
    //
    // if (password == null || "".equals(password)) {
    //
    // // 让编缉框振动
    //
    // return false;
    // }
    //
    // if (user == null) {
    //
    // String contact = this.password.getText().toString();
    // if (contact == null || "".equals(contact)) {
    //
    // // 让编缉框振动
    // return false;
    // }
    // }

    return true;
  }

  private User makeUser() {

    String userName = this.userName.getText().toString();
    String password = this.password.getText().toString();

    User user = new User();

    user.userName = userName;
    user.password = password;

    return user;
  }

  private void submit() {

    User user = this.makeUser();

    UserService userService = new UserService(this);

    String mobileCode = this.mobileCode.getText().toString();
    String pictureCode = this.pictureCode.getText().toString();

    String message = "正在注册";

    userService.register(user, mobileCode, pictureCode, this, message);
  }

  @Override
  public void OnSuccess(JsonObject jsonObject) {

    finish(); // 提交成功
  }

  @Override
  public void OnFail(Error error) {

    // 提示失败.

    finish();
  }
}
