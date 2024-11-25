package com.farm.tex.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import com.farm.util.web.FarmHtmlUtils;

/**
 * 段落分隔
 * 
 * @author Wd
 *
 */
public class ParagraphSeparator {

	public static class Paragraph {
		List<String> list = new ArrayList<String>();

		public int length() {
			int n = 0;
			for (String node : list) {
				n = n + node.length();
			}
			return n;
		}

		public int lineNum() {
			return list.size();
		}

		public void append(String node) {
			list.add(node);
		}

		public String getString() {
			StringBuffer text = new StringBuffer();
			for (String node : list) {
				text.append(node);
			}
			return text.toString();
		}

		public void clear() {
			list = new ArrayList<String>();
		}

	}

	/**去除文本中特殊字符和标点符号（只返回数字字母和中文）
	 * @param msg
	 * @return
	 */
	public static String getChatOnly(String msg) {
		String resultString = msg.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
		return resultString;
	}

	/**
	 * 分割長文本为短一些的段落
	 * 
	 * @param text
	 * @return
	 */
	public static List<String> doSeparat(String text) {
		text = FarmHtmlUtils.HtmlRemoveTag(text);
		List<String> backList = new ArrayList<String>();
		String[] parts = splitBySentence(text);
		Paragraph oneNode = new Paragraph();
		for (String node : parts) {
			if (node.length() > 200) {
				// 允许单一句子的长度
				node = node.substring(0, 197) + "...";
			}
			if ((oneNode.lineNum() < 10 && oneNode.length() < 500) || oneNode.length() < 500) {
				// 可以添加
				oneNode.append(node);
			} else {
				// 結束
				if (oneNode.length() > 1800) {
					backList.add(oneNode.getString().substring(0, 1800));
				} else {
					if (oneNode.length() > 0) {
						backList.add(oneNode.getString());
					}
				}
				oneNode.clear();
				oneNode.append(node);
			}
		}
		if (oneNode.length() > 0) {
			if (oneNode.length() > 1800) {
				backList.add(oneNode.getString().substring(0, 1800));
			} else {
				if (oneNode.length() > 0) {
					backList.add(oneNode.getString());
				}
			}
		}
		return backList;
	}

	/**
	 * 获取段落的简述
	 * 
	 * @param txt
	 * @return
	 */
	public static String getDescribe(String txt) {
		return null;
	}

	/**
	 * 按句子分割字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String[] splitBySentence(String text) {
		text = StringEscapeUtils.unescapeHtml4(text);
		text = text.replace(". ", "。");
		// 定义正则表达式，匹配句子末尾的标点符号
		Pattern pattern = Pattern.compile("[!。！;；]+\\s*");
		Matcher matcher = pattern.matcher(text);
		// 使用匹配结果进行分割
		ArrayList<String> sentences = new ArrayList<>();
		int start = 0;
		while (matcher.find()) {
			String sentence = text.substring(start, matcher.end());
			sentences.add(sentence);
			start = matcher.end();
		}
		if (start < text.length()) {
			sentences.add(text.substring(start));
		}
		return sentences.toArray(new String[0]);
	}

}
