package com.mjiayou.trecore.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mjiayou.trecore.R;

public class TCInfoActivity extends TCActivity {

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, TCInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tc_activity_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
