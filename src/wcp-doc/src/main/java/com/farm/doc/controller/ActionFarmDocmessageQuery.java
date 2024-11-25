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
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDocmessage;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 留言板
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/docmessage")
@Controller
public class ActionFarmDocmessageQuery extends WebUtils {
	private static final Logger log = Logger.getLogger(ActionFarmDocmessageQuery.class);
	@Resource
	private FarmDocmessageManagerInter farmDocmessageManagerImpl;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query,
			HttpServletRequest request, String ids) {
		query = EasyUiUtils.formatGridQuery(request, query);
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			query.addRule(new DBRule("APPID", ids, "="));
			DataResult result = farmDocmessageManagerImpl.createMessageQuery(query)
					.search();
			result.runformatTime("CTIME", "yyyy-MM-dd HH:mm:ss");
			result.runDictionary("1:已读,0:未读", "READSTATE");
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
				.returnModelAndView("doc/docmessageResult");
	}

	/**
	 * 提交修改数据
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(FarmDocmessage entity, HttpSession session) {
		try {
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.putAttr("entity", entity).returnObjMode();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.ADD)
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
	public Map<String, Object> addSubmit(FarmDocmessage entity, HttpSession session) {
		try {
			farmDocmessageManagerImpl.sendMessage(entity.getReaduserid(), entity.getContent(), entity.getTitle(), "私人消息", getCurrentUser(session));
			return ViewMode.getInstance().putAttr("entity", entity).returnObjMode();
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
				farmDocmessageManagerImpl.deleteMessage(id, getCurrentUser(session));
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
						.returnModelAndView("parameter/pAloneApplogEntity");
			}
			case (0): {// 展示
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmDocmessageManagerImpl.getMessage(ids))
						.returnModelAndView("parameter/pAloneApplogEntity");
			}
			case (2): {// 修改
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmDocmessageManagerImpl.getMessage(ids))
						.returnModelAndView("parameter/pAloneApplogEntity");
			}
			default:
				break;
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("parameter/pAloneApplogEntity");
		}
		return ViewMode.getInstance().returnModelAndView(
				"parameter/pAloneParameterEntity");
	}
}
