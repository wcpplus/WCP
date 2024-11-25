package com.farm.tex.domainex;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.farm.llm.domain.LlmMessages;
import com.farm.llm.domain.ReferenceMaterial;
import com.farm.parameter.FarmParameterService;
import com.farm.util.cache.FarmCacheName;
import com.farm.util.cache.FarmCaches;
import com.farm.util.web.FarmHtmlUtils;

/**
 * 向大语言模型提问（前度）
 * 
 * @author Wd
 *
 */
public class AiQuestorMessage {
	private static final Logger log = Logger.getLogger(AiQuestorMessage.class);
	/**
	 * 当前问题ID，不会做持久化，主要用于在前台标定问题并加载答案
	 */
	private String id;
	// 问题的回答文本
	private String message;
	// 用户提出的问题
	private String userQuestion;
	// 用户loginname或ip(判断当前用户提问状态，如果提问中不允许同时多个提问)
	private String userkey;
	/**
	 * 是否立即停止
	 */
	private boolean isStop = false;
	// 参考资料信息
	private Map<String, ReferenceMaterial> referenceDic = new HashMap<String, ReferenceMaterial>();

	private List<ReferenceMaterial> reference = new ArrayList<ReferenceMaterial>();

	public List<ReferenceMaterial> getReference() {
		return reference;
	}

	@SuppressWarnings("unused")
	private AiQuestorMessage() {
	}

