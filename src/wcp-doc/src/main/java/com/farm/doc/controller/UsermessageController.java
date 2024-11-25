package com.farm.doc.controller;

import com.farm.doc.domain.Usermessage;
import com.farm.doc.server.UsermessageServiceInter;

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

import com.farm.authority.service.UserServiceInter;
import com.farm.core.page.RequestMode;
import com.farm.core.page.OperateType;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.page.ViewMode;
import com.farm.web.WebUtils;

/* *
 *功能：用户消息控制层
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@RequestMapping("/usermessage")
@Controller
public class UsermessageController extends WebUtils {
	private final static Logger log = Logger
			.getLogger(UsermessageController.class);
	@Resource
	private UsermessageServiceInter usermessageServiceImpl;
	@Resource
	private UserServiceInter userServiceImpl;

	public UsermessageServiceInter getUsermessageServiceImpl() {
		return usermessageServiceImpl;
	}

	public void setUsermessageServiceImpl(
			UsermessageServiceInter usermessageServiceImpl) {
		this.usermessageServiceImpl = usermessageServiceImpl;
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

		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			DataResult result = usermessageServiceImpl
					.createUsermessageSimpleQuery(query).search();
			result.runDictionary("0:未读,1:已读", "READSTATE");
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
	public Map<String, Object> editSubmit(Usermessage entity,
			HttpSession session) {

		try {
			entity = usermessageServiceImpl.editUsermessageEntity(entity,
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
	public Map<String, Object> addSubmit(Usermessage entity, HttpSession session) {
		try {
			entity = usermessageServiceImpl.insertUsermessageEntity(entity,
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
				usermessageServiceImpl.deleteUsermessageEntity(id,
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
		return ViewMode.getInstance().returnModelAndView(
				"doc/UsermessageResult");
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
				Usermessage entity = usermessageServiceImpl.getUsermessageEntity(ids);
				
				return ViewMode
						.getInstance()
						.putAttr("pageset", pageset)
						.putAttr("entity", entity)
						.putAttr("readusername", userServiceImpl.getUserEntity(entity.getReaduserid()).getName())
						.returnModelAndView("doc/UsermessageForm");
			}
			case (1): {// 新增
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.returnModelAndView("doc/UsermessageForm");
			}
			case (2): {// 修改
				Usermessage entity = usermessageServiceImpl.getUsermessageEntity(ids);
				return ViewMode
						.getInstance()
						.putAttr("pageset", pageset)
						.putAttr("entity", entity)
						.putAttr("readusername", userServiceImpl.getUserEntity(entity.getReaduserid()).getName())
						.returnModelAndView("doc/UsermessageForm");
			}
			default:
				break;
			}
			return ViewMode.getInstance().returnModelAndView(
					"doc/UsermessageForm");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("doc/UsermessageForm");
		}
	}
}
