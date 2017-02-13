package com.mjiayou.patchupdatedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mjiayou.patchupdatedemo.util.ApkExtract;
import com.mjiayou.patchupdatedemo.util.JNIUtil;
import com.mjiayou.trecorelib.util.AppUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "matengfei";
    private final int REQUEST_CODE_DIFF = 1;
    private final int REQUEST_CODE_PATCH = 2;

    private Activity mActivity;
    private Context mContext;

    private Button mBtnDiff;
    private Button mBtnPatch;
    private TextView mTvInfo;

    private File oldAPK;
    private File newAPK;
    private File diffPatch;
    private File targetApk;

    private StringBuilder mLog = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;

        mBtnDiff = (Button) findViewById(R.id.btn_diff);
        mBtnPatch = (Button) findViewById(R.id.btn_patch);
        mTvInfo = (TextView) findViewById(R.id.tv_info);

        mBtnDiff.setVisibility(View.VISIBLE);
        mBtnDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_DIFF);
                } else {
                    diff();
                }
            }
        });

        mBtnPatch.setVisibility(View.VISIBLE);
        mBtnPatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PATCH);
                } else {
                    patch();
                }
            }
        });

        mTvInfo.setMovementMethod(new ScrollingMovementMethod());

        oldAPK = new File(ApkExtract.extract(this));
        newAPK = new File(getFilePath("new.apk"));
        diffPatch = new File(getFilePath("diff.patch"));
        targetApk = new File(getFilePath("target.apk"));

        refreshLog(AppUtil.getVersionInfo(mContext));
        refreshLog("--------------------------------");
        refreshLog("文件路径信息：");
        refreshLog("oldAPK.getAbsolutePath() -> " + "\n" + oldAPK.getAbsolutePath() + " | " + oldAPK.exists());
        refreshLog("newAPK.getAbsolutePath() -> " + "\n" + newAPK.getAbsolutePath() + " | " + newAPK.exists());
        refreshLog("diffPatch.getAbsolutePath() -> " + "\n" + diffPatch.getAbsolutePath() + " | " + diffPatch.exists());
        refreshLog("targetApk.getAbsolutePath() -> " + "\n" + targetApk.getAbsolutePath() + " | " + targetApk.exists());
        refreshLog("--------------------------------");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_CODE_DIFF:
                    diff();
                    break;
                case REQUEST_CODE_PATCH:
                    patch();
                    break;
            }
        }
    }

    /**
     * 生成增量文件
     */
    private void diff() {
        refreshLog("生成增量文件开始-diff");

        if (!oldAPK.exists()) {
            refreshLog("旧版本APK不存在-!oldAPK.exists()");
            return;
        }

        if (!newAPK.exists()) {
            refreshLog("新版本APK不存在-!newAPK.exists()");
            return;
        }

        refreshLog("ing");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JNIUtil.bsdiff(oldAPK.getAbsolutePath(), newAPK.getAbsolutePath(), diffPatch.getAbsolutePath());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLog("生成增量文件完成-diff done");

                        if (!diffPatch.exists()) {
                            refreshLog("生成增量文件失败-!diffPatch.exists()");
                        } else {
                            refreshLog("生成增量文件成功-diffPatch.exists()");
                        }

                        refreshLog("--------------------------------");
                    }
                });
            }
        }).start();
    }

    /**
     * 合成增量文件
     */
    private void patch() {
        refreshLog("合成增量文件开始-doPatch");

        if (!oldAPK.exists()) {
            refreshLog("旧版本APK不存在-!oldAPK.exists()");
            return;
        }

        if (!diffPatch.exists()) {
            refreshLog("增量文件不存在-!diffPatch.exists()");
            return;
        }

        refreshLog("ing");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JNIUtil.bspatch(oldAPK.getAbsolutePath(), targetApk.getAbsolutePath(), diffPatch.getAbsolutePath());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLog("合成增量文件完成-patch done");

                        if (!targetApk.exists()) {
                            refreshLog("合成增量文件失败-!targetApk.exists()");
                        } else {
                            refreshLog("合成增量文件成功-targetApk.exists()");

                            new AlertDialog.Builder(mContext)
                                    .setTitle("提示")
                                    .setMessage("目标APK已生成，确认安装？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ApkExtract.install(mContext, targetApk.getAbsolutePath());
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }

                        refreshLog("--------------------------------");
                    }
                });
            }
        }).start();
    }

    private void refreshLog(String message) {
        mLog.append(message).append("\n");
        mTvInfo.setText(mLog.toString());
    }

    private String getFilePath(String fileName) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/treason/" + fileName;
    }
}
