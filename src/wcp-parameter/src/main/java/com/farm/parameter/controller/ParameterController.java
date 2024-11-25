package com.farm.parameter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.parameter.domain.AloneParameter;
import com.farm.parameter.service.ParameterServiceInter;
import com.farm.parameter.service.impl.ConstantVarService;
import com.farm.parameter.service.impl.PropertiesFileService;
import com.farm.core.page.RequestMode;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 系统参数Action
 * 
 * @author zhang_hc
 * @time 2012-8-31 上午11:47:25
 * @author wangdong
 * @time 2015-7-03 上午10:19:25
 */
@RequestMapping("/parameter")
@Controller
public class ParameterController extends WebUtils {

	private static final Logger log = Logger
			.getLogger(ParameterController.class);

	@Resource
	ParameterServiceInter parameterServiceImpl;

	/**
	 * 查询结果集合
	 *
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query,
			HttpServletRequest request) {
		query = EasyUiUtils.formatGridQuery(request, query);
		try {
			query = DataQuery.init(//
					query, //
					"Alone_Parameter a", //
					"id,domain,name,pkey,pvalue,vtype,comments");

			query.addSort(new DBSort("a.utime", "desc"));// 按最后修改事件排序
			DataResult result = query.search();
			// 状态转义
			HashMap<String, String> transMap = new HashMap<String, String>();
			transMap.put("null", "");
			result.runDictionary(transMap, "COMMENTS");
			return ViewMode.getInstance()
					.putAttrs(EasyUiUtils.formatGridData(result))
					.returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}

	/**进入参数定义界面
	 * @param session
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView(
				"parameter/pAloneParameterLayout");
	}

	/**进入参数配置界面
	 * @return
	 */
	@RequestMapping("/editlist")
	public ModelAndView showPara() {
		List<Entry<String, String>> filePropertys = PropertiesFileService
				.getEntrys();
		List<Entry<String, String>> constantPropertys = ConstantVarService
				.getEntrys();
		return ViewMode.getInstance().putAttr("filePropertys", filePropertys)
				.putAttr("constantPropertys", constantPropertys)
				.returnModelAndView("parameter/pAloneParameterConf");
	}

	@RequestMapping("/userelist")
	public ModelAndView showUserPara() {
		return ViewMode.getInstance().returnModelAndView(
				"parameter/pAloneParameterConfForUser");
	}

	/**
	 * @param query
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryForEU")
	@ResponseBody
	public List<Map<String, Object>> queryallForEasyUi(DataQuery query,
			HttpServletRequest request) {
		try {
			List<Map<String, Object>> propertys = EasyUiUtils
					.formatPropertygridData(
							parameterServiceImpl.getAllParameters(), "NAME",
							"PVALUE", "DOMAIN", "VTYPE", "RULES", "ID");
			return propertys;
		} catch (Exception e) {
			log.error(e);
			return new ArrayList<Map<String, Object>>();
		}
	}

	@RequestMapping("/userqueryForEU")
	@ResponseBody
	public List<Map<String, Object>> userQueryallForEasyUi(DataQuery query,
			HttpServletRequest request, HttpSession session) {
		try {
			List<Map<String, Object>> propertys = EasyUiUtils
					.formatPropertygridData(
							parameterServiceImpl
									.getUserParameters(getCurrentUser(session)
											.getId()), "NAME", "PVALUE",
							"DOMAIN", "VTYPE", "RULES", "ID");
			return propertys;
		} catch (Exception e) {
			log.error(e);
			return new ArrayList<Map<String, Object>>();
		}
	}

	/**
	 * 提交修改数据
	 *
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(AloneParameter entity,
			HttpSession session) {
		try {
			entity = parameterServiceImpl.editEntity(entity,
					getCurrentUser(session));
			return ViewMode.getInstance().putAttr("entity", entity)
					.returnObjMode();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}

	/**
	 * 提交新增数据
	 *
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addSubmit(AloneParameter entity,
			HttpSession session) {
		try {
			entity = parameterServiceImpl.insertEntity(entity,
					entity.getDomain(), getCurrentUser(session));
			return ViewMode.getInstance().putAttr("entity", entity)
					.returnObjMode();
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
				parameterServiceImpl.deleteEntity(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}

	/**
	 * 修改系统参数的值
	 *
	 * @return
	 */
	@RequestMapping("/editEU")
	@ResponseBody
	public Map<String, Object> editSubmitByPValue(String ids,
			HttpSession session) {
		try {
			String[] paraArrays = ids.split("&2582&");
			for (int i = 0; i < paraArrays.length; i++) {
				String[] paraEntry = paraArrays[i].split("&2581&");
				String id = paraEntry[0];
				String value = paraEntry[1];
				parameterServiceImpl.setValue(parameterServiceImpl
						.getEntity(id).getPkey(), value,
						getCurrentUser(session));
			}
		} catch (Exception e) {
			log.error(e);
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 修改系统参数的值
	 *
	 * @return
	 */
	@RequestMapping("/editUserEU")
	@ResponseBody
	public Map<String, Object> editSubmitByUserPValue(String ids,
			HttpSession session) {
		try {
			String[] paraArrays = ids.split("&2582&");
			for (int i = 0; i < paraArrays.length; i++) {
				String[] paraEntry = paraArrays[i].split("&2581&");
				String id = paraEntry[0];
				String value = paraEntry[1];
				parameterServiceImpl.setUserValue(parameterServiceImpl
						.getEntity(id).getPkey(), value,
						getCurrentUser(session));
			}
		} catch (Exception e) {
			log.error(e);
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
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
						.returnModelAndView("parameter/pAloneParameterEntity");
			}
			case (0): {// 展示
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", parameterServiceImpl.getEntity(ids))
						.returnModelAndView("parameter/pAloneParameterEntity");
			}
			case (2): {// 修改
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", parameterServiceImpl.getEntity(ids))
						.returnModelAndView("parameter/pAloneParameterEntity");
			}
			default:
				break;
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("parameter/pAloneParameterEntity");
		}
		return ViewMode.getInstance().returnModelAndView(
				"parameter/pAloneParameterEntity");
	}
}
