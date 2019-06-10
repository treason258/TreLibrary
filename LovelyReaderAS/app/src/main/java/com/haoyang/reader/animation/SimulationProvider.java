package com.haoyang.reader.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.haoyang.reader.sdk.PageAnimationService;

public class SimulationProvider extends AnimationProvider {

	private int mCornerX = 0; // 拖拽点对应的页脚
	private int mCornerY = 0;

	private Path mPath0;
	private Path mPath1;

	PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
	PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点

	PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	PointF mBezierControl2 = new PointF();
	PointF mBeziervertex2 = new PointF();
	PointF mBezierEnd2 = new PointF();

	float mMiddleX;
	float mMiddleY;
	float mDegrees;
	float mTouchToCornerDis;

	ColorMatrixColorFilter mColorMatrixFilter;
	Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	boolean mIsRTandLB; // 是否属于右上左下
	float mMaxLength = (float) Math.hypot(1080, 1920); // 计算一直角三角形的斜边长度
	// float mMaxLength
	int[] mBackShadowColors;
	int[] mFrontShadowColors;

	GradientDrawable mBackShadowDrawableLR;
	GradientDrawable mBackShadowDrawableRL;
	GradientDrawable mFolderShadowDrawableLR;
	GradientDrawable mFolderShadowDrawableRL;

	GradientDrawable mFrontShadowDrawableHBT;
	GradientDrawable mFrontShadowDrawableHTB;
	GradientDrawable mFrontShadowDrawableVLR;
	GradientDrawable mFrontShadowDrawableVRL;

	private Paint mPaint;

	private Bitmap currentPageBitmap, targetPageBitmap;
	private boolean forward = false;

	public SimulationProvider(PageAnimationService pageAnimationService,
							  int animationSpeed,
							  float speedFactor) {

		super(pageAnimationService,
				animationSpeed,
				speedFactor);

		mPath0 = new Path();
		mPath1 = new Path();

		this.createDrawable();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();

		float[] array = {0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0};
		cm.set(array);

		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();

		endX = 0.01f; // 不让x,y为0,否则在点计算时会有问题
		endY = 0.01f;
	}

	public void drawInternal(Canvas canvas) {

		canvas.drawColor(0xFFAAAAAA);
		calcPoints();

		if (this.myMode == Mode.ManualScrolling) {

			Bitmap currentPageBitmap = super.getBitmapFrom();

			drawCurrentPageArea(canvas, currentPageBitmap, mPath0);

			drawNextPageAreaAndShadow(canvas, this.targetPageBitmap);
			drawCurrentPageShadow(canvas);
			drawCurrentBackArea(canvas, currentPageBitmap);
			return;
		}

		Bitmap currentPageBitmap = super.getTopBitmap();
		Bitmap nextPageBitmap = super.getBottomBitmap();

		drawCurrentPageArea(canvas, currentPageBitmap, mPath0);
		drawNextPageAreaAndShadow(canvas, nextPageBitmap);
		drawCurrentPageShadow(canvas);
		drawCurrentBackArea(canvas, currentPageBitmap);
	}

	public void singleClickScroll(Point currentPoint, int speed, boolean forward) {

		this.forward = forward;

		// 设定一个假的点击点。
		if (forward) { // 下一页

			currentPoint.x = 1000;
			currentPoint.y = 1800;
		} else { // 上一页

			currentPoint.x = 100;
			currentPoint.y = 1800;
		}

		startManualScrolling(currentPoint);

		this.myMode = Mode.ManualScrolling; // 设置这一步是让循环跑起来.
		this.recentForward = this.forward;

		startAnimatedScrolling(currentPoint);

	}

	@Override
	public void startManualScrolling(Point point) {

		this.mMaxLength = (float) Math.hypot(screenWidth, screenHeight);

		super.startManualScrolling(point);

		float half = screenWidth / 2.0f;
		if (point.x >= half) {

			forward = true;

			targetPageBitmap = this.pageAnimationService.getNextBitmapPage().getBitmap();
		} else {

			forward = false;
			targetPageBitmap = this.pageAnimationService.getPreviousBitmapPage().getBitmap();
		}

		this.calcCornerXY(point.x, point.y);
	}

