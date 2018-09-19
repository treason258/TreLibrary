/**
 * 
 */
package com.app.base.common.calculation;

/**
 * 用来定义两个点之间的距离.
 * 
 * @author tianyu912@yeah.net
 */
public class Space {

	/**
	 *  x,y轴需要增加或增小的距离.
	 */
	public float x_space = 0, y_space = 0;

	/**
	 * 
	 */
	public Space() {
		super();
	}

	/**
	 * 构造Space对象.
	 * @param x_space x轴需要增加或增小的距离.
	 * @param y_space y轴需要增加或增小的距离.
	 */
	public Space(float x_space, float y_space) {
		super();
		this.x_space = x_space;
		this.y_space = y_space;
	}
	
}
