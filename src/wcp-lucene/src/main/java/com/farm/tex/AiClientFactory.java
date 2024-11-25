package com.farm.tex;

import com.farm.llm.FarmLlmInter;
import com.farm.llm.aliyun.client.AliyunLlmClient;
import com.farm.llm.openai.client.ChatGptClient;
import com.farm.parameter.FarmParameterService;

/**
 * 通过配置来加载AI模型类型
 * 
 * @author Wd
 *
 */
public class AiClientFactory {

	public static FarmLlmInter getClient() {

		if (FarmParameterService.getInstance().getParameter("config.imbar.qa.model.type").equals("aliyun")) {
			return AliyunLlmClient.getInstance(
					FarmParameterService.getInstance().getParameter("config.ai.aliyun.secret"),
					FarmParameterService.getInstance().getParameter("config.ai.aliyun.model.em.key"),
					FarmParameterService.getInstance().getParameter("config.ai.aliyun.model.qa.key"),
					FarmParameterService.getInstance().getParameterInt("config.ai.aliyun.max.char.num"),
					FarmParameterService.getInstance().getParameterInt("config.ai.aliyun.model.em.pass.score")
					);
		}
		if (FarmParameterService.getInstance().getParameter("config.imbar.qa.model.type").equals("chatgpt3-5")) {
			return ChatGptClient.getInstance(
					   FarmParameterService.getInstance().getParameter("config.ai.chatgpt3.api.key"),
					   FarmParameterService.getInstance().getParameter("config.ai.chatgpt3.model.em.key"),
					   FarmParameterService.getInstance().getParameter("config.ai.chatgpt3.model.qa.key"),
					FarmParameterService.getInstance().getParameterInt("config.ai.chatgpt3.max.char.num"),
					   FarmParameterService.getInstance().getParameter("config.ai.chatgpt3.prox.ip"), 
					FarmParameterService.getInstance().getParameterInt("config.ai.chatgpt3.prox.port"), 
					   FarmParameterService.getInstance().getParameter("config.ai.chatgpt3.api.baseurl"));
		}
		throw new RuntimeException("the QA model type is not found："+  FarmParameterService.getInstance().getParameter("config.imbar.qa.model.type"));
	}

}
