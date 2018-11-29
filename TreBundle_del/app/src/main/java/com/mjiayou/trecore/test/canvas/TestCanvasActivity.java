package com.mjiayou.trecore.test.canvas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mjiayou.trecore.TCActivity;
import com.mjiayou.trebundle.R;

public class TestCanvasActivity extends TCActivity {

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, TestCanvasActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_canvas);
    }
}

