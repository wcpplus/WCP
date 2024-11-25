package com.farm.core.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.farm.core.Context;
import com.farm.core.auth.util.KeyUtil;

/**
 * 读取licence
 * 
 * @author macplus
 *
 */
public class ReadKey {
	private static boolean isOk = false;

	public static void read(String path) {
		try {
			if (isOk == false) {
				Context.FK = readTxtFile(path + File.separator + "licence");
				Context.FLAG = KeyUtil.getFkey(KeyUtil.getMKey()).equals(Context.FK);
				isOk = true;
			}
		} catch (Exception e) {
			Context.FK = "NONE1";
		}
	}

	public static String readTxtFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
				read.close();
			}
		} catch (Exception e) {
		}
		return "none";
	}
}
