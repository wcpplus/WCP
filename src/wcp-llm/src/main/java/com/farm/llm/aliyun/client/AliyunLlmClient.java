package com.farm.llm.aliyun.client;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.log4j.Logger;

import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.farm.llm.FarmEmbedding;
import com.farm.llm.FarmEmbeddings;
import com.farm.llm.FarmLlmInter;
import com.farm.llm.domain.LlmMessages;
import com.farm.llm.utils.FarmLlmTools;
import com.farm.llm.utils.LlmMessage;
import com.farm.llm.utils.LlmMessage.M_TYPE;
import com.google.gson.Gson;

/**
 * 接口模型實現類
 * 
 * @author macpl
 *
 */
public class AliyunLlmClient implements FarmLlmInter {
	private static final Logger log = Logger.getLogger(AliyunLlmClient.class);
	private String apiKey;
	private String embeddingModelKey;
	private String qaModelKey;
	private int maxCharNum;
	private int embeddingPassScore;

	/**
	 * @param apiKey
	 * @param embeddingModelKey  向量模型text-embedding-v1
	 * @param qaModelKey         对话模型qwen-plus
	 * @param maxCharNum         会话字符长度，最大3500
	 * @param embeddingPassScore 向量相关度的及格分数
	 * @return
	 */
	public static FarmLlmInter getInstance(String apiKey, String embeddingModelKey, String qaModelKey, int maxCharNum,
			int embeddingPassScore) {
		AliyunLlmClient client = new AliyunLlmClient();
		client.apiKey = apiKey;
		client.embeddingModelKey = embeddingModelKey;
		client.qaModelKey = qaModelKey;
		client.maxCharNum = maxCharNum;
		client.embeddingPassScore = embeddingPassScore;
		return client;
	}

	@Override
	public FarmEmbedding getEmbedding(String text) throws Exception {
		Thread.sleep(100);
		Constants.apiKey = apiKey;
		TextEmbeddingParam param = TextEmbeddingParam.builder().model(embeddingModelKey).texts(Arrays.asList(text))
				.build();
		TextEmbedding textEmbedding = new TextEmbedding();
		TextEmbeddingResult result = textEmbedding.call(param);
		List<Double> embedding = result.getOutput().getEmbeddings().get(0).getEmbedding();
		byte[] ebyte = FarmEmbeddings.doubleArrayToByteArray(FarmEmbeddings.convertListToDoubleArray(embedding));
		return new FarmEmbedding(getModelKey(), ebyte,
				FarmEmbeddings.toFloatArrays(FarmEmbeddings.convertListToDoubleArray(embedding)));
	}

	@Override
	public Integer getEmbeddingPassScore() {
		return embeddingPassScore;
	}
	
	
	@Override
	public String getModelKey() {
		return "ALIYUN";
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
	public void sendMsg(LlmMessages messages, BiConsumer<String, Boolean> msghandle, int hismsgRoundNum,
			int hisMsgUnitCharNum) {
		Constants.apiKey = apiKey;
		Generation gen = new Generation();
		// 获得格式化提示词（还有上下文）
		List<LlmMessage> talMsgs = FarmLlmTools.getMessageManagers(messages, maxCharNum, hismsgRoundNum,
				hisMsgUnitCharNum);
		MessageManager msgManager = new MessageManager(10);
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
			msgManager.add(Message.builder().role(role.getValue()).content(talMsg.getMessage()).build());
		}
		try {
			log.info("【" + "通义千问" + "|AI问答】-----" + getClass().getName() + "消息：\n" + new Gson().toJson(msgManager));
		} catch (Exception e) {
			//
		}
		QwenParam param = QwenParam.builder().model(qaModelKey).resultFormat(QwenParam.ResultFormat.MESSAGE)
				.messages(msgManager.get()).topP(0.8).incrementalOutput(true) // get streaming output
				.build();
		try {
			gen.streamCall(param, new ResultCallback<GenerationResult>() {
				@Override
				public void onEvent(GenerationResult message) {
					msghandle.accept(message.getOutput().getChoices().get(0).getMessage().getContent(), false);
				}

				@Override
				public void onError(Exception err) {
					err.printStackTrace();
					msghandle.accept("当前问题可能包含不适当的内容，请换一个问题", true);
				}

				@Override
				public void onComplete() {
					msghandle.accept("", true);
				}
			});
		} catch (ApiException | NoApiKeyException | InputRequiredException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String sendMsg(String message) {
		Constants.apiKey = apiKey;
		try {
			Conversation conversation = new Conversation();
			ConversationParam param = ConversationParam.builder().model(qaModelKey).prompt(message).build();
			ConversationResult result = conversation.call(param);
			return result.getOutput().getText();
		} catch (ApiException | NoApiKeyException | InputRequiredException e) {
			log.error("消息长度:" + message.length() + "|" + message, e);
			return ("当前问题可能包含不适当的内容，请换一个问题");
		}
	}

//	@Override
//	public String useQuestionTemplete(String userQuestionStr, String referenceKnows) {
//		String bk = "你好";
//		if (userQuestionStr != null) {
//			bk = "你扮演一个知识库管理员来回答用户提出的问题，知识库中存在的知识放在问题最后部分你可以参考这些知识或者不参考，现在你来直接回答用户的问题，问题是，【\"" + userQuestionStr
//					+ "\"】";
//		}
//		if (referenceKnows != null) {
//			bk = bk + ",作答要求：1.如果回答中参考了知识，则仅展示'参考知识ID'而且要放在回答的最后面，和回答之间空两行；2.只回答用户的问题不说其他无关内容； 以下是知识库中的部分知识："
//					+ referenceKnows;
//		}
//		return bk;
//	}

}
