package com.haoyang.reader.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;

import com.haoyang.reader.sdk.PageAnimationService;

/**
 * 覆盖动画。
 * 
 * @author tianyu912@yeah.net
 *
 *         Dec 4, 2017
 */
public final class SlideAnimationProvider extends SimpleAnimationProvider {

	private final Paint myDarkPaint = new Paint();
	private final Paint myPaint = new Paint();

	public SlideAnimationProvider(PageAnimationService pageAnimationService,
								  int animationSpeed,
								  float speedFactor) {

		super(pageAnimationService,
				animationSpeed,
				speedFactor);
	}

	@Override
	protected void drawInternal(Canvas canvas) {

		final int dX = (int) (endX - startX);
		// final int dX = (int)endX;

		if (this.myMode == Mode.ManualScrolling) {

			// 如果是手动滑动，那么就实时计算，当前要滑向的页。
			drawBitmapTo(canvas, 0, 0, myDarkPaint); // 绘制上一页或下一页的 bitmap
			drawBitmapFrom(canvas, dX, 0, myPaint); // 绘制当前页,也就是当前正在动画的页。
			drawShadowVertical(canvas, 0, screenHeight, dX);

			return;
		} else {

			// 如果是自动动画，那么就用开始自动动画时记录的bitmap来绘制。

			Bitmap b = this.getBottomBitmap();
			drawBitmapTo(canvas, 0, 0, b, myDarkPaint); // 绘制上一页或下一页的 bitmap

			b = this.getTopBitmap();
			drawBitmapFrom(canvas, dX, 0, b, myPaint); // 绘制当前页,也就是当前正在动画的页。

			drawShadowVertical(canvas, 0, screenHeight, dX);
		}
	}

	private void drawShadowVertical(Canvas canvas, int top, int bottom, int dX) {

		final GradientDrawable.Orientation orientation = dX > 0 ? GradientDrawable.Orientation.RIGHT_LEFT
				: GradientDrawable.Orientation.LEFT_RIGHT;
		final int[] colors = new int[] { 0x46000000, 0x00000000 };
		final GradientDrawable gradient = new GradientDrawable(orientation,
				colors);
		gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		gradient.setDither(true);
		if (dX > 0) {
			gradient.setBounds(dX - 16, top, dX, bottom);
		} else {
			gradient.setBounds(screenWidth + dX, top, screenWidth + dX + 16,
					bottom);
		}
		gradient.draw(canvas);
	}

	// private void setDarkFilter(int visible, int full) {
	//
	// int darkColorLevel = 145 + 100 * Math.abs(visible) / full;
	// if (myColorLevel != null) {
	// darkColorLevel = darkColorLevel * myColorLevel / 0xFF;
	// }
	// ViewUtil.setColorLevel(myDarkPaint, darkColorLevel);
	// }
	//
	// private void drawShadowHorizontal(Canvas canvas, int left, int right, int
	// dY) {
	//
	// final GradientDrawable.Orientation orientation = dY > 0 ?
	// GradientDrawable.Orientation.BOTTOM_TOP
	// : GradientDrawable.Orientation.TOP_BOTTOM;
	// final int[] colors = new int[] { 0x46000000, 0x00000000 };
	// final GradientDrawable gradient = new GradientDrawable(orientation,
	// colors);
	// gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	// gradient.setDither(true);
	// if (dY > 0) {
	// gradient.setBounds(left, dY - 16, right, dY);
	// } else {
	// gradient.setBounds(left, screenHeight + dY, right, screenHeight
	// + dY + 16);
	// }
	// gradient.draw(canvas);
	// }
}
