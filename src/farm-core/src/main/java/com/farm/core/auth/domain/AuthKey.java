package com.farm.core.auth.domain;

public interface AuthKey {
	/**
	 * 是否需要用户登录使用
	 * 
	 * @return
	 */
	public boolean isLogin();

	/**
	 * 是否需要检查用户权限
	 * 
	 * @return
	 */
	public boolean isCheck();

	/**
	 * 是否可用
	 * 
	 * @return
	 */
	public boolean isUseAble();

	/**
	 * 获得名称
	 * 
	 * @return
	 */
	public String getTitle();
}
