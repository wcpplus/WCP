package com.farm.wcp.util;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.util.HtmlUtils;
import com.farm.lucene.adapter.DocMap;

/**
 * 生成全文检索时辅助生成索引元数据对象
 * 
 * @author Administrator
 * 
 */
public class LuceneDocUtil {
	public static DocMap getDocMap(DocEntire doc) {
		String text = "";
		if (doc.getTexts() != null) {
			text = doc.getTexts().getText1();
		}
		text = HtmlUtils.HtmlRemoveTag(text);
		DocMap map = new DocMap(doc.getDoc().getId());
		String typeAll = "";
		// 拼接所有上级分类用于索引
		if (doc.getCurrenttypes() != null) {
			for (FarmDoctype node : doc.getCurrenttypes()) {
				if (typeAll.equals("")) {
					typeAll = node.getName();
				} else {
					typeAll = typeAll + "/" + node.getName();
				}
			}
		}
		map.put("TYPENAME", typeAll, Store.YES, Index.ANALYZED);
		map.put("TAGKEY", doc.getDoc().getTagkey(), Store.YES, Index.ANALYZED);
		map.put("TITLE", doc.getDoc().getTitle(), Store.YES, Index.ANALYZED);
		map.put("AUTHOR", doc.getDoc().getAuthor(), Store.YES, Index.ANALYZED);
		map.put("DOCDESCRIBE", doc.getDoc().getDocdescribe(), Store.YES, Index.ANALYZED);
		map.put("VISITNUM", "0", Store.YES, Index.ANALYZED);
		map.put("PUBTIME", doc.getDoc().getPubtime(), Store.YES, Index.ANALYZED);
		map.put("USERID", doc.getDoc().getCuser(), Store.YES, Index.ANALYZED);
		map.put("DOMTYPE", doc.getDoc().getDomtype(), Store.YES, Index.ANALYZED);
		map.put("TEXT", text, Store.YES, Index.ANALYZED);
		return map;
	}
}
