package com.haoyang.reader.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.haoyang.reader.sdk.PageAnimationService;

public abstract class AnimationProvider {

	/**
	 * 翻页动画不同状态的定义。
	 * 
	 * @author tianyu912@yeah.net
	 */
	public enum Mode {

		NoScrolling(false),
		PreManualScrolling(false),
		ManualScrolling(false),

		// 自动执行
		AutoScrollingForward(true), // 下一页
		AutoScrollingBackward(true), // 上一页
		CancelScrolling(true); // 取消这次分页

		public final boolean auto; // 是否是自动执行的动画。

		Mode(boolean auto) {

			this.auto = auto;
		}
	}

	protected Bitmap topBitmap, bottomBitmap; // 记录分页时，上下两个页的bitmap。

	protected final PageAnimationService pageAnimationService;

	protected float speedFactor;

	protected int startX; // 记录手指触摸屏幕时的开始点
	protected int startY;

	protected float endX; // 记录手指移动时手指所在的点。
	protected float endY;

	protected float finalEndX;

	protected int animationVelocity;
	protected int speed;

	protected int screenWidth;
	protected int screenHeight;

	protected Mode myMode = Mode.NoScrolling;

	protected Integer myColorLevel;
	protected Integer initSpeed;

	protected AnimationProvider(PageAnimationService pageAnimationService,
								int animationSpeed,
								float speedFactor) {

		this.pageAnimationService = pageAnimationService;

		this.animationVelocity = animationSpeed;
		this.speedFactor = speedFactor;
	}

	public Mode getMode() {
		return myMode;
	}

	public void terminate() {

		this.myMode = Mode.NoScrolling;
		this.speed = 0;
	}

	/**
	 * 手动动画即将开始，将动画的状态设成准备模式。
	 * 
	 * @param point
	 */
	public void startManualScrolling(Point point) {

		myMode = Mode.PreManualScrolling;
		endX = startX = point.x;
		endY = startY = point.y;
	}

	boolean recentForward = false; // 最近一次滑动的方向。

	/**
	 * 手指按下之后没有抬起，然后在屏幕上移动中.
	 * 
	 * @param point
	 */
	public final void scrollTo(Point point) {

		if (point.x - endX == 0) {
			return;
		}

		switch (myMode) {
		case ManualScrolling:

			float moveDistance = point.x - endX;

			if (moveDistance != 0) {
				this.recentForward = !(moveDistance > 0);
			}

			endX = point.x; // 手指移动时，最后手指所在的点.
			endY = point.y;
			break;
		case PreManualScrolling:

			endX = point.x;
			endY = point.y;

			this.myMode = detectManualMode();
			break;
		default:
			break;
		}
	}

	/**
	 * 检查当前的动画状态。
	 */
	protected Mode detectManualMode() {

		final int moveDistance = (int) Math.abs(startX - endX);

		if (moveDistance > 0) {

			return Mode.ManualScrolling;
		}

		return Mode.PreManualScrolling;
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

		// 检查自动动画是向前滚动还是向后滚动.
		final float moveDistance = endPoint.x - startX;

		boolean forward = !(moveDistance > 0);

		if (forward == this.recentForward) {

			myMode = forward ? Mode.AutoScrollingForward
					: Mode.AutoScrollingBackward;

			this.speed = forward ? -this.animationVelocity
					: this.animationVelocity; // 下一页: 负数; 上一页：正数

		} else {

			myMode = Mode.CancelScrolling;
			this.speed = this.recentForward ? -this.animationVelocity
					: this.animationVelocity;
		}

		this.initSpeed = this.speed;
		
		this.startAnimatedScrolling(forward, endPoint);
	}

	/**
	 *
	 * @param forward
	 *            前一页还是后一页。
	 */
	protected void startAnimatedScrolling(boolean forward, Point endPoint) {

		finalEndX = endPoint.x; // 记录手指抬起时那个点的x坐标。

		// 记录自动动画执行过程中，动画页和静止页的bitmap。
		// 后边动画执行过程中是一直绘制这两个bitmap。

		this.topBitmap = this.pageAnimationService.getCurrentBitmapPage().getBitmap();

		// Log.d("aaa", "forward : " + forward);

		if (forward) {

			this.bottomBitmap = this.pageAnimationService.getNextBitmapPage().getBitmap();

		} else if (!forward) {

			this.bottomBitmap = this.pageAnimationService.getPreviousBitmapPage().getBitmap();
		}

		startAnimatedScrollingInternal(this.speed);
	}

