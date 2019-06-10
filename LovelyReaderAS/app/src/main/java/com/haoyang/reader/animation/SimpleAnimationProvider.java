package com.haoyang.reader.animation;

import android.graphics.Point;

import com.haoyang.reader.sdk.PageAnimationService;

abstract class SimpleAnimationProvider extends AnimationProvider {

	SimpleAnimationProvider(PageAnimationService pageAnimationService,
							int animationSpeed,
							float speedFactor) {

		super(pageAnimationService,
				animationSpeed,
				speedFactor);
	}

	@Override
	public boolean getPageToScrollTo(Point point) {

		return startX < point.x; // true: 上一页; false:下一页

//		return startX < point.x ? abstractReadService.getPageService().getPreviousPage()
//				: abstractReadService.getPageService().getNextPage();

	}

	@Override
	protected void startAnimatedScrollingInternal(float speed) {

		doStep();
	}

	@Override
	public void doStep() {

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

			if (this.endX - startX >= (screenWidth)) {
				terminate();
				return;
			}

			if (this.endX - startX >= (screenWidth - 200)) {
				this.speed = this.initSpeed;
				return;
			}

		} else { // 去下一页

			if (this.endX - this.startX <= (-screenWidth)) {
				terminate();
				return;
			}

			if (this.endX - startX < -(screenWidth - 200)) {
				this.speed = this.initSpeed;
				return;
			}
		}
		this.speed *= this.speedFactor;
	}
}
