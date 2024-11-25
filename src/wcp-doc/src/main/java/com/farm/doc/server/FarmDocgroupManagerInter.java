package com.farm.doc.server;

import java.util.List;
import java.util.Map;

import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDocgroup;
import com.farm.doc.domain.FarmDocgroupUser;
import com.farm.doc.domain.ex.GroupBrief;
import com.farm.doc.domain.ex.GroupEntire;
import com.farm.doc.exception.NoGroupAuthForLicenceException;

/**
 * 工作小组
 * 
 * @author MAC_wd
 * 
 */
public interface FarmDocgroupManagerInter {

	/**
	 * 查询条件是否执行
	 * 
	 * @author wangdong
	 *
	 */
	public enum SearchType {
		yes, no, none
	}

	/**
	 * 修改工作小组实体(控制台修改)
	 * 
	 * @param entity
	 */
	public FarmDocgroup editFarmDocgroupEntity(FarmDocgroup entity, LoginUser user);

	/**
	 * 删除工作小组实体
	 * 
	 * @param entity
	 */
	public void deleteFarmDocgroupEntity(String groupId, LoginUser user);

	/**
	 * 获得工作小组实体
	 * 
	 * @param id
	 * @return
	 */
	public FarmDocgroup getFarmDocgroupEntity(String id);

	/**
	 * 获得工作小组实体
	 * 
	 * @param groupid
	 * @return
	 */
	public GroupEntire getFarmDocgroup(String groupid);

	/**
	 * 修改工作小组成员实体(控制台修改)
	 * 
	 * @param entity
	 */
	public FarmDocgroupUser editFarmDocgroupUserEntity(FarmDocgroupUser entity, LoginUser user);

	/**
	 * 删除工作小组成员实体
	 * 
	 * @param entity
	 */
	public void deleteFarmDocgroupUserEntity(String entity, LoginUser user);

	/**
	 * 获得工作小组成员实体
	 * 
	 * @param id
	 * @return
	 */
	public FarmDocgroupUser getFarmDocgroupUserEntity(String id);

	/**
	 * 创建一个基本查询用来查询当前工作小组成员实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createFarmDocgroupUserSimpleQuery(DataQuery query);

	/**
	 * 创建一个工作小组
	 * 
	 * @param groupname
	 *            小组名称
	 * @param grouptag
	 *            小组tag
	 * @param groupimg
	 *            小组头像
	 * @param joincheck
	 *            加入小组是否需要验证
	 * @param groupnote
	 *            小组备注
	 * @param currentUser
	 *            当前操作用户
	 */
	public FarmDocgroup creatDocGroup(String groupname, String grouptag, String groupimg, boolean joincheck,
			String groupnote, LoginUser currentUser) throws NoGroupAuthForLicenceException;

	/**
	 * 修改一个工作小组
	 * 
	 * @param id
	 * @param groupname
	 * @param grouptag
	 * @param groupimg
	 * @param b
	 * @param groupnote
	 * @param currentUser
	 * @return
	 */
	public FarmDocgroup editDocGroup(String id, String groupname, String grouptag, String groupimg, boolean joincheck,
			String groupnote, LoginUser currentUser);

	/**
	 * 是否需要验证才能够加入小组
	 * 
	 * @param groupid
	 * @return
	 */
	public boolean isJoinCheck(String groupid);

	/**
	 * 用户申请加入小组(如果不需要验证则直接加入)
	 * 
	 * @param groupId
	 *            小组ID
	 * @param joinUserId
	 *            成员ID
	 * @param note
	 *            申请备注
	 * @param currentUser
	 *            当前操作人员
	 */
	public FarmDocgroupUser applyGroup(String groupId, String joinUserId, String note, LoginUser currentUser);

	/**
	 * 小组邀请用户加入
	 * 
	 * @param groupId
	 *            小组ID
	 * @param joinUserId
	 *            成员ID
	 * @param isLead
	 *            是否管理员
	 * @param isEdit
	 *            是否享有编辑权限
	 * @param currentUser
	 *            当前操作人员
	 */
	public void inviteGroup(String groupId, String joinUserId, boolean isLead, boolean isEdit, LoginUser currentUser);

