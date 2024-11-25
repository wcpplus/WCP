package com.farm.wcp.know.service;

import java.io.IOException;
import java.net.URL;

import com.farm.wcp.know.util.HttpResourceHandle;


/**
 * 网络文档功能接口
 * 
 * @author Administrator
 * 
 */
public interface WebDocServiceInter {
	static enum DOC_TYPE {
		HTML, IMAGE
	}

	/**
	 * 获得一个页面的内容从网络到本地
	 * 
	 * @param url
	 * @param type
	 * @return String[1:文件内容TEXT,2:文件名称,3.文件内容HTML]
	 * @throws IOException
	 */
	public String[] crawlerWebDocTempFileId(URL url, DOC_TYPE type,
			HttpResourceHandle handle) throws IOException;
}
