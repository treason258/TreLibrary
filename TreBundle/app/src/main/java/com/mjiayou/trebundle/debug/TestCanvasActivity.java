package com.mjiayou.trebundle.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mjiayou.trebundle.R;
import com.mjiayou.trecore.base.TCActivity;

public class TestCanvasActivity extends TCActivity {

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, TestCanvasActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_canvas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTitleBar().setTitle("TestCanvasActivity");
    }
}
