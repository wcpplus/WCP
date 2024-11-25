package com.farm.wda.exception;

/**
 * 文档类型错误
 * 
 * @author Administrator
 *
 */
public class ErrorTypeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8445889251576068034L;

	public ErrorTypeException() {
		super();
	}

	/**
	 * @param msg 支持类型
	 */
	public ErrorTypeException(String msg) {
		super(msg);
	}
}
