package com.farm.tex;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.farm.llm.FarmEmbedding;
import com.farm.llm.FarmLlmInter;
import com.farm.parameter.FarmParameterService;
import com.farm.tex.domain.Shorttext;

/**
 * 文本语义向量生成器
 * 
 * @author Wd
 *
 */
public class EmbeddingStarter {
	private static final Logger log = Logger.getLogger(EmbeddingStarter.class);
	private static boolean isStarted = false;

	public static boolean isStarted() {
		return isStarted;
	}

	private static int cnum = 0;

	public static void start() {
		if (!isStarted) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					isStarted = true;
					try {
						Thread.sleep(2000);
						cnum = 0;
						while (true) {
							List<Shorttext> texts = ShortTextFactory.getInctance().getNoEmbeddingTexts(100);
							if (texts.size() <= 0) {
								break;
							} else {
								// 向量化
								for (Shorttext text : texts) {
									try {
										cnum++;
										// 创建 ExecutorService
										ExecutorService executor = Executors.newSingleThreadExecutor();
										// 调用方法并设置超时时间为1秒
										Future<Void> future = executor.submit(() -> {
											log.info("<开始提取" + FarmParameterService.getInstance()
													.getParameter("config.ai.index.model.type") + "模型...");
											if (FarmParameterService.getInstance()
													.getParameter("config.ai.index.model.type").equals("embedding")) {
												// 向量嵌入
												FarmLlmInter client = AiClientFactory.getClient();
												FarmEmbedding fed = client.getEmbedding(text.getText());
												ShortTextFactory.getInctance().editEmbedding(text.getId(),
														fed.getEmbeddingBytes(), fed.getEmbeddingFloats().length,
														fed.getModelkey());
											} else {
												// 使用全文检索
												ShortTextFactory.getInctance().editEmbedding(text.getId(), null,
														text.getLen(), "FULLTEXT");
											}
											log.info("完成提取模型>");
											return null;
										});
										// 等待方法执行完成，最多等待1秒
										future.get(10, TimeUnit.SECONDS);
										// 关闭 ExecutorService
										executor.shutdown();
									} catch (TimeoutException e) {
										log.error("提取向量超时", e);
										ShortTextFactory.getInctance().errorHandle(text.getId(), e.getMessage());
									} catch (Exception e) {
										log.error(e.getMessage(), e);
										ShortTextFactory.getInctance().errorHandle(text.getId(), e.getMessage());
									}
								}
							}
							Thread.sleep(1000);
						}
						if (!IndexingStarter.isStarted()) {
							IndexingStarter.start();
						}
					} catch (Exception e) {
						e.printStackTrace();

					} finally {
						isStarted = false;
					}
				}
			}).start();
		} else {
			throw new RuntimeException("模型生成中" + cnum + "...");
		}
	}

}
