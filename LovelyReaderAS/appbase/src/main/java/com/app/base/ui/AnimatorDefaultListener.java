/**
 * 
 */
package com.app.base.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

/**
 * 动画监听的默认实现.
 * 需要使用动画监听时，继承这个类，需要那个方法就实现那个方法。
 * 为了保证代码囊洁。
 * 
 * @author tianyu912@yeah.net
 *
 */
public class AnimatorDefaultListener implements AnimatorListener {

	@Override
	public void onAnimationStart(Animator animation) {
	}

	@Override
	public void onAnimationEnd(Animator animation) {
	}

	@Override
	public void onAnimationCancel(Animator animation) {
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
	}
}
