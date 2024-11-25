package com.farm.wcp.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.farm.core.time.TimeTool;
import com.farm.llm.utils.LlmMessage;
import com.farm.llm.utils.LlmMessage.M_TYPE;

/**
 * 管理聊天记录
 * 
 * @author macpl
 *
 */
public class ImHistory {
	private static String SESSION_IM_HIS = "SESSION_IM_HIS";

	/**
	 * @param session
	 * @param message html消息
	 * @param rawMsg 原始消息
	 * @param type   mine|service
	 */
	public static void addMessage(HttpSession session, String message, String rawMsg, M_TYPE type) {
		@SuppressWarnings("unchecked")
		List<LlmMessage> list = (List<LlmMessage>) session.getAttribute(SESSION_IM_HIS);
		if (list == null) {
			list = new ArrayList<LlmMessage>();
		}
		list.add(new LlmMessage(message,type, TimeTool.format(new Date(), "yyyy-MM-dd HH:mm")));
		session.setAttribute(SESSION_IM_HIS, list);
	}

	public static void clearMessage(HttpSession session) {
		session.setAttribute(SESSION_IM_HIS, new ArrayList<LlmMessage>());
	}

	public static List<LlmMessage> getMessages(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<LlmMessage> list = (List<LlmMessage>) session.getAttribute(SESSION_IM_HIS);
		if (list == null) {
			return new ArrayList<LlmMessage>();
		}
		return list;
	}
}
