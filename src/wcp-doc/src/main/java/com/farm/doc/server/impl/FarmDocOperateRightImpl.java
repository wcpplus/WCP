package com.farm.doc.server.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.authority.domain.Organization;
import com.farm.authority.service.OrganizationServiceInter;
import com.farm.authority.service.UserServiceInter;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.FarmDocDaoInter;
import com.farm.doc.dao.FarmRfDoctypeDaoInter;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.plus.domain.Doctypepop;

@Service
public class FarmDocOperateRightImpl implements FarmDocOperateRightInter {
	@Resource
	private FarmDocDaoInter farmDocDao;
	@Resource
	private FarmDocgroupManagerInter farmdocgroupServer;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private FarmRfDoctypeDaoInter farmRfDoctypeDao;
	@Resource
	private OrganizationServiceInter organizationServiceImpl;
	@Resource
	UserServiceInter userServiceImpl;

	@Override
	@Transactional
	public boolean isDel(LoginUser user, Doc doc) {
		FarmDoctype type = farmRfDoctypeDao.getDocType(doc.getId());
		if (type != null && !type.getAuditpop().equals("0")) {
			// 分类需要审核
			return false;
		}
		if (user == null) {
			return false;
		}
		// 非小组权限只允许本人删除
		if (!doc.getWritepop().equals("2") && doc.getCuser().equals(user.getId())) {
			return true;
		}
		// 如果编辑权限是小组的，只有管理员可以删除
		if (doc.getWritepop().equals("2")) {
			if (doc.getDocgroupid() == null) {
				return false;
			}
			// 本人可以删除
			// if(doc.getCuser().equals(user.getId())){
			// return true;
			// }
			if (farmdocgroupServer.isAdminForGroup(user.getId(), doc.getDocgroupid())) {
				return true;
			}
		}
		return false;
	}

