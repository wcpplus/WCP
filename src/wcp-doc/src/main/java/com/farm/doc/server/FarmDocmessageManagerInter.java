package com.farm.doc.server;

import com.farm.doc.domain.FarmDocmessage;

import java.util.List;
import java.util.Map;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;

/**
 * 留言板
 * 
 * @author MAC_wd
 * 
 */
public interface FarmDocmessageManagerInter {
	/**
	 * 发送消息
	 * 
	 * @param readUserId
	 *            收件人
	 * @param text
	 *            内容
	 * @param title
	 *            主题
	 * @param note
	 *            备注
	 * @param appId
	 *            应用id
	 * @param sendUser
	 *            发送人
	 * @return
	 */
	public FarmDocmessage sendMessage(String readUserId, String text, String title, String note, String appId,
			LoginUser sendUser);

	/**
	 * 发送消息
	 * 
	 * @param readUserId
	 *            收件人
	 * @param text
	 *            内容
	 * @param title
	 *            主题
	 * @param note
	 *            备注
	 * @param sendUser
	 *            发送人
	 * @return
	 */
	public FarmDocmessage sendMessage(String readUserId, String text, String title, String note, LoginUser sendUser);

	/**
	 * 回复消息
	 * 
	 * @param entity
	 */
	public FarmDocmessage reSendMessage(String messageId, String text, LoginUser sendUser);

	/**
	 * 删除消息
	 * 
	 * @param entity
	 */
	public void deleteMessage(String messageId, LoginUser user);

	/**
	 * 阅读消息
	 * 
	 * @param id
	 * @return
	 */
	public FarmDocmessage readMessage(String messageId);

	/**
	 * 获得没有阅读的消息数量
	 * 
	 * @param user
	 * @return
	 */
	public int getNoReadMessageNum(LoginUser user);

	/**
	 * 查询消息列表
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createMessageQuery(DataQuery query);

	/**
	 * 获得消息
	 * 
	 * @param id
	 * @return
	 */
	public FarmDocmessage getMessage(String messageId);

	/**
	 * 发表一篇业务留言
	 * 
	 * @param content
	 *            内容（被显示的）
	 * @param title
	 *            主题
	 * @param mark
	 *            留言的类型标签
	 * @param appid
	 *            业务主键（按照该键可以找到业务得留言）
	 * @param sendUser
	 *            发表人
	 * @return
	 */
	public FarmDocmessage sendAnswering(String content, String title, String mark, String appid, LoginUser sendUser);

	/**
	 * 获得文档所有评论
	 * 
	 * @param docid
	 *            文档id
	 * @param num
	 *            页数
	 * @param pagesize
	 *            每页多少条
	 * @return
	 */
	public DataResult getMessages(String docid, int num, int pagesize);

	/**
	 * 赞同
	 * @param id
	 * @return
	 * FarmDocmessage
	 */
	public FarmDocmessage approveOf(String id, LoginUser loginUser);

	/**
	 * 反对
	 * @param id
	 * @return
	 * FarmDocmessage
	 */
	public FarmDocmessage oppose(String id, LoginUser loginUser);

	/**
	 * 回复 评论
	 * @param content 内容
	 * @param appid 知识id或其他业务id
	 * @param messageId 父消息id（被回复id）
	 * @param loginUser 当前用户
	 */
	public void reply(String content,String appid,String messageId, LoginUser loginUser);

	/**
	 * 
	 * v1.0 zhanghc 2016年7月14日上午10:19:59
	 * @param docid
	 * @param docMessageId
	 * @return
	 * Object
	 */
	public List<Map<String, Object>> getReplys(String docid, String docMessageId);

}