package com.farm.doc.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.OperateType;
import com.farm.core.page.RequestMode;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.parameter.FarmParameterService;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiTreeNode;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 文档分类
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/doctype")
@Controller
public class ActionFarmDoctypeQuery extends WebUtils {
	private static final Logger log = Logger.getLogger(ActionFarmDoctypeQuery.class);
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;

	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query, HttpServletRequest request) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			if (query.getQueryRule().size() <= 0) {
				query.addRule(new DBRule("A.PARENTID", "NONE", "="));
			}
			query.addDefaultSort(new DBSort("SORT", "asc"));
			DataResult result = farmDocManagerImpl.createSimpleTypeQuery(query).search()
					.runDictionary(FarmParameterService.getInstance().getDictionary("FARM_DOCTYE_TYPEMOD"), "TYPEMOD")
					.runDictionary(FarmParameterService.getInstance().getDictionary("FARM_DOCTYE_DOCMOD"), "CONTENTMOD")
					.runDictionary("1:结构,2:建设,3:知识,4:链接,5:单页", "TYPE").runDictionary("1:可用,0:禁用", "PSTATE")
					.runDictionary("1:限制,0:所有人,2:继承", "READPOP").runDictionary("1:限制,0:所有人,2:继承", "WRITEPOP")
					.runDictionary("1:需要审核,0:不审核,2:继承", "AUDITPOP");
			return ViewMode.getInstance().putAttrs(EasyUiUtils.formatGridData(result)).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		String isOpen = FarmParameterService.getInstance().getParameter("config.sys.opensource");
		if (isOpen.equals("true")) {
			return ViewMode.getInstance().returnModelAndView("doc/DoctypeResultopen");
		} else {
			return ViewMode.getInstance().returnModelAndView("doc/DoctypeResult");
		}
	}

	@RequestMapping("/FarmDoctypeLoadTreeNode")
	@ResponseBody
	public Map<String, Object> loadTreeNode(DataQuery query, HttpServletRequest request, String id) {
		query = EasyUiUtils.formatGridQuery(request, query);
		try {
			List<EasyUiTreeNode> treeNodes = EasyUiTreeNode.formatAsyncAjaxTree(
					EasyUiTreeNode.queryTreeNodeOne(id, "SORT", "FARM_DOCTYPE", "ID", "PARENTID", "NAME", "CTIME")
							.getResultList(),
					EasyUiTreeNode.queryTreeNodeTow(id, "SORT", "FARM_DOCTYPE", "ID", "PARENTID", "NAME", "CTIME")
							.getResultList(),
					"PARENTID", "ID", "NAME", "CTIME");
			return ViewMode.getInstance().putAttr("treeNodes", treeNodes).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 提交修改数据
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(FarmDoctype entity, HttpSession session) {
		try {
			entity = farmDocTypeManagerImpl.editType(entity, getCurrentUser(session));

			return ViewMode.getInstance().setOperate(OperateType.ADD).putAttr("entity", entity).returnObjMode();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.ADD).setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 提交新增数据
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addSubmit(FarmDoctype entity, HttpSession session) {
		try {
			entity = farmDocTypeManagerImpl.insertType(entity, getCurrentUser(session));
			return ViewMode.getInstance().putAttr("entity", entity).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
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
				farmDocTypeManagerImpl.deleteType(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
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
			case (1): {// 新增
				FarmDoctype parent = farmDocTypeManagerImpl.getType(ids);

				return ViewMode.getInstance().putAttr("pageset", pageset).putAttr("parent", parent)
						.returnModelAndView("doc/DoctypeForm");
			}
			case (0): {// 展示
				FarmDoctype entity = farmDocTypeManagerImpl.getType(ids);
				FarmDoctype parent = null;
				if (entity.getParentid() != null && !entity.getParentid().isEmpty()) {
					parent = farmDocTypeManagerImpl.getType(entity.getParentid());
				}

				return ViewMode.getInstance().putAttr("pageset", pageset).putAttr("entity", entity)
						.putAttr("parent", parent).returnModelAndView("doc/DoctypeForm");
			}
			case (2): {// 修改
				FarmDoctype entity = farmDocTypeManagerImpl.getType(ids);

				FarmDoctype parent = null;
				if (entity.getParentid() != null && !entity.getParentid().isEmpty()) {
					parent = farmDocTypeManagerImpl.getType(entity.getParentid());
				}

				return ViewMode.getInstance().putAttr("pageset", pageset).putAttr("entity", entity)
						.putAttr("parent", parent).returnModelAndView("doc/DoctypeForm");
			}
			case (3): {// 到达选择分类页面
				return ViewMode.getInstance().returnModelAndView("doc/DoctypeChooseTreeWin");
			}
			default:
				break;
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage()).returnModelAndView("doc/DoctypeForm");
		}
		return ViewMode.getInstance().returnModelAndView("doc/DoctypeForm");
	}

}
