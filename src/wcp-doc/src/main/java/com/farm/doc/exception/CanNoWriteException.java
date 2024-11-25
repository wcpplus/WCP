package com.farm.doc.exception;

import com.farm.parameter.FarmParameterService;

/**
 * 没有修改权限异常
 * 
 * @author Administrator
 * 
 */
public class CanNoWriteException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CanNoWriteException(String message) {
		super(message);
	}

	public CanNoWriteException() {
		super(FarmParameterService.getInstance().getParameter(
				"title.com.farm.doc.exception.nowrite"));
	}
}
