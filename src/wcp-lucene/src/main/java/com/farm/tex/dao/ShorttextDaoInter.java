package com.farm.tex.dao;

import com.farm.tex.domain.Shorttext;
import org.hibernate.Session;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import java.util.List;
import java.util.Map;

/* *
 *功能：知识段落文本数据库持久层接口
 *详细：
 *
 *版本：v0.1
 *作者：Farm代码工程自动生成
 *日期：20150707114057
 *说明：
 */
public interface ShorttextDaoInter {
	/**
	 * 删除一个知识段落文本实体
	 * 
	 * @param entity 实体
	 */
	public void deleteEntity(Shorttext shorttext);

	/**
	 * 由知识段落文本id获得一个知识段落文本实体
	 * 
	 * @param id
	 * @return
	 */
	public Shorttext getEntity(String shorttextid);

	/**
	 * 插入一条知识段落文本数据
	 * 
	 * @param entity
	 */
	public Shorttext insertEntity(Shorttext shorttext);

	/**
	 * 获得记录数量
	 * 
	 * @return
	 */
	public int getAllListNum();

	/**
	 * 修改一个知识段落文本记录
	 * 
	 * @param entity
	 */
	public void editEntity(Shorttext shorttext);

	/**
	 * 获得一个session
	 */
	public Session getSession();

	/**
	 * 执行一条知识段落文本查询语句
	 */
	public DataResult runSqlQuery(DataQuery query);

	/**
	 * 条件删除知识段落文本实体，依据对象字段值(一般不建议使用该方法)
	 * 
	 * @param rules 删除条件
	 */
	public void deleteEntitys(List<DBRule> rules);

	/**
	 * 条件查询知识段落文本实体，依据对象字段值,当rules为空时查询全部(一般不建议使用该方法)
	 * 
	 * @param rules 查询条件
	 * @return
	 */
	public List<Shorttext> selectEntitys(List<DBRule> rules);

	public List<Shorttext> selectEntitys(List<DBRule> rules, int maxnum);

	/**
	 * 条件修改知识段落文本实体，依据对象字段值(一般不建议使用该方法)
	 * 
	 * @param values 被修改的键值对
	 * @param rules  修改条件
	 */
	public void updataEntitys(Map<String, Object> values, List<DBRule> rules);

	/**
	 * 条件合计知识段落文本:count(*)
	 * 
	 * @param rules 统计条件
	 */
	public int countEntitys(List<DBRule> rules);

	/**
	 * 刪除段落
	 * 
	 * @param id
	 */
	public void deleteByAnyId(String id);

	/**
	 * 通过知识ID获得文本id
	 * 
	 * @param sid
	 * @return
	 */
	public List<String> getIdBySid(String sid);
}