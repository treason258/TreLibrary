/**
 * 
 */
package com.app.base.common.service;

import com.app.base.common.util.Size;

/**
 * @author tianyu
 *
 */
public class ImageThumbnail {

	public Size thumbnailSize;
	public int inSampleSize;

	public ImageThumbnail() {
		super();
		this.thumbnailSize = new Size();
		this.inSampleSize = 0;
	}
}