	public AiQuestorMessage(String userkey) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.userkey = userkey;
	}

	public void setReference(LlmMessages llmMessages) {
		for (ReferenceMaterial node : llmMessages.getMaterials().getList()) {
			ReferenceMaterial n = new ReferenceMaterial();
			n.setTitle(node.getTitle());
			n.setUrl(node.getUrl());
			n.setMarkId(node.getMarkId());
			n.setExcellent(node.isExcellent());
			referenceDic.put(n.getMarkId(), n);
			reference.add(n);
		}
	}

	// 当前参考资料是从全文检索获得，将保存查询时的关键字
	private String indexkeys;

	/**
	 * 获得高亮语义编码
	 * 
	 * @return
	 */
	private String getCodeKey(String node) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("javascript", "js");
		map.put("html", "html");
		map.put("css", "css");
		map.put("php", "php");
		map.put("Perl", "pl");
		map.put("Python", "py");
		map.put("Ruby", "rb");
		map.put("Java", "java");
		map.put("VB", "vb");
		map.put("ASP", "vb");
		map.put("cpp", "cpp");
		map.put("C++", "cpp");
		map.put("C", "cpp");
		map.put("C#", "cs");
		map.put("XML", "xml");
		map.put("Shell", "bsh");
		map.put("Sql", "sql");
		map.put("Other", "other");
		for (Entry<String, String> entry : map.entrySet()) {
			if (node.toLowerCase().startsWith(entry.getKey().toLowerCase())
					|| node.toLowerCase().startsWith(entry.getValue().toLowerCase())) {
				return entry.getValue();
			}
		}
		return "other";
	}

	/**
	 * 获得前台输出的参考资料
	 * 
	 * @return
	 */
	public String getHtmlmsg() {
		String html = "";
		int showKnowNum = 0;
		{
			String htmlmsg;
			boolean currentIsPre = false;
			htmlmsg = message.replaceAll("(?i)\\shref=['\"]\\S*['\"]", "");
			if (htmlmsg.indexOf("```") == 0) {
				currentIsPre = true;
			}
			for (String node : htmlmsg.replace("\n", "[br/]").split("```")) {
				if (node.equals("")) {
					continue;
				}
				if (currentIsPre) {
					html = html + "<pre title='" + getCodeKey(node) + "'>"
							+ FarmHtmlUtils.escapeHtml(node.replace("[br/]", "\n"));
				} else {
					html = html + "</pre>" + node;
				}

				if (currentIsPre) {
					currentIsPre = false;
				} else {
					currentIsPre = true;
				}
			}
			if (!currentIsPre) {
				html = html + "</pre>";
			}
			if (html.indexOf("</pre>") == 0) {
				html = html.replaceFirst("</pre>", "");
			}
			html = html.replace("[br/]", "<br/>");
		}
		{
			for (String mark : referenceDic.keySet()) {
				if (html.indexOf(mark) >= 0) {
					html = html.replace(mark, "<a href='" + referenceDic.get(mark).getUrl() + "' >"
							+ referenceDic.get(mark).getTitle() + "</a>");
					referenceDic.get(mark).setShow(true);
					showKnowNum++;
				}
			}
			try {
				// 寻找引用知识的漏网之鱼,通过自动后三位匹配到知识引用
				String regex = "STEXT[0-9a-fA-F]{16,64}";
				List<String> matches = new ArrayList<>();
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(html);
				while (matcher.find()) {
					matches.add(matcher.group());
				}
				for (String kid : matches) {
					String match = kid.substring(kid.length() - 3);
					String ktitle = null;
					for (String mark : referenceDic.keySet()) {
						if (mark.substring(mark.length() - 3).equals(match)) {
							ktitle = "<a href='" + referenceDic.get(mark).getUrl() + "' >"
									+ referenceDic.get(mark).getTitle() + "</a>";
							referenceDic.get(mark).setShow(true);
							showKnowNum++;
						}
					}
					if (ktitle != null) {
						html = html.replace(kid, ktitle);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (isStop) {
			html = html + "<b>[已停止]</b>";
		}
		if (state.equals(MessageState.LOADING) || state.equals(MessageState.NEW)) {
			return html + "...";
		} else {
			String more = "";
			if (indexkeys == null) {
				indexkeys = userQuestion;
			}
			if (FarmParameterService.getInstance().getParameterBoolean("config.imbar.qa.reference.show.is")) {
				// 处理对话中的强制推送参考知识
				int maxnum = FarmParameterService.getInstance()
						.getParameterInt("config.imbar.qa.reference.show.maxnum");

				// 添加参考资料
				for (ReferenceMaterial material : reference) {
					if (showKnowNum >= maxnum) {
						break;
					}
					if (material.isExcellent() && !material.isShow()) {
						String ktitle = "推荐知识：<a href='" + material.getUrl() + "' >" + material.getTitle() + "</a>";
						html = html + "<br/>" + ktitle;
						showKnowNum++;
					}
				}
			}
			try {
				if (html.indexOf("farm_wcp_ai_more") < 0 && indexkeys != null) {
					more = "<div  class='farm_wcp_ai_more'><a title='" + indexkeys + "' href='websearch/PubDo.do?word="
							+ URLEncoder.encode(URLEncoder.encode(indexkeys, "utf8"), "utf8") + "'>更多知识...</a></div>";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return html.replace("参考资料ID:", "参考资料:") + more;
		}
	}

	public String getIndexkeys() {
		return indexkeys;
	}

	public void setIndexkeys(String indexkeys) {
		this.indexkeys = indexkeys;
	}

	public String getUserkey() {
		return userkey;
	}

	private MessageState state;

	public enum MessageState {
		NEW, LOADING, COMPELET, EXPIRE;
	}

	public String getUserQuestion() {
		return userQuestion;
	}

	public void setUserQuestion(String userQuestion) {
		this.userQuestion = userQuestion;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		if (state.equals(MessageState.LOADING) || state.equals(MessageState.NEW)) {
			return message + "...";
		} else {
			return message;
		}
	}

	public void appendMessage(String message) {
		log.info(message);
		if (this.message == null) {
			this.message = "";
		}
		this.message = this.message + message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageState getState() {
		return state;
	}

	private void setState(MessageState state) {
		this.state = state;
	}

	public static AiQuestorMessage load(String id) {
		AiQuestorMessage aimsg = (AiQuestorMessage) FarmCaches.getInstance().getCacheData(id,
				FarmCacheName.wcpAiMessages);
		if (aimsg == null) {
			aimsg = new AiQuestorMessage(id);
			aimsg.setId(id);
			aimsg.setMessage("未找到该消息!");
			aimsg.setUserQuestion("未知");
			aimsg.setState(MessageState.COMPELET);
		}
		return aimsg;
	}

	public static void stop(String id) {
		AiQuestorMessage aimsg = (AiQuestorMessage) FarmCaches.getInstance().getCacheData(id,
				FarmCacheName.wcpAiMessages);
		aimsg.setStop(true);
	}

	/**
	 * 获得在页面前端的预置文本框（占位用，延遲加载内容）
	 * 
	 * @return
	 */
	public String getHtml() {
		return "<div class='farm_wcp_ai_msg' id='" + this.getId() + "' data-state='" + this.getState() + "'>...</div>";
	}

	/**
	 * @param expire
	 * @return 是否强制停止
	 */
	public boolean submit(MessageState state) {
		if (isStop) {
			this.setState(MessageState.COMPELET);
		} else {
			this.setState(state);
		}
		FarmCaches.getInstance().putCacheData(this.getId(), this, FarmCacheName.wcpAiMessages);
		// NEW, LOADING, COMPELET, EXPIRE;
		if (state.equals(MessageState.COMPELET) || state.equals(MessageState.EXPIRE)) {
			FarmCaches.getInstance().removeCacheData(userkey, FarmCacheName.wcpAiUserTalking);
		}
		if (isStop) {
			return true;
		} else {
			return false;
		}
	}

}
