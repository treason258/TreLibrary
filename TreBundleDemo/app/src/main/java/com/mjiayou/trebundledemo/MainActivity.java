package com.mjiayou.trebundledemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mjiayou.trecore.base.TCActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends TCActivity {

    @InjectView(R.id.btn_debug)
    Button mBtnDebug;

    @OnClick({R.id.btn_debug})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_debug:
                DebugActivity.open(mContext);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }
}
