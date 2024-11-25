package com.farm.wcp.know.service.impl;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.farm.wcp.know.service.WebDocServiceInter;
import com.farm.wcp.know.util.HttpDocument;
import com.farm.wcp.know.util.HttpResourceHandle;

@Service
public class WebDocServiceImpl implements WebDocServiceInter {
	private static final WebDocServiceInter thisObj = new WebDocServiceImpl();

	/**
	 * 获得单例对象
	 * 
	 * @return
	 */
	public static WebDocServiceInter instance() {
		return thisObj;
	}

	@Override
	public String[] crawlerWebDocTempFileId(URL url, DOC_TYPE type, HttpResourceHandle handle) throws IOException {
		String contentText = null;
		HttpDocument htmlDoc = HttpDocument.instance(Jsoup.connect(url.toString()));
		contentText = htmlDoc.getDocument().text();
		Elements body = htmlDoc.getDocument().getElementsByTag("body");
		htmlDoc.removeOutContent();
		htmlDoc.rewriteResources(handle);
		return new String[] { contentText, htmlDoc.getTitle(), body.html() };
	}

}
