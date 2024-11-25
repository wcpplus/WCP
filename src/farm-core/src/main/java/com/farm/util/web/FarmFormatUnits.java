package com.farm.util.web;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转义工具集合
 * 
 * @author Administrator
 * 
 */
public class FarmFormatUnits {
	/**
	 * 为文件设置大小并加上单位
	 * 
	 * @param fileLength
	 *            文件大小（b）
	 * @return
	 */
	public static String getFileLengthAndUnit(int fileLength) {
		String unit = "b";
		Integer length = fileLength;
		if ((Integer) fileLength / 1024 > 0) {
			length = (Integer) fileLength / 1024;
			unit = "kb";
		}
		if ((Integer) fileLength / 1024 / 1024 > 0) {
			length = (Integer) fileLength / 1024 / 1024;
			unit = "mb";
		}
		return length + unit;
	}

	/**
	 * 去掉日期时间的格式（去掉'-'、'：'、'空格'）
	 * 
	 * @param timeStr
	 * @return
	 */
	public static String getReFormateTime14(String timeStr) {
		timeStr = timeStr.replaceAll("-", "").replaceAll(":", "").replaceAll(
				" ", "")
				+ "00000000000000";
		return timeStr.substring(0, 14);
	}

	/**
	 * 格式化时间
	 * 
	 * @param timeStr08_14
	 *            yyyyMMddHHmmss
	 * @param isshowCurrentDay
	 *            是否当天显示为“今天”
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getFormateTime(String timeStr08_14,
			boolean isshowCurrentDay) {
		if (timeStr08_14 == null || timeStr08_14.trim().length() <= 0) {
			return null;
		}
		int tlength = timeStr08_14.length();
		timeStr08_14 = timeStr08_14 + "00000000";
		String yyyy = timeStr08_14.substring(0, 4);
		String MM = timeStr08_14.substring(4, 6);
		String dd = timeStr08_14.substring(6, 8);
		String HH = timeStr08_14.substring(8, 10);
		String mm = timeStr08_14.substring(10, 12);
		String ss = timeStr08_14.substring(12, 14);
		String returnData = null;
		if (tlength == 8 && returnData == null) {
			returnData = yyyy + "-" + MM + "-" + dd;
		}
		if (tlength == 10 && returnData == null) {
			returnData = yyyy + "-" + MM + "-" + dd + " " + HH;
		}
		if (tlength == 12 && returnData == null) {
			returnData = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm;
		}
		if (returnData == null) {
			returnData = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm + ":"
					+ ss;
		}
		SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentday = _sdf.format(new Date());
		return isshowCurrentDay ? returnData.replace(currentday, "今天")
				: returnData;
	}

	/**
	 * 时间去格式
	 * 
	 * @param timeStr
	 *            格式化的時間yyyy-MM-dd HH:mm:ss
	 * @param length
	 *            最后截取时间的长度
	 * @return yyyyMMddHHmmss
	 */
	public static String reFormateTime(String timeStr, int length) {
		if (timeStr == null || timeStr.trim().length() <= 0) {
			return null;
		}
		timeStr = timeStr.replace(":", "").replace("-", "").replace(" ", "");
		if (timeStr.length() > length) {
			timeStr = timeStr.substring(0, length);
		}
		return timeStr;

	}

}
