package com.farm.wcp.group.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.FarmDocgroup;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.exception.CanNoWriteException;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.util.web.FarmFormatUnits;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 文件
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/group")
@Controller
public class GroupController extends WebUtils {
	private static final Logger log = Logger.getLogger(GroupController.class);
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;

	/**
	 * 查询我的小组文档
	 * 
	 * @return
	 */
	@RequestMapping("/searchGroupDoc")
	@ResponseBody
	public Map<String, Object> searchGroupDoc(String searchDocKey, DataQuery query, HttpSession session) {
		try {
			if (searchDocKey != null && searchDocKey.trim().length() > 0) {
				query.addRule(new DBRule("a.TITLE", searchDocKey, "like"));
			}
			DataResult result = farmDocgroupManagerImpl.createUserGroupDocQuery(query, getCurrentUser(session).getId())
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				if (node.get("GROUPIMG") != null) {
					node.put("GROUPIMG", farmFileManagerImpl.getFileURL(node.get("GROUPIMG").toString()));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 初始化小组首页数据
	 * 
	 * @return
	 */
	@RequestMapping("/group")
	public ModelAndView loadGroupHome(String gourpid) {
		// 获取小组首页
		FarmDocgroup group = farmDocgroupManagerImpl.getFarmDocgroupEntity(gourpid);
		DocEntire doc = farmDocManagerImpl.getDoc(group.getHomedocid());
		return ViewMode.getInstance().returnModelAndView("");
	}

	/**
	 * 提交小组首页修改
	 * 
	 * @return
	 */
	@RequestMapping("/editCommit")
	public ModelAndView editGroupHomeCommit(DocEntire doc, String editNote, String text, HttpSession session) {
		doc = farmDocManagerImpl.getDoc(doc.getDoc().getId());
		doc.getTexts().setText1(text);
		try {
			farmDocManagerImpl.editDocByUser(doc, editNote, getCurrentUser(session));
		} catch (CanNoWriteException e) {
			return ViewMode.getInstance().setError(e + e.getMessage()).returnModelAndView("");
		}
		return ViewMode.getInstance().returnModelAndView("");
	}

	/**
	 * 编辑小组首页
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView editGroupHome(String groupid) {
		// 获取小组首页
		FarmDocgroup group = farmDocgroupManagerImpl.getFarmDocgroupEntity(groupid);
		DocEntire doc = farmDocManagerImpl.getDoc(group.getHomedocid());
		return ViewMode.getInstance().putAttr("group", group).putAttr("doc", doc).returnModelAndView("");
	}

	/**
	 * 首页我的小组动态
	 * 
	 * @return
	 */
	@RequestMapping("/myGroupDoc")
	@ResponseBody
	public Map<String, Object> myGroupDoc(HttpSession session) {
		DataResult result = null;
		try {
			result = farmDocgroupManagerImpl.createUserGroupDocQuery(new DataQuery(), getCurrentUser(session).getId())
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				if (node.get("GROUPIMG") != null) {
					node.put("GROUPIMG", farmFileManagerImpl.getFileURL(node.get("GROUPIMG").toString()));
				}
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttrs(EasyUiUtils.formatGridData(result)).returnObjMode();
	}

	/**
	 * 加载用户小组动态
	 * 
	 * @return
	 */
	@RequestMapping("/userGroupDoc")
	@ResponseBody
	public Map<String, Object> userGroupDoc(HttpServletRequest request, DataQuery query, String userId) {
		DataResult result = null;
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			query.setPagesize(3);
			result = farmDocgroupManagerImpl.createFarmDocgroupQueryJoinUser(query)
					.addRule(new DBRule("a.PSTATE", "1", "=")).addRule(new DBRule("b.PSTATE", "1", "="))
					.addRule(new DBRule("b.USERID", userId, "=")).addSort(new DBSort("SHOWSORT,a.CTIME", "ASC"))
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				if (node.get("GROUPIMG") != null) {
					node.put("GROUPIMG", farmFileManagerImpl.getFileURL(node.get("GROUPIMG").toString()));
				}
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result).returnObjMode();
	}

	/**
	 * 首页公共小组
	 * 
	 * @return
	 */
	@RequestMapping("/pubGroup")
	@ResponseBody
	public Map<String, Object> loadHomePubGroup(HttpServletRequest request, HttpSession session) {
		DataResult result = null;
		try {
			DataQuery query = EasyUiUtils.formatGridQuery(request, new DataQuery());

			query.setPagesize(4);
			if (getCurrentUser(session) != null) {
				query = farmDocgroupManagerImpl.createFarmDocgroupQueryNuContainUser(query,
						getCurrentUser(session).getId());
				result = query.addRule(new DBRule("a.PSTATE", "1", "=")).search();
			} else {
				query = farmDocgroupManagerImpl.createFarmDocgroupQuery(query);
				result = query.addRule(new DBRule("PSTATE", "1", "=")).search();

			}
			query.addSort(new DBSort("USERNUM", "desc"));
			for (Map<String, Object> node : result.getResultList()) {
				if (node.get("GROUPIMG") != null) {
					node.put("GROUPIMG", farmFileManagerImpl.getFileURL(node.get("GROUPIMG").toString()));
				}
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result.getResultList()).returnObjMode();
	}

	/**
	 * 加载小组的最新文档
	 * 
	 * @return
	 */
	@RequestMapping("/userGroupDocNew")
	@ResponseBody
	public Map<String, Object> loadGroupNewDoc(String groupid, HttpSession session) {
		DataResult result = null;
		try {
			result = farmDocgroupManagerImpl.getGroupNewDocQuery(new DataQuery(), groupid, getCurrentUser(session))
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				node.put("PUBTIME", FarmFormatUnits.getFormateTime(node.get("PUBTIME").toString(), true));
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result.getResultList()).returnObjMode();
	}

	/**
	 * 加载小组的优质文档
	 * 
	 * @return
	 */
	@RequestMapping("/userGroupDocGood")
	@ResponseBody
	public Map<String, Object> loadGroupGoodDoc(String groupid, HttpSession session) {
		DataResult result = null;
		try {
			result = farmDocgroupManagerImpl.getGroupGoodDocQuery(new DataQuery(), groupid, getCurrentUser(session))
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				node.put("PUBTIME", FarmFormatUnits.getFormateTime(node.get("PUBTIME").toString(), true));
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result.getResultList()).returnObjMode();
	}

	/**
	 * 加载小组的待改善文档
	 * 
	 * @return
	 */
	@RequestMapping("/userGroupDocBad")
	@ResponseBody
	public Map<String, Object> loadGroupBadDoc(String groupid, HttpSession session) {
		DataResult result = null;
		try {
			result = farmDocgroupManagerImpl.getGroupBadDocQuery(new DataQuery(), groupid, getCurrentUser(session))
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				node.put("PUBTIME", FarmFormatUnits.getFormateTime(node.get("PUBTIME").toString(), true));
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result.getResultList()).returnObjMode();
	}

	/**
	 * 加载小组的最热文档
	 * 
	 * @return
	 */
	@RequestMapping("/userGroupDocHot")
	@ResponseBody
	public Map<String, Object> loadGroupHotDoc(String groupid, HttpSession session) {
		DataResult result = null;
		try {
			result = farmDocgroupManagerImpl.getGroupHotDocQuery(new DataQuery(), groupid, getCurrentUser(session))
					.search();
			for (Map<String, Object> node : result.getResultList()) {
				node.put("PUBTIME", FarmFormatUnits.getFormateTime(node.get("PUBTIME").toString(), true));
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result.getResultList()).returnObjMode();
	}

	/**
	 * 小组首页显示
	 * 
	 * @return
	 */
	@RequestMapping("/home")
	public ModelAndView groupHomeShow(HttpSession session, String gourpid) {
		try {
			farmDocgroupManagerImpl.setGroupHomeShow(gourpid, getCurrentUser(session).getId());
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage()).returnModelAndView("");
		}
		return ViewMode.getInstance().returnModelAndView("");
	}

	/**
	 * 小组首页隐藏
	 * 
	 * @return
	 */
	@RequestMapping("/hideHome")
	@ResponseBody
	public Map<String, Object> hideHome(String groupId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.setGroupHomeHide(groupId, getCurrentUser(session).getId());
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 小组显示排序上移
	 * 
	 * @return
	 */
	@RequestMapping("/sortUp")
	@ResponseBody
	public Map<String, Object> groupSortUp(String groupId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.setGroupSortUp(groupId, getCurrentUser(session).getId());
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 设置为小组管理员
	 * 
	 * @return
	 */
	@RequestMapping("/setAdmin")
	@ResponseBody
	public Map<String, Object> groupSetAdmin(String groupUserId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.setAdminForGroup(groupUserId, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 设置小组编辑权限
	 * 
	 * @return
	 */
	@RequestMapping("/setEditor")
	@ResponseBody
	public Map<String, Object> groupSetEditor(String groupUserId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.setEditorForGroup(groupUserId, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 去除小组编辑权限
	 * 
	 * @return
	 */

	@RequestMapping("/wipeEditor")
	@ResponseBody
	public Map<String, Object> groupWipeEditor(String groupUserId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.wipeEditorForGroup(groupUserId, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 同意加入申请
	 * 
	 * @return
	 */
	@RequestMapping("/agreeApply")
	@ResponseBody
	public Map<String, Object> groupAgreeApply(String groupUserId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.agreeJoinApply(groupUserId, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 拒绝加入申请
	 * 
	 * @return
	 */
	@RequestMapping("/refuseApply")
	@ResponseBody
	public Map<String, Object> groupRefuseApply(String groupUserId, HttpSession session) {
		try {
			farmDocgroupManagerImpl.refuseJoinApply(groupUserId, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 加载我的小组
	 * 
	 * @return
	 */
	@RequestMapping("/leave")
	@ResponseBody
	public Map<String, Object> loadGroups(HttpServletRequest request, HttpSession session) {
		DataQuery query;
		DataResult result;
		try {
			query = EasyUiUtils.formatGridQuery(request, new DataQuery());
			query.setPagesize(3);
			result = farmDocgroupManagerImpl.createFarmDocgroupQueryJoinUser(query)
					.addRule(new DBRule("a.PSTATE", "1", "=")).addRule(new DBRule("b.PSTATE", "1", "="))
					.addRule(new DBRule("b.USERID", getCurrentUser(session).getId(), "="))
					.addSort(new DBSort("SHOWSORT,a.CTIME", "ASC")).search();
			for (Map<String, Object> node : result.getResultList()) {
				if (node.get("GROUPIMG") != null) {
					node.put("GROUPIMG", farmFileManagerImpl.getFileURL(node.get("GROUPIMG").toString()));
				}
			}
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage()).returnObjMode();
		}
		return ViewMode.getInstance().putAttr("result", result).returnObjMode();
	}

	

	

	
}
