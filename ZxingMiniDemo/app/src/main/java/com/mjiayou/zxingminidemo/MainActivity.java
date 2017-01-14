package com.mjiayou.zxingminidemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;

//import com.uuzuche.lib_zxing.activity.CaptureActivity;
//import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 100;
    private final String EXTRA_QR_RESULT = "result";

    // 扫描二维码
    private Button mBtnScan;
    private TextView mTvResult;
    // 生成二维码
    private EditText mEtInput;
    private Button mBtnGenerate;
    private ImageView mIvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

//        ZXingLibrary.initDisplayOpinion(this);
    }

    private void initView() {
        // 扫描二维码
        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        // 生成二维码
        mEtInput = (EditText) findViewById(R.id.et_input);
        mBtnGenerate = (Button) findViewById(R.id.btn_generate);
        mIvResult = (ImageView) findViewById(R.id.iv_result);

        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), REQUEST_CODE);
            }
        });
        mBtnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtInput.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(MainActivity.this, "字符串不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String filePath = getFileRoot(MainActivity.this)
                        + File.separator
                        + "qr_" + System.currentTimeMillis()
                        + ".jpg";
                int widthPix = 400;
                int heightPix = 400;

                boolean result = false;
                // 方式1
//                result = QRCodeUtil.createQRImage(str, widthPix, heightPix, null, filePath);
                // 方式2
                Bitmap bitmap = EncodingUtils.createQRCode(str, widthPix, heightPix, null);
                try {
                    result = bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
                    bitmap.recycle();
                    bitmap = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "result -> " + result, Toast.LENGTH_SHORT).show();
                if (result) {
                    hideKeyboard(mEtInput);
                    mIvResult.setImageBitmap(BitmapFactory.decodeFile(filePath));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString(EXTRA_QR_RESULT);
            mTvResult.setText(result);
        }

//        /**
//         * 处理二维码扫描结果
//         */
//        if (requestCode == REQUEST_CODE) {
//            // 处理扫描结果（在界面上显示）
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }

    /**
     * 文件存储根目录
     */
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 隐藏键盘
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}