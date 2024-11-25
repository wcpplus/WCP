package com.farm.tex.service;

import com.farm.tex.domain.Shorttext;
import com.farm.tex.domainex.EmbSearchResults;
import com.farm.core.sql.query.DataQuery;
import com.farm.llm.domain.LlmMessages;
import com.farm.llm.domain.LlmRefMaterials;
import com.farm.lucene.adapter.DocMap;

import java.util.List;

import com.farm.core.auth.domain.LoginUser;

/* *
 *功能：知识段落文本服务层接口
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
public interface ShorttextServiceInter {

	public void append(DocMap doc);

	public void remove(String docId);

	/**
	 * 删除实体管理实体
	 * 
	 * @param entity
	 */
	public void deleteShorttextEntity(String id, LoginUser user);

	/**
	 * 获得实体管理实体
	 * 
	 * @param id
	 * @return
	 */
	public Shorttext getShorttextEntity(String id);

	/**
	 * 创建一个基本查询用来查询当前实体管理实体
	 * 
	 * @param query 传入的查询条件封装
	 * @return
	 */
	public DataQuery createShorttextSimpleQuery(DataQuery query);

	/**
	 * @param id
	 * @param currentUser
	 */
	public void deleteEmbedding(String id, LoginUser currentUser);

	/**
	 * 获得未做向量的段落
	 * 
	 * @return
	 */
	public List<Shorttext> getNoEmbeddingTexts(int maxnum);

	/**
	 * 插入向量
	 * 
	 * @param id
	 * @param embedding
	 * @param modelkey
	 */
	public void editEmbedding(String id, byte[] embedding, int embeddinglen, String modelkey);

	/**
	 * 获取没有加载到向量库的数据
	 * 
	 * @return
	 */
	public List<Shorttext> getNoEmbDbTexts(int maxnum);

	/**
	 * 向量入库成功
	 * 
	 * @param text
	 */
	public void loadedIndexSuccess(Shorttext text);

	/**
	 * 重置状态（重置后可以重新加载向量到向量库中）
	 * 
	 * @param id
	 */
	public void reState(String id);

	/**
	 * 获得问答的参考资料
	 * 
	 * @param llmMsgs         用戶問題
	 * @param textids         參考資料的短文本id
	 * @param userReadTypeIds 用户权限id集合（一般是分类的id）
	 * @return
	 */
	public LlmRefMaterials getQuestionsByAi(LlmMessages llmMsgs, EmbSearchResults textids);

	/**
	 * 获得知识的展示地址
	 * 
	 * @param info
	 * @return
	 */
	public String getTextUrl(Shorttext text);

	/**
	 * 数据转换报错
	 * 
	 * @param id
	 */
	public void errorHandle(String id, String message);

}