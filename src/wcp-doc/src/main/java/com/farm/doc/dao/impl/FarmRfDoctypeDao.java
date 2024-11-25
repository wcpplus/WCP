package com.farm.doc.dao.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.utils.HibernateSQLTools;
import com.farm.doc.dao.FarmRfDoctypeDaoInter;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.FarmRfDoctype;

/**
 * 中间表-分类文档
 * 
 * @author MAC_wd
 * 
 */
@Repository
public class FarmRfDoctypeDao extends HibernateSQLTools<FarmRfDoctype>implements FarmRfDoctypeDaoInter {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFatory;

	@Transactional
	public void deleteEntity(FarmRfDoctype entity) {
		Session session = sessionFatory.getCurrentSession();
		session.delete(entity);
	}

	@Transactional
	public int getAllListNum() {
		Session session = sessionFatory.getCurrentSession();
		SQLQuery sqlquery = session.createSQLQuery("select count(*) from farm_rf_doctype");
		BigInteger num = (BigInteger) sqlquery.list().get(0);
		return num.intValue();
	}

	@Transactional
	public FarmRfDoctype getEntity(String id) {
		Session session = sessionFatory.getCurrentSession();
		return (FarmRfDoctype) session.get(FarmRfDoctype.class, id);
	}

	@Transactional
	public FarmRfDoctype insertEntity(FarmRfDoctype entity) {
		Session session = sessionFatory.getCurrentSession();
		session.save(entity);
		return entity;
	}

	@Transactional
	public void editEntity(FarmRfDoctype entity) {
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
	@Transactional
	public void deleteEntitys(List<DBRule> rules) {
		deleteSqlFromFunction(sessionFatory.getCurrentSession(), rules);
	}

	@Override
	@Transactional
	public List<FarmRfDoctype> selectEntitys(List<DBRule> rules) {
		return selectSqlFromFunction(sessionFatory.getCurrentSession(), rules);
	}

	@Override
	@Transactional
	public void updataEntitys(Map<String, Object> values, List<DBRule> rules) {
		updataSqlFromFunction(sessionFatory.getCurrentSession(), values, rules);
	}

	public SessionFactory getSessionFatory() {
		return sessionFatory;
	}

	public void setSessionFatory(SessionFactory sessionFatory) {
		this.sessionFatory = sessionFatory;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<FarmDoctype> getDocTypes(String docId) {
		Session session = sessionFatory.getCurrentSession();
		SQLQuery sqlquery = (SQLQuery) session
				.createSQLQuery(
						"select a.NAME AS NAME,a.TYPEMOD AS TYPEMOD,a.READPOP as READPOP,a.WRITEPOP as WRITEPOP,a.AUDITPOP as AUDITPOP,a.CONTENTMOD AS CONTENTMOD,a.SORT AS SORT,a.TYPE AS TYPE,a.METATITLE AS METATITLE,a.METAKEY AS METAKEY,a.METACONTENT AS METACONTENT,a.LINKURL AS LINKURL,a.ID AS ID,a.CTIME AS CTIME,a.ETIME AS ETIME,a.CUSERNAME AS CUSERNAME,a.CUSER AS CUSER,a.EUSERNAME AS EUSERNAME,a.EUSER AS EUSER,a.PCONTENT AS PCONTENT,a.PSTATE,a.PARENTID,a.TAGS,a.TREECODE FROM farm_doctype a LEFT JOIN farm_rf_doctype b ON a.ID=b.TYPEID where b.docid=?")
				.addEntity(FarmDoctype.class).setString(0, docId);
		return (List<FarmDoctype>) sqlquery.list();
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFatory;
	}

	@Override
	protected Class<FarmRfDoctype> getTypeClass() {
		return FarmRfDoctype.class;
	}

	@Override
	public FarmDoctype getDocType(String docId) {
		Session session = sessionFatory.getCurrentSession();
		SQLQuery sqlquery = (SQLQuery) session
				.createSQLQuery(
						"select a.NAME AS NAME,a.TYPEMOD AS TYPEMOD,a.READPOP as READPOP,a.WRITEPOP as WRITEPOP,a.AUDITPOP as AUDITPOP,a.CONTENTMOD AS CONTENTMOD,a.SORT AS SORT,a.TYPE AS TYPE,a.METATITLE AS METATITLE,a.METAKEY AS METAKEY,a.METACONTENT AS METACONTENT,a.LINKURL AS LINKURL,a.ID AS ID,a.CTIME AS CTIME,a.ETIME AS ETIME,a.CUSERNAME AS CUSERNAME,a.CUSER AS CUSER,a.EUSERNAME AS EUSERNAME,a.EUSER AS EUSER,a.PCONTENT AS PCONTENT,a.PSTATE,a.PARENTID,a.TAGS,a.TREECODE FROM farm_doctype a LEFT JOIN farm_rf_doctype b ON a.ID=b.TYPEID where b.docid=?")
				.addEntity(FarmDoctype.class).setString(0, docId);
		@SuppressWarnings("unchecked")
		List<FarmDoctype> list = (List<FarmDoctype>) sqlquery.list();
		return list.size() > 0 ? list.get(0) : null;
	}
}
