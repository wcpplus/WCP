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
import com.farm.doc.dao.FarmRfDoctextfileDaoInter;
import com.farm.doc.domain.FarmRfDoctextfile;

/**中间表-内容-附件
 * @author MAC_wd
 * 
 */
@Repository
public class FarmRfDoctextfileDao extends HibernateSQLTools<FarmRfDoctextfile> implements  FarmRfDoctextfileDaoInter {
	@Resource(name = "sessionFactory") 
	private SessionFactory sessionFatory;

	public void deleteEntity(FarmRfDoctextfile entity) {
		Session session=sessionFatory.getCurrentSession();
		session.delete(entity);
	}
	public int getAllListNum(){
		Session session= sessionFatory.getCurrentSession();
		SQLQuery sqlquery= session.createSQLQuery("select count(*) from farm_rf_doctextfile");
		BigInteger num=(BigInteger)sqlquery.list().get(0);
		return num.intValue() ;
	}
	public FarmRfDoctextfile getEntity(String id) {
		Session session= sessionFatory.getCurrentSession();
		return (FarmRfDoctextfile)session.get(FarmRfDoctextfile.class, id);
	}
	public FarmRfDoctextfile insertEntity(FarmRfDoctextfile entity) {
		Session session= sessionFatory.getCurrentSession();
		session.save(entity);
		return entity;
	}
	public void editEntity(FarmRfDoctextfile entity) {
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
	public List<FarmRfDoctextfile> selectEntitys(List<DBRule> rules) {
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
	protected Class<FarmRfDoctextfile> getTypeClass() {
		return FarmRfDoctextfile.class;
	}
}
