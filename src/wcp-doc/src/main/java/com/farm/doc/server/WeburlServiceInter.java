package com.farm.doc.server;

import java.util.List;
import java.util.Map;

import com.farm.doc.domain.Weburl;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.auth.domain.LoginUser;

/* *
 *功能：推荐服务服务层接口
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
public interface WeburlServiceInter {
	/**
	 * 新增实体管理实体
	 * 
	 * @param entity
	 */
	public Weburl insertWeburlEntity(Weburl entity, LoginUser user);

	/**
	 * 修改实体管理实体
	 * 
	 * @param entity
	 */
	public Weburl editWeburlEntity(Weburl entity, LoginUser user);

	/**
	 * 删除实体管理实体
	 * 
	 * @param entity
	 */
	public void deleteWeburlEntity(String id, LoginUser user);

	/**
	 * 获得实体管理实体
	 * 
	 * @param id
	 * @return
	 */
	public Weburl getWeburlEntity(String id);

	/**
	 * 创建一个基本查询用来查询当前实体管理实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createWeburlSimpleQuery(DataQuery query);

	/**
	 * 获取列表集合
	 * @return
	 * List<Weburl>
	 */
	public List<Map<String, Object>> getList();

	/**
	 * 删除附件
	 * v1.0 zhanghc 2015年11月12日下午8:34:32
	 * @param imgid
	 * void
	 */
	public void delImg(String imgid);
}