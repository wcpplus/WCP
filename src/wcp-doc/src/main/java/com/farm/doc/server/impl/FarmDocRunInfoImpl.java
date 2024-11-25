package com.farm.doc.server.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.authority.FarmAuthorityService;
import com.farm.authority.domain.User;
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
import com.farm.doc.dao.FarmDocmessageDaoInter;
import com.farm.doc.dao.FarmDocruninfoDaoInter;
import com.farm.doc.dao.FarmDocruninfoDetailDaoInter;
import com.farm.doc.dao.FarmDoctextDaoInter;
import com.farm.doc.dao.FarmDoctypeDaoInter;
import com.farm.doc.dao.FarmRfDoctextfileDaoInter;
import com.farm.doc.dao.FarmRfDoctypeDaoInter;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDocenjoy;
import com.farm.doc.domain.FarmDocruninfo;
import com.farm.doc.domain.FarmDocruninfoDetail;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.DocBrief;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.commons.DocumentConfig;
import com.farm.doc.util.HtmlUtils;
import com.farm.parameter.FarmParameterService;
import com.farm.util.web.FarmFormatUnits;
import com.farm.util.web.FarmproHotnum;
import com.farm.util.web.WebVisitBuff;

/**
 * @author Administrator
 * 
 */
@Service
public class FarmDocRunInfoImpl implements FarmDocRunInfoInter {
	@Resource
	private FarmDocDaoInter farmDocDao;
	@Resource
	private FarmFileManagerInter farmFileServer;
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
	private FarmDocruninfoDetailDaoInter farmDocruninfoDetailDao;
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private UserServiceInter userServiceImpl;
	private final static Logger log = Logger.getLogger(FarmDocRunInfoImpl.class);

	@Override
	@Transactional
	public void visitDoc(String docId, LoginUser user, String ip) {
		WebVisitBuff vbuff = WebVisitBuff.getInstance("KNOW", 1000);
		String userId = "noLogin";
		if (user != null) {
			userId = user.getId();
		}
		if (vbuff.canVisite(docId + ip + userId)) {
			DocEntire doc = new DocEntire(farmDocDao.getEntity(docId));
			doc.setRuninfo(farmDocruninfoDao.getEntity(doc.getDoc().getRuninfoid()));
			doc.getRuninfo().setVisitnum(doc.getRuninfo().getVisitnum() + 1);
			doc.getRuninfo().setHotnum(countHotNum(doc));
			doc.getRuninfo().setLastvtime(TimeTool.getTimeDate12());
			farmDocruninfoDao.editEntity(doc.getRuninfo());
		}
	}

