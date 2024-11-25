package com.farm.wcp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.farm.authority.service.UserServiceInter;
import com.farm.core.page.ViewMode;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.doc.server.FarmDocOperateRightInter.POP_TYPE;
import com.farm.parameter.FarmParameterService;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmDocmessageManagerInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.wcp.util.ThemesUtil;
import com.farm.wcp.webfile.server.WcpWebFileManagerInter;
import com.farm.web.WebUtils;

/**
 * 资源文件
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/webfile")
@Controller
public class WebFileController extends WebUtils {
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;
	@Resource
	private FarmDocRunInfoInter farmDocRunInfoImpl;
	@Resource
	private FarmDocmessageManagerInter farmDocmessageManagerImpl;
	@Resource
	private FarmDocOperateRightInter farmDocOperateRightImpl;
	@Resource
	private UserServiceInter userServiceImpl;
	@Resource
	private WcpWebFileManagerInter wcpWebFileManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;

	@RequestMapping("/add")
	public ModelAndView creatWebFile(String typeid, String groupid, HttpSession session) {
		DocEntire doc = new DocEntire(new Doc());
		if (typeid != null && !typeid.toUpperCase().trim().equals("NONE") && !typeid.toUpperCase().trim().equals("")) {
			FarmDoctype doctype = farmDocTypeManagerImpl.getType(typeid);
			doc.setType(doctype);
		}
		if (groupid != null && !groupid.toUpperCase().trim().equals("NONE")
				&& !groupid.toUpperCase().trim().equals("")) {
			doc.getDoc().setDocgroupid(groupid);
		}
		List<TypeBrief> types = farmDocTypeManagerImpl.getTypesForWriteDoc(getCurrentUser(session));

		String filetypeString = FarmParameterService.getInstance().getParameter("config.doc.upload.types").toLowerCase()
				.replaceAll("，", ",");
		StringBuffer filetypestrplus = new StringBuffer();
		for (String node : parseIds(filetypeString)) {
			if (filetypestrplus.length() > 0) {
				filetypestrplus.append(";");
			}
			filetypestrplus.append("*." + node);
		}
		return ViewMode.getInstance().putAttr("types", types).putAttr("doc", doc).putAttr("filetypestr", filetypeString)
				.putAttr("filetypestrplus", filetypestrplus.toString())
				.returnModelAndView(ThemesUtil.getThemePath() + "/webfile/creat");
	}

	@RequestMapping("/edit")
	public ModelAndView editWebfile(String docId, HttpSession session, HttpServletRequest request) {
		DocEntire doc = null;
		try {
			doc = farmDocManagerImpl.getDoc(docId, getCurrentUser(session));
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.toString())
					.returnModelAndView(ThemesUtil.getThemePath() + "/error");
		}
		List<TypeBrief> types = farmDocTypeManagerImpl.getTypesForWriteDoc(getCurrentUser(session));

		String filetypeString = FarmParameterService.getInstance().getParameter("config.doc.upload.types").toLowerCase()
				.replaceAll("，", ",");
		StringBuffer filetypestrplus = new StringBuffer();
		for (String node : parseIds(filetypeString)) {
			if (filetypestrplus.length() > 0) {
				filetypestrplus.append(";");
			}
			filetypestrplus.append("*." + node);
		}
		return ViewMode.getInstance().putAttr("doce", doc).putAttr("types", types)
				.putAttr("filetypestr", filetypeString).putAttr("filetypestrplus", filetypestrplus.toString())
				.returnModelAndView(ThemesUtil.getThemePath() + "/webfile/edit");
	}

	@RequestMapping("/editCommit")
	public ModelAndView editCommit(String docid, String fileId, String knowtype, String knowtitle, String knowtag,
			String docgroup, String writetype, String readtype, String text, String editnote, HttpSession session) {
		DocEntire doc = null;
		try {
			if (docgroup.equals("0")) {
				docgroup = null;
			}
			doc = wcpWebFileManagerImpl.editWebFile(docid, Arrays.asList(fileId.trim().split(",")), knowtype, knowtitle,
					knowtag, docgroup, text, POP_TYPE.getEnum(writetype), POP_TYPE.getEnum(readtype), editnote,
					getCurrentUser(session));
			if (doc.getAudit() != null) {
				return ViewMode.getInstance().returnRedirectUrl("/audit/tempdoc.do?auditid=" + doc.getAudit().getId());
			}
			return ViewMode.getInstance().returnRedirectUrl("/webdoc/view/Pub" + doc.getDoc().getId() + ".html");
		} catch (Exception e) {
			e.printStackTrace();
			return ViewMode.getInstance().setError(e.toString())
					.returnModelAndView(ThemesUtil.getThemePath() + "/error");
		}
	}

	@RequestMapping("/addsubmit")
	public ModelAndView creatWebFileSubmit(String fileId, String creattype, String knowtype, String knowtitle,
			String knowtag, String text, String docgroup, String writetype, String readtype, HttpSession session) {
		try {
			List<String> fileids = Arrays.asList(fileId.trim().split(","));
			if (creattype != null && creattype.equals("on")) {
				/** 创建为独立知识 **/
				Map<String, String> doclinks = new HashMap<String, String>();
				DocEntire doc = null;
				for (String fileid : fileids) {
					List<String> fileidlist = new ArrayList<>();
					fileidlist.add(fileid);
					if (docgroup != null && docgroup.equals("0")) {
						docgroup = null;
					}
					String fileName = farmFileManagerImpl.getFile(fileid).getName();
					doc = wcpWebFileManagerImpl.creatWebFile(fileidlist, knowtype, fileName, knowtag, docgroup, text,
							POP_TYPE.getEnum(writetype), POP_TYPE.getEnum(readtype), getCurrentUser(session));
					if (doc.getAudit() != null) {
						doclinks.put(fileName, "/audit/tempdoc.do?auditid=" + doc.getAudit().getId());
					} else {
						doclinks.put(fileName, "/webdoc/view/Pub" + doc.getDoc().getId() + ".html");
					}
				}
				if (doc.getAudit() != null) {
					return ViewMode.getInstance().putAttr("MESSAGE", fileids.size() + "个资源文件创建成功，但是需要审核后才能够被他人访问！")
							.returnModelAndView(ThemesUtil.getThemePath() + "/message");
				}
				return ViewMode.getInstance().putAttr("MESSAGE", fileids.size() + "个资源文件创建成功！")
						.putAttr("LINKS", doclinks).returnModelAndView(ThemesUtil.getThemePath() + "/message");
			} else {
				/** 创建为一个知识 **/
				DocEntire doc = null;
				if (docgroup.equals("0")) {
					docgroup = null;
				}
				doc = wcpWebFileManagerImpl.creatWebFile(fileids, knowtype, knowtitle, knowtag, docgroup, text,
						POP_TYPE.getEnum(writetype), POP_TYPE.getEnum(readtype), getCurrentUser(session));
				if (doc.getAudit() != null) {
					return ViewMode.getInstance()
							.returnRedirectUrl("/audit/tempdoc.do?auditid=" + doc.getAudit().getId());
				}
				return ViewMode.getInstance().returnRedirectUrl("/webdoc/view/Pub" + doc.getDoc().getId() + ".html");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ViewMode.getInstance().setError(e.toString())
					.returnModelAndView(ThemesUtil.getThemePath() + "/error");
		}
	}
}
