package com.farm.tex.domainex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.farm.util.web.FarmHtmlUtils;

public class EmbSearchResults {

	/**
	 * 参考资料中textid集合，顺序为相关度
	 */
	private List<String> ids;
	/**
	 * 最优参考资料ids集合
	 */
	private Set<String> excellentIds;
	/**
	 * <textid,语义距离得分，越小越接近>
	 */
	private Map<String, Float> scores;

	private Map<String, String> titles;

	private INDEX_MODEL indexModel;

	public INDEX_MODEL getIndexModel() {
		return indexModel;
	}

	public enum INDEX_MODEL {
		LUCENE, MILVUS
	}

	public Map<String, String> getTitles() {
		return titles;
	}

	/**
	 * 标题序列
	 */

	public EmbSearchResults(INDEX_MODEL indexModel) {
		ids = new ArrayList<String>();
		excellentIds =new HashSet<String>();
		scores = new HashMap<String, Float>();
		titles = new HashMap<String, String>();
		this.indexModel = indexModel;
	}

	public String getTitle(String id) {
		return titles.get(id);
	}

	public void add(String id, float score, String title) {
		ids.add(id);
		scores.put(id, score);
		if (title != null) {
			titles.put(id, FarmHtmlUtils.HtmlRemoveTag(title));
		}
	}

	public void add(String id, float score) {
		ids.add(id);
		scores.put(id, score);
	}
	
	public void addExcellentId(String id) {
		excellentIds.add(id);
	}

	public List<String> getIds() {
		return ids;
	}

	public Float getScores(String id) {
		return scores.get(id);
	}
	public Set<String> getExcellentIds() {
		return excellentIds;
	}
}
