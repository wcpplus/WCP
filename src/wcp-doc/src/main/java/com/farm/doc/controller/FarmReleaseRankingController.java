package com.farm.doc.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/* *
 *功能：文档发布排名控制层
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@RequestMapping("/farmReleaseRanking")
@Controller
public class FarmReleaseRankingController extends WebUtils {
	private final static Logger log = Logger.getLogger(FarmReleaseRankingController.class);
	
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	@Resource
	private FarmDocRunInfoInter farmDocRunInfoImpl;
	/**
	 * 查询结果集合
	 * 
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query, HttpServletRequest request) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			query.addSort(new DBSort("B.GOODRATE", "DESC"));
			DataResult result = farmDocRunInfoImpl.createReleaseRankingSimpleQuery(query).search();
			return ViewMode.getInstance()
					.putAttrs(EasyUiUtils.formatGridData(result))
					.returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}
	
	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("doc/FarmReleaseRankingResult");
	}
}
