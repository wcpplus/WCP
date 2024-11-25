package com.farm.core.sql.query;

import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 查询工具工具类
 * 
 * @author Administrator
 * 
 */
public class DataQuerys {
	/**
	 * 检查SQL注入风险
	 * 
	 * @param var
	 *            拼写SQL的值,仅仅用于对值的处理
	 */
	public static void wipeVirus(String var) {
		//防止sql注入的字符转义
		var = StringEscapeUtils.escapeSql(var);
		if (var.indexOf("(") + var.indexOf(")") + var.indexOf("||") + var.indexOf("||") > 0) {
			throw new RuntimeException("违反SQL注入风险约束！");
		}
	}

	/**
	 * 解析一个id的集合为多id的字符串拼接，可以用于sql的in子句如:'id1','id2'...
	 * 
	 * @param vars
	 * @return
	 */
	public static String parseSqlValues(Collection<String> vars) {
		String typeids_Rule = null;
		for (String typeid : vars) {
			if (typeids_Rule == null) {
				typeids_Rule = "'" + typeid + "'";
			} else {
				typeids_Rule = typeids_Rule + "," + "'" + typeid + "'";
			}
		}
		return typeids_Rule;
	}
}
