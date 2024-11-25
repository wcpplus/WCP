package com.farm.doc.dao.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.utils.HibernateSQLTools;
import com.farm.doc.dao.FarmDocmessageDaoInter;
import com.farm.doc.domain.FarmDocmessage;

/**
 * 留言板
 * 
 * @author MAC_wd
 * 
 */
@Repository
public class FarmDocmessageDao extends HibernateSQLTools<FarmDocmessage> implements FarmDocmessageDaoInter {
	@Resource(name = "sessionFactory") 
	private SessionFactory sessionFatory;

	public void deleteEntity(FarmDocmessage entity) {
		Session session = sessionFatory.getCurrentSession();
		session.delete(entity);
	}

	public int getAllListNum() {
		Session session = sessionFatory.getCurrentSession();
		SQLQuery sqlquery = session
				.createSQLQuery("select count(*) from farm_docmessage");
		BigInteger num = (BigInteger) sqlquery.list().get(0);
		return num.intValue();
	}

	public FarmDocmessage getEntity(String id) {
		Session session = sessionFatory.getCurrentSession();
		return (FarmDocmessage) session.get(FarmDocmessage.class, id);
	}

	public FarmDocmessage insertEntity(FarmDocmessage entity) {
		Session session = sessionFatory.getCurrentSession();
		session.save(entity);
		return entity;
	}

	public void editEntity(FarmDocmessage entity) {
		Session session = sessionFatory.getCurrentSession();
		session.update(entity);
	}

	@Override
	public Session getSession() {
		return sessionFatory.getCurrentSession();
	}

	public DataResult runSqlQuery(DataQuery query) {
		try {
			return query.search(sessionFatory.getCurrentSession());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void deleteEntitys(List<DBRule> rules) {
		deleteSqlFromFunction(sessionFatory.getCurrentSession(), rules);
	}

	@Override
	public List<FarmDocmessage> selectEntitys(List<DBRule> rules) {
		return selectSqlFromFunction(
				sessionFatory.getCurrentSession(), rules);
	}

	@Override
	public void updataEntitys(Map<String, Object> values, List<DBRule> rules) {
		updataSqlFromFunction(sessionFatory.getCurrentSession(),
				values, rules);
	}

	public SessionFactory getSessionFatory() {
		return sessionFatory;
	}

	public void setSessionFatory(SessionFactory sessionFatory) {
		this.sessionFatory = sessionFatory;
	}

	@Override
	public int getNoReadMessageNum(String userId) {
		Session session = sessionFatory.getCurrentSession();
		SQLQuery sqlquery = session
				.createSQLQuery("select count(*) from farm_docmessage where READUSERID=? and READSTATE='0'");
		sqlquery.setString(0, userId);
		BigInteger num = (BigInteger) sqlquery.list().get(0);
		return num.intValue();
	}

	@Override
	public int getAppMessageNum(String appid) {
		Session session = sessionFatory.getCurrentSession();
		SQLQuery sqlquery = session
				.createSQLQuery("select count(*) from farm_docmessage where APPID=?");
		sqlquery.setString(0, appid);
		BigInteger num = (BigInteger) sqlquery.list().get(0);
		return num.intValue();
	}
	
	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFatory;
	}
	@Override
	protected Class<FarmDocmessage> getTypeClass() {
		return FarmDocmessage.class;
	}
}
