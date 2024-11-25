package com.farm.doc.server.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.farm.core.time.TimeTool;
import com.farm.parameter.FarmParameterService;

public class FarmDocFiles {
	private static final Logger log = Logger.getLogger(FarmDocFiles.class);

	/**
	 * 生成文件目录
	 * 
	 * @return
	 */
	public static String generateDir() {
		// 把临时文件拷贝到上传目录下
		String dirPath = File.separator + TimeTool.getTimeDate12().substring(0, 4) + File.separator
				+ TimeTool.getTimeDate12().substring(4, 6) + File.separator + TimeTool.getTimeDate12().substring(6, 8)
				+ File.separator + TimeTool.getTimeDate12().substring(8, 10) + File.separator;
		File accessFile = new File(getFileDirPath() + dirPath);
		accessFile.mkdirs();
		return dirPath;
	}

	public static String getFileDirPath() {
		String path = FarmParameterService.getInstance().getParameter("config.doc.dir");
		try {
			if (path.startsWith("WEBROOT")) {
				path = path.replace("WEBROOT",
						FarmParameterService.getInstance().getParameter("farm.constant.webroot.path"));
			}
			String separator = File.separator;
			if (separator.equals("\\")) {
				separator = "\\\\";
			}
			path = path.replaceAll("\\\\", "/").replaceAll("/", separator);
		} catch (Exception e) {
			log.warn(path + ":路径地址有误！");
			path = DocumentConfig.getString("config.doc.dir");
		}
		return path;
	}

	/**
	 * 获得文件的扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExName(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 重html中获取系统中附件id
	 * 
	 * @param html
	 * @return
	 */
	public static List<String> getFilesIdFromHtml(String html) {
		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		for (Element node : doc.getElementsByTag("img")) {
			String urlStr = node.attr("src");
			if (urlStr.indexOf(DocumentConfig.getString("config.doc.download.url")) >= 0) {
				String splits = urlStr.substring(urlStr.indexOf(DocumentConfig.getString("config.doc.download.url"))
						+ DocumentConfig.getString("config.doc.download.url").length());
				list.add(splits);
			}
		}
		for (Element node : doc.getElementsByTag("a")) {
			String urlStr = node.attr("href");
			if (urlStr.indexOf(DocumentConfig.getString("config.doc.download.url")) >= 0) {
				String splits = urlStr.substring(urlStr.indexOf(DocumentConfig.getString("config.doc.download.url"))
						+ DocumentConfig.getString("config.doc.download.url").length());
				list.add(splits);
			}
		}
		return list;
	}

	/**
	 * 拷贝一个文件到新的地址
	 * 
	 * @param file
	 * @param newPath
	 */
	public static void copyFile(File file, String newPath) {
		int byteread = 0;
		File oldfile = file;
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = null;
			FileOutputStream fs = null;
			try {
				inStream = new FileInputStream(file);
				fs = new FileOutputStream(newPath + file.getName());
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				try {
					inStream.close();
					fs.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 将文件流保存到一个地址
	 * 
	 * @param file
	 * @param newPath
	 */
	public static Long saveFile(InputStream inStream, String filename, String newPath) {
		int byteread = 0;
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(newPath + filename);
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				inStream.close();
				fs.close();
			} catch (IOException e) {
			}
		}
		File file = new File(newPath + filename);
		return file.length();
	}

}
