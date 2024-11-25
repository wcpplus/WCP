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
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 全文索引
 * 
 * @author zhanghc
 * 
 */
@RequestMapping("/docIndex")
@Controller
public class ActionFarmDocIndexQuery extends WebUtils {
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	private static final Logger log = Logger.getLogger(ActionFarmDocIndexQuery.class);

	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("doc/DocIndexLayout");
	}

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
			DataResult result = farmDocManagerImpl.createSimpleDocQuery(query).search();
			result.runDictionary("1:开放,0:禁用,2:待审核", "STATE");
			result.runDictionary("1:HTML,2:TXT", "DOMTYPE");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "WRITEPOP");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "READPOP");
			result.runformatTime("PUBTIME", "yyyy-MM-dd HH:mm:ss");
			return ViewMode.getInstance().putAttrs(EasyUiUtils.formatGridData(result)).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}
}
