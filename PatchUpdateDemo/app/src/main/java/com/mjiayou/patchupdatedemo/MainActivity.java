package com.mjiayou.patchupdatedemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mjiayou.patchupdatedemo.util.ApkExtract;
import com.mjiayou.patchupdatedemo.util.BsPatch;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "matengfei";

    private TextView mTvInfo;
    private Button mBtnPatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mBtnPatch = (Button) findViewById(R.id.btn_patch);

        mTvInfo.setText("版本-1.0");
        Log.i(TAG, "版本-1.0");

        mBtnPatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    doBspatch();
                }

            }
        });
    }

    private void doBspatch() {
        Log.i(TAG, "doBspatch");

        final File diffPatch = new File(Environment.getExternalStorageDirectory(), "diff.patch");
        Log.i(TAG, "diffPatch.exists() -> " + diffPatch.exists() + " | diffPatch.getAbsolutePath() -> " + diffPatch.getAbsolutePath());

        final File targetApk = new File(Environment.getExternalStorageDirectory(), "target.apk");
        BsPatch.bspatch(ApkExtract.extract(this), targetApk.getAbsolutePath(), diffPatch.getAbsolutePath());
        Log.i(TAG, "targetApk.exists() -> " + targetApk.exists() + " | targetApk.getAbsolutePath() -> " + targetApk.getAbsolutePath());

        Log.i(TAG, new File(Environment.getExternalStorageDirectory(), "old").getAbsolutePath());
        if (targetApk.exists()) {
            ApkExtract.install(this, targetApk.getAbsolutePath());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doBspatch();
            }
        }
    }

//    1、房子问题：
//        a 房子是肯定买的，婚先婚后的问题；
//        b 如果婚前不买房，等婚后买房时候，就我这么一个儿子，于情于理我爸妈都肯定会出钱；
//        c 关于我爸妈帮买房的问题，可以采用付部分首付以及帮还月供的方式；
//        d 关于我爸妈帮买房的问题，可以当面说明这个问题，口头承诺人格担保（接受不了一家人签字画押这一套，至少是在我家不需要这样）；
//
//    2、我家里的情况：
//        a 爸爸教师工作，政府编制，月工资5000+，每年都会几百的涨幅，退休后有退休金比现在高，所以父母养老以及小妹读书方面，理论上在经济方面不需要我操心；
//        b 老家一套房子能值个10万+，一直没人住，因为爷爷不同意原因一直没卖；
//        c 爸妈现在住的房子是市区20万+，房贷14年已还清，也是一笔财富，虽然和我无关，提它是因为这个和现在的家庭经济情况是挂钩的；

}
