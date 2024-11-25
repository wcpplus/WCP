package com.farm.doc.server;

import java.util.List;
import java.util.Map;

import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDocruninfo;
import com.farm.doc.domain.ex.DocBrief;

/**
 * 文档用量接口
 * 
 * @author 王东
 * @version 20140902
 */
public interface FarmDocRunInfoInter {
	/**
	 * 用户访问知识
	 * 
	 * @param docId
	 */
	public void visitDoc(String docId, LoginUser user, String ip);

	/**
	 * 关注一片文章
	 */
	public void enjoyDoc(String userId, String docId);

	/**
	 * 取消关注一片文章
	 */
	public void unEnjoyDoc(String userId, String docId);

	/**
	 * 用户是否关注一篇文章
	 * 
	 * @return
	 */
	public boolean isEnjoyDoc(String userId, String docId);
	/**获得文档的关注用户
	 * @param docid
	 * @return
	 */
	public List<String> getDocJoinUserIds(String docid);
	/**
	 * 获得用户关注的文章
	 * 
	 * @return
	 */
	public DataQuery getUserEnjoyDoc(String userId);

	/**
	 * 重新计算文章热度
	 * 
	 * @param userId
	 * @param docId
	 */
	public void reCountDocHotNum(String docId);

	/**
	 * 给文档一个好评（登录用户）必须配合loadRunInfo使用
	 * 
	 * @param docId
	 * @param user
	 */
	public void praiseDoc(String docId, LoginUser user, String IP);

	/**
	 * 给文档一个好评（未登录用户）必须配合loadRunInfo使用
	 * 
	 * @param docId
	 * @param IP
	 */
	public void praiseDoc(String docId, String IP);

	/**
	 * 给文档一个差评（登录用户）必须配合loadRunInfo使用
	 * 
	 * @param docId
	 * @param user
	 */
	public void criticalDoc(String docId, LoginUser user, String IP);

	/**
	 * 给文档一个差评（未登录用户）必须配合loadRunInfo使用
	 * 
	 * @param docId
	 * @param IP
	 */
	public void criticalDoc(String docId, String IP);

	/**
	 * 更新和加载计算用量信息
	 * 
	 * @param docId
	 * @return
	 */
	public FarmDocruninfo loadRunInfo(String docId);

	/**
	 * 获得最热知识
	 * 
	 * @param num
	 *            返回多少条
	 * @return
	 */
	public List<DocBrief> getPubHotDoc(int num);

	/**
	 * 展示最新知识
	 */
	public List<DocBrief> getNewKnowList(int pagesize);

	/**
	 * 获得分类知识
	 * 
	 * @param typeid
	 *            分类id
	 * @param userid
	 *            用户id
	 * @param num
	 *            查询数量
	 * @return
	 */
	public List<DocBrief> getTypeDocs(String typeid, String userid, int num);

	/**
	 * 获得置顶知识
	 * 
	 * @param num
	 * @return
	 */
	public List<DocBrief> getPubTopDoc(int num);

	/**
	 * 获得好评用户
	 * 
	 * @param num
	 * @return ID,NAME,SUMYES
	 */
	public DataResult getStatGoodUsers(int num);

	/**
	 * 获得好评小组
	 * 
	 * @param num
	 * @return
	 */
	public DataResult getStatGoodGroups(int num);

	/**
	 * 做多知识用户
	 * 
	 * @param num
	 * @return
	 */
	public DataResult getStatMostUsers(int num);

	/**
	 * 好评文档
	 * 
	 * @param num
	 * @return
	 */
	public DataResult getStatGoodDocs(int num);

	/**
	 * 差评文档
	 * 
	 * @param num
	 * @return
	 */
	public DataResult getStatBadDocs(int num);

	/**
	 * 整体用量(获得每天的知识总数、好评数、差评数)
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getStatNumForDay() throws Exception;
	
	
	/**库的统计总数，知识总数、总人数、好评数、差评数、分类数量
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> getStatNum() throws Exception;
	

	/**
	 * 用户知识统计
	 * 
	 * @param user
	 * @return
	 */
	public DataResult getStatUser(User user);

	/**
	 * 获得用户所有文档
	 * 
	 * @param userid
	 * @param domtype
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult userDocs(String userid, String domtype, int pagesize, int pagenum);

	/**
	 * 获得用户所有公开文档
	 * 
	 * @param userid
	 * @param domtype
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult userPubDocs(String userid, String domtype, int pagesize, int pagenum);

	/**
	 * 获得我历史审核
	 * 
	 * @param userid
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult getMyAuditedByUser(String userid, int pagesize, int pagenum);

	/**
	 * 获得我的未审核文档
	 * 
	 * @param userid
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult getMyAuditingByUser(String userid, int pagesize, int pagenum);

	/**
	 * 获得需要我审核的文档列表
	 * 
	 * @param user
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult getAuditDocByUser(String user, int pagesize, int pagenum);
	
	/**
	 * 创建一个用户发布排名查询
	 * 
	 * @param query
	 * @return DataQuery
	 */
	public DataQuery createReleaseRankingSimpleQuery(DataQuery query);
}
