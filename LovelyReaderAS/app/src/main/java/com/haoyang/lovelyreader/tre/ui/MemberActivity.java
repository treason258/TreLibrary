package com.haoyang.lovelyreader.tre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonData;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.util.ToastUtils;

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

        getToken();
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

//        requestEntity.addParam("randomChar", randomChar);
//        requestEntity.addParam("sign", EncodeHelper.getSignForToken(randomChar));

        String randomChar = EncodeHelper.getRandomChar();
        CommonParam commonParam = new CommonParam();
        commonParam.setData(randomChar);
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setCommonData(CommonData.get());
        apiRequest.setParam(commonParam);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiTokenTemp);
        requestEntity.setMethod(RequestMethod.POST_STRING);
        requestEntity.setContent(apiRequest);
        RequestBuilder.get().send(requestEntity, new RequestCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, String object) {

            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(msg);
            }
        });
    }
}
