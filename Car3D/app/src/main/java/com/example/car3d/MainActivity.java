package com.example.car3d;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  protected static final String TAG = "MainActivity";

  // 图片容器
  private ImageView imageView;

  // 开始按下位置
  private float startX;
  // 当前位置
  private float currentX;

  // 当前图片的编号
  private int scrNum;
  // 图片的总数
  private static int maxNum;

  // 资源图片集合
  private int[] srcs = new int[] {
      R.drawable.car0,
      R.drawable.car1,
      R.drawable.car2,
      R.drawable.car3,
      R.drawable.car4,
      R.drawable.car5,
      R.drawable.car6,
      R.drawable.car7,
      R.drawable.car8,
      R.drawable.car9,
      R.drawable.car10,
      R.drawable.car11,
      R.drawable.car12,
      R.drawable.car13,
      R.drawable.car14,
      R.drawable.car15
  };

  private ArrayList<Bitmap> bitmaps = new ArrayList<>();

  private GestureDetector gestureDetector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    imageView = (ImageView) findViewById(R.id.imageView);

    // 初始化当前图片的编号
    scrNum = 1;
    // 初始化图片的总数
    maxNum = srcs.length;

    // 读取图片到内存
    for (int src : srcs) {
      bitmaps.add(BitmapFactory.decodeResource(getResources(), src));
    }

    gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
      @Override public boolean onDown(MotionEvent event) {
        //Log.d(TAG, "onDown | event.getX() -> " + event.getX() + " | event.getY() -> " + event.getY());
        return false;
      }

      @Override public void onShowPress(MotionEvent event) {
        //Log.d(TAG, "onShowPress | event.getX() -> " + event.getX() + " | event.getY() -> " + event.getY());
      }

      @Override public boolean onSingleTapUp(MotionEvent event) {
        //Log.d(TAG, "onSingleTapUp | event.getX() -> " + event.getX() + " | event.getY() -> " + event.getY());
        return false;
      }

      @Override public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        //Log.d(TAG, "onScroll | event1.getX() -> " + event1.getX() + " | event2.getX() -> " + event2.getX() + " | velocityX -> " + distanceX + " | velocityY -> " + distanceY);
        return false;
      }

      @Override public void onLongPress(MotionEvent event) {
        //Log.d(TAG, "onLongPress | event.getX() -> " + event.getX() + " | event.getY() -> " + event.getY());
      }

      @Override public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        //Log.d(TAG, "onFling | event1.getX() -> " + event1.getX() + " | event2.getX() -> " + event2.getX() + " | velocityX -> " + velocityX + " | velocityY -> " + velocityY);
        return false;
      }
    });

    imageView.setOnTouchListener(new View.OnTouchListener() {
      @SuppressLint("ClickableViewAccessibility")
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch | event.getX() -> " + event.getX() + " | event.getY() -> " + event.getY() + " | event.getAction() -> " + event.getAction());
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            startX = event.getX();
            break;
          case MotionEvent.ACTION_MOVE:
            currentX = event.getX();
            // 判断手势滑动方向，并切换图片
            if (currentX - startX > 10) {
              modifySrcL();
              // 重置起始位置
              startX = event.getX();
            } else if (currentX - startX < -10) {
              modifySrcR();
              // 重置起始位置
              startX = event.getX();
            }
            break;
          case MotionEvent.ACTION_UP:
            break;
        }
        return true;
      }
    });
  }

  /**
   * 向右滑动修改资源
   */
  private void modifySrcR() {
    if (scrNum > maxNum) {
      scrNum = 1;
    }

    if (scrNum > 0) {
      //imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), srcs[scrNum - 1]));
      imageView.setImageBitmap(bitmaps.get(scrNum - 1));
      scrNum++;
    }
  }

  /**
   * 向左滑动修改资源
   */
  private void modifySrcL() {
    if (scrNum <= 0) {
      scrNum = maxNum;
    }

    if (scrNum <= maxNum) {
      //imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), srcs[scrNum - 1]));
      imageView.setImageBitmap(bitmaps.get(scrNum - 1));
      scrNum--;
    }
  }
}
