package com.farm.tex;

import java.util.List;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.farm.llm.domain.LlmMessages;
import com.farm.llm.utils.LlmMessage;
import com.farm.parameter.FarmParameterService;
import com.farm.tex.domainex.AiQuestorMessage;
import com.farm.tex.domainex.EmbSearchResults;
import com.farm.tex.domainex.AiQuestorMessage.MessageState;
import com.farm.tex.index.ShortTextIndexFactory;
import com.farm.tex.index.ShortTextIndexInter;
import com.farm.util.cache.FarmCacheName;
import com.farm.util.cache.FarmCaches;
import com.farm.web.WebUtils;

/**
 * 向大语言模型提问
 * 
 * @author Wd
 *
 */
public class AiQuestor {

	private static final Logger log = Logger.getLogger(AiQuestor.class);

	/**
	 * 创建一个异步消息
	 * 
	 * @param id
	 * @return
	 */
	private static AiQuestorMessage creatMessage(String questMessage, String userkey) {
		AiQuestorMessage am = new AiQuestorMessage(userkey);
		am.setUserQuestion(questMessage);
		am.setMessage("");
		am.submit(MessageState.NEW);
		FarmCaches.getInstance().putCacheData(userkey, am, FarmCacheName.wcpAiUserTalking);
		return am;
	}

	/**
	 * 提问
	 * 
	 * @param message         问题文本
	 * @param talkSessionId   用户loginname或者用户IP
	 * @param userReadTypeIds 用户权限ids（一般为分类id）
	 * @return
	 */
	public static AiQuestorMessage send(String message, String talkSessionId, List<String> userReadTypeIds,
			List<LlmMessage> hismsg) {
		AiQuestorMessage backMessage = talking(talkSessionId);
		if (backMessage != null) {
			// 有消息正在进行中就什么都不做
			return backMessage;
		} else {
			AiQuestorMessage newMessage = backMessage = creatMessage(message, talkSessionId);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ShortTextIndexInter index = ShortTextIndexFactory.getInstance();
						EmbSearchResults result = index.search(newMessage);
						LlmMessages llmMsg = LlmMessages.getInstance(message, hismsg);
						newMessage.appendMessage("知识加载中...");
						// 获取参考资料
						llmMsg.setMaterials(
								ShortTextFactory.getInctance().getQuestionsByAi(llmMsg, result));
						newMessage.setMessage("");
						// 加载参考资料到用户消息中
						newMessage.setReference(llmMsg);
						// 发送到大语言模型
						AiClientFactory.getClient().sendMsg(llmMsg, new BiConsumer<String, Boolean>() {
							@Override
							public void accept(String msg, Boolean isComplete) {
								newMessage.appendMessage(msg);
								if (isComplete) {
									newMessage.submit(MessageState.COMPELET);
								} else {
									newMessage.submit(MessageState.LOADING);
								}
							}
						}, FarmParameterService.getInstance().getParameterInt("config.imbar.qa.model.his.round"),
								FarmParameterService.getInstance().getParameterInt("config.imbar.qa.model.his.maxlength"));
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						newMessage.setMessage("当前问题可能包含不适当的内容，请换一个问题");
						newMessage.submit(MessageState.COMPELET);
					}
				}
			}).start();
			return newMessage;
		}
	}

	public static AiQuestorMessage loadMessage(String id) {
		return AiQuestorMessage.load(id);
	}

	/**
	 * 获得当前会话，返回空表示该用户当前无会话
	 * 
	 * @param talkSessionId
	 * @return
	 */
	public static AiQuestorMessage talking(String talkSessionId) {
		return (AiQuestorMessage) FarmCaches.getInstance().getCacheData(talkSessionId, FarmCacheName.wcpAiUserTalking);
	}

	/**
	 * 獲得用戶的AI問答会话ID（区别是否同一人的会话，每人只能开启一个）
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	public static String getTalkSessionId(HttpSession session, HttpServletRequest request) {
		String talkSessionId = WebUtils.getCurrentUser(session) == null ? WebUtils.getCurrentIp(request)
				: WebUtils.getCurrentUser(session).getLoginname();
		return talkSessionId;
	}

}
