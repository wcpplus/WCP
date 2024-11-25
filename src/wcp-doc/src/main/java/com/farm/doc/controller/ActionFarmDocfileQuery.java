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

import com.farm.core.page.OperateType;
import com.farm.core.page.RequestMode;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 文件
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/docfile")
@Controller
public class ActionFarmDocfileQuery extends WebUtils {
	private static final Logger log = Logger.getLogger(ActionFarmDocfileQuery.class);
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query, HttpServletRequest request) {
		query = EasyUiUtils.formatGridQuery(request, query);
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			DataQuery dbQuery = DataQuery.init(query, "farm_docfile",
					"id,DIR,TYPE,NAME,EXNAME,LEN,FILENAME,PSTATE,PCONTENT");
			DataResult result = dbQuery.search();
			result.runDictionary("1:使用,0:临时", "PSTATE");
			result.runDictionary("1:图片,2:资源,3:压缩,0:其他", "TYPE");
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
		return ViewMode.getInstance()
				.returnModelAndView("doc/DocfileResult");
	}
	
	/**
	 * 提交修改数据
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(FarmDocfile entity, HttpSession session) {
		try {
			return ViewMode.getInstance().setOperate(OperateType.ADD).returnObjMode();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance()
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
	public Map<String, Object> addSubmit(HttpSession session) {
		try {
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
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
				farmFileManagerImpl.delFile(id,
						getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
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
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.returnModelAndView("doc/DocfileForm");
			}
			case (0): {// 展示
				FarmDocfile entity = farmFileManagerImpl.getFile(ids);
				entity.setUrl(farmFileManagerImpl.getFileURL(ids));
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", entity)
						.returnModelAndView("doc/DocfileForm");
			}
			case (2): {// 修改
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmFileManagerImpl.getFile(ids))
						.returnModelAndView("doc/DocfileForm");
			}
			default:
				break;
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("doc/DocfileForm");
		}
		return ViewMode.getInstance().returnModelAndView(
				"parameter/DocfileForm.jsp");
	}
}
