/**
 * 
 */
package com.app.base.common.util;

/**
 * 
 * @author tianyu
 */
public class Size {

	public int width;

	public int height;

	public Size() {
	}

	public Size(int height) {
		super();
		this.height = height;
	}

	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
