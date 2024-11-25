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
import com.farm.core.time.TimeTool;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDoctext;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.web.WebUtils;
import com.farm.web.easyui.EasyUiUtils;

/**
 * 文档管理
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/doc")
@Controller
public class ActionFarmDocQuery extends WebUtils {
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	@Resource
	private FarmDocOperateRightInter farmDocOperateRightImpl;
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;

	private static final Logger log = Logger.getLogger(ActionFarmDocQuery.class);

	/**
	 * 查询结果集合
	 *
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query, HttpServletRequest request) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			DataResult result = farmDocManagerImpl.createSimpleDocQuery(query).search();
			result.runDictionary("1:开放,0:禁用,2:待审核", "STATE");
			result.runDictionary("1:HTML,2:TXT,3:html站点,4:小组首页,5:资源文件", "DOMTYPE");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "WRITEPOP");
			result.runDictionary("1:分类,0:本人,2:小组,3:禁止", "READPOP");
			result.runformatTime("PUBTIME", "yyyy-MM-dd HH:mm:ss");
			return ViewMode.getInstance().putAttrs(EasyUiUtils.formatGridData(result)).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("doc/DocTResult");
	}

	/**
	 * 提交修改数据
	 *
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> editSubmit(Doc entity, HttpSession session, String content) {
		try {
			DocEntire doc = new DocEntire(entity);
			Doc dbdoc = farmDocManagerImpl.getDocOnlyBean(entity.getId());
			doc.getDoc().setCuser(dbdoc.getCuser());
			doc.getDoc().setCusername(dbdoc.getCusername());
			doc.setTexts(new FarmDoctext(content, TimeTool.getTimeDate14(), TimeTool.getTimeDate14(),
					getCurrentUser(session).getName(), getCurrentUser(session).getId(),
					getCurrentUser(session).getName(), getCurrentUser(session).getId(), "1"));
			//doc = farmDocManagerImpl.editDoc(doc, "系统管理员修改", getCurrentUser(session));
			//返回新的doc页面会使得连接丢失，不知道是不是对象太大导致无法获得连接
			 farmDocManagerImpl.editDoc(doc, "系统管理员修改", getCurrentUser(session));
			return ViewMode.getInstance().setOperate(OperateType.ADD).putAttr("entity", doc).returnObjMode();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return ViewMode.getInstance().setOperate(OperateType.ADD).setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 提交修改数据
	 *
	 * @return
	 */
	@RequestMapping("/FarmDocRighteditCommit")
	@ResponseBody
	public Map<String, Object> rightEditCommit(Doc entity, HttpSession session, String ids) {
		try {
			for (String id : parseIds(ids)) {
				entity = farmDocOperateRightImpl.editDocRight(id, POP_TYPE.getEnum(entity.getReadpop()),
						POP_TYPE.getEnum(entity.getWritepop()), getCurrentUser(session));
			}
			return ViewMode.getInstance().setOperate(OperateType.ADD).returnObjMode();

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
	public Map<String, Object> addSubmit(Doc entity, HttpSession session, String content, String doctypeId) {
		try {
			DocEntire doc = new DocEntire(entity);
			// 构造文档text
			doc.setTexts(new FarmDoctext(content, TimeTool.getTimeDate14(), TimeTool.getTimeDate14(),
					getCurrentUser(session).getName(), getCurrentUser(session).getId(),
					getCurrentUser(session).getName(), getCurrentUser(session).getId(), "1"));
			// 构造分类
			if (doctypeId != null && !doctypeId.isEmpty()) {
				doc.setType(farmDocTypeManagerImpl.getType(doctypeId));
			}

			if ("".equals(doc.getDoc().getImgid())) {
				doc.getDoc().setImgid(null);
			}
			doc = farmDocManagerImpl.createDoc(doc, getCurrentUser(session));
			return ViewMode.getInstance().putAttr("entity", doc).returnObjMode();
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
				farmDocManagerImpl.deleteDocNoPop(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * （修改文档权限）
	 *
	 * @return
	 */
	@RequestMapping("/FarmDocRightedit")
	public ModelAndView rightEdit(FarmDoctype doctype, String ids) {
		try {
			if (doctype != null && doctype.getId() != null && doctype.getId().trim().length() > 0) {
			}
			@SuppressWarnings("deprecation")
			DocEntire entity = farmDocManagerImpl.getDoc(ids);
			doctype = entity.getType();
			RequestMode pageset = new RequestMode();
			pageset.setOperateType(2);

			return ViewMode.getInstance().putAttr("entity", entity).putAttr("doctype", doctype)
					.putAttr("pageset", pageset).returnModelAndView("doc/DocTRightSetPage");
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnModelAndView("doc/DocTRightSetPage");
		}
	}

	/**
	 * 显示详细信息（修改或浏览时）
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/form")
	public ModelAndView view(RequestMode pageset, HttpServletRequest request, DataQuery query, HttpSession session,
			FarmDoctype doctype, String ids) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			query.setPagesize(100);
			query.addRule(new DBRule("b.userid", getCurrentUser(session).getId(), "="));
			DataResult result = farmDocgroupManagerImpl.createFarmDocgroupQueryJoinUser(query).search();
			String typeid = null;
			if (doctype != null && doctype.getId() != null && doctype.getId().trim().length() > 0) {
				typeid = doctype.getId();
			}
			switch (pageset.getOperateType()) {
			case (1): {// 新增
				doctype = farmDocTypeManagerImpl.getType(typeid);
				DocEntire entity = new DocEntire(new Doc());
				entity.getDoc()
						.setPubtime(TimeTool.getFormatTimeDate12(TimeTool.getTimeDate14(), "yyyy-MM-dd HH:mm:ss"));
				entity.getDoc().setAuthor(getCurrentUser(session).getName());
				return ViewMode.getInstance().putAttr("pageset", pageset).putAttr("entity", entity)
						.putAttr("doctype", doctype).putAttr("result", result).returnModelAndView("doc/DocTForm");
			}
			case (0): {// 展示
				DocEntire entity = farmDocManagerImpl.getDoc(ids);
				if (entity.getDoc().getPubtime() != null && entity.getDoc().getPubtime().trim().length() > 0) {
					entity.getDoc().setPubtime(
							TimeTool.getFormatTimeDate12(entity.getDoc().getPubtime(), "yyyy-MM-dd HH:mm:ss"));
				}
				doctype = entity.getType();
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmDocManagerImpl.getDoc(ids)).putAttr("result", result)
						.putAttr("doctype", doctype).returnModelAndView("doc/DocTForm");
			}
			case (2): {// 修改
				DocEntire entity = farmDocManagerImpl.getDoc(ids);
				if (entity.getDoc().getPubtime() != null && entity.getDoc().getPubtime().trim().length() > 0) {
					entity.getDoc().setPubtime(
							TimeTool.getFormatTimeDate12(entity.getDoc().getPubtime(), "yyyy-MM-dd HH:mm:ss"));
				}
				doctype = entity.getType();
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", farmDocManagerImpl.getDoc(ids)).putAttr("result", result)
						.putAttr("doctype", doctype).returnModelAndView("doc/DocTForm");
			}
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ViewMode.getInstance().setError(e + e.getMessage()).returnModelAndView("doc/DocTForm");
		}
		return ViewMode.getInstance().returnModelAndView("doc/DocTForm");
	}

	@RequestMapping("/toKindEditor")
	public ModelAndView toKindEditor(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("doc/DocTkindeditor");
	}

	@RequestMapping("/toManagerDocMsg")
	public ModelAndView toManagerDocMsg(HttpSession session, String ids) {
		return ViewMode.getInstance().putAttr("ids", ids).returnModelAndView("doc/DocTCommentSetPage");
	}
	
	/**
	 * 删除附件
	 * @return Map<String,Object>
	 */
	@RequestMapping("/delImg")
	@ResponseBody
	public Map<String, Object> delImg(String imgid) {
		try {
			farmDocManagerImpl.delImg(imgid);
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.toString());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}
	
	/**
	 * 移动文档到指定分类
	 * @param imgid
	 * @return
	 * Map<String,Object>
	 */
	@RequestMapping("/move2Type")
	@ResponseBody
	public Map<String, Object> move2Type(String docIds, String typeId) {
		try {
			farmDocManagerImpl.move2Type(docIds, typeId);
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.toString());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	
}
