package com.farm.doc.server.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.authority.domain.Organization;
import com.farm.authority.service.OrganizationServiceInter;
import com.farm.authority.service.UserServiceInter;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.query.DataQuery.CACHE_UNIT;
import com.farm.core.sql.result.DataResult;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.FarmDocDaoInter;
import com.farm.doc.dao.FarmDocenjoyDaoInter;
import com.farm.doc.dao.FarmDocfileDaoInter;
import com.farm.doc.dao.FarmDocgroupDaoInter;
import com.farm.doc.dao.FarmDocmessageDaoInter;
import com.farm.doc.dao.FarmDocruninfoDaoInter;
import com.farm.doc.dao.FarmDocruninfoDetailDaoInter;
import com.farm.doc.dao.FarmDoctextDaoInter;
import com.farm.doc.dao.FarmDoctypeDaoInter;
import com.farm.doc.dao.FarmRfDoctextfileDaoInter;
import com.farm.doc.dao.FarmRfDoctypeDaoInter;
import com.farm.doc.dao.FarmtopDaoInter;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.plus.FarmTypePopServerInter;
import com.farm.doc.server.plus.domain.Doctypepop;
import com.farm.parameter.FarmParameterService;

/**
 * 文档分类管理
 * 
 * @author MAC_wd
 */
@Service
public class FarmDocTypeManagerImpl implements FarmDocTypeInter {
	@Resource
	private FarmDocDaoInter farmDocDao;
	@Resource
	private FarmDocfileDaoInter farmDocfileDao;
	@Resource
	private FarmDoctextDaoInter farmDoctextDao;
	@Resource
	private FarmRfDoctextfileDaoInter farmRfDoctextfileDao;
	@Resource
	private FarmRfDoctypeDaoInter farmRfDoctypeDao;
	@Resource
	private FarmDoctypeDaoInter farmDoctypeDao;
	@Resource
	private FarmDocmessageDaoInter farmDocmessageDao;
	@Resource
	private FarmDocruninfoDaoInter farmDocruninfoDao;
	@Resource
	private FarmDocenjoyDaoInter farmDocenjoyDao;
	@Resource
	private FarmDocOperateRightInter farmDocOperate;
	@Resource
	private FarmDocgroupDaoInter farmDocgroupDao;
	@Resource
	private FarmDocruninfoDetailDaoInter farmDocruninfoDetailDao;
	@Resource
	private FarmFileManagerInter farmFileServer;
	@Resource
	private FarmtopDaoInter farmtopDaoImpl;
	@Resource
	private OrganizationServiceInter organizationServiceImpl;
	@Resource
	private FarmTypePopServerInter farmTypePopServerImpl;
	@Resource
	private UserServiceInter userServiceImpl;
	private static final Logger log = Logger.getLogger(FarmDocTypeManagerImpl.class);

	@Transactional
	public FarmDoctype insertType(FarmDoctype entity, LoginUser user) {
		entity.setCtime(TimeTool.getTimeDate14());
		entity.setEtime(TimeTool.getTimeDate14());
		entity.setCuser(user.getId());
		entity.setEuser(user.getId());
		entity.setCusername(user.getName());
		entity.setEusername(user.getName());
		entity.setReadpop("0");
		entity.setWritepop("0");
		entity.setAuditpop("0");
		if (entity.getParentid() == null || entity.getParentid().trim().length() <= 0) {
			entity.setParentid("NONE");
		}
		entity.setTreecode("NONE");
		entity = farmDoctypeDao.insertEntity(entity);
		if (entity.getParentid().equals("NONE")) {
			entity.setTreecode(entity.getId());
		} else {
			entity.setTreecode(farmDoctypeDao.getEntity(entity.getParentid()).getTreecode() + entity.getId());
		}
		return farmDoctypeDao.insertEntity(entity);
	}