	/**
	 * 判断用户是否加入到某个小组中
	 * 
	 * @param groupId
	 *            小组id
	 * @param userId
	 *            用户id
	 */
	public boolean isJoinGroupByUser(String groupId, String userId);

	/**
	 * 创建一个外连接小组用户的查询用来查询当前工作小组实体farm_docgroup a left join FARM_DOCGROUP_USER b
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createFarmDocgroupQueryJoinUser(DataQuery query);

	/**
	 * 创建一个基本查询用来查询当前工作小组实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createFarmDocgroupQuery(DataQuery query);

	/**
	 * 查询用户没有加入的小组
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @param userid
	 *            用户id
	 * @return
	 */
	public DataQuery createFarmDocgroupQueryNuContainUser(DataQuery query, String userid);

	/**
	 * 获得用户的groupUser
	 * 
	 * @param groupId
	 * @param currentUser
	 * @return
	 */
	public FarmDocgroupUser getFarmDocgroupUser(String groupId, String userId);

	/**
	 * 离开小组
	 * 
	 * @param groupID
	 * @param userId
	 */
	public void leaveGroup(String groupID, String userId);

	/**
	 * 获得小组所有的管理员信息
	 * 
	 * @param groupId
	 *            小组ID
	 * @return
	 */
	public List<User> getAllAdministratorByGroup(String groupId);

	/**
	 * 获得小组所有状态下的成员信息(含state不为1的的)
	 * 
	 * @param groupId
	 *            小组ID
	 * @return
	 */
	public List<FarmDocgroupUser> getAllUserByGroup(String groupId);

	/**
	 * 获得小组所有可用成员信息（只含有state为1的）
	 * 
	 * @param groupId
	 *            小组ID
	 * @return
	 */
	public List<User> getAllUserNoApplyByGroup(String groupId);

	/**
	 * 同意用户加入申请
	 * 
	 * @param groupUserId
	 */
	public void agreeJoinApply(String groupUserId, LoginUser currentUser);

	/**
	 * 拒绝用户加入申请
	 * 
	 * @param groupUserId
	 */
	public void refuseJoinApply(String groupUserId, LoginUser currentUser);

	/**
	 * 删除用户小组管理员权限
	 * 
	 * @param groupUserId
	 */
	public void wipeAdminFromGroup(String groupUserId, LoginUser currentUser);

	/**
	 * 将用户退出小组
	 * 
	 * @param groupUserId
	 */
	public void leaveGroup(String groupUserId, LoginUser currentUser);

	/**
	 * 去除小组编辑权限
	 * 
	 * @param groupUserId
	 */
	public void wipeEditorForGroup(String groupUserId, LoginUser currentUser);

	/**
	 * 设置小组编辑权限
	 * 
	 * @param groupUserId
	 */
	public void setEditorForGroup(String groupUserId, LoginUser currentUser);

	/**
	 * 设置为小组管理员
	 * 
	 * @param groupUserId
	 */
	public void setAdminForGroup(String groupUserId, LoginUser currentUser);

	/**
	 * 用户是否为小组管理员
	 * 
	 * @param userid
	 *            用户ID
	 * @param groupId
	 *            小组ID
	 */
	public boolean isAdminForGroup(String userid, String groupId);

	/**
	 * 在我的首页显示小组
	 * 
	 * @param id
	 */
	public void setGroupHomeShow(String groupId, String userId);

	/**
	 * 在我的首页隐藏小组
	 * 
	 * @param id
	 */
	public void setGroupHomeHide(String groupId, String userId);

	/**
	 * 在我的小组中为小组上移一个排序
	 * 
	 * @param id
	 */
	public void setGroupSortUp(String groupId, String userId);

	/**
	 * 获得用户所有的小组
	 * 
	 * @param userId
	 */
	public List<FarmDocgroupUser> getAllGroupUserByUser(String userId);

