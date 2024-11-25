package com.farm.doc.server.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.authority.FarmAuthorityService;
import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.query.DataQuerys;
import com.farm.core.sql.query.DataQuery.CACHE_UNIT;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.result.ResultsHandle;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.FarmDocDaoInter;
import com.farm.doc.dao.FarmDocgroupDaoInter;
import com.farm.doc.dao.FarmDocgroupUserDaoInter;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDocgroup;
import com.farm.doc.domain.FarmDocgroupUser;
import com.farm.doc.domain.FarmDoctext;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.domain.ex.GroupBrief;
import com.farm.doc.domain.ex.GroupEntire;
import com.farm.doc.exception.CanNoDeleteException;
import com.farm.doc.exception.NoGroupAuthForLicenceException;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.UsermessageServiceInter;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;
import com.farm.parameter.FarmParameterService;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.doc.server.FarmFileManagerInter;

/**
 * 工作小组
 * 
 * @author MAC_wd
 */
@Service
public class FarmDocgroupManagerImpl implements FarmDocgroupManagerInter {
	@Resource
	private FarmDocgroupDaoInter farmDocgroupDao;
	@Resource
	private FarmDocgroupUserDaoInter farmDocgroupUserDao;
	@Resource
	private FarmDocmessageManagerInter farmDocmessageServer;
	@Resource
	private FarmDocDaoInter farmDocDao;
	@Resource
	private FarmDocManagerInter farmdocServer;
	@Resource
	private FarmDocOperateRightInter farmDocOperate;
	@Resource
	private FarmFileManagerInter farmFileServer;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private UsermessageServiceInter usermessageServiceImpl;

	private static final Logger log = Logger.getLogger(FarmDocgroupManagerImpl.class);

	@Transactional
	public FarmDocgroup editFarmDocgroupEntity(FarmDocgroup entity, LoginUser user) {
		FarmDocgroup entity2 = farmDocgroupDao.getEntity(entity.getId());
		// entity2.setEuser(user.getId());
		// entity2.setEusername(user.getName());
		// entity2.setEtime(TimeTool.getTimeDate14());
		entity2.setPstate(entity.getPstate());
		entity2.setPcontent(entity.getPcontent());
		entity2.setGroupname(entity.getGroupname());
		entity2.setGroupnote(entity.getGroupnote());
		entity2.setGrouptag(entity.getGrouptag());
		entity2.setGroupimg(entity.getGroupimg());
		entity2.setJoincheck(entity.getJoincheck());
		farmDocgroupDao.editEntity(entity2);
		return entity2;
	}

	@Transactional
	public void deleteFarmDocgroupEntity(String groupId, LoginUser user) {
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("GROUPID", groupId, "="));
		String homeDocId = farmDocgroupDao.getEntity(groupId).getHomedocid();
		if (homeDocId != null) {
			try {
				if (farmdocServer.getDocOnlyBean(homeDocId) != null) {
					farmdocServer.deleteDocNoPop(homeDocId, user);
				}
			} catch (CanNoDeleteException e) {
				throw new RuntimeException(e);
			}
		}
		farmDocgroupUserDao.deleteEntitys(list);
		{
			List<DBRule> doclist = new ArrayList<DBRule>();
			doclist.add(new DBRule("DOCGROUPID", groupId, "="));
			// 释放所有文档
			for (Doc node : farmDocDao.selectEntitys(doclist)) {
				farmDocOperate.flyDoc(node);
			}
		}

