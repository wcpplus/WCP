package com.farm.core;

import com.farm.core.auth.util.AuthenticateInter;
import com.farm.core.auth.util.AuthenticateProvider;
import com.farm.core.config.PropertiesUtils;
import com.farm.core.time.TimeTool;
import com.farm.util.validate.ValidUtils;
import com.farm.util.web.FarmFormatUnits;
import com.farm.util.web.FarmproHotnum;
import com.farm.util.web.WebHotCase;
import com.farm.util.web.WebVisitBuff;

/**
 * 平台工具类入口
 * 
 * @author wangdong
 * 
 */
public class FarmUtils {
	/**获得验证类工具
	 * @return
	 */
	public static ValidUtils getValidUtils() {
		return new ValidUtils();
	}

	/**
	 * 获得加解密类工具
	 * 
	 * @return
	 */
	public static AuthenticateInter getAuthUtils() {
		return AuthenticateProvider.getInstance();
	}

	/**
	 * 获得Properties文件工具
	 * 
	 * @param fileName
	 *            如jdbc.properties
	 * @return
	 */
	public static PropertiesUtils getPropertiesUtils(String fileName) {
		return new PropertiesUtils(fileName);
	}

	/**
	 * 获得时间类工具
	 * 
	 * @return
	 */
	public static TimeTool getTimeTools() {
		return new TimeTool();
	}

	/**
	 * 获得格式化工具（友好的时间格式化，文件大小格式化）
	 * 
	 * @return
	 */
	public static FarmFormatUnits getFormatUtils() {
		return new FarmFormatUnits();
	}

	/**
	 * 访问热度计算工具
	 * 
	 * @return
	 */
	public static FarmproHotnum getHotUtils() {
		return new FarmproHotnum();
	}

	/**
	 * 计算热词（如搜索关键字的统计）
	 * 
	 * @return
	 */
	public static WebHotCase getHotWordUtils() {
		return new WebHotCase();
	}

	/**
	 * 判断用户在一定时间内是否访问的工具，用来计算一个KEY在一定时间内是否已经被标记（如控制相同用户不重复计算文章的访问量）
	 * 
	 * @param domain
	 *            统计域，不同域被分隔开控制（相互间不受影响）
	 * @param maxNum
	 *            每个域中允许缓存的key数据量（超出后域被刷新）
	 * @return
	 */
	public static WebVisitBuff getWebVisitBuff(String domain, int maxNum) {
		return WebVisitBuff.getInstance(domain, maxNum);
	}

}
