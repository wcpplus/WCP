package com.farm.llm;

import java.util.function.BiConsumer;

import com.farm.llm.domain.LlmMessages;

/**
 * 大语言知识库扩展接口
 * 
 * @author Wd
 *
 */
public interface FarmLlmInter {

	/**
	 * 获得向量
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public FarmEmbedding getEmbedding(String text) throws Exception;

	/**
	 * 获得向量
	 * 
	 * @param bytes
	 * @return
	 */
	public FarmEmbedding getEmbedding(byte[] bytes, int embeddingLen);

	/**
	 * 获得模型关键字
	 * 
	 * @return
	 */
	public String getModelKey();

	/**
	 * 发送消息(聊天对话)
	 * 
	 * @param message
	 * @param msghandle 回调函数（流式返回的回答，是否完成回答）
	 */
	public void sendMsg(LlmMessages message, BiConsumer<String, Boolean> msghandle, int hismsgRoundNum,int hisMsgUnitCharNum);

	/**
	 * 发送消息(直接返回)
	 * 
	 * @param message
	 * @return
	 */
	public String sendMsg(String message);

	/**
	 * 获得向量相关得分的及格分数（返回空表示無此參數）
	 * 
	 * @return
	 */
	public Integer getEmbeddingPassScore();

}
