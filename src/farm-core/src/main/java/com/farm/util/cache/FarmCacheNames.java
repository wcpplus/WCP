package com.farm.util.cache;

/**
 * 缓存名称(有持久缓存的)
 * 
 * @author macpl
 *
 */
public enum FarmCacheNames {
	;
	
	/**
	 * 持久缓存
	 */
	private String permanentCacheName;
	/**
	 * 动态缓存
	 */
	private String liveCacheName;

	FarmCacheNames(String permanentCacheName) {
		this.permanentCacheName = permanentCacheName;
		this.liveCacheName = permanentCacheName + "-live";
	}

	/**
	 * 如果只有一个缓存就是这个持久缓缓存
	 * 
	 * @return
	 */
	public String getPermanentCacheName() {
		return permanentCacheName;
	}

	/**
	 * 动态缓存，短时间的缓存
	 * 
	 * @return
	 */
	public String getLiveCacheName() {
		return liveCacheName;
	}

}
