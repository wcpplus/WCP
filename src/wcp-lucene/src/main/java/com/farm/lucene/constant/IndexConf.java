package com.farm.lucene.constant;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class IndexConf {
	private static final String BUNDLE_NAME = "indexConfig"; //$NON-NLS-1$
	private static final Logger log = Logger.getLogger(IndexConf.class);
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private IndexConf() {
	}

	public static String getString(String key) {
		try {
			String messager = RESOURCE_BUNDLE.getString(key);
			return messager;
		} catch (MissingResourceException e) {
			String messager = "不能在配置文件" + BUNDLE_NAME + "中发现参数：" + '!' + key
					+ '!';
			log.error(messager);
			throw new RuntimeException(messager);
		}
	}
}