	@Transactional
	public FarmDoctype editType(FarmDoctype entity, LoginUser user) {
		FarmDoctype entity2 = farmDoctypeDao.getEntity(entity.getId());
		entity2.setEtime(TimeTool.getTimeDate14());
		entity2.setEuser(user.getId());
		entity2.setEusername(user.getName());
		entity2.setName(entity.getName());
		entity2.setTypemod(entity.getTypemod());
		entity2.setContentmod(entity.getContentmod());
		entity2.setSort(entity.getSort());
		entity2.setTags(entity.getTags());
		entity2.setType(entity.getType());
		entity2.setMetatitle(entity.getMetatitle());
		entity2.setMetakey(entity.getMetakey());
		entity2.setMetacontent(entity.getMetacontent());
		entity2.setLinkurl(entity.getLinkurl());
		entity2.setPcontent(entity.getPcontent());
		entity2.setPstate(entity.getPstate());
		farmDoctypeDao.editEntity(entity2);
		return entity2;
	}

	@Transactional
	public void deleteType(String typeId, LoginUser user) {
		// 删除分类中间表
		List<DBRule> rulesDelType = new ArrayList<DBRule>();
		rulesDelType.add(new DBRule("TYPEID", typeId, "="));
		farmRfDoctypeDao.deleteEntitys(rulesDelType);
		farmTypePopServerImpl.delTypePop(typeId);
		farmDoctypeDao.deleteEntity(farmDoctypeDao.getEntity(typeId));
	}

	@Transactional
	public FarmDoctype getType(String id) {
		if (id == null) {
			return null;
		}
		return farmDoctypeDao.getEntity(id);
	}

	@Override
	@Transactional
	public List<FarmDoctype> getTypeAllParent(String typeid) {
		String id = typeid;
		List<FarmDoctype> types = new ArrayList<FarmDoctype>();
		while (id != null) {
			FarmDoctype centity = farmDoctypeDao.getEntity(id);
			if (centity == null || centity.getParentid() == null || centity.getParentid().trim().length() <= 0) {
				id = null;
			} else {
				id = centity.getParentid();

			}
			if (centity != null) {
				types.add(centity);
			}
		}
		Collections.reverse(types);
		return types;
	}

