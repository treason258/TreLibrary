package com.haoyang.lovelyreader.tre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.api.AppUpgradeRequest;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.json.JsonHelper;
import com.mjiayou.trecorelib.util.UserUtils;

import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by xin on 18/9/22.
 */

public class MemberActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        // findViewById
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        initView();

//        getToken();
        checkUpdate();
    }

    @Override
    protected void initView() {
        super.initView();
        tvTitle.setText("升级会员");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取临时token
     */
    private void getToken() {
        String randomChar = EncodeHelper.getRandomChar();

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiTokenTemp);
        requestEntity.setMethod(RequestMethod.POST);
        requestEntity.addParam("randomChar", randomChar);
        requestEntity.addParam("sign", EncodeHelper.getSignForToken(randomChar));

        RequestBuilder.get().send(requestEntity, new RequestCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, String object) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        AppUpgradeRequest appUpgradeRequest = new AppUpgradeRequest();
        appUpgradeRequest.setTerminal("ANDROID_PHONE");
        appUpgradeRequest.setVersion("1.0.0");

        String url = UrlConfig.apiAppUpgrade;
        String token = UserUtils.getToken();
        String type = "2";
        String jsonData = JsonHelper.get().toJson(appUpgradeRequest);
        String urlWithSign = EncodeHelper.getUrlWithSign(url, token, type, jsonData);
        RequestEntity requestEntity = new RequestEntity(urlWithSign);
        requestEntity.setMethod(RequestMethod.POST_STRING);
        requestEntity.setContent(appUpgradeRequest);
        RequestBuilder.get().send(requestEntity, new RequestCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, String object) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });

    }
}
