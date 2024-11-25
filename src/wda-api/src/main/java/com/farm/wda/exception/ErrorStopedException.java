package com.farm.wda.exception;

/**
 * 因为错误而停止的活动
 * 
 * @author Administrator
 *
 */
public class ErrorStopedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorStopedException() {
		super();
	}

	public ErrorStopedException(String msg) {
		super(msg);
	}

}
