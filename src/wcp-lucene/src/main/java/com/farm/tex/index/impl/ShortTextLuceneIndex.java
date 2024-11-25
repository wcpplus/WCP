package com.farm.tex.index.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import com.farm.lucene.FarmLuceneFace;
import com.farm.lucene.adapter.DocMap;
import com.farm.lucene.common.IRResult;
import com.farm.lucene.server.DocIndexInter;
import com.farm.lucene.server.DocQueryInter;
import com.farm.tex.domain.Shorttext;
import com.farm.tex.domainex.AiQuestorMessage;
import com.farm.tex.domainex.EmbSearchResults;
import com.farm.tex.domainex.EmbSearchResults.INDEX_MODEL;
import com.farm.tex.index.ShortTextIndexInter;
import com.farm.util.web.FarmHtmlUtils;

public class ShortTextLuceneIndex implements ShortTextIndexInter {
	private static final Logger log = Logger.getLogger(ShortTextLuceneIndex.class);
	/**
	 * lucene的索引文件存储路径
	 */
	public static final String ShortIndexFileKey = "shortindex";

	@Override
	public void initIndex() {
		log.info("初始化： 模型LUCENE索引环境...");
	}

	@Override
	public void appendIndex(Shorttext text) {
		DocIndexInter index = null;
		try {
			FarmLuceneFace face = FarmLuceneFace.inctance();
			index = face.getDocIndex(face.getIndexPathFile(ShortIndexFileKey));
			index.indexDoc(getDocMap(text));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (index != null) {
					index.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		log.info("添加： 模型LUCENE索引...");
	}

	@Override
	public void remove(String id) {
		DocIndexInter index = null;
		try {
			FarmLuceneFace face = FarmLuceneFace.inctance();
			index = face.getDocIndex(face.getIndexPathFile(ShortIndexFileKey));
			index.deleteFhysicsIndex(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (index != null) {
					index.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		log.info("删除： 模型LUCENE索引...");
	}

	/**
	 * 获得一个知识的索引
	 * 
	 * @param doc
	 * @return
	 */
	private static DocMap getDocMap(Shorttext doc) {
		DocMap map = new DocMap(doc.getId());
		map.put("TEXT", doc.getText(), Store.YES, Index.ANALYZED);
		map.put("TITLE", doc.getTitle(), Store.YES, Index.ANALYZED);
		return map;
	}

	@Override
	public void finalsIndex() {
		DocIndexInter index = null;
		try {
			FarmLuceneFace face = FarmLuceneFace.inctance();
			index = face.getDocIndex(face.getIndexPathFile(ShortIndexFileKey));
			index.mergeIndex();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (index != null) {
					index.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		log.info("合并： 模型索引...");

	}

	@Override
	public EmbSearchResults search(AiQuestorMessage aiMessage) {
		try {
			EmbSearchResults result = new EmbSearchResults(INDEX_MODEL.LUCENE);
			String indexkey = aiMessage.getUserQuestion();
			log.info("AI问答：参考资料全文检索关键字：" + indexkey);
			FarmLuceneFace face = FarmLuceneFace.inctance();
			DocQueryInter query = face.getDocQuery(face.getIndexPathFile(ShortIndexFileKey));
			IRResult irResult = query.queryByMultiIndex(
					"WHERE(TITLE,TEXT=" + FarmHtmlUtils.HtmlRemoveTag(indexkey.trim()) + ")", 1, 200);
			int n = 0;
			for (Map<String, Object> node : irResult.getDataResult().getResultList()) {
				n++;
				result.add((String) node.get("ID"), n, (String) node.get("TITLE"));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().indexOf("没有找到索引文件") >= 0||e.getMessage().indexOf("no segments") >= 0) {
				Shorttext text = new Shorttext();
				text.setId("333");
				text.setText("333");
				text.setTitle("空索引");
				appendIndex(text);
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isLive(boolean isThrowException) {
		return true;
	}
}
