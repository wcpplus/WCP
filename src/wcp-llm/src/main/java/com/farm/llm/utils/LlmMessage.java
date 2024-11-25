package com.farm.llm.utils;

import java.util.regex.Pattern;

/**
 * @author Wd
 *
 */
public class LlmMessage {
	private String message;
	private M_TYPE type;
	private String ctime;

	public enum M_TYPE {
		/**
		 * 功能信息，与对话无关
		 */
		FUNCTIP,
		// 模型消息
		ASSISTANT,
		// 用户消息
		USER,
		// 角色提示
		SYSTEM
	};

	public LlmMessage(String message, M_TYPE type, String ctime) {
		super();
		if (message != null) {
			//message = message.replace("\n", "<br/>");
		} else {
			message = "";
		}
		this.message = message;
		this.type = type;
		this.ctime = ctime;
	}

	public LlmMessage(String message, M_TYPE type) {
		super();
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public M_TYPE getType() {
		return type;
	}

	public String getCtime() {
		return ctime;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void appendMessage(String str) {
		this.message = message + str;
	}

	/**
	 * 删除Html标签
	 * 
	 * @param inputString
	 * @return
	 */
	public static String HtmlRemoveTag(String html) {
		if (html != null) {
			// 段落间没有句号分隔的，自动添加句号,方便向量检索时划分段落
			html = html.replace("</div>", "。");
			html = html.replace("</p>", "。");
			html = html.replaceAll("([!。！;；]+)\\s*\\。", "$1");
			html = html.replaceAll("\\s{2,}", "  ");
			html = html.replaceAll("<img", "[图片]<img");
		}
		if (html == null)
			return null;
		String htmlStr = html; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			// System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr.replaceAll("\\s+", " ");// 返回文本字符串
	}
}