	private boolean isRead(LoginUser user, Doc doc, FarmDoctype type) {
		// 1分类
		if (doc.getReadpop().equals("1")) {
			if (type == null) {
				type = farmRfDoctypeDao.getDocType(doc.getId());
			}
			if (type == null) {
				//如果该分类不存在则运行访问
				return true;
			}
			if (type.getReadpop().equals("0")) {
				// 分类公开
				return true;
			} else {
				// 分类有权限
				if (user != null) {
					Organization org = userServiceImpl.getUserOrganization(user.getId());
					List<Doctypepop> pops = farmDocTypeManagerImpl.getTypePops(type.getId());
					for (Doctypepop node : pops) {
						if (node.getFuntype().equals("1")) {
							if (node.getOid().equals(user.getId())) {
								// 该用户又权限访问分类
								return true;
							}
							if (org.getTreecode().indexOf(node.getOid()) >= 0) {
								// 该组织机构又权限访问分类
								return true;
							}
						}
					}
					return false;
				} else {
					return false;
				}
			}
		}
		// 0本人
		if (doc.getReadpop().equals("0")) {
			// 权限是本人的时候,允许本人阅读
			if (user != null && doc.getCuser().equals(user.getId())) {
				return true;
			}
			return false;
		}
		// 2小组
		if (doc.getReadpop().equals("2")) {
			// 当阅读权限被指定到小组，且小组是公开的时候可以阅读
			if (doc.getDocgroupid() != null && !farmdocgroupServer.isJoinCheck(doc.getDocgroupid())) {
				return true;
			}
			if (user != null && doc.getDocgroupid() != null
					&& farmdocgroupServer.isJoinGroupByUser(doc.getDocgroupid(), user.getId())) {
				return true;
			}
		}
		// 3禁用
		if (doc.getReadpop().equals("3")) {
			return false;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean isRead(LoginUser user, Doc doc) {
		return isRead(user, doc, null);
	}

	@Override
	@Transactional
	public boolean isAllUserRead(Doc doc) {
		return isRead(null, doc);
	}

	@Override
	@Transactional
	public boolean isAllUserRead(Doc doc, FarmDoctype type) {
		return isRead(null, doc, type);
	}

	@Override
	@Transactional
	public boolean isWrite(LoginUser user, Doc doc) {
		// 1分类
		if (doc.getWritepop().equals("1")) {
			FarmDoctype type = farmRfDoctypeDao.getDocType(doc.getId());
			if (type.getWritepop().equals("0")) {
				// 分类公开
				return true;
			} else {
				// 分类有权限
				if (user != null) {
					// 如果是资源则不能被修改
					if (doc.getDomtype().equals("5")) {
						return false;
					}
					Organization org = userServiceImpl.getUserOrganization(user.getId());
					List<Doctypepop> pops = farmDocTypeManagerImpl.getTypePops(type.getId());
					for (Doctypepop node : pops) {
						if (node.getFuntype().equals("2")) {
							if (node.getOid().equals(user.getId())) {
								// 该用户又权限访问分类
								return true;
							}
							if (org.getTreecode().indexOf(node.getOid()) >= 0) {
								// 该组织机构又权限访问分类
								return true;
							}
						}
					}
					return false;
				} else {
					return false;
				}
			}
		}
		// 0本人
		if (doc.getWritepop().equals("0")) {
			// 权限是本人的时候,允许本人阅读
			if (user != null && doc.getCuser().equals(user.getId())) {
				return true;
			}
			return false;
		}
		// 2小组
		if (doc.getWritepop().equals("2")) {
			// 当阅读权限被指定到小组，且小组是公开的时候可以阅读
			if (doc.getDocgroupid() != null && !farmdocgroupServer.isJoinCheck(doc.getDocgroupid())) {
				return true;
			}
			if (user != null && doc.getDocgroupid() != null
					&& farmdocgroupServer.isJoinGroupByUser(doc.getDocgroupid(), user.getId())) {
				return true;
			}
		}
		// 3禁用
		if (doc.getWritepop().equals("3")) {
			return false;
		}
		return false;
	}

	@Override
	@Transactional
	public Doc editDocRight(String docId, POP_TYPE pop_type_read, POP_TYPE pop_type_write, LoginUser currentUser) {
		Doc entity2 = farmDocDao.getEntity(docId);
		entity2.setWritepop(pop_type_write.getValue());
		entity2.setReadpop(pop_type_read.getValue());
		entity2.setEuser(currentUser.getId());
		entity2.setEusername(currentUser.getName());
		entity2.setEtime(TimeTool.getTimeDate14());
		farmDocDao.editEntity(entity2);
		return entity2;
	}

	@Override
	@Transactional
	public void flyDoc(String id, LoginUser currentUser) {
		Doc doc = farmDocDao.getEntity(id);
		if (isDel(currentUser, doc)) {
			flyDoc(doc);
		} else {
			throw new RuntimeException("您没有此权限");
		}
	}

	@Override
	@Transactional
	public void flyDoc(Doc doc) {
		doc.setDocgroupid(null);
		doc.setReadpop(POP_TYPE.PUB.getValue());
		doc.setWritepop(POP_TYPE.PUB.getValue());
		farmDocDao.editEntity(doc);
	}

	public FarmDocDaoInter getFarmDocDao() {
		return farmDocDao;
	}

	public void setFarmDocDao(FarmDocDaoInter farmDocDao) {
		this.farmDocDao = farmDocDao;
	}

	public FarmDocgroupManagerInter getFarmdocgroupServer() {
		return farmdocgroupServer;
	}

	public void setFarmdocgroupServer(FarmDocgroupManagerInter farmdocgroupServer) {
		this.farmdocgroupServer = farmdocgroupServer;
	}

	@Override
	@Transactional
	public boolean isAudit(LoginUser user, Doc doc) {
		// 1分类
		FarmDoctype type = farmRfDoctypeDao.getDocType(doc.getId());
		if (type.getAuditpop().equals("0")) {
			// 不需要审核
			return false;
		} else {
			// 分类有权限
			if (user != null) {
				Organization org = userServiceImpl.getUserOrganization(user.getId());
				List<Doctypepop> pops = farmDocTypeManagerImpl.getTypePops(type.getId());
				for (Doctypepop node : pops) {
					if (node.getFuntype().equals("3")) {
						if (node.getOid().equals(user.getId())) {
							// 该用户又权限访问分类
							return true;
						}
						if (org.getTreecode().indexOf(node.getOid()) >= 0) {
							// 该组织机构又权限访问分类
							return true;
						}
					}
				}
				return false;
			} else {
				return false;
			}
		}
	}

}