	/**
	 * 动画自动执行的开始。
	 * 
	 * @param endPoint
	 */
	public void startAnimatedScrolling(Point endPoint) {

		if (myMode != Mode.ManualScrolling) {
			return;
		}

		if (forward) {

			if (!this.recentForward) {
				myMode = Mode.CancelScrolling;
				this.speed = animationVelocity;
			} else {
				myMode = Mode.AutoScrollingForward;
				this.speed = -animationVelocity;
			}
		} else {

			if (this.recentForward) {
				myMode = Mode.CancelScrolling;
				this.speed = -animationVelocity;
			} else {
				myMode = Mode.AutoScrollingBackward;
				this.speed = animationVelocity;
			}
		}

		this.startAnimatedScrolling(forward, endPoint);
	}

	/**
	 * @param forward
	 *            前一页还是后一页。
	 */
	protected final void startAnimatedScrolling(boolean forward, Point endPoint) {

		finalEndX = endPoint.x; // 记录手指抬起时那个点的x坐标。

		// 记录自动动画执行过程中，动画页和静止页的bitmap。
		// 后边动画执行过程中是一直绘制这两个bitmap。
		this.topBitmap = this.pageAnimationService.getCurrentBitmapPage().getBitmap();
		if (forward) {

			this.bottomBitmap = this.pageAnimationService.getNextBitmapPage().getBitmap();

		} else if (!forward) {

			this.bottomBitmap = this.pageAnimationService.getPreviousBitmapPage().getBitmap();
		}

		startAnimatedScrollingInternal(this.speed);
	}

	protected void startAnimatedScrollingInternal(float speed) {

		startAnimation();
		targetPageBitmap = null;
	}

	private void startAnimation() {

		doStep();
	}

	public final void doStep() {

		if (!getMode().auto) {
			return;
		}

		endX += speed * 4;

		if (this.myMode == Mode.CancelScrolling) {

			if (this.speed > 0) { // 去上一页

				if (this.endX >= this.startX) {
					terminate();
					return;
				}
			} else { // 去下一页
				if (this.endX <= this.startX) {
					terminate();
					return;
				}
			}

		} else if (this.speed > 0) { // 去上一页

			if (this.endX >= (screenWidth * 3)) {

				terminate();
				return;
			}
			// if (this.endX >= (screenWidth * 3 - 300)) {
			//
			// this.speed = this.initSpeed;
			// return;
			// }
		} else { // 去下一页

			if (this.endX <= (-screenWidth * 2)) {

				terminate();
				return;
			}
			// if (this.endX <= -(screenWidth * 2 - 300)) {
			//
			// this.speed = this.initSpeed;
			// return;
			// }
		}
		// this.speed *= this.speedFactor;
	}

	public boolean getPageToScrollTo(Point point) {

		return this.forward;

//		return this.forward ? abstractReadService.getPageService().getPreviousPage()
//				: abstractReadService.getPageService().getNextPage();

	}

	/**
	 * 是否从左边翻向右边
	 */
	public boolean DragToRight() {

		return mCornerX <= 0;
	}

	/**
	 * 求解直线P1P2和直线P3P4的交点坐标
	 */
	private PointF getCross(PointF P2, PointF P3, PointF P4) {

		PointF CrossP = new PointF();
		// 二元函数通式： y=ax+b
		float a1 = (P2.y - endY) / (P2.x - endX);
		float b1 = ((endX * P2.y) - (P2.x * endY)) / (endX - P2.x);

		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}

	/**
	 * 计算拖拽点对应的拖拽脚
	 */
	private void calcCornerXY(float x, float y) {

		if (x <= screenWidth / 2) {
			mCornerX = 0;
		} else {
			mCornerX = screenWidth;
		}

		if (y <= screenHeight / 2)
			mCornerY = 0;
		else
			mCornerY = screenHeight;

		mIsRTandLB = (mCornerX == 0 && mCornerY == screenHeight)
				|| (mCornerX == screenWidth && mCornerY == 0);
	}