	@Override
	@Transactional
	public List<TypeBrief> getTypeInfos(LoginUser user, String parentId) {
		DataQuery query = null;
		query = DataQuery.init(query,
				"(SELECT a.NAME as NAME,a.readpop as READPOP,a.WRITEPOP as WRITEPOP, a.ID as ID, a.PARENTID as PARENTID, (SELECT COUNT(B1.ID) FROM FARM_DOC B1 LEFT JOIN FARM_RF_DOCTYPE B2 ON B1.ID = B2.DOCID LEFT JOIN FARM_DOCTYPE B3 ON B3.ID = B2.TYPEID WHERE B1.STATE='1' and B3.TREECODE  LIKE CONCAT(A.TREECODE,'%') AND B1.STATE='1') AS NUM FROM farm_doctype AS a LEFT JOIN farm_doctype AS b ON b.ID = a.PARENTID WHERE 1 = 1 AND (a.TYPE = '1' OR a.TYPE = '3') AND a.PSTATE = '1' and (a.PARENTID='"
						+ parentId + "' or b.PARENTID='" + parentId + "') ORDER BY a.SORT ASC) AS e",
				"NAME,ID,PARENTID,NUM,READPOP,WRITEPOP");
		query.setDistinct(true);
		{
			// 加载分类权限
			String typeids = null;
			for (String id : getUserReadTypeIds(user)) {
				if (typeids == null) {
					typeids = "'" + id + "'";
				} else {
					typeids = typeids + "," + "'" + id + "'";
				}
			}
			if (user != null) {
				query.addSqlRule("and (READPOP='0' or (READPOP!='0' and ID in (" + typeids + ")))");
			} else {
				query.addSqlRule("and READPOP='0'");
			}
		}
		query.setPagesize(1000);
		query.setNoCount();
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		try {
			return query.search().getObjectList(TypeBrief.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<TypeBrief>();
		}
	}

	@Override
	@Transactional
	public DataResult getTypeDocs(LoginUser user, String typeid, int pagesize, int currentPage) {
		String userid = (user == null ? "none" : user.getId());
		String typeids = null;
		for (String id : getUserReadTypeIds(user)) {
			if (typeids == null) {
				typeids = "'" + id + "'";
			} else {
				typeids = typeids + "," + "'" + id + "'";
			}
		}
		FarmDoctype type = getType(typeid);
		DataQuery query = DataQuery.init(new DataQuery(),
				"farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID LEFT JOIN ALONE_AUTH_USER e ON e.ID = a.CUSER",
				"a.ID as DOCID,a.DOMTYPE as DOMTYPE,a.TITLE AS title,a.DOCDESCRIBE AS DOCDESCRIBE,a.AUTHOR AS AUTHOR,a.PUBTIME AS PUBTIME,a.TAGKEY AS TAGKEY ,a.IMGID AS IMGID,b.VISITNUM AS VISITNUM,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,b.EVALUATE as EVALUATE,b.ANSWERINGNUM as ANSWERINGNUM,d.id as TYPEID,d.NAME AS TYPENAME, e.ID as USERID, e.NAME as USERNAME, e.IMGID as USERIMGID");
		query.addSort(new DBSort("a.etime", "desc"));
		query.setCurrentPage(currentPage);
		query.addRule(new DBRule("a.STATE", "1", "="));
		query.addRule(new DBRule("DOMTYPE", "4", "!="));
		if (type != null) {
			query.addRule(new DBRule("d.TREECODE", type.getTreecode(), "like-"));
		}
		if (user != null) {
			query.addSqlRule("and (d.READPOP='0' or (d.READPOP!='0' and d.id in (" + typeids + ")))");
		} else {
			query.addSqlRule("and d.READPOP='0'");
		}
		// 文章三种情况判断
		// 1.文章阅读权限为公共
		// 2.文章的创建者为当前登录用户
		// 3.文章的阅读权限为小组，并且当前登陆用户为组内成员.(使用子查询处理)
		query.addSqlRule("and (a.READPOP='1' or a.CUSER='" + userid
				+ "' OR (a.READPOP = '2' AND 0 < (SELECT count(e.ID) FROM farm_docgroup_user e WHERE e.PSTATE = 1 AND e.GROUPID = a.DOCGROUPID AND e.CUSER = '"
				+ userid + "')))");
		query.setPagesize(pagesize);
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		DataResult docs = null;
		try {
			docs = query.search();
		} catch (SQLException e) {
			log.error(e.toString());
			return DataResult.getInstance();
		}
		for (Map<String, Object> map : docs.getResultList()) {
			if (map.get("USERIMGID") != null && !map.get("USERIMGID").toString().isEmpty()) {
				map.put("PHOTOURL", farmFileServer.getFileURL(map.get("USERIMGID").toString()));
			} else {
				map.put("PHOTOURL", farmFileServer.getFileURL("NONE"));
			}
		}
		return docs;
	}

	@Override
	public List<Doctypepop> getTypePops(String typeid) {
		List<Doctypepop> doctypepops = new ArrayList<>();
		for (FarmDoctype type : getTypeAllParent(typeid)) {
			farmTypePopServerImpl.getTypePopsHandle(doctypepops, type.getReadpop(), type.getWritepop(),
					type.getAuditpop(), type.getId());
		}
		return doctypepops;
	}

	@Override
	@Transactional
	public List<FarmDoctype> getAllSubNode(String typeid) {
		DataQuery query = DataQuery.getInstance(1,
				"NAME,TYPEMOD,CONTENTMOD,SORT,TYPE,METATITLE,METAKEY,METACONTENT,LINKURL,ID,TAGS,CTIME,ETIME,CUSERNAME,CUSER,EUSERNAME,EUSER,PCONTENT,PSTATE,PARENTID,TREECODE,READPOP,WRITEPOP,AUDITPOP",
				"FARM_DOCTYPE");
		query.addRule(new DBRule("TREECODE", farmDoctypeDao.getEntity(typeid).getTreecode(), "like-"));
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		try {
			return query.search().getObjectList(FarmDoctype.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public List<TypeBrief> getPubTypes() {
		DataQuery query = DataQuery.init(new DataQuery(),
				"(SELECT a.NAME as NAME, a.ID as ID, a.PARENTID as PARENTID, (SELECT COUNT(B1.ID) FROM FARM_DOC B1 LEFT JOIN FARM_RF_DOCTYPE B2 ON B1.ID = B2.DOCID LEFT JOIN FARM_DOCTYPE B3 ON B3.ID = B2.TYPEID WHERE  B1.STATE='1' and  B3.TREECODE  LIKE CONCAT(A.TREECODE,'%') AND B1.STATE='1') AS NUM FROM farm_doctype AS a WHERE 1 = 1 AND (TYPE = '1' OR TYPE = '3') AND PSTATE = '1' ORDER BY SORT ASC) AS e",
				"NAME,ID,PARENTID,NUM");
		query.setPagesize(1000);
		query.setNoCount();
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		try {
			return query.search().getObjectList(TypeBrief.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<TypeBrief>();
		}
	}

	@Override
	@Transactional
	public List<TypeBrief> getTypes() {
		DataQuery query = DataQuery.init(new DataQuery(), "FARM_DOCTYPE",
				"ID,NAME,TYPE,PARENTID,READPOP,WRITEPOP,AUDITPOP");
		query.setPagesize(1000);
		query.setNoCount();
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.immediately")),
				CACHE_UNIT.second);
		try {
			return query.search().getObjectList(TypeBrief.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<TypeBrief>();
		}
	}

	@Override
	public List<TypeBrief> getPopTypesForReadDoc(LoginUser user) {
		DataQuery query = DataQuery.init(new DataQuery(),
				"(SELECT a.NAME as NAME,a.SORT as SORT, a.ID as ID, a.READPOP as READPOP,a.WRITEPOP AS WRITEPOP, a.AUDITPOP AS AUDITPOP, a.PARENTID AS PARENTID, (SELECT COUNT(B1.ID) FROM FARM_DOC B1 LEFT JOIN FARM_RF_DOCTYPE B2 ON B1.ID = B2.DOCID LEFT JOIN FARM_DOCTYPE B3 ON B3.ID = B2.TYPEID WHERE  B1.STATE='1' and  B3.TREECODE  LIKE CONCAT(A.TREECODE,'%') AND B1.STATE='1') AS NUM,f.oid as OID,f.FUNTYPE as FUNTYPE FROM farm_doctype AS a left join FARM_DOCTYPE_POP as f on f.TYPEID=a.ID WHERE 1 = 1 AND (TYPE = '1' OR TYPE = '3') AND PSTATE = '1' ) AS e",
				"NAME,ID,PARENTID,NUM,OID,READPOP,AUDITPOP,WRITEPOP,FUNTYPE,SORT");
		query.setPagesize(1000);
		query.setNoCount();
		if (user != null) {
			query.addSqlRule("and ('1'!='" + user.getId() + "' )");
		}
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		query.addSort(new DBSort("SORT", "ASC"));
		try {
			DataResult result = query.search();
			// 1浏览、2编辑、3审核
			return getTypeLimit(result, user, "1");
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<TypeBrief>();
		}
	}

	@Override
	public List<TypeBrief> getTypesForWriteDoc(LoginUser user) {
		DataQuery query = DataQuery.init(new DataQuery(),
				"(SELECT a.NAME as NAME, a.ID as ID,a.TYPE as type, a.READPOP as READPOP,a.WRITEPOP AS WRITEPOP, a.AUDITPOP AS AUDITPOP, a.PARENTID AS PARENTID, (SELECT COUNT(B1.ID) FROM FARM_DOC B1 LEFT JOIN FARM_RF_DOCTYPE B2 ON B1.ID = B2.DOCID LEFT JOIN FARM_DOCTYPE B3 ON B3.ID = B2.TYPEID WHERE B3.TREECODE  LIKE CONCAT(A.TREECODE,'%') AND B1.STATE='1') AS NUM,f.oid as OID,f.FUNTYPE as FUNTYPE FROM farm_doctype AS a left join FARM_DOCTYPE_POP as f on f.TYPEID=a.ID WHERE   (TYPE = '1' OR TYPE = '3') AND PSTATE = '1' ORDER BY SORT ASC) AS e",
				"NAME,ID,PARENTID,NUM,OID,READPOP,TYPE,AUDITPOP,WRITEPOP,FUNTYPE");
		query.setPagesize(1000);
		query.setNoCount();
		query.setDistinct(true);
		if (user != null) {
			query.addSqlRule("and ('1'!='" + user.getId() + "' )");
		}
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		try {
			DataResult result = query.search();
			// 1浏览、2编辑、3审核
			return getTypeLimit(result, user, "2");
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<TypeBrief>();
		}
	}

	/**
	 * 配合getPubTypesForWriteDoc、getPubTypesForReadDoc方法完成分类权限查询的方法
	 * 
	 * @param result
	 *            中间结果集合
	 * @param user
	 *            当前用户
	 * @param type
	 *            1浏览、2编辑、3审核
	 * @return
	 */
	private List<TypeBrief> getTypeLimit(DataResult result, LoginUser user, String type) {
		Organization org = null;
		if (user != null) {
			org = userServiceImpl.getUserOrganization(user.getId());
		}
		// 用来过滤重复分类的
		Set<String> idset = new HashSet<String>();
		// 做权限过滤
		List<Map<String, Object>> newResult = new ArrayList<>();
		{
			for (Map<String, Object> node : result.getResultList()) {
				if (idset.contains((String) node.get("ID"))) {
					// 过滤重复分类
					continue;
				}
				// READPOP,AUDITPOP,WRITEPOP
				String marktitle = null;
				if (type.equals("1")) {
					marktitle = "READPOP";
				}
				if (type.equals("2")) {
					marktitle = "WRITEPOP";
				}
				if (node.get(marktitle).equals("0") || node.get(marktitle).equals("2")) {
					// 无权限任何人都可以访问
					newResult.add(node);
					idset.add((String) node.get("ID"));
					continue;
				}
				if (node.get(marktitle).equals("1") && user != null) {
					// 如果有权限控制判断当前用户是否可以访问
					if (node.get("OID").equals(user.getId()) && node.get("FUNTYPE").equals(type)) {
						// 该用户有权限
						newResult.add(node);
						idset.add((String) node.get("ID"));
						continue;
					}
					if (org != null && org.getTreecode().indexOf((String) node.get("OID")) >= 0
							&& node.get("FUNTYPE").equals(type)) {
						// 组织机构有权限
						newResult.add(node);
						idset.add((String) node.get("ID"));
						continue;
					}
				}
			}
		}
		result.setResultList(newResult);
		return result.getObjectList(TypeBrief.class);
	}

	/*
	 * (non-Javadoc)获得用户所有（非阅读开放权限的）分类的ID（阅读）
	 * 
	 * @see
	 * com.farm.doc.server.FarmDocTypeInter#getUserReadTypeIds(com.farm.core.
	 * auth.domain.LoginUser)
	 */
	@Override
	@Transactional
	public List<String> getUserReadTypeIds(LoginUser user) {
		if (user == null) {
			return new ArrayList<>();
		}
		List<String> ids = new ArrayList<>();
		// 所有用户分类
		Set<String> idset = new HashSet<String>();
		// 获得所有用户分配到的分类权限（不包括公开的分类）
		{
			Organization org = null;
			if (user != null) {
				org = userServiceImpl.getUserOrganization(user.getId());
			}
			DataQuery query = DataQuery.getInstance("1", "TYPEID,ID", "FARM_DOCTYPE_POP");
			query.setPagesize(1000);
			query.setNoCount();
			query.addSqlRule("and FUNTYPE='1' ");
			query.addSqlRule(
					"and (OID ='" + user.getId() + "' or OID ='" + (org == null ? "none" : org.getId()) + "')");
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
					CACHE_UNIT.second);
			try {
				for (Map<String, Object> node : query.search().getResultList()) {
					List<FarmDoctype> list = getAllSubNode((String) node.get("TYPEID"));
					for (FarmDoctype one : list) {
						idset.add(one.getId());
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
		for (String typeid : idset) {
			ids.add(typeid);
		}
		return ids;
	}

	@Override
	@Transactional
	public List<String> getUserAuditTypeIds(String userid) {
		if (userid == null) {
			return new ArrayList<>();
		}
		List<String> ids = new ArrayList<>();
		// 所有用户分类
		Set<String> idset = new HashSet<String>();
		// 获得所有用户分配到的分类权限（不包括公开的分类）
		{
			Organization org = null;
			String orgids = null;
			if (userid != null) {
				org = userServiceImpl.getUserOrganization(userid);
				if (org != null) {
					for (Organization node : organizationServiceImpl.getParentOrgs(org.getId())) {
						if (orgids == null) {
							orgids = "'" + node.getId() + "'";
						} else {
							orgids = orgids + ",'" + node.getId() + "'";
						}
					}
				}
			}
			DataQuery query = DataQuery.getInstance("1", "TYPEID,ID", "FARM_DOCTYPE_POP");
			query.addSqlRule("and FUNTYPE='3' ");
			query.addSqlRule(
					"and (OID ='" + userid + "' " + (orgids == null ? "" : "or OID in (" + orgids + ")") + ")");
			try {
				for (Map<String, Object> node : query.search().getResultList()) {
					List<FarmDoctype> list = getAllSubNode((String) node.get("TYPEID"));
					for (FarmDoctype one : list) {
						idset.add(one.getId());
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
		for (String typeid : idset) {
			ids.add(typeid);
		}
		return ids;
	}

	@Override
	public List<String> getPopTypeAudtUserIds(String typeid) {
		List<String> ids = new ArrayList<>();
		// 找到分类的权限节点
		for (Doctypepop pop : getTypePops(typeid)) {
			// 处理审核权限
			if (pop.getFuntype().equals("3")) {
				// 查询所有审核人
				if (pop.getPoptype().equals("1")) {
					ids.add(pop.getOid());
				}
				// 查询所有审核机构
				if (pop.getPoptype().equals("2")) {
					// 将机构转换为人
					List<String> users = organizationServiceImpl.getOrgUsers(pop.getOid());
					for (String userid : users) {
						ids.add(userid);
					}
				}
			}
		}
		return ids;
	}

	@Override
	@Transactional
	public List<String> getTypesForUserRead(LoginUser user) {
		// 获得所有无权限分类
		List<Map<String, Object>> pubTypes = null;
		List<String> types = new ArrayList<String>();
		DataQuery query = DataQuery.getInstance(1, "ID,NAME", "FARM_DOCTYPE").setPagesize(1000);
		query.addRule(new DBRule("READPOP", "0", "="));
		query.setNoCount();
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
				CACHE_UNIT.second);
		try {
			pubTypes = query.search().getResultList();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		for (Map<String, Object> node : pubTypes) {
			types.add((String) node.get("ID"));
		}
		if (user != null) {
			// 获得用户的阅读权限分类
			List<String> userTypes = getUserReadTypeIds(user);
			if (userTypes != null) {
				types.addAll(userTypes);
			}
		}
		return types;
	}
}
