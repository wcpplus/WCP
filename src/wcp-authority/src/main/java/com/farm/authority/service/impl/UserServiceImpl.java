package com.farm.authority.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.farm.authority.dao.OrganizationDaoInter;
import com.farm.authority.dao.PostDaoInter;
import com.farm.authority.dao.UserDaoInter;
import com.farm.authority.dao.UserorgDaoInter;
import com.farm.authority.dao.UserpostDaoInter;
import com.farm.authority.domain.Action;
import com.farm.authority.domain.AuthMenuImpl;
import com.farm.authority.domain.Organization;
import com.farm.authority.domain.Post;
import com.farm.authority.domain.User;
import com.farm.authority.domain.Userorg;
import com.farm.authority.domain.Userpost;
import com.farm.authority.service.OrganizationServiceInter;
import com.farm.authority.service.UserServiceInter;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.domain.WebMenu;
import com.farm.core.auth.util.AuthenticateInter;
import com.farm.core.auth.util.AuthenticateProvider;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.time.TimeTool;
import com.farm.parameter.FarmParameterService;
import com.farm.util.validate.ValidUtils;

/* *
 *功能：用户服务层实现类
 *详细：
 *
 *版本：v0.1
 *作者：王东
 *日期：20141119144919
 *说明：
 */
@Service
public class UserServiceImpl implements UserServiceInter {
	@Resource
	private UserDaoInter userDaoImpl;
	@Resource
	private UserpostDaoInter userpostDaoImpl;
	@Resource
	private PostDaoInter postDaoImpl;
	@Resource
	private OrganizationDaoInter organizationDao;
	@Resource
	private OrganizationServiceInter organizationServiceImpl;
	@Resource
	private UserorgDaoInter userorgDaoImpl;
	private AuthenticateInter authUtil = AuthenticateProvider.getInstance();

	// private static final Logger log =
	// Logger.getLogger(UserServiceImpl.class);

	@Override
	@Transactional
	public User insertUserEntity(User entity, LoginUser user, String orgId, String postIds) {
		entity.setCuser(user.getId());
		entity.setCtime(TimeTool.getTimeDate14());
		entity.setMuser(user.getId());
		entity.setUtime(TimeTool.getTimeDate14());
		if (validateIsRepeatLoginName(entity.getLoginname(), null)) {
			throw new RuntimeException("登录名已经存在!");
		}
		entity.setPassword(authUtil.encodeLoginPasswordOnMd5(
				FarmParameterService.getInstance().getParameter("config.default.password"), entity.getLoginname()));
		userDaoImpl.insertEntity(entity);

		// 保存用户机构关系
		userorgDaoImpl.insertEntity(new Userorg("", orgId, entity.getId()));

		// 保存用户岗位关系
		String[] postIdArr = postIds.split(",");
		for (String postId : postIdArr) {
			userpostDaoImpl.insertEntity(new Userpost("", postId, entity.getId()));
		}
		return entity;
	}

	@Override
	@Transactional
	public User insertUserEntity(User entity, LoginUser user) {
		entity.setCuser(user.getId());
		entity.setCtime(TimeTool.getTimeDate14());
		entity.setMuser(user.getId());
		entity.setUtime(TimeTool.getTimeDate14());
		if (validateIsRepeatLoginName(entity.getLoginname(), null)) {
			throw new RuntimeException("登录名已经存在!");
		}
		if (entity.getPassword() == null || entity.getPassword().isEmpty()) {
			entity.setPassword(FarmParameterService.getInstance().getParameter("config.default.password"));
		}
		entity.setPassword(authUtil.encodeLoginPasswordOnMd5(entity.getPassword(), entity.getLoginname()));
		return userDaoImpl.insertEntity(entity);
	}

