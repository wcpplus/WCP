package com.farm.wcp.know.service;

import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DataQuery;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.exception.CanNoWriteException;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;

/**
 * 知识管理
 * 
 * @author MAC_wd
 * 
 */
public interface KnowServiceInter {
	public static final String LUCENE_DIR = "KNOW";

	/**
	 * 用户创建一条知识
	 * 
	 * @param knowtitle
	 *            知识标题
	 * @param knowtype
	 *            知识分类
	 * @param text
	 *            知识内容
	 * @param knowtag
	 *            知识标签
	 * @param pop_type_edit
	 *            编辑权限
	 * @param pop_type_read
	 *            阅读权限
	 * @param groupId
	 *            小组id（可以为空）
	 * @param currentUser
	 *            当前操作用户
	 * @return 文档对象
	 */
	public DocEntire creatKnow(String knowtitle, String knowtypeId, String text, String knowtag, POP_TYPE pop_type_edit,
			POP_TYPE pop_type_read, String groupId, LoginUser currentUser);

	/**
	 * 高级用户修改一条知识
	/**
	 * @param id
	 * @param knowtitle
	 * @param knowtype
	 * @param text
	 * @param knowtag
	 * @param pop_type_edit
	 * @param pop_type_read
	 * @param groupId
	 * @param currentUser
	 * @param editNote
	 * @return
	 * @throws CanNoWriteException
	 */
	public DocEntire editKnow(String id, String knowtitle, String knowtype, String text, String knowtag,
			POP_TYPE pop_type_edit, POP_TYPE pop_type_read, String groupId, LoginUser currentUser, String editNote)
					throws CanNoWriteException;

	/**
	 * 普通用户修改知识标签和内容
	 * 
	 * @param docid
	 *            知识主键
	 * @param text
	 *            内容
	 * @param knowtag
	 * @param currentUser
	 * @param editNote
	 *            修改备注
	 * @return
	 */
	public DocEntire editKnow(String docid, String text, String knowtag, LoginUser currentUser, String editNote)
			throws CanNoWriteException;


	/**
	 * 由网页获得一条知识
	 * 
	 * @param url
	 * @return
	 */
	public DocEntire getDocByWeb(String url, LoginUser user);

	/**
	 * 展示当前用户的知识
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery getMyDocQuery(DataQuery dataQuery, User user);
}