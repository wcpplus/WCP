package com.farm.tex.domainex;

/**封装少量的短知识信息（用于检索资料查询时的预查询）
 * @author Wd
 *
 */
public class ShortTextLessInfo {

	private String id;
	private Integer len;
	private String typeid;

	public ShortTextLessInfo(String id, Integer len, String typeid) {
		this.id = id;
		this.len = len;
		this.typeid = typeid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getLen() {
		return len;
	}

	public void setLen(Integer len) {
		this.len = len;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

}