	@Override
	@Transactional
	public User editUserEntity(User entity, LoginUser user, String orgId, String postIds) {
		User entity2 = userDaoImpl.getEntity(entity.getId());
		if (validateIsRepeatLoginName(entity.getLoginname(), entity2.getId())) {
			throw new RuntimeException("登录名已经存在!");
		}
		if (entity2.getState().equals("2")) {
			throw new RuntimeException("该用户已被删除，无法修改");
		}
		entity2.setMuser(user.getId());
		entity2.setUtime(TimeTool.getTimeDate14());
		if (!ValidUtils.isEmptyString(entity.getLoginname())) {
			entity2.setLoginname(entity.getLoginname());
		}
		if (!ValidUtils.isEmptyString(entity.getState())) {
			entity2.setState(entity.getState());
		}
		if (!ValidUtils.isEmptyString(entity.getType())) {
			entity2.setType(entity.getType());
		}
		if (!ValidUtils.isEmptyString(entity.getImgid())) {
			entity2.setImgid(entity.getImgid());
		}
		if (!ValidUtils.isEmptyString(entity.getComments())) {
			entity2.setComments(entity.getComments());
		}
		if (!ValidUtils.isEmptyString(entity.getName())) {
			entity2.setName(entity.getName());
		}

		userDaoImpl.editEntity(entity2);

		// 更新用户机构关系
		userorgDaoImpl.deleteEntitys(new DBRule("USERID", entity.getId(), "=").getDBRules());
		userorgDaoImpl.insertEntity(new Userorg("", orgId, entity.getId()));

		// 更新用户岗位关系
		userpostDaoImpl.deleteEntitys(new DBRule("USERID", entity.getId(), "=").getDBRules());
		String[] postIdArr = postIds.split(",");
		for (String postId : postIdArr) {
			userpostDaoImpl.insertEntity(new Userpost("", postId, entity.getId()));
		}
		return entity2;
	}

	@Override
	@Transactional
	public boolean validateIsRepeatLoginName(String loginname, String userId) {
		List<User> list = null;
		if (userId == null || userId.trim().equals("")) {
			list = userDaoImpl.findUserByLoginName(loginname.trim());
		} else {
			list = userDaoImpl.findUserByLoginName(loginname.trim(), userId);
		}
		return list.size() > 0;
	}

	@Override
	@Transactional
	public void deleteUserEntity(String id, LoginUser user) {
		String[] idArr = id.split(",");
		for (int i = 0; i < idArr.length; i++) {
			User entity2 = userDaoImpl.getEntity(idArr[i]);
			entity2.setMuser(user.getId());
			entity2.setUtime(TimeTool.getTimeDate14());
			entity2.setState("2");
			entity2.setLoginname(entity2.getId());
			userDaoImpl.editEntity(entity2);
		}
	}

	@Override
	@Transactional
	public User getUserEntity(String id) {
		if (id == null) {
			return null;
		}
		return userDaoImpl.getEntity(id);
	}

	@Override
	@Transactional
	public DataQuery createUserSimpleQuery(DataQuery query, LoginUser currentUser) {
		DataQuery dbQuery = DataQuery.init(query,
				"ALONE_AUTH_USER A " + "LEFT JOIN ALONE_AUTH_USERORG B ON A.ID = B.USERID "
						+ "LEFT JOIN ALONE_AUTH_ORGANIZATION A3 ON B.ORGANIZATIONID = A3.ID",
				"A.ID AS ID,A.LOGINTIME AS LOGINTIME,A.LOGINNAME AS LOGINNAME,A.STATE AS STATE,"
						+ "A.TYPE AS TYPE,A.COMMENTS AS COMMENTS,A.NAME AS NAME,A3.NAME AS ORGNAME");

		User entity = userDaoImpl.getEntity(currentUser.getId());

		if (entity.getType() != null && (!entity.getType().equals("3"))) {
			dbQuery.addRule(new DBRule("A.TYPE", 3, "!="));
		}
		return dbQuery;
	}

	// ----------------------------------------------------------------------------------

	@Override
	@Transactional
	public void initDefaultPassWord(String userid, LoginUser currentUser) {
		User entity2 = userDaoImpl.getEntity(userid);
		entity2.setMuser(currentUser.getId());
		entity2.setUtime(TimeTool.getTimeDate14());
		entity2.setPassword(authUtil.encodeLoginPasswordOnMd5(
				FarmParameterService.getInstance().getParameter("config.default.password"), entity2.getLoginname()));
		userDaoImpl.editEntity(entity2);
	}

