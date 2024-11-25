package com.farm.llm.openai.client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.farm.llm.FarmEmbedding;
import com.farm.llm.FarmEmbeddings;
import com.farm.llm.FarmLlmInter;
import com.farm.llm.aliyun.client.AliyunLlmClient;
import com.farm.llm.domain.LlmMessages;
import com.farm.llm.domain.ReferenceMaterial;
import com.farm.llm.openai.client.util.OpenAiServer;
import com.farm.llm.utils.FarmLlmTools;
import com.farm.llm.utils.LlmMessage;
import com.farm.llm.utils.LlmMessage.M_TYPE;
import com.google.gson.Gson;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPT.ChatGPTBuilder;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.ChatGPTStream.ChatGPTStreamBuilder;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.AbstractStreamListener;

/**
 * 接口模型實現類
 * 
 * @author macpl
 *
 */
public class ChatGptClient implements FarmLlmInter {
	private static final Logger log = Logger.getLogger(ChatGptClient.class);
	private String apiKey;
	private String embeddingModelKey;
	private String qaModelKey;
	private int maxCharNum;

	private String baseUrl;
	private String prox_ip;
	private Integer prox_port;

	/**
	 * @param apiKey
	 * @param embeddingModelKey 向量模型text-embedding-v1
	 * @param qaModelKey        对话模型qwen-plus
	 * @param maxCharNum        会话字符长度，最大3500
	 * @return
	 */
	public static FarmLlmInter getInstance(String apiKey, String embeddingModelKey, String qaModelKey, int maxCharNum,
			String proxIp, Integer proxPort, String baseUrl) {
		ChatGptClient client = new ChatGptClient();
		client.apiKey = apiKey;
		client.embeddingModelKey = embeddingModelKey;
		client.qaModelKey = qaModelKey;
		client.maxCharNum = maxCharNum;
		client.prox_ip = proxIp;
		client.prox_port = proxPort;
		client.baseUrl = baseUrl;
		return client;
	}

	@Override
	public FarmEmbedding getEmbedding(String text) throws Exception {
		// "https://api.openai.com/"
		OpenAiServer server = new OpenAiServer(prox_ip, prox_port, baseUrl + "v1/embeddings", apiKey);
		List<Double> embedding = server.getEmbedding(embeddingModelKey, text);
		byte[] ebyte = FarmEmbeddings.doubleArrayToByteArray(FarmEmbeddings.convertListToDoubleArray(embedding));
		return new FarmEmbedding(getModelKey(), ebyte,
				FarmEmbeddings.toFloatArrays(FarmEmbeddings.convertListToDoubleArray(embedding)));
	}

	@Override
	public FarmEmbedding getEmbedding(byte[] bytes, int embeddingLen) {
		if (bytes == null) {
			return null;
		}
		double[] embedding = FarmEmbeddings.byteArrayToDoubleArray(bytes, embeddingLen);
		return new FarmEmbedding(getModelKey(), bytes, FarmEmbeddings.toFloatArrays(embedding));
	}

	@Override
	public String getModelKey() {
		return "OPENAI";
	}

	@Override
	public void sendMsg(LlmMessages messages, BiConsumer<String, Boolean> msghandle,int hismsgRoundNum,int hisMsgUnitCharNum) {
		// 不需要代理的话，注销此行
		ChatGPTStreamBuilder builder = ChatGPTStream.builder();
		if (prox_ip != null && prox_port != null) {
			builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(prox_ip, prox_port)));
		}
		ChatGPTStream chatGPTStream = builder.timeout(10 * 1000).apiKey(apiKey).apiHost(baseUrl).build().init();
		AbstractStreamListener listener = new AbstractStreamListener() {
			@Override
			public void onMsg(String message) {
				msghandle.accept(message, false);
			}

			@Override
			public void onError(Throwable throwable, String response) {
				if (throwable != null) {
					throwable.printStackTrace();
				}
				msghandle.accept("当前问题可能包含不适当的内容，请换一个问题", true);
				// System.exit(0);
			}
		};

		// 获得格式化提示词（还有上下文）
		List<LlmMessage> talMsgs = FarmLlmTools.getMessageManagers(messages, maxCharNum, hismsgRoundNum,hisMsgUnitCharNum);
		List<Message> msgs = new ArrayList<Message>();
		for (LlmMessage talMsg : talMsgs) {
			Role role = null;
			if (talMsg.getType().equals(M_TYPE.USER)) {
				role = Role.USER;
			}
			if (talMsg.getType().equals(M_TYPE.ASSISTANT)) {
				role = Role.ASSISTANT;
			}
			if (talMsg.getType().equals(M_TYPE.SYSTEM)) {
				role = Role.SYSTEM;
			}
			msgs.add(Message.builder().role(role.getValue()).content(talMsg.getMessage()).build());
		}
		try {
			log.info("【"+"ChatGPT"+"|AI问答】-----" + getClass().getName() + "消息：\n" + new Gson().toJson(msgs));
		} catch (Exception e) {
			//
		}
		ChatCompletion chatCompletion = ChatCompletion.builder().model(qaModelKey).messages(msgs).build();
		chatGPTStream.streamChatCompletion(chatCompletion, listener);
		listener.setOnComplate(new Consumer<String>() {
			@Override
			public void accept(String t) {
				msghandle.accept("", true);
				// System.exit(0);
			}
		});
	}

	@Override
	public String sendMsg(String msg) {
		// 国内需要代理 国外不需要
		ChatGPTBuilder builder = ChatGPT.builder();
		if (prox_ip != null && prox_port != null) {
			builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(prox_ip, prox_port)));
		}
		ChatGPT chatGPT = builder.apiKey(apiKey).timeout(10 * 1000).apiHost(baseUrl).build().init();
		Message message = Message.of(msg);
		ChatCompletion chatCompletion = ChatCompletion.builder().model(qaModelKey).messages(Arrays.asList(message))
				.build();
		ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
		Message res = response.getChoices().get(0).getMessage();
		return res.getContent();
	}

	@Override
	public Integer getEmbeddingPassScore() {
		return null;
	}

}
