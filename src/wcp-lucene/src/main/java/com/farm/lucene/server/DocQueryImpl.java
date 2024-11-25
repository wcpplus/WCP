package com.farm.lucene.server;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.farm.lucene.common.IRResult;
import com.farm.lucene.common.IqlAnalyse;
import com.farm.lucene.common.IqlAnalyseInter;
import com.farm.lucene.constant.IndexConf;

public class DocQueryImpl implements DocQueryInter {
	static boolean IsQueryAble = true;
	private static Logger log = Logger.getLogger(DocQueryImpl.class);
	private File indexFile = null;
	private List<File> indexFiles = null;

	private DocQueryImpl() {
	}

	/**
	 * 工厂类
	 * 
	 * @param indexFile
	 * @return
	 */
	public static DocQueryInter getInstance(File indexFile) {
		DocQueryImpl docQuery = new DocQueryImpl();
		docQuery.indexFile = indexFile;
		docQuery.indexFiles = new ArrayList<>();
		docQuery.indexFiles.add(indexFile);
		return docQuery;
	}

	/**
	 * 工厂类
	 * 
	 * @param indexFile
	 * @return
	 */
	public static DocQueryInter getInstance(List<File> indexFiles) {
		DocQueryImpl docQuery = new DocQueryImpl();
		docQuery.indexFiles = indexFiles;
		return docQuery;
	}

	@Override
	public IRResult query(String Iql, int currentPage, int pagesize)
			throws Exception {
		if (!IsQueryAble) {
			return IRResult.getInstance(1);
		}
		IndexSearcher searcher = null;
		Directory directory = null;
		IRResult Result = null;
		long startTime = new Date().getTime();

		try {
			log.debug("EXCUTE-IQL:" + Iql + " at " + indexFile.getPath());
			IqlAnalyseInter iqlAnalyse = new IqlAnalyse(Iql, currentPage,
					pagesize);
			directory = FSDirectory.open(indexFile);

			IndexReader reade = IndexReader.open(directory);
			searcher = new IndexSearcher(reade);
			QueryParser mqp = new MultiFieldQueryParser(Version.LUCENE_35,
					iqlAnalyse.getLimitTitle(), new IKAnalyzer());
			Query qp = mqp.parse(iqlAnalyse.getLimitValue());
			// 在索引器中使用IKSimilarity相似度评估器
			// searcher.setSimilarity( new IKAnalyzer())
			// 使用IKQueryParser查询分析器构造Query对象
			// 开始排序----------------------------------------------------
			Sort sort = iqlAnalyse.getSortTitle();
			TopFieldDocs search = searcher.search(qp, Integer
					.valueOf(iqlAnalyse.getMaxTopNum()), sort);
			// 开始结束----------------------------------------------------
			// // 搜索相似度最高的5条记录
			// TopDocs topDocs = isearcher.search(query, Integer
			// .valueOf(iqlAnalyse.getMaxTopNum()));
			ScoreDoc[] sco = search.scoreDocs;
			int totleSize = sco.length;
			// 将结果截取用户需要的
			sco = iqlAnalyse.subDoc(sco);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sco.length; i++) {
				Document targetDoc = searcher.doc(sco[i].doc);
				list.add(splitDoc(targetDoc, qp, iqlAnalyse.getLimitTitle()));
			}
			Result = IRResult.getInstance(currentPage);
			Result.setPageSize(pagesize);
			Result.setTotleSize(totleSize);
			Result.setResultList(list);

		} catch (IOException e) {
			log.error("商品索引检索：" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

		}
		long endTime = new Date().getTime();
		log.debug("共检索到记录" + Result.getTotleSize() + "条，用时"
				+ (endTime - startTime) + "毫秒");
		Result.setRuntime(endTime - startTime);
		Result.setTotalPage((Result.getTotleSize() - 1) / Result.getPageSize()
				+ 1);
		return Result;
	}

