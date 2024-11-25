package com.farm.doc.server;

import java.util.List;

import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.domain.ex.DocEntire;

public interface FarmFileIndexManagerInter {

	/**
	 * 添加一个附件索引
	 * 
	 * @param fileid
	 * @param docid
	 * @param text
	 */
	public void addFileLuceneIndex(String fileid, DocEntire doc, String text);

	/**
	 * 删除一个附件索引
	 * 
	 * @param fileid
	 * @param doc
	 */
	public void delFileLucenneIndex(String fileid, DocEntire doc);

	/**
	 * 修改知识分类后处理（主要为了处理移动分类）
	 * 
	 * @param oldDoce
	 *            旧的知识
	 * @param newdoc
	 */
	public void handleFileLucenneIndexBymoveType(DocEntire oldDoce, DocEntire newdoc);

	/**
	 * 删除一批附件索引，判断新的和原来的附件列表少了哪些，少的附件将被删除索引
	 * 
	 * @param originalfiles
	 *            原来的
	 * @param newfiles
	 *            新的
	 * @param doc
	 *            附件属于的知识
	 */
	public void delFileLucenneIndexs(List<FarmDocfile> originalfiles, List<FarmDocfile> newfiles, DocEntire doc);

	/**
	 * 删除一个附件索引，在所有索引文件中
	 * 
	 * @param fileid
	 *            文件id
	 */
	public void delFileLucenneIndex(String fileid);

}
