/**
 * 
 */
package com.app.base.exception;

/**
 * 网连接不可用的异常 。
 * 
 * @author tianyu912@yeah.net
 */
public class NetworkUnAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NetworkUnAvailableException(String detailMessage) {
		super(detailMessage);
	}
	
}
