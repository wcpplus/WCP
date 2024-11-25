package com.farm.doc.server.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.UsermessageDaoInter;
import com.farm.doc.domain.Usermessage;
import com.farm.doc.server.UsermessageServiceInter;

/* *
 *功能：用户消息服务层实现类
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Service
public class UsermessageServiceImpl implements UsermessageServiceInter {
	@Resource
	private UsermessageDaoInter usermessageDaoImpl;
	@Override
	@Transactional
	public Usermessage insertUsermessageEntity(Usermessage entity, LoginUser user) {
		entity.setCtime(TimeTool.getTimeDate12());
		entity.setCuser(user.getId());
		entity.setCusername(user.getName());
		entity.setPstate("1");
		return usermessageDaoImpl.insertEntity(entity);
	}

	@Override
	@Transactional
	public Usermessage editUsermessageEntity(Usermessage entity, LoginUser user) {
		Usermessage entity2 = usermessageDaoImpl.getEntity(entity.getId());
		entity2.setContent(entity.getContent());
		entity2.setReadstate(entity.getReadstate());
		entity2.setReaduserid(entity.getReaduserid());
		entity2.setTitle(entity.getTitle());
		usermessageDaoImpl.editEntity(entity2);
		return entity2;
	}

	@Override
	@Transactional
	public void deleteUsermessageEntity(String id, LoginUser user) {

		usermessageDaoImpl.deleteEntity(usermessageDaoImpl.getEntity(id));
	}

	@Override
	@Transactional
	public Usermessage getUsermessageEntity(String id) {

		if (id == null) {
			return null;
		}
		return usermessageDaoImpl.getEntity(id);
	}

	@Override
	@Transactional
	public DataQuery createUsermessageSimpleQuery(DataQuery query) {

		DataQuery dbQuery = DataQuery.init(query,
				"FARM_USERMESSAGE USERMESSAGE " + "LEFT JOIN ALONE_AUTH_USER USER ON USERMESSAGE.READUSERID = USER.ID",
				"USERMESSAGE.ID AS ID, USERMESSAGE.CTIME AS USERMESSAGECTIME, USERMESSAGE.READUSERID AS READUSERID,USERMESSAGE.CONTENT AS CONTENT,"
						+ "USERMESSAGE.TITLE AS TITLE,USERMESSAGE.READSTATE AS READSTATE,USERMESSAGE.PCONTENT AS PCONTENT,"
						+ "USERMESSAGE.PSTATE AS PSTATE,USERMESSAGE.CUSERNAME AS CUSERNAME,USERMESSAGE.CUSER AS CUSER,"
						+ "USERMESSAGE.CTIME AS CTIME, USER.NAME AS READUSERNAME");
		return dbQuery;
	}

	// ----------------------------------------------------------------------------------
	public UsermessageDaoInter getUsermessageDaoImpl() {
		return usermessageDaoImpl;
	}

	public void setUsermessageDaoImpl(UsermessageDaoInter dao) {
		this.usermessageDaoImpl = dao;
	}

	@Override
	@Transactional
	public void setRead(String id) {
		Usermessage entity = usermessageDaoImpl.getEntity(id);
		entity.setReadstate("1");
	}

	@Override
	@Transactional
	public void sendMessage(String readUserId, String text, String title, String note, LoginUser sendUser) {
		Usermessage message = new Usermessage();
		message.setContent(text);
		message.setCtime(TimeTool.getTimeDate14());
		if (sendUser != null) {
			message.setCuser(sendUser.getId());
			message.setCusername(sendUser.getName());
		} else {
			message.setCuser("SYS");
			message.setCusername("系统消息");
		}
		if (note != null) {
			message.setPcontent(note);
		}
		message.setPstate("1");
		message.setReadstate("0");
		message.setReaduserid(readUserId);
		message.setTitle(title);
		usermessageDaoImpl.insertEntity(message);
	}

	@Override
	@Transactional
	public void sendMessage(String readUserId, String text, String title, LoginUser sendUser) {
		sendMessage(readUserId, text, title, null, sendUser);
	}

	@Override
	@Transactional
	public void sendMessage(String readUserId, String text, String title) {
		sendMessage(readUserId, text, title, null, null);
	}

	@Override
	@Transactional
	public void sendMessage(List<String> userids, String text, String title) {
		for (String id : userids) {
			sendMessage(id, text, title);
		}
	}

	@Override
	public DataResult getMyMessageByUser(String userId, int pageSize, Integer currPage) {
		DataQuery query = createUsermessageSimpleQuery(null);
		query.addRule(new DBRule("USERMESSAGE.READUSERID", userId, "="));
		query.addSort(new DBSort("USERMESSAGE.CTIME", "DESC"));
		query.setCurrentPage(currPage);
		query.setPagesize(pageSize);
		try {
			return query.search();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
