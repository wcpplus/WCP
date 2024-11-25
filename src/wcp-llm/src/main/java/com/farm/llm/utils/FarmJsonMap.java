package com.farm.llm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

/**
 * 讀取json工具類, new WtcJsonMap(json.toMap())創建jsonmap對象
 * 
 * @author macpl
 *
 */
public class FarmJsonMap {
	private Map<String, Object> data;

	public String toString() {
		String bstr = "";
		for (Entry<String, Object> node : data.entrySet()) {
			bstr = node.getKey() + ":" + node.getValue() + "," + bstr;
		}
		return bstr;
	}

	public FarmJsonMap(Map<String, Object> map) {
		data = map;
	}

	public FarmJsonMap(String jsonStr) {
		data = new JSONObject(jsonStr).toMap();
		;
	}

	public String getString(String... keys) {
		if (getObject(keys) == null) {
			return " ";
		}
		return getObject(keys).toString();
	}

	@SuppressWarnings("unchecked")
	public Object getObject(String... keys) {
		Object curentObj = null;
		for (String key : keys) {
			if (curentObj == null) {
				curentObj = data.get(key);
			} else {
				curentObj = ((Map<String, Object>) curentObj).get(key);
			}
		}
		return curentObj;
	}

	public int getInt(String... keys) {
		if (getObject(keys) == null) {
			return 0;
		}
		return Integer.valueOf(getObject(keys).toString());
	}

	public Float getFloat(String... keys) {
		if (getObject(keys) == null) {
			return new Float(0);
		}
		return Float.valueOf(getObject(keys).toString());
	}

	public long getLong(String... keys) {
		if (getObject(keys) == null) {
			return new Long(0);
		}
		return Long.valueOf(getObject(keys).toString());
	}

	@SuppressWarnings("unchecked")
	public List<FarmJsonMap> getList(String... keys) {
		List<FarmJsonMap> list = new ArrayList<>();
		Object obj = getObject(keys);
		if (obj == null) {
			return new ArrayList<>();
		}
		for (Map<String, Object> node : (List<Map<String, Object>>) obj) {
			list.add(new FarmJsonMap(node));
		}
		return list;
	}

	public Object getValue() {
		for (Map.Entry<String, Object> node : data.entrySet()) {
			return node.getValue();
		}
		return null;
	}


	public Map<String, Object> getData() {
		return data;
	}

	public Boolean getBoolean(String... keys) {
		if (getObject(keys) == null) {
			return null;
		}
		return Boolean.valueOf(getObject(keys).toString());
	}

}
