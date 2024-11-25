package com.farm.doc.dao;

import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.FarmRfDoctype;
import org.hibernate.Session;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import java.util.List;
import java.util.Map;

/**中间表-分类文档
 * @author MAC_wd
 *
 */
public interface FarmRfDoctypeDaoInter  {
	/** 删除一个实体
 	 * @param entity 实体
 	 */
 	public void deleteEntity(FarmRfDoctype entity) ;
	/** 由id获得一个实体
	 * @param id
	 * @return
	 */
	public FarmRfDoctype getEntity(String id) ;
	/** 插入一条数据
	 * @param entity
	 */
	public FarmRfDoctype insertEntity(FarmRfDoctype entity);
	/** 获得记录数量
	 * @return
	 */
	public int getAllListNum();
	/**修改一个记录
	 * @param entity
	 */
	public void editEntity(FarmRfDoctype entity);
	/**获得一个session
	 */
	public Session getSession();
	/**执行一条查询语句
	 */
	public DataResult runSqlQuery(DataQuery query);
	/**
	 * 条件删除实体，依据对象字段值(一般不建议使用该方法)
	 * 
	 * @param rules
	 *            删除条件
	 */
	public void deleteEntitys(List<DBRule> rules);

	/**
	 * 条件查询实体，依据对象字段值,当rules为空时查询全部(一般不建议使用该方法)
	 * 
	 * @param rules
	 *            查询条件
	 * @return
	 */
	public List<FarmRfDoctype> selectEntitys(List<DBRule> rules);

	/**
	 * 条件修改实体，依据对象字段值(一般不建议使用该方法)
	 * 
	 * @param values
	 *            被修改的键值对
	 * @param rules
	 *            修改条件
	 */
	public void updataEntitys(Map<String, Object> values, List<DBRule> rules);
	
	/**获得文档的分类
	 * @param docId 文档分类
	 * @return
	 */
	public List<FarmDoctype> getDocTypes(String docId);
	/**获得文档的分类
	 * @param docId 文档分类
	 * @return
	 */
	public FarmDoctype getDocType(String docId);
}