package com.farm.doc.dao;

import com.farm.doc.domain.FarmDocmessage;
import org.hibernate.Session;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import java.util.List;
import java.util.Map;

/**
 * 留言板
 * 
 * @author MAC_wd
 * 
 */
public interface FarmDocmessageDaoInter {
	/**
	 * 删除一个实体
	 * 
	 * @param entity
	 *            实体
	 */
	public void deleteEntity(FarmDocmessage entity);

	/**
	 * 由id获得一个实体
	 * 
	 * @param id
	 * @return
	 */
	public FarmDocmessage getEntity(String id);

	/**
	 * 插入一条数据
	 * 
	 * @param entity
	 */
	public FarmDocmessage insertEntity(FarmDocmessage entity);

	/**
	 * 获得记录数量
	 * 
	 * @return
	 */
	public int getAllListNum();

	/**
	 * 修改一个记录
	 * 
	 * @param entity
	 */
	public void editEntity(FarmDocmessage entity);

	/**
	 * 获得一个session
	 */
	public Session getSession();

	/**
	 * 执行一条查询语句
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
	public List<FarmDocmessage> selectEntitys(List<DBRule> rules);

	/**
	 * 条件修改实体，依据对象字段值(一般不建议使用该方法)
	 * 
	 * @param values
	 *            被修改的键值对
	 * @param rules
	 *            修改条件
	 */
	public void updataEntitys(Map<String, Object> values, List<DBRule> rules);

	/**
	 * 获得用户没有读的消息数量
	 * 
	 * @param userId 用户id
	 * @return
	 */
	public int getNoReadMessageNum(String userId);

	/**获得对应到某业务id的留言数
	 * @param appid
	 * @return
	 */
	public int getAppMessageNum(String appid);
}