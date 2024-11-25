package com.farm.web.task.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.farm.web.constant.FarmConstant;
import com.farm.web.task.ServletInitJobInter;

public class ServerLicenceInit implements ServletInitJobInter {

	private final static Logger log = Logger.getLogger(ServerLicenceInit.class);

	@Override
	public void execute(ServletContext context) {
		try {
			FarmConstant.LICENCE = read(
					new File(context.getRealPath("") + File.separator
							+ "licence.data")).replace("\n", "");
		} catch (Exception e) {
			FarmConstant.LICENCE = null;
		}
		try {
//			System.out.println("info: case is "+FarmConstant.LICENCE + " for "
//					+ MayCase.isCase(FarmConstant.LICENCE));
		} catch (Exception e) {
			System.out.println("info: case is "+FarmConstant.LICENCE + " for "+ ":false");
		}
	}

	public String read(File file) throws Exception {
		StringBuffer lines = new StringBuffer();
		InputStream inputStream = null;
		BufferedReader reader = null;
		InputStreamReader inputStreamReader = null;
		try {
			inputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.append(line).append("\n");
			}
		} catch (IOException e) {
			log.info(e.getMessage(), e);
		} finally {
			try {
				inputStreamReader.close();
				inputStream.close();
				reader.close();
			} catch (IOException e) {
				log.info(e.getMessage(), e);
			}
		}
		return lines.toString();
	}
}