	protected abstract void startAnimatedScrollingInternal(float speed);

	public void computeScroll() {
	}

	/**
	 * 查看动画是否还在运行中。
	 * 
	 * @return
	 */
	public boolean inProgress() {

		switch (myMode) {
		case NoScrolling:
		case PreManualScrolling:

			return false;
		default:
			return true;
		} // end switch
	}

	/**
	 * 查看动画是否还在运行中。
	 * 
	 * @return
	 */
	public boolean inAutoAnimating() {

		switch (myMode) {
		case NoScrolling:
		case PreManualScrolling:
		case ManualScrolling:

			return false;
		default:
			return true;
		} // end switch
	}

	public void singleClickScroll(Point currentPoint,
								  int velocity,
								  boolean forward) {

		if (forward) { // 下一页

			this.startX = 0;
			this.startY = 0;

			this.endX = -1;
			this.endY = -1;
			this.myMode = Mode.AutoScrollingForward;
		} else { // 上一页

			this.startX = -1;
			this.startY = -1;

			this.endX = 0;
			this.endY = 0;
			this.myMode = Mode.AutoScrollingBackward;
		}

		// 下一页: 负数; 上一页：正数
		this.speed = forward ? -this.animationVelocity : this.animationVelocity;
		this.initSpeed = this.speed;
		
		startAnimatedScrolling(forward, currentPoint);
	}

	public final void init(int width,
						   int height,
						   Integer colorLevel) {

		screenWidth = width;
		screenHeight = height;
		myColorLevel = colorLevel;
	}

	public abstract void doStep();

	public final void draw(Canvas canvas) {

		drawInternal(canvas);
	}

	protected abstract void drawInternal(Canvas canvas);

	/**
	 * 确认是向上一页走，还是向下一页走  true: 上一页; false:下一页
	 * 
	 * @param point
	 * @return
	 */
	public abstract boolean getPageToScrollTo(Point point);


	private Point endPoint = new Point();
	/**
	 *
	 * 
	 * @return
	 */
	public final boolean getPageToScrollTo() {

		endPoint.x = (int)endX;
		endPoint.y  = (int)endY;

		return getPageToScrollTo(endPoint);
	}

	protected Bitmap getBitmapFrom() {

		return this.pageAnimationService.getCurrentBitmapPage().getBitmap();
	}

	/**
	 * 获取滚动到页面的bitmap.
	 * 
	 * @return
	 */
	protected Bitmap getBitmapTo() {

		boolean direction = getPageToScrollTo();

		// Log.d("aaaa", "getBitmapTo : " + pageIndex);


		if (direction == true) {

			return this.pageAnimationService.getPreviousBitmapPage().getBitmap();
		}

		return this.pageAnimationService.getNextBitmapPage().getBitmap();
	}

	protected void drawBitmapFrom(Canvas canvas,
								  int x,
								  int y,
								  Bitmap bitmap,
								  Paint paint) {

		this.pageAnimationService.drawBitmap(canvas, x, y, bitmap, paint);
	}

	/**
	 * 绘制当前页面.也就是动画中动的那个页面。
	 */
	protected void drawBitmapFrom(Canvas canvas, int x, int y, Paint paint) {

		this.pageAnimationService.drawBitmap(canvas,
											x,
											y,
											this.pageAnimationService.getCurrentBitmapPage().getBitmap(),
											paint);

	}

	protected void drawBitmapTo(Canvas canvas,
								int x,
								int y,
								Bitmap bitmap,
								Paint paint) {

		this.pageAnimationService.drawBitmap(canvas, x, y, bitmap, paint);
	}

	/**
	 * 绘制将要进入的页面，根据手指的滑动实时计算出来的。
	 */
	protected void drawBitmapTo(Canvas canvas, int x, int y, Paint paint) {

		Bitmap bitmap = null;

		bitmap = getBitmapTo();

		this.pageAnimationService.drawBitmap(canvas, x, y, bitmap, paint);
	}

	public Bitmap getTopBitmap() {
		return topBitmap;
	}

	public Bitmap getBottomBitmap() {
		return bottomBitmap;
	}
}
