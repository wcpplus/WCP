package com.farm.doc.server.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.authority.FarmAuthorityService;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.result.ResultsHandle;
import com.farm.doc.dao.FarmDocDaoInter;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.ex.DocBrief;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.server.FarmDocIndexInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.util.HtmlUtils;
import com.farm.doc.util.LuceneDocUtil;
import com.farm.lucene.FarmLuceneFace;
import com.farm.lucene.adapter.DocMap;
import com.farm.lucene.server.DocIndexInter;
import com.farm.lucene.server.DocQueryImpl;
import com.farm.lucene.server.DocQueryInter;
import com.farm.util.web.WebHotCase;

@Service
public class FarmDocIndexManagerImpl implements FarmDocIndexInter {
	@Resource
	private FarmDocDaoInter farmDocDao;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	private final static Logger log = Logger.getLogger(FarmDocIndexManagerImpl.class);

	@Override
	@Transactional
	public List<DocBrief> getRelationDocs(String docid, int num) {
		Doc doc = farmDocDao.getEntity(docid);
		List<File> file = new ArrayList<File>();
		List<String> types = farmDocTypeManagerImpl.getTypesForUserRead(getLoginUser(null));
		for (String typeid : types) {
			file.add(FarmLuceneFace.inctance().getIndexPathFile("TYPE" + typeid));
		}
		DocQueryInter query = DocQueryImpl.getInstance(file);
		String iql = null;
		if (iql == null) {
			iql = "WHERE(TITLE,TEXT=" + HtmlUtils.HtmlRemoveTag(doc.getTitle().trim()).replaceAll(":", "") + ")";
		}
		try {
			DataResult result = query.queryByMultiIndex(iql, 1, num).getDataResult();
			result.runHandle((new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					row.put("DOCID", row.get("ID"));
				}
			}));
			return result.getObjectList(DocBrief.class);
		} catch (Exception e) {
			log.error(e.toString());
			return new ArrayList<>();
		}
	}

	private LoginUser getLoginUser(String userId) {
		if (userId == null || userId.isEmpty()) {
			return null;
		} else {
			return FarmAuthorityService.getInstance().getUserById(userId);
		}
	}

	@Override
	@Transactional
	public DataResult search(String word, String userid, Integer pagenum) throws Exception {
		// TITLE,PUBTIME,VISITNUM,TYPENAME,AUTHOR,TAGKEY,DOCDESCRIBE,TEXT
		List<File> files = new ArrayList<File>();
		if (userid != null) {
			DataResult groups = farmDocgroupManagerImpl.getGroupsByUser(userid, 1000, 1);
			for (Map<String, Object> node : groups.getResultList()) {
				File file = FarmLuceneFace.inctance().getIndexPathFile("GROUP" + (String) node.get("ID"));
				if (file.isDirectory()) {
					files.add(file);
				}
			}
		}
		List<String> types = farmDocTypeManagerImpl.getTypesForUserRead(getLoginUser(userid));
		for (String typeid : types) {
			files.add(FarmLuceneFace.inctance().getIndexPathFile("TYPE" + typeid));
		}
		DocQueryInter query = DocQueryImpl.getInstance(files);
		String iql = null;
		word = HtmlUtils.HtmlRemoveTag(word.trim());
		if (word.indexOf("TYPE:") >= 0 && iql == null) {
			word = word.substring(word.indexOf("TYPE:") + 5).replaceAll(":", "");
			iql = "WHERE(TYPENAME=" + word + ")";
		}
		if (word.indexOf("TAG:") >= 0 && iql == null) {
			word = word.substring(word.indexOf("TYPE:") + 5).replaceAll(":", "");
			iql = "WHERE(TAGKEY=" + word + ")";
		}
		if (word.indexOf("AUTHOR:") >= 0 && iql == null) {
			word = word.substring(word.indexOf("AUTHOR:") + 7).replaceAll(":", "");
			iql = "WHERE(AUTHOR=" + word + ")";
		}
		if (word.indexOf("TITLE:") >= 0 && iql == null) {
			word = word.substring(word.indexOf("TITLE:") + 6).replaceAll(":", "");
			iql = "WHERE(TITLE=" + word + ")";
		}
		if (iql == null) {
			// word.substring(word.indexOf("TYPE:"));
			iql = "WHERE(TITLE,TEXT,TAGKEY,TYPENAME,AUTHOR=" + word.replaceAll(":", "") + ")";
		}
		if (pagenum == null) {
			pagenum = 1;
		}
		WebHotCase.putCase(word);
		DataResult result = query.queryByMultiIndex(iql, pagenum, 10).getDataResult();
		for (Map<String, Object> node : result.getResultList()) {
			String tags = node.get("TAGKEY").toString();
			String text = node.get("TEXT").toString();
			node.put("DOCDESCRIBE", text.length() > 256 ? text.substring(0, 256) : text);
			if (tags != null && tags.trim().length() > 0) {
				String[] tags1 = tags.trim().replaceAll("，", ",").replaceAll("、", ",").split(",");
				node.put("TAGKEY", Arrays.asList(tags1));
			} else {
				node.put("TAGKEY", new ArrayList<String>());
			}
		}
		return result;
	}

	/**
	 * 添加索引
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void addLuceneIndex(DocEntire entity) {
		// 做索引
		{
			DocIndexInter typeindex = null;
			DocIndexInter groupindex = null;
			if (entity.getCurrenttypes() == null && entity.getType() != null && entity.getType().getId() != null) {
				// 构造文档分类
				entity.setCurrenttypes(farmDocTypeManagerImpl.getTypeAllParent(entity.getType().getId()));
			}
			try {
				FarmLuceneFace luceneImpl = FarmLuceneFace.inctance();
				if (entity.getType() != null && entity.getType().getId() != null) {
					// 创建分类索引
					typeindex = luceneImpl.getDocIndex(luceneImpl.getIndexPathFile("TYPE" + entity.getType().getId()));
					typeindex.indexDoc(LuceneDocUtil.getDocMap(entity));
				}
				if (entity.getGroup() != null && !entity.getGroup().getId().isEmpty()) {
					// 创建小组索引
					groupindex = luceneImpl
							.getDocIndex(luceneImpl.getIndexPathFile("GROUP" + entity.getGroup().getId()));
					groupindex.indexDoc(LuceneDocUtil.getDocMap(entity));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (groupindex != null) {
						groupindex.close();
					}
					if (typeindex != null) {
						typeindex.close();
					}
				} catch (Exception e1) {
					log.error("lucene error:" + e1);
				}
			}
		}
	}

	@Override
	@Transactional
	public void addLuceneIndex(String fileid, String name, String text, DocEntire entity) {
		// 做索引
		{
			DocIndexInter typeindex = null;
			DocIndexInter groupindex = null;
			try {
				FarmLuceneFace luceneImpl = FarmLuceneFace.inctance();
				if (entity.getType() != null && entity.getType().getId() != null) {
					// 创建分类索引
					typeindex = luceneImpl.getDocIndex(luceneImpl.getIndexPathFile("TYPE" + entity.getType().getId()));
					DocMap map = LuceneDocUtil.getDocMap(entity);
					typeindex.indexDoc(LuceneDocUtil.convertFileMap(map, fileid, name, text));
				}
				if (entity.getGroup() != null && !entity.getGroup().getId().isEmpty()) {
					// 创建小组索引
					groupindex = luceneImpl
							.getDocIndex(luceneImpl.getIndexPathFile("GROUP" + entity.getGroup().getId()));
					DocMap map = LuceneDocUtil.getDocMap(entity);
					groupindex.indexDoc(LuceneDocUtil.convertFileMap(map, fileid, name, text));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (groupindex != null) {
						groupindex.close();
					}
					if (typeindex != null) {
						typeindex.close();
					}
				} catch (Exception e1) {
					log.error("lucene error:" + e1);
				}
			}
		}

	}

	@Override
	@Transactional
	public void delLuceneIndex(String docid, List<String> typeIdList, List<String> groupIdList) {
		// 做索引
		{
			DocIndexInter index = null;
			DocIndexInter groupindex = null;
			FarmLuceneFace luceneImpl = FarmLuceneFace.inctance();
			if (typeIdList != null) {
				for (String typeid : typeIdList) {
					try {
						// 删除分类索引
						index = luceneImpl.getDocIndex(luceneImpl.getIndexPathFile("TYPE" + typeid));
						index.deleteFhysicsIndex(docid);
					} catch (Exception e) {
						throw new RuntimeException(e + "删除索引");
					} finally {
						try {
							if (index != null) {
								index.close();
							}
						} catch (Exception e1) {
							log.error("lucene error:" + e1);
						}
					}
				}
			}
			if (groupIdList != null) {
				for (String groupid : groupIdList) {
					try {
						// 删除小组索引
						groupindex = luceneImpl.getDocIndex(luceneImpl.getIndexPathFile("GROUP" + groupid));
						groupindex.deleteFhysicsIndex(docid);
					} catch (Exception e) {
						throw new RuntimeException(e + "删除索引");
					} finally {
						try {
							if (groupindex != null) {
								groupindex.close();
							}
						} catch (Exception e1) {
							log.error("lucene error:" + e1);
						}
					}
				}
			}

		}

	}

	@Override
	@Transactional
	public void delLuceneIndex(DocEntire doc) {
		delLuceneIndex(doc, doc.getDoc().getId());
	}

	@Override
	@Transactional
	public void delLuceneIndex(DocEntire doc, String indexId) {
		// 做索引
		{
			DocIndexInter index = null;
			DocIndexInter groupindex = null;
			try {
				FarmLuceneFace luceneImpl = FarmLuceneFace.inctance();
				if (doc.getType() != null && doc.getType().getId() != null) {
					// 删除分类索引
					index = luceneImpl.getDocIndex(luceneImpl.getIndexPathFile("TYPE" + doc.getType().getId()));
					index.deleteFhysicsIndex(indexId);
				}
				if (doc.getDoc().getDocgroupid() != null && !doc.getDoc().getDocgroupid().isEmpty()) {
					// 删除小组索引
					groupindex = luceneImpl
							.getDocIndex(luceneImpl.getIndexPathFile("GROUP" + doc.getDoc().getDocgroupid()));
					groupindex.deleteFhysicsIndex(indexId);
				}
			} catch (Exception e) {
				throw new RuntimeException(e + "删除索引");
			} finally {
				try {
					if (groupindex != null) {
						groupindex.close();
					}
					if (index != null) {
						index.close();
					}
				} catch (Exception e1) {
					log.error("lucene error:" + e1);
				}
			}
		}

	}

	/**
	 * 重做索引
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void reLuceneIndex(DocEntire olddoc, DocEntire newdoc) {
		delLuceneIndex(olddoc);
		addLuceneIndex(newdoc);
	}
}
