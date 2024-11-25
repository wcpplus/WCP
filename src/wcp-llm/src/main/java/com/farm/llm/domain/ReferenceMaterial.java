package com.farm.llm.domain;

/**
 * 參考資料描述
 * 
 * @author Wd
 *
 */
public class ReferenceMaterial {
	// 格式"STEXT+Id"
	private String markId;
	private String title;
	private String url;
	private String text;
	// 是否最佳答案
	private boolean excellent = false;
	//是否在对话中已经展示（没有的话可以主动展示在回答最后方）
	private boolean isShow = false;

	public ReferenceMaterial() {
	}

	public void setMarkId(String markId) {
		this.markId = markId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMarkId() {
		return markId;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getText() {
		return text;
	}

	public boolean isExcellent() {
		return excellent;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public void setExcellent(boolean excellent) {
		this.excellent = excellent;
	}
}