	protected Map<String, Object> splitDoc(Document node, Query query,
			String[] fields) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 高亮显示{--
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
				IndexConf.getString("config.file.lucene_highLight_simple_s"),
				IndexConf.getString("config.file.lucene_highLight_simple_e"));
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
				new QueryScorer(query));
		// --}
		for (Fieldable feild : node.getFields()) {
			String title = feild.name().toUpperCase();
			String value = node.get(feild.name());
			// 如果处理字段是ID的话就不加标红处理
			if (title.equals("ID")) {
				map.put(title, value);
				continue;
			}
			if (ArrayUtils.contains(fields, title)) {
				// 标红处理
				// 高亮词{--
				String highLight = null;
				/**
				 * <--------------------
				 * 2013-10-30王东，问题：页面上高亮关键字被截断导致样式扩散，处理：将下面语句从209行移植到该处
				 */
				int textLength = Integer.valueOf(IndexConf
						.getString("config.file.lucene_highLight_maxnum"));

				if (value.length() > textLength) {
					value = value.substring(0, textLength);
				}
				/**
				 * ---------------->
				 */
				// 做标题关键字标红
				{
					highlighter
							.setTextFragmenter(new SimpleFragmenter(
									Integer
											.valueOf(IndexConf
													.getString("config.file.lucene_highLight_maxnum"))));
					String text = node.get(title).replace("\n", "").replace(
							"\t", "").replace(" ", "");
					Analyzer analyzer = new IKAnalyzer();
					TokenStream tokenStream = analyzer.tokenStream(title
							.toUpperCase(), new StringReader(text));
					try {
						highLight = highlighter.getBestFragment(tokenStream,
								text);
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				}
				if (highLight != null) {
					value = highLight;
				}
			}
			// --}

			map.put(title, value);
		}
		return map;
	}

	@Override
	public IRResult queryByMultiIndex(String Iql, int currentPage, int pagesize)
			throws Exception {
		if (!IsQueryAble) {
			return IRResult.getInstance(1);
		}
		IndexSearcher searcher = null;
		Directory directory = null;
		IRResult Result = null;
		long startTime = new Date().getTime();

		try {
			log.debug("EXCUTE-IQL:" + Iql + " at " + indexFiles);
			IqlAnalyseInter iqlAnalyse = new IqlAnalyse(Iql, currentPage,
					pagesize);
			// 查询构造
			QueryParser mqp = new MultiFieldQueryParser(Version.LUCENE_35,
					iqlAnalyse.getLimitTitle(), new IKAnalyzer());
			Query qp = mqp.parse(iqlAnalyse.getLimitValue());
			// 开始排序----------------------------------------------------
			Sort sort = iqlAnalyse.getSortTitle();
			// 索引文件集合
			IndexReader[] readers = new IndexReader[indexFiles.size()];
			for (int i = 0; i < indexFiles.size(); i++) {
				if (!indexFiles.get(i).isDirectory()) {
					// 打开后立即关闭相当于初始化索引目录。
					DocIndexImpl.getInstance(indexFiles.get(i)).close();
				}
				directory = FSDirectory.open(indexFiles.get(i));
				IndexReader reade = IndexReader.open(directory);
				readers[i] = reade;
			}
			MultiReader mr = new MultiReader(readers);
			searcher = new IndexSearcher(mr);
			TopFieldDocs search = searcher.search(qp, Integer
					.valueOf(iqlAnalyse.getMaxTopNum()), sort);
			// 开始结束----------------------------------------------------
			// // 搜索相似度最高的5条记录
			// TopDocs topDocs = isearcher.search(query, Integer
			// .valueOf(iqlAnalyse.getMaxTopNum()));
			ScoreDoc[] sco = search.scoreDocs;
			int totleSize = sco.length;
			// 将结果截取用户需要的
			sco = iqlAnalyse.subDoc(sco);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sco.length; i++) {
				Document targetDoc = searcher.doc(sco[i].doc);
				list.add(splitDoc(targetDoc, qp, iqlAnalyse.getLimitTitle()));
			}
			Result = IRResult.getInstance(currentPage);
			Result.setPageSize(pagesize);
			Result.setTotleSize(totleSize);
			Result.setResultList(list);

		} catch (IOException e) {
			log.error("商品索引检索：" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

		}
		long endTime = new Date().getTime();
		log.debug("共检索到记录" + Result.getTotleSize() + "条，用时"
				+ (endTime - startTime) + "毫秒");
		Result.setRuntime(endTime - startTime);
		Result.setTotalPage((Result.getTotleSize() - 1) / Result.getPageSize()
				+ 1);
		return Result;
	}
}
