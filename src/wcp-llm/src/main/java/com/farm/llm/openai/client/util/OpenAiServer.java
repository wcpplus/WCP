package com.farm.llm.openai.client.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.farm.llm.utils.FarmJsonMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * GPT接口封装
 * 
 * @author macpl
 *
 */
public class OpenAiServer {
	private static OkHttpClient okHttpClient = null;

	private String apiKey;
	private String baseUrl;
	private String prox_ip;
	private Integer prox_port;

	public OpenAiServer(String proxIp, int proxPort, String baseurl, String apikey) {
		this.prox_ip = proxIp;
		this.prox_port = proxPort;
		this.baseUrl = baseurl;
		this.apiKey = apikey;
	}

	private static OkHttpClient getHttpClient(String prox_ip, Integer prox_port) {
		if (okHttpClient == null) {
			Builder builer = null;
			builer = new OkHttpClient.Builder();
			if (prox_ip != null && prox_port != null) {
				builer.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(prox_ip, prox_port)));
			}
			okHttpClient = builer.connectTimeout(10, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
					.readTimeout(120, TimeUnit.SECONDS).build();
		}
		return okHttpClient;
	}

	/**
	 * 生成向量
	 * 
	 * @param messagesJsonString
	 * @return
	 */
	public List<Double> getEmbedding(String modelkey, String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("model", modelkey);
		map.put("input", data);
		JSONObject json = new JSONObject(map);
		FarmJsonMap wjm = openAiPost(prox_ip, prox_port, baseUrl, apiKey, json.toString());

		if (wjm.getObject("error") != null) {
			throw new RuntimeException(wjm.toString());
		} else {
			List<FarmJsonMap> list = wjm.getList("data");
			if (list.size() > 0) {
				@SuppressWarnings("unchecked")
				List<Double> emb = (List<Double>) (list.get(0).getObject("embedding"));
				return emb;
			}
		}
		throw new RuntimeException(wjm.toString());
	}

	/**
	 * openAi通用提交
	 * 
	 * @param prox_ip
	 * @param prox_port
	 * @param apiBaseUrl
	 * @param apiKey
	 * @param jsonData
	 * @return
	 */
	public FarmJsonMap openAiPost(String prox_ip, int prox_port, String apiBaseUrl, String apiKey, String jsonData) {
		String backjson = "";
		try {
			@SuppressWarnings("deprecation")
			Request request = new Request.Builder().url(apiBaseUrl).header("Authorization", "Bearer " + apiKey)
					.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData)).build();
			Response response = getHttpClient(prox_ip, prox_port).newCall(request).execute();
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			backjson = (response.body().string());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		JSONObject result = new JSONObject(backjson);
		FarmJsonMap wjm = new FarmJsonMap(result.toMap());
		return wjm;
	}

}