		farmDocgroupDao.deleteEntity(farmDocgroupDao.getEntity(groupId));
	}

	@Transactional
	public FarmDocgroup getFarmDocgroupEntity(String id) {
		if (id == null) {
			return null;
		}
		FarmDocgroup group = farmDocgroupDao.getEntity(id);
		return group;
	}

	@Override
	public DataQuery createFarmDocgroupQueryJoinUser(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query, "farm_docgroup a left join FARM_DOCGROUP_USER b on b.GROUPID=a.id",
				"a.id as ID,b.SHOWHOME as SHOWHOME, a.PSTATE as PSTATE,a.PCONTENT as PCONTENT,GROUPNAME,GROUPNOTE,GROUPTAG,GROUPIMG,JOINCHECK");
		return dbQuery;
	}

	@Override
	public DataQuery createFarmDocgroupQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query,
				"(select ID,(SELECT COUNT(a.id) FROM farm_doc a WHERE a.DOCGROUPID=farm_docgroup.ID ) as DOCNUM,PSTATE,PCONTENT,USERNUM,CUSERNAME,CTIME,GROUPNAME,GROUPNOTE,GROUPTAG,GROUPIMG,JOINCHECK from farm_docgroup) b ",
				"ID,DOCNUM,PSTATE,PCONTENT,USERNUM,CUSERNAME,CTIME,GROUPNAME,GROUPNOTE,GROUPTAG,GROUPIMG,JOINCHECK");
		return dbQuery;
	}

	@Transactional
	public FarmDocgroupUser editFarmDocgroupUserEntity(FarmDocgroupUser entity, LoginUser user) {
		FarmDocgroupUser entity2 = farmDocgroupUserDao.getEntity(entity.getId());
		// entity2.setEuser(user.getId());
		// entity2.setEusername(user.getName());
		// entity2.setEtime(TimeTool.getTimeDate14());
		entity2.setPstate(entity.getPstate());
		entity2.setPcontent(entity.getPcontent());
		entity2.setGroupid(entity.getGroupid());
		entity2.setUserid(entity.getUserid());
		entity2.setLeadis(entity.getLeadis());
		entity2.setEditis(entity.getEditis());
		entity2.setShowhome(entity.getShowhome());
		entity2.setShowsort(entity.getShowsort());
		farmDocgroupUserDao.editEntity(entity2);
		return entity2;
	}

	@Transactional
	public void deleteFarmDocgroupUserEntity(String entity, LoginUser user) {
		farmDocgroupUserDao.deleteEntity(farmDocgroupUserDao.getEntity(entity));
	}

	@Transactional
	public FarmDocgroupUser getFarmDocgroupUserEntity(String id) {
		if (id == null) {
			return null;
		}
		return farmDocgroupUserDao.getEntity(id);
	}

	@Override
	public DataQuery createFarmDocgroupUserSimpleQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query,
				"farm_docgroup_user a left join alone_auth_user b on a.userid=b.id left join farm_docgroup c on a.groupid=c.id",
				"a.id as id,a.PSTATE as PSTATE,GROUPID,b.name as username,LEADIS,EDITIS,SHOWHOME,SHOWSORT,c.groupname as groupname");
		return dbQuery;
	}

	// ----------------------------------------------------------------------------------

	public FarmDocgroupUserDaoInter getFarmDocgroupUserDao() {
		return farmDocgroupUserDao;
	}

	public FarmDocDaoInter getFarmDocDao() {
		return farmDocDao;
	}

	public FarmDocManagerInter getFarmdocServer() {
		return farmdocServer;
	}

	public void setFarmdocServer(FarmDocManagerInter farmdocServer) {
		this.farmdocServer = farmdocServer;
	}

	public void setFarmDocDao(FarmDocDaoInter farmDocDao) {
		this.farmDocDao = farmDocDao;
	}

	public FarmDocmessageManagerInter getFarmDocmessageServer() {
		return farmDocmessageServer;
	}

	public void setFarmDocmessageServer(FarmDocmessageManagerInter farmDocmessageServer) {
		this.farmDocmessageServer = farmDocmessageServer;
	}

	public void setFarmDocgroupUserDao(FarmDocgroupUserDaoInter farmDocgroupUserDao) {
		this.farmDocgroupUserDao = farmDocgroupUserDao;
	}

	public FarmFileManagerInter getFarmFileServer() {
		return farmFileServer;
	}

	public void setFarmFileServer(FarmFileManagerInter farmFileServer) {
		this.farmFileServer = farmFileServer;
	}

	public FarmDocgroupDaoInter getFarmDocgroupDao() {
		return farmDocgroupDao;
	}

	public void setFarmDocgroupDao(FarmDocgroupDaoInter farmDocgroupDao) {
		this.farmDocgroupDao = farmDocgroupDao;
	}

	public FarmDocOperateRightInter getFarmDocOperate() {
		return farmDocOperate;
	}

	public void setFarmDocOperate(FarmDocOperateRightInter farmDocOperate) {
		this.farmDocOperate = farmDocOperate;
	}

	@Override
	@Transactional
	public FarmDocgroup creatDocGroup(String groupname, String grouptag, String groupimg, boolean joincheck,
			String groupnote, LoginUser currentUser) throws NoGroupAuthForLicenceException {
		// String licence = FarmConstant.LICENCE;
		// if (!MayCase.isCase(licence == null ? "NONE" : licence)) {
		// FarmManager.instance().initLicence(currentUser);
		// FarmConstant.LICENCE_FLAG="0";
		// throw new NoGroupAuthForLicenceException();
		// }
		FarmDocgroup entity = new FarmDocgroup();
		entity.setCuser(currentUser.getId());
		entity.setCtime(TimeTool.getTimeDate14());
		entity.setCusername(currentUser.getName());
		entity.setEuser(currentUser.getId());
		entity.setEusername(currentUser.getName());
		entity.setEtime(TimeTool.getTimeDate14());
		entity.setPstate("1");
		entity.setGroupimg(groupimg);
		if (groupimg.trim().length() <= 0) {
			throw new RuntimeException("小组头像不能为空");
		}
		farmFileServer.submitFile(groupimg);
		entity.setGroupname(groupname);
		entity.setGroupnote(groupnote);
		entity.setUsernum(1);
		entity.setGrouptag(grouptag);
		entity.setJoincheck(joincheck ? "1" : "0");
		entity = farmDocgroupDao.insertEntity(entity);
		// 将小组付给当前用户并设置为管理员
		FarmDocgroupUser goupuser = new FarmDocgroupUser();
		goupuser.setCuser(currentUser.getId());
		goupuser.setCtime(TimeTool.getTimeDate14());
		goupuser.setCusername(currentUser.getName());
		goupuser.setEuser(currentUser.getId());
		goupuser.setEusername(currentUser.getName());
		goupuser.setEtime(TimeTool.getTimeDate14());
		goupuser.setPstate("1");
		goupuser.setGroupid(entity.getId());
		goupuser.setUserid(currentUser.getId());
		goupuser.setLeadis("1");
		goupuser.setEditis("1");
		goupuser.setShowhome("1");
		goupuser.setShowsort(10);
		farmDocgroupUserDao.insertEntity(goupuser);
		{
			// 创建小组首页文档
			DocEntire homedoc = new DocEntire(new Doc());
			// 标题、发布时间、内容类型是必填 texts中的TEXT1中存放超文本内容
			homedoc.getDoc().setTitle(entity.getGroupname());
			homedoc.getDoc().setPubtime(TimeTool.getTimeDate14());
			homedoc.getDoc().setDomtype("4");
			homedoc.getDoc().setWritepop(POP_TYPE.DOCGROUP.getValue());
			homedoc.getDoc().setReadpop(POP_TYPE.DOCGROUP.getValue());
			homedoc.getDoc().setDocgroupid(entity.getId());
			homedoc.getDoc().setState("1");
			FarmDoctext docText = new FarmDoctext();
			docText.setCtime(TimeTool.getTimeDate12());
			docText.setCuser(currentUser.getId());
			docText.setCusername(currentUser.getName());
			docText.setEtime(TimeTool.getTimeDate12());
			docText.setEuser(currentUser.getId());
			docText.setEusername(currentUser.getName());
			docText.setText1(groupnote);
			homedoc.setTexts(docText);
			// homedoc.setTexts("欢迎访问小组首页", currentUser);
			homedoc = farmdocServer.createDoc(homedoc, currentUser);
			entity.setHomedocid(homedoc.getDoc().getId());
			farmDocgroupDao.editEntity(entity);
		}
		return entity;
	}

	@Override
	@Transactional
	public FarmDocgroup editDocGroup(String id, String groupname, String grouptag, String groupimg, boolean joincheck,
			String groupnote, LoginUser currentUser) {
		List<DBRule> rules = new ArrayList<DBRule>();
		rules.add(new DBRule("GROUPID", id, "="));
		rules.add(new DBRule("LEADIS", "1", "="));
		rules.add(new DBRule("USERID", currentUser.getId(), "="));
		if (farmDocgroupUserDao.selectEntitys(rules).size() <= 0) {
			throw new RuntimeException("用户没有修改小组权限");
		}
		FarmDocgroup entity2 = farmDocgroupDao.getEntity(id);
		entity2.setEuser(currentUser.getId());
		entity2.setEusername(currentUser.getName());
		entity2.setEtime(TimeTool.getTimeDate14());
		entity2.setGroupname(groupname);
		entity2.setGroupnote(groupnote);
		entity2.setGrouptag(grouptag);
		farmFileServer.cancelFile(entity2.getGroupimg());
		farmFileServer.submitFile(groupimg);
		entity2.setGroupimg(groupimg);
		entity2.setJoincheck(joincheck ? "1" : "0");
		farmDocgroupDao.editEntity(entity2);
		return entity2;
	}

	@Override
	@Transactional
	public boolean isJoinCheck(String groupid) {
		return getFarmDocgroupEntity(groupid).getJoincheck().equals("1");
	}

	@Override
	@Transactional
	public FarmDocgroupUser applyGroup(String groupId, String joinUserId, String note, LoginUser currentUser) {
		FarmDocgroup entity = farmDocgroupDao.getEntity(groupId);
		FarmDocgroupUser goupuser = new FarmDocgroupUser();
		goupuser.setCuser(currentUser.getId());
		goupuser.setCtime(TimeTool.getTimeDate14());
		goupuser.setCusername(currentUser.getName());
		goupuser.setEuser(currentUser.getId());
		goupuser.setEusername(currentUser.getName());
		goupuser.setEtime(TimeTool.getTimeDate14());
		// 申请
		if (entity.getJoincheck().equals("1")) {
			goupuser.setPstate("3");
		} else {
			goupuser.setPstate("1");
			entity.setUsernum(getAllUserNoApplyByGroup(groupId).size() + 1);
			farmDocgroupDao.editEntity(entity);
		}
		goupuser.setGroupid(groupId);
		goupuser.setUserid(joinUserId);
		goupuser.setLeadis("0");
		goupuser.setEditis("0");
		goupuser.setShowhome("1");
		goupuser.setShowsort(10);
		goupuser.setApplynote(note);
		farmDocgroupUserDao.insertEntity(goupuser);
		// 发送消息给管理员
		// for (User node : getAllAdministratorByGroup(groupId)) {
		// try {
		// // 申请
		// if (entity.getJoincheck().equals("1")) {
		// farmDocmessageServer.sendMessage(node.getId(),
		// "有用户'" + currentUser.getName() + "'申请加入" + entity.getGroupname() +
		// "小组,申请备注：" + note,
		// "有用户'" + currentUser.getName() + "'申请加入" + entity.getGroupname() +
		// "小组", "工作小组",
		// currentUser);
		// } else {
		// farmDocmessageServer.sendMessage(node.getId(),
		// "用户'" + currentUser.getName() + "'加入" + entity.getGroupname() + "小组",
		// "有用户'" + currentUser.getName() + "'加入" + entity.getGroupname() +
		// "小组", "工作小组", currentUser);
		// }
		//
		// } catch (Exception e) {
		// log.error("用户申请加入小组时发送站内消息失败" + e.getMessage());
		// }
		// }
		for (User node : getAllAdministratorByGroup(groupId)) {
			try {
				// 申请
				if (entity.getJoincheck().equals("1")) {
					usermessageServiceImpl.sendMessage(node.getId(),
							"有用户'" + currentUser.getName() + "'申请加入" + entity.getGroupname() + "小组,申请备注：" + note + "。请进入\"小组首页/成员管理\"进行审核。",
							"有用户'" + currentUser.getName() + "'申请加入" + entity.getGroupname() + "小组", "工作小组",
							currentUser);
				} else {
					usermessageServiceImpl.sendMessage(node.getId(),
							"用户'" + currentUser.getName() + "'加入" + entity.getGroupname() + "小组",
							"有用户'" + currentUser.getName() + "'加入" + entity.getGroupname() + "小组", "工作小组", currentUser);
				}
			} catch (Exception e) {
				log.error("用户申请加入小组时发送站内消息失败" + e.getMessage());
			}
		}
		return goupuser;
	}

	@Override
	@Transactional
	public void inviteGroup(String groupId, String joinUserId, boolean isLead, boolean isEdit, LoginUser currentUser) {
		FarmDocgroupUser goupuser = new FarmDocgroupUser();
		goupuser.setCuser(currentUser.getId());
		goupuser.setCtime(TimeTool.getTimeDate14());
		goupuser.setCusername(currentUser.getName());
		goupuser.setEuser(currentUser.getId());
		goupuser.setEusername(currentUser.getName());
		goupuser.setEtime(TimeTool.getTimeDate14());
		// 邀请
		goupuser.setPstate("0");
		goupuser.setGroupid(groupId);
		goupuser.setUserid(joinUserId);
		goupuser.setLeadis(isLead ? "1" : "0");
		goupuser.setEditis(isEdit ? "1" : "0");
		goupuser.setShowhome("1");
		goupuser.setShowsort(10);
		farmDocgroupUserDao.insertEntity(goupuser);
	}

	@Override
	@Transactional
	public boolean isJoinGroupByUser(String groupId, String userId) {
		List<DBRule> rules = new ArrayList<DBRule>();
		rules.add(new DBRule("GROUPID", groupId, "="));
		rules.add(new DBRule("USERID", userId, "="));
		if (farmDocgroupUserDao.selectEntitys(rules).size() <= 0) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public FarmDocgroupUser getFarmDocgroupUser(String groupId, String userId) {
		List<DBRule> rules = new ArrayList<DBRule>();
		rules.add(new DBRule("GROUPID", groupId, "="));
		rules.add(new DBRule("USERID", userId, "="));
		List<FarmDocgroupUser> list = farmDocgroupUserDao.selectEntitys(rules);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	@Transactional
	public void leaveGroup(String groupID, String userId) {
		List<DBRule> rules = new ArrayList<DBRule>();
		rules.add(new DBRule("GROUPID", groupID, "="));
		rules.add(new DBRule("USERID", userId, "="));
		FarmDocgroup group = farmDocgroupDao.getEntity(groupID);
		{
			// 更新成员数量
			group.setUsernum(getAllUserNoApplyByGroup(group.getId()).size() - 1);
			farmDocgroupDao.editEntity(group);
		}
		farmDocgroupUserDao.deleteEntitys(rules);
		LoginUser user = FarmAuthorityService.getInstance().getUserById(userId);
		for (User node : getAllAdministratorByGroup(groupID)) {
			try {
				// 退出小组
				farmDocmessageServer.sendMessage(node.getId(),
						"用户'" + user.getName() + "'退出" + group.getGroupname() + "小组",
						"有用户'" + user.getName() + "'退出" + group.getGroupname() + "小组", "工作小组", user);
			} catch (Exception e) {
				log.error("用户退出小组时发送站内消息失败" + e.getMessage());
			}
		}
	}

	@Override
	@Transactional
	public List<User> getAllAdministratorByGroup(String groupId) {
		List<User> list = null;
		DataQuery query = DataQuery.getInstance(1,
				"c.COMMENTS AS COMMENTS, c.CTIME AS CTIME, c.CUSER AS CUSER, c.ID AS ID, c.LOGINNAME AS LOGINNAME, c.LOGINTIME AS LOGINTIME, c. NAME AS NAME, c.STATE AS STATE, c.TYPE AS TYPE, c.IMGID AS IMGID",
				" FARM_DOCGROUP a LEFT JOIN FARM_DOCGROUP_USER b ON a.ID = b.GROUPID LEFT JOIN ALONE_AUTH_USER c ON c.ID = b.USERID");
		query.setPagesize(1000);
		query.setNoCount();
		query.addRule(new DBRule("a.id", groupId, "="));
		query.addRule(new DBRule("b.PSTATE", "1", "="));
		query.addRule(new DBRule("b.LEADIS", "1", "="));
		try {
			list = query.search().getObjectList(User.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<>();
		}
		return list;
	}

	@Override
	@Transactional
	public List<FarmDocgroupUser> getAllUserByGroup(String groupId) {
		// 1在用，0邀请，3申请
		List<DBRule> rules = new ArrayList<DBRule>();
		rules.add(new DBRule("GROUPID", groupId, "="));
		return farmDocgroupUserDao.selectEntitys(rules);
	}

	@Override
	@Transactional
	public List<User> getAllUserNoApplyByGroup(String groupId) {
		List<User> list = null;
		DataQuery query = DataQuery.getInstance(1,
				"c.COMMENTS AS COMMENTS, c.CTIME AS CTIME, c.CUSER AS CUSER, c.ID AS ID, c.LOGINNAME AS LOGINNAME, c.LOGINTIME AS LOGINTIME, c. NAME AS NAME, c.STATE AS STATE, c.TYPE AS TYPE, c.IMGID AS IMGID",
				" FARM_DOCGROUP a LEFT JOIN FARM_DOCGROUP_USER b ON a.ID = b.GROUPID LEFT JOIN ALONE_AUTH_USER c ON c.ID = b.USERID");
		query.setPagesize(1000);
		query.setNoCount();
		query.addRule(new DBRule("a.id", groupId, "="));
		query.addRule(new DBRule("b.PSTATE", "1", "="));
		try {
			list = query.search().getObjectList(User.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<>();
		}
		return list;
	}

	@Override
	@Transactional
	public void agreeJoinApply(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		gu.setPstate("1");
		{
			// 更新成员数量
			FarmDocgroup group = farmDocgroupDao.getEntity(gu.getGroupid());
			group.setUsernum(getAllUserNoApplyByGroup(group.getId()).size() + 1);
			farmDocgroupDao.editEntity(group);
		}
		farmDocgroupUserDao.editEntity(gu);
		try {
			farmDocmessageServer.sendMessage(gu.getUserid(), "见主题",
					"小组‘" + getFarmDocgroupEntity(gu.getGroupid()).getGroupname() + "’已经同意你的加入申请", null, currentUser);
		} catch (Exception e) {
			log.error("用户申请加入小组时发送站内消息失败" + e.getMessage());
		}

	}

	@Override
	@Transactional
	public void refuseJoinApply(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		farmDocgroupUserDao.deleteEntity(gu);
	}

	@Override
	@Transactional
	public void leaveGroup(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		{
			// 更新成员数量
			FarmDocgroup group = farmDocgroupDao.getEntity(gu.getGroupid());
			group.setUsernum(getAllUserNoApplyByGroup(group.getId()).size() - 1);
			farmDocgroupDao.editEntity(group);
		}
		farmDocgroupUserDao.deleteEntity(gu);
	}

	@Override
	@Transactional
	public void setAdminForGroup(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		gu.setLeadis("1");
		farmDocgroupUserDao.editEntity(gu);
	}

	@Override
	@Transactional
	public void setEditorForGroup(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		gu.setEditis("1");
		farmDocgroupUserDao.editEntity(gu);
	}

	@Override
	@Transactional
	public void wipeAdminFromGroup(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		gu.setLeadis("0");
		farmDocgroupUserDao.editEntity(gu);
	}

	@Override
	@Transactional
	public void wipeEditorForGroup(String groupUserId, LoginUser currentUser) {
		FarmDocgroupUser gu = farmDocgroupUserDao.getEntity(groupUserId);
		if (!isAdminForGroup(currentUser.getId(), gu.getGroupid())) {
			throw new RuntimeException("没有操作权限");
		}
		gu.setEditis("0");
		farmDocgroupUserDao.editEntity(gu);
	}

	@Override
	@Transactional
	public boolean isAdminForGroup(String userid, String groupId) {
		FarmDocgroupUser groupUser = getFarmDocgroupUser(groupId, userid);
		if (groupUser == null) {
			return false;
		}
		if (groupUser.getLeadis().equals("1")) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public void setGroupHomeHide(String groupId, String userId) {
		FarmDocgroupUser groupUser = getFarmDocgroupUser(groupId, userId);
		groupUser.setShowhome("0");
		farmDocgroupUserDao.editEntity(groupUser);
	}

	@Override
	@Transactional
	public void setGroupHomeShow(String groupId, String userId) {
		FarmDocgroupUser groupUser = getFarmDocgroupUser(groupId, userId);
		groupUser.setShowhome("1");
		farmDocgroupUserDao.editEntity(groupUser);
	}

	@Override
	@Transactional
	public void setGroupSortUp(String groupId, String userId) {
		List<FarmDocgroupUser> lists = getAllGroupUserByUser(userId);
		Collections.sort(lists, new Comparator<FarmDocgroupUser>() {
			@Override
			public int compare(FarmDocgroupUser o1, FarmDocgroupUser o2) {
				if (o1.getShowsort().compareTo(o2.getShowsort()) == 0) {
					return o1.getCtime().compareTo(o2.getCtime());
				} else {
					return o1.getShowsort().compareTo(o2.getShowsort());
				}
			}
		});
		int n = 0;
		int index = 0;
		for (FarmDocgroupUser node : lists) {
			node.setShowsort(n);
			if (node.getGroupid().equals(groupId)) {
				index = n;
			}
			farmDocgroupUserDao.editEntity(node);
			n++;
		}
		if (index == 0) {
			return;
		}
		int temp = 0;
		temp = lists.get(index).getShowsort();
		lists.get(index).setShowsort(lists.get(index - 1).getShowsort());
		lists.get(index - 1).setShowsort(temp);
		farmDocgroupUserDao.editEntity(lists.get(index));
		farmDocgroupUserDao.editEntity(lists.get(index - 1));
	}

	@Override
	@Transactional
	public List<FarmDocgroupUser> getAllGroupUserByUser(String userId) {
		List<DBRule> rules = new ArrayList<DBRule>();
		rules.add(new DBRule("USERID", userId, "="));
		return farmDocgroupUserDao.selectEntitys(rules);
	}

	@Override
	public List<Map<String, Object>> getEditorGroupByUser(String userId) {
		DataQuery query = DataQuery.getInstance("1", "b.groupNAME AS NAME,b.ID AS id",
				"FARM_DOCGROUP_USER a  LEFT JOIN farm_docgroup b ON a.GROUPID=b.ID");
		query.setPagesize(20);
		DataResult result = null;
		// /a.EDITIS='1' AND b.PSTATE='1' AND a.USERID=''
		query.addRule(new DBRule("a.EDITIS", "1", "="));
		query.addRule(new DBRule("b.PSTATE", "1", "="));
		query.addRule(new DBRule("a.USERID", userId, "="));
		try {
			result = query.search();
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return result.getResultList();
	}

	@Override
	@Transactional
	public boolean isGroupEditor(String docgroupid, String userId) {
		if (!isJoinGroupByUser(docgroupid, userId)) {
			return false;
		}
		if (getFarmDocgroupUser(docgroupid, userId).getEditis().equals("1")) {
			return true;
		}
		return false;
	}

	@Override
	public DataQuery getGroupNewDocQuery(DataQuery query, String groupId, LoginUser currentUser) {
		String userid = "none";
		if (currentUser != null) {
			userid = currentUser.getId();
		}
		DataQuerys.wipeVirus(groupId);
		StringBuffer sqlform = new StringBuffer();
		sqlform.append("(SELECT DISTINCT ");
		sqlform.append(
				"A.ID AS ID,B.EVALUATE as EVALUATE,ANSWERINGNUM, A.DOMTYPE AS DOMTYPE,A.ETIME as ETIME, A.TITLE AS TITLE, A.DOCDESCRIBE AS DOCDESCRIBE,A.AUTHOR AS AUTHOR, A.PUBTIME AS PUBTIME, A.TAGKEY  AS TAGKEY, A.IMGID AS IMGID, B.VISITNUM    AS VISITNUM, B.PRAISEYES   AS PRAISEYES, B.PRAISENO    AS PRAISENO, B.HOTNUM      AS HOTNUM, D.NAME        AS TYPENAME ");
		sqlform.append(
				" FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID = b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID = a.ID LEFT JOIN farm_doctype d   ON d.ID = c.TYPEID LEFT JOIN farm_docgroup_user e   ON e.GROUPID=a.DOCGROUPID  ");
		sqlform.append(" WHERE 1 = 1 AND A.STATE = '1'  AND DOCGROUPID = '" + groupId
				+ "' AND (a.READPOP = '1'   OR (a.READPOP = '2' AND e.USERID = '" + userid
				+ "')  OR (a.READPOP = '0' AND a.CUSER = '" + userid + "'))");
		sqlform.append(" ) ");
		query = DataQuery.init(query, sqlform.toString() + " a",
				"ID,DOMTYPE,TITLE,DOCDESCRIBE,AUTHOR,PUBTIME,ANSWERINGNUM,TAGKEY,IMGID, VISITNUM,PRAISEYES,PRAISENO,EVALUATE, HOTNUM, TYPENAME,ETIME");
		query.addSort(new DBSort("ETIME", "desc"));
		query.setPagesize(10);
		return query;
	}

	@Override
	public DataQuery getGroupGoodDocQuery(DataQuery query, String groupId, LoginUser currentUser) {
		String userid = "none";
		if (currentUser != null) {
			userid = currentUser.getId();
		}
		DataQuerys.wipeVirus(groupId);
		StringBuffer sqlform = new StringBuffer();
		sqlform.append("(SELECT DISTINCT ");
		sqlform.append(
				"A.ID AS ID,B.EVALUATE as EVALUATE,ANSWERINGNUM, A.DOMTYPE AS DOMTYPE,A.ETIME as ETIME, A.TITLE AS TITLE, A.DOCDESCRIBE AS DOCDESCRIBE,A.AUTHOR AS AUTHOR, A.PUBTIME AS PUBTIME, A.TAGKEY  AS TAGKEY, A.IMGID AS IMGID, B.VISITNUM    AS VISITNUM, B.PRAISEYES   AS PRAISEYES, B.PRAISENO    AS PRAISENO, B.HOTNUM      AS HOTNUM, D.NAME        AS TYPENAME ");
		sqlform.append(
				" FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID = b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID = a.ID LEFT JOIN farm_doctype d   ON d.ID = c.TYPEID LEFT JOIN farm_docgroup_user e   ON e.GROUPID=a.DOCGROUPID  ");
		sqlform.append(" WHERE 1 = 1 AND A.STATE = '1'  AND DOCGROUPID = '" + groupId
				+ "' AND (a.READPOP = '1'   OR (a.READPOP = '2' AND e.USERID = '" + userid
				+ "')  OR (a.READPOP = '0' AND a.CUSER = '" + userid + "'))");
		sqlform.append(" ) ");
		query = DataQuery.init(query, sqlform.toString() + " a",
				"ID,DOMTYPE,TITLE,DOCDESCRIBE,AUTHOR,PUBTIME,ANSWERINGNUM,TAGKEY,IMGID, VISITNUM,PRAISEYES,PRAISENO,EVALUATE, HOTNUM, TYPENAME,ETIME");
		query.addSort(new DBSort("EVALUATE", "desc"));
		query.setPagesize(10);
		return query;
	}

	@Override
	public DataQuery getGroupHotDocQuery(DataQuery query, String groupId, LoginUser currentUser) {
		StringBuffer sqlform = new StringBuffer();
		String userid = "none";
		if (currentUser != null) {
			userid = currentUser.getId();
		}
		DataQuerys.wipeVirus(groupId);
		sqlform.append("(SELECT DISTINCT ");
		sqlform.append(
				"A.ID AS ID,B.EVALUATE as EVALUATE, A.DOMTYPE AS DOMTYPE,ANSWERINGNUM,A.ETIME as ETIME, A.TITLE AS TITLE, A.DOCDESCRIBE AS DOCDESCRIBE,A.AUTHOR AS AUTHOR, A.PUBTIME AS PUBTIME, A.TAGKEY  AS TAGKEY, A.IMGID AS IMGID, B.VISITNUM    AS VISITNUM, B.PRAISEYES   AS PRAISEYES, B.PRAISENO    AS PRAISENO, B.HOTNUM      AS HOTNUM, D.NAME        AS TYPENAME ");
		sqlform.append(
				" FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID = b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID = a.ID LEFT JOIN farm_doctype d   ON d.ID = c.TYPEID LEFT JOIN farm_docgroup_user e   ON e.GROUPID=a.DOCGROUPID  ");
		sqlform.append(" WHERE 1 = 1 AND A.STATE = '1'  AND DOCGROUPID = '" + groupId
				+ "' AND (a.READPOP = '1'   OR (a.READPOP = '2' AND e.USERID = '" + userid
				+ "')  OR (a.READPOP = '0' AND a.CUSER = '" + userid + "'))");
		sqlform.append(" ) ");
		query = DataQuery.init(query, sqlform.toString() + " a",
				"ID,DOMTYPE,TITLE,DOCDESCRIBE,AUTHOR,PUBTIME,TAGKEY,IMGID,ANSWERINGNUM, VISITNUM,PRAISEYES,PRAISENO,EVALUATE, HOTNUM, TYPENAME,ETIME");
		query.addSort(new DBSort("HOTNUM", "desc"));
		query.setPagesize(10);
		return query;
	}

	@Override
	@Transactional
	public int getGroupDocNum(String groupId) {
		return farmDocgroupDao.getGroupDocNum(groupId);
	}

	@Override
	@Transactional
	public FarmDocgroupUser editMinFarmDocgroupUserEntity(FarmDocgroupUser entity, LoginUser currentUser) {
		FarmDocgroupUser entity2 = farmDocgroupUserDao.getEntity(entity.getId());
		entity2.setPstate(entity.getPstate());
		entity2.setLeadis(entity.getLeadis());
		entity2.setEditis(entity.getEditis());
		entity2.setShowhome(entity.getShowhome());
		// entity2.setShowsort(entity.getShowsort());
		// entity2.setEuser(user.getId());
		// entity2.setEusername(user.getName());
		// entity2.setEtime(TimeTool.getTimeDate14());
		// entity2.setPcontent(entity.getPcontent());
		// entity2.setGroupid(entity.getGroupid());
		// entity2.setUserid(entity.getUserid());
		farmDocgroupUserDao.editEntity(entity2);
		return entity2;
	}

	@Override
	public DataQuery createUserGroupDocQuery(DataQuery query, String userid) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"SELECT A.ID  AS ID,GROUPNAME,f.id as GROUPID, B.EVALUATE    AS EVALUATE, A.DOMTYPE     AS DOMTYPE, A.ETIME       AS ETIME, A.TITLE       AS TITLE, A.DOCDESCRIBE AS DOCDESCRIBE,");
		sqlBuffer.append(
				"A.AUTHOR AS AUTHOR,e.SHOWHOME as SHOWHOME, A.PUBTIME     AS PUBTIME, A.TAGKEY      AS TAGKEY, A.IMGID       AS IMGID, B.VISITNUM    AS VISITNUM, B.PRAISEYES   AS PRAISEYES, B.PRAISENO    AS PRAISENO, B.HOTNUM      AS HOTNUM, ");
		sqlBuffer.append(" D.NAME        AS TYPENAME, f.GROUPIMG    AS groupIMG");
		sqlBuffer.append(
				" FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID = b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID = a.ID");
		sqlBuffer.append(
				"  LEFT JOIN farm_doctype d ON d.ID = c.TYPEID LEFT JOIN farm_docgroup_user e ON e.GROUPID = a.DOCGROUPID LEFT JOIN farm_docgroup f ON f.ID=e.GROUPID");
		sqlBuffer.append(" WHERE 1 = 1  AND A.STATE = '1' AND  e.SHOWHOME ='1'  AND e.USERID='" + userid + "'");
		sqlBuffer.append("   AND (a.READPOP = '1'  OR (a.READPOP = '2' AND e.USERID = '" + userid
				+ "') OR (a.READPOP = '0' AND a.CUSER = '" + userid + "'))");
		query = DataQuery.init(query, "(" + sqlBuffer.toString() + ") a",
				"ID,EVALUATE,DOMTYPE,ETIME,SHOWHOME,GROUPNAME,GROUPID,TITLE,DOCDESCRIBE,AUTHOR,PUBTIME,TAGKEY,IMGID,VISITNUM,PRAISEYES,PRAISENO,HOTNUM,TYPENAME,GROUPIMG");
		query.setPagesize(10);
		return query;
	}

	@Override
	@Transactional
	public FarmDocgroup editDocgroupJoinCheck(boolean isJoinCheck, String groupId, LoginUser currentUser) {
		FarmDocgroup entity2 = farmDocgroupDao.getEntity(groupId);
		entity2.setEuser(currentUser.getId());
		entity2.setEusername(currentUser.getName());
		entity2.setEtime(TimeTool.getTimeDate14());
		entity2.setJoincheck(isJoinCheck ? "1" : "0");
		farmDocgroupDao.editEntity(entity2);
		return entity2;
	}

	@Override
	public DataQuery getGroupBadDocQuery(DataQuery query, String groupId, LoginUser currentUser) {
		String userid = "none";
		if (currentUser != null) {
			userid = currentUser.getId();
		}
		DataQuerys.wipeVirus(groupId);
		StringBuffer sqlform = new StringBuffer();
		sqlform.append("(SELECT DISTINCT ");
		sqlform.append(
				"A.ID AS ID,B.EVALUATE as EVALUATE,ANSWERINGNUM, A.DOMTYPE AS DOMTYPE,A.ETIME as ETIME, A.TITLE AS TITLE, A.DOCDESCRIBE AS DOCDESCRIBE,A.AUTHOR AS AUTHOR, A.PUBTIME AS PUBTIME, A.TAGKEY  AS TAGKEY, A.IMGID AS IMGID, B.VISITNUM    AS VISITNUM, B.PRAISEYES   AS PRAISEYES, B.PRAISENO    AS PRAISENO, B.HOTNUM      AS HOTNUM, D.NAME        AS TYPENAME ");
		sqlform.append(
				" FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID = b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID = a.ID LEFT JOIN farm_doctype d   ON d.ID = c.TYPEID LEFT JOIN farm_docgroup_user e   ON e.GROUPID=a.DOCGROUPID  ");
		sqlform.append(" WHERE 1 = 1 AND A.STATE = '1'  AND DOCGROUPID = '" + groupId
				+ "' AND (a.READPOP = '1'   OR (a.READPOP = '2' AND e.USERID = '" + userid
				+ "')  OR (a.READPOP = '0' AND a.CUSER = '" + userid + "'))");
		sqlform.append(" ) ");
		query = DataQuery.init(query, sqlform.toString() + " a",
				"ID,DOMTYPE,TITLE,DOCDESCRIBE,AUTHOR,PUBTIME,ANSWERINGNUM,TAGKEY,IMGID, VISITNUM,PRAISEYES,PRAISENO,EVALUATE, HOTNUM, TYPENAME,ETIME");
		query.addSort(new DBSort("EVALUATE", "asc"));
		query.setPagesize(10);
		return query;
	}

	@Override
	public DataQuery createFarmDocgroupQueryNuContainUser(DataQuery query, String userid) {
		DataQuery dbQuery = DataQuery.init(query,
				"farm_docgroup a LEFT JOIN FARM_DOCGROUP_USER b ON b.GROUPID=a.ID  AND b.USERID='" + userid + "'",
				"a.ID as ID,(SELECT COUNT(id) FROM farm_doc  WHERE DOCGROUPID=a.ID ) as DOCNUM,a.PSTATE as PSTATE,a.PCONTENT as PCONTENT,a.USERNUM as USERNUM,a.CUSERNAME as CUSERNAME,a.CTIME as CTIME,a.GROUPNAME as GROUPNAME,a.GROUPNOTE as GROUPNOTE,a.GROUPTAG as GROUPTAG,a.GROUPIMG as GROUPIMG,a.JOINCHECK as JOINCHECK,b.USERID as USERID");
		dbQuery.addSqlRule(" and USERID is null");
		query.setNoCount();
		return dbQuery;
	}

	@Override
	public List<GroupBrief> getHotDocGroups(int num) {
		return getHotDocGroups(num, null);
	}

	@Override
	public List<GroupBrief> getHotDocGroups(int num, LoginUser currentUser) {
		DataQuery query = DataQuery.init(new DataQuery(),
				"FARM_DOCGROUP as a left join FARM_DOCGROUP_USER as b on a.id=b.GROUPID and b.USERID='"
						+ (currentUser == null ? "NONE" : currentUser.getId()) + "'",
				"a.ID AS ID, a.CTIME AS CTIME, a.ETIME AS ETIME, a.CUSERNAME AS CUSERNAME, a.CUSER AS CUSER, a.EUSERNAME AS EUSERNAME, a.EUSER AS EUSER, a.PSTATE AS PSTATE, a.PCONTENT AS PCONTENT, a.GROUPNAME AS GROUPNAME, a.GROUPNOTE AS GROUPNOTE, a.GROUPTAG AS GROUPTAG, a.GROUPIMG AS GROUPIMG, a.JOINCHECK AS JOINCHECK, a.USERNUM AS USERNUM, a.HOMEDOCID AS HOMEDOCID, b.USERID AS USERID");
		query.addSort(new DBSort("a.USERNUM", "desc"));
		query.setCurrentPage(1);
		query.setDistinct(true);
		query.setPagesize(num);
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
				CACHE_UNIT.second);
		try {
			DataResult result = query.search();
			result.runHandle(new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					row.put("IMGURL", farmFileServer.getFileURL(row.get("GROUPIMG").toString()));
					if (row.get("USERID") != null && !row.get("USERID").toString().isEmpty()) {
						row.put("USERJOIN", "1");
					} else {
						row.put("USERJOIN", "0");
					}
				}
			});
			return result.getObjectList(GroupBrief.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public GroupEntire getFarmDocgroup(String groupid) {
		if (groupid == null) {
			return null;
		}
		FarmDocgroup group = farmDocgroupDao.getEntity(groupid);
		GroupEntire groupb = new GroupEntire();
		try {
			BeanUtils.copyProperties(groupb, group);
		} catch (Exception e) {
			log.error(e.toString());
			return groupb;
		}
		groupb.setUsers(getAllUserNoApplyByGroup(groupid));
		groupb.setAdmins(getAllAdministratorByGroup(groupid));
		if (group.getGroupimg() != null && group.getGroupimg().trim().length() <= 0) {
			group.setGroupimg(null);
		}
		if (group.getGroupimg() != null) {
			groupb.setImgurl(farmFileServer.getFileURL(group.getGroupimg()));
		}
		if (group.getGrouptag() != null) {
			String tags = group.getGrouptag();
			String[] tags1 = tags.trim().replaceAll("，", ",").replaceAll("、", ",").split(",");
			groupb.setTags(Arrays.asList(tags1));
		}
		return groupb;
	}

	@Override
	public DataResult getNewGroupDoc(LoginUser user, String gourpid, int pagesize, Integer pagenum) {
		DataQuery query = DataQuery.init(new DataQuery(),
				"FARM_DOC a LEFT JOIN farm_docruninfo b on a.RUNINFOID=b.ID left join FARM_RF_DOCTYPE f on a.id=f.docid left join FARM_DOCTYPE g on g.id=f.typeid  left join FARM_DOCGROUP d on a.DOCGROUPID=d.ID left join FARM_DOCGROUP_USER c on c.GROUPID=d.ID and c.USERID='"
						+ (user == null ? "none" : user.getId()) + "' LEFT JOIN ALONE_AUTH_USER e ON e.ID = a.CUSER  ",
				"a.ID as DOCID,d.GROUPNAME as GROUPNAME,d.ID as GROUPID,d.GROUPIMG as GROUPIMG,a.DOMTYPE as DOMTYPE,a.TITLE AS title,a.DOCDESCRIBE AS DOCDESCRIBE,a.AUTHOR AS AUTHOR,a.PUBTIME AS PUBTIME,a.TAGKEY AS TAGKEY ,a.IMGID AS IMGID,b.VISITNUM AS VISITNUM,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,b.EVALUATE as EVALUATE,b.ANSWERINGNUM as ANSWERINGNUM, E.ID AS USERID, E.NAME AS USERNAME, E.IMGID AS USERIMGID");
		query.addSort(new DBSort("a.etime", "desc"));
		query.setCurrentPage(pagenum);
		query.setDistinct(true);
		query.addRule(new DBRule("a.STATE", "1", "="));
		if (gourpid != null) {
			query.addRule(new DBRule("a.DOCGROUPID", gourpid, "="));
		}
		query.addRule(new DBRule("DOMTYPE", "4", "!="));
		// 文章三种情况判断
		// 1.文章阅读权限为公共
		// 2.文章的创建者为当前登录用户
		// 3.文章的阅读权限为小组，并且当前登陆用户为组内成员.(使用子查询处理)
		// 4.判断该用户有无分类权限
		String typeids = null;
		for (String id : farmDocTypeManagerImpl.getUserReadTypeIds(user)) {
			if (typeids == null) {
				typeids = "'" + id + "'";
			} else {
				typeids = typeids + "," + "'" + id + "'";
			}
		}
		if (user != null) {
			query.addSqlRule("and (g.READPOP='0' or (g.READPOP!='0' and g.id in (" + typeids + ")))");
		} else {
			query.addSqlRule("and g.READPOP='0'");
		}
		query.addSqlRule(
				" and a.DOCGROUPID is not null AND ( a.READPOP = '1' OR ( a.READPOP = '2' AND d.JOINCHECK = '0' ) OR ( a.READPOP = '2' AND d.JOINCHECK = '1' AND c.PSTATE = '1' ))");
		query.setPagesize(pagesize);
		try {
			DataResult result = query.search();
			result.runHandle(new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					row.put("IMGURL", farmFileServer.getFileURL((String)row.get("GROUPIMG")));
					row.put("PHOTOURL", farmFileServer.getFileURL((String)row.get("USERIMGID")));
				}
			});
			return result;
		} catch (SQLException e) {
			log.error(e.toString());
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult getGroupUser(String groupid, SearchType isApply, SearchType isAdmin, SearchType isEditor,
			int page, int pagesize) {
		DataQuery query = DataQuery.getInstance(page,
				"A.ID AS GROUPUSERID, b.NAME as name,b.ID as ID,b.IMGID as IMGID,a.PSTATE as PSTATE,a.GROUPID as GROUPID,a.LEADIS as LEADIS,a.EDITIS as EDITIS,a.APPLYNOTE as APPLYNOTE",
				"FARM_DOCGROUP_USER a left join ALONE_AUTH_USER b on a.userid=b.id");
		query.setPagesize(pagesize);

		if (isApply.equals(SearchType.yes)) {
			query.addRule(new DBRule("PSTATE", "3", "="));
		}
		if (isApply.equals(SearchType.no)) {
			query.addRule(new DBRule("PSTATE", "1", "="));
		}
		if (isAdmin.equals(SearchType.no)) {
			query.addRule(new DBRule("LEADIS", "0", "="));
		}
		if (isAdmin.equals(SearchType.yes)) {
			query.addRule(new DBRule("LEADIS", "1", "="));
		}
		if (isEditor.equals(SearchType.yes)) {
			query.addRule(new DBRule("EDITIS", "1", "="));
		}
		if (isEditor.equals(SearchType.no)) {
			query.addRule(new DBRule("EDITIS", "0", "="));
		}
		query.addRule(new DBRule("GROUPID", groupid, "="));
		query.addSort(new DBSort("LEADIS", "desc"));
		DataResult result = null;
		try {
			result = query.search();
		} catch (SQLException e) {
			log.error(e);
			return DataResult.getInstance();
		}
		return result;
	}

	@Override
	@Transactional
	public boolean isAuditing(String groupId, String userid) {
		FarmDocgroupUser du = getFarmDocgroupUser(groupId, userid);
		if (du == null) {
			return false;
		}
		if (du.getPstate().equals("3")) {
			return true;
		}
		return false;
	}

	@Override
	public DataResult getGroupsByUser(String userid, int pagesize, Integer pagenum) {
		DataQuery query = DataQuery.init(new DataQuery(),
				"FARM_DOCGROUP as a left join FARM_DOCGROUP_USER as b on a.id=b.GROUPID",
				"a.ID AS ID, a.CTIME AS CTIME, a.ETIME AS ETIME, a.CUSERNAME AS CUSERNAME, a.CUSER AS CUSER, a.EUSERNAME AS EUSERNAME, a.EUSER AS EUSER, a.PSTATE AS PSTATE, a.PCONTENT AS PCONTENT, a.GROUPNAME AS GROUPNAME, a.GROUPNOTE AS GROUPNOTE, a.GROUPTAG AS GROUPTAG, a.GROUPIMG AS GROUPIMG, a.JOINCHECK AS JOINCHECK, a.USERNUM AS USERNUM, a.HOMEDOCID AS HOMEDOCID, b.USERID AS USERID");
		query.addSort(new DBSort("a.USERNUM", "desc"));
		query.addRule(new DBRule("b.USERID", userid, "="));
		query.setCurrentPage(pagenum);
		query.setDistinct(true);
		query.setPagesize(pagesize);
		try {
			DataResult result = query.search();
			result.runHandle(new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					row.put("IMGURL", farmFileServer.getFileURL(row.get("GROUPIMG").toString()));
					if (row.get("USERID") != null && !row.get("USERID").toString().isEmpty()) {
						row.put("USERJOIN", "1");
					} else {
						row.put("USERJOIN", "0");
					}
				}
			});
			return result;
		} catch (SQLException e) {
			log.error(e.toString());
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult getGroups(int pagesize, Integer pagenum) {
		DataQuery query = DataQuery.init(new DataQuery(), "FARM_DOCGROUP as a ",
				"a.ID AS ID, a.CTIME AS CTIME, a.ETIME AS ETIME, a.CUSERNAME AS CUSERNAME, a.CUSER AS CUSER, a.EUSERNAME AS EUSERNAME, a.EUSER AS EUSER, a.PSTATE AS PSTATE, a.PCONTENT AS PCONTENT, a.GROUPNAME AS GROUPNAME, a.GROUPNOTE AS GROUPNOTE, a.GROUPTAG AS GROUPTAG, a.GROUPIMG AS GROUPIMG, a.JOINCHECK AS JOINCHECK, a.USERNUM AS USERNUM, a.HOMEDOCID AS HOMEDOCID");
		query.addSort(new DBSort("a.USERNUM", "desc"));
		query.setCurrentPage(pagenum);
		query.setDistinct(true);
		query.setPagesize(pagesize);
		try {
			DataResult result = query.search();
			result.runHandle(new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					row.put("IMGURL", farmFileServer.getFileURL(row.get("GROUPIMG").toString()));
					if (row.get("USERID") != null && !row.get("USERID").toString().isEmpty()) {
						row.put("USERJOIN", "1");
					} else {
						row.put("USERJOIN", "0");
					}
				}
			});
			return result;
		} catch (SQLException e) {
			log.error(e.toString());
			return DataResult.getInstance();
		}
	}

	@Override
	@Transactional
	public List<FarmDocgroup> getAllGroup() {
		return farmDocgroupDao.selectEntitys(null);
	}

}
