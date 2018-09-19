package com.mjiayou.trecorejitpack;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mjiayou.trecorelib.util.AppUtil;
import com.mjiayou.trecorelib.util.HelloUtil;
import com.mjiayou.trecorelib.util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    private TextView mTvInfo;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // findViewById
        mTvInfo = (TextView) findViewById(R.id.tv_info);

        // mTvInfo
        mTvInfo.append("\n");
        mTvInfo.append(HelloUtil.getHI());
        mTvInfo.append("\n");
        mTvInfo.append(AppUtil.getAppInfoDetail(mContext));
        mTvInfo.append("\n");

        // ToastUtil
        ToastUtil.show("ToastUtil TEST");
    }
}
