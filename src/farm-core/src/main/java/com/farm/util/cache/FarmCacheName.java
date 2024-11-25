package com.farm.util.cache;

/**
 * 缓存名称
 * 
 * @author macpl
 *
 */
public enum FarmCacheName {
	//AI问答消息缓存
	wcpAiMessages("wcp-ai-messages"),
	//AI问答中的用户
	wcpAiUserTalking("wcp-ai-talkuser")
	;

	/**
	 * 持久缓存
	 */
	private String permanentCacheName;

	FarmCacheName(String permanentCacheName) {
		this.permanentCacheName = permanentCacheName;
	}

	/**
	 * 如果只有一个缓存就是这个持久缓缓存
	 * 
	 * @return
	 */
	public String getPermanentCacheName() {
		return permanentCacheName;
	}
}
