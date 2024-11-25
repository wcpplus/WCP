package  com.farm.doc.dao.impl;

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
import com.farm.doc.dao.FarmDoctextDaoInter;
import com.farm.doc.domain.FarmDoctext;

/**文档内容
 * @author MAC_wd
 * 
 */
@Repository
public class FarmDoctextDao extends HibernateSQLTools<FarmDoctext> implements  FarmDoctextDaoInter {
	@Resource(name = "sessionFactory") 
	private SessionFactory sessionFatory;

	public void deleteEntity(FarmDoctext entity) {
		Session session=sessionFatory.getCurrentSession();
		session.delete(entity);
	}
	public int getAllListNum(){
		Session session= sessionFatory.getCurrentSession();
		SQLQuery sqlquery= session.createSQLQuery("select count(*) from farm_doctext");
		BigInteger num=(BigInteger)sqlquery.list().get(0);
		return num.intValue() ;
	}
	public FarmDoctext getEntity(String id) {
		Session session= sessionFatory.getCurrentSession();
		return (FarmDoctext)session.get(FarmDoctext.class, id);
	}
	public FarmDoctext insertEntity(FarmDoctext entity) {
		Session session= sessionFatory.getCurrentSession();
		session.save(entity);
		return entity;
	}
	public void editEntity(FarmDoctext entity) {
		Session session= sessionFatory.getCurrentSession();
		session.update(entity);
	}
	
	@Override
	public Session getSession() {
		return sessionFatory.getCurrentSession();
	}
	public DataResult runSqlQuery(DataQuery query){
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
	public List<FarmDoctext> selectEntitys(List<DBRule> rules) {
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
	protected SessionFactory getSessionFactory() {
		return sessionFatory;
	}
	@Override
	protected Class<FarmDoctext> getTypeClass() {
		return FarmDoctext.class;
	}
}
