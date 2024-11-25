package com.farm.wcp.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.ViewMode;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.wcp.know.service.KnowServiceInter;
import com.farm.wcp.util.ThemesUtil;
import com.farm.web.WebUtils;

/**
 * 文件
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/know")
@Controller
public class KnowController extends WebUtils {
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
	 * 进入创建知识
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(String typeid, String groupid, HttpSession session, HttpServletRequest request) {
		List<TypeBrief> types = farmDocTypeManagerImpl.getTypesForWriteDoc(getCurrentUser(session));

		// 回显分类
		DocEntire doce = new DocEntire();
		FarmDoctype doctype = farmDocTypeManagerImpl.getType(typeid);
		doce.setType(doctype);

		// 回显小组
		Doc doc = new Doc();
		doc.setDocgroupid(groupid);
		doce.setDoc(doc);

		return ViewMode.getInstance().putAttr("types", types).putAttr("doce", doce)
				.returnModelAndView(ThemesUtil.getThemePath() + "/know/creat");
	}

	/**
	 * 进入网页输入页面
	 * 
	 * @return
	 */
	@RequestMapping("/webdown")
	public ModelAndView downWeb(String typeid, String groupid, HttpSession session, HttpServletRequest request) {
		return ViewMode.getInstance().putAttr("typeid", typeid).putAttr("groupid", groupid)
				.returnModelAndView(ThemesUtil.getThemePath() + "/know/downWeb");
	}

	/**
	 * 提交并下载网页创建知识
	 * 
	 * @return
	 */
	@RequestMapping("/webDLoadDo")
	public ModelAndView downWebCommit(String url, String typeid, String groupid, HttpSession session,
			HttpServletRequest request) {
		try {
			DocEntire doc = KnowServiceImpl.getDocByWeb(url, getCurrentUser(session));
			if (typeid != null && !typeid.toUpperCase().trim().equals("NONE")
					&& !typeid.toUpperCase().trim().equals("")) {
				FarmDoctype doctype = farmDocTypeManagerImpl.getType(typeid);
				doc.setType(doctype);
			}
			if (groupid != null && !groupid.toUpperCase().trim().equals("NONE")
					&& !groupid.toUpperCase().trim().equals("")) {
				doc.getDoc().setDocgroupid(groupid);
			}
			List<TypeBrief> types = farmDocTypeManagerImpl.getTypesForWriteDoc(getCurrentUser(session));
			return ViewMode.getInstance().putAttr("doce", doc).putAttr("types", types)
					.returnModelAndView(ThemesUtil.getThemePath() + "/know/creat");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.toString())
					.returnModelAndView(ThemesUtil.getThemePath() + "/error");
		}
	}

	/**
	 * 进入修改知识
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/edit")
	public ModelAndView edit(String docid, HttpSession session) {
		DocEntire doce = null;
		try {
			doce = farmDocManagerImpl.getDoc(docid);
			// 解决kindedit中HTML脚本被转义的问题
			doce.getTexts()
					.setText1(doce.getTexts().getText1().replaceAll("&gt;", "&amp;gt;").replaceAll("&lt;", "&amp;lt;"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<TypeBrief> types = farmDocTypeManagerImpl.getTypesForWriteDoc(getCurrentUser(session));
		return ViewMode.getInstance().putAttr("doce", doce).putAttr("types", types)
				.returnModelAndView(ThemesUtil.getThemePath() + "/know/edit");
	}

	/**
	 * 提交创建知识
	 * 
	 * @return
	 */
	@RequestMapping("/addsubmit")
	public ModelAndView submitAdd(String docgroup, String knowtitle, String knowtype, String text, String knowtag,
			String writetype, String readtype, HttpSession session) {
		DocEntire doc = null;
		try {
			if ("0".equals(docgroup)) {
				docgroup = null;
			}
			doc = KnowServiceImpl.creatKnow(knowtitle, knowtype, text, knowtag, POP_TYPE.getEnum(writetype),
					POP_TYPE.getEnum(readtype), docgroup, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.toString())
					.returnModelAndView(ThemesUtil.getThemePath() + "/error");
		}
		if (doc.getAudit() != null) {
			return ViewMode.getInstance().returnRedirectUrl("/audit/tempdoc.do?auditid=" + doc.getAudit().getId());
		}
		return ViewMode.getInstance().returnRedirectUrl("/webdoc/view/Pub" + doc.getDoc().getId() + ".html");
	}

	/**
	 * 提交修改知识
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/editsubmit")
	public ModelAndView editCommit(String docid, String docgroup, String knowtitle, String knowtype, String text,
			String knowtag, String writetype, String readtype, String editNote, HttpSession session) {
		DocEntire doc = null;
		try {
			if ("0".equals(docgroup)) {
				docgroup = null;
			}
			// 高级权限用户修改
			if (farmDocOperateRightImpl.isDel(getCurrentUser(session), farmDocManagerImpl.getDocOnlyBean(docid))) {
				doc = KnowServiceImpl.editKnow(docid, knowtitle, knowtype, text, knowtag, POP_TYPE.getEnum(writetype),
						POP_TYPE.getEnum(readtype), docgroup, getCurrentUser(session), editNote);
				return ViewMode.getInstance().returnRedirectUrl("/webdoc/view/Pub" + docid + ".html");
			}
			// 低级权限用户修改
			{
				doc = KnowServiceImpl.editKnow(docid, text, knowtag, getCurrentUser(session), editNote);
			}
		} catch (Exception e) {
			List<TypeBrief> types = farmDocTypeManagerImpl.getTypesForWriteDoc(getCurrentUser(session));
			return ViewMode.getInstance().putAttr("doce", farmDocManagerImpl.getDoc(doc.getDoc().getId()))
					.putAttr("types", types).returnModelAndView(ThemesUtil.getThemePath() + "/know/edit");
		}
		if (doc.getAudit() != null) {
			return ViewMode.getInstance().returnRedirectUrl("/audit/tempdoc.do?auditid=" + doc.getAudit().getId());
		}
		return ViewMode.getInstance().returnRedirectUrl("/webdoc/view/Pub" + doc.getDoc().getId() + ".html");
	}
}
