package com.farm.doc.server;

import com.farm.doc.domain.Usermessage;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;

import java.util.List;

import com.farm.core.auth.domain.LoginUser;

/* *
 *功能：用户消息服务层接口
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
public interface UsermessageServiceInter {
	/**
	 * 新增实体管理实体
	 * 
	 * @param entity
	 */
	public Usermessage insertUsermessageEntity(Usermessage entity,
			LoginUser user);

	/**
	 * 修改实体管理实体
	 * 
	 * @param entity
	 */
	public Usermessage editUsermessageEntity(Usermessage entity, LoginUser user);

	/**
	 * 删除实体管理实体
	 * 
	 * @param entity
	 */
	public void deleteUsermessageEntity(String id, LoginUser user);

	/**
	 * 获得实体管理实体
	 * 
	 * @param id
	 * @return
	 */
	public Usermessage getUsermessageEntity(String id);

	/**
	 * 创建一个基本查询用来查询当前实体管理实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createUsermessageSimpleQuery(DataQuery query);

	/**
	 * 设置为阅读
	 * @param id
	 * void
	 */
	public void setRead(String id);

	/**
	 * 发送消息
	 * 
	 * @param readUserId 收件人
	 * @param text 内容
	 * @param title 主题
	 * @param note 备注
	 * @param sendUser 发送人
	 * @return
	 */
	public void sendMessage(String readUserId, String text, String title,
			String note, LoginUser sendUser);
	/**
	 * 发送消息
	 * 
	 * @param readUserId 收件人
	 * @param text 内容
	 * @param title 主题
	 * @param note 备注
	 * @param sendUser 发送人
	 * @return
	 */
	public void sendMessage(String readUserId, String text, String title,LoginUser sendUser);
	/**
	 * 发送消息
	 * 
	 * @param readUserId 收件人
	 * @param text 内容
	 * @param title 主题
	 * @param note 备注
	 * @param sendUser 发送人
	 * @return
	 */
	public void sendMessage(String readUserId, String text, String title);

	/**发送消息给一些人
	 * @param userids
	 * @param text
	 * @param title
	 */
	public void sendMessage(List<String> userids, String text, String title);

	/**
	 * 获取我的消息
	 * @param userId 用户ID
	 * @param pageSize 每页多少条
	 * @param currPage 当前第几页
	 * @return
	 * DataResult
	 */
	public DataResult getMyMessageByUser(String userId, int pageSize, Integer currPage);
}