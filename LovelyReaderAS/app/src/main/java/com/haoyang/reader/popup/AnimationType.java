/**
 * 
 */
package com.haoyang.reader.sdk;

/**
 * 开发者指定阅读器翻页动画效果类型的定义。
 * 
 * @author tianyu912@yeah.net
 */
public enum AnimationType {

	/**
	 * 没有翻页效果。
	 */
	NoneAnimation(1),
	/**
	 * 仿真翻页。
	 */
	CurlAnimation(2),
	/**
	 * 覆盖翻页。
	 */
	SlideAnimation(3),
	
	/**
	 * 滚动翻页。
	 */
	ScrollAnimation(4);

	private int value;

	AnimationType(int value) {
		this.value = value;
	}

	/**
	 * 获取翻页动画的值。
	 * @return
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * 数值到翻页动画类型的转换。
	 * @param value
	 * @return
	 */
	public static AnimationType valueOf(int value) {

		switch (value) {
		case 1:
			return NoneAnimation;
		case 2:
			return CurlAnimation;
		case 3:
			return SlideAnimation;
		case 4:
			return ScrollAnimation;

		default:
			return CurlAnimation;
		}
	}
}
