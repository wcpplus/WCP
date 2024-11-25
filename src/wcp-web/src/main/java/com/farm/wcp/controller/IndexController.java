package com.farm.wcp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.page.ViewMode;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBSort;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.doc.domain.ex.DocBrief;
import com.farm.doc.domain.ex.GroupBrief;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocRunInfoInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.FarmtopServiceInter;
import com.farm.doc.server.WeburlServiceInter;
import com.farm.wcp.know.service.KnowServiceInter;
import com.farm.wcp.util.ThemesUtil;
import com.farm.wcp.util.ZxingTowDCode;
import com.farm.web.WebUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 文件
 * 
 * @author autoCode
 * 
 */
@RequestMapping("/home")
@Controller
public class IndexController extends WebUtils {
	@Resource
	private FarmDocRunInfoInter farmDocRunInfoImpl;
	@Resource
	private KnowServiceInter KnowServiceImpl;
	@Resource
	private FarmtopServiceInter farmTopServiceImpl;
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;
	@Resource
	private WeburlServiceInter weburlServiceImpl;

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("/Pubindex")
	public ModelAndView index(HttpSession session) {
		// 获取前五条置顶文档
		List<DocBrief> topdocs = farmDocRunInfoImpl.getPubTopDoc(5);
		// 加载热词
		List<DocBrief> hotdocs = farmDocRunInfoImpl.getPubHotDoc(10);
		// 最新知识
		List<DocBrief> newdocs = farmDocRunInfoImpl.getNewKnowList(6);
		// 分类
		List<TypeBrief> typesons = farmDocTypeManagerImpl.getPopTypesForReadDoc(getCurrentUser(session));
		// 获得最热小组
		List<GroupBrief> groups = farmDocgroupManagerImpl.getHotDocGroups(10, getCurrentUser(session));
		return ViewMode.getInstance().putAttr("hotdocs", hotdocs).putAttr("groups", groups).putAttr("docbriefs", newdocs)
				.putAttr("typesons", typesons).putAttr("newdocs", newdocs).putAttr("topDocList", topdocs)
				.returnModelAndView(ThemesUtil.getThemePath() + "/index");
	}

	/**
	 * 联系方式
	 * 
	 * @return
	 */
	@RequestMapping("/PubAbout")
	public ModelAndView contact(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView(ThemesUtil.getThemePath() + "/about");
	}

	/**
	 * 展示二维码
	 */
	@RequestMapping("/PubQRCode")
	public void QRCode(HttpServletRequest request, HttpServletResponse response) {
		OutputStream outp = null;
		try {
			String path = request.getContextPath();
			String text = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
					+ "/";
			int width = 300;
			int height = 300;
			// 二维码的图片格式
			String format = "gif";
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			// 内容所使用编码
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
			// 关于文件下载时采用文件流输出的方式处理：
			response.setContentType("application/x-download");
			// String filedownload = "想办法找到要提供下载的文件的物理路径＋文件名";
			String filedisplay = "给用户提供的下载文件名";
			filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
			outp = response.getOutputStream();
			ZxingTowDCode.writeToStream(bitMatrix, format, outp);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outp != null) {
				try {
					outp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取推荐服务
	 * 
	 * @return List<?>
	 */
	@RequestMapping("/PubrecommendServiceList")
	@ResponseBody
	public List<?> recommendServiceList() {
		List<Map<String, Object>> weburlList = weburlServiceImpl.getList();
		return ViewMode.returnListObjMode(weburlList);
	}

	/**
	 * 加载机构
	 * 
	 * @param session
	 * @return ModelAndView
	 */
	@RequestMapping("/PubFPloadOrgs")
	public ModelAndView userInfo(HttpSession session) {
		try {
			DataQuery query = DataQuery.getInstance(1, "ID,NAME,PARENTID", "alone_auth_organization");
			query.addRule(new DBRule("STATE", "1", "="));
			query.addSort(new DBSort("SORT", "ASC"));
			DataResult result = query.search();
			return ViewMode.getInstance().putAttr("result", result).returnModelAndView("web/user/commons/impl/pubOrg");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.toString()).returnModelAndView("");
		}
	}

	/**
	 * 按照名称查询知识（数据库查询）
	 * 
	 * @return
	 */
	@RequestMapping("/FPsearchKnow")
	@ResponseBody
	public Map<String, Object> FPsearchKnow(String knowtitle) {
		try {
			DataQuery query = DataQuery.getInstance(1, "TITLE,ID,DOMTYPE", "FARM_DOC");
			if (knowtitle != null) {
				query.addRule(new DBRule("TITLE", knowtitle, "like"));
			} else {
				query.addSort(new DBSort("ctime", "desc"));
			}
			query.addRule(new DBRule("STATE", "1", "="));
			query.addSqlRule(" and (READPOP='1' or READPOP='2')");
			DataResult result = query.search();
			List<Map<String, Object>> list = result.getResultList();
			return ViewMode.getInstance().putAttr("list", list).putAttr("size", list.size()).returnObjMode();
		} catch (Exception e) {
			return ViewMode.getInstance().putAttr("list", new ArrayList<Map<String, Object>>()).putAttr("size", 0)
					.returnObjMode();
		}
	}
}
