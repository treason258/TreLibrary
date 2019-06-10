package com.haoyang.reader.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.haoyang.reader.sdk.PageAnimationService;

public final class ShiftAnimationProvider extends SimpleAnimationProvider {

	private final Paint myPaint = new Paint();

	public ShiftAnimationProvider(PageAnimationService pageAnimationService,
								  int animationSpeed,
								  float speedFactor) {
		
		super(pageAnimationService,
				animationSpeed,
				speedFactor);

		myPaint.setColor(Color.rgb(127, 127, 127));
	}

	public void singleClickScroll(Point currentPoint, int velocity,
			boolean forward) {

		if (forward) { // 下一页

			this.startX = currentPoint.x;
			this.startY = currentPoint.y;

			this.endX = currentPoint.x;
			this.endY = currentPoint.y;
			this.myMode = Mode.AutoScrollingForward;
		} else { // 上一页

			this.startX = currentPoint.x;
			this.startY = currentPoint.y;

			this.endX = currentPoint.x;
			this.endY = currentPoint.y;
			this.myMode = Mode.AutoScrollingBackward;
		}

		// 下一页: 负数; 上一页：正数
		this.speed = forward ? -velocity : velocity;

		super.startAnimatedScrolling(forward, currentPoint);
	}

	@Override
	protected void drawInternal(Canvas canvas) {

		final int dX = (int) (endX - startX);
		int x = dX > 0 ? dX - screenWidth : dX + screenWidth;

		if (this.myMode == Mode.ManualScrolling) {

			drawBitmapTo(canvas, x, 0, myPaint);

			drawBitmapFrom(canvas, dX, 0, myPaint);

			return;
		}

		Bitmap b = null;

		b = this.getBottomBitmap();

		drawBitmapTo(canvas, x, 0, b, myPaint); // 绘制上一页或下一页的 bitmap

		b = this.getTopBitmap();

		drawBitmapFrom(canvas, dX, 0, b, myPaint); // 绘制当前页,也就是当前正在动画的页。
	}

	@Override
	public final void doStep() {

		if (!getMode().auto) {
			return;
		}

		this.endX += this.speed;

		if (this.myMode == Mode.CancelScrolling) {

			if (this.speed > 0) { // 去上一页

				if (this.endX >= startX) {
					terminate();
					return;
				}
			} else { // 去下一页
				if (this.endX < startX) {
					terminate();
					return;
				}
			}

		} else if (this.speed > 0) { // 去上一页

			if (this.endX >= (screenWidth + startX)) {
				terminate();
				return;
			}

			// if (this.endX >= (screenWidth + startX - 300)) {
			// this.speed = this.initSpeed;
			// return;
			// }

		} else { // 去下一页
			if (this.endX <= (-screenWidth + startX)) {
				terminate();
				return;
			}

			// if (this.endX - startX < -(screenWidth - 300)) {
			// this.speed = this.initSpeed;
			// return;
			// }
		}

		this.speed *= this.speedFactor; // 不让加速
	}
}
