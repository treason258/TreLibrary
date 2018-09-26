/**
 * 
 */
package com.haoyang.lovelyreader.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import com.app.base.service.android.SharedPreferenceService;
import com.app.base.service.business.BusinessJsonResultListener;
import com.app.base.service.business.Error;
import com.google.gson.JsonObject;
import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.entity.User;
import com.haoyang.lovelyreader.service.UserService;
import com.java.common.service.CommonKeys;

/**
 * @author tianyu
 *
 */
public class LoginActivity extends Activity implements
		BusinessJsonResultListener {

	private EditText userName = null;
	private EditText password = null;
	private EditText checkCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 去掉TitleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.file_browse);

	}

	private boolean checkData(User user) {

		String userName = this.userName.getText().toString();

		if (userName == null || "".equals(userName)) {

			// 让编缉框振动

			return false;
		}
		String code = this.checkCode.getText().toString();

		if (code == null || "".equals(code)) {

			// 让编缉框振动

			return false;
		}
		String password = this.password.getText().toString();

		if (password == null || "".equals(password)) {

			// 让编缉框振动

			return false;
		}

		if (user == null) {

			String contact = this.password.getText().toString();
			if (contact == null || "".equals(contact)) {

				// 让编缉框振动
				return false;
			}
		}

		return true;
	}

	private User makeUser() {

		String userName = this.userName.getText().toString();
		String password = this.password.getText().toString();
		String code = this.checkCode.getText().toString();

		User user = new User();

		user.userName = userName;
		user.password = password;

		return user;
	}

	private void submit() {

		User user = this.makeUser();

		UserService userService = new UserService(this);

		String message = "正在登录";
		userService.login(user, this, message);
	}

	@Override
	public void OnSuccess(JsonObject jsonObject) {

		// 提交成功
		String token = "";

		SharedPreferenceService sharedPreferenceService = new SharedPreferenceService(
				this);
		sharedPreferenceService.putValue(CommonKeys.TOKEN, token);

		// 存储用户信息。
		// UserService.user

		finish();
	}

	@Override
	public void OnFail(Error error) {

		// 提示失败.

	}
}
