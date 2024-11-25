package com.farm.wcp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.page.ViewMode;
import com.farm.core.sql.result.DataResult;
import com.farm.core.time.TimeTool;
import com.farm.doc.server.FarmDocIndexInter;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.util.HtmlUtils;
import com.farm.llm.utils.LlmMessage;
import com.farm.llm.utils.LlmMessage.M_TYPE;
import com.farm.parameter.FarmParameterService;
import com.farm.tex.AiQuestor;
import com.farm.tex.domainex.AiQuestorMessage;
import com.farm.tex.domainex.AiQuestorMessage.MessageState;
import com.farm.wcp.util.ImHistory;
import com.farm.wcp.util.ThemesUtil;
import com.farm.web.WebUtils;

/**
 * 智能对话服务
 * 
 * @author wangdogn
 *
 */
@RequestMapping("/aiweb")
@Controller
public class AIController extends WebUtils {

	private static final Logger log = Logger.getLogger(AIController.class);
	@Resource
	private FarmDocIndexInter farmDocIndexManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private FarmDocManagerInter farmDocManagerImpl;

	public static String getThemePath() {
		return FarmParameterService.getInstance().getParameter("config.sys.web.themes.path");
	}

	/**
	 * 独立的AI对话页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/PubAiChat", method = RequestMethod.GET)
	public ModelAndView aiChat(String docid, String actiontype, HttpSession session, HttpServletRequest request)
			throws Exception {
		try {
			return ViewMode.getInstance().returnModelAndView("web-simple/aichat/aiChat");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.toString())
					.returnModelAndView(ThemesUtil.getThemePath() + "/error");
		}
	}

	/**
	 * 发送消息，并接收回复
	 */
	@RequestMapping("/PubSendmgs")
	@ResponseBody
	public Map<String, Object> StatCommitTestDate(String message, HttpSession session, HttpServletRequest request) {
		String backmessage = null;
		String talkSessionId = AiQuestor.getTalkSessionId(session, request);
		if (FarmParameterService.getInstance().getParameterBoolean("config.imbar.login.force")
				&& getCurrentUser(session) == null) {
			// 未登录返回必须登录提示信息
			return ViewMode.getInstance().putAttr("msg", "<div class='farm_wcp_ai_msg' >请&nbsp;<b>登录</b>&nbsp;账户</div>")
					.returnObjMode();
		}
		// 向会话缓存中添加历史记录
		ImHistory.addMessage(session, message, message, LlmMessage.M_TYPE.USER);
		// none/gpt/search
		if (FarmParameterService.getInstance().getParameter("config.imbar.type").equals("llm")) {
			if (AiQuestor.talking(talkSessionId) != null) {
				return ViewMode.getInstance()
						.putAttr("msg", AiQuestor.talking(talkSessionId).getHtml() + "<b>(当前存在未完成的会话)</b>")
						.returnObjMode();
			}
			// AI问答
			AiQuestorMessage bmessage = AiQuestor.send(message, talkSessionId, null, ImHistory.getMessages(session));
			backmessage = bmessage.getHtml();
		}

		if (backmessage == null || backmessage.indexOf("[ERROR]") == 0
				|| FarmParameterService.getInstance().getParameter("config.imbar.type").equals("search")) { // 本地查詢接口
			// search接口
			String searchRrt = null;
			try {
				DataResult result = farmDocIndexManagerImpl.search(message,
						getCurrentUser(session, request) == null ? null : getCurrentUser(session, request).getId(), 1);
				String linkKnows = getKnowLinksBySearchResult(result.getResultList());
				if (StringUtils.isNotBlank(linkKnows)) {
					searchRrt = "找到以下知识：<br/><br/>" + linkKnows;
				} else {
					searchRrt = "暂未找到相关知识";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (StringUtils.isBlank(backmessage)) {
				backmessage = searchRrt;
			} else {
				backmessage = backmessage + "<br/>" + searchRrt;
			}

			AiQuestorMessage bmessage = new AiQuestorMessage(talkSessionId);
			bmessage.setMessage(backmessage);
			bmessage.submit(MessageState.COMPELET);
			ImHistory.addMessage(session, bmessage.getHtmlmsg(), HtmlUtils.HtmlRemoveTag(bmessage.getMessage()),
					LlmMessage.M_TYPE.ASSISTANT);
		}
		return ViewMode.getInstance().putAttr("msg", backmessage).returnObjMode();
	}

	/**
	 * 加载当前消息
	 */
	@RequestMapping("/PubLoadmsg")
	@ResponseBody
	public Map<String, Object> loadmsg(String ids, HttpSession session, HttpServletRequest request) {
		try {
			List<AiQuestorMessage> msgs = new ArrayList<AiQuestorMessage>();
			for (String id : parseIds(ids)) {
				AiQuestorMessage bmessage = AiQuestor.loadMessage(id);
				if (bmessage.getState().equals(MessageState.COMPELET)) {
					ImHistory.addMessage(session, bmessage.getHtmlmsg(), HtmlUtils.HtmlRemoveTag(bmessage.getMessage()),
							LlmMessage.M_TYPE.ASSISTANT);
					bmessage.submit(MessageState.EXPIRE);
				}
				msgs.add(bmessage);
			}
			return ViewMode.getInstance().putAttr("msgs", msgs).returnObjMode();
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 拼接一个供gpt参考的知识内容，从查询结果中抽取该知识
	 * 
	 * @param list
	 * @return
	 */
	private String getKnowLinksBySearchResult(List<Map<String, Object>> list) {
		String know = null;
		for (Map<String, Object> node : list) {
			String title = (String) node.get("TITLE");
			title = HtmlUtils.HtmlRemoveTag(title).replace("\"", "");
			String docdescribe = (String) node.get("DOCDESCRIBE");
			docdescribe = HtmlUtils.HtmlRemoveTag(docdescribe).replace("\"", "");
			String ID = (String) node.get("ID");
			String DOMTYPE = (String) node.get("DOMTYPE");
			String url = null;
			if (DOMTYPE.equals("file")) {
				url = "webdoc/view/PubFile" + ID + ".html";
			}
			if (DOMTYPE.equals("fqa")) {
				url = "webquest/fqa/Pub" + ID + ".html";
			}
			if (!DOMTYPE.equals("fqa") && !DOMTYPE.equals("file")) {
				url = "webdoc/view/Pub" + ID + ".html";
			}
			String a = "<a target='_blank' title='"
					+ docdescribe + "' href='" + url + "'>" + title + "</a><br/>";
			if (know == null) {
				know = a;
			} else {
				know = know + a;
			}
		}
		return know;
	}

	/**
	 * 清理历史消息
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/PubClear")
	@ResponseBody
	public Map<String, Object> PubClear(HttpSession session, HttpServletRequest request) {
		ImHistory.clearMessage(session);
		return ViewMode.getInstance().returnObjMode();
	}

	/**
	 * 立即停止回答
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/PubStopMsg")
	@ResponseBody
	public Map<String, Object> PubStopMsg(String ids, HttpSession session, HttpServletRequest request) {
		try {
			for (String id : parseIds(ids)) {
				AiQuestorMessage.stop(id);
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 加载历史消息
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/PubLoadhis")
	@ResponseBody
	public Map<String, Object> PubLoadhis(HttpSession session, HttpServletRequest request) {
		List<LlmMessage> list = ImHistory.getMessages(session);
		if (list.size() <= 0) {
			String key = FarmParameterService.getInstance().getParameter("config.imbar.default.tip");
			if (!key.trim().toLowerCase().equals("none")) {
				LlmMessage message = null;
				// 获取知识库默认提示消息
				if (message == null) {
					message = new LlmMessage(key, M_TYPE.FUNCTIP);
				}
				list.add(message);
			}
		}
		return ViewMode.getInstance().putAttr("msgs", list).returnObjMode();
	}

	private LoginUser getCurrentUser(HttpSession session, final HttpServletRequest request) {
		LoginUser user = null;
		if (getCurrentUser(session) != null) {
			user = getCurrentUser(session);
		} else {
			user = new LoginUser() {

				@Override
				public String getName() {
					return null;
				}

				@Override
				public String getLoginname() {
					return null;
				}

				@Override
				public String getId() {
					return null;
				}
			};
		}
		return user;
	}
}
