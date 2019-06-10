package com.haoyang.reader.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.app.base.common.util.Size;

public class BitmapService {

	public static Bitmap createBitmap(int width, int height, Config c) {
		try {
			return Bitmap.createBitmap(width, height, c);
		} catch (OutOfMemoryError e) {
			System.gc();
			System.gc();
			return Bitmap.createBitmap(width, height, c);
		}
	}

	public static Bitmap createBitmap(int width, int height) {
		return createBitmap(width, height, Config.RGB_565);
	}

	public static Bitmap createRoundConerColor(int mRadius, Size size,
			int colorValue) {

		return createRoundConerColor(mRadius, size, colorValue, false, -1, 0);
	}

	/**
	 * 根据原图添加圆角
	 *
	 * @return
	 */
	public static Bitmap createRoundConerColor(int mRadius, Size size,
			int colorValue, boolean isRound, int roundColorValue,
			int strokeWidth) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(colorValue);

		Bitmap target = Bitmap.createBitmap(size.width, size.height,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, size.width, size.height);
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);

		if (!isRound) {
			return target;
		}

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		paint.setColor(roundColorValue);
		rect = new RectF(0, 0, size.width, size.height);
		paint.setStrokeWidth(strokeWidth); // 设置“空心”的外框的宽度
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);

		return target;
	}

	public static Bitmap createRoundConerImage(int mRadius, Size size,
			Bitmap source) {

		return createRoundConerImage(mRadius, size, source, false, -1, 0);
	}

	/**
	 * 根据原图添加圆角
	 * 
	 * @param source
	 * @return
	 */
	public static Bitmap createRoundConerImage(int mRadius, Size size,
			Bitmap source, boolean isRound, int roundColorValue, int strokeWidth) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap target = Bitmap.createBitmap(size.width, size.height,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, size.width, size.height);

		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		canvas.drawBitmap(source, 0, 0, paint);

		if (!isRound) {
			return target;
		}

		paint.setColor(roundColorValue);

		// 设置“空心”的外框的宽度
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.STROKE);

		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		return target;
	}
}
