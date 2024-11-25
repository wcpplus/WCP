package com.farm.doc.server;

import java.util.List;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.plus.domain.Doctypepop;

/**
 * 文档分类
 * 
 * @author 王东
 * @version 20140902
 */
public interface FarmDocTypeInter {

	/**
	 * 新增分类
	 * 
	 * @param entity
	 */
	public FarmDoctype insertType(FarmDoctype entity, LoginUser user);

	/**
	 * 修改分类
	 * 
	 * @param entity
	 */
	public FarmDoctype editType(FarmDoctype entity, LoginUser user);

	/**
	 * 删除分类实体
	 * 
	 * @param entity
	 */
	public void deleteType(String entity, LoginUser user);

	/**
	 * 获得分类实体
	 * 
	 * @param id
	 * @return
	 */
	public FarmDoctype getType(String id);

	/**
	 * 获得一个分类的所有上层节点序列（包含该分类，有排序）
	 * 
	 * @param typeid
	 * @return
	 */
	public List<FarmDoctype> getTypeAllParent(String typeid);

	/**
	 * 获取知识分类详细信息（带分类下知识数量）（下级分类和下下级别分类）
	 * 
	 * @param query
	 * @return
	 */
	public List<TypeBrief> getTypeInfos(LoginUser user, String parentId);

	/**
	 * 获得用户的分类下的知识
	 * 
	 * @return DOCID,title,DOCDESCRIBE,AUTHOR,PUBTIME,TAGKEY
	 *         ,IMGID,VISITNUM,PRAISEYES,PRAISENO,HOTNUM,EVALUATE,ANSWERINGNUM,
	 *         TYPENAME,DOMTYPE,TYPEID
	 */
	public DataResult getTypeDocs(LoginUser userid, String typeid, int pagesize, int currentPage);

	/**
	 * 查询分类的权限(检查该分类的所有上级节点，并获取权限信息)
	 * 
	 * @param query
	 * @param type
	 * @param typeid
	 * @return
	 */
	public List<Doctypepop> getTypePops(String typeid);

	/**
	 * 获得分类所有子节点
	 * 
	 * @param typeid
	 * @return
	 */
	public List<FarmDoctype> getAllSubNode(String typeid);

	/**
	 * 获取知识分类信息（带分类下知识数量）
	 * 
	 * @param query
	 * @return
	 */
	public List<TypeBrief> getPubTypes();
	/**
	 * 获取所有知识分类
	 * 
	 * @param query
	 * @return
	 */
	public List<TypeBrief> getTypes();
	/**
	 * 获得用户所有（被授权阅读的受限分类的分类的ID）（阅读） ///////受限分类是相对于为分配权限的分类（为分配权限的分类，所有人均可访问）
	 * 
	 * @param user
	 * @return
	 */
	public List<String> getUserReadTypeIds(LoginUser user);

	/**
	 * 获得用户所有（需要其审核的）分类的ID列表
	 * 
	 * @param userid
	 *            用户id
	 * @return
	 */
	public List<String> getUserAuditTypeIds(String userid);

	/**
	 * 获得分类的审核人ID列表
	 * 
	 * @param typeid
	 * @return
	 */
	public List<String> getPopTypeAudtUserIds(String typeid);

	/**
	 * 获取知识分类信息（带分类下知识数量）
	 * 
	 * @param user
	 *            user可空
	 * @return
	 */
	public List<TypeBrief> getPopTypesForReadDoc(LoginUser user);

	/**
	 * 获取知识分类信息（带分类下知识数量）用于创建知识时候的
	 * 
	 * @param user
	 * @return
	 */
	public List<TypeBrief> getTypesForWriteDoc(LoginUser user);

	/**
	 * 获得用户可以阅读的所有分类ID
	 * 
	 * @return
	 */
	public List<String> getTypesForUserRead(LoginUser user);

}