	private void calcPoints() {

		mMiddleX = (endX + mCornerX) / 2;
		mMiddleY = (endY + mCornerY) / 2;

		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
				* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
				* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
				/ 2;
		mBezierStart1.y = mCornerY;

		// 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
		// 如果继续翻页，会出现BUG故在此限制
		if (endX > 0 && endX < this.screenWidth) {
			if (mBezierStart1.x < 0 || mBezierStart1.x > this.screenWidth) {
				if (mBezierStart1.x < 0)
					mBezierStart1.x = this.screenWidth - mBezierStart1.x;

				float f1 = Math.abs(mCornerX - endX);
				float f2 = this.screenWidth * f1 / mBezierStart1.x;
				endX = Math.abs(mCornerX - f2);

				float f3 = Math.abs(mCornerX - endX)
						* Math.abs(mCornerY - endY) / f1;
				endY = Math.abs(mCornerY - f3);

				mMiddleX = (endX + mCornerX) / 2;
				mMiddleY = (endY + mCornerY) / 2;

				mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
						* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				mBezierControl1.y = mCornerY;

				mBezierControl2.x = mCornerX;
				mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
						* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

				mBezierStart1.x = mBezierControl1.x
						- (mCornerX - mBezierControl1.x) / 2;
			}
		}

		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
				/ 2;

		mTouchToCornerDis = (float) Math.hypot((endX - mCornerX),
				(endY - mCornerY)); // 计算直角三角形的斜边长

		mBezierEnd1 = getCross(mBezierControl1, mBezierStart1, mBezierStart2);
		mBezierEnd2 = getCross(mBezierControl2, mBezierStart1, mBezierStart2);

		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}

	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {

		mPath0.reset();

		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y); // 移动画笔到此处。

