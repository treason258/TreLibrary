package com.aiseminar.platerecognizer.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aiseminar.EasyPR.PlateRecognizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "treason";
    private Context mContext;

    private ImageView mIvOrigin;
    private Button mBtnProcess;

    private String mImagePath;
    private Bitmap mBitmapOrigin;
    private Bitmap mBitmapProcessed;
    private Bitmap mBitmapHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // findViewById
        mIvOrigin = (ImageView) findViewById(R.id.iv_origin);
        mBtnProcess = (Button) findViewById(R.id.btn_process);

        // mImagePath
        mImagePath = getImagePath();
        mBitmapOrigin = BitmapFactory.decodeFile(mImagePath);

        // mIvOrigin
        mIvOrigin.setImageBitmap(mBitmapOrigin);

        // mBtnProcess
        mBtnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PlateRecognizer plateRecognizer = new PlateRecognizer(MainActivity.this);
                        final float[] floats = plateRecognizer.recognizePosition(mImagePath); // 识别车牌

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 获取到八个关键点，floats是车牌的绝对位置，x1,y1,x2,y2,x3,y3,x4,y4 四个点
                                if (floats == null) {
                                    Log.i(TAG, "floats == null");
                                    return;
                                } else {
                                    Log.i(TAG, "floats |");
                                    for (int i = 0; i < floats.length; i++) {
                                        Log.i(TAG, i + " -> " + floats[i]);
                                    }
                                }

                                if (floats.length < 8) {
                                    Log.i(TAG, "floats.length < 8");
                                    return;
                                }

                                // points 0,1,2,3 指从左上角开始，顺时针方向顺序的四个点：左上、右上、右下、左下
                                PointF[] points = new PointF[floats.length / 2];
                                for (int i = 0; i < points.length; i++) {
                                    points[i] = new PointF((floats[i * 2]), (floats[i * 2 + 1])); // 把点的位置转换成相对位置
                                }

                                // 画笔
                                Paint paint = new Paint();
                                paint.setColor(Color.RED);
                                paint.setStyle(Paint.Style.FILL);
                                paint.setStrokeWidth(Math.max(mBitmapOrigin.getWidth(), mBitmapOrigin.getHeight()) / 100f);

                                // 画布
                                mBitmapProcessed = Bitmap.createBitmap(mBitmapOrigin.getWidth(), mBitmapOrigin.getHeight(), Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(mBitmapProcessed);
                                canvas.drawBitmap(mBitmapOrigin, new Matrix(), null);

                                // HINT
                                try {
                                    paint.setColor(Color.RED);
                                    canvas.drawPoint(100, 100, paint);
                                    canvas.drawPoint(200, 200, paint);
                                    canvas.drawPoint(500, 500, paint);
                                    canvas.drawPoint(1000, 1000, paint);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                                // 画多边形
//                                try {
//                                    paint.setColor(Color.LTGRAY);
//                                    Path path = new Path();
//                                    path.moveTo(points[0].x, points[0].y); // 此点为多边形的起点
//                                    path.lineTo(points[1].x, points[1].y);
//                                    path.lineTo(points[2].x, points[2].y);
//                                    path.lineTo(points[3].x, points[3].y);
//                                    path.close(); // 使这些点构成封闭的多边形
//                                    canvas.drawPath(path, paint);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }

                                // 画占位图
                                try {
                                    paint.setColor(Color.LTGRAY);
                                    Path path = new Path();
                                    path.moveTo(points[0].x, points[0].y); // 此点为多边形的起点
                                    path.lineTo(points[1].x, points[1].y);
                                    path.lineTo(points[2].x, points[2].y);
                                    path.lineTo(points[3].x, points[3].y);
                                    path.close(); // 使这些点构成封闭的多边形
                                    canvas.drawPath(path, paint);

                                    RectF rect = new RectF(); // size.x * ENLAGE    // size.y * ENLAGE
                                    mBitmapHint = BitmapFactory.decodeResource(getResources(), R.drawable.hint);
                                    mBitmapHint = createRotateBitmap(mBitmapHint, points, rect);//创建一张变形的车牌图片
                                    canvas.drawBitmap(mBitmapHint, rect.left, rect.top, paint);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // 四个点不同颜色
                                try {
                                    paint.setColor(Color.RED);
                                    canvas.drawPoint(points[0].x, points[0].y, paint);
                                    paint.setColor(Color.GREEN);
                                    canvas.drawPoint(points[1].x, points[1].y, paint);
                                    paint.setColor(Color.YELLOW);
                                    canvas.drawPoint(points[2].x, points[2].y, paint);
                                    paint.setColor(Color.BLUE);
                                    canvas.drawPoint(points[3].x, points[3].y, paint);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // mIvOrigin
                                mIvOrigin.setImageBitmap(mBitmapProcessed);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private String getImagePath() {
        File sdCardDir;
        File imageFile;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            sdCardDir = Environment.getExternalStorageDirectory(); // 获取跟目录
            imageFile = new File(sdCardDir, "treason/car/car_1_1.jpg");
            Log.i(TAG, "imageFile.getAbsolutePath() -> " + imageFile.getAbsolutePath());
            return imageFile.getAbsolutePath();
        } else {
            Log.i(TAG, "SD卡不存在");
            return null;
        }
    }

    public static Bitmap createRotateBitmap(Bitmap bitmap, PointF[] points, RectF rect) {
        PointF[] xIncrease = Arrays.copyOf(points, points.length);
        PointF[] yIncrease = Arrays.copyOf(points, points.length);

        Arrays.sort(xIncrease, new Comparator<PointF>() {
            @Override
            public int compare(PointF lhs, PointF rhs) {
                return (int) (lhs.x - rhs.x);
            }
        });
        Arrays.sort(yIncrease, new Comparator<PointF>() {

            @Override
            public int compare(PointF lhs, PointF rhs) {
                return (int) (lhs.y - rhs.y);
            }
        });
        rect.left = xIncrease[0].x;
        rect.right = xIncrease[xIncrease.length - 1].x;
        rect.top = yIncrease[0].y;
        rect.bottom = yIncrease[yIncrease.length - 1].y;
        float width = rect.width();
        float height = rect.height();
        Bitmap bit = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        PointF p1 = xIncrease[0];
        PointF p4 = xIncrease[xIncrease.length - 1];
        ArrayList<PointF> xList = new ArrayList<>(Arrays.asList(yIncrease));
        for (int i = xIncrease.length - 1; i >= 0; i--) {
            if (xList.get(i) == p1) {
                xList.remove(i);
            } else if (xList.get(i) == p4) {
                xList.remove(i);
            }
        }
        PointF p2 = xList.get(0);
        PointF p3 = xList.get(1);
        PointF temp;
        if (p2.y < p1.y) {
            temp = p2;
            p2 = p1;
            p1 = temp;
        }
        if (p3.y > p4.y) {
            temp = p3;
            p3 = p4;
            p4 = temp;
        }
        if (p3.y < p2.y) {
            temp = p3;
            p3 = p2;
            p2 = temp;
        }
        Canvas canvas = new Canvas(bit);
        float[] verts = new float[]{p1.x - rect.left, p1.y - rect.top, p2.x - rect.left, p2.y - rect.top,
                p3.x - rect.left, p3.y - rect.top, p4.x - rect.left, p4.y - rect.top,};
        canvas.drawBitmapMesh(bitmap, 1, 1, verts, 0, null, 0, null);
        return bit;
    }
}
