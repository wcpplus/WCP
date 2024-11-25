package com.farm.doc.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.RequestMode;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDocgroup;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocIndexInter;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.web.WebUtils;

/**
 * 文档管理
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/lucene")
@Controller
public class ActionFarmLuceneQuery extends WebUtils {
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	@Resource
	private FarmDocOperateRightInter farmDocOperateRightImpl;
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	@Resource
	private FarmDocIndexInter farmDocIndexManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	private int doNum;

	/**
	 * 重做索引
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/reIndex")
	@ResponseBody
	public Map<String, Object> reIndex(DataQuery query, HttpServletRequest request) {
		DataResult result;
		query = farmDocManagerImpl.createSimpleDocQuery(query);
		query.setCurrentPage(1);
		int i = 1;
		doNum = 0;
		while (true) {
			try {
				query.setPagesize(100);
				query.setCurrentPage(i++);
				result = query.search();
				if (result.getResultList().size() <= 0) {
					break;
				}
				for (Map<String, Object> node : result.getResultList()) {
					doNum++;
					DocEntire doc = farmDocManagerImpl.getDoc(node.get("ID").toString());
					farmDocIndexManagerImpl.reLuceneIndex(doc, doc);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				break;
			}
		}
		return ViewMode.getInstance().putAttr("num", 0).returnObjMode();
	}

	/**
	 * 删除索引表单
	 * 
	 * @return
	 */
	@RequestMapping("/delForm")
	public ModelAndView delForm(RequestMode pageset, String ids) {
		return ViewMode.getInstance().putAttr("pageset", pageset).returnModelAndView("doc/DocIndexForm");
	}

	/**
	 * 提交删除索引
	 *
	 * @return
	 */
	@RequestMapping("/delCommit")
	@ResponseBody
	public Map<String, Object> delCommit(HttpSession session, String indexid) {
		try {
			// 在所有分类和小组中删除该知识
			List<TypeBrief> types = farmDocTypeManagerImpl.getPubTypes();
			List<String> types_str = new ArrayList<>();
			for (TypeBrief type : types) {
				types_str.add(type.getId());
			}
			List<FarmDocgroup> groups = farmDocgroupManagerImpl.getAllGroup();
			List<String> groups_str = new ArrayList<>();
			for (FarmDocgroup group : groups) {
				groups_str.add(group.getId());
			}
			farmDocIndexManagerImpl.delLuceneIndex(indexid, types_str, groups_str);
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			e.printStackTrace();
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 获取当前索引状态
	 * 
	 * @return
	 */
	@RequestMapping("/indexPersont")
	@ResponseBody
	public Map<String, Object> loadIndexStatr() {
		return ViewMode.getInstance().putAttr("cdocs", doNum).returnObjMode();
	}

}
