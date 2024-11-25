package com.farm.doc.server.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.FarmDocDaoInter;
import com.farm.doc.dao.FarmDocmessageDaoInter;
import com.farm.doc.dao.FarmDocruninfoDaoInter;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDocmessage;
import com.farm.doc.domain.FarmDocruninfo;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.doc.server.UsermessageServiceInter;
import com.farm.doc.server.commons.DocMessageCache;

/**
 * 留言板
 * 
 * @author MAC_wd
 */
@Service
public class FarmDocmessageManagerImpl implements FarmDocmessageManagerInter {
	@Resource
	private FarmDocmessageDaoInter farmDocmessageDao;
	@Resource
	private FarmDocruninfoDaoInter farmDocruninfoDao;
	@Resource
	private FarmDocDaoInter farmDocDao;
	@Resource
	private FarmDocRunInfoInter farmDocRunInfoImpl;
	@Resource
	private UsermessageServiceInter usermessageServiceImpl;
	// private static final Logger log = Logger
	// .getLogger(FarmDocmessageManagerImpl.class);
	private final static Logger log = Logger.getLogger(FarmDocmessageManagerImpl.class);

	@Transactional
	public FarmDocmessage sendMessage(String readUserId, String text, String title, String note, String appId,
			LoginUser sendUser) {
		FarmDocmessage message = new FarmDocmessage();
		message.setCtime(TimeTool.getTimeDate14());
		message.setCuser(sendUser.getId());
		message.setCusername(sendUser.getName());
		message.setAppid(appId);
		message.setContent(text);
		message.setPcontent(note);
		message.setPstate("1");
		message.setReadstate("0");
		message.setReaduserid(readUserId);
		message.setTitle(title);
		return farmDocmessageDao.insertEntity(message);
	}

	@Transactional
	public FarmDocmessage reSendMessage(String messageId, String text, LoginUser sendUser) {
		FarmDocmessage obMes = farmDocmessageDao.getEntity(messageId);
		FarmDocmessage message = new FarmDocmessage();
		message.setCtime(TimeTool.getTimeDate14());
		message.setCuser(sendUser.getId());
		message.setCusername(sendUser.getName());
		message.setAppid(obMes.getAppid());
		message.setContent(text);
		message.setPcontent(obMes.getPcontent());
		message.setPstate("1");
		message.setReadstate("0");
		message.setReaduserid(obMes.getCuser());
		message.setTitle(obMes.getTitle());
		return farmDocmessageDao.insertEntity(message);
	}

	@Transactional
	public void deleteMessage(String entity, LoginUser user) {
		farmDocmessageDao.deleteEntity(farmDocmessageDao.getEntity(entity));
	}

	@Transactional
	public FarmDocmessage getMessage(String id) {
		if (id == null) {
			return null;
		}
		return farmDocmessageDao.getEntity(id);
	}

	@Transactional
	public FarmDocmessage readMessage(String id) {
		if (id == null) {
			return null;
		}
		FarmDocmessage obMes = farmDocmessageDao.getEntity(id);
		obMes.setReadstate("1");
		farmDocmessageDao.editEntity(obMes);
		return obMes;
	}

