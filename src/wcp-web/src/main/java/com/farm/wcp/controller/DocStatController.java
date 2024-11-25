package com.farm.wcp.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.authority.domain.User;
import com.farm.authority.service.UserServiceInter;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.wcp.know.service.KnowServiceInter;
import com.farm.wcp.util.ThemesUtil;
import com.farm.web.WebUtils;

/**
 * 工作小组
 * 
 * @author wangdong
 *
 */
@RequestMapping("/webstat")
@Controller
public class DocStatController extends WebUtils {
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
	private UserServiceInter userServiceImpl;

	/**
	 * 统计首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/PubHome", method = RequestMethod.GET)
	public ModelAndView show(Integer pagenum, HttpSession session, HttpServletRequest request) throws Exception {
		ViewMode mode = ViewMode.getInstance();
		// 好评用户排名
		DataResult goodUsers = farmDocRunInfoImpl.getStatGoodUsers(10);
		mode.putAttr("goodUsers", goodUsers);
		// 好评小组排名
		DataResult goodGroups = farmDocRunInfoImpl.getStatGoodGroups(10);
		mode.putAttr("goodGroups", goodGroups);
		// 用户发布排名
		DataResult manyUsers = farmDocRunInfoImpl.getStatMostUsers(10);
		mode.putAttr("manyUsers", manyUsers);
		// 好评文章排名
		DataResult goodDocs = farmDocRunInfoImpl.getStatGoodDocs(10);
		mode.putAttr("goodDocs", goodDocs);
		// 待完善文章排名
		DataResult badDocs = farmDocRunInfoImpl.getStatBadDocs(10);
		mode.putAttr("badDocs", badDocs);
		// User使用量分析
		LoginUser luser = getCurrentUser(session);
		if (luser != null) {
			User user = userServiceImpl.getUserEntity(luser.getId());
			DataResult users = farmDocRunInfoImpl.getStatUser(user);
			if (user.getImgid() != null && user.getImgid().trim().length() > 0) {
				mode.putAttr("photourl", farmFileManagerImpl.getFileURL(user.getImgid()));
			}
			mode.putAttr("users", users);
		}
		return mode.returnModelAndView(ThemesUtil.getThemePath()+"/statis/heros");
	}

	@RequestMapping("/PubStatAll")
	@ResponseBody
	public Map<String, Object> statAll(HttpSession session, String id) {
		ViewMode mode = ViewMode.getInstance();
		// wcp使用量分析
		Map<String, Object> nums;
		try {
			nums = farmDocRunInfoImpl.getStatNumForDay();
			mode.putAttr("nums", nums);
		} catch (Exception e) {
			return mode.returnObjMode();
		}
		return mode.returnObjMode();
	}
}
