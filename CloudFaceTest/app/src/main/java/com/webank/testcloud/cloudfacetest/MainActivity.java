package com.webank.testcloud.cloudfacetest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.webank.testcloud.R;
import com.webank.wbcloudfaceverify2.tools.ErrorCode;
import com.webank.wbcloudfaceverify2.tools.IdentifyCardValidate;
import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;
import com.webank.wbcloudfaceverify2.ui.FaceVerifyStatus;
import com.webank.wbcloudfaceverify2.ui.component.SlipButton;

import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private RelativeLayout faceVerifyEasy;
    private RelativeLayout faceVerifyMid;
    private RelativeLayout faceVerifyAdv;

    private EditText nameEt;
    private EditText idNoEt;
    private SlipButton slipButton;
    private RadioGroup colorChoose;
    private RadioButton blackBtn;
    private RadioButton whiteBtn;

    private ProgressDialog progressDlg;

    private SharedPreferences sp;

    private boolean isShowSuccess;
    private String color;

    private AppHandler appHandler;
    private SignUseCase signUseCase;

    private String name;
    private String id;

    private String userId = "testCloudFaceVerify" + System.currentTimeMillis();
    private String nonce = "52014832029547845621032584562012";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = this.getSharedPreferences("FaceVerify", MODE_PRIVATE);
        appHandler = new AppHandler(this);
        signUseCase = new SignUseCase(Executors.newSingleThreadExecutor(), appHandler);

        initViews();
        setListeners();
    }

    private void initViews() {
        initProgress();

        faceVerifyEasy = (RelativeLayout) findViewById(R.id.faceVerifyEasy);
        faceVerifyMid = (RelativeLayout) findViewById(R.id.faceVerifyMid);
        faceVerifyAdv = (RelativeLayout) findViewById(R.id.faceVerifyAdv);
        nameEt = (EditText) findViewById(R.id.et_name);
        idNoEt = (EditText) findViewById(R.id.et_idNo);
        slipButton = (SlipButton) findViewById(R.id.slip_btn);
        colorChoose = (RadioGroup) findViewById(R.id.color_choose);
        blackBtn = (RadioButton) findViewById(R.id.black_btn);
        whiteBtn = (RadioButton) findViewById(R.id.white_btn);
    }

    private void initProgress() {
        if (progressDlg != null) {
            progressDlg.dismiss();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            progressDlg = new ProgressDialog(this);
        } else {
            progressDlg = new ProgressDialog(this);
            progressDlg.setInverseBackgroundForced(true);
        }
        progressDlg.setMessage("加载中...");
        progressDlg.setIndeterminate(true);
        progressDlg.setCanceledOnTouchOutside(false);
        progressDlg.setCancelable(true);
        progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDlg.setCancelable(false);
    }

    private void setListeners() {
        //默认选择黑色模式
        blackBtn.setChecked(true);
        color = WbCloudFaceVerifySdk.BLACK;
        colorChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.black_btn) {
                    color = WbCloudFaceVerifySdk.BLACK;
                } else if (checkedId == R.id.white_btn) {
                    color = WbCloudFaceVerifySdk.WHITE;
                }
            }
        });

        faceVerifyEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString().trim();
                id = idNoEt.getText().toString().trim();
                if (name != null && name.length() != 0) {
                    if (id != null && id.length() != 0) {
                        if (id.contains("x")) {
                            id = id.replace('x', 'X');
                        }
                        IdentifyCardValidate vali = new IdentifyCardValidate();
                        String msg = vali.validate_effective(id);
                        if (msg.equals(id)) {
                            Log.i(TAG, "Param right!");
                            Log.i(TAG, "Called Face Verify Sdk EASY MODE!");
                            progressDlg.show();
                            signUseCase.execute(AppHandler.DATA_MODE_EASY, "TIDA0001", userId, nonce);
                        } else {
                            Toast.makeText(MainActivity.this, "用户证件号错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "用户证件号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "用户姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        faceVerifyMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameEt.getText().toString().trim();
                id = idNoEt.getText().toString().trim();
                if (name != null && name.length() != 0) {
                    if (id != null && id.length() != 0) {
                        if (id.contains("x")) {
                            id = id.replace('x', 'X');
                        }

                        IdentifyCardValidate vali = new IdentifyCardValidate();
                        String msg = vali.validate_effective(id);
                        if (msg.equals(id)) {
                            Log.i(TAG, "Param right!");
                            Log.i(TAG, "Called Face Verify Sdk MIDDLE MODE!");
                            progressDlg.show();
                            signUseCase.execute(AppHandler.DATA_MODE_MID, "TIDA0001", userId, nonce);
                        } else {
                            Toast.makeText(MainActivity.this, "用户证件号错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "用户证件号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "用户姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        faceVerifyAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString().trim();
                id = idNoEt.getText().toString().trim();
                if (name != null && name.length() != 0) {
                    if (id != null && id.length() != 0) {
                        if (id.contains("x")) {
                            id = id.replace('x', 'X');
                        }

                        IdentifyCardValidate vali = new IdentifyCardValidate();
                        String msg = vali.validate_effective(id);
                        if (msg.equals(id)) {
                            Log.i(TAG, "Param right!");
                            Log.i(TAG, "Called Face Verify Sdk ADVANCED MODE!");
                            progressDlg.show();
                            signUseCase.execute(AppHandler.DATA_MODE_ADVANCED, "TIDA0001", userId, nonce);
                        } else {
                            Toast.makeText(MainActivity.this, "用户证件号错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "用户证件号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "用户姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        slipButton.setCheck(true);
        isShowSuccess = true;
        slipButton.SetOnChangedCallBack(new SlipButton.onChangedCallBack() {
            @Override
            public void OnChanged(boolean CheckState) {
                if (CheckState) {
                    Log.i(TAG, "CheckState=" + CheckState + "; isShowSuccess = true");
                    isShowSuccess = true;
                } else {
                    Log.i(TAG, "CheckState=" + CheckState + "; isShowSuccess = false");
                    isShowSuccess = false;
                }
            }
        });

    }

    public void openCloudFaceService(final FaceVerifyStatus.Mode mode, String sign) {

        final String modeShowGuide = mode.toString();

        Bundle data = new Bundle();
        WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
                name,
                "01",
                id,
                "test" + System.currentTimeMillis(),
                "ip",
                "gps",
                "TIDA0001",
                "1.0.0",
                nonce,
                userId,
                sign,
                sp.getBoolean(modeShowGuide, true),
                mode,
//                "oLDmBnPgZ6bFSdjMuKnucwv8djzhxglDGtBGulYqgml2DGwRaEe8nAtFqWPyqPK4yMguQ9UzazPufLV5L1rtrsCmZ/0+sKMfM/rMx9zyVYQ+Hsz0su1BUKj4KCHrno77Xxz7ZWA9+Tt69NOUFtU72RYngVIZNsWPBeh9+2kGhpi1CiaoqqLLl/sl67GGqpmoglKAHv14TdkqcFe1FRZLt2aIT7qGbOKtmTav9sJT2KWUR7lasm1Jaw9qQ/zbtc+fEMWfA4FGl3iaQE9hpjmH8+9hiAlevdGzscu99STOyR1ext54/xC40EQFX1MIRb9SZaPZJBsxm7iSpPF+fc3MzA==");
                "DXxhjJPPI3XSg189mivu/wT6DcvJ8402PB9eAVPc99Kcx0KVZ/i+PyY7B0gDV6au2sF//s52H3gKVEt4fETXfdoXaK8CJK4lH26WC3H4dOZHiDXKw/bSkoZd1XEScmEQq+J6xtElY/PT3nf6qW8hZxu2wjcr16iHnZ45pkVMpjOQOj5yE+z56JgkBlSeBpt8SfioSRrSmIrWVnMy6S3aXHsaYXmz1ukWPhjlFQ1y5aXyS40Apo6DQwDHxOlg9gM+i5cF8iEQcpRmF0k9KonTNIpdR/nNJ+wQFTGDT96hhxhSbaxapMaAs149nuNbp/gcdnQ8c3fGZgj0D5zVOn10VQ==");

        data.putSerializable(WbCloudFaceVerifySdk.INPUT_DATA, inputData);
        //是否展示刷脸成功页面，默认展示
        data.putBoolean(WbCloudFaceVerifySdk.SHOW_SUCCESS_PAGE, isShowSuccess);
        //颜色设置
        data.putString(WbCloudFaceVerifySdk.COLOR_MODE, color);
        //是否对录制视频进行检查,默认不检查
//        data.putBoolean(WbCloudFaceVerifySdk.VIDEO_CHECK, true);

        WbCloudFaceVerifySdk.getInstance().init(MainActivity.this, data, new WbCloudFaceVerifySdk.FaceVerifyLoginListener() {
            @Override
            public void onLoginSuccess() {
                Log.i(TAG, "onLoginSuccess");
                progressDlg.dismiss();

                WbCloudFaceVerifySdk.getInstance().startActivityForSecurity(new WbCloudFaceVerifySdk.FaceVerifyResultForSecureListener() {
                    @Override
                    public void onFinish(int resultCode, boolean nextShowGuide, String faceCode, String faceMsg, String sign, Bundle extendData) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean(modeShowGuide, nextShowGuide);
                        editor.commit();

                        if (faceCode == null) {
                            faceCode = "";
                        }
                        if (faceMsg == null) {
                            faceMsg = "";
                        }

                        if (resultCode == 0) {
                            Log.d(TAG, "刷脸成功！errorCode=" + resultCode + " ;faceCode= " + faceCode + " ;faceMsg=" + faceMsg + " ;Sign=" + sign);
                            Toast.makeText(MainActivity.this, "刷脸成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "刷脸失败！errorCode=" + resultCode + " ;faceCode= " + faceCode + " ;faceMsg=" + faceMsg + " ;Sign=" + sign);
                            Toast.makeText(MainActivity.this, "刷脸失败：errorCode=" + resultCode + " ;faceCode= " + faceCode + " ;faceMsg=" + faceMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onLoginFailed(String errorCode, String errorMsg) {
                Log.i(TAG, "onLoginFailed!");
                progressDlg.dismiss();
                if (errorCode.equals(ErrorCode.FACEVERIFY_LOGIN_PARAMETER_ERROR)) {
                    Toast.makeText(MainActivity.this, "传入参数有误！" + errorMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "登录刷脸sdk失败！" + "errorCode= " + errorCode + " ;errorMsg=" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hideLoading() {
        progressDlg.dismiss();

    }
}