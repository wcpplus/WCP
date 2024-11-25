package  com.farm.doc.dao.impl;

import java.math.BigInteger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.farm.doc.domain.Farmtop;
import com.farm.doc.dao.FarmtopDaoInter;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.utils.HibernateSQLTools;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;

/* *
 *功能：置顶文档持久层实现
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Repository
public class FarmtopDaoImpl  extends HibernateSQLTools<Farmtop> implements  FarmtopDaoInter {
  @Resource(name = "sessionFactory")
  private SessionFactory sessionFatory;
  
  @Override
  public void deleteEntity(Farmtop farmtop) {
    // TODO 自动生成代码,修改后请去除本注释
    Session session=sessionFatory.getCurrentSession();
    session.delete(farmtop);
  }
  @Override
  public int getAllListNum(){
    // TODO 自动生成代码,修改后请去除本注释
    Session session= sessionFatory.getCurrentSession();
    SQLQuery sqlquery= session.createSQLQuery("select count(*) from farm_code_field");
    BigInteger num=(BigInteger)sqlquery.list().get(0);
    return num.intValue() ;
  }
  @Override
  public Farmtop getEntity(String farmtopid) {
    // TODO 自动生成代码,修改后请去除本注释
    Session session= sessionFatory.getCurrentSession();
    return (Farmtop)session.get(Farmtop.class, farmtopid);
  }
  @Override
  public Farmtop insertEntity(Farmtop farmtop) {
    // TODO 自动生成代码,修改后请去除本注释
    Session session= sessionFatory.getCurrentSession();
    session.save(farmtop);
    return farmtop;
  }
  @Override
  public void editEntity(Farmtop farmtop) {
    // TODO 自动生成代码,修改后请去除本注释
    Session session= sessionFatory.getCurrentSession();
    session.update(farmtop);
  }
  
  @Override
  public Session getSession() {
    // TODO 自动生成代码,修改后请去除本注释
    return sessionFatory.getCurrentSession();
  }
  @Override
  public DataResult runSqlQuery(DataQuery query){
    // TODO 自动生成代码,修改后请去除本注释
    try {
      return query.search(sessionFatory.getCurrentSession());
    } catch (Exception e) {
      return null;
    }
  }
  @Override
  public void deleteEntitys(List<DBRule> rules) {
    // TODO 自动生成代码,修改后请去除本注释
    deleteSqlFromFunction(sessionFatory.getCurrentSession(), rules);
  }

  @Override
  public List<Farmtop> selectEntitys(List<DBRule> rules) {
    // TODO 自动生成代码,修改后请去除本注释
    return selectSqlFromFunction(sessionFatory.getCurrentSession(), rules);
  }

  @Override
  public void updataEntitys(Map<String, Object> values, List<DBRule> rules) {
    // TODO 自动生成代码,修改后请去除本注释
   updataSqlFromFunction(sessionFatory.getCurrentSession(), values, rules);
  }
  
  @Override
  public int countEntitys(List<DBRule> rules) {
    // TODO 自动生成代码,修改后请去除本注释
   return countSqlFromFunction(sessionFatory.getCurrentSession(), rules);
  }
  
  public SessionFactory getSessionFatory() {
    return sessionFatory;
  }

  public void setSessionFatory(SessionFactory sessionFatory) {
    this.sessionFatory = sessionFatory;
  }
 @Override
	protected Class<?> getTypeClass() {
		return Farmtop.class;
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFatory;
	}
}
