/**
 * 
 */
package com.app.base.exception;

/**
 * @author asd
 *
 */
public class TokenFailureException extends Exception {

	private static final long serialVersionUID = 8381669544579295490L;

	public TokenFailureException() {
		super();
	}
	 
	public TokenFailureException(String message) {
		super(message);
	}

}
