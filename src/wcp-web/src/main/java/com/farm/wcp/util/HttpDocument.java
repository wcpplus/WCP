package com.farm.wcp.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.farm.wcp.know.util.HttpResourceHandle;

/**
 * 配合jsoup处理HTML文档
 * 
 * @author Administrator
 * 
 */
public class HttpDocument {
	private Document document;
	private URL url;
	private static final String AGENT_REQUEST = "Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0";

	/**
	 * 获得jsoup的document对象
	 * 
	 * @return
	 */
	public Document getDocument() {
		return document;
	}

	private HttpDocument() {

	}

	/**
	 * 删除和内容无关的标签(保留图片)
	 */
	public void removeOutContent() {
		document.getElementsByTag("script").remove();
		document.getElementsByTag("textarea ").remove();
		document.getElementsByTag("style").remove();
		document.getElementsByTag("a").removeAttr("href");
		document.getElementsByTag("input").remove();
		document.getElementsByTag("button").remove();
		// document.getElementsByTag("img").removeAttr("src");
		document.getElementsByTag("iframe").remove();
	}

	/**
	 * 改写页面中的远程资源
	 * 
	 * @param handle
	 */
	public void rewriteResources(HttpResourceHandle handle) {
		Elements imgs = document.getElementsByTag("img");
		Iterator<Element> imgtor = imgs.iterator();
		while (imgtor.hasNext()) {
			Element element = imgtor.next();
			String url = null;
			String rUrl = null;
			url = element.attr("src");
			if (url != null && url.trim().length() > 0) {
				// 下载文件
				rUrl = handle.handle(url, this.url);
				// 替换文件
				element.attr("src", rUrl);
			}
		}
	}

	/**
	 * 获得html的字符集
	 * 
	 * @return
	 */
	public String getCharset() {
		Elements meta = document.getElementsByTag("meta");
		// <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		for (Element node : meta) {
			String attr = node.attr("content");
			if (attr.toUpperCase().indexOf("CHARSET") >= 0) {
				String charstr = attr.toUpperCase().substring(
						attr.toUpperCase().indexOf("=") + 1);
				return charstr;
			}
		}
		return "UTF-8";
	}

	public String getTitle() {
		Elements Styles = document.getElementsByTag("title");
		return Styles.get(0).text();
	}

	// Elements meta=doc.getElementsByTag("meta");
	/**
	 * 实例化
	 * 
	 * @param con
	 *            Jsoup.connect(url.toString()
	 * @return
	 */
	public static HttpDocument instance(Connection con) {
		HttpDocument macDoc = null;
		try {
			macDoc = new HttpDocument();
			macDoc.url = con.request().url();
			macDoc.document = con.userAgent(AGENT_REQUEST).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return macDoc;
	}

	/**
	 * 实例化
	 * 
	 * @param con
	 *            Jsoup.connect(url.toString()
	 * @return
	 */
	public static HttpDocument instance(String html) {
		HttpDocument macDoc = null;
		macDoc = new HttpDocument();
		macDoc.document = Jsoup.parseBodyFragment(html);
		return macDoc;
	}

	// Elements meta=doc.getElementsByTag("meta");
	/**
	 * 实例化
	 * 
	 * @param con
	 *            Jsoup.connect(url.toString()
	 * @return
	 */
	public static HttpDocument instance(File file) {
		HttpDocument macDoc = null;
		try {
			macDoc = new HttpDocument();
			macDoc.document = Jsoup.parse(file, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return macDoc;
	}
}