	@Override
	public DataQuery createMessageQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query, "farm_docmessage a left join alone_auth_user c on c.id=a.READUSERID ",
				"a.id as id,a.CTIME as CTIME,a.CUSERNAME as CUSERNAME,a.CUSER as CUSER,a.PSTATE as PSTATE,a.PCONTENT as PCONTENT,a.READUSERID as READUSERID,c.name as READUSERNAME,a.CONTENT as CONTENT,a.TITLE as TITLE,a.APPID as APPID,a.READSTATE as READSTATE");
		return dbQuery;
	}

	// ----------------------------------------------------------------------------------
	public FarmDocmessageDaoInter getfarmDocmessageDao() {
		return farmDocmessageDao;
	}

	public void setfarmDocmessageDao(FarmDocmessageDaoInter dao) {
		this.farmDocmessageDao = dao;
	}

	public FarmDocruninfoDaoInter getFarmDocruninfoDao() {
		return farmDocruninfoDao;
	}

	public void setFarmDocruninfoDao(FarmDocruninfoDaoInter farmDocruninfoDao) {
		this.farmDocruninfoDao = farmDocruninfoDao;
	}

	public FarmDocDaoInter getFarmDocDao() {
		return farmDocDao;
	}

	public void setFarmDocDao(FarmDocDaoInter farmDocDao) {
		this.farmDocDao = farmDocDao;
	}

	@Override
	public FarmDocmessage sendMessage(String readUserId, String text, String title, String note, LoginUser sendUser) {
		return sendMessage(readUserId, text, title, note, null, sendUser);
	}

	@Override
	public int getNoReadMessageNum(LoginUser user) {
		return farmDocmessageDao.getNoReadMessageNum(user.getId());
	}

	@Override
	@Transactional
	public FarmDocmessage sendAnswering(String content, String title, String mark, String appid, LoginUser sendUser) {
		FarmDocmessage message = new FarmDocmessage();
		message.setCtime(TimeTool.getTimeDate14());
		message.setCuser(sendUser.getId());
		message.setCusername(sendUser.getName());
		message.setAppid(appid);
		message.setContent(content);
		message.setPcontent(mark);
		message.setPstate("1");
		message.setReadstate("0");
		message.setTitle(title);
		message.setReaduserid("NONE");
		// 如果是文档就刷新文档的用量信息
		Doc doc = farmDocDao.getEntity(appid);
		if (doc != null) {
			FarmDocruninfo info = farmDocruninfoDao.getEntity(doc.getRuninfoid());
			int num = farmDocmessageDao.getAppMessageNum(appid);
			info.setAnsweringnum(num + 1);
			farmDocruninfoDao.editEntity(info);
		}
		// 发送消息给知识创建者和关注者
		usermessageServiceImpl.sendMessage(farmDocRunInfoImpl.getDocJoinUserIds(appid),
				"知识<a class='color_a' href='webdoc/view/Pub" + doc.getId() + ".html'>" + doc.getTitle() + "</a>收到一条评论:"
						+ content,
				"收到一条知识评论");
		return farmDocmessageDao.insertEntity(message);
	}

	@Override
	public DataResult getMessages(String docid, int num, int pagesize) {
		DataQuery dbQuery = DataQuery.init(new DataQuery(),
				"FARM_DOCMESSAGE A " + "LEFT JOIN ALONE_AUTH_USER C ON C.ID=A.READUSERID "
						+ "LEFT JOIN ALONE_AUTH_USER D ON A.CUSER = D.ID",
				"A.ID AS ID,A.CTIME AS CTIME,A.CUSERNAME AS CUSERNAME,A.CUSER AS CUSER,"
						+ "A.PSTATE AS PSTATE,A.PCONTENT AS PCONTENT,A.READUSERID AS READUSERID,"
						+ "C.NAME AS READUSERNAME,A.CONTENT AS CONTENT,A.TITLE AS TITLE,A.APPID AS APPID,"
						+ "A.READSTATE AS READSTATE, D.IMGID AS IMGID, "
						+ "A.PRAISNUM AS PRAISNUM, A.CRITCISMNUM AS CRITCISMNUM");
		dbQuery.addRule(new DBRule("APPID", docid, "="));
		dbQuery.addSqlRule(" AND A.PARENTID IS NULL ");
		dbQuery.addSort(new DBSort("CTIME", "DESC"));
		dbQuery.setCurrentPage(num);
		dbQuery.setPagesize(pagesize);
		DataResult result = null;
		try {
			result = dbQuery.search();
		} catch (SQLException e) {
			log.error(e.toString());
			return DataResult.getInstance();
		}
		return result;
	}

	@Override
	@Transactional
	public FarmDocmessage approveOf(String id, LoginUser loginUser) {
		FarmDocmessage entity = farmDocmessageDao.getEntity(id);
		if (!DocMessageCache.getInstance().add(loginUser.getId(), entity.getAppid(), entity.getId())) {
			return entity;
		}

		entity.setPraisnum(entity.getPraisnum() + 1);
		farmDocmessageDao.editEntity(entity);
		return entity;
	}

	@Override
	@Transactional
	public FarmDocmessage oppose(String id, LoginUser loginUser) {
		FarmDocmessage entity = farmDocmessageDao.getEntity(id);
		if (!DocMessageCache.getInstance().add(loginUser.getId(), entity.getAppid(), entity.getId())) {
			return entity;
		}

		entity.setCritcismnum(entity.getCritcismnum() + 1);
		farmDocmessageDao.editEntity(entity);
		return entity;
	}

	@Override
	@Transactional
	public void reply(String content, String appid, String messageId, LoginUser loginUser) {
		FarmDocmessage entity = new FarmDocmessage();
		entity.setCtime(TimeTool.getTimeDate14());
		entity.setCuser(loginUser.getId());
		entity.setCusername(loginUser.getName());
		entity.setPstate("1");
		entity.setPcontent("");
		entity.setReadstate("1");
		entity.setTitle("回复");
		entity.setReaduserid(loginUser.getId());
		entity.setContent(content);
		entity.setAppid(appid);
		entity.setPraisnum(0);
		entity.setCritcismnum(0);
		entity.setParentid(messageId);
		// 如果是文档就刷新文档的用量信息
		Doc doc = farmDocDao.getEntity(entity.getAppid());
		if (doc != null) {
			FarmDocruninfo info = farmDocruninfoDao.getEntity(doc.getRuninfoid());
			int num = farmDocmessageDao.getAppMessageNum(entity.getAppid());
			info.setAnsweringnum(num + 1);
			farmDocruninfoDao.editEntity(info);
		}
		farmDocmessageDao.insertEntity(entity);
		FarmDocmessage parentMessage = farmDocmessageDao.getEntity(entity.getParentid());
		usermessageServiceImpl.sendMessage(parentMessage.getCuser(),
				"知识<a class='color_a' href='webdocmessage/Pubmsg.do?docid=" + doc.getId() + "'>" + doc.getTitle()
						+ "</a>收到一条回复:" + content,
				"收到一条评论回复");
	}

	@Override
	public List<Map<String, Object>> getReplys(String docid, String docMessageId) {
		try {
			DataQuery dbQuery = DataQuery.init(new DataQuery(), "FARM_DOCMESSAGE A ",
					"A.ID AS ID, A.CONTENT AS CONTENT,CTIME,CUSERNAME,CUSER");

			dbQuery.addRule(new DBRule("APPID", docid, "="));
			dbQuery.addRule(new DBRule("PARENTID", docMessageId, "="));
			dbQuery.addSort(new DBSort("CTIME", "DESC"));
			dbQuery.setCurrentPage(1);
			dbQuery.setPagesize(1000);
			return dbQuery.search().getResultList();
		} catch (SQLException e) {
			log.error(e.toString());
			return new ArrayList<Map<String, Object>>();
		}
	}
}
