package com.farm.lucene.server;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.farm.lucene.adapter.DocMap;
import com.farm.tex.ShortTextFactory;
import com.farm.tex.index.impl.ShortTextLuceneIndex;

public class DocIndexImpl implements DocIndexInter {
	private static final Logger log = Logger.getLogger(DocIndexImpl.class);
	/**
	 * 索引目录
	 */
	private File indexDir = null;
	/**
	 * 中文分词器
	 */
	private static Analyzer analyzer = new IKAnalyzer();
	/**
	 * 临时内存索引
	 */
	private IndexWriter ramWriter = null;
	/**
	 * 临时内存索引目录
	 */
	private Directory ramdirectory = null;

	private DocIndexImpl() {
	}

	/**
	 * 工厂构造器(创建该对象后执行close即可以初始化索引目录)
	 * 
	 * @param indexDir
	 *            索引文件路径
	 * @return
	 * @throws IOException
	 */
	public static DocIndexInter getInstance(File indexDir) throws IOException {
		DocIndexImpl indexFace = new DocIndexImpl();
		indexFace.indexDir = indexDir;
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		indexFace.ramdirectory = new RAMDirectory();
		indexFace.ramWriter = new IndexWriter(indexFace.ramdirectory, iwc);
		if (!indexDir.exists()) {
			indexDir.mkdirs();
		}
		return indexFace;
	}

	@Override
	public void indexDoc(DocMap doc) throws Exception {
		// 建立新索引
		Document document = new Document();
		{
			for (String key : doc.keySet()) {
				Field field = new Field(key, doc.getValue(key) == null ? " " : doc.getValue(key), doc.getStore(key),
						doc.getIndex(key));
				document.add(field);
			}
		}
		ramWriter.addDocument(document);
		if(indexDir.getPath().indexOf(ShortTextLuceneIndex.ShortIndexFileKey) < 0) {
			//只有知识索引才添加到段落索引中
			//生成一个文本段落
			ShortTextFactory.getInctance().append(doc);
		}
		log.debug("索引引擎" + "建立内存索引:" + doc.getInfo().replace("\n", ""));
	}

	/**
	 * 创建IndexWriter文件对象并初始化配置索引配置
	 * 
	 * @return
	 * @throws IOException
	 */
	private IndexWriter openFSIndexWriter() throws IOException {
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		return new IndexWriter(FSDirectory.open(indexDir), iwc);
	}

	@Override
	public void close() throws Exception {
		IndexWriter fswriter = null;
		try {
			// 将内存索引合并到物理索引
			ramWriter.close();
			fswriter = openFSIndexWriter();
			fswriter.addIndexes(new Directory[] { ramdirectory });
			log.debug("关闭索引" + "，并建立硬盘索引:" + indexDir);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			ramWriter.close();
			fswriter.close();
		}
	}

	@Override
	public void deleteFhysicsIndex(String docId) throws Exception {
		long startTime = System.currentTimeMillis();
		Directory dir = FSDirectory.open(indexDir);
		if (!IndexReader.indexExists(dir)) {
			return;
		}

		IndexReader reader = IndexReader.open(dir, false);
		Term term = new Term("ID", docId);
		reader.deleteDocuments(term);
		reader.flush();
		reader.close();
		long endTime = System.currentTimeMillis();
		if (indexDir.getPath().indexOf(ShortTextLuceneIndex.ShortIndexFileKey) < 0) {
			// 只有知识索引才添加到段落索引中
			// 生成一个文本段落
			ShortTextFactory.getInctance().remove(docId);
		}
		log.debug("删除磁盘索引ID:" + docId + ",total time: " + (endTime - startTime) + " ms");
	}

	@Override
	public void deleteRamIndex(String docId) throws Exception {
		long startTime = System.currentTimeMillis();
		Directory dir = ramdirectory;
		IndexReader reader = IndexReader.open(dir, false);
		Term term = new Term("ID", docId);
		reader.deleteDocuments(term);
		reader.flush();
		reader.close();
		long endTime = System.currentTimeMillis();
		log.debug("删除内存索引ID:" + docId + ",total time: " + (endTime - startTime) + " ms");
	}
	
	
	
	@Override
	public void mergeIndex() {
		IndexWriter indexWriter = null;
		try {
			Directory index2 = FSDirectory.open(indexDir);
			Directory merged = FSDirectory.open(indexDir);
			indexWriter = new IndexWriter(merged, new IKAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
			indexWriter.addIndexes(new Directory[] { index2 });
			indexWriter.optimize();
		} catch (Exception e) {
			System.out.println("合并索引出错！");
			e.printStackTrace();
		} finally {
			try {
				if (indexWriter != null)
					indexWriter.close();
			} catch (Exception e) {
			}
		}
	}
}
