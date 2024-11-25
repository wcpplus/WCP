package com.farm.wcp.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.wcp.know.service.KnowServiceInter;
import com.farm.wcp.util.ThemesUtil;
import com.farm.web.WebUtils;

@RequestMapping("/webtype")
@Controller
public class DocTypeController extends WebUtils {
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	@Resource
	private FarmDocRunInfoInter farmDocRunInfoImpl;
	@Resource
	private KnowServiceInter KnowServiceImpl;
	@Resource
	private FarmDocmessageManagerInter farmDocmessageManagerImpl;
	@Resource
	private FarmDocOperateRightInter farmDocOperateRightImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;

	/**
	 * 分类首页
	 * 
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/view{typeid}/Pub{pagesize}", method = RequestMethod.GET)
	public ModelAndView types(@PathVariable("typeid") String typeid, HttpSession session, HttpServletRequest request,
			@PathVariable("pagesize") Integer pagenum) throws Exception {
		ViewMode mode = ViewMode.getInstance();
		if (typeid == null || typeid.isEmpty()) {
			typeid = "NONE";
		} else {
			mode.putAttr("type", farmDocTypeManagerImpl.getType(typeid));
		}
		if (pagenum == null) {
			pagenum = 1;
		}
		LoginUser user = getCurrentUser(session);
		mode.putAttr("typeid", typeid);
		List<FarmDoctype> typepath =farmDocTypeManagerImpl.getTypeAllParent(typeid);
		List<TypeBrief> types = farmDocTypeManagerImpl.getPopTypesForReadDoc(getCurrentUser(session));
		List<TypeBrief> typesons = farmDocTypeManagerImpl.getTypeInfos(getCurrentUser(session), typeid);
		DataResult docs = farmDocTypeManagerImpl.getTypeDocs(user, typeid, 10, pagenum);
		return mode.putAttr("types", types).putAttr("typesons", typesons).putAttr("typepath", typepath).putAttr("docs", docs)
				.returnModelAndView(ThemesUtil.getThemePath()+"/type/type");
	}
}
