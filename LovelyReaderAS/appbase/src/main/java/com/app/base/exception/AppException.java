package com.app.base.exception;

/**
 * 自定义异常.
 * 
 * @author xdb
 * 
 */
public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8381669544579295490L;

	/**
	 * 
	 */
	public AppException() {
		super();
	}

	/**
	 * 对于例外的文字描述. 主调程序可打印此字串,以向用户解释操作失败的原因.
	 * 
	 * @param message
	 */
	public AppException(String message) {
		super(message);
	}

	// /**
	// * @param cause
	// */
	// public LeyunException(Throwable cause) {
	// super(cause);
	// }
	//
	// /**
	// * 对于例外的文字描述. 主调程序可打印此字串,以向用户解释操作失败的原因.
	// *
	// * @param message
	// * @param cause
	// */
	// public LeyunException(String message, Throwable cause) {
	// super(message, cause);
	// }

}
