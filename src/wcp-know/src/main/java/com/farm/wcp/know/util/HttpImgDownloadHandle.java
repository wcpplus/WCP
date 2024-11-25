package com.farm.wcp.know.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.farm.core.auth.domain.LoginUser;
import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.server.FarmFileManagerInter;

/**
 * 下载网页中的图片资源
 * 
 * @author wangdong
 *
 */
public class HttpImgDownloadHandle implements HttpResourceHandle {
	private FarmFileManagerInter farmFileManagerImpl;
	private static final Logger log = Logger.getLogger(HttpImgDownloadHandle.class);

	public HttpImgDownloadHandle(FarmFileManagerInter farmFileManagerImpl) {
		this.farmFileManagerImpl = farmFileManagerImpl;
	}

	@Override
	public String handle(String eurl, URL baseUrl) {
		// eurl=http://img.baidu.com/img/baike/logo-baike.png
		log.info("html img from:"+ eurl);
		String exname = null;
		try {
			if (eurl.lastIndexOf("?") > 0) {
				exname = eurl.substring(0, eurl.lastIndexOf("?"));
			} else {
				exname = eurl;
			}
			if (eurl.lastIndexOf(".") > 0) {
				exname = eurl.substring(eurl.lastIndexOf(".") + 1);
			} else {
				exname = eurl;
			}
			if (exname == null || exname.length() > 10) {
				exname = "gif";
			}
		} catch (Exception e) {
			exname = "gif";
		}
		if(eurl.indexOf("//")==0){
			eurl = baseUrl.getProtocol()+":"+ eurl;
		}
		if (eurl.toUpperCase().indexOf("HTTP") < 0) {
			eurl = baseUrl.toString().substring(0, baseUrl.toString().lastIndexOf("/") + 1) + eurl;
		}
		try {
			URL innerurl = new URL(eurl);
			// 创建连接的地址
			HttpURLConnection connection = (HttpURLConnection) innerurl.openConnection();
			// 返回Http的响应状态码
			InputStream input = null;
			try {
				input = connection.getInputStream();
			} catch (Exception e) {
				System.out.println(e + e.getMessage());
				return eurl;
			}
			FarmDocfile file = farmFileManagerImpl.openFile(exname, eurl.length() > 128 ? eurl.substring(0, 128) : eurl,
					new NoneUser());
			OutputStream fos = new FileOutputStream(file.getFile());
			// 获取输入流
			try {
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			} finally {
				input.close();
				fos.close();
			}
			// config.file.client.html.resource.url
			eurl = farmFileManagerImpl.getFileURL(file.getId());
		} catch (IOException e) {
			log.error(e + "网络图片文件保存失败");
		}
		log.info("html img to:"+ eurl);
		return eurl;
	}

	class NoneUser implements LoginUser {
		@Override
		public String getName() {
			return "NONE";
		}

		@Override
		public String getLoginname() {
			return "NONE";
		}

		@Override
		public String getId() {
			return "NONE";
		}
	}
}