	@Override
	public DataQuery createReleaseRankingSimpleQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query,
				"(SELECT A.USERNAME AS USERNAME, A.DOCNUM AS DOCNUM, A.GOODNUM AS GOODNUM, A.BADNUM AS BADNUM,"
						+ "	A.VISITNUM AS VISITNUM, A.PRAISEYES AS PRAISEYES, A.PRAISENO AS PRAISENO,"
						+ "	TRUNCATE(A.GOODNUM / A.DOCNUM * 100, 0) AS GOODRATE " + "FROM ("
						+ "	SELECT MAX(USER.NAME) AS USERNAME, COUNT(*) AS DOCNUM, "
						+ "		SUM(CASE WHEN DOCRUNINFO.EVALUATE > 0 THEN 1 ELSE 0 END) AS GOODNUM,"
						+ "		SUM(CASE WHEN DOCRUNINFO.EVALUATE < 0 THEN 1 ELSE 0 END) AS BADNUM,"
						+ "		SUM(DOCRUNINFO.VISITNUM) AS VISITNUM," + "		SUM(DOCRUNINFO.PRAISEYES) AS PRAISEYES,"
						+ "		SUM(DOCRUNINFO.PRAISENO) AS PRAISENO"
						+ "	FROM (SELECT * FROM FARM_DOC A WHERE A.STATE = '1') DOC "
						+ "	INNER JOIN FARM_DOCRUNINFO DOCRUNINFO ON DOC.RUNINFOID = DOCRUNINFO.ID"
						+ "	LEFT JOIN ALONE_AUTH_USER USER ON DOC.CUSER = USER.ID" + "	GROUP BY DOC.CUSER) A )B",
				"B.USERNAME AS USERNAME, B.DOCNUM AS DOCNUM, B.GOODNUM AS GOODNUM, B.BADNUM AS BADNUM,"
						+ "	B.VISITNUM AS VISITNUM, B.PRAISEYES AS PRAISEYES, B.PRAISENO AS PRAISENO, B.GOODRATE AS GOODRATE");
		return dbQuery;
	}

	/**
	 * 计算文章热度
	 * 
	 * @param doc
	 * @return
	 */
	private int countHotNum(DocEntire doc) {
		int hotnum = FarmproHotnum.getHotnum(doc.getRuninfo().getLastvtime(), doc.getRuninfo().getHotnum(), 1,
				Integer.valueOf(DocumentConfig.getString("config.doc.hot.weight")));
		return hotnum;
	}

	@Override
	@Transactional
	public void enjoyDoc(String userId, String docId) {
		farmDocenjoyDao.insertEntity(new FarmDocenjoy(docId.trim(), userId.trim()));
	}

	@Override
	@Transactional
	public DataQuery getUserEnjoyDoc(String userId) {
		DataQuery query = DataQuery.getInstance("1",
				"TITLE,b.ID AS ID,b.PUBTIME as PUBTIME,b.id as DOCID ,c.VISITNUM as VISITNUM,c.HOTNUM as HOTNUM,c.ANSWERINGNUM as ANSWERINGNUM",
				"FARM_DOCENJOY a left join FARM_DOC b on a.DOCID=b.ID and b.STATE = '1' left join FARM_DOCRUNINFO c on c.id =b.RUNINFOID");
		query.addRule(new DBRule("a.USERID", userId, "="));
		query.setDistinct(true);
		return query;
	}

	@Override
	@Transactional
	public boolean isEnjoyDoc(String userId, String docId) {
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("DOCID", docId, "="));
		list.add(new DBRule("USERID", userId, "="));
		return farmDocenjoyDao.selectEntitys(list).size() > 0;
	}

	@Override
	@Transactional
	public List<DocBrief> getNewKnowList(int pagesize) {
		DataQuery query = DataQuery.getInstance("1",
				"a.ID as docid,a.TITLE AS title,a.CUSER as authorid,a.DOCDESCRIBE AS text,DOMTYPE,a.AUTHOR AS AUTHOR,a.PUBTIME AS pubtime,a.IMGID AS imgid,b.VISITNUM AS VISITNUM,b.answeringnum as answeringnum,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,d.NAME AS typename,e.IMGID AS photoid, e.id as userid, e.name as username",
				"farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID   LEFT JOIN ALONE_AUTH_USER e ON e.ID=a.CUSER");
		query.addRule(new DBRule("a.READPOP", "1", "="));
		query.addRule(new DBRule("a.STATE", "1", "="));
		query.addRule(new DBRule("d.READPOP", "0", "="));
		query.addSort(new DBSort("a.etime", "desc"));
		query.setPagesize(pagesize);
		query.setNoCount();
		List<DocBrief> result = null;
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
				CACHE_UNIT.second);
		try {
			result = query.search().getObjectList(DocBrief.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (DocBrief node : result) {
			node.setPubtime(FarmFormatUnits.getFormateTime(node.getPubtime().toString(), true));
			node.setText(node.getText().toString().replaceAll("<", "").replaceAll(">", ""));
			if (node.getPhotoid() != null) {
				node.setPhotourl(farmFileManagerImpl.getFileURL(node.getPhotoid()));
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void unEnjoyDoc(String userId, String docId) {
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("DOCID", docId, "="));
		list.add(new DBRule("USERID", userId, "="));
		farmDocenjoyDao.deleteEntitys(list);
	}

	@Override
	public List<String> getDocJoinUserIds(String docid) {
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("DOCID", docid, "="));
		List<FarmDocenjoy> enjoyList = farmDocenjoyDao.selectEntitys(list);
		List<String> ids = new ArrayList<String>();
		for (FarmDocenjoy node : enjoyList) {
			ids.add(node.getUserid());
		}
		return ids;
	}

	@Override
	@Transactional
	public void reCountDocHotNum(String docId) {
		DocEntire doc = new DocEntire(farmDocDao.getEntity(docId));
		doc.setRuninfo(farmDocruninfoDao.getEntity(doc.getDoc().getRuninfoid()));
		doc.getRuninfo().setHotnum(countHotNum(doc));
		farmDocruninfoDao.editEntity(doc.getRuninfo());
	}

	@Override
	@Transactional
	public void criticalDoc(String docId, LoginUser user, String IP) {
		evaluateDoc(docId, user, IP, "3");
	}

	/**
	 * 评价一个文档
	 * 
	 * @param docId
	 *            文档id
	 * @param user
	 *            当前用户（可空）
	 * @param IP
	 *            用户IP
	 * @param type
	 *            评价类型（2好评、3差评）
	 * @return 文档评价值(好评减去差评)
	 */
	private void evaluateDoc(String docId, LoginUser user, String IP, String type) {
		Doc doc = farmDocDao.getEntity(docId);
		FarmDocruninfo runinfo = farmDocruninfoDao.getEntity(doc.getRuninfoid());
		// 删除本用户对于该文档的评价(好评、差评)
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("RUNINFOID", runinfo.getId(), "="));
		list.add(new DBRule("VTYPE", "2", "="));
		if (user != null) {
			list.add(new DBRule("CUSER", user.getId(), "="));
		} else {
			list.add(new DBRule("USERIP", IP, "="));
		}
		farmDocruninfoDetailDao.deleteEntitys(list);
		List<DBRule> list2 = new ArrayList<DBRule>();
		list2.add(new DBRule("RUNINFOID", runinfo.getId(), "="));
		list2.add(new DBRule("VTYPE", "3", "="));
		if (user != null) {
			list2.add(new DBRule("CUSER", user.getId(), "="));
		} else {
			list2.add(new DBRule("USERIP", IP, "="));
		}
		farmDocruninfoDetailDao.deleteEntitys(list2);
		// 增加一条好评
		FarmDocruninfoDetail infDetail = new FarmDocruninfoDetail();
		infDetail.setCtime(TimeTool.getTimeDate14());
		if (user != null) {
			infDetail.setCuser(user.getId());
			infDetail.setCusername(user.getName());
		}
		infDetail.setDoctextid(doc.getTextid());
		infDetail.setPstate("1");
		infDetail.setRuninfoid(runinfo.getId());
		infDetail.setUserip(IP);
		// 1访问、2好评、3差评
		infDetail.setVtype(type);
		farmDocruninfoDetailDao.insertEntity(infDetail);
	}

	@Override
	@Transactional
	public void criticalDoc(String docId, String IP) {
		evaluateDoc(docId, null, IP, "3");
	}

	@Override
	@Transactional
	public void praiseDoc(String docId, LoginUser user, String IP) {
		evaluateDoc(docId, user, IP, "2");
	}

	@Override
	@Transactional
	public void praiseDoc(String docId, String IP) {
		evaluateDoc(docId, null, IP, "2");
	}

	@Override
	@Transactional
	public FarmDocruninfo loadRunInfo(String docId) {
		Doc doc = farmDocDao.getEntity(docId);
		FarmDocruninfo runinfo = farmDocruninfoDao.getEntity(doc.getRuninfoid());
		// 更新用量信息（好评、差评、评价）
		List<DBRule> listCount1 = new ArrayList<DBRule>();
		listCount1.add(new DBRule("RUNINFOID", runinfo.getId(), "="));
		listCount1.add(new DBRule("VTYPE", "2", "="));
		int good = farmDocruninfoDetailDao.countEntitys(listCount1);
		List<DBRule> listCount2 = new ArrayList<DBRule>();
		listCount2.add(new DBRule("RUNINFOID", runinfo.getId(), "="));
		listCount2.add(new DBRule("VTYPE", "3", "="));
		int bad = farmDocruninfoDetailDao.countEntitys(listCount2);
		// （2好评、3差评）
		runinfo.setPraiseyes(good);
		runinfo.setPraiseno(bad);
		runinfo.setEvaluate(good - bad);
		farmDocruninfoDao.editEntity(runinfo);
		return runinfo;
	}

	@Override
	@Transactional
	public List<DocBrief> getPubHotDoc(int num) {
		List<DocBrief> list = null;
		DataQuery query = DataQuery.getInstance("1",
				"A.ID AS docid,a.DOCDESCRIBE as text,A.AUTHOR AS author, A.TITLE  AS TITLE, domtype, B.HOTNUM AS hotnum,b.VISITNUM as VISITNUM,b.ANSWERINGNUM as ANSWERINGNUM",
				"farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID = b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID ");
		query.addRule(new DBRule("a.READPOP", "1", "="));
		query.addSort(new DBSort("b.HOTNUM", "desc"));
		query.addRule(new DBRule("d.READPOP", "0", "="));
		query.setNoCount();
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
				CACHE_UNIT.second);
		query.setPagesize(num);
		try {
			list = query.search().getObjectList(DocBrief.class);
			for (DocBrief node : list) {
				node.setText(HtmlUtils.HtmlRemoveTag(node.getText()));
				node.setText(("" + node.getText()).length() > 50 ? ("" + node.getText()).substring(0, 50) + "..."
						: node.getText());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional
	public List<DocBrief> getTypeDocs(String typeid, String userid, int num) {
		FarmDoctype doctype = farmDoctypeDao.getEntity(typeid);
		DataQuery query = new DataQuery();
		query = DataQuery.init(query,
				"farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID",
				"a.ID as DOCID,a.TITLE AS title,a.DOCDESCRIBE AS DOCDESCRIBE,a.AUTHOR AS AUTHOR,a.PUBTIME AS PUBTIME,a.TAGKEY AS TAGKEY ,a.IMGID AS IMGID,b.VISITNUM AS VISITNUM,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,b.EVALUATE as EVALUATE,b.ANSWERINGNUM as ANSWERINGNUM,d.NAME AS TYPENAME");
		query.addSort(new DBSort("a.etime", "desc"));
		query.addRule(new DBRule("a.STATE", "1", "="));
		query.addRule(new DBRule("d.TREECODE", doctype.getTreecode(), "like-"))
				// 文章三种情况判断
				// 1.文章阅读权限为公共
				// 2.文章的创建者为当前登录用户
				// 3.文章的阅读权限为小组，并且当前登陆用户为组内成员.(使用子查询处理)
				.addSqlRule("and (a.READPOP='1' or a.CUSER='" + userid
						+ "' OR (a.READPOP = '2' AND 0 < (SELECT e.ID FROM farm_docgroup_user e WHERE e.PSTATE = 1 AND e.GROUPID = a.DOCGROUPID AND e.CUSER = '"
						+ userid + "')))");
		query.setPagesize(num);
		List<DocBrief> list = null;
		try {
			list = query.search().getObjectList(DocBrief.class);
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<DocBrief>();
		}
		return list;
	}

	@Override
	public List<DocBrief> getPubTopDoc(int num) {
		// 获取前五条置顶文档
		DataResult result = null;
		List<DocBrief> list = null;
		try {
			DataQuery query = new DataQuery();
			query.setPagesize(num);
			query.addSort(new DBSort("TOP.SORT", "asc"));
			query = DataQuery.init(query, "FARM_TOP TOP " + "LEFT JOIN FARM_DOC A ON TOP.DOCID = A.ID and a.STATE='1' "
					+ "LEFT JOIN FARM_RF_DOCTYPE B ON B.DOCID =A.ID " + "LEFT JOIN FARM_DOCTYPE C ON C.ID=B.TYPEID "
					+ "LEFT JOIN FARM_DOCGROUP D ON D.ID=A.DOCGROUPID "
					+ "LEFT JOIN FARM_DOCRUNINFO DOCRUNINFO ON A.RUNINFOID = DOCRUNINFO.ID",
					"TOP.ID AS ID, TOP.SORT AS SORT, A.DOCDESCRIBE AS text,A.WRITEPOP AS WRITEPOP,"
							+ "A.READPOP AS READPOP,A.TITLE AS TITLE,A.AUTHOR AS AUTHOR,A.PUBTIME AS PUBTIME,"
							+ "A.DOMTYPE AS DOMTYPE,A.SHORTTITLE AS SHORTTITLE,A.TAGKEY AS TAGKEY,A.STATE AS STATE,"
							+ "D.GROUPNAME AS GROUPNAME, DOCRUNINFO.VISITNUM AS VISITNUM, DOCRUNINFO.ANSWERINGNUM AS ANSWERINGNUM, "
							+ "A.IMGID AS IMGID, A.ID AS docid,a.cuser as authorid");
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
					CACHE_UNIT.second);
			result = query.search();
			result.runDictionary("1:开放,0:禁用,2:待审核", "STATE");
			result.runDictionary("1:HTML,2:TXT", "DOMTYPE");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "WRITEPOP");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "READPOP");
			result.runformatTime("PUBTIME", "yyyy-MM-dd HH:mm:ss");
			list = result.getObjectList(DocBrief.class);
			for (DocBrief node : list) {
				if (node.getImgid() != null) {
					node.setImgurl(farmFileManagerImpl.getFileURL(node.getImgid()));
				}
			}
		} catch (SQLException e) {
			log.error(e.toString());
			list = new ArrayList<DocBrief>();
		}
		return list;
	}

	@Override
	public DataResult getStatGoodUsers(int i) {
		try {
			DataQuery query = new DataQuery();
			query.setPagesize(i);
			query = DataQuery.init(query,
					"( SELECT d.ID AS ID,d.NAME AS NAME,SUM(b.PRAISEYES) AS SUMYES FROM FARM_DOC a LEFT JOIN FARM_DOCRUNINFO b ON a.RUNINFOID = b.ID LEFT JOIN ALONE_AUTH_USER d ON a.CUSER = d.ID where a.STATE='1' GROUP BY d.ID,d.NAME ) g",
					"ID,NAME,SUMYES");
			query.addSort(new DBSort("g.SUMYES", "DESC"));
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
					CACHE_UNIT.second);
			return query.search();
		} catch (Exception e) {
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult getStatGoodGroups(int i) {
		try {
			DataQuery query = new DataQuery();
			query.setPagesize(i);
			query = DataQuery.init(query,
					"( SELECT d.ID   AS ID, d.GROUPNAME AS NAME,d.JOINCHECK as JOINCHECK, SUM(b.PRAISEYES) AS SUMYES FROM FARM_DOC a LEFT JOIN FARM_DOCRUNINFO b ON a.RUNINFOID = b.ID LEFT JOIN FARM_DOCGROUP d ON d.id=a.DOCGROUPID WHERE a.STATE='1' and d.ID IS NOT NULL GROUP BY d.ID,d.GROUPNAME,d.JOINCHECK) g",
					"ID,NAME,SUMYES,JOINCHECK");
			query.addSort(new DBSort("g.SUMYES", "DESC"));
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
					CACHE_UNIT.second);
			return query.search();
		} catch (Exception e) {
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult getStatMostUsers(int i) {
		try {
			DataQuery query = new DataQuery();
			query.setPagesize(i);
			query = DataQuery.init(query,
					"( SELECT b.ID AS ID, b.name AS NAME, COUNT(*) AS NUM FROM FARM_DOC a LEFT JOIN ALONE_AUTH_USER b ON a.CUSER = b.ID where a.STATE='1' GROUP BY b.id,b.NAME) g",
					"ID,NAME,NUM");
			query.addSort(new DBSort("NUM", "DESC"));
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
					CACHE_UNIT.second);
			return query.search();
		} catch (Exception e) {
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult getStatGoodDocs(int i) {
		try {
			DataQuery query = new DataQuery();
			query.setPagesize(i);
			query = DataQuery.init(query,
					"FARM_DOC a LEFT JOIN FARM_DOCRUNINFO b ON a.RUNINFOID=b.ID LEFT JOIN FARM_DOCGROUP c ON a.DOCGROUPID=c.ID LEFT JOIN ALONE_AUTH_USER d ON a.CUSER=d.ID",
					"a.title AS title,a.id AS id,c.JOINCHECK AS JOINCHECK,b.EVALUATE AS EVALUATE,c.GROUPIMG AS GROUPIMG,d.IMGID AS PHOTOID,d.NAME AS USERNAME,c.GROUPNAME AS GROUPNAME,a.DOCGROUPID AS GROUPID");
			query.addSort(new DBSort("b.EVALUATE", "DESC"));
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
					CACHE_UNIT.second);
			return query.search();
		} catch (Exception e) {
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult getStatBadDocs(int i) {
		try {
			DataQuery query = new DataQuery();
			query.setPagesize(i);
			query = DataQuery.init(query,
					"FARM_DOC a LEFT JOIN FARM_DOCRUNINFO b ON a.RUNINFOID=b.ID LEFT JOIN FARM_DOCGROUP c ON a.DOCGROUPID=c.ID LEFT JOIN ALONE_AUTH_USER d ON a.CUSER=d.ID",
					"a.title AS title,a.id AS id,c.JOINCHECK AS JOINCHECK,b.EVALUATE AS EVALUATE,c.GROUPIMG AS GROUPIMG,d.IMGID AS PHOTOID,d.NAME AS USERNAME,c.GROUPNAME AS GROUPNAME,a.DOCGROUPID AS GROUPID");
			query.addSort(new DBSort("b.EVALUATE", "ASC"));
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
					CACHE_UNIT.second);
			return query.search();
		} catch (Exception e) {
			return DataResult.getInstance();
		}
	}

	@Override
	public Map<String, Object> getStatNumForDay() throws Exception {
		Map<String, Object> map = new HashMap<>();
		DataQuery query = new DataQuery();
		query = DataQuery.init(query,
				"(SELECT COUNT(id) AS num,LEFT(ctime,8) AS DATE FROM farm_doc GROUP BY DATE) AS a", "NUM,DATE");
		query.setPagesize(10000);
		query.addSort(new DBSort("DATE", "ASC"));
		query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
				CACHE_UNIT.minute);
		DataResult result = query.search();
		DataQuery queryGood = DataQuery.init(query,
				"(SELECT COUNT(a.id) AS num,LEFT(a.ctime,8) AS DATE FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID WHERE b.EVALUATE>0  GROUP BY DATE) AS a",
				"NUM,DATE");
		queryGood.setPagesize(10000);
		queryGood.addSort(new DBSort("DATE", "ASC"));
		DataResult resultGood = queryGood.search();
		DataQuery queryBad = DataQuery.init(query,
				"(SELECT COUNT(a.id) AS num,LEFT(a.ctime,8) AS DATE FROM farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID WHERE b.EVALUATE<0  GROUP BY DATE) AS a",
				"NUM,DATE");
		queryBad.setPagesize(10000);
		queryBad.addSort(new DBSort("DATE", "ASC"));
		DataResult resultBad = queryBad.search();
		List<List<Object>> jsonResultDayNum = new ArrayList<List<Object>>();
		List<List<Object>> jsonResultTotalNum = new ArrayList<List<Object>>();
		List<List<Object>> jsonResultTotalGoodNum = new ArrayList<List<Object>>();
		List<List<Object>> jsonResultTotalBadNum = new ArrayList<List<Object>>();
		for (Map<String, Object> node : result.getResultList()) {
			List<Object> nodeList = new ArrayList<Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			nodeList.add(Long.valueOf(sdf.parse(node.get("DATE").toString()).getTime()));
			nodeList.add(Float.valueOf(node.get("NUM").toString()));
			jsonResultDayNum.add(nodeList);
		}
		Float TotalNum = Float.valueOf(0);
		for (Map<String, Object> node : result.getResultList()) {
			List<Object> nodeList = new ArrayList<Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			nodeList.add(Long.valueOf(sdf.parse(node.get("DATE").toString()).getTime()));
			TotalNum = TotalNum + Float.valueOf(node.get("NUM").toString());
			nodeList.add(TotalNum);
			jsonResultTotalNum.add(nodeList);
		}
		Float TotalGoodNum = Float.valueOf(0);
		for (Map<String, Object> node : resultGood.getResultList()) {
			List<Object> nodeList = new ArrayList<Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			nodeList.add(Long.valueOf(sdf.parse(node.get("DATE").toString()).getTime()));
			TotalGoodNum = TotalGoodNum + Float.valueOf(node.get("NUM").toString());
			nodeList.add(TotalGoodNum);
			jsonResultTotalGoodNum.add(nodeList);
		}
		Float TotalBadNum = Float.valueOf(0);
		for (Map<String, Object> node : resultBad.getResultList()) {
			List<Object> nodeList = new ArrayList<Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			nodeList.add(Long.valueOf(sdf.parse(node.get("DATE").toString()).getTime()));
			TotalBadNum = TotalBadNum + Float.valueOf(node.get("NUM").toString());
			nodeList.add(TotalBadNum);
			jsonResultTotalBadNum.add(nodeList);
		}
		map.put("DayNum", jsonResultDayNum);
		map.put("TotalNum", jsonResultTotalNum);
		map.put("GoodNum", jsonResultTotalGoodNum);
		map.put("BadNum", jsonResultTotalBadNum);
		return map;
	}

	@Override
	public DataResult getStatUser(User user) {
		try {
			DataQuery query = new DataQuery();
			StringBuffer sql = new StringBuffer();
			sql.append(
					"SELECT a.NAME AS NAME,a.ID AS ID,a.ctime AS ctime,(SELECT COUNT(*) FROM FARM_DOC WHERE cuser=a.ID and STATE='1') num,(SELECT MIN(ctime) FROM FARM_DOC WHERE cuser=a.ID) stime,(SELECT MAX(ctime) FROM FARM_DOC WHERE cuser=a.ID) etime,");
			sql.append(
					"(SELECT SUM(pb.PRAISEYES) FROM FARM_DOC pa LEFT JOIN FARM_DOCRUNINFO pb ON pb.id=pa.RUNINFOID WHERE cuser=a.ID and pa.STATE='1') AS OKNUM,");
			sql.append(
					"(SELECT SUM(pb.PRAISENO) FROM FARM_DOC pa LEFT JOIN FARM_DOCRUNINFO pb ON pb.id=pa.RUNINFOID WHERE cuser=a.ID and pa.STATE='1') AS NONUM,");
			sql.append(
					"(SELECT SUM(pb.EVALUATE) FROM FARM_DOC pa LEFT JOIN FARM_DOCRUNINFO pb ON pb.id=pa.RUNINFOID WHERE cuser=a.ID and pa.STATE='1') AS EVANUM,");
			sql.append(
					"(SELECT SUM(pb.VISITNUM) FROM FARM_DOC pa LEFT JOIN FARM_DOCRUNINFO pb ON pb.id=pa.RUNINFOID WHERE cuser=a.ID and pa.STATE='1') AS VINUM,");
			sql.append(
					"(SELECT COUNT(*) FROM FARM_DOC pa LEFT JOIN FARM_DOCRUNINFO pb ON pb.id=pa.RUNINFOID WHERE cuser=a.ID AND pb.EVALUATE>0 and pa.STATE='1') AS GOODNUM,");
			sql.append(
					"(SELECT COUNT(*) FROM FARM_DOC pa LEFT JOIN FARM_DOCRUNINFO pb ON pb.id=pa.RUNINFOID WHERE cuser=a.ID AND pb.EVALUATE<0 and pa.STATE='1') AS BADNUM");
			sql.append(" FROM ALONE_AUTH_USER a WHERE a.ID='" + user.getId() + "'");
			query.setPagesize(10);
			query = DataQuery.init(query, "(" + sql.toString() + ") g",
					"ID,NAME,NUM,OKNUM,NONUM,EVANUM,VINUM,GOODNUM,BADNUM,STIME,ETIME,CTIME");
			query.setNoCount();

			// query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.long")),
			// CACHE_UNIT.second);//修改用户信息后，这里取到的是缓存数据。
			DataResult result = query.search();
			if (result.getResultList().get(0).get("EVANUM") == null) {
				result.getResultList().get(0).put("EVANUM", 0);
			}
			if (result.getResultList().get(0).get("VINUM") == null) {
				result.getResultList().get(0).put("VINUM", 0);
			}
			if (result.getResultList().get(0).get("OKNUM") == null) {
				result.getResultList().get(0).put("OKNUM", 0);
			}
			if (result.getResultList().get(0).get("NONUM") == null) {
				result.getResultList().get(0).put("NONUM", 0);
			}
			float eva = new Float(result.getResultList().get(0).get("EVANUM").toString());
			if (eva < 0) {
				eva = 0;// 该等级值不能为负数
			}
			float startRight = 5;
			int L4 = (int) Math.floor(eva / (startRight * startRight * startRight));// 125
			float L4m = eva % (startRight * startRight * startRight);
			int L3 = (int) Math.floor(L4m / (startRight * startRight));// 25
			float L3m = L4m % (startRight * startRight);// 25
			int L2 = (int) Math.floor(L3m / startRight);// 5
			float L2m = L3m % startRight;// 5
			int L1 = (int) Math.floor(L2m);// 1
			result.getResultList().get(0).put("L4", L4);
			result.getResultList().get(0).put("L3", L3);
			result.getResultList().get(0).put("L2", L2);
			result.getResultList().get(0).put("L1", L1);
			return result;
		} catch (Exception e) {
			return DataResult.getInstance();
		}
	}

	@Override
	public DataResult userDocs(String userid, String domtype, int pagesize, int pagenum) {
		DataQuery query = DataQuery.getInstance(pagenum,
				"a.ID as docid,a.TITLE AS title,a.CUSER as authorid,a.DOCDESCRIBE AS text,DOMTYPE,a.AUTHOR AS AUTHOR,a.PUBTIME AS pubtime,a.IMGID AS imgid,b.VISITNUM AS VISITNUM,b.answeringnum as answeringnum,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,d.NAME AS typename,e.IMGID AS photoid, e.id as userid, e.name as username",
				"farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID   LEFT JOIN ALONE_AUTH_USER e ON e.ID=a.CUSER");
		query.addRule(new DBRule("a.STATE", "1", "="));
		query.addRule(new DBRule("a.domtype", domtype, "="));
		query.addRule(new DBRule("a.CUSER", userid, "="));
		query.addSort(new DBSort("a.etime", "desc"));
		query.setPagesize(pagesize);
		DataResult result = null;
		try {
			result = query.search();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Map<String, Object> node : result.getResultList()) {
			node.put("PUBTIME", FarmFormatUnits.getFormateTime(node.get("PUBTIME").toString(), true));
			node.put("TEXT", node.get("TEXT").toString().replaceAll("<", "").replaceAll(">", ""));
			if (node.get("PHOTOID") != null) {
				node.put("PHOTOID", farmFileManagerImpl.getFileURL(node.get("PHOTOID").toString()));
			}
		}
		return result;
	}

	@Override
	public DataResult userPubDocs(String userid, String domtype, int pagesize, int pagenum) {
		DataQuery query = DataQuery.getInstance(pagenum,
				"a.ID as docid,a.TITLE AS title,a.CUSER as authorid,a.DOCDESCRIBE AS text,DOMTYPE,a.AUTHOR AS AUTHOR,a.PUBTIME AS pubtime,a.IMGID AS imgid,b.VISITNUM AS VISITNUM,b.answeringnum as answeringnum,b.PRAISEYES AS PRAISEYES,b.PRAISENO AS PRAISENO,b.HOTNUM AS HOTNUM,d.NAME AS typename,e.IMGID AS photoid, e.id as userid, e.name as username",
				"farm_doc a LEFT JOIN farm_docruninfo b ON a.RUNINFOID=b.ID LEFT JOIN farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID   LEFT JOIN ALONE_AUTH_USER e ON e.ID=a.CUSER");
		query.addRule(new DBRule("a.READPOP", "1", "="));
		query.addRule(new DBRule("a.STATE", "1", "="));
		query.addRule(new DBRule("a.domtype", domtype, "="));
		query.addRule(new DBRule("a.CUSER", userid, "="));
		query.addSort(new DBSort("a.etime", "desc"));
		query.setPagesize(pagesize);
		DataResult result = null;
		try {
			result = query.search();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Map<String, Object> node : result.getResultList()) {
			node.put("PUBTIME", FarmFormatUnits.getFormateTime(node.get("PUBTIME").toString(), true));
			node.put("TEXT", node.get("TEXT").toString().replaceAll("<", "").replaceAll(">", ""));
			if (node.get("PHOTOID") != null) {
				node.put("PHOTOID", farmFileManagerImpl.getFileURL(node.get("PHOTOID").toString()));
			}
		}
		return result;
	}

	@Override
	public DataResult getMyAuditingByUser(String userid, int pagesize, int pagenum) {
		DataQuery query = DataQuery.getInstance(pagenum,
				"a.ID as docid,b.id as AUDITID,b.PCONTENT as AUDITCONTENT,d.NAME AS typename,a.TITLE AS title,b.PSTATE as STATE,a.CUSER as authorid,a.DOCDESCRIBE AS text,DOMTYPE,f.PCONTENT as CONTENT,a.AUTHOR AS AUTHOR,a.PUBTIME AS pubtime,a.IMGID AS imgid",
				"FARM_DOC_AUDIT b left join FARM_DOC a on a.id=b.DOCID LEFT JOIN  farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID  left join FARM_DOCTEXT f on f.id=b.TEXTID");
		query.addRule(new DBRule("b.PSTATE", "1", "="));
		query.addRule(new DBRule("a.CUSER", userid, "="));
		query.addSort(new DBSort("b.etime", "desc"));
		query.setPagesize(pagesize);
		DataResult result = null;
		try {
			result = query.search();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public DataResult getAuditDocByUser(String userid, int pagesize, int pagenum) {
		// 先查出该用户所有拥有的审核权限的分类
		// 在用这些分类id去查询分类下的待审核的知识
		List<String> types = farmDocTypeManagerImpl.getUserAuditTypeIds(userid);
		String typeids = null;
		for (String node : types) {
			if (typeids == null) {
				typeids = "'" + node + "'";
			} else {
				typeids = typeids + ",'" + node + "'";
			}
		}
		DataQuery query = DataQuery.getInstance(pagenum,
				"a.ID as docid,b.id as AUDITID,b.PCONTENT as AUDITCONTENT,d.NAME AS typename,a.TITLE AS title,b.PSTATE as STATE,a.CUSER as authorid,a.DOCDESCRIBE AS text,DOMTYPE,f.PCONTENT as CONTENT,a.AUTHOR AS AUTHOR,a.PUBTIME AS pubtime,a.IMGID AS imgid",
				"FARM_DOC_AUDIT b left join FARM_DOC a on a.id=b.DOCID LEFT JOIN  farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID left join FARM_DOCTEXT f on f.id=b.TEXTID");
		query.addRule(new DBRule("b.PSTATE", "1", "="));
		if (typeids != null) {
			query.addSqlRule(" and d.id in(" + typeids + ")");
		} else {
			query.addSqlRule(" and 1=2");
		}
		query.addSort(new DBSort("a.etime", "desc"));
		query.setPagesize(pagesize);
		DataResult result = null;
		try {
			result = query.search();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public DataResult getMyAuditedByUser(String userid, int pagesize, int pagenum) {
		DataQuery query = DataQuery.getInstance(pagenum,
				"a.ID as docid,b.id as AUDITID,b.etime as ETIME,b.PCONTENT as AUDITCONTENT,d.NAME AS typename,a.TITLE AS title,b.PSTATE as STATE,a.CUSER as authorid,a.DOCDESCRIBE AS text,DOMTYPE,f.PCONTENT as CONTENT,a.AUTHOR AS AUTHOR,a.PUBTIME AS pubtime,a.IMGID AS imgid",
				"FARM_DOC_AUDIT b left join FARM_DOC a on a.id=b.DOCID LEFT JOIN  farm_rf_doctype c ON c.DOCID=a.ID LEFT JOIN farm_doctype d ON d.ID=c.TYPEID  left join FARM_DOCTEXT f on f.id=b.TEXTID");
		query.addSqlRule(" and (b.PSTATE='2' or b.PSTATE='3')");
		query.addRule(new DBRule("a.CUSER", userid, "="));
		query.addSort(new DBSort("b.etime", "desc"));
		query.setPagesize(pagesize);
		DataResult result = null;
		try {
			result = query.search();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@Transactional
	public Map<String, Integer> getStatNum() throws Exception {
		// 知识总数、总人数、好评文档数、差评文档数、分类数量
		Map<String, Integer> map = new HashMap<>();
		map.put("KNOWNUM", farmDocruninfoDao.getKnowsNum());
		map.put("USERNUM", userServiceImpl.getUsersNum());
		map.put("GOODKNOWNUM", farmDocruninfoDao.getGoodKnowsNum());
		map.put("BADKNOWNUM", farmDocruninfoDao.getBadKnowsNum());
		map.put("TYPESNUM", farmDoctypeDao.getTypesNum());
		return map;
	}
}