	/**
	 * 获得所有具有编辑权限的小组
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getEditorGroupByUser(String userId);

	/**
	 * 用户是否具有小组的编辑权限
	 * 
	 * @param docgroupid
	 * @param id
	 */
	public boolean isGroupEditor(String docgroupid, String userId);

	/**
	 * 获得小组最新文档查询对象
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery getGroupNewDocQuery(DataQuery query, String groupId, LoginUser user);

	/**
	 * 获得小组最优质文档查询对象
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery getGroupGoodDocQuery(DataQuery query, String groupId, LoginUser currentUser);

	/**
	 * 获得小组最优质文档查询对象
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery getGroupHotDocQuery(DataQuery query, String groupId, LoginUser currentUser);

	/**
	 * 获得小组文档的数量
	 * 
	 * @param groupId
	 *            小组ID
	 * @return
	 */
	public int getGroupDocNum(String groupId);

	/**
	 * 修改部分用户的小组权限（状态，是否小组管理员，是否有修改权限，是否首页显示）
	 * 
	 * @param entity
	 * @param currentUser
	 * @return
	 */
	public FarmDocgroupUser editMinFarmDocgroupUserEntity(FarmDocgroupUser entity, LoginUser currentUser);

	/**
	 * 创建一个我的小组文档的查询对象
	 * 
	 * @param query
	 * 
	 * @param query
	 * @param userid
	 * @return (ID,EVALUATE,DOMTYPE,ETIME,SHOWHOME,GROUPNAME,GROUPID,TITLE,
	 *         DOCDESCRIBE
	 *         ,AUTHOR,PUBTIME,TAGKEY,IMGID,VISITNUM,PRAISEYES,PRAISENO,HOTNUM,
	 *         TYPENAME,GROUPIMG)
	 */
	public DataQuery createUserGroupDocQuery(DataQuery query, String userid);

	/**
	 * 修改小组加入时是否验证
	 * 
	 * @param isJoinCheck
	 *            加入时是否验证
	 * @param currentUser
	 *            当前用户
	 * @return
	 */
	public FarmDocgroup editDocgroupJoinCheck(boolean isJoinCheck, String groupId, LoginUser currentUser);

	/**
	 * 获得小组最差评文档查询对象
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery getGroupBadDocQuery(DataQuery query, String groupId, LoginUser currentUser);

	/**
	 * 获得小组最新知识
	 * 
	 * @param userid
	 *            用户id
	 * @param gourpid
	 *            小组id
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult getNewGroupDoc(LoginUser user, String gourpid, int pagesize, Integer pagenum);

	/**
	 * 获得最热门小组
	 * 
	 * @param num
	 *            获得数量
	 * @return
	 */
	public List<GroupBrief> getHotDocGroups(int num);

	public List<GroupBrief> getHotDocGroups(int num, LoginUser currentUser);

	/**
	 * 获得小组用户
	 * 
	 * @param groupid
	 *            小组id
	 * @param isApply
	 *            是否申请
	 * @param isAdmin
	 *            是否管理员
	 * @param isEditor
	 *            是否编辑权限
	 * @return
	 */
	public DataResult getGroupUser(String groupid, SearchType isApply, SearchType isAdmin, SearchType isEditor,
			int page, int pagesize);

	/**
	 * 用户是否正在小组审核中
	 * 
	 * @param groupId
	 * @param userid
	 * @return
	 */
	public boolean isAuditing(String groupId, String userid);

	/**
	 * 获得用户小组
	 * 
	 * @param userid
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult getGroupsByUser(String userid, int pagesize, Integer pagenum);

	/**
	 * 获得小组
	 * 
	 * @param pagesize
	 * @param pagenum
	 * @return
	 */
	public DataResult getGroups(int pagesize, Integer pagenum);

	/**
	 * 获得所有小组
	 * 
	 * @return
	 */
	public List<FarmDocgroup> getAllGroup();

}