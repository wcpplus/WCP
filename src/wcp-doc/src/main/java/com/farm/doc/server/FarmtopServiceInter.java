package com.farm.doc.server;

import com.farm.doc.domain.Farmtop;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.auth.domain.LoginUser;

/* *
 *功能：置顶文档服务层接口
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
public interface FarmtopServiceInter {
	/**
	 * 新增实体管理实体
	 * 
	 * @param entity
	 */
	public Farmtop insertFarmtopEntity(Farmtop entity, LoginUser user);

	/**
	 * 修改实体管理实体
	 * 
	 * @param entity
	 */
	public Farmtop editFarmtopEntity(Farmtop entity, LoginUser user);

	/**
	 * 删除实体管理实体
	 * 
	 * @param entity
	 */
	public void deleteFarmtopEntity(String id, LoginUser user);

	/**
	 * 获得实体管理实体
	 * 
	 * @param id
	 * @return
	 */
	public Farmtop getFarmtopEntity(String id);

	/**
	 * 创建一个基本查询用来查询当前实体管理实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createFarmtopSimpleQuery(DataQuery query);

	/**
	 * 置顶文档选择文档列表数据
	 * v1.0 zhanghc 2015年9月5日下午2:03:20
	 * @param query
	 * @return DataQuery
	 */
	public DataQuery docTopChooseDocList(DataQuery query);
}