		// 用于绘制圆滑曲线，即贝塞尔曲线。
		// 其中，x1，y1为控制点的坐标值，x2，y2为终点的坐标值；
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
				mBezierEnd1.y);

		mPath0.lineTo(endX, endY); // 用于进行直线绘制。
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);

		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
				mBezierStart2.y);

		mPath0.lineTo(mCornerX, mCornerY);

		mPath0.close(); // 回到初始点形成封闭的曲线

		canvas.save();

		canvas.clipPath(path, Region.Op.XOR); // 是全集形状减去交集形状之后的部分
		canvas.drawBitmap(bitmap, 0, 0, null);

		canvas.restore();
	}

	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {

		mPath1.reset();
		mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.lineTo(mCornerX, mCornerY);
		mPath1.close();

		mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
				- mCornerX, mBezierControl2.y - mCornerY));
		int leftx;
		int rightx;
		GradientDrawable mBackShadowDrawable;
		if (mIsRTandLB) {
			leftx = (int) (mBezierStart1.x);
			rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
			mBackShadowDrawable = mBackShadowDrawableLR;
		} else {
			leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
			rightx = (int) mBezierStart1.x;
			mBackShadowDrawable = mBackShadowDrawableRL;
		}
		canvas.save();

		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
				(int) (mMaxLength + mBezierStart1.y));
		mBackShadowDrawable.draw(canvas);

		canvas.restore();
	}

	/**
	 * 创建阴影的GradientDrawable
	 */
	private void createDrawable() {

		int[] color = { 0x333333, 0xb0333333 };

		mFolderShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFolderShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0xff111111, 0x111111 };

		mBackShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableVRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(
				GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	public void drawCurrentPageShadow(Canvas canvas) {

		double degree;

		if (mIsRTandLB) {
			degree = Math.PI
					/ 4
					- Math.atan2(mBezierControl1.y - endY, endX
							- mBezierControl1.x);
		} else {
			degree = Math.PI
					/ 4
					- Math.atan2(endY - mBezierControl1.y, endX
							- mBezierControl1.x);
		}

		// 翻起页阴影顶点与touch点的距离
		double d1 = (float) 25 * 1.414 * Math.cos(degree);
		double d2 = (float) 25 * 1.414 * Math.sin(degree);
		float x = (float) (endX + d1);
		float y;

		if (mIsRTandLB) {
			y = (float) (endY + d2);
		} else {
			y = (float) (endY - d2);
		}

		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(endX, endY);
		mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
		mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.close();
		float rotateDegrees;
		canvas.save();

		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		int leftx;
		int rightx;
		GradientDrawable mCurrentPageShadow;
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl1.x);
			rightx = (int) mBezierControl1.x + 25;
			mCurrentPageShadow = mFrontShadowDrawableVLR;
		} else {
			leftx = (int) (mBezierControl1.x - 25);
			rightx = (int) mBezierControl1.x + 1;
			mCurrentPageShadow = mFrontShadowDrawableVRL;
		}

		rotateDegrees = (float) Math.toDegrees(Math.atan2(endX
				- mBezierControl1.x, mBezierControl1.y - endY));
		canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
		mCurrentPageShadow.setBounds(leftx,
				(int) (mBezierControl1.y - mMaxLength), rightx,
				(int) (mBezierControl1.y));
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(endX, endY);
		mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.close();
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		if (mIsRTandLB) {
			leftx = (int) (mBezierControl2.y);
			rightx = (int) (mBezierControl2.y + 25);
			mCurrentPageShadow = mFrontShadowDrawableHTB;
		} else {
			leftx = (int) (mBezierControl2.y - 25);
			rightx = (int) (mBezierControl2.y + 1);
			mCurrentPageShadow = mFrontShadowDrawableHBT;
		}
		rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y
				- endY, mBezierControl2.x - endX));
		canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
		float temp;
		if (mBezierControl2.y < 0)
			temp = mBezierControl2.y - this.screenHeight;
		else
			temp = mBezierControl2.y;

		int hmg = (int) Math.hypot(mBezierControl2.x, temp);
		if (hmg > mMaxLength)
			mCurrentPageShadow
					.setBounds((int) (mBezierControl2.x - 25) - hmg, leftx,
							(int) (mBezierControl2.x + mMaxLength) - hmg,
							rightx);
		else
			mCurrentPageShadow.setBounds(
					(int) (mBezierControl2.x - mMaxLength), leftx,
					(int) (mBezierControl2.x), rightx);

		mCurrentPageShadow.draw(canvas);
		canvas.restore();
	}

	/**
	 * 绘制翻起页背面
	 */
	private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {

		int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
		float f1 = Math.abs(i - mBezierControl1.x);
		int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
		float f2 = Math.abs(i1 - mBezierControl2.y);
		float f3 = Math.min(f1, f2);
		mPath1.reset();
		mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
		mPath1.lineTo(endX, endY);
		mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath1.close();
		GradientDrawable mFolderShadowDrawable;
		int left;
		int right;
		if (mIsRTandLB) {
			left = (int) (mBezierStart1.x - 1);
			right = (int) (mBezierStart1.x + f3 + 1);
			mFolderShadowDrawable = mFolderShadowDrawableLR;
		} else {
			left = (int) (mBezierStart1.x - f3 - 1);
			right = (int) (mBezierStart1.x + 1);
			mFolderShadowDrawable = mFolderShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		mPaint.setColorFilter(mColorMatrixFilter);

		float dis = (float) Math.hypot(mCornerX - mBezierControl1.x,
				mBezierControl2.y - mCornerY);
		float f8 = (mCornerX - mBezierControl1.x) / dis;
		float f9 = (mBezierControl2.y - mCornerY) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
		mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
		canvas.drawBitmap(bitmap, mMatrix, mPaint);

		mPaint.setColorFilter(null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
				(int) (mBezierStart1.y + mMaxLength));
		mFolderShadowDrawable.draw(canvas);
		canvas.restore();
	}
}