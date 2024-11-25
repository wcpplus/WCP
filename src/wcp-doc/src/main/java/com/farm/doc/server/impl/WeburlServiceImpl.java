package com.farm.doc.server.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.query.DataQuery.CACHE_UNIT;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.result.ResultsHandle;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.WeburlDaoInter;
import com.farm.doc.domain.Weburl;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.WeburlServiceInter;
import com.farm.doc.server.commons.DocumentConfig;
import com.farm.parameter.FarmParameterService;

/* *
 *功能：推荐服务服务层实现类
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Service
public class WeburlServiceImpl implements WeburlServiceInter {
	@Resource
	private WeburlDaoInter weburlDaoImpl;
	@Resource
	private FarmFileManagerInter farmFileServer;

	@Override
	@Transactional
	public Weburl insertWeburlEntity(Weburl entity, LoginUser user) {
		entity.setCuser(user.getId());
		entity.setCtime(TimeTool.getTimeDate14());
		entity.setCusername(user.getName());
		entity.setEuser(user.getId());
		entity.setEusername(user.getName());
		entity.setEtime(TimeTool.getTimeDate14());
		entity.setPstate("1");
		return weburlDaoImpl.insertEntity(entity);
	}

	@Override
	@Transactional
	public Weburl editWeburlEntity(Weburl entity, LoginUser user) {
		Weburl entity2 = weburlDaoImpl.getEntity(entity.getId());
		entity2.setWebname(entity.getWebname());
		entity2.setUrl(entity.getUrl());
		entity2.setEusername(user.getName());
		entity2.setEuser(user.getId());
		entity2.setEtime(TimeTool.getTimeDate12());
		entity2.setFileid(entity.getFileid());
		weburlDaoImpl.editEntity(entity2);
		return entity2;
	}

	@Override
	@Transactional
	public void deleteWeburlEntity(String id, LoginUser user) {

		weburlDaoImpl.deleteEntity(weburlDaoImpl.getEntity(id));
	}

	@Override
	@Transactional
	public Weburl getWeburlEntity(String id) {

		if (id == null) {
			return null;
		}
		return weburlDaoImpl.getEntity(id);
	}

	@Override
	@Transactional
	public DataQuery createWeburlSimpleQuery(DataQuery query) {

		DataQuery dbQuery = DataQuery
				.init(query, "FARM_WEBURL",
						"ID,WEBNAME,URL,CTIME,ETIME,CUSERNAME,CUSER,EUSERNAME,EUSER,PCONTENT,PSTATE,SORT,FILEID");
		return dbQuery;
	}

	// ----------------------------------------------------------------------------------
	public WeburlDaoInter getWeburlDaoImpl() {
		return weburlDaoImpl;
	}

	public void setWeburlDaoImpl(WeburlDaoInter dao) {
		this.weburlDaoImpl = dao;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> getList() {
		try {
			DataQuery query = createWeburlSimpleQuery(null);
			query.setNoCount();
			query.setPagesize(100);
			query.setCache(Integer.valueOf(FarmParameterService.getInstance().getParameter("config.wcp.cache.brief")),
					CACHE_UNIT.second);
			DataResult result = query.search();
			result.runHandle(new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					if(row.get("FILEID") != null && !row.get("FILEID").toString().isEmpty()){
						String imgUrl = DocumentConfig.getString("config.doc.download.url") + row.get("FILEID");
						row.put("IMGURL", imgUrl);
					}else{
						row.put("IMGURL", "text/img/none.png");
					}
				}
			});
			List<Map<String, Object>> list = result.getResultList();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public void delImg(String imgid) {
		//清空图片字段
		List<Weburl> list = weburlDaoImpl.selectEntitys(new DBRule("FILEID", imgid, "=").getDBRules());
		for(Weburl weburl : list){
			weburl.setFileid(null);
		}
		
		//删除附件表和附件
		farmFileServer.delFile(imgid, null);
	}
}
