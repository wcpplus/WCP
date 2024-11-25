package com.farm.authority.controller;

import com.farm.authority.domain.Organization;
import com.farm.authority.domain.Post;
import com.farm.authority.domain.User;
import com.farm.authority.service.UserServiceInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.OperateType;
import com.farm.core.page.RequestMode;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;

/* *
 *功能：用户控制层
 *详细：
 *
 * 版本：v0.1
 * @author zhaonaixia
 * @time 2015-6-26 上午10:19:25
 * 说明：
 */
@RequestMapping("/user")
@Controller
public class WebUserController extends WebUtils {
	private final static Logger log = Logger.getLogger(WebUserController.class);
	@Resource
	UserServiceInter userServiceImpl;

	public UserServiceInter getUserServiceImpl() {
		return userServiceImpl;
	}

	public void setUserServiceImpl(UserServiceInter userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	/**
	 * 查询结果集合
	 * 
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(
			@ModelAttribute("query") DataQuery query, HttpServletRequest request,HttpSession session) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			if (query.getQueryRule().size() <= 0) {
				query.addRule(new DBRule("a.STATE", "1", "="));
			}
			DataResult result = userServiceImpl.createUserSimpleQuery(query, getCurrentUser(session))
					.search();
			result.runDictionary("0:禁用,1:可用,2:删除", "STATE");
			result.runDictionary("1:系统用户,2:其他,3:超级用户", "TYPE");
			result.runformatTime("LOGINTIME", "yyyy-MM-dd HH:mm:ss");
			return ViewMode.getInstance()
					.putAttrs(EasyUiUtils.formatGridData(result))
					.returnObjMode();

		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage())
					.returnObjMode();
		}
	}
	/**进入用户管理界面
	 * @param session
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance()
				.returnModelAndView("authority/UserResult");	
	}
	
	/**
	 * 跳转到修改密码页面
	 * 
	 * @return
	 */
	@RequestMapping("/updatePassword")
	public ModelAndView forSend(HttpSession session) {
		return ViewMode.getInstance()
				.returnModelAndView("frame/password");	
	}
	
	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping("LoginUser_PassWordUpdata")
	@ResponseBody
	public Object editPassword(HttpSession session,String passwordl,String passwordn1) {
		try {
			if (!userServiceImpl.editLoginPassword(getCurrentUser(session).getLoginname(),
					passwordl, passwordn1)) {
				throw new RuntimeException("密码修改失败！");
			}
			return ViewMode.getInstance()
					.returnObjMode();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance()
					.setError(e.getMessage())
					.returnObjMode();
		}
	}
	
	@RequestMapping("/organization")
	public ModelAndView userOrgTree(HttpSession session,String ids) {
		return ViewMode.getInstance()
				.putAttr("ids", ids)
				.returnModelAndView("authority/UserorgChooseTreeWin");
	}

	 /**
	 * 查询组织机构用户结果集合
	 *
	 * @return
	 */
	@RequestMapping("/orgUserQuery")
	@ResponseBody
	 public Map<String,Object> queryOrgUser(
			 @ModelAttribute("query") DataQuery query, HttpServletRequest request,String ids) {
			 try {
			 query = EasyUiUtils.formatGridQuery(request, query);
			 query.addRule(new DBRule("ORG.ID", ids, "="));
			 DataResult result = userServiceImpl.createUserPostQuery(query).search();
			 result.runDictionary("0:禁用,1:可用,2:删除", "USERSTATE");
			 result.runDictionary("1:标准岗位,2:临时岗位", "TYPE");
			 result.runDictionary("0:禁用,1:可用,2:删除", "a.STATE");
			 result.runformatTime("LOGINTIME", "yyyy-MM-dd HH:mm:ss");
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
	 * 提交新增数据
	 *
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addSubmit(User user, HttpSession session, String orgId, String postIds) {
		try {
			User entity = userServiceImpl.insertUserEntity(user,
					getCurrentUser(session), orgId, postIds);
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.putAttr("entity", entity).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.setError(e.getMessage()).returnObjMode();
		}
		
		
	}
	
	/**
	 * 提交编辑数据
	 *
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(User user, HttpSession session, String orgId, String postIds) {
		try {
			User entity = userServiceImpl.editUserEntity(user,
					getCurrentUser(session), orgId, postIds);
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.putAttr("entity", entity).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.ADD)
					.setError(e.getMessage()).returnObjMode();
		}
		
		
	}
	
	/**
	 * 删除选中单条数据
	 *
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public ModelAndView delSubmit(String ids, HttpSession session) {
		try {
			userServiceImpl.deleteUserEntity(ids,
					getCurrentUser(session));
			return ViewMode.getInstance()
					.returnModelAndView("authority/UserResult");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance()
					.setError(e.getMessage())
					.returnModelAndView("authority/UserResult");
		}
	}

	 /**
	 * 密码初始化
	 *
	 * @return
	 */
	@RequestMapping("/init")
	@ResponseBody
	 public ModelAndView initPassWord(String ids,HttpSession session) {
		 try {
			userServiceImpl.initDefaultPassWord(ids, getCurrentUser(session));
			return ViewMode.getInstance()
					.returnModelAndView("authority/UserResult");
		 } catch (Exception e) {
			 return null;
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
						.returnModelAndView("authority/UserForm");
			}
			case (0): {// 展示
				User entity = userServiceImpl.getUserEntity(ids);
				List<Post> posts = userServiceImpl.getUserPosts(ids);
				Organization organization = userServiceImpl
						.getUserOrganization(ids);
				return ViewMode.getInstance().putAttr("entity", entity)
						.putAttr("posts", posts).putAttr("pageset", pageset)
						.putAttr("organization", organization)
						.returnModelAndView("authority/UserForm");
			}
			case (2): {// 修改
				User entity = userServiceImpl.getUserEntity(ids);
				Organization org = userServiceImpl.getOrg(entity.getId());
				List<Post> postList = userServiceImpl.getPost(entity.getId());
				String postIds = "";
				for(int i = 0; i < postList.size(); i ++){
					postIds += postList.get(i).getId();
					if(i < postList.size() - 1){
						postIds += ",";
					}
				}
				return ViewMode.getInstance()
						.putAttr("pageset", pageset)
						.putAttr("entity", entity)
						.putAttr("orgId", org==null?null:org.getId())
						.putAttr("postIds", postIds)
						.returnModelAndView("authority/UserForm");
			}
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e + e.getMessage())
					.returnModelAndView("authority/UserForm");
		}
		return ViewMode.getInstance().returnModelAndView("authority/UserForm");
	}

	private static final long serialVersionUID = 1L;
}
