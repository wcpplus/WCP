package com.farm.util.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class FarmHtmlUtils {
	/**
	 * 转义html
	 * 
	 * @param input
	 * @return
	 */
	public static String escapeHtml(String input) {
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll("\"", "&quot;");
		input = input.replaceAll("'", "&apos;");
		return input;
	}

	/**
	 * 删除Html标签
	 * 
	 * @param inputString
	 * @return
	 */
	public static String HtmlRemoveTag(String html, String... tagNames) {
		if (html == null) {
			return null;
		}
		String htmlStr = html; // 含html标签的字符串
		String textStr = "";
		for (String tagName : tagNames) {
			java.util.regex.Pattern p_script;
			java.util.regex.Matcher m_script;
			java.util.regex.Pattern p_style;
			java.util.regex.Matcher m_style;
			try {

				// }
				String regEx_html1 = "<" + tagName + "\\b[^>]*>"; // 定义HTML标签的正则表达式
				String regEx_html2 = "</" + tagName + "\\b[^>]*>"; // 定义HTML标签的正则表达式

				p_script = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
				m_script = p_script.matcher(htmlStr);
				htmlStr = m_script.replaceAll(""); // 过滤script标签

				p_style = Pattern.compile(regEx_html2, Pattern.CASE_INSENSITIVE);
				m_style = p_style.matcher(htmlStr);
				htmlStr = m_style.replaceAll(""); // 过滤style标签
				textStr = htmlStr;

			} catch (Exception e) {
				// System.err.println("Html2Text: " + e.getMessage());
			}
		}
		return textStr;// 返回文本字符串
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

	/**
	 * 将style字符串转义为map
	 * 
	 * @param style 字符串如width:100.0%;height:auto;
	 * @return
	 */
	public static Map<String, String> parseStyleToMap(String style) {
		Map<String, String> css = new HashMap<>();
		for (String node : style.split(";")) {
			String[] nodeArray = node.split(":");
			if (nodeArray.length == 2) {
				css.put(nodeArray[0].trim().toLowerCase(), nodeArray[1].trim());
			}
		}
		return css;
	}

	/**
	 * 将style的map连接为style字符串
	 * 
	 * @param styles
	 * @return
	 */
	public static String joinStyleMap(Map<String, String> styles) {
		String style = null;
		for (Entry<String, String> node : styles.entrySet()) {
			if (StringUtils.isNotBlank(node.getKey()) && StringUtils.isNotBlank(node.getValue())) {
				if (style == null) {
					style = node.getKey() + ":" + node.getValue();
				} else {
					style = style + node.getKey() + ":" + node.getValue();
				}
			}
		}
		return style;
	}
}
