package com.farm.doc.server;

import java.util.List;

import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDoctext;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.exception.CanNoDeleteException;
import com.farm.doc.exception.CanNoReadException;
import com.farm.doc.exception.CanNoWriteException;
import com.farm.doc.exception.DocNoExistException;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DataQuery;

/**
 * 文档管理
 * 
 * @author MAC_wd
 * 
 */
public interface FarmDocManagerInter {

	/**
	 * 修改文档(不做权限控制)
	 * 
	 * @param entity
	 *            标题、发布时间、内容类型是必填 texts中的TEXT1中存放超文本内容
	 * @param editNote
	 *            修改时的注释
	 * @param user
	 *            操作用户
	 * @return
	 */
	public DocEntire editDoc(DocEntire entity, String editNote, LoginUser user);

	/**
	 * 带权限的修改实体
	 * 
	 * @param entity
	 *            标题、发布时间、内容类型是必填 texts中的TEXT1中存放超文本内容
	 * @param editNote
	 *            修改备注(记录为啥修改)
	 * @param user
	 * @return
	 * @throws CanNoWriteException
	 */
	public DocEntire editDocByUser(DocEntire entity, String editNote, LoginUser user) throws CanNoWriteException;

	/**
	 * 删除文档(带权限)
	 * 
	 * @param entity
	 */
	public DocEntire deleteDoc(String entity, LoginUser user) throws CanNoDeleteException;

	/**
	 * 删除文档（不带权限删除）
	 * 
	 * @param entity
	 */
	public DocEntire deleteDocNoPop(String entity, LoginUser user) throws CanNoDeleteException;

	/**
	 * 创建文档
	 * 
	 * @param entity
	 *            标题、发布时间、内容类型是必填 texts中的TEXT1中存放超文本内容
	 * @param currentUser
	 * @return
	 */
	public DocEntire createDoc(DocEntire entity, LoginUser currentUser);


	/**
	 * 知识基本查询
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createSimpleDocQuery(DataQuery query);

	/**
	 * 获取文档数据 判断权限
	 * 
	 * @param id
	 * @param user
	 *            阅读用户
	 * @return
	 */
	public DocEntire getDoc(String docid, LoginUser user) throws CanNoReadException, DocNoExistException;

	/**
	 * 获取文档数据
	 * 
	 * @param id
	 * @return
	 */
	public Doc getDocOnlyBean(String id);

	/**
	 * 获取文档数据(不判断权限)
	 * 
	 * @param id
	 * @return
	 */
	@Deprecated
	public DocEntire getDoc(String id);

	/**
	 * 创建一个基本查询用来查询当前分类实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createSimpleTypeQuery(DataQuery query);

	/**
	 * 更新文档的分类（唯一分类）将会清空doc的其它分类
	 * 
	 * @param docid
	 * @param typeId
	 */
	public void updateDocTypeOnlyOne(String docid, String typeId);

	/**
	 * 获得文档的版本信息
	 * 
	 * @param docId
	 * @return ID,ETIME,CUSERNAME,DOCID,PCONTENT
	 */
	public List<FarmDoctext> getDocVersions(String docId);

	/**
	 * 获得文档版本信息
	 * 
	 * @param textId
	 * @param currentUser
	 * @return
	 */
	public DocEntire getDocVersion(String textId, LoginUser currentUser);

	/**
	 * 删除附件 void
	 */
	public void delImg(String imgid);

	

	/**
	 * 获得所有不带分类权限的知识
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery createAllPubDocQuery(DataQuery query);

	/**
	 * 移动文档到指定分类
	 * @param docIds 文档ID（多个ID用英文逗号分割）
	 * @param typeId 分类ID
	 * void
	 */
	public void move2Type(String docIds, String typeId);

	
}