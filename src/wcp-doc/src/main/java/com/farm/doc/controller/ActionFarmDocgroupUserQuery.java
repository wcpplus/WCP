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
import com.farm.doc.domain.FarmDocgroupUser;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 工作小组成员
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/docgroupUser")
@Controller
public class ActionFarmDocgroupUserQuery extends WebUtils {
	private static final Logger log = Logger.getLogger(ActionFarmDocgroupUserQuery.class);
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;

	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query, HttpServletRequest request, String ids) {
		query = EasyUiUtils.formatGridQuery(request, query);
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			if (ids != null) {
				query.addRule(new DBRule("GROUPID", ids, "="));
			}
			DataResult result = farmDocgroupManagerImpl.createFarmDocgroupUserSimpleQuery(query).search();
			result.runDictionary("1:是,0:否", "LEADIS");
			result.runDictionary("1:是,0:否", "EDITIS");
			result.runDictionary("1:在用,0:邀请,2:删除,3:申请", "PSTATE");
			return ViewMode.getInstance().putAttrs(EasyUiUtils.formatGridData(result)).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("doc/DocgroupUserResult");
	}

	/**
	 * 提交修改数据
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(FarmDocgroupUser entity, HttpSession session) {
		try {
			entity = farmDocgroupManagerImpl.editMinFarmDocgroupUserEntity(entity, getCurrentUser(session));

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
	public Map<String, Object> addSubmit(String userids, String ids, HttpSession session) {
		try {
			for (String uerId : parseIds(userids)) {
				if (!farmDocgroupManagerImpl.isJoinGroupByUser(ids, uerId)) {
					FarmDocgroupUser groupUser = farmDocgroupManagerImpl.applyGroup(ids, uerId, "系统管理员操作",
							getCurrentUser(session));
					farmDocgroupManagerImpl.agreeJoinApply(groupUser.getId(), getCurrentUser(session));
					farmDocgroupManagerImpl.setEditorForGroup(groupUser.getId(), getCurrentUser(session));
				}
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 设置管理员
	 * 
	 * @return
	 */
	@RequestMapping("/FarmDocgroupUserSetAdmin")
	@ResponseBody
	public Map<String, Object> adminSubmit(String ids, HttpSession session) {
		try {
			for (String id : parseIds(ids)) {
				farmDocgroupManagerImpl.setAdminForGroup(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 取消管理员
	 * 
	 * @return
	 */
	@RequestMapping("/FarmDocgroupUserResetAdmin")
	@ResponseBody
	public Map<String, Object> reAdminSubmit(String ids, HttpSession session) {
		try {
			for (String id : parseIds(ids)) {
				FarmDocgroupUser groupUser = farmDocgroupManagerImpl.getFarmDocgroupUserEntity(id);
				if (groupUser.getUserid().equals(getCurrentUser(session).getId())) {
					throw new RuntimeException("不能取消自己为管理员!");
				}
				farmDocgroupManagerImpl.wipeAdminFromGroup(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
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
				FarmDocgroupUser groupUser = farmDocgroupManagerImpl.getFarmDocgroupUserEntity(id);
				if (groupUser.getUserid().equals(getCurrentUser(session).getId())) {
					throw new RuntimeException("不能删除自己!");
				}
				farmDocgroupManagerImpl.deleteFarmDocgroupUserEntity(id, getCurrentUser(session));
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
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.returnModelAndView("parameter/pAloneApplogEntity");
			}
			case (0): {// 展示
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmDocgroupManagerImpl.getFarmDocgroupUserEntity(ids))
						.returnModelAndView("parameter/pAloneApplogEntity");
			}
			case (2): {// 修改
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmDocgroupManagerImpl.getFarmDocgroupUserEntity(ids))
						.returnModelAndView("parameter/pAloneApplogEntity");
			}
			default:
				break;
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("parameter/pAloneApplogEntity");
		}
		return ViewMode.getInstance().returnModelAndView("parameter/pAloneParameterEntity");
	}
}
