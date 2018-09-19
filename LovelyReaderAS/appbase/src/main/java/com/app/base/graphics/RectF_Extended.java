/**
 * 
 */
package com.app.base.graphics;

import android.graphics.Rect;
import android.graphics.RectF;


/**
 * 
 * 自定义的矩形实体.
 * 增加了通过坐标来实例化矩形的构造方法.
 * 
 * @author tianyu912@yeah.net
 */
public class RectF_Extended extends RectF {

	public RectF_Extended() {
		super();

	}

	public RectF_Extended(float left, float top, float right, float bottom) {
		super(left, top, right, bottom);

	}

	public RectF_Extended(Rect r) {
		super(r);

	}

	public RectF_Extended(RectF r) {
		super(r);

	}

	/**
	 * 通过左上角和右下角坐标来实例化矩形.
	 * @param leftTop 左上角坐标
	 * @param rightBottom 右下角坐标
	 */
	public RectF_Extended(Coordinate leftTop, Coordinate rightBottom) {

		this(leftTop, rightBottom, 0);
	}
	
	/**
	 * 通过左上角和右下角坐标来实例化矩形,两个坐标可以通过space参数向外扩.
	 * @param leftTop 左上角坐标
	 * @param rightBottom 右下角坐标
	 * @param space 矩形向外扩的大小.
	 */
	public RectF_Extended(Coordinate leftTop, Coordinate rightBottom, float space) {

		left = leftTop.x - space;
		top = leftTop.y - space;

		right = rightBottom.x + space;
		bottom = rightBottom.y + space;
	}
}
