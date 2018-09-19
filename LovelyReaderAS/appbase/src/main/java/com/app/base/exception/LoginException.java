/**
 * 
 */
package com.app.base.exception;


/**
 * 
 * @author Tianyu
 * email:tianyu912@yeah.net<br>
 * 
 * date:Aug 11, 2014 4:40:56 AM <br>
 */
public class LoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginException() {
		super();
	}

	public LoginException(String detailMessage) {
		super(detailMessage);
	}
}