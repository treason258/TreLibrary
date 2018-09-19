/**
 * 
 */
package com.app.base.graphics;

/**
 * 
 * @author tianyu912@yeah.net
 */
public class CoordinateService {
	
	
	
	public boolean isInRect(Coordinate ci, RectF_Extended rect) {
		
		return ci.x > rect.left && ci.x < rect.right && ci.y > rect.top && ci.y < rect.bottom;
	}
}
