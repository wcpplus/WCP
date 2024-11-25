package com.farm.doc.controller;

import com.farm.doc.domain.Doc;
import com.farm.doc.domain.Farmtop;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmtopServiceInter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;

import com.farm.web.easyui.EasyUiUtils;

import java.util.Map;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

import com.farm.core.page.RequestMode;
import com.farm.core.page.OperateType;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.page.ViewMode;
import com.farm.web.WebUtils;

/* *
 *功能：置顶文档控制层
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@RequestMapping("/farmtop")
@Controller
public class FarmtopController extends WebUtils {
	private final static Logger log = Logger.getLogger(FarmtopController.class);
	@Resource
	FarmtopServiceInter farmTopServiceImpl;
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;

	public FarmtopServiceInter getFarmtopServiceImpl() {
		return farmTopServiceImpl;
	}

	public void setFarmtopServiceImpl(FarmtopServiceInter farmTopServiceImpl) {
		this.farmTopServiceImpl = farmTopServiceImpl;
	}

	/**
	 * 查询结果集合
	 * 
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query,
			HttpServletRequest request) {
		// TODO 自动生成代码,修改后请去除本注释
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			query.addSort(new DBSort("TOP.SORT", "DESC"));
			DataResult result = farmTopServiceImpl.createFarmtopSimpleQuery(query).search();
			result.runDictionary("1:开放,0:禁用,2:待审核", "STATE");
			result.runDictionary("1:HTML,2:TXT", "DOMTYPE");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "WRITEPOP");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "READPOP");
			result.runformatTime("PUBTIME", "yyyy-MM-dd HH:mm:ss");
			
			return ViewMode.getInstance()
					.putAttrs(EasyUiUtils.formatGridData(result))
					.returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}

	/**
	 * 提交修改数据
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(Farmtop entity, HttpSession session) {
		// TODO 自动生成代码,修改后请去除本注释
		try {
			entity = farmTopServiceImpl.editFarmtopEntity(entity,
					getCurrentUser(session));
			return ViewMode.getInstance().setOperate(OperateType.UPDATE)
					.putAttr("entity", entity).returnObjMode();

		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.UPDATE)
					.setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 提交新增数据
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addSubmit(Farmtop entity, HttpSession session) {
		// TODO 自动生成代码,修改后请去除本注释
		try {
			entity = farmTopServiceImpl.insertFarmtopEntity(entity,
					getCurrentUser(session));
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.putAttr("entity", entity).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 删除数据
	 * 
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public Map<String, Object> delSubmit(String ids, HttpSession session) {
		try {
			for (String id : parseIds(ids)) {
				farmTopServiceImpl.deleteFarmtopEntity(id,
						getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}

	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("doc/FarmtopResult");
	}

	/**
	 * 显示详细信息（修改或浏览时）
	 *
	 * @return
	 */
	@RequestMapping("/form")
	public ModelAndView view(RequestMode pageset, String ids) {
		try {
			switch (pageset.getOperateType()) {
			case (0): {// 查看
				Farmtop entity = farmTopServiceImpl.getFarmtopEntity(ids);
				Doc doc = farmDocManagerImpl.getDocOnlyBean(entity.getDocid());
				
				return ViewMode
						.getInstance()
						.putAttr("pageset", pageset)
						.putAttr("entity", farmTopServiceImpl.getFarmtopEntity(ids))
						.putAttr("doctitle", doc.getTitle())
						.returnModelAndView("doc/FarmtopForm");
			}
			case (1): {// 新增
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.returnModelAndView("doc/FarmtopForm");
			}
			case (2): {// 修改
				Farmtop entity = farmTopServiceImpl.getFarmtopEntity(ids);
				Doc doc = farmDocManagerImpl.getDocOnlyBean(entity.getDocid());
				
				return ViewMode
						.getInstance()
						.putAttr("pageset", pageset)
						.putAttr("entity", entity)
						.putAttr("doctitle", doc.getTitle())
						.returnModelAndView("doc/FarmtopForm");
			}
			case (3): {//达到选择文档页面
				return ViewMode .getInstance().returnModelAndView("doc/DocCommonSelectPageForDoc");
			}
			default:
				break;
			}
			return ViewMode.getInstance().returnModelAndView("doc/FarmtopForm");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("doc/FarmtopForm");
		}
	}
	
	/**
	 * 达到文档置顶选择文档页面
	 * v1.0 zhanghc 2015年9月5日下午12:59:45
	 * @return ModelAndView
	 */
	@RequestMapping("/toDocTopChooseDoc")
	public ModelAndView toDocTopChooseDoc() {
		return ViewMode.getInstance().returnModelAndView("doc/DocTopChooseDoc");
	}
	
	/**
	 * 文档置顶选择文档列表数据，只要读权限是公开的数据
	 * v1.0 zhanghc 2015年9月5日下午1:03:03
	 * @param query
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping("/docTopChooseDocList")
	@ResponseBody
	public Map<String, Object> docTopChooseDocList(DataQuery query, HttpServletRequest request) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			query.getAndRemoveRule("READPOP");
			query.addRule(new DBRule("a.READPOP", "1", "="));
			DataResult result = farmTopServiceImpl.docTopChooseDocList(query).search();
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