	@Override
	@Transactional
	public User getUserByLoginName(String loginName) {
		List<User> users = userDaoImpl.findUserByLoginName(loginName);
		if (users.size() <= 0) {
			return null;
		}
		if (users.size() > 1) {
			throw new RuntimeException("该登录名返回了多个用户！");
		}
		return users.get(0);
	}

	@Override
	@Transactional
	public void setLoginTime(String userId) {
		User entity2 = userDaoImpl.getEntity(userId);
		entity2.setLogintime(TimeTool.getTimeDate14());
		userDaoImpl.editEntity(entity2);
	}

	@Override
	@Transactional
	public DataQuery createUserPostQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query,
				"ALONE_AUTH_ORGANIZATION ORG "
						+ "INNER JOIN ALONE_AUTH_USERORG USERORG ON ORG.ID = USERORG.ORGANIZATIONID "
						+ "INNER JOIN ALONE_AUTH_USER USER ON USERORG.USERID = USER.ID",
				"USER.ID AS USERID,USER.LOGINTIME AS LOGINTIME,USER.NAME AS USERNAME,USER.STATE AS USERSTATE");
		dbQuery.addRule(new DBRule("USER.STATE", "1", "="));
		return dbQuery;
	}

	@Override
	@Transactional
	public List<Action> getUserActions(String userId) {
		DataQuery dbQuery = DataQuery.getInstance(1, "d.id,d.AUTHKEY,d.NAME,d.COMMENTS,d.STATE,d.CHECKIS,d.LOGINIS",
				"alone_auth_userpost a LEFT JOIN alone_auth_postaction b ON a.POSTID =b.POSTID LEFT JOIN alone_auth_actiontree c ON b.MENUID=c.ID LEFT JOIN alone_auth_action d ON d.ID=c.ACTIONID");
		dbQuery.addRule(new DBRule("d.STATE", "1", "="));
		dbQuery.addRule(new DBRule("a.USERID", userId, "="));
		dbQuery.setDistinct(true);
		dbQuery.setPagesize(5000);
		List<Action> list = new ArrayList<Action>();
		try {
			for (Map<String, Object> node : dbQuery.search().getResultList()) {
				Action action = new Action();
				action.setAuthkey(node.get("D_AUTHKEY") != null ? node.get("D_AUTHKEY").toString() : null);
				action.setId(node.get("D_ID") != null ? node.get("D_ID").toString() : null);
				action.setName(node.get("D_NAME") != null ? node.get("D_NAME").toString() : null);
				action.setComments(node.get("D_COMMENTS") != null ? node.get("D_COMMENTS").toString() : null);
				action.setState(node.get("D_STATE") != null ? node.get("D_STATE").toString() : null);
				action.setCheckis(node.get("D_CHECKIS") != null ? node.get("D_CHECKIS").toString() : null);
				action.setLoginis(node.get("D_LOGINIS") != null ? node.get("D_LOGINIS").toString() : null);
				list.add(action);
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		return list;
	}

	@Override
	@Transactional
	public List<WebMenu> getUserMenus(String userId) {
		DataQuery query = DataQuery.getInstance(1, "SORT,ID,PARENTID,NAME,TYPE,STATE,ICON,IMGID,PARAMS,AUTHKEY",
				"(SELECT c.SORT,c.ID,c.PARENTID,c.NAME,c.TYPE,c.STATE,c.ICON,c.IMGID,c.PARAMS,d.AUTHKEY FROM  alone_auth_userpost a LEFT JOIN alone_auth_postaction b ON a.POSTID =b.POSTID LEFT JOIN alone_auth_actiontree c ON b.MENUID=c.ID LEFT JOIN alone_auth_action d ON d.ID=c.ACTIONID WHERE (d.STATE = '1'||d.STATE IS NULL) and a.userid='"
						+ userId + "' and c.type!='3' order by LENGTH(c.TREECODE),c.SORT asc) e");
		List<WebMenu> menus = new ArrayList<WebMenu>();
		query.setPagesize(1000);
		query.setNoCount();
		query.setDistinct(true);
		try {
			for (Map<String, Object> map : query.search().getResultList()) {
				AuthMenuImpl node = new AuthMenuImpl();
				node.setIcon(map.get("ICON") != null ? map.get("ICON").toString() : null);
				node.setId(map.get("ID") != null ? map.get("ID").toString() : null);
				node.setName(map.get("NAME") != null ? map.get("NAME").toString() : null);
				node.setParams(map.get("PARAMS") != null ? map.get("PARAMS").toString() : null);
				node.setParentid(map.get("PARENTID") != null ? map.get("PARENTID").toString() : null);
				node.setUrl(map.get("AUTHKEY") != null ? map.get("AUTHKEY").toString() : null);
				menus.add(node);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return menus;
	}

	@Override
	@Transactional
	public List<Post> getUserPosts(String userId) {
		List<Userpost> userposts = userpostDaoImpl.selectEntitys(new DBRule("USERID", userId, "=").getDBRules());
		List<Post> list = new ArrayList<Post>();
		for (Userpost userPost : userposts) {
			list.add(postDaoImpl.getEntity(userPost.getPostid()));
		}
		return list;
	}

	@Override
	@Transactional
	public boolean editLoginPassword(String loginname, String oldPassword, String newPassword) {
		User user = getUserByLoginName(loginname);
		if (user == null) {
			throw new RuntimeException("不存在该用户!");
		}
		if (authUtil.encodeLoginPasswordOnMd5(oldPassword, loginname).equals(user.getPassword())) {
			// 验证成功,修改密码
			user.setPassword(authUtil.encodeLoginPasswordOnMd5(newPassword, loginname));
			userDaoImpl.editEntity(user);
			return true;
		} else {
			throw new RuntimeException("原密码错误!");
		}
	}

	@Override
	@Transactional
	public List<String> getUserPostIds(String userId) {
		List<Userpost> userposts = userpostDaoImpl.selectEntitys(new DBRule("USERID", userId, "=").getDBRules());
		List<String> list = new ArrayList<String>();
		for (Userpost userPost : userposts) {
			list.add(userPost.getPostid());
		}
		return list;
	}

	@Override
	@Transactional
	public Organization getUserOrganization(String userId) {
		return getOrg(userId);
	}

	@Override
	@Transactional
	public User registUser(final User user) {
		LoginUser noneuser = new LoginUser() {
			@Override
			public String getName() {
				return user.getName();
			}

			@Override
			public String getLoginname() {
				return user.getLoginname();
			}

			@Override
			public String getId() {
				return user.getLoginname();
			}
		};
		String defaultOrg = FarmParameterService.getInstance().getParameter("config.user.org.default.id");
		String defaultpost = FarmParameterService.getInstance().getParameter("config.user.post.default.id");
		return insertUserEntity(user, noneuser, defaultOrg, defaultpost);
	}

	@Override
	@Transactional
	public User registUser(final User user, String orgid) {
		// 保存新增用户
		LoginUser noneuser = new LoginUser() {
			@Override
			public String getName() {
				return user.getName();
			}

			@Override
			public String getLoginname() {
				return user.getLoginname();
			}

			@Override
			public String getId() {
				return user.getLoginname();
			}
		};

		String defaultOrg = "";
		String showOrg = FarmParameterService.getInstance().getParameter("config.regist.showOrg");
		if (showOrg != null && !showOrg.isEmpty() && showOrg.equals("true") && orgid != null && !orgid.isEmpty()) {
			defaultOrg = orgid;
		} else {
			defaultOrg = FarmParameterService.getInstance().getParameter("config.user.org.default.id");
		}

		String defaultpost = FarmParameterService.getInstance().getParameter("config.user.post.default.id");
		User user2 = insertUserEntity(user, noneuser, defaultOrg, defaultpost);
		return user2;
	}

	@Override
	public Organization getOrg(String id) {
		try {
			DataQuery query = DataQuery.getInstance(1,
					"ORG.ID as ID,ORG.TYPE as TYPE,ORG.SORT as SORT,ORG.PARENTID as PARENTID,ORG.MUSER as MUSER,"
							+ "ORG.CUSER as CUSER,ORG.STATE as STATE,ORG.UTIME as UTIME,ORG.CTIME as CTIME,"
							+ "ORG.COMMENTS as COMMENTS,ORG.NAME as NAME,ORG.TREECODE as TREECODE",
					"ALONE_AUTH_USERORG USERORG "
							+ "INNER JOIN ALONE_AUTH_ORGANIZATION ORG ON USERORG.ORGANIZATIONID = ORG.ID");
			query.addRule(new DBRule("USERORG.USERID", id, "="));
			query.setPagesize(1000);
			query.setNoCount();
			DataResult result = query.search();
			List<Object> orgList = result.getObjectList(Organization.class);
			if (orgList.size() > 0) {
				return (Organization) orgList.get(0);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Post> getPost(String id) {
		// ID,EXTENDIS,NAME,ORGANIZATIONID,PSTATE,EUSER,EUSERNAME,CUSER,CUSERNAME,ETIME,CTIME
		try {
			DataQuery query = DataQuery.getInstance(1,
					"POST.ID AS ID,POST.EXTENDIS AS EXTENDIS,POST.NAME AS NAME,POST.ORGANIZATIONID AS ORGANIZATIONID,POST.PSTATE AS PSTATE,POST.EUSER AS EUSER,POST.EUSERNAME AS EUSERNAME,POST.CUSER AS CUSER,POST.CUSERNAME AS CUSERNAME,POST.ETIME AS ETIME,POST.CTIME AS CTIME",
					"ALONE_AUTH_USERPOST USERPOST " + "LEFT JOIN ALONE_AUTH_POST POST ON USERPOST.POSTID = POST.ID");
			query.addRule(new DBRule("USERPOST.USERID", id, "="));
			DataResult result = query.search();
			result.getObjectList(Post.class);
			return result.getObjectList(Post.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataQuery createOrgUserQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query,
				"ALONE_AUTH_ORGANIZATION ORG "
						+ "INNER JOIN ALONE_AUTH_USERORG USERORG ON ORG.ID = USERORG.ORGANIZATIONID "
						+ "INNER JOIN ALONE_AUTH_USER USER ON USERORG.USERID = USER.ID",
				"USER.ID AS USERID,USER.LOGINTIME AS LOGINTIME,USER.NAME AS USERNAME,USER.STATE AS USERSTATE");
		dbQuery.addRule(new DBRule("USER.STATE", "1", "="));
		return dbQuery;
	}

	@Override
	@Transactional
	public void editCurrentUser(String id, String name, String photoid, String orgid) {
		// 更新用户
		User user = userDaoImpl.getEntity(id);
		user.setName(name);
		user.setImgid(photoid);

		// 更新机构
		String showOrg = FarmParameterService.getInstance().getParameter("config.regist.showOrg");
		if (showOrg != null && !showOrg.isEmpty() && showOrg.equals("true") && orgid != null && !orgid.isEmpty()) {
			Userorg userorg = userorgDaoImpl.getEntityByUserId(user.getId());
			userorg.setOrganizationid(orgid);

			// 删除岗位（岗位暂时无用，wd说的）
			userpostDaoImpl.deleteEntitys(new DBRule("userid", id, "=").getDBRules());
		}
	}

	@Override
	@Transactional
	public void editCurrentUserPwdCommit(String id, String password, String newPassword) {
		User user = userDaoImpl.getEntity(id);
		String oldPwd = authUtil.encodeLoginPasswordOnMd5(password, user.getLoginname());
		if (!user.getPassword().equals(oldPwd)) {
			throw new RuntimeException("旧密码错误!");
		}

		String newPwd = authUtil.encodeLoginPasswordOnMd5(newPassword, user.getLoginname());
		user.setPassword(newPwd);
		userDaoImpl.editEntity(user);
	}

	@Override
	@Transactional
	public boolean validCurrentUserPwd(String id, String password) {
		User user = userDaoImpl.getEntity(id);
		String pwdForMd5 = authUtil.encodeLoginPasswordOnMd5(password, user.getLoginname());
		if (!user.getPassword().equals(pwdForMd5)) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public void editCurrentUser(String id, String name, String photoid) {
		User user = userDaoImpl.getEntity(id);
		user.setName(name);
		user.setImgid(photoid);
	}

	@Override
	public Integer getUsersNum() {
		return userDaoImpl.getUsersNum();
	}
}
