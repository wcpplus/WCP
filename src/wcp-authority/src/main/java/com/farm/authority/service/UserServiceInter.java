package com.farm.authority.service;

import java.util.List;

import com.farm.authority.domain.Action;
import com.farm.authority.domain.Organization;
import com.farm.authority.domain.Post;
import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.domain.WebMenu;
import com.farm.core.sql.query.DataQuery;

/* *
 *功能：用户服务层接口
 *详细：
 *
 *版本：v0.1
 *作者：王东
 *日期：20141119144919
 *说明：
 */
public interface UserServiceInter {
	/**
	 * 新增实体管理实体(密码为空时，系统取默认密码)
	 * 
	 * @param entity
	 */
	public User insertUserEntity(User entity, LoginUser user, String orgId, String postIds);

	/**
	 * 新增实体管理实体(密码为空时，系统取默认密码)
	 * 
	 * @param entity
	 *            用户实例
	 * @param user
	 *            操作人
	 * @return
	 */
	public User insertUserEntity(User entity, LoginUser user);

	/**
	 * 修改实体管理实体
	 * 
	 * @param entity
	 */
	public User editUserEntity(User entity, LoginUser user, String orgId, String postIds);

	/**
	 * 删除实体管理实体
	 * 
	 * @param entity
	 */
	public void deleteUserEntity(String id, LoginUser user);

	/**
	 * 获得实体管理实体
	 * 
	 * @param id
	 * @return
	 */
	public User getUserEntity(String id);

	/**
	 * 创建一个基本查询用来查询当前实体管理实体
	 * 
	 * @param query
	 *            传入的查询条件封装
	 * @return
	 */
	public DataQuery createUserSimpleQuery(DataQuery query, LoginUser currentUser);

	/**
	 * 验证登录名是否重复
	 * 
	 * @param loginname
	 *            登录名
	 * @param userId
	 *            用户id（修改时判断是不是本用户的登录名，是自己的不算重复）
	 * @return
	 */
	public boolean validateIsRepeatLoginName(String loginname, String userId);

	/**
	 * 初始化用户密码
	 * 
	 * @param userid
	 * @param currentUser
	 */
	public void initDefaultPassWord(String userid, LoginUser currentUser);

	/**
	 * 获得用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName);

	/**
	 * 设置用户登录时间
	 * 
	 * @param userId
	 */
	public void setLoginTime(String userId);

	/**
	 * 查询岗位用户
	 * 
	 * @param query
	 * @return
	 */
	public DataQuery createUserPostQuery(DataQuery query);

	/**
	 * 获得用户所有的权限
	 * 
	 * @param userId
	 * @return
	 */
	public List<Action> getUserActions(String userId);

	/**
	 * 获得用户菜单
	 * 
	 * @param userId
	 * @return
	 */
	public List<WebMenu> getUserMenus(String userId);

	/**
	 * 获得用户岗位序列
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> getUserPostIds(String userId);

	/**
	 * 获得用户岗位序列
	 * 
	 * @param userId
	 * @return
	 */
	public List<Post> getUserPosts(String userId);

	/**
	 * 修改密码
	 * 
	 * @param loginname
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public boolean editLoginPassword(String loginname, String oldPassword, String newPassword);

	/**
	 * 获得用户的组织机构
	 * 
	 * @param userId
	 * @return
	 */
	public Organization getUserOrganization(String userId);

	/**
	 * 用户注册(请)
	 * 
	 * @param user
	 * @param orgid
	 * @return
	 */
	public User registUser(User user, String orgid);

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	public User registUser(User user);

	/**
	 * 获取组织机构
	 * 
	 * @param id
	 * @return Organization
	 */
	public Organization getOrg(String id);

	/**
	 * 获取岗位
	 * 
	 * @param id
	 * @return List<Post>
	 */
	public List<Post> getPost(String id);

	/**
	 * 获取机构下的用户
	 * 
	 * @param query
	 * @return DataQuery
	 */
	public DataQuery createOrgUserQuery(DataQuery query);

	/**
	 * 更新当前登录用户信息
	 * 
	 * @param id
	 * @param name
	 * @param photoid
	 * @param orgid
	 *            void
	 */
	public void editCurrentUser(String id, String name, String photoid, String orgid);

	/**
	 * 更新当前登录用户信息
	 * 
	 * @param id
	 * @param name
	 * @param photoid
	 *            void
	 */
	public void editCurrentUser(String id, String name, String photoid);

	/**
	 * 编辑当前登录用户密码
	 * 
	 * @param id
	 * @param password
	 * @param newPassword
	 *            void
	 */
	public void editCurrentUserPwdCommit(String id, String password, String newPassword);

	/**
	 * 校验当前登录用户密码是否有效
	 * 
	 * @param id
	 * @param password
	 * @return boolean
	 */
	public boolean validCurrentUserPwd(String id, String password);

	/**系统当前可用用户数
	 * @return
	 */
	public Integer getUsersNum();
}