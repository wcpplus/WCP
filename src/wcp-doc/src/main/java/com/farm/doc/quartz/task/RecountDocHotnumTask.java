package com.farm.doc.quartz.task;

import java.sql.SQLException;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.util.spring.BeanFactory;

/**
 * 计算文档热度的定时器
 * 
 * @author Administrator
 * 
 */
public class RecountDocHotnumTask implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		 FarmDocRunInfoInter docRunInfoIMP = (FarmDocRunInfoInter) BeanFactory
				.getBean("farmDocRunInfoImpl");
		DataResult result;
		DataQuery query;
		query = DataQuery.getInstance("1", "id,title", "farm_doc");
		query.setCurrentPage(1);
		query.addRule(new DBRule("STATE", "1", "="));
		query.addRule(new DBRule("READPOP", "1", "="));
		int i = 1;
		int docnum = 0;
		while (true) {
			try {
				query.setPagesize(100);
				query.setCurrentPage(i++);
				result = query.search();
				if (result.getResultList().size() <= 0) {
					break;
				}
				for (Map<String, Object> node : result.getResultList()) {
					String docid = node.get("ID").toString();
					docRunInfoIMP.reCountDocHotNum(docid);
					docnum++;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				break;
			}
		}
		System.out.println("对文档(" + docnum + "条数据)计算热度");
	}
}
