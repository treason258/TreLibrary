package com.aiseminar.EasyPR;

import android.content.Context;
import android.util.Log;

import com.aiseminar.util.FileUtil;
import com.aiseminar.util.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ares on 6/19/16.
 */
public class PlateRecognizer {

    private boolean inited;

    public PlateRecognizer(Context context) {
        String mSvmpath = FileUtil.getDir(context, "opencv");
        checkAndUpdateModelFile(context, mSvmpath);
        try {
            initPR(mSvmpath);
            inited = true;
        } catch (Throwable e) {
            inited = false;
        }
    }

    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        uninitPR(0);
    }

    public void checkAndUpdateModelFile(Context mContext, String mSvmpath) {
        try {
            if (!new File(mSvmpath, "ann_chinese.xml").exists() || !new File(mSvmpath, "ann.xml").exists() ||
                    !new File(mSvmpath, "province_mapping").exists() || !new File(mSvmpath, "svm.xml").exists()) {
                File file = new File(mSvmpath, "model.zip");
                FileUtil.copyAssets2SD(mContext, "model.zip", file);
                ZipUtils.upZipFile(file, mSvmpath);
            }
        } catch (IOException e) {
            Log.e("PlateRecognizer", "解压失败", e);
        }
    }


    public String recognizeNum(String imagePath) {
        //判断文件夹是否存在
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return null;
        }
        byte[] retBytes = plateRecognize(0, imagePath);
        String result = null;
        try {
            result = new String(retBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public float[] recognizePosition(String imagePath) {
        //判断文件夹是否存在
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return null;
        }
        float[] result = null;
        if (inited) {
            result = platePosition(imagePath);
        }
        return result;
    }

    /**
     * JNI Functions
     */
    // 加载车牌识别库
    static {
        try {
            System.loadLibrary("EasyPR");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("WARNING: Could not load EasyPR library!");
        }
    }

    public static native String stringFromJNI();

    public static native long initPR(String svmpath);

    public static native long uninitPR(long recognizerPtr);

    public static native byte[] plateRecognize(long recognizerPtr, String imgpath);

    public static native float[] platePosition(String imgpath);